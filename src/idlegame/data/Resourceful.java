package idlegame.data;

import javafx.beans.binding.DoubleExpression;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class Resourceful {
    protected final ResourceTanks tanks;
    protected Resourceful otherLocation = null;
    protected final Set<Producer> producers;
    protected final String id;

    protected Resourceful(ResourceTanks tanks, String id) {
        this.tanks = tanks;
        producers = new HashSet<>();
        this.id = id;
    }

    public void addAllProducers(Set<Producer> producersS){
        producersS.forEach(p -> {
            p.setStorage(this);
            producers.add(p);
        });
    }

    public Resource getResource(ResourceType type){
        return tanks.getResource(type);
    }

    public double store(ResourceType type,  double amount){
        double remains = tanks.getResource(type).store(amount);

        if (remains > 0 && otherLocation != null){
            remains = otherLocation.storeOnlyHere(type, remains);
        }

        return remains;
    }

    public double storeOnlyHere(ResourceType type, double amount){
        return tanks.getResource(type).store(amount);
    }

    public double request(ResourceType type,  double amount){
        double obtained = tanks.getResource(type).request(amount);

        if (obtained < (amount) && otherLocation != null){
            double missing = amount - (obtained);
            obtained = obtained + (otherLocation.requestOnlyHere(type, missing));
        }

        return obtained;
    }

    public Map<ResourceType, Double> request(Map<ResourceType, ? extends DoubleExpression> requires) {
        Map<ResourceType, Double> obtained = new HashMap<>();

        requires.forEach((key, value) -> obtained.compute(key, (k, v) -> request(k, value.getValue())));

        return obtained;
    }

    public double requestOnlyHere(ResourceType type, double amount){
        return tanks.getResource(type).request(amount);
    }

    public ResourceTanks getTanks(){
        return tanks;
    }

    public Set<Producer> getAllProducers() {
        return producers;
    }


    public abstract void setLocation(Resourceful location);

    public Resourceful getOtherLocation() {
        return otherLocation;
    }

    public String getId(){
        return id;
    }


}