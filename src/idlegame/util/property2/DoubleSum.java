package idlegame.util.property2;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.DoubleExpression;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashSet;

public class DoubleSum extends DoubleBinding {
    private final DoubleStringBinding asString;

    private final ObservableSet<DoubleExpression> toAdd;

    public DoubleSum(){
        toAdd = FXCollections.observableSet(new HashSet<>());
        bind(toAdd); // when a value is added or removed from the set, this is recalculated

        NumberFormat formatter = new DecimalFormat("0.000###E0");
        asString = new DoubleStringBinding(formatter, this);
    }

    public void add(DoubleExpression value){
        bind(value);
        toAdd.add(value);
    }

    public void remove(DoubleExpression value){
        unbind(value);
        toAdd.remove(value);
    }

    public DoubleStringBinding asStringProperty() {
        return asString;
    }

    /**
     * Calculates the current value of this binding.
     * <p>
     * Classes extending {@code ObjectBinding} have to provide an implementation
     * of {@code computeValue}.
     *
     * @return the current value
     */
    @Override
    protected double computeValue() {
        return toAdd.isEmpty() ? 0d : toAdd.stream().reduce(0d, (a, b) -> a + b.getValue(), Double::sum);
    }

}