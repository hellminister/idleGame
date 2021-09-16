package idlegame.data;


import idlegame.util.BigDecimalProperty;
import idlegame.util.ReadOnlyBigDecimalProperty;
import javafx.beans.binding.ObjectBinding;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Producer {
    private final String name;

    private final Map<String, ProdResource> produced;
    private final Map<String, ProdResource> consumed;
    private final ResourceTanks storage;
    private final BigDecimalProperty productionRate;

    public Producer(String name, Set<Resource> produces, Set<Resource> consumes, ResourceTanks storage) {
        productionRate = new BigDecimalProperty(BigDecimal.valueOf(0.25));
        produced = produces.stream().collect(Collectors.toMap(Resource::getName, r -> new ProdResource(r,productionRate)));
        consumed = consumes.stream().collect(Collectors.toMap(Resource::getName, r -> new ProdResource(r,productionRate)));
        this.name = name;
        this.storage = storage;
    }

    public void generate(){
        // first lets empty the previous production if any is left
        produced.values().forEach(r -> transfer(r, storage.getResource(r.getType()), r.getAmount().getValue()));

        // second lets fill the producer
        consumed.values().forEach(r -> transfer(storage.getResource(r.getType()), r, r.actualProduction()));

        // last, if the producer is at least at production rate capacity, and the produced resources are not all full, generate the resources, and empty the producers tanks by
        // the production rate
        // using toggles i could add different producing behaviors
        if (isAllAtLeast(consumed) && !isAllFull(produced)){
            produced.values().forEach(r -> r.store(r.actualProduction()));
            consumed.values().forEach((r -> r.request(r.actualProduction())));
        }
    }


    private boolean isAllEmpty(Map<String, ProdResource> tanks){
        return tanks.values().stream().allMatch(ProdResource::isEmpty);
    }

    private boolean isAllFull(Map<String, ProdResource> tanks){
        return tanks.values().stream().allMatch(ProdResource::isAtMaxCapacity);
    }

    private boolean isAllAtLeast(Map<String, ProdResource> tanks) {
        return tanks.values().stream().allMatch(r -> r.isAtLeastOfCapacity(productionRate.getValue()));
    }

    private void transfer(Resource from, Resource to, BigDecimal amount){
        BigDecimal took = from.request(amount);
        took = to.store(took);
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


    public static void main(String[] args) {
        ResourceTanks tanks = new ResourceTanks(Set.of(
                new Resource(ResourceType.get("Iron"), BigDecimal.valueOf(1000)),
                new Resource(ResourceType.get("Energy"), BigDecimal.valueOf(1000)),
                new Resource(ResourceType.get("Air"), BigDecimal.valueOf(1000)),
                new Resource(ResourceType.get("Coal"), BigDecimal.valueOf(1000))
        ));

        List<Producer> producers = List.of(
                new Producer("Coal mine",
                        Set.of(new Resource(ResourceType.get("Coal"), BigDecimal.valueOf(10))),
                        Set.of(),
                        tanks),
                new Producer("Air pump",
                        Set.of(new Resource(ResourceType.get("Air"), BigDecimal.valueOf(100))),
                        Set.of(),
                        tanks),
                new Producer("Coal power plant",
                        Set.of(new Resource(ResourceType.get("Energy"), BigDecimal.valueOf(40))),
                        Set.of(new Resource(ResourceType.get("Coal"), BigDecimal.valueOf(100)),
                               new Resource(ResourceType.get("Air"), BigDecimal.valueOf(150))),
                        tanks)
        );


        for (int i = 0; i < 100; i++) {
            long timeA = System.nanoTime();
            producers.forEach(Producer::generate);
            long timeB = System.nanoTime();


            System.out.println("-------------Epoch " + i + " " + (timeB-timeA) + " n1s ----------------------");
            System.out.println(tanks);
            producers.forEach(p -> System.out.println(p.toString()));
            System.out.println("--------------------------------------------------");

        }


    }

    private static class ProdResource extends Resource{

        private final ProductionBinding actualProduction;

        public ProdResource(ResourceType type, BigDecimal maxCapacity, ReadOnlyBigDecimalProperty prodRate) {
            super(type, maxCapacity);
            actualProduction = new ProductionBinding(this.getMaxCapacity(), prodRate);
        }

        private ProdResource(Resource r, BigDecimalProperty productionRate) {
            this(r.getType(), r.maxCapacity.getValue(), productionRate);
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