package idlegame.data;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

/**
 * This serves as a way to group producers, researches and such under same conditions
 * will also place some flag controls to state the group behavior
 */
public class PriorityGroup {
    private final ObservableList<Prioritable> group;
    private ExecutionType executionType;

    public PriorityGroup() {
        group = FXCollections.observableArrayList();
        executionType = ExecutionType.SEQUENTIAL;
    }

    public void addTask(Prioritable task){
        group.add(task);
    }

    public void removeTask(Prioritable task){
        group.remove(task);
    }

    public void setExecutionType(ExecutionType type){
        executionType = type;
    }


    public void execute(){
        executionType.execute(group);
    }

    private enum ExecutionType {
        SEQUENTIAL((group) -> {
            for (Prioritable task : group){
            task.preRun();
            Map<ResourceType, BigDecimal> requires = task.getNeeds();
            Map<ResourceType, BigDecimal> obtained = task.getResourceful().request(requires);
            task.receive(obtained);
            }
        }),
        PARALLEL((group) -> {
            Map<Prioritable, Map<ResourceType, BigDecimal>> neededResources = new HashMap<>();
            Map<ResourceType, BigDecimal> ratios = new HashMap<>();
            Map<ResourceType, BigDecimal> cumulative = new HashMap<>();
            for (Prioritable task : group){
                task.preRun();
                Map<ResourceType, BigDecimal> requires = task.getNeeds();
                neededResources.put(task, requires);
                requires.forEach((k, v) ->{
                    ratios.merge(k, v, BigDecimal::add);
                });
                Map<ResourceType, BigDecimal> obtained = task.getResourceful().request(requires);
                obtained.forEach((k, v) ->{
                    cumulative.merge(k, v, BigDecimal::add);
                });
            }

            ratios.forEach((k, v) -> {
                ratios.put(k, cumulative.get(k).divide(v, 6, RoundingMode.HALF_UP));
            });

            for (Prioritable task : group){
                Map<ResourceType, BigDecimal> requires = neededResources.get(task);
                requires.forEach((k, v) ->{
                    requires.put(k, ratios.get(k).multiply(v));
                });


                task.receive(requires);
            }

        });

        final Execution execution;

        ExecutionType(Execution execution) {
            this.execution = execution;
        }


        public void execute(ObservableList<Prioritable> group){
            execution.execute(group);
        }
    }

    private interface Execution{
        void execute(ObservableList<Prioritable> group);
    }
}