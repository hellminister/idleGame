package idlegame.data;

import idlegame.data.dataloader.shiploader.ShipLoader;

import java.nio.file.Path;
import java.nio.file.Paths;

public class GameData{
    private static final Path dataPath = Paths.get("resources/data/");

    private Ship myShip;


    public GameData(){
        Location.loadLocations(dataPath);
        myShip = ShipLoader.loadShip(dataPath).orElseThrow();

    }

    public Ship getMyShip(){
        return myShip;
    }
}