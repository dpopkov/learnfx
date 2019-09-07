package learnfx.javafx9be.ch07graphics;

import javafx.application.Application;
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

        Button loadLocalButton = new Button("Load local image");
        loadLocalButton.setOnAction(event -> loadLocalImage());
        Button loadClassPathImage = new Button("Load class path image");
        loadClassPathImage.setOnAction(event -> loadClassPathImage());
        Button loadRemoteButton = new Button("Load remote image");
        loadRemoteButton.setOnAction(event -> loadRemoteImage());

        box.getChildren().addAll(loadLocalButton, loadClassPathImage, loadRemoteButton);
        root.setTop(box);

        imageBox = new HBox(10);
        imageBox.setPadding(new Insets(10));
        root.setCenter(imageBox);

        Scene scene = new Scene(root, 900, 300);
        stage.setScene(scene);
        stage.show();
    }

    private void loadLocalImage() {
        try {
            File file = new File("img/ch06controls/BackgroundProcesses_1.png");
            String localUrl = file.toURI().toURL().toString();
            System.out.print("localUrl = " + localUrl);
            System.out.println("  exists: " + file.exists());
            Image localImage = new Image(localUrl, true);
            System.out.println("Image " + localUrl + " loading");
            ImageView view = new ImageView(localImage);
            imageBox.getChildren().add(view);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    private void loadClassPathImage() {
        final String name = "images/ch06/sportscar.png";
        URL resource = LoadingImagesExample.class.getClassLoader().getResource(name);
        if (resource != null) {
            String urlStr = resource.toExternalForm();
            System.out.println("urlStr   = " + urlStr);
            Image classPathUrlImage = new Image(urlStr, true);
            System.out.println("Image " + urlStr + " loading");
            ImageView view = new ImageView(classPathUrlImage);
            imageBox.getChildren().add(view);
        } else {
            System.err.println("Cannot load resource: " + name);
        }
    }

    @SuppressWarnings("unused")
    private void loadRemoteImage() {
        String remoteUrl = "https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_92x30dp.png";
        Image remoteImage = new Image(remoteUrl, true);
        System.out.println("Image " + remoteUrl + " loading");
        ImageView view = new ImageView(remoteImage);
        imageBox.getChildren().add(view);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
