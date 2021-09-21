package idlegame.data.dataloader.shiploader;

import idlegame.data.Location;
import idlegame.data.Producer;
import idlegame.data.ResourceTanks;
import idlegame.data.Ship;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

public class ShipLoader {
    private static final Logger LOG = Logger.getLogger(ShipLoader.class.getName());
    private static final Set<String> tags = Set.of("_tank", "_startIn", "_producer", "_end");
    private static final ShipTreatSections treaters = new ShipTreatSections();

    public static Optional<Ship> loadShip(Path path){
        Path shipPath = path.resolve("Ship.txt");
        try (BufferedReader br = Files.newBufferedReader(shipPath)) {
            String line = br.readLine();

            Set<String> fileTags = new HashSet<>(tags);
            Ship ship = null;


            while(line != null){
                if (!line.isBlank() && !line.startsWith("#")){
                    switch (line) {
                        case "_tank" -> {
                            ResourceTanks tanks = treaters.shipTankSection.treat(fileTags, br);
                            ship = new Ship(tanks);
                        }
                        case "_startIn" -> {
                            Location location = treaters.shipStartInSection.treat(fileTags, br);
                            assert ship != null;
                            ship.setLocation(location);
                        }
                        case "_producer" -> {
                            Set<Producer> producers = treaters.shipProducerSection.treat(fileTags, br);
                            assert ship != null;
                            ship.addAllProducers(producers);
                        }
                        case "_end" -> treaters.shipEndSection.treat(fileTags, br);
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

            return Optional.ofNullable(ship);

        } catch (IOException e) {
            LOG.severe(() -> "Error loading data file : " + path);
        } catch (IllegalStateException e){
            LOG.severe(() -> "Location file malformed : " + e.getMessage());
        }


        return Optional.empty();
    }

}