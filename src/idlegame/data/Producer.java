package idlegame.data;


import idlegame.language.Localize;
import idlegame.util.property.BigDecimalProperty;
import idlegame.util.property.ReadOnlyBigDecimalProperty;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Producer {
    private final ReadOnlyStringProperty name;
    private final ReadOnlyStringProperty description;

    private final Map<ResourceType, ProdResource> produced;
    private final Map<ResourceType, ProdResource> consumed;
    private final Resourceful storage;
    private final BigDecimalProperty productionRate;
    private final BooleanProperty unlocked;

    public Producer(String name, String description, Set<Resource> produces, Set<Resource> consumes, Resourceful storage) {
        productionRate = new BigDecimalProperty(BigDecimal.valueOf(1.0));
        produced = produces.stream().collect(Collectors.toMap(Resource::getType, r -> new ProdResource(r,productionRate, false, true, true)));
        consumed = consumes.stream().collect(Collectors.toMap(Resource::getType, r -> new ProdResource(r,productionRate, true, false, true)));
        this.name = Localize.get(name);
        this.description = Localize.get(description);
        this.storage = storage;
        unlocked = new SimpleBooleanProperty(true);
    }

    public void generate(){
        // first lets empty the previous production if any is left
        produced.values().forEach(r -> transfer(r, storage, r.getAmount().getValue()));

        // second lets fill the producer
        consumed.values().forEach(r -> transfer(storage, r, r.actualProduction()));

        // last, if the producer is at least at production rate capacity, and the produced resources are not all full, generate the resources, and empty the producers tanks by
        // the production rate
        // using toggles i could add different producing behaviors
        if (isAllAtLeast(consumed) && !isAllFull(produced)){
            produced.values().forEach(r -> r.store(r.actualProduction()));
            consumed.values().forEach((r -> r.request(r.actualProduction())));
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
    private void transfer(Resourceful from, Resource to, BigDecimal amount){
        BigDecimal took = from.request(to.getType(), amount);
        took = to.store(took);
        from.store(to.getType(), took);  // puts back what cannot be stored
    }

    // from producer to locale
    private void transfer(Resource from, Resourceful to, BigDecimal amount){
        BigDecimal took = from.request(amount);
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

    public BigDecimalProperty getProductionRate() {
        return productionRate;
    }

    public static class ProdResource extends Resource{

        private final ProductionBinding actualProduction;

        public ProdResource(ResourceType type, BigDecimal maxCapacity, ReadOnlyBigDecimalProperty prodRate, boolean countStore, boolean countRequest, boolean invert) {
            super(type, maxCapacity);
            this.countRequest = countRequest;
            this.countStore = countStore;
            this.invert = invert;
            actualProduction = new ProductionBinding(this.getMaxCapacity(), prodRate);
        }

        private ProdResource(Resource r, BigDecimalProperty productionRate, boolean countStore, boolean countRequest, boolean invert) {
            this(r.getType(), r.maxCapacity.getValue(), productionRate, countStore, countRequest, invert);
        }


        public BigDecimal actualProduction(){
            return actualProduction.getValue();
        }
    }

    private static class ProductionBinding extends ObjectBinding<BigDecimal> {
        private final ReadOnlyBigDecimalProperty value;
        private final ReadOnlyBigDecimalProperty mod;

        public ProductionBinding(ReadOnlyBigDecimalProperty value, ReadOnlyBigDecimalProperty mod) {
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
        protected BigDecimal computeValue() {
            return value.getValue().multiply(mod.getValue());
        }
    }


}