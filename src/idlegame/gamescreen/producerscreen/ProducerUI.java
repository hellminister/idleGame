package idlegame.gamescreen.producerscreen;

import idlegame.data.Producer;
import idlegame.gamescreen.storagescreen.Tank;
import idlegame.util.textfilter.BigDecimalPercentageStringConverter;
import idlegame.util.textfilter.BigDecimalPercentageTextFilter;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class ProducerUI extends HBox {
    private final Producer producer;
    private ResourceMonitor selected;

    public ProducerUI(Producer producer) {
        this.producer = producer;

        getStyleClass().add("producer-ui-main-box");
        visibleProperty().bind(producer.unlockedProperty());

        var layoutA = new AnchorPane();

        Label name = new Label(producer.getName());
        name.getStyleClass().add("producer-ui-title-label");

        TextField pRate = new TextField();
        pRate.setEditable(true);

        TextFormatter<BigDecimal> pRateTextFormatter = new TextFormatter<BigDecimal>(new BigDecimalPercentageStringConverter(new DecimalFormat("##0.#")),
                producer.getProductionRate().getValue(),
                new BigDecimalPercentageTextFilter(d -> (d.compareTo(BigDecimal.ZERO) >= 0 && d.compareTo(BigDecimal.valueOf(100)) <= 0)));
        pRateTextFormatter.valueProperty().bindBidirectional(producer.getProductionRate());

        pRate.setTextFormatter(pRateTextFormatter);
        pRate.getStyleClass().addAll("producer-ui-production-rate-text-field", "producer-ui-title-label");

        Label description = new Label(producer.getDescription());
        description.getStyleClass().add("producer-ui-description-label");

        Label produces = new Label("Produces");
        produces.getStyleClass().add("producer-ui-section-label");

        Label consumes = new Label("Consumes");
        consumes.getStyleClass().add("producer-ui-section-label");

        VBox producers = new VBox(5);

        producer.getProduced().values().forEach(v -> {
            ResourceMonitor rm = new ResourceMonitor(v, this);
            producers.getChildren().add(rm);
        });

        VBox consumers = new VBox(5);

        producer.getConsumed().values().forEach(v -> {
            ResourceMonitor rm = new ResourceMonitor(v, this);
            consumers.getChildren().add(rm);
        });


        layoutA.getChildren().addAll(name, description, produces, consumes, producers, consumers, pRate);

        AnchorPane.setTopAnchor(name, 5.0);
        AnchorPane.setLeftAnchor(name, 5.0);

        AnchorPane.setTopAnchor(pRate, 5.0);
        AnchorPane.setRightAnchor(pRate, 5.0);

        AnchorPane.setTopAnchor(description, 45.0);
        AnchorPane.setLeftAnchor(description, 5.0);

        AnchorPane.setTopAnchor(produces, 100.0);
        AnchorPane.setLeftAnchor(produces, 5.0);

        AnchorPane.setTopAnchor(consumes, 100.0);
        AnchorPane.setLeftAnchor(consumes, 200.0);

        AnchorPane.setTopAnchor(producers, 125.0);
        AnchorPane.setLeftAnchor(producers, 5.0);

        AnchorPane.setTopAnchor(consumers, 125.0);
        AnchorPane.setLeftAnchor(consumers, 200.0);

        layoutA.setMinSize(400, 300);
        layoutA.setPrefSize(400, 300);
        layoutA.setMaxSize(400, 300);

        getChildren().addAll(layoutA);
    }

    private ResourceMonitor getSelected(){
        return selected;
    }
    private void setSelected(ResourceMonitor rm) {
        selected = rm;
    }

    static class ResourceMonitor extends VBox {
        private final Producer.ProdResource resource;
        private final ProducerUI owner;
        private final Tank tank;

        ResourceMonitor(Producer.ProdResource resource, ProducerUI owner) {
            this.resource = resource;
            this.owner = owner;
            tank = new Tank(resource);

            Label name =  new Label(resource.getName());
            name.getStyleClass().add("producer-ui-resource-label");

            ProgressBar fillRatioBar = new ProgressBar();
            fillRatioBar.progressProperty().bind(resource.getFillRatio());
            fillRatioBar.getStyleClass().add("producer-ui-resource-fill-bar");

            fillRatioBar.setOnMouseClicked(event -> {
                ResourceMonitor selected = owner.getSelected();
                if (selected == this){
                    owner.setSelected(null);
                    owner.getChildren().remove(tank);
                } else {
                    if (selected != null){
                        owner.getChildren().remove(selected.tank);
                    }
                    owner.getChildren().add(tank);
                    owner.setSelected(this);
                }
            });

            getChildren().addAll(name, fillRatioBar);
        }
    }


}