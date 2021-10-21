package idlegame.data;


import idlegame.util.property2.DoubleSum;
import javafx.beans.binding.DoubleExpression;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.VBox;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This serves as a way to group producers, researches and such under same conditions
 * will also place some flag controls to state the group behavior
 */
public class PriorityGroup extends VBox {
    private final ObservableList<Prioritable> group;
    private ExecutionType executionType;
    private final Map<Prioritable, Map<ResourceType, DoubleExpression>> neededResourcesPerTask;
    private final Map<Resourceful, Map<ResourceType, DoubleSum>> neededResourcesPerLocation;
    private final Map<ResourceType, DoubleSum> totalNeededResources;


    public PriorityGroup() {
        group = FXCollections.observableArrayList();
        neededResourcesPerTask = new HashMap<>();
        neededResourcesPerLocation = new HashMap<>();
        executionType = ExecutionType.SEQUENTIAL;

        totalNeededResources = Collections.unmodifiableMap(generateRessourceSumMap());
    }

    private Map<ResourceType, DoubleSum> generateRessourceSumMap() {
        final Map<ResourceType, DoubleSum> sumMap;
        sumMap = new HashMap<>();

        for (ResourceType type : ResourceType.getAll().values()){
            sumMap.put(type, new DoubleSum());
        }
        return sumMap;
    }

    public void addTask(Prioritable task){
        group.add(task);
        var needs = task.getNeeds();
        neededResourcesPerTask.put(task, needs);

        var perLocation = neededResourcesPerLocation.computeIfAbsent(task.getResourceful(), (k) -> {
            var map = generateRessourceSumMap();
            for (Map.Entry<ResourceType, DoubleSum> s : map.entrySet()){
                totalNeededResources.get(s.getKey()).add(s.getValue());
            }
            return Collections.unmodifiableMap(map);
        });

        for (Map.Entry<ResourceType, DoubleExpression> s2 : needs.entrySet()){
            perLocation.get(s2.getKey()).add(s2.getValue());
        }
    }

    public void removeTask(Prioritable task){
        group.remove(task);
        var resources = neededResourcesPerTask.remove(task);

        var perLocation = neededResourcesPerLocation.get(task.getResourceful());

        for (Map.Entry<ResourceType, DoubleExpression> s2 : resources.entrySet()){
            perLocation.get(s2.getKey()).remove(s2.getValue());
        }
    }

    public void setExecutionType(ExecutionType type){
        executionType = type;
    }


    public void execute(){
        executionType.execute(this);
    }

    private enum ExecutionType {
        SEQUENTIAL((group) -> {
            for (Prioritable task : group.group){
            task.preRun();
            Map<ResourceType, DoubleExpression> requires = task.getNeeds();
            Map<ResourceType, Double> obtained = task.getResourceful().request(requires);
            task.receive(obtained);
            }
        }),
        PARALLEL((group) -> {
            Map<ResourceType, Double> ratios = new HashMap<>();
            Map<ResourceType, Double> obtainedCumulative = new HashMap<>();
            for (Prioritable task : group.group){
                task.preRun();
            }

            for (Map.Entry<Resourceful, Map<ResourceType, DoubleSum>> needs : group.neededResourcesPerLocation.entrySet()){
                var obtained = needs.getKey().request(needs.getValue());
                obtained.forEach((k, v) -> obtainedCumulative.merge(k, v, Double::sum));
            }

            for (Map.Entry<ResourceType, Double> gotten : obtainedCumulative.entrySet()){
                var total = group.totalNeededResources.get(gotten.getKey()).getValue();
                ratios.put(gotten.getKey(), total == 0 ? 0d : gotten.getValue() / total);
            }

            for (Prioritable task : group.group){
                Map<ResourceType, Double> sends = new HashMap<>();
                group.neededResourcesPerTask.get(task).forEach((k, v) -> sends.put(k, ratios.get(k) * (v.getValue())));
                task.receive(sends);
            }

        });

        final Execution execution;

        ExecutionType(Execution execution) {
            this.execution = execution;
        }


        public void execute(PriorityGroup group){
            execution.execute(group);
        }
    }

    private interface Execution{
        void execute(PriorityGroup group);
    }
}