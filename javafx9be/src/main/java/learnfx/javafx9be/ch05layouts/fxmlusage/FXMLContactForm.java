package learnfx.javafx9be.ch05layouts.fxmlusage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class FXMLContactForm extends Application {
    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("FXMLContactForm");
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/ContactForm.fxml"));
            Scene scene = new Scene(root, 380, 150, Color.WHITE);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
