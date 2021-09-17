package idlegame.util.textfilter;

import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.text.NumberFormat;

public class BigDecimalStringConverter extends StringConverter<BigDecimal> {


    private final NumberFormat numberFormat;

    public BigDecimalStringConverter(NumberFormat nf){
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
    public String toString(BigDecimal object) {
        return numberFormat.format(object).replaceAll("E", "e");
    }

    /**
     * Converts the string provided into an object defined by the specific converter.
     * Format of the string and type of the resulting object is defined by the specific converter.
     *
     * @param string the {@code String} to convert
     * @return an object representation of the string passed in.
     */
    @Override
    public BigDecimal fromString(String string) {
        return new BigDecimal(string.replaceAll(",", "."));
    }
}