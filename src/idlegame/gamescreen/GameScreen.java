package idlegame.gamescreen;

import idlegame.Main;
import idlegame.gamescreen.producerscreen.ProducerScreen;
import idlegame.gamescreen.storagescreen.StorageScreen;
import idlegame.util.Util;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.HashMap;

public class GameScreen extends Scene {

    private static final String TANK_STYLE_SHEET_PATH = Util.getFilePathString("resources/stylesheet/Tank.css");
    private static final String PRODUCERUI_STYLE_SHEET_PATH = Util.getFilePathString("resources/stylesheet/ProducerUI.css");

    private final HashMap<String, Pane> views;

 //   private final AnimationTimer ticking;
    private final TickTicker ticking;

    /**
     * Creates a Scene for a specific root Node.
     *
     * Currently copying (Mostly) the interface of Space Travel Idle (since I like that user interface)
     *
     */
    public GameScreen(Main main) {
        super(new BorderPane());

        getStylesheets().addAll(TANK_STYLE_SHEET_PATH, PRODUCERUI_STYLE_SHEET_PATH);

        var root = (BorderPane)getRoot();
        root.setStyle("-fx-background-color: black");

        views = new HashMap<>();

        var centerPane = new StackPane();

        root.setCenter(centerPane);

        VBox categories = new VBox();
        categories.setAlignment(Pos.TOP_CENTER);
        categories.setStyle("-fx-border-color: white;" +
                " -fx-border-width: 3");

        Button storage = createButton("Storage", new StorageScreen(main.getGameData()), centerPane);
        Button producer = createButton("Producer", new ProducerScreen(main.getGameData()), centerPane);

        categories.getChildren().addAll(storage, producer);

        root.setLeft(categories);
        views.get("Storage").setVisible(true);

       // ticking = new GameAnimationTimer(main.getGameData());
        ticking = new TickTicker(main.getGameData());
    }

    private Button createButton(String name, Pane view, StackPane center){
        Button button = new Button(name);
        views.put(name, view);
        center.getChildren().add(view);
        view.setVisible(false);
        button.setOnAction(event -> switchTo(name));
        return button;
    }

    private void switchTo(String viewName) {
        views.values().forEach(v -> v.setVisible(false));
        views.get(viewName).setVisible(true);
    }

    public void start() {
        ticking.start();
    }

    public void stop() {
        ticking.stop();
    }

}