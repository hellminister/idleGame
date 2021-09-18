package idlegame.language;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Singleton class containing the texts to show
 * This is to reduce the need to pass objects through object that otherwise wouldn't need to see the parent calls
 */
public final class Localize {
    private static final Logger LOG = Logger.getLogger(Localize.class.getName());

    /**
     * The singleton object
     */
    private static final Localize localize;

    static {
        Map<String, StringProperty> temp = new HashMap<>();

        Path dataFolder = Paths.get("resources/data/");

        try (Stream<Path> walk = Files.walk(dataFolder)) {
            walk.filter(Files::isRegularFile).forEach(path -> {
                try (BufferedReader br = Files.newBufferedReader(path)) {
                    String line = br.readLine();

                    while(line != null){
                        if (!line.isBlank() && !line.startsWith("#")){
                            String[] text = line.split(" ");

                            Arrays.stream(text).filter(s -> s.startsWith("idt_")).forEach(s -> temp.putIfAbsent(s, new SimpleStringProperty()));

                        }
                        line = br.readLine();
                    }

                } catch (IOException e) {
                    LOG.severe(() -> "Error loading data file : " + path);
                }
            });
        } catch (IOException e) {
            LOG.severe(() -> "Error treating data folder : " + dataFolder);
        }

        localize = new Localize(temp);
    }

    /**
     * @param target the string to obtain
     * @return the asked string
     */
    public static ReadOnlyStringProperty get(String target){
        return localize.getString(target);
    }

    /**
     * Loads the asked language
     * @param language the language to load
     */
    public static void load(String language){
        localize.loadLanguage(language);
    }

    /**
     * @return the currently loaded language
     */
    public static String getCurrentLanguage(){
        return localize.getLanguage();
    }

    /**
     * The list of localized String
     */
    private final Map<String, StringProperty> localizedStrings;

    /**
     * The currently loaded language
     */
    private String language;

    private Localize(Map<String, StringProperty> temp) {
        localizedStrings = Collections.unmodifiableMap(temp);
    }

    /**
     * @param target the wanted String name
     * @return the wanted String
     */
    private ReadOnlyStringProperty getString(String target){
        return  localizedStrings.getOrDefault(target, new SimpleStringProperty("Error for " + target));
    }

    private void loadLanguage(String language) {
        this.language = language;
        Path languageFolder = Paths.get("resources/language/" + language);
        Set<String> notLoaded = new HashSet<>(localizedStrings.keySet());

        try (Stream<Path> walk = Files.walk(languageFolder)) {
            walk.filter(Files::isRegularFile).forEach(path -> {
                String line = "";
                try (BufferedReader br = Files.newBufferedReader(path)) {
                    line = br.readLine();

                    while(line != null){
                        if (!line.isBlank() && !line.startsWith("#")){
                            String[] text = line.split(" ", 2);

                            String key = text[0];
                            localizedStrings.get(key).setValue(text[1]);
                            notLoaded.remove(key);
                        }

                        line = br.readLine();
                    }

                } catch (IOException e) {
                    LOG.severe(() -> "Error loading localization file : " + path);
                } catch (Exception e) {
                    String finalLine = line;
                    LOG.warning(() -> "tries to do something with " + finalLine);
                }
            });
        } catch (IOException e) {
            LOG.severe(() -> "Error treating language folder " + language + " : " + languageFolder);
        }

        if (!notLoaded.isEmpty()){
            LOG.severe(() -> "Missing localization of these entries : " + notLoaded);
        }
    }
    /**
     * @return the currently loaded language
     */
    private String getLanguage(){
        return language;
    }
}