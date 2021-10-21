package idlegame.ui.gamescreen.storagescreen;

import idlegame.data.Resource;
import idlegame.language.Localize;
import idlegame.util.Util;
import idlegame.util.property2.DoubleStringBinding;
import idlegame.util.textfilter.DoublePercentageTextFilter;
import idlegame.util.textfilter.DoubleStringConverter;
import idlegame.util.textfilter.DoubleTextFilter;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.converter.NumberStringConverter;
import javafx.util.converter.PercentageStringConverter;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Tank extends HBox {
    private static final String TANK_STYLE_SHEET_PATH = Util.getFilePathString("resources/stylesheet/Tank.css");

    protected Resource tank;

    public Tank(Resource tank) {
        getStylesheets().add(TANK_STYLE_SHEET_PATH);

        this.tank = tank;
        getStyleClass().add("tank-main-box");
        AnchorPane layoutA = new AnchorPane();
        getChildren().add(layoutA);

        Label name = new Label();
        name.textProperty().bind(tank.getName());
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

        Label amountNameLabel = new Label();
        amountNameLabel.textProperty().bind(Localize.get("idt_TANK_AMOUNT_LABEL"));
        amountNameLabel.getStyleClass().add("tank-section-label");

        Label amountLabel = new Label();
        NumberFormat formatter = new DecimalFormat("0.000###E0");
        var amountString = new DoubleStringBinding(formatter, tank.getAmount());
        amountLabel.textProperty().bind(amountString);
        amountLabel.getStyleClass().add("tank-value-label");

        Label maxCapNameLabel = new Label();
        maxCapNameLabel.textProperty().bind(Localize.get("idt_TANK_MAXIMUM_CAP_LABEL"));
        maxCapNameLabel.getStyleClass().add("tank-section-label");


        Label maxCapLabel = new Label();
        var maxCapacityString = new DoubleStringBinding(formatter, tank.getMaxCapacity());
        maxCapLabel.textProperty().bind(maxCapacityString);
        maxCapLabel.getStyleClass().add("tank-value-label");

        Label effectiveNameLabel = new Label();
        effectiveNameLabel.textProperty().bind(Localize.get("idt_TANK_EFFECTIVE_CAP_LABEL"));
        effectiveNameLabel.getStyleClass().add("tank-section-label");

        TextField effectiveCapRatio = new TextField();
        effectiveCapRatio.setEditable(true);

        TextFormatter<Number> ratioTextFormatter = new TextFormatter<>(new PercentageStringConverter(new DecimalFormat("##0.0###%")),
                tank.getEffectiveMaxCapacityRatio().doubleValue(),
                new DoublePercentageTextFilter(d -> d <= 100));
        ratioTextFormatter.valueProperty().bindBidirectional(tank.getEffectiveMaxCapacityRatio());

        effectiveCapRatio.setTextFormatter(ratioTextFormatter);
        effectiveCapRatio.getStyleClass().addAll("tank-effective-cap-ratio-text-field", "tank-value-label");


        TextField effectiveCap = new TextField();
        effectiveCap.setEditable(true);

        TextFormatter<Number> effectiveTextFormatter = new TextFormatter<>(new DoubleStringConverter(new DecimalFormat("0.000###E0")),
                tank.getEffectiveMaxCapacity().getValue(),
                new DoubleTextFilter(bd -> bd.compareTo(tank.getMaxCapacity().get()) < 1));

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

        Label weightNameLabel = new Label();
        weightNameLabel.textProperty().bind(Localize.get("idt_TANK_WEIGHT_LABEL"));
        weightNameLabel.getStyleClass().add("tank-section-label");

        Label weightLabel = new Label();
        var weightString = new DoubleStringBinding(formatter, tank.getWeight());
        weightLabel.textProperty().bind(weightString);
        weightLabel.getStyleClass().add("tank-value-label");

        Label deltaLabel = new Label();
        deltaLabel.textProperty().bind(tank.perSecondProperty().asStringProperty().concat("/s"));
        deltaLabel.getStyleClass().add("tank-delta-label");


        layoutA.getChildren().addAll(name, fillRatio, effectiveSlider, amountNameLabel, maxCapNameLabel, effectiveNameLabel,
                amountLabel, maxCapLabel, effectiveCapRatio, effectiveCap, ratioToggle, effectiveToggle, weightLabel, deltaLabel, weightNameLabel);


        AnchorPane.setTopAnchor(amountNameLabel, 50.0);
        AnchorPane.setLeftAnchor(amountNameLabel, 50.0);

        AnchorPane.setTopAnchor(amountLabel, 70.0);
        AnchorPane.setLeftAnchor(amountLabel, 50.0);


        AnchorPane.setTopAnchor(deltaLabel, 95.0);
        AnchorPane.setLeftAnchor(deltaLabel, 50.0);

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