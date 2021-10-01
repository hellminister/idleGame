package idlegame.ui.gamescreen.producerscreen;

import idlegame.data.GameData;
import idlegame.data.Resourceful;
import idlegame.data.Ship;
import idlegame.util.Util;
import javafx.geometry.Insets;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class ProducerScreen extends VBox {
    private static final String PRODUCER_SCREEN_STYLE_SHEET_PATH = Util.getFilePathString("resources/stylesheet/ProducerScreen.css");

    private final FlowPane locationProducer;
    private Ship ship;

    public ProducerScreen(GameData gameData){
        super(20);
        setPadding(new Insets(20));
        getStylesheets().add(PRODUCER_SCREEN_STYLE_SHEET_PATH);

        ship = gameData.getMyShip();

        setStyle("-fx-background-color: blue");

        var shipProducers = new FlowPane();
        shipProducers.getStyleClass().add("producer-screen-section-box");
        getChildren().add(shipProducers);

        shipProducers.setPadding(new Insets(10));
        shipProducers.setHgap(10);
        shipProducers.setVgap(10);

        locationProducer = new FlowPane();
        locationProducer.getStyleClass().add("producer-screen-section-box");
        getChildren().add(locationProducer);

        locationProducer.setPadding(new Insets(10));
        locationProducer.setHgap(10);
        locationProducer.setVgap(10);

        ship.getAllProducers().forEach(r -> shipProducers.getChildren().add(r.getProducerUI()));

        fillLocation();
    }

    public void emptyLocation(){
        locationProducer.getChildren().clear();
    }

    public void fillLocation(){
        Resourceful location = ship.getOtherLocation();
        if (location != null){
            location.getAllProducers().forEach(r -> locationProducer.getChildren().add(r.getProducerUI()));
        }
    }
}