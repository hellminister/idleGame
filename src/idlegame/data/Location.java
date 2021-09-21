package idlegame.data;

import idlegame.data.dataloader.locationloader.LocationLoader;
import idlegame.language.Localize;
import javafx.beans.property.ReadOnlyStringProperty;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * this class represent a local the player can visit, whether a planet, a city, a dimension
 */
public class Location extends Resourceful{
    private static final Logger LOG = Logger.getLogger(Location.class.getName());

    private static Map<ReadOnlyStringProperty, Location> allLocation;

    public static void loadLocations(Path dataPath) {
        allLocation = new HashMap<>();

        Path locationsFolder = dataPath.resolve("locations");

        try (Stream<Path> walk = Files.walk(locationsFolder)) {
            walk.filter(Files::isRegularFile).forEach(path -> {
                Optional<Location> location = LocationLoader.loadLocation(path);
                location.ifPresent(value -> allLocation.put(value.name, value));
            });
        } catch (IOException e) {
            LOG.severe(() -> "Error treating data folder : " + locationsFolder);
        }
    }

    public static Location getLocation(ReadOnlyStringProperty name){
        return allLocation.get(name);
    }

    public static Location getLocation(String idName){
        return allLocation.get(Localize.get(idName));
    }

    private final ReadOnlyStringProperty name;
    private final ReadOnlyStringProperty description;

    public Location(ResourceTanks tanks, String name, String description) {
        super(tanks, name);
        this.name = Localize.get(name);
        this.description = Localize.get(description);
    }


    @Override
    public void setLocation(Resourceful location) {
        otherLocation = location;
    }
}