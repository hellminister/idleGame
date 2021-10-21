package idlegame.util.textfilter;

import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;

public class DoublePercentageStringConverter extends StringConverter<Number> {
    private final NumberFormat numberFormat;

    public DoublePercentageStringConverter(NumberFormat nf){
        numberFormat = nf;
    }

    /**
     * Converts the object provided into its string form.
     * Format of the returned string is defined by the specific converter.
     *
     * @param object the object of type {@code T} to convert
     * @return a string representation of the object passed in.
     */
    @Override
    public String toString(Number object) {
        return numberFormat.format((Double)object * 100d) + "%";
    }

    /**
     * Converts the string provided into an object defined by the specific converter.
     * Format of the string and type of the resulting object is defined by the specific converter.
     *
     * @param string the {@code String} to convert
     * @return an object representation of the string passed in.
     */
    @Override
    public Number fromString(String string) {
        return Double.parseDouble(string.replaceAll(",", ".").replaceAll("%", "")) / 100;
    }

}