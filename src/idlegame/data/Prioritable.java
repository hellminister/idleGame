package idlegame.data;

import javafx.beans.binding.DoubleExpression;

import java.util.Map;

public interface Prioritable {
    void preRun();

    Map<ResourceType, DoubleExpression> getNeeds();

    Resourceful getResourceful();

    void receive(Map<ResourceType, Double> obtained);
}