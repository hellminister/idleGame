package idlegame.data;

import idlegame.util.property.*;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Objects;


public class Resource {

    protected final ResourceType type;                          // The resource name will never change, it is static
    protected final BigDecimalProperty amount;
    protected final BigDecimalProperty maxCapacity;
    protected final BigDecimal initialMaxCapacity;

    protected final DoubleBinding fillRatio;

    protected final BigDecimalProperty effectiveMaxCapacity;
    protected final DoubleProperty effectiveMaxCapacityRatio;
    protected final BooleanProperty effectiveRatioToggle;
    protected final BigDecimalBinding weightProperty;
    protected boolean countStore = true;
    protected boolean countRequest = true;
    protected boolean invert = false;


    protected final BigDecimalPerSecondProperty perSecond;

    public Resource(ResourceType type, BigDecimal maxCapacityInitial) {
        this.type = type;
        this.amount = new BigDecimalProperty(BigDecimal.ZERO);
        this.initialMaxCapacity = maxCapacityInitial;
        this.maxCapacity = new BigDecimalProperty(maxCapacityInitial);
        perSecond = new BigDecimalPerSecondProperty();

        fillRatio = new DoubleBinding() {
            {
                bind(amount);
                bind(maxCapacity);
            }
            @Override
            protected double computeValue() {
                return amount.get().divide(maxCapacity.get(), 10, RoundingMode.HALF_UP).doubleValue();
            }
        };

        effectiveMaxCapacity = new BigDecimalProperty(maxCapacity.getValue());
        effectiveMaxCapacityRatio = new SimpleDoubleProperty(1.0);
        effectiveRatioToggle = new SimpleBooleanProperty(true);

        maxCapacity.addListener((mc, oldV, newV) -> {
            if (effectiveRatioToggle.get()){
                effectiveMaxCapacity.set(newV.multiply(BigDecimal.valueOf(effectiveMaxCapacityRatio.get())));
            } else {
                if (effectiveMaxCapacity.get().compareTo(newV) >= 1){
                    effectiveMaxCapacity.set(newV);
                }
                effectiveMaxCapacityRatio.set(effectiveMaxCapacity.get().divide(newV, 10, RoundingMode.HALF_UP).doubleValue());
            }

            if (amount.get().compareTo(effectiveMaxCapacity.get())>= 0){
                amount.set(effectiveMaxCapacity.get());
            }
        });

        effectiveMaxCapacityRatio.addListener((ratio, oldV, newV) ->{
            // probably a useless check
            if (!Objects.equals(oldV, newV)){
                effectiveMaxCapacity.set(maxCapacity.get().multiply(BigDecimal.valueOf((Double) newV)));
                if (amount.get().compareTo(effectiveMaxCapacity.get())>= 0){
                    amount.set(effectiveMaxCapacity.get());
                }
            }
        });

        effectiveMaxCapacity.addListener((ratio, oldV, newV) ->{
            if (!Objects.equals(oldV, newV)){
                effectiveMaxCapacityRatio.set(effectiveMaxCapacity.get().divide(maxCapacity.get(), 10, RoundingMode.HALF_UP).doubleValue());
                if (amount.get().compareTo(effectiveMaxCapacity.get())>= 0){
                    amount.set(effectiveMaxCapacity.get());
                }
            }
        });

        weightProperty = new BigDecimalBinding() {
            final BigDecimalStringProperty asString;

            {
                bind(amount, type.getWeight());

                NumberFormat formatter = new DecimalFormat("0.000###E0");
                asString = new BigDecimalStringProperty(formatter, this);
            }
            @Override
            protected BigDecimal computeValue() {
                return amount.get().multiply(type.getWeight().getValue());
            }

            public BigDecimalStringProperty asStringProperty() {
                return asString;
            }
        };
        type.register(this);
    }

    public void moveTick(){
        perSecond.moveTick();
    }


    public BigDecimalPerSecondProperty perSecondProperty() {
        return perSecond;
    }

    /**
     *
     * @param toStore amount to be stored
     * @return the amount exceeding max capacity
     */
    public BigDecimal store(BigDecimal toStore){
        BigDecimal excess;
        if (isAtMaxCapacity()){
            excess = toStore;
        } else {

            BigDecimal sum = amount.get().add(toStore);

            if (sum.compareTo(effectiveMaxCapacity.get()) <= 0) {
                amount.setValue(sum);
                excess = BigDecimal.ZERO;
            } else {
                amount.setValue(effectiveMaxCapacity.getValue());
                excess = sum.subtract(effectiveMaxCapacity.get());
            }
        }
        BigDecimal actualStore = toStore.subtract(excess);
        if (countStore) {
            perSecond.add(invert ? actualStore.negate() : actualStore);
        }
        return excess;
    }

    public boolean isAtMaxCapacity() {
        return amount.get().compareTo(effectiveMaxCapacity.get()) >= 0;
    }

    // rename to realMaxCapacity, and add equivalent methode for effective capacity
    public boolean isAtLeastOfCapacity(BigDecimal value) {
        return amount.get().compareTo(maxCapacity.get().multiply(value)) >= 0;
    }

    /**
     * @param requested amount to be retrieved
     * @return actual amount retrieved
     */
    public BigDecimal request(BigDecimal requested){
        BigDecimal retrieved;

        if (isEmpty()){
            retrieved =  BigDecimal.ZERO;
        } else {
            BigDecimal balance = amount.get().subtract(requested);

            if (balance.compareTo(BigDecimal.ZERO) >= 0) {
                amount.setValue(balance);
                retrieved =  requested;
            } else {
                balance = amount.get();
                amount.setValue(BigDecimal.ZERO);
                retrieved = balance;
            }
        }
        if (countRequest){
            perSecond.add(invert ? retrieved : retrieved.negate());
        }
        return retrieved;
    }

    public boolean isEmpty() {
        return amount.get().compareTo(BigDecimal.ZERO) <= 0;
    }

    public BigDecimalProperty getMaxCapacity() {
        return maxCapacity;
    }

    public ReadOnlyStringProperty getName(){
        return type.getName();
    }

    public ReadOnlyBigDecimalProperty getAmount() {
        return amount;
    }

    public String toString(){
        return type.getName() + " -> " + amount.asStringProperty().get() + "  /  " + maxCapacity.asStringProperty().get();
    }

    public DoubleBinding getFillRatio(){
        return fillRatio;
    }

    public ResourceType getType() {
        return type;
    }

    public BigDecimalProperty getEffectiveMaxCapacity() {
        return effectiveMaxCapacity;
    }

    public DoubleProperty getEffectiveMaxCapacityRatio() {
        return effectiveMaxCapacityRatio;
    }

    public BooleanProperty getEffectiveMaxCapacityRatioToggle() {
        return effectiveRatioToggle;
    }

    public BigDecimalBinding getWeight() {
        return weightProperty;
    }

}