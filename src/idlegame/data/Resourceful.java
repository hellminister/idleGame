package idlegame.data;

import idlegame.gamescreen.producerscreen.ProducerScreen;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public abstract class Resourceful {
    protected final ResourceTanks tanks;
    protected Resourceful otherLocation = null;
    protected final Set<Producer> producers;

    protected Resourceful(ResourceTanks tanks) {
        this.tanks = tanks;
        producers = new HashSet<>();
    }

    public void addAllProducers(Set<Producer> producersS){
        producers.addAll(producersS);
    }

    public Resource getResource(ResourceType type){
        return tanks.getResource(type);
    }

    public BigDecimal store(ResourceType type,  BigDecimal amount){
        BigDecimal remains = tanks.getResource(type).store(amount);

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


}