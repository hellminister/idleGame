package idlegame.data.dataloader.locationloader;

import idlegame.data.dataloader.TreatSection;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Set;

public class TreatLocationInfoSection extends TreatSection<LocationLoader.LocationInfo> {
    public TreatLocationInfoSection() {
        super("_info", null);
    }

    @Override
    protected LocationLoader.LocationInfo treatImpl(Set<String> tags, BufferedReader br) throws IOException {

        String name = br.readLine();
        String description = br.readLine();
        String endTag = br.readLine();

        if (!endSectionTag.equals(endTag.trim())){
            throw new IllegalStateException("Section _info has unexpected structure");
        }

        return new LocationLoader.LocationInfo(name, description);
    }
}