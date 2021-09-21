package idlegame.data.dataloader.shiploader;

import idlegame.data.Location;
import idlegame.data.dataloader.TreatSection;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Set;

public class TreatShipStartInSection extends TreatSection<Location> {
    public TreatShipStartInSection() {
        super("_startIn", "_tank");
    }

    @Override
    protected Location treatImpl(Set<String> tags, BufferedReader br) throws IOException {
        String locationId = br.readLine();
        String endTag = br.readLine();

        if (!endSectionTag.equals(endTag.trim())){
            throw new IllegalStateException("Section _startIn has unexpected structure");
        }

        Location location = Location.getLocation(locationId);

        if (location == null){
            throw new IllegalStateException(locationId + " is not a valid id, no location with such id found");
        }

        return location;
    }
}