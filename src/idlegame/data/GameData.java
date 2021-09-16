package idlegame.data;

import java.math.BigDecimal;
import java.util.Set;

public class GameData {
    private Ship myShip;

    // statically defined for test purposed
    public GameData(){
        myShip = new Ship(new ResourceTanks(Set.of(
                new Resource(ResourceType.get("Iron"), BigDecimal.valueOf(1000)),
                new Resource(ResourceType.get("Energy"), BigDecimal.valueOf(1000)),
                new Resource(ResourceType.get("Air"), BigDecimal.valueOf(1000)),
                new Resource(ResourceType.get("Coal"), BigDecimal.valueOf(1000)),
                new Resource(ResourceType.get("Steel"), BigDecimal.valueOf(1000))
        )));
    }

    public Ship getMyShip(){
        return myShip;
    }
}