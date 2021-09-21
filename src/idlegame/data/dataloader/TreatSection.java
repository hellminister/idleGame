package idlegame.data.dataloader;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Set;

public abstract class TreatSection<T> {
    protected final String myTag;
    protected final String endSectionTag;
    protected final String previous;

    public TreatSection(String myTag, String previous) {
        this.myTag = myTag;
        this.previous = previous;
        endSectionTag = myTag.substring(1).concat("_");
    }

    public T treat(Set<String> tags, BufferedReader br) throws IllegalStateException, IOException {
        if (previous != null && tags.contains(previous)){
            throw new IllegalStateException(previous + " was not treated");
        }
        if (!tags.contains(myTag)){
            throw new IllegalStateException(myTag + " was already treated");
        }
        tags.remove(myTag);

        return treatImpl(tags, br);
    }

    protected abstract T treatImpl(Set<String> tags, BufferedReader br) throws IOException;

}