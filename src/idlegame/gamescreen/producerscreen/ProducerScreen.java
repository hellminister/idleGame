package idlegame.gamescreen.producerscreen;

import idlegame.data.GameData;
import javafx.geometry.Insets;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;

public class ProducerScreen extends StackPane {

    public ProducerScreen(GameData gameData){
        setStyle("-fx-background-color: blue");

        var shipProducers = new FlowPane();
        getChildren().add(shipProducers);

        shipProducers.setPadding(new Insets(10));
        shipProducers.setHgap(10);
        shipProducers.setVgap(10);

        gameData.getMyShip().getAllProducers().forEach(r -> {
            shipProducers.getChildren().add(new ProducerUI(r));
        });
    }
}