package idlegame.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PriorityList {
    private final ObservableList<PriorityGroup> groups;


    public PriorityList() {
        groups = FXCollections.observableArrayList();
    }

    public PriorityGroup newGroup() {
        PriorityGroup group = new PriorityGroup();
        groups.add(group);
        return group;
    }

    public ObservableList<PriorityGroup> getGroups(){
        return groups;
    }

    public void run() {
        groups.forEach(PriorityGroup::execute);
    }
}