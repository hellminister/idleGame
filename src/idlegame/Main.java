package idlegame;

import idlegame.ui.MainWindow;
import javafx.application.Application;

/**
 * this class with MainWindow permits a hiding of the modules classes from the outside
 */
public class Main {

    public static void main(String[] args) {
        Application.launch(MainWindow.class, args);
    }

}