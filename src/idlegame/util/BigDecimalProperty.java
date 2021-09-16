package idlegame.util;

import javafx.beans.property.SimpleObjectProperty;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public final class BigDecimalProperty extends SimpleObjectProperty<BigDecimal> implements ReadOnlyBigDecimalProperty {

    private final BigDecimalStringProperty asString;

    public BigDecimalProperty(BigDecimal value){
        super(value);

        NumberFormat formatter = new DecimalFormat("0.000###E0");
        asString = new BigDecimalStringProperty(formatter, this);
    }

    @Override
    public BigDecimalStringProperty asStringProperty() {
        return asString;
    }
}