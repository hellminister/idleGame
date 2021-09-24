package idlegame.data;

/**
 * this class is the player character whether he is spaceship, sea ship, ether ship, etc.
 */
public class Ship extends Resourceful{

    public Ship(ResourceTanks tanks) {
        super(tanks, "Ship");
    }

    @Override
    public void setLocation(Resourceful location) {
        if (otherLocation != null){
            otherLocation.setLocation(null);
        }
        otherLocation = location;
        otherLocation.setLocation(this);
    }

    public Location getLocation(){
        return (Location)otherLocation;
    }


}