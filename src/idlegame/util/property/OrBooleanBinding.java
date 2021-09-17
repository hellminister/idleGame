package idlegame.util.property;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ReadOnlyBooleanProperty;

import java.util.HashSet;
import java.util.Set;

public class OrBooleanBinding extends BooleanBinding {
    private final Set<ReadOnlyBooleanProperty> values;

    public OrBooleanBinding() {
        this.values = new HashSet<>();
    }

    public void addValue(ReadOnlyBooleanProperty value){
        values.add(value);
    }


    /**
     * Calculates the current value of this binding.
     * <p>
     * Classes extending {@code BooleanBinding} have to provide an
     * implementation of {@code computeValue}.
     *
     * @return the current value
     */
    @Override
    protected boolean computeValue() {
        boolean v = values.stream().anyMatch(ReadOnlyBooleanProperty::getValue);
        System.out.println(v);
        return v;
    }
}