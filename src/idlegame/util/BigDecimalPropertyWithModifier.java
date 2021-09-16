package idlegame.util;

import javafx.beans.binding.ObjectBinding;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Set;

public class BigDecimalPropertyWithModifier extends ObjectBinding<BigDecimal> {

    private final BigDecimalProperty initialValue;
    private final Set<BigDecimalProperty> multipliersMods;
    private final Set<BigDecimalProperty> additiveMods;

    public BigDecimalPropertyWithModifier(BigDecimalProperty initialValue, Set<BigDecimalProperty> multipliersMods, Set<BigDecimalProperty> additiveMods) {
        super();
        this.initialValue = initialValue;
        this.multipliersMods = Set.copyOf(multipliersMods);
        this.additiveMods = Set.copyOf(additiveMods);

        bind(initialValue);
        for (BigDecimalProperty bip : this.multipliersMods){
            bind(bip);
        }

        for (BigDecimalProperty bip : this.additiveMods){
            bind(bip);
        }

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
        BigDecimal value = initialValue.getValue();

        BigDecimal mod1 = additiveMods.stream().map(BigDecimalProperty::getValue).reduce(BigDecimal.ZERO, BigDecimalPropertyWithModifier::add);

        return null;
    }

    private static BigDecimal add(BigDecimal a, BigDecimal b) {
        return a.add(b);
    }
}