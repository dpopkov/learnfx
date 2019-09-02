package learnfx.javafx9be.ch06controls;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RadioButtonsExample extends Application {
    private VBox box;
    private ToggleGroup paymentGroup;

    @Override
    public void start(Stage stage) {
        stage.setTitle("RadioButtons Example");
        box = new VBox(5);
        box.setPadding(new Insets(20));
        box.getChildren().add(new Label("What is your method of payment?"));
        paymentGroup = new ToggleGroup();
        addRadioButton("Visa", true);
        addRadioButton("PayPal");
        addRadioButton("Master Card");
        addRadioButton("BitCoin");
        addRadioButton("Cash");
        paymentGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) ->
                System.out.println("Payment type: " + newValue.getUserData()));
        Scene scene = new Scene(box, 300, 200);
        stage.setScene(scene);
        stage.show();
    }

    private void addRadioButton(String text) {
        addRadioButton(text, false);
    }

    private void addRadioButton(String text, boolean selected) {
        RadioButton radioButton = new RadioButton(text);
        radioButton.setSelected(selected);
        radioButton.setUserData(text);
        radioButton.setToggleGroup(paymentGroup);
        box.getChildren().add(radioButton);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
