package idlegame.data.dataloader.locationloader;

import idlegame.data.dataloader.TreatSection;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Set;

public class TreatLocationUnlockSection extends TreatSection<String> {
    public TreatLocationUnlockSection() {
        super("_unlock", "_producer");
    }

    @Override
    protected String treatImpl(Set<String> tags, BufferedReader br) throws IOException {
        br.readLine();
        return "";
    }
}