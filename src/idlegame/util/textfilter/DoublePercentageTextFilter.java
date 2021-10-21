package idlegame.util.textfilter;

public class DoublePercentageTextFilter extends TextFilter<Double> {

    public DoublePercentageTextFilter(){
        super("[0-9]{1,13}(,[0-9]*)?%");
    }


    public DoublePercentageTextFilter(ValueCondition<Double> vc){
        super("[0-9]{1,13}(,[0-9]*)?%", vc, newString -> Double.parseDouble(newString.replaceAll("%", "").replaceAll(",", ".")));
    }
}