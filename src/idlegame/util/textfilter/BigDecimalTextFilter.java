package idlegame.util.textfilter;

import java.math.BigDecimal;

public class BigDecimalTextFilter extends TextFilter<BigDecimal>{

    public BigDecimalTextFilter(){
        super("([0-9](,[0-9]*)?e-?[0-9]([0-9]*)?)|([0-9]([0-9]*)?(,[0-9]*)?)");
    }

    public BigDecimalTextFilter(ValueCondition<BigDecimal> vc){
        super("([0-9](,[0-9]*)?e-?[0-9]([0-9]*)?)|([0-9]([0-9]*)?(,[0-9]*)?)", vc, newString -> new BigDecimal(newString.replaceAll(",", ".")));
    }
}