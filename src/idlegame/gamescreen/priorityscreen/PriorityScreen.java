package idlegame.gamescreen.priorityscreen;

import idlegame.data.GameData;
import idlegame.data.PriorityList;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;

public class PriorityScreen extends VBox {

    public PriorityScreen(GameData gameData) {
        super(20);
        setPadding(new Insets(20));
   //     getStylesheets().add(PRODUCER_SCREEN_STYLE_SHEET_PATH);

        PriorityList list = gameData.getMyShip().getLocation().getPriorityList();



        setStyle("-fx-background-color: #490749");
    }
}