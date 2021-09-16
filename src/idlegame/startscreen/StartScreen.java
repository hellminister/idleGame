package idlegame.startscreen;

import idlegame.Main;
import idlegame.language.Localize;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static idlegame.language.Localize.Target.START_SCREEN_START_BUTTON;

public class StartScreen extends Scene {
    /**
     * Creates a Scene for a specific root Node.
     *
     * @param main the Main game object
     */
    public StartScreen(Main main) {
        super(new StackPane());

        var root = (StackPane)getRoot();
        root.setStyle("-fx-background-color: black");
        root.setAlignment(Pos.CENTER);

        Image image = null;
        // Background image
        try (InputStream is = Files.newInputStream(Paths.get("resources/images/3787597.jpg"))) {
            image = new Image(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create background Pane
        var backgroundPlate = new ImageView(image);
        backgroundPlate.setPreserveRatio(true);

        // this pane is to permit resizing of the window
        var backgroundImagePane = new StackPane();
        backgroundImagePane.getChildren().add(backgroundPlate);

        root.setPrefSize(backgroundPlate.getLayoutBounds().getWidth(), backgroundPlate.getLayoutBounds().getHeight());

        backgroundPlate.fitHeightProperty().bind(root.heightProperty());
        backgroundPlate.fitWidthProperty().bind(root.widthProperty());

        root.getChildren().add(backgroundImagePane);

        var buttonBar = new VBox();
        buttonBar.setAlignment(Pos.CENTER);

        Button startButton = new Button();
        startButton.textProperty().bind(Localize.get(START_SCREEN_START_BUTTON));
        startButton.setOnAction(event -> main.switchToGameScene());


        buttonBar.getChildren().addAll(startButton);
        root.getChildren().addAll(buttonBar);
    }
}