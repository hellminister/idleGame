package idlegame.util.property;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class BigDecimalPerSecondProperty extends BigDecimalBinding {

    private static final int AVERAGE_OVER_SECONDS = 2;
    private static final int TICK_PER_SECOND = 10;
    private static final int MAX_TICKS = AVERAGE_OVER_SECONDS * TICK_PER_SECOND; // this should give about 5 secs;

    private final BigDecimal[] lastValues;
    private int tickNum;

    private BigDecimalProperty sum;

    private final BigDecimalStringProperty asString;

    public BigDecimalPerSecondProperty(){
        sum = new BigDecimalProperty(BigDecimal.ZERO);
        bind(sum);

        lastValues = new BigDecimal[MAX_TICKS];
        for (int i = 0; i < MAX_TICKS; i++) {
            lastValues[i] = BigDecimal.ZERO;
        }

        tickNum = 0;

        NumberFormat formatter = new DecimalFormat("+0.0E0;-0.0E0");

        asString = new BigDecimalStringProperty(formatter, this);

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
        return sum.getValue().divide(BigDecimal.valueOf(AVERAGE_OVER_SECONDS), 10, RoundingMode.HALF_UP);
    }

    public void add(BigDecimal value){
        lastValues[tickNum] = lastValues[tickNum].add(value);
    }

    public void moveTick() {
        BigDecimal difference = lastValues[tickNum];

        tickNum++;
        if (tickNum >= MAX_TICKS){
            tickNum = 0;
        }

        difference = difference.subtract(lastValues[tickNum]);

        sum.set(sum.getValue().add(difference));
        lastValues[tickNum] = BigDecimal.ZERO;

    }

    @Override
    public BigDecimalStringProperty asStringProperty() {
        return asString;
    }
}