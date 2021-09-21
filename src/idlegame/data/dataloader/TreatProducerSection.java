package idlegame.data.dataloader;

import idlegame.data.Producer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class TreatProducerSection extends TreatSection<Set<Producer>> {
    public TreatProducerSection(String previous) {
        super("_producer", previous);
    }

    @Override
    protected Set<Producer> treatImpl(Set<String> tags, BufferedReader br) throws IOException {
        String line = br.readLine();
        Set<Producer> producers = new HashSet<>();

        String name = "";
        String description = "";
        Set<String> produced = new HashSet<>();
        Set<String> consumed = new HashSet<>();
        Section section = Section.NAME;


        while (line != null && !endSectionTag.equals(line.trim())){
            if (!line.isBlank()) {
                switch (section) {
                    case NAME -> {
                        name = line;
                        section = Section.DESCRIPTION;
                    }
                    case DESCRIPTION -> {
                        description = line;
                        section = Section.PRODUCED;
                    }
                    case PRODUCED -> {
                        if (line.equals("/endProduced")) {
                            section = Section.CONSUMED;
                        } else {
                            produced.add(line);
                        }
                    }
                    case CONSUMED -> {
                        if (line.equals("/endConsumed")) {
                            section = Section.NAME;
                            Producer producer = new Producer(name, description, produced, consumed);
                            producers.add(producer);
                            produced = new HashSet<>();
                            consumed = new HashSet<>();
                        } else {
                            consumed.add(line);
                        }
                    }
                    default -> throw new IllegalStateException("Unexpected value: " + section);
                }
            }

            line = br.readLine();
        }



        if (line == null || !endSectionTag.equals(line.trim())) {
            throw new IllegalStateException("Section _tank has unexpected structure");
        }

        return producers;
    }

    private enum Section{
        NAME,
        DESCRIPTION,
        PRODUCED,
        CONSUMED
    }
}