package learnfx.javafx9be.ch06controls;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

@SuppressWarnings("SpellCheckingInspection")
public class ButtonFun extends Application {
    private static final String CSS_FILE = "/css/ch06/button-fun.css";

    private Car[] cars;

    @Override
    public void init() throws Exception {
        super.init();
        cars = new Car[3];
        cars[0] = Car.from("/images/ch06/spr_bluecar_0_0.png",
                "/images/ch06/spr_bluecar_0_0-backwards.png",
                "Select this car to drive to work.");
        cars[1] = Car.from("/images/ch06/sportscar.png",
                "/images/ch06/sportscar-backwards.png",
                "Select this car to drive to the theater.");
        cars[2] = Car.from("/images/ch06/travel_vehicle.png",
                "/images/ch06/travel_vehicle.png",
                "Select this car to go on vacation.");
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Button Fun");
        BorderPane root = new BorderPane();
        root.setId("background");
        Scene scene = new Scene(root, 900, 250);
        scene.getStylesheets().add(getClass().getResource(CSS_FILE).toExternalForm());
        VBox leftControlPane = new VBox(10);
        leftControlPane.setPadding(new Insets(0, 10, 20, 15));

        // Create radio buttons for linear, ease in and ease out
        ToggleGroup chosenCarToggle = new ToggleGroup();
        RadioButton easeLinearBtn = new RadioButton("Work Car");
        easeLinearBtn.setUserData(cars[0]);
        easeLinearBtn.getStyleClass().add("option-button");
        easeLinearBtn.setSelected(true);
        easeLinearBtn.setToggleGroup(chosenCarToggle);
        RadioButton easeInBtn = new RadioButton("Weekend Car");
        easeInBtn.setUserData(cars[1]);
        easeInBtn.getStyleClass().add("option-button");
        easeInBtn.setToggleGroup(chosenCarToggle);
        RadioButton easeOutBtn = new RadioButton("Travel Van");
        easeOutBtn.setUserData(cars[2]);
        easeOutBtn.getStyleClass().add("option-button");
        easeOutBtn.setToggleGroup(chosenCarToggle);

        // hyperlink
        Hyperlink carInfoLink = createHyperLink(chosenCarToggle);

        leftControlPane.getChildren().add(carInfoLink);
        // Create check boxes to turn lights on or off.
        CheckBox headLightsCheckBox = new CheckBox("Headlights on");
        leftControlPane.getChildren().add(headLightsCheckBox);
        leftControlPane.setAlignment(Pos.BOTTOM_LEFT);
        leftControlPane.getChildren().addAll(easeLinearBtn, easeInBtn, easeOutBtn);

        // Create button controls to move car forward or backward.
        HBox hbox = new HBox(10);
        Button leftBtn = new Button("<");
        leftBtn.getStyleClass().add("nav-button");
        Button rightBtn = new Button(">");
        rightBtn.getStyleClass().add("nav-button");
        FlowPane controlPane = new FlowPane();
        FlowPane.setMargin(hbox, new Insets(0, 5, 10, 10));
        hbox.getChildren().addAll(leftBtn, rightBtn);
        controlPane.getChildren().add(hbox);
        root.setBottom(controlPane);

        // Draw the ground surface
        AnchorPane surface = new AnchorPane();
        root.setCenter(surface);
        root.setLeft(leftControlPane);
        int x1 = 20, x2 = 500;
        ImageView carView = new ImageView(cars[0].getForwards());
        carView.setPreserveRatio(true);
        carView.setFitWidth(150);
        carView.setX(x1);
        Arc carHeadlights = new Arc();
        carHeadlights.setId("car-headlights-1");
        carHeadlights.setCenterX(50.0f);
        carHeadlights.setCenterY(90.0f);
        carHeadlights.setRadiusX(300.0f);
        carHeadlights.setRadiusY(300.0f);
        carHeadlights.setStartAngle(170.0f);
        carHeadlights.setLength(15f);
        carHeadlights.setType(ArcType.ROUND);
        carHeadlights.visibleProperty().bind(headLightsCheckBox.selectedProperty());

        // Easing car (sports car)
        AnchorPane.setBottomAnchor(carView, 20.0);
        AnchorPane.setBottomAnchor(carHeadlights, 20.0);
        AnchorPane carPane = new AnchorPane(carHeadlights, carView);
        AnchorPane.setBottomAnchor(carPane, 20.0);
        surface.getChildren().add(carPane);

        // The animation based on the currently selected radio buttons.
        TranslateTransition animateCar = new TranslateTransition(Duration.millis(400), carPane);
        animateCar.setInterpolator(Interpolator.LINEAR);
        animateCar.toXProperty().set(x2);
        //animateCar.setInterpolator((Interpolator) group.getSelectedToggle().getUserData());
        animateCar.setDelay(Duration.millis(100));

        // Go forward (Left)
        leftBtn.setTooltip(new Tooltip("Drive forward"));
        leftBtn.setOnAction( ae -> {
            animateCar.stop();
            Car selectedCar = (Car) chosenCarToggle.getSelectedToggle().getUserData();
            carView.setImage(selectedCar.getForwards());
            animateCar.toXProperty().set(x1);
            animateCar.playFromStart();
        });

        // Go backward (Right)
        rightBtn.setTooltip(new Tooltip("Drive backward"));
        rightBtn.setOnAction( ae -> {
            animateCar.stop();
            Car selectedCar = (Car) chosenCarToggle.getSelectedToggle().getUserData();
            carView.setImage(selectedCar.getBackwards());
            animateCar.toXProperty().set(x2);
            animateCar.playFromStart();
        });
        chosenCarToggle.selectedToggleProperty().addListener((ob, oldVal, newVal) -> {
            Car selectedCar = (Car) newVal.getUserData();
            System.out.println("selected car: " + selectedCar.getDescription());
            carView.setImage(selectedCar.getForwards());
        });
        stage.setScene(scene);
        stage.show();
    }

    private Hyperlink createHyperLink(ToggleGroup chosenCarToggle) {
        Hyperlink carInfoLink = new Hyperlink("Car Information");
        Popup carInfoPopup = new Popup();
        carInfoPopup.getScene().getStylesheets().add(getClass().getResource(CSS_FILE).toExternalForm());
        carInfoPopup.setAutoHide(true);
        carInfoPopup.setHideOnEscape(true);
        Arc pointer = new Arc(0, 0, 20, 20, -20, 40);
        pointer.setType(ArcType.ROUND);
        Rectangle msgRect = new Rectangle(18, -20, 200.5, 150);

        Shape msgBubble = Shape.union(pointer, msgRect);
        msgBubble.getStyleClass().add("message-bubble");

        TextFlow textMsg = new TextFlow();
        textMsg.setPrefWidth(msgRect.getWidth() - 5);
        textMsg.setPrefHeight(msgRect.getHeight() - 5);
        textMsg.setLayoutX(pointer.getBoundsInLocal().getWidth() + 5);
        textMsg.setLayoutY(msgRect.getLayoutY() + 5);

        Text descr = new Text();
        descr.setFill(Color.ORANGE);
        textMsg.getChildren().add(descr);
        // whenever a selected car set the text.
        chosenCarToggle.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            Car selectedCar = (Car) newVal.getUserData();
            descr.setText(selectedCar.getDescription());
        });
        carInfoPopup.getContent().addAll(msgBubble, textMsg);
        carInfoLink.setOnAction(actionEvent -> {
            Bounds linkBounds = carInfoLink.localToScreen(carInfoLink.getBoundsInLocal());
            carInfoPopup.show(carInfoLink, linkBounds.getMaxX(), linkBounds.getMinY() -10);
        });
        return carInfoLink;
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
