package idlegame.ui;

import idlegame.data.GameData;
import idlegame.ui.gamescreen.GameScreen;
import idlegame.language.Localize;
import idlegame.ui.startscreen.StartScreen;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainWindow extends Application {
    private StartScreen startScreen;
    private GameScreen gameScreen;

    private GameData gameData;

    private Stage stage;

    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     *
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages.
     */
    @Override
    public void start(Stage primaryStage) {

        Localize.load("francais");
        stage = primaryStage;
        primaryStage.titleProperty().bind(Localize.get("idt_GAME_TITLE"));

        gameData = new GameData();

        startScreen = new StartScreen(this);
        gameScreen = new GameScreen(this);

        primaryStage.setScene(startScreen);
        primaryStage.sizeToScene();
        primaryStage.setMaximized(true);
        primaryStage.show();


    }

    public void stop(){
        gameScreen.stop();
    }


    public void switchToGameScene() {
        stage.setScene(gameScreen);
        stage.setMaximized(false);
        stage.setMaximized(true);
        gameScreen.start();
    }

    public GameData getGameData() {
        return gameData;
    }
}