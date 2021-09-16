package idlegame.gamescreen.storagescreen;

import idlegame.data.GameData;
import javafx.geometry.Insets;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;

public class StorageScreen extends StackPane {
    private GameData gameData;

    public StorageScreen(GameData gameData){
        setStyle("-fx-background-color: green");
        this.gameData = gameData;

        var shipTanks = new FlowPane();
        getChildren().add(shipTanks);

        shipTanks.setPadding(new Insets(10));
        shipTanks.setHgap(10);
        shipTanks.setVgap(10);

        gameData.getMyShip().getTanks().getAllResources().forEach(r -> {
            shipTanks.getChildren().add(new Tank(r));
        });

    }

}