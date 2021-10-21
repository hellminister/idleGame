package idlegame.util.property2;

import javafx.beans.binding.DoubleExpression;
import javafx.beans.binding.StringBinding;

import java.text.NumberFormat;

public class DoubleStringBinding extends StringBinding {
    private final NumberFormat formatter;
    private final DoubleExpression value;

    public DoubleStringBinding(NumberFormat formatter, DoubleExpression value) {
        this.formatter = formatter;
        this.value = value;
        this.bind(this.value);
    }

    /**
     * Calculates the current value of this binding.
     * <p>
     * Classes extending {@code StringBinding} have to provide an implementation
     * of {@code computeValue}.
     *
     * @return the current value
     */
    @Override
    protected String computeValue() {
        return formatter.format(value.getValue()).replaceAll("E", "e");
    }
}