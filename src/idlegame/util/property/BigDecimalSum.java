package idlegame.util.property;

import javafx.beans.binding.ObjectExpression;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashSet;

public class BigDecimalSum extends BigDecimalBinding{

    private final BigDecimalStringProperty asString;

    private final ObservableSet<ObjectExpression<BigDecimal>> toAdd;

    public BigDecimalSum(){
        toAdd = FXCollections.observableSet(new HashSet<>());
        bind(toAdd); // when a value is added or removed from the set, this is recalculated

        NumberFormat formatter = new DecimalFormat("0.000###E0");
        asString = new BigDecimalStringProperty(formatter, this);
    }

    public void add(ObjectExpression<BigDecimal> value){
        bind(value);
        toAdd.add(value);
    }

    public void remove(ObjectExpression<BigDecimal> value){
        unbind(value);
        toAdd.remove(value);
    }

    @Override
    public BigDecimalStringProperty asStringProperty() {
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
    protected BigDecimal computeValue() {
        return toAdd.isEmpty() ? BigDecimal.ZERO : toAdd.stream().reduce(BigDecimal.ZERO, (a, b) -> a.add(b.getValue()), BigDecimal::add);
    }
}