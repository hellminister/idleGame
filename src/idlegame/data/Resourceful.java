package idlegame.data;

import java.math.BigDecimal;
import java.util.HashSet;
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

    public BigDecimal store(ResourceType type,  BigDecimal amount){
        System.out.println("Storing " + amount + " " + type.getName().get() + " to " + id);
        BigDecimal remains = tanks.getResource(type).store(amount);
        System.out.println("Remains " + remains + " " + type.getName().get());

        if (remains.compareTo(BigDecimal.ZERO) > 0 && otherLocation != null){
            remains = otherLocation.storeOnlyHere(type, remains);
        }

        return remains;
    }

    public BigDecimal storeOnlyHere(ResourceType type, BigDecimal amount){
        return tanks.getResource(type).store(amount);
    }

    public BigDecimal request(ResourceType type,  BigDecimal amount){
        BigDecimal obtained = tanks.getResource(type).request(amount);

        if (obtained.compareTo(amount) < 0 && otherLocation != null){
            obtained = otherLocation.requestOnlyHere(type, obtained);
        }

        return obtained;
    }

    public BigDecimal requestOnlyHere(ResourceType type, BigDecimal amount){
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