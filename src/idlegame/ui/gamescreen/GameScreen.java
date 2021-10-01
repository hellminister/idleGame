package idlegame.ui.gamescreen;

import idlegame.ui.MainWindow;
import idlegame.ui.gamescreen.priorityscreen.PriorityScreen;
import idlegame.ui.gamescreen.producerscreen.ProducerScreen;
import idlegame.ui.gamescreen.storagescreen.StorageScreen;
import idlegame.language.Localize;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.HashMap;

public class GameScreen extends Scene {
    private final HashMap<String, Pane> views;

 //   private final AnimationTimer ticking;
    private final TickTicker ticking;

    /**
     * Creates a Scene for a specific root Node.
     *
     * Currently, copying (Mostly) the interface of Space Travel Idle (since I like that user interface)
     *
     * @param main the main window
     */
    public GameScreen(MainWindow main) {
        super(new BorderPane());

        var root = (BorderPane)getRoot();
        root.setStyle("-fx-background-color: black");

        views = new HashMap<>();

        var centerPane = new StackPane();

        root.setCenter(centerPane);

        VBox categories = new VBox();
        categories.setAlignment(Pos.TOP_CENTER);
        categories.setStyle("-fx-border-color: white;" +
                " -fx-border-width: 3");

        Button storage = createButton("idt_STORAGE_BUTTON_GAME_SCREEN", new StorageScreen(main.getGameData()), centerPane);
        Button producer = createButton("idt_PRODUCER_BUTTON_GAME_SCREEN", new ProducerScreen(main.getGameData()), centerPane);
        Button priority = createButton("idt_PRIORITY_BUTTON_GAME_SCREEN", new PriorityScreen(main.getGameData()), centerPane);

        categories.getChildren().addAll(storage, producer, priority);

        root.setLeft(categories);
        views.get("idt_STORAGE_BUTTON_GAME_SCREEN").setVisible(true);

       // ticking = new GameAnimationTimer(main.getGameData());
        ticking = new TickTicker(main.getGameData());
    }

    private Button createButton(String name, Pane view, StackPane center){
        Button button = new Button();
        button.textProperty().bind(Localize.get(name));
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