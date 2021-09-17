package idlegame.data;

import java.math.BigDecimal;
import java.util.Set;

public class GameData {
    private Ship myShip;

    // statically defined for test purposed
    public GameData(){
        myShip = new Ship(new ResourceTanks(Set.of(
                new Resource(ResourceType.get("Iron"), BigDecimal.valueOf(10000)),
                new Resource(ResourceType.get("Energy"), BigDecimal.valueOf(10000)),
                new Resource(ResourceType.get("Air"), BigDecimal.valueOf(10000)),
                new Resource(ResourceType.get("Coal"), BigDecimal.valueOf(10000)),
                new Resource(ResourceType.get("Steel"), BigDecimal.valueOf(10000))
        )));

        myShip.addAllProducers(Set.of(
                new Producer("Coal mine",
                        "Best way to get coal.",
                        Set.of(new Resource(ResourceType.get("Coal"), BigDecimal.valueOf(10))),
                        Set.of(),
                        myShip),
                new Producer("Air pump",
                        "Let's pump air.",
                        Set.of(new Resource(ResourceType.get("Air"), BigDecimal.valueOf(100))),
                        Set.of(),
                        myShip),
                new Producer("Coal power plant",
                        "Time to produce electricity!",
                        Set.of(new Resource(ResourceType.get("Energy"), BigDecimal.valueOf(40))),
                        Set.of(new Resource(ResourceType.get("Coal"), BigDecimal.valueOf(100)),
                                new Resource(ResourceType.get("Air"), BigDecimal.valueOf(150))),
                        myShip),
                new Producer("Iron Mine",
                        "Let's dig in the soil. Some coal can also be found.",
                        Set.of(new Resource(ResourceType.get("Iron"), BigDecimal.valueOf(10)),
                                new Resource(ResourceType.get("Coal"), BigDecimal.valueOf(1))),
                        Set.of(new Resource(ResourceType.get("Energy"), BigDecimal.valueOf(500))),
                        myShip),
                new Producer("Steel Factory",
                        "Now time to transform some of those materials. Now to see how the wrapping works and how many words can be inserted in the description",
                        Set.of(new Resource(ResourceType.get("Steel"), BigDecimal.valueOf(100))),
                        Set.of(new Resource(ResourceType.get("Coal"), BigDecimal.valueOf(100)),
                                new Resource(ResourceType.get("Air"), BigDecimal.valueOf(200)),
                                new Resource(ResourceType.get("Iron"), BigDecimal.valueOf(100)),
                                new Resource(ResourceType.get("Energy"), BigDecimal.valueOf(500))),
                        myShip)));
    }

    public Ship getMyShip(){
        return myShip;
    }
}