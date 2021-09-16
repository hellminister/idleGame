package idlegame.gamescreen.storagescreen;

import idlegame.data.Resource;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.util.StringConverter;
import javafx.util.converter.PercentageStringConverter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.function.UnaryOperator;

class Tank extends HBox {
    protected Resource tank;

    public Tank(Resource tank) {

        this.tank = tank;
        setStyle("-fx-background-color: black; " +
                "-fx-border-width: 2; " +
                "-fx-border-color: white");
        AnchorPane layoutA = new AnchorPane();
        getChildren().add(layoutA);

        Label name = new Label(tank.getName());
        name.setTextFill(Color.WHITE);
        name.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 30));


        var fillRatioBar = new ProgressBar();
        fillRatioBar.progressProperty().bind(tank.getFillRatio());
        fillRatioBar.setRotate(-90);
        fillRatioBar.setStyle("-fx-accent: white; -fx-control-inner-background: black");

        // because of rotation height is width and width is height
        fillRatioBar.setPrefSize(250, 40);
        fillRatioBar.setMinSize(250, 40);
        fillRatioBar.setMaxSize(250, 40);

        var fillRatio = new Group();
        fillRatio.getChildren().add(fillRatioBar);

        var effectiveSlider = new Slider();
        effectiveSlider.setMin(0.0);
        effectiveSlider.setMax(1.0);
        effectiveSlider.valueProperty().bindBidirectional(tank.getEffectiveMaxCapacityRatio());
        effectiveSlider.setOrientation(Orientation.VERTICAL);

        effectiveSlider.setPrefSize(0, 249);
        effectiveSlider.setMinSize(0, 249);
        effectiveSlider.setMaxSize(0, 249);

        effectiveSlider.getStyleClass().add("max-capacity-ratio-slider");

        Label amountNameLabel = new Label("Amount");
        amountNameLabel.setTextFill(Color.WHITE);
        amountNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 15));

        Label amountLabel = new Label();
        amountLabel.textProperty().bind(tank.getAmount().asStringProperty());
        amountLabel.setTextFill(Color.WHITE);
        amountLabel.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 20));

        Label maxCapNameLabel = new Label("Maximum");
        maxCapNameLabel.setTextFill(Color.WHITE);
        maxCapNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 15));

        Label maxCapLabel = new Label();
        maxCapLabel.textProperty().bind(tank.getMaxCapacity().asStringProperty());
        maxCapLabel.setTextFill(Color.WHITE);
        maxCapLabel.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 20));

        Label effectiveNameLabel = new Label("Caped At");
        effectiveNameLabel.setTextFill(Color.WHITE);
        effectiveNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 15));

        TextField effectiveCapRatio = new TextField();
        effectiveCapRatio.setEditable(true);

        UnaryOperator<TextFormatter.Change> doubleFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[0-9]{1,13}(,[0-9]*)?%")) {
                double d = Double.parseDouble(newText.replaceAll("%", "").replaceAll(",", "."));
                if (d > 100) {
                    return null;
                }
                return change;
            }
            return null;
        };

        TextFormatter<Number> ratioTextFormatter = new TextFormatter<>(new PercentageStringConverter(new DecimalFormat("##0.0###%")),
                tank.getEffectiveMaxCapacityRatio().doubleValue(),
                doubleFilter);
        effectiveCapRatio.setTextFormatter(ratioTextFormatter);
        ratioTextFormatter.valueProperty().bindBidirectional(tank.getEffectiveMaxCapacityRatio());

        effectiveCapRatio.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 20));
        effectiveCapRatio.getStyleClass().add("effective-cap-ratio-text-field");


        TextField effectiveCap = new TextField();
        effectiveCap.setEditable(true);

        UnaryOperator<TextFormatter.Change> bigDecimalFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[0-9](,[0-9]*)?E-?[0-9]([0-9]*)?")) {
                BigDecimal bd = new BigDecimal(newText.replaceAll(",", "."));
                if (bd.compareTo(tank.getMaxCapacity().get()) >= 1) {
                    return null;
                }
                return change;
            }
            if (newText.matches("[0-9]([0-9]*)?(,[0-9]*)?")) {
                BigDecimal bd = new BigDecimal(newText.replaceAll(",", "."));
                if (bd.compareTo(tank.getMaxCapacity().get()) >= 1) {
                    return null;
                }
                return change;
            }
            return null;
        };

        TextFormatter<BigDecimal> effectiveTextFormatter = new TextFormatter<>(new StringConverter<>() {

            private final NumberFormat numberFormat = new DecimalFormat("0.000###E0");

            /**
             * Converts the object provided into its string form.
             * Format of the returned string is defined by the specific converter.
             *
             * @param object the object of type {@code T} to convert
             * @return a string representation of the object passed in.
             */
            @Override
            public String toString(BigDecimal object) {
                return numberFormat.format(object);
            }

            /**
             * Converts the string provided into an object defined by the specific converter.
             * Format of the string and type of the resulting object is defined by the specific converter.
             *
             * @param string the {@code String} to convert
             * @return an object representation of the string passed in.
             */
            @Override
            public BigDecimal fromString(String string) {
                string = string.replaceAll(",", ".");
                System.out.println(string);
                BigDecimal bigDecimal = new BigDecimal(string);
                System.out.println(bigDecimal);
                return bigDecimal;
            }
        },
                tank.getEffectiveMaxCapacity().getValue(),
                bigDecimalFilter);

        effectiveCap.setTextFormatter(effectiveTextFormatter);
        effectiveTextFormatter.valueProperty().bindBidirectional(tank.getEffectiveMaxCapacity());

        effectiveCap.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 20));
        effectiveCap.getStyleClass().add("effective-cap-text-field");

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
        weightNameLabel.setTextFill(Color.WHITE);
        weightNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 15));

        Label weightLabel = new Label();
        weightLabel.textProperty().bind(tank.getWeight().asStringProperty());
        weightLabel.setTextFill(Color.WHITE);
        weightLabel.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 20));

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

        AnchorPane.setTopAnchor(weightLabel, 270.0);
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