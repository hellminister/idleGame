package idlegame.util.textfilter;

import java.math.BigDecimal;

public class BigDecimalPercentageTextFilter extends TextFilter<BigDecimal> {
    public BigDecimalPercentageTextFilter(){
        super("([0-9](,[0-9]*)?E-?[0-9]([0-9]*)?)|([0-9]([0-9]*)?(,[0-9]*)?)%");
    }

    public BigDecimalPercentageTextFilter(ValueCondition<BigDecimal> vc){
        super("([0-9](,[0-9]*)?E-?[0-9]([0-9]*)?)|([0-9]([0-9]*)?(,[0-9]*)?)%", vc, newString -> new BigDecimal(newString.replaceAll("%", "").replaceAll(",", ".")));
    }
}