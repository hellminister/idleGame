package idlegame.data;

import idlegame.language.Localize;
import idlegame.util.property.BigDecimalProperty;
import idlegame.util.property.ReadOnlyBigDecimalProperty;
import javafx.beans.property.ReadOnlyStringProperty;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ResourceType {

    private static final Map<String, ResourceType> resourceTypes;
    private static final String filePath = "resources/data/ResourceTypes.txt";

    static {
        var resourceTypesTemp = new HashMap<String, ResourceType>();
        Path toData = Paths.get(filePath);

        try (BufferedReader br = Files.newBufferedReader(toData)){
            String line = br.readLine();
            while (line != null){
                String[] info = line.split(" ");

                ResourceType rt = new ResourceType(info[0], new BigDecimal(info[1]));
                resourceTypesTemp.put(info[0], rt);

                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        resourceTypes = Collections.unmodifiableMap(resourceTypesTemp);
    }

    public static ResourceType get(String name){
        return resourceTypes.get(name);
    }


    private final String id;
    private final ReadOnlyStringProperty name;
    private final BigDecimal initWeightPerUnit;
    private final BigDecimalProperty weightPerUnit;

    private final Set<Resource> tanks;


    private ResourceType(String id, BigDecimal weightPerUnit) {
        this.id = id;
        this.name = Localize.get(id);
        initWeightPerUnit = weightPerUnit;
        this.weightPerUnit = new BigDecimalProperty(weightPerUnit);
        tanks = new HashSet<>();
    }

    public static Map<String, ResourceType> getAll() {
        return resourceTypes;
    }

    public void register(Resource tank){
        tanks.add(tank);
    }

    public ReadOnlyStringProperty getName() {
        return name;
    }

    public ReadOnlyBigDecimalProperty getWeight(){
        return weightPerUnit;
    }

    public Set<Resource> getTanks() {
        return tanks;
    }
}