package idlegame.util;

import java.net.MalformedURLException;
import java.nio.file.Paths;

/**
 * This class will contain utility methods and constants
 */
public final class Util {
    private Util(){}

    public static String getFileCSSPathString(String s) {
        try {
            return Paths.get(s).toUri().toURL().toExternalForm();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "";
        }
    }
}