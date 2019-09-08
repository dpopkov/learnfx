package learnfx.javafx9be.ch07graphics;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class LoadingImagesExample extends Application {
    private HBox imageBox;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Loading Images Example");
        BorderPane root = new BorderPane();
        HBox box = new HBox(10);
        box.setPadding(new Insets(10));
        box.getChildren().addAll(
                makeButton("Load local image", event -> loadLocalImage()),
                makeButton("Load class path image", event -> loadClassPathImage()),
                makeButton("Load remote image", event -> loadRemoteImage())
        );
        root.setTop(box);

        imageBox = new HBox(10);
        imageBox.setPadding(new Insets(10));
        root.setCenter(imageBox);

        Scene scene = new Scene(root, 900, 300);
        stage.setScene(scene);
        stage.show();
    }

    private Button makeButton(String text, EventHandler<ActionEvent> handler) {
        Button button = new Button(text);
        button.setOnAction(handler);
        return button;
    }

    private void loadLocalImage() {
        try {
            File file = new File("img/ch06controls/BackgroundProcesses_1.png");
            String localUrl = file.toURI().toURL().toString();
            Image localImage = new Image(localUrl, true);
            addImage(localImage);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void loadClassPathImage() {
        final String name = "images/ch06/sportscar.png";
        URL resource = LoadingImagesExample.class.getClassLoader().getResource(name);
        if (resource != null) {
            String urlStr = resource.toExternalForm();
            Image classPathUrlImage = new Image(urlStr, true);
            addImage(classPathUrlImage);
        } else {
            System.err.println("Cannot load resource: " + name);
        }
    }

    private void loadRemoteImage() {
        String remoteUrl = "https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_92x30dp.png";
        Image remoteImage = new Image(remoteUrl, true);
        addImage(remoteImage);
    }

    private void addImage(Image localImage) {
        ImageView view = new ImageView(localImage);
        imageBox.getChildren().add(view);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
