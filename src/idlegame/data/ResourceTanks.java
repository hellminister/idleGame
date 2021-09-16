package idlegame.data;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ResourceTanks {
    private final Map<ResourceType, Resource> tanks;

    public ResourceTanks(Set<Resource> resources) {
        tanks = resources.stream().collect(Collectors.toMap(Resource::getType, Function.identity()));
    }

    public Resource getResource(ResourceType name){
        return tanks.get(name);
    }

    public String toString(){
        StringBuilder s = new StringBuilder("Tanks : \n");
        for (Resource r : tanks.values()){
            s.append("\t").append(r.toString()).append("\n");
        }
        return s.toString();
    }

    public Collection<Resource> getAllResources() {
        return tanks.values();
    }
}