package idlegame.util.textfilter;

import java.math.BigDecimal;

public class DoubleTextFilter extends TextFilter<Double>{

    public DoubleTextFilter(){
        super("([0-9](,[0-9]*)?e-?[0-9]([0-9]*)?)|([0-9]([0-9]*)?(,[0-9]*)?)");
    }

    public DoubleTextFilter(ValueCondition<Double> vc){
        super("([0-9](,[0-9]*)?e-?[0-9]([0-9]*)?)|([0-9]([0-9]*)?(,[0-9]*)?)", vc, newString -> Double.parseDouble(newString.replaceAll(",", ".")));
    }
}