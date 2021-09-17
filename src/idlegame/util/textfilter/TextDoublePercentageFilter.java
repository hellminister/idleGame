package idlegame.util.textfilter;

public class TextDoublePercentageFilter extends TextFilter<Double> {

    public TextDoublePercentageFilter(){
        super("[0-9]{1,13}(,[0-9]*)?%");
    }


    public TextDoublePercentageFilter(ValueCondition<Double> vc){
        super("[0-9]{1,13}(,[0-9]*)?%", vc, newString -> Double.parseDouble(newString.replaceAll("%", "").replaceAll(",", ".")));
    }
}