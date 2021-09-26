package idlegame.data;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.LinkedList;

public class PriorityList extends SimpleListProperty<PriorityGroup> {
 //   private final ObservableList<PriorityGroup> groups;


    public PriorityList() {
        super(FXCollections.observableList(new LinkedList<PriorityGroup>()));
    }

    public PriorityGroup newGroup() {
        PriorityGroup group = new PriorityGroup();
        add(group);
        return group;
    }

    public void run() {
        forEach(PriorityGroup::execute);
    }
}