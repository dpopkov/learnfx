package learnfx.javafx9be.ch09media;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class PlayingAudio extends Application {
    private MediaPlayer mediaPlayer;
    private Point2D anchorPt;
    private Point2D previousLocation;
    private ChangeListener<Duration> progressListener;
    private BooleanProperty playAndPauseToggle = new SimpleBooleanProperty(true);
    private EventHandler<MouseEvent> mouseEventConsumer = Event::consume;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // Remove native window borders and title bar
        stage.initStyle(StageStyle.TRANSPARENT);

        // Create the application surface or background
        Pane root = new AnchorPane();
        root.setId("app-surface");
        Scene scene = new Scene(root, 551, 270, Color.rgb(0, 0, 0, 0));
        scene.getStylesheets().add(getClass().getResource("/css/ch09/playing-audio.css").toExternalForm());
        stage.setScene(scene);

        // Initialize stage to be movable via mouse
        initMovablePlayer(stage);

        // Create a Path instance for the area chart
        Path chartArea = new Path();
        chartArea.setId("chart-area");

        // Create the button panel (stop, play and pause)
        Node buttonPanel = createButtonPanel(root);
        AnchorPane.setRightAnchor(buttonPanel, 3.0);
        AnchorPane.setBottomAnchor(buttonPanel, 3.0);

        // Create a slider for our progress and seek control
        Slider progressSlider = createSlider();
        AnchorPane.setLeftAnchor(progressSlider, 2.0);
        AnchorPane.setBottomAnchor(progressSlider, 2.0);

        // Updates slider as audio/video is progressing (play)
        progressListener = ((observable, oldValue, newValue) -> progressSlider.setValue(newValue.toSeconds()));

        // Initializing Scene to accept files
        // using drag and dropping over the surface to load media
        initFileDragNDrop(root);

        // Create the close button
        Node closeButton = createCloseButton();
        AnchorPane.setRightAnchor(closeButton, 2.0);
        AnchorPane.setTopAnchor(closeButton, 2.0);

        root.getChildren().addAll(chartArea, buttonPanel, progressSlider, closeButton);
        stage.centerOnScreen();
        stage.show();
    }

    private Node createCloseButton() {
        // todo: implement
        return null;
    }

    private void initFileDragNDrop(Pane root) {
        // todo: implement
    }

    private Slider createSlider() {
        // todo: implement
        return null;
    }

    private Node createButtonPanel(Pane root) {
        // todo: implement
        return null;
    }

    private void initMovablePlayer(Stage stage) {
        // todo: implement
    }
}
