package idlegame.data;

/**
 * this class is the player character whether he is spaceship, sea ship, ether ship, etc.
 */
public class Ship {
    private final ResourceTanks tanks;

    private Location currentlyAt;

    public Ship(ResourceTanks tanks) {
        this.tanks = tanks;
    }

    public ResourceTanks getTanks(){
        return tanks;
    }
}