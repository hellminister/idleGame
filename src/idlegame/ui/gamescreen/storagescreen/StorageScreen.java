package idlegame.ui.gamescreen.storagescreen;

import idlegame.data.GameData;
import idlegame.data.Resourceful;
import idlegame.data.Ship;
import idlegame.util.Util;
import javafx.geometry.Insets;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class StorageScreen extends VBox {
    private static final String STORAGE_SCREEN_STYLE_SHEET_PATH = Util.getFilePathString("resources/stylesheet/StorageScreen.css");

    private final FlowPane locationStorage;
    private Ship ship;

    public StorageScreen(GameData gameData){
        super(20);
        setPadding(new Insets(20));
        getStylesheets().add(STORAGE_SCREEN_STYLE_SHEET_PATH);

        setStyle("-fx-background-color: green");

        ship = gameData.getMyShip();


        var shipTanks = new FlowPane();
        shipTanks.getStyleClass().add("storage-screen-section-box");
        getChildren().add(shipTanks);

        shipTanks.setPadding(new Insets(10));
        shipTanks.setHgap(10);
        shipTanks.setVgap(10);

        locationStorage = new FlowPane();
        locationStorage.getStyleClass().add("storage-screen-section-box");
        getChildren().add(locationStorage);

        locationStorage.setPadding(new Insets(10));
        locationStorage.setHgap(10);
        locationStorage.setVgap(10);

        ship.getTanks().getAllResources().forEach(r -> shipTanks.getChildren().add(r.getTankUI()));

        fillLocation();

    }

    public void emptyLocation(){
        locationStorage.getChildren().clear();
    }

    public void fillLocation(){
        Resourceful location = ship.getOtherLocation();
        if (location != null){
            location.getTanks().getAllResources().forEach(r -> locationStorage.getChildren().add(r.getTankUI()));
        }
    }

}