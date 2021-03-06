package idlegame.data;


import idlegame.ui.gamescreen.producerscreen.ProducerUI;
import idlegame.language.Localize;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.binding.ObjectExpression;
import javafx.beans.property.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Producer implements Prioritable {
    private static final Map<ReadOnlyStringProperty, Producer> allProducers;

    static {
        allProducers = new HashMap<>();
    }

    private static void addProducer(Producer producer){
        allProducers.put(producer.getName(), producer);
    }

    public static Producer getProducer(ReadOnlyStringProperty name){
        return allProducers.get(name);
    }

    public static Producer getProducer(String nameIdTag){
        return allProducers.get(Localize.get(nameIdTag));
    }


    private final ReadOnlyStringProperty name;
    private final ReadOnlyStringProperty description;

    private final Map<ResourceType, ProdResource> produced;
    private final Map<ResourceType, ProdResource> consumed;
    private Resourceful storage;
    private final DoubleProperty productionRate;
    private final BooleanProperty unlocked;
    private ProducerUI ui = null;
    private final Map<ResourceType, DoubleExpression> needs;

    public Producer(String name, String description, Set<String> produces, Set<String> consumes) {
        productionRate = new SimpleDoubleProperty(1.0);
        produced = produces.stream().map(s -> new ProdResource(s, productionRate, false, true, true)).collect(Collectors.toMap(Resource::getType, Function.identity()));
        consumed = consumes.stream().map(s -> new ProdResource(s, productionRate, true, false, true)).collect(Collectors.toMap(Resource::getType, Function.identity()));

        this.name = Localize.get(name);
        this.description = Localize.get(description);
        unlocked = new SimpleBooleanProperty(true);

        Map<ResourceType, DoubleExpression> needsTemp = new HashMap<>();

        consumed.values().forEach(r -> needsTemp.put(r.getType(), new ResourceNeed(r)));

        needs = Collections.unmodifiableMap(needsTemp);

        addProducer(this);
    }

    public void setStorage(Resourceful storage){
        this.storage = storage;
    }


    @Override
    public void preRun() {
        produced.values().forEach(r -> transfer(r, storage, r.getAmount().getValue()));
    }

    @Override
    public Map<ResourceType, DoubleExpression> getNeeds() {
        return needs;
    }

    @Override
    public Resourceful getResourceful() {
        return storage;
    }

    @Override
    public void receive(Map<ResourceType, Double> obtained) {
        // second lets fill the producer
        consumed.values().forEach(r -> r.store(obtained.getOrDefault(r.getType(), 0d)));

        // last, if the producer is at least at production rate capacity, and the produced resources are not all full, generate the resources, and empty the producers tanks by
        // the production rate
        // using toggles i could add different producing behaviors
        if (isAllAtLeast(consumed) && !isAllFull(produced)){
            produced.values().forEach(r -> r.store(r.getActualProduction()));
            consumed.values().forEach((r -> r.request(r.getActualProduction())));
        }
    }

    private boolean isAllEmpty(Map<ResourceType, ProdResource> tanks){
        return tanks.values().stream().allMatch(ProdResource::isEmpty);
    }

    private boolean isAllFull(Map<ResourceType, ProdResource> tanks){
        return tanks.values().stream().allMatch(ProdResource::isAtMaxCapacity);
    }

    private boolean isAllAtLeast(Map<ResourceType, ProdResource> tanks) {
        return tanks.values().stream().allMatch(r -> r.isAtLeastOfCapacity(productionRate.getValue()));
    }

    // from locale to producer
    private void transfer(Resourceful from, Resource to, double amount){
        double took = from.request(to.getType(), amount);
        took = to.store(took);
        from.store(to.getType(), took);  // puts back what cannot be stored
    }

    // from producer to locale
    private void transfer(Resource from, Resourceful to, double amount){
        double took = from.request(amount);
        took = to.store(from.getType(), took);
        from.store(took);  // puts back what cannot be stored
    }

    public String toString(){
        StringBuilder s = new StringBuilder(name + "\n" + "Produced:\n");

        for (Resource r : produced.values()){
            s.append("\t").append(r.toString()).append("\n");
        }

        s.append("Consumed:\n");
        for (Resource r : consumed.values()){
            s.append("\t").append(r.toString()).append("\n");
        }

        return s.toString();
    }

    public ReadOnlyBooleanProperty unlockedProperty() {
        return unlocked;
    }

    public ReadOnlyStringProperty getName() {
        return name;
    }

    public ReadOnlyStringProperty getDescription() {
        return description;
    }

    public Map<ResourceType, ProdResource> getProduced() {
        return produced;
    }

    public Map<ResourceType, ProdResource> getConsumed() {
        return consumed;
    }

    public DoubleProperty getProductionRate() {
        return productionRate;
    }

    public ProducerUI getProducerUI() {
        if (ui == null){
            ui = new ProducerUI(this);
        }
        return ui;
    }



    public static class ProdResource extends Resource{
        private final ProductionBinding actualProduction;
        private final FillProductionBinding productionFilledRatio;

        public ProdResource(String resourceString, ReadOnlyDoubleProperty prodRate, boolean countStore, boolean countRequest, boolean invert) {
            super(resourceString);
            this.countRequest = countRequest;
            this.countStore = countStore;
            this.invert = invert;
            actualProduction = new ProductionBinding(this.getMaxCapacity(), prodRate);
            productionFilledRatio = new FillProductionBinding(actualProduction, getAmount());
        }


        public double getActualProduction(){
            return actualProduction.getValue();
        }
        public double getProductionFilledRatio(){ return productionFilledRatio.getValue();}

        public DoubleBinding actualProductionProperty() {
            return actualProduction;
        }

        public DoubleBinding productionFilledRatioProperty() {
            return productionFilledRatio;
        }
    }

    private static class ProductionBinding extends DoubleBinding {
        private final ReadOnlyDoubleProperty value;
        private final ReadOnlyDoubleProperty mod;

        public ProductionBinding(ReadOnlyDoubleProperty value, ReadOnlyDoubleProperty mod) {
            this.value = value;
            this.mod = mod;

            bind(value, mod);
        }


        /**
         * Calculates the current value of this binding.
         * <p>
         * Classes extending {@code ObjectBinding} have to provide an implementation
         * of {@code computeValue}.
         *
         * @return the current value
         */
        @Override
        protected double computeValue() {
            return value.getValue() * mod.getValue();
        }
    }

    private static class FillProductionBinding extends DoubleBinding {
        private final ProductionBinding max;
        private final ReadOnlyDoubleProperty amount;

        public FillProductionBinding(ProductionBinding max, ReadOnlyDoubleProperty amount) {
            this.max = max;
            this.amount = amount;

            bind(max, amount);
        }


        /**
         * Calculates the current value of this binding.
         * <p>
         * Classes extending {@code ObjectBinding} have to provide an implementation
         * of {@code computeValue}.
         *
         * @return the current value
         */
        @Override
        protected double computeValue() {
            return amount.getValue() / max.getValue();
        }
    }

    private static class ResourceNeed extends DoubleBinding {

        private final DoubleBinding actualProductionP;
        private final ReadOnlyDoubleProperty effectiveMaxCapacity;
        private final ReadOnlyDoubleProperty amount;


        public ResourceNeed(ProdResource resource){
            actualProductionP = resource.actualProductionProperty();
            effectiveMaxCapacity = resource.getEffectiveMaxCapacity();
            amount = resource.getAmount();

            bind(actualProductionP, effectiveMaxCapacity, amount);
        }


        /**
         * Calculates the current value of this binding.
         * <p>
         * Classes extending {@code ObjectBinding} have to provide an implementation
         * of {@code computeValue}.
         *
         * @return the current value
         */
        @Override
        protected double computeValue() {
            return effectiveMaxCapacity.getValue() - Double.min(amount.getValue(), actualProductionP.getValue());
        }
    }


}