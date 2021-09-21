package idlegame.data.dataloader;

import java.io.BufferedReader;
import java.util.Set;

public class TreatEndSection extends TreatSection<String> {

    public TreatEndSection(String previous) {
        super("_end", previous);
    }

    @Override
    protected String treatImpl(Set<String> tags, BufferedReader br) {
        return "";
    }
}