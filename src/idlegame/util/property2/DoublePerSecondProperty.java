package idlegame.util.property2;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class DoublePerSecondProperty extends DoubleBinding {


    private static final int AVERAGE_OVER_SECONDS = 2;
    private static final int TICK_PER_SECOND = 10;
    private static final int MAX_TICKS = AVERAGE_OVER_SECONDS * TICK_PER_SECOND; // this should give about 5 secs;

    private final Double[] lastValues;
    private int tickNum;

    private final DoubleProperty sum;

    private final DoubleStringBinding asString;

    public DoublePerSecondProperty(){
        sum = new SimpleDoubleProperty(0);
        bind(sum);

        lastValues = new Double[MAX_TICKS];
        for (int i = 0; i < MAX_TICKS; i++) {
            lastValues[i] = 0d;
        }

        tickNum = 0;

        NumberFormat formatter = new DecimalFormat("+0.0E0;-0.0E0");

        asString = new DoubleStringBinding(formatter, this);

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
        return sum.getValue() / AVERAGE_OVER_SECONDS;
    }

    public void addNewValue(double value){
        lastValues[tickNum] = lastValues[tickNum] + (value);
    }

    public void moveTick() {
        Double difference = lastValues[tickNum];

        tickNum++;
        if (tickNum >= MAX_TICKS){
            tickNum = 0;
        }

        difference = difference - (lastValues[tickNum]);

        sum.set(sum.getValue() + (difference));
        lastValues[tickNum] = 0d;

    }

    public DoubleStringBinding asStringProperty() {
        return asString;
    }
}