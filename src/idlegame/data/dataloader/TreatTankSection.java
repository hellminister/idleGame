package idlegame.data.dataloader;

import idlegame.data.Resource;
import idlegame.data.ResourceTanks;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class TreatTankSection extends TreatSection<ResourceTanks> {
    public TreatTankSection(String previous) {
        super("_tank", previous);
    }

    @Override
    protected ResourceTanks treatImpl(Set<String> tags, BufferedReader br) throws IOException {
        String line = br.readLine();
        Set<Resource> resources = new HashSet<>();

        while (line != null && !endSectionTag.equals(line.trim())){
            if (!line.isBlank()) {
                resources.add(new Resource(line));
            }

            line = br.readLine();
        }

        if (line == null || !endSectionTag.equals(line.trim())) {
            throw new IllegalStateException("Section _tank has unexpected structure");
        }

        return new ResourceTanks(resources);
    }
}