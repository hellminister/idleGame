package idlegame.data;

import idlegame.data.dataloader.shiploader.ShipLoader;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

public class GameData{
    private static final Path dataPath = Paths.get("resources/data/");

    private Ship myShip;


    public GameData(){
        Location.loadLocations(dataPath);
        myShip = ShipLoader.loadShip(dataPath).orElseThrow();

    }



    // statically defined for test purposed
 /*   public GameData(){
        myShip = new Ship(new ResourceTanks(Set.of(
                new Resource(ResourceType.get("idt_R1"), BigDecimal.valueOf(10000), ""),
                new Resource(ResourceType.get("idt_R2"), BigDecimal.valueOf(10000), ""),
                new Resource(ResourceType.get("idt_R3"), BigDecimal.valueOf(10000), ""),
                new Resource(ResourceType.get("idt_R4"), BigDecimal.valueOf(10000), ""),
                new Resource(ResourceType.get("idt_R5"), BigDecimal.valueOf(10000), "")
        )));

        myShip.addAllProducers(Set.of(
                new Producer("idt_EARTH_PRODUCER_1_NAME",
                        "idt_EARTH_PRODUCER_1_DESCRIPTION",
                        Set.of(new Resource(ResourceType.get("idt_R4"), BigDecimal.valueOf(10), "")),
                        Set.of()),
                new Producer("idt_EARTH_PRODUCER_2_NAME",
                        "idt_EARTH_PRODUCER_2_DESCRIPTION",
                        Set.of(new Resource(ResourceType.get("idt_R3"), BigDecimal.valueOf(100), "")),
                        Set.of()),
                new Producer("idt_EARTH_PRODUCER_3_NAME",
                        "idt_EARTH_PRODUCER_3_DESCRIPTION",
                        Set.of(new Resource(ResourceType.get("idt_R2"), BigDecimal.valueOf(40), "")),
                        Set.of(new Resource(ResourceType.get("idt_R4"), BigDecimal.valueOf(100), ""),
                                new Resource(ResourceType.get("idt_R3"), BigDecimal.valueOf(150), ""))),
                new Producer("idt_EARTH_PRODUCER_4_NAME",
                        "idt_EARTH_PRODUCER_4_DESCRIPTION",
                        Set.of(new Resource(ResourceType.get("idt_R1"), BigDecimal.valueOf(10), ""),
                                new Resource(ResourceType.get("idt_R4"), BigDecimal.valueOf(1), "")),
                        Set.of(new Resource(ResourceType.get("idt_R2"), BigDecimal.valueOf(500), ""))),
                new Producer("idt_EARTH_PRODUCER_5_NAME",
                        "idt_EARTH_PRODUCER_5_DESCRIPTION",
                        Set.of(new Resource(ResourceType.get("idt_R5"), BigDecimal.valueOf(100), "")),
                        Set.of(new Resource(ResourceType.get("idt_R4"), BigDecimal.valueOf(100), ""),
                                new Resource(ResourceType.get("idt_R3"), BigDecimal.valueOf(200), ""),
                                new Resource(ResourceType.get("idt_R1"), BigDecimal.valueOf(100), ""),
                                new Resource(ResourceType.get("idt_R2"), BigDecimal.valueOf(500), "")))));
    }
*/
    public Ship getMyShip(){
        return myShip;
    }
}