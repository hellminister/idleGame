package idlegame.data.dataloader.locationloader;

import idlegame.data.dataloader.TreatEndSection;
import idlegame.data.dataloader.TreatProducerSection;
import idlegame.data.dataloader.TreatTankSection;

public class LocationTreatSections {
    public final TreatLocationInfoSection locationInfoSection;
    public final TreatTankSection locationTankSection;
    public final TreatProducerSection locationProducerSection;
    public final TreatLocationUnlockSection locationUnlockSection;
    public final TreatEndSection locationEndSection;

    public LocationTreatSections() {
        locationInfoSection = new TreatLocationInfoSection();
        locationTankSection = new TreatTankSection("_info");
        locationProducerSection = new TreatProducerSection("_tank");
        locationUnlockSection = new TreatLocationUnlockSection();
        locationEndSection = new TreatEndSection("_unlock");
    }
}