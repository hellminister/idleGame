package idlegame.util.property;

import javafx.beans.property.ReadOnlyProperty;

import java.math.BigDecimal;

public sealed interface ReadOnlyBigDecimalProperty extends ReadOnlyProperty<BigDecimal>, StringableBigDecimalExpression permits BigDecimalProperty {

}