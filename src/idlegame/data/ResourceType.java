package idlegame.data;

import idlegame.util.BigDecimalProperty;
import idlegame.util.ReadOnlyBigDecimalProperty;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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


    private final String name;
    private final BigDecimal initWeightPerUnit;
    private final BigDecimalProperty weightPerUnit;


    private ResourceType(String name, BigDecimal weightPerUnit) {
        this.name = name;
        initWeightPerUnit = weightPerUnit;
        this.weightPerUnit = new BigDecimalProperty(weightPerUnit);
    }


    public String getName() {
        return name;
    }

    public ReadOnlyBigDecimalProperty getWeight(){
        return weightPerUnit;
    }

}