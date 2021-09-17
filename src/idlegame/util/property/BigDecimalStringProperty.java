package idlegame.util.property;

import javafx.beans.binding.ObjectExpression;
import javafx.beans.binding.StringBinding;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class BigDecimalStringProperty extends StringBinding {
    private final NumberFormat formatter;
    private final ObjectExpression<BigDecimal> value;

    public BigDecimalStringProperty(NumberFormat formatter, ObjectExpression<BigDecimal> value) {
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
        return formatter.format(value.getValue()).replaceAll("E", " E");
    }
}