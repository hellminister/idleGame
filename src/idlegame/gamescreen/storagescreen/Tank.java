package idlegame.gamescreen.storagescreen;

import idlegame.data.Resource;
import idlegame.util.textfilter.BigDecimalStringConverter;
import idlegame.util.textfilter.BigDecimalTextFilter;
import idlegame.util.textfilter.TextDoublePercentageFilter;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.converter.PercentageStringConverter;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class Tank extends HBox {
    protected Resource tank;

    public Tank(Resource tank) {

        this.tank = tank;
        getStyleClass().add("tank-main-box");
        AnchorPane layoutA = new AnchorPane();
        getChildren().add(layoutA);

        Label name = new Label(tank.getName());
        name.getStyleClass().add("tank-title-label");

        var fillRatioBar = new ProgressBar();
        fillRatioBar.progressProperty().bind(tank.getFillRatio());
        fillRatioBar.getStyleClass().add("tank-fill-bar");
        fillRatioBar.setRotate(-90);

        var fillRatio = new Group();
        fillRatio.getChildren().add(fillRatioBar);

        var effectiveSlider = new Slider();
        effectiveSlider.setMin(0.0);
        effectiveSlider.setMax(1.0);
        effectiveSlider.valueProperty().bindBidirectional(tank.getEffectiveMaxCapacityRatio());
        effectiveSlider.getStyleClass().add("tank-max-capacity-ratio-slider");

        Label amountNameLabel = new Label("Amount");
        amountNameLabel.getStyleClass().add("tank-section-label");

        Label amountLabel = new Label();
        amountLabel.textProperty().bind(tank.getAmount().asStringProperty());
        amountLabel.getStyleClass().add("tank-value-label");

        Label maxCapNameLabel = new Label("Maximum");
        maxCapNameLabel.getStyleClass().add("tank-section-label");


        Label maxCapLabel = new Label();
        maxCapLabel.textProperty().bind(tank.getMaxCapacity().asStringProperty());
        maxCapLabel.getStyleClass().add("tank-value-label");

        Label effectiveNameLabel = new Label("Caped At");
        effectiveNameLabel.getStyleClass().add("tank-section-label");

        TextField effectiveCapRatio = new TextField();
        effectiveCapRatio.setEditable(true);

        TextFormatter<Number> ratioTextFormatter = new TextFormatter<>(new PercentageStringConverter(new DecimalFormat("##0.0###%")),
                tank.getEffectiveMaxCapacityRatio().doubleValue(),
                new TextDoublePercentageFilter(d -> d <= 100));
        ratioTextFormatter.valueProperty().bindBidirectional(tank.getEffectiveMaxCapacityRatio());

        effectiveCapRatio.setTextFormatter(ratioTextFormatter);
        effectiveCapRatio.getStyleClass().addAll("tank-effective-cap-ratio-text-field", "tank-value-label");


        TextField effectiveCap = new TextField();
        effectiveCap.setEditable(true);

        TextFormatter<BigDecimal> effectiveTextFormatter = new TextFormatter<>(new BigDecimalStringConverter(new DecimalFormat("0.000###E0")),
                tank.getEffectiveMaxCapacity().getValue(),
                new BigDecimalTextFilter(bd -> bd.compareTo(tank.getMaxCapacity().get()) < 1));

        effectiveCap.setTextFormatter(effectiveTextFormatter);
        effectiveTextFormatter.valueProperty().bindBidirectional(tank.getEffectiveMaxCapacity());

        effectiveCap.getStyleClass().addAll("tank-effective-cap-text-field", "tank-value-label");

        ToggleGroup toggleGroup = new ToggleGroup();

        var ratioToggle = new RadioButton();
        ratioToggle.selectedProperty().bindBidirectional(tank.getEffectiveMaxCapacityRatioToggle());
        ratioToggle.setToggleGroup(toggleGroup);

        var effectiveToggle = new RadioButton();
        effectiveToggle.setToggleGroup(toggleGroup);
        if (!tank.getEffectiveMaxCapacityRatioToggle().get()) {
            effectiveToggle.setSelected(true);
        }

        Label weightNameLabel = new Label("Weight");
        weightNameLabel.getStyleClass().add("tank-section-label");

        Label weightLabel = new Label();
        weightLabel.textProperty().bind(tank.getWeight().asStringProperty());
        weightLabel.getStyleClass().add("tank-value-label");


        layoutA.getChildren().addAll(name, fillRatio, effectiveSlider, amountNameLabel, maxCapNameLabel, effectiveNameLabel,
                amountLabel, maxCapLabel, effectiveCapRatio, effectiveCap, ratioToggle, effectiveToggle, weightLabel, weightNameLabel);


        AnchorPane.setTopAnchor(amountNameLabel, 50.0);
        AnchorPane.setLeftAnchor(amountNameLabel, 50.0);

        AnchorPane.setTopAnchor(amountLabel, 75.0);
        AnchorPane.setLeftAnchor(amountLabel, 50.0);

        AnchorPane.setTopAnchor(maxCapNameLabel, 110.0);
        AnchorPane.setLeftAnchor(maxCapNameLabel, 50.0);

        AnchorPane.setTopAnchor(maxCapLabel, 135.0);
        AnchorPane.setLeftAnchor(maxCapLabel, 50.0);

        AnchorPane.setTopAnchor(effectiveNameLabel, 165.0);
        AnchorPane.setLeftAnchor(effectiveNameLabel, 50.0);

        AnchorPane.setTopAnchor(effectiveCapRatio, 187.0);
        AnchorPane.setLeftAnchor(effectiveCapRatio, 70.0);

        AnchorPane.setTopAnchor(ratioToggle, 190.0);
        AnchorPane.setLeftAnchor(ratioToggle, 50.0);

        AnchorPane.setTopAnchor(effectiveCap, 212.0);
        AnchorPane.setLeftAnchor(effectiveCap, 70.0);

        AnchorPane.setTopAnchor(effectiveToggle, 215.0);
        AnchorPane.setLeftAnchor(effectiveToggle, 50.0);

        AnchorPane.setTopAnchor(weightNameLabel, 245.0);
        AnchorPane.setLeftAnchor(weightNameLabel, 50.0);

        AnchorPane.setTopAnchor(weightLabel, 267.0);
        AnchorPane.setLeftAnchor(weightLabel, 50.0);

        AnchorPane.setTopAnchor(name, 2.0);
        AnchorPane.setLeftAnchor(name, 5.0);

        AnchorPane.setBottomAnchor(fillRatio, 3.0);
        AnchorPane.setLeftAnchor(fillRatio, 4.0);

        AnchorPane.setBottomAnchor(effectiveSlider, 4.0);
        AnchorPane.setLeftAnchor(effectiveSlider, 25.0);

        layoutA.setMinSize(200, 300);
        layoutA.setPrefSize(200, 300);
        layoutA.setMaxSize(200, 300);
    }
}