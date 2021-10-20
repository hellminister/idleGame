package idlegame.data;

import javafx.beans.binding.ObjectExpression;

import java.math.BigDecimal;
import java.util.Map;

public interface Prioritable {
    void preRun();

    Map<ResourceType, ObjectExpression<BigDecimal>> getNeeds();

    Resourceful getResourceful();

    void receive(Map<ResourceType, BigDecimal> obtained);
}