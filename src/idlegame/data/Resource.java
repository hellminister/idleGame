package idlegame.data;

import idlegame.ui.gamescreen.storagescreen.Tank;
import idlegame.util.property2.*;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.*;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Objects;


public class Resource {

    protected final ResourceType type;                          // The resource name will never change, it is static
    protected final DoubleProperty amount;
    protected final DoubleProperty maxCapacity;
    protected final double initialMaxCapacity;

    protected final DoubleBinding fillRatio;

    protected final DoubleProperty effectiveMaxCapacity;
    protected final DoubleProperty effectiveMaxCapacityRatio;
    protected final BooleanProperty effectiveRatioToggle;
    protected final DoubleBinding weightProperty;
    protected boolean countStore = true;
    protected boolean countRequest = true;
    protected boolean invert = false;

    protected Tank tankUI = null;


    protected final DoublePerSecondProperty perSecond;

    public Resource(String creationLine, String... otherTags){
        this(concat(creationLine.split(" "), otherTags));
    }

    private static String[] concat(String[] s, String[] otherTags) {
        String[] result = new String[s.length + otherTags.length];

        System.arraycopy(s, 0, result, 0, s.length);
        System.arraycopy(otherTags, 0, result, s.length, otherTags.length);

        return result;
    }

    private Resource(String[] s) {
        this(ResourceType.get(s[0]), Double.parseDouble(s[1]), Arrays.copyOfRange(s, 2, s.length));
    }

    // TODO treat tags
    public Resource(ResourceType type, double maxCapacityInitial, String... tags) {
        this.type = type;
        this.amount = new SimpleDoubleProperty(0d);
        this.initialMaxCapacity = maxCapacityInitial;
        this.maxCapacity = new SimpleDoubleProperty(maxCapacityInitial);
        perSecond = new DoublePerSecondProperty();

        fillRatio = new DoubleBinding() {
            {
                bind(amount);
                bind(maxCapacity);
            }
            @Override
            protected double computeValue() {
                return amount.get() / maxCapacity.get();
            }
        };

        effectiveMaxCapacity = new SimpleDoubleProperty(maxCapacity.getValue());
        effectiveMaxCapacityRatio = new SimpleDoubleProperty(1.0);
        effectiveRatioToggle = new SimpleBooleanProperty(true);

        maxCapacity.addListener((mc, oldV, newV) -> {
            if (effectiveRatioToggle.get()){
                effectiveMaxCapacity.set((Double)newV * effectiveMaxCapacityRatio.get());
            } else {
                if (effectiveMaxCapacity.get() > ((Double)newV)){
                    effectiveMaxCapacity.set((Double)newV);
                }
                effectiveMaxCapacityRatio.set(effectiveMaxCapacity.get() / ((Double)newV));
            }

            if (amount.get() >= (effectiveMaxCapacity.get())){
                amount.set(effectiveMaxCapacity.get());
            }
        });

        effectiveMaxCapacityRatio.addListener((ratio, oldV, newV) ->{
            // probably a useless check
            if (!Objects.equals(oldV, newV)){
                effectiveMaxCapacity.set(maxCapacity.get()* ((Double) newV));
                if (amount.get() >= (effectiveMaxCapacity.get())){
                    amount.set(effectiveMaxCapacity.get());
                }
            }
        });

        effectiveMaxCapacity.addListener((ratio, oldV, newV) ->{
            if (!Objects.equals(oldV, newV)){
                effectiveMaxCapacityRatio.set(effectiveMaxCapacity.get() / (maxCapacity.get()));
                if (amount.get()>=(effectiveMaxCapacity.get())){
                    amount.set(effectiveMaxCapacity.get());
                }
            }
        });

        weightProperty = new DoubleBinding() {
            final DoubleStringBinding asString;

            {
                bind(amount, type.getWeight());

                NumberFormat formatter = new DecimalFormat("0.000###E0");
                asString = new DoubleStringBinding(formatter, this);
            }
            @Override
            protected double computeValue() {
                return amount.get() * ((Double)type.getWeight().getValue());
            }

            public DoubleStringBinding asStringProperty() {
                return asString;
            }
        };
        type.register(this);
    }



    public void moveTick(){
        perSecond.moveTick();
    }


    public DoublePerSecondProperty perSecondProperty() {
        return perSecond;
    }

    /**
     *
     * @param toStore amount to be stored
     * @return the amount exceeding max capacity
     */
    public double store(double toStore){
        double excess;
        if (isAtMaxCapacity()){
            excess = toStore;
        } else {

            double sum = amount.get() + (toStore);

            if (sum <= (effectiveMaxCapacity.get())) {
                amount.setValue(sum);
                excess = 0d;
            } else {
                amount.setValue(effectiveMaxCapacity.getValue());
                excess = sum - (effectiveMaxCapacity.get());
            }
        }
        double actualStore = toStore - excess;
        if (countStore) {
            perSecond.addNewValue(invert ? actualStore * -1 : actualStore);
        }
        return excess;
    }

    public boolean isAtMaxCapacity() {
        return amount.get() >= (effectiveMaxCapacity.get());
    }

    // rename to realMaxCapacity, and add equivalent methode for effective capacity
    public boolean isAtLeastOfCapacity(double value) {
        return amount.get() >= (maxCapacity.get() * (value));
    }

    /**
     * @param requested amount to be retrieved
     * @return actual amount retrieved
     */
    public double request(double requested){
        double retrieved;

        if (isEmpty()){
            retrieved =  0d;
        } else {
            double balance = amount.get() - (requested);

            if (balance >= (0)) {
                amount.setValue(balance);
                retrieved = requested;
            } else {
                balance = amount.get();
                amount.setValue(0d);
                retrieved = balance;
            }
        }
        if (countRequest){
            perSecond.addNewValue(invert ? retrieved : retrieved * -1);
        }
        return retrieved;
    }

    public boolean isEmpty() {
        return amount.get() <= 0;
    }

    public ReadOnlyDoubleProperty getMaxCapacity() {
        return maxCapacity;
    }

    public ReadOnlyStringProperty getName(){
        return type.getName();
    }

    public ReadOnlyDoubleProperty getAmount() {
        return amount;
    }

    public DoubleBinding getFillRatio(){
        return fillRatio;
    }

    public ResourceType getType() {
        return type;
    }

    public DoubleProperty getEffectiveMaxCapacity() {
        return effectiveMaxCapacity;
    }

    public DoubleProperty getEffectiveMaxCapacityRatio() {
        return effectiveMaxCapacityRatio;
    }

    public BooleanProperty getEffectiveMaxCapacityRatioToggle() {
        return effectiveRatioToggle;
    }

    public DoubleBinding getWeight() {
        return weightProperty;
    }

    public Tank getTankUI() {
        if (tankUI == null){
            tankUI = new Tank(this);
        }
        return tankUI;
    }
}