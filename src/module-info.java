module IdleGame {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;

    exports idlegame;
    exports idlegame.ui to javafx.graphics;
}