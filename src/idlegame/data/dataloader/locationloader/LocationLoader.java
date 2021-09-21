package idlegame.data.dataloader.locationloader;

import idlegame.data.Location;
import idlegame.data.Producer;
import idlegame.data.ResourceTanks;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

public class LocationLoader {
    private static final Logger LOG = Logger.getLogger(LocationLoader.class.getName());
    private static final Set<String> tags = Set.of("_info", "_tank", "_producer", "_unlock", "_end");
    private static final LocationTreatSections treaters = new LocationTreatSections();

    public static Optional<Location> loadLocation(Path path){

        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line = br.readLine();

            Set<String> fileTags = new HashSet<>(tags);
            Location location = null;
            LocationInfo info = null;


            while(line != null){
                if (!line.isBlank() && !line.startsWith("#")){
                    switch (line) {
                        case "_info" -> info = treaters.locationInfoSection.treat(fileTags, br);
                        case "_tank" -> {
                            ResourceTanks tanks = treaters.locationTankSection.treat(fileTags, br);
                            assert info != null;
                            location = new Location(tanks, info.name, info.description);
                        }
                        case "_producer" -> {
                            Set<Producer> producers = treaters.locationProducerSection.treat(fileTags, br);
                            assert location != null;
                            location.addAllProducers(producers);
                        }
                        case "_unlock" -> treaters.locationUnlockSection.treat(fileTags, br);
                        case "_end" -> treaters.locationEndSection.treat(fileTags, br);
                        default -> {
                            String finalLine = line;
                            LOG.warning(() -> "Untreated : " + finalLine);
                        }
                    }

                }
                line = br.readLine();
            }

            if (!fileTags.isEmpty()){
                throw new IllegalStateException("Missing tags in file : " + fileTags);
            }

            return Optional.ofNullable(location);

        } catch (IOException e) {
            LOG.severe(() -> "Error loading data file : " + path);
        } catch (IllegalStateException e){
            LOG.severe(() -> "Location file malformed : " + e.getMessage());
        }


        return Optional.empty();
    }

    static record LocationInfo(String name, String description) {
    }
}