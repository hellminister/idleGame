package idlegame.data.dataloader.shiploader;

import idlegame.data.dataloader.TreatEndSection;
import idlegame.data.dataloader.TreatProducerSection;
import idlegame.data.dataloader.TreatTankSection;


public class ShipTreatSections {
    public final TreatTankSection shipTankSection;
    public final TreatShipStartInSection shipStartInSection;
    public final TreatProducerSection shipProducerSection;
    public final TreatEndSection shipEndSection;

    public ShipTreatSections() {
        shipTankSection = new TreatTankSection(null);
        shipStartInSection = new TreatShipStartInSection();
        shipProducerSection = new TreatProducerSection("_startIn");
        shipEndSection = new TreatEndSection("_producer");

    }
}