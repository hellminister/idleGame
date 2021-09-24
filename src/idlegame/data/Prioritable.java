package idlegame.data;

import java.math.BigDecimal;
import java.util.Map;

public interface Prioritable {
    void preRun();

    Map<ResourceType, BigDecimal> getNeeds();

    Resourceful getResourceful();

    void receive(Map<ResourceType, BigDecimal> obtained);
}