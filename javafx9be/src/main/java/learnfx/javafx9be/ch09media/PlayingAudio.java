package learnfx.javafx9be.ch09media;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Slider;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.net.MalformedURLException;

import static javafx.scene.media.MediaPlayer.*;

public class PlayingAudio extends Application {
    private MediaPlayer mediaPlayer;
    private Point2D anchorPt;
    private Point2D previousLocation;
    private ChangeListener<Duration> progressListener;
    private final BooleanProperty playAndPauseToggle = new SimpleBooleanProperty(true);
    private final EventHandler<MouseEvent> mouseEventConsumer = Event::consume;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
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

    /**
     * Initialize the stage to allow the mouse cursor to move the application using dragging.
     * @param primaryStage the applications primary stage window
     */
    private void initMovablePlayer(Stage primaryStage) {
        Scene scene = primaryStage.getScene();
        Pane root = (Pane) scene.getRoot();
        root.setPickOnBounds(true);
        // starting initial anchor point
        root.setOnMousePressed(mouseEvent -> anchorPt = new Point2D(mouseEvent.getSceneX(), mouseEvent.getScreenY()));
        // Dragging the stage by moving itx x, y
        // based on the previous location.
        root.setOnMouseDragged(mouseEvent -> {
            if (anchorPt != null && previousLocation != null) {
                primaryStage.setX(previousLocation.getX() + mouseEvent.getSceneX() - anchorPt.getX());
                primaryStage.setY(previousLocation.getY() + mouseEvent.getSceneY() - anchorPt.getY());
            }
        });
        // Set the new previous to the current mouse x, y coordinate
        root.setOnMouseReleased(mouseEvent -> previousLocation = new Point2D(primaryStage.getX(), primaryStage.getY()));
        // Initialize previous location after stage is shown
        primaryStage.addEventHandler(WindowEvent.WINDOW_SHOWN,
                (WindowEvent t) -> previousLocation = new Point2D(primaryStage.getX(), primaryStage.getY()));
    }

    /**
     * Creates a node containing the audio player's stop, pause and play buttons.
     * @param root the scene graph's root pane
     * @return a button panel having play, pause and stop buttons
     */
    private Node createButtonPanel(Pane root) {
        // create button control panel
        FlowPane buttonPanel = new FlowPane();
        buttonPanel.setId("button-panel");
        // stop button control
        Node stopButton = new Rectangle(10, 10);
        stopButton.setId("stop-button");
        stopButton.setOnMouseClicked(mouseEvent -> {
            if (mediaPlayer != null) {
                updatePlayAndPauseButtons(true, root);
                if (mediaPlayer.getStatus() == Status.PLAYING || mediaPlayer.getStatus() == Status.PAUSED) {
                    mediaPlayer.stop();
                }
                playAndPauseToggle.set(false);
            }
        });
        // Toggle Button containing a Play and Pause button
        StackPane playPauseToggleButton = new StackPane();
        // Play button control
        Arc playButton = new Arc(12, 16, 15, 15, 150, 60);
        playButton.setId("play-button");
        playButton.setType(ArcType.ROUND);
        playButton.setMouseTransparent(true);
        // Pause control
        Group pauseButton = new Group();
        pauseButton.setId("pause-button");
        Circle pauseBackground = new Circle(12, 16, 10);
        pauseBackground.getStyleClass().add("pause-circle");
        Line firstLine = new Line(6, 6, 6, 14);
        firstLine.getStyleClass().addAll("pause-line", "first-line");
        Line secondLine = new Line(6, 6, 6, 14);
        secondLine.getStyleClass().addAll("pause-line", "second-line");
        pauseButton.getChildren().addAll(pauseBackground, firstLine, secondLine);
        pauseButton.setMouseTransparent(true);
        playPauseToggleButton.getChildren().addAll(playButton, pauseButton);
        playPauseToggleButton.setOnMouseEntered(mouseEvent -> {
            Color red = Color.rgb(255, 0, 0, .90);
            pauseBackground.setStroke(red);
            firstLine.setStroke(red);
            secondLine.setStroke(red);
            playButton.setStroke(red);
        });
        playPauseToggleButton.setOnMouseExited(mouseEvent -> {
            Color white = Color.rgb(255, 255, 255, .90);
            pauseBackground.setStroke(white);
            firstLine.setStroke(white);
            secondLine.setStroke(white);
            playButton.setStroke(white);
        });
        // Boolean property toggling the playing and pausing the media player
        playAndPauseToggle.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                if (mediaPlayer != null) {
                    updatePlayAndPauseButtons(false, root);
                    mediaPlayer.play();
                }
            } else {
                if (mediaPlayer!=null) {
                    updatePlayAndPauseButtons(true, root);
                    if (mediaPlayer.getStatus() == Status.PLAYING) {
                        mediaPlayer.pause();
                    }
                }
            }
        });
        // Press toggle button
        playPauseToggleButton.setOnMouseClicked( mouseEvent ->{
            if (mouseEvent.getClickCount() == 1) {
                playAndPauseToggle.set(!playAndPauseToggle.get());
            }
        });
        buttonPanel.getChildren().addAll(stopButton, playPauseToggleButton);
        buttonPanel.setPrefWidth(50);
        // Filter out to prevent the root node to receive mouse events to drag the window around.
        buttonPanel.addEventHandler(MouseEvent.ANY, mouseEventConsumer);
        return buttonPanel;
    }

    /**
     * Sets play button visible and pause button not visible when playVisible is true otherwise the opposite.
     * @param playVisible - value of true the play becomes visible and pause non visible, otherwise the opposite.
     * @param root the root node (AnchorPane)
     */
    private void updatePlayAndPauseButtons(boolean playVisible, Pane root) {
        Node playButton = root.lookup("#play-button");
        Node pauseButton = root.lookup("#pause-button");
        if (playVisible) {
            playButton.toFront();
            playButton.setVisible(true);
            pauseButton.setVisible(false);
            pauseButton.toBack();
        } else {
            pauseButton.toFront();
            pauseButton.setVisible(true);
            playButton.setVisible(false);
            playButton.toBack();
        }
    }

    /**
     * A position slider to seek backward and forward that is bound to a media player control.
     * @return slider control bound to media player.
     */
    private Slider createSlider() {
        Slider slider = new Slider(0, 100, 1);
        slider.setId("seek-position-slider");
        slider.valueProperty().addListener((observable) -> {
            if (slider.isValueChanging()) {
                if (mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
                    double durationMillis = slider.getValue() * 1000;
                    mediaPlayer.seek(Duration.millis(durationMillis));
                }
            }
        });
        return slider;
    }

    /**
     * Initialize the Drag and Drop ability for media files.
     * @param root the scene graph's root pane
     */
    private void initFileDragNDrop(Pane root) {
        // Drag over surface
        root.setOnDragOver(dragEvent -> {
            Dragboard db = dragEvent.getDragboard();
            if (db.hasFiles() || db.hasUrl()) {
                dragEvent.acceptTransferModes(TransferMode.LINK);
            } else {
                dragEvent.consume();
            }
        });
        // Dropping over surface
        root.setOnDragDropped(dragEvent -> {
            Dragboard db = dragEvent.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                if (db.getFiles().size() > 0) {
                    try {
                        String filePath = db.getFiles().get(0).toURI().toURL().toString();
                        playMedia(filePath, root);
                        success = true;
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                // audio file from some host or jar
                playMedia(db.getUrl(), root);
                success = true;
            }
            dragEvent.setDropCompleted(success);
            dragEvent.consume();
        });
    }

    /**
     * After a file is dragged onto the application a new media player instance is created with a media file
     * @param url the url pointing to an audio file
     * @param root the scene graph's root pane
     */
    private void playMedia(String url, Pane root) {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            mediaPlayer.setOnPaused(null);
            mediaPlayer.setOnPlaying(null);
            mediaPlayer.setOnReady(null);
            mediaPlayer.currentTimeProperty().removeListener(progressListener);
            mediaPlayer.setAudioSpectrumListener(null);
        }
        // create a new media player
        Media validMedia;
        try {
            validMedia = new Media(url);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> e.printStackTrace());
            return;
        }
        final Media media = validMedia;
        mediaPlayer = new MediaPlayer(media);
        // as the media is playing move the slider for progress
        mediaPlayer.currentTimeProperty().addListener(progressListener);
        mediaPlayer.setOnReady(() -> {
            // display media's metadata
            media.getMetadata().forEach((name, val) -> System.out.println(name + ": " + val));
            updatePlayAndPauseButtons(false, root);
            Slider progressSlider = (Slider) root.lookup("#seek-position-slider");
            progressSlider.setValue(0);
            progressSlider.setMax(mediaPlayer.getMedia().getDuration().toSeconds());
            mediaPlayer.play();
        });
        // Rewind back to the beginning
        mediaPlayer.setOnEndOfMedia(() -> {
            updatePlayAndPauseButtons(true, root);
            mediaPlayer.stop();
            playAndPauseToggle.set(false);
        });

        // Obtain chart path area
        Path chartArea = (Path) root.lookup("#chart-area");
        int chartPadding = 5;
        // The frequency domain is the X Axis. The freqAxisY is the Y coordinate of the freq axis.
        double freqAxisY = root.getHeight() - 45;
        // The chart's height
        double chartHeight = freqAxisY - chartPadding;
        // In the CSS file the padding is set with the following:
        // -fx-border-insets: 6 6 6 6; (top, right, bottom, left)
        // -fx-border-width: 1.5;
        // Below the padding will equal 7.5 which is the union of
        // the inset 6 (left) and border width of 1.5)
        double padding = root.getBorder().getInsets().getLeft();
        double chartWidth = root.getWidth() - ((2 * padding) + (2 * chartPadding));
        // Audio sound is in decibels and the magnitude is from 0 to -60
        // Squaring the magnitudes stretches the plot. Also dividing into
        // the chart height will normalize or keep the y coordinate within the
        // chart bounds.
        double scaleY = chartHeight / (60 * 60);
        double space = 5; // between each data point. Help

        mediaPlayer.setAudioSpectrumListener(
            (double timestamp, double duration, float[] magnitudes, float[] phases) -> {
                if (mediaPlayer.getStatus() == Status.PAUSED || mediaPlayer.getStatus() == Status.STOPPED) {
                    return;
                }
                double freqBarX = padding + chartPadding;   // The freqBarX is the x coordinate to plot
                // The scaleX is one unit. This keeps the plotting within the chart width area
                double scaleX = chartWidth / (magnitudes.length * space);
                // Checks if the data array is created.
                // If not create the number of path components to be
                // added to the chartArea path. The check of the size minus 3 is excluding
                // the first MoveTo element, and the last two elements LineTo, and ClosePath
                // respectively.
                if ((chartArea.getElements().size() - 3) != magnitudes.length) {
                    chartArea.getElements().clear();    // Move to bottom left of chart.
                    chartArea.getElements().add(new MoveTo(freqBarX, freqAxisY));
                    // Update all LineTo elements to draw the line chart
                    for (float magnitude : magnitudes) {
                        double dB = magnitude * magnitude;
                        dB = chartHeight - dB * scaleY;
                        chartArea.getElements().add(new LineTo(freqBarX, freqAxisY - dB));
                        freqBarX += (scaleX * space);
                    }
                    // Close the path by adding LineTo to the bottom right
                    // of the chart and close path to form an shape.
                    chartArea.getElements().add(new LineTo(freqBarX, freqAxisY));
                    chartArea.getElements().add(new ClosePath());
                } else {
                    // If elements already created go through and update path elements
                    int idx = 0;
                    for (float magnitude : magnitudes) {
                        double dB = magnitude * magnitude;
                        dB = chartHeight - dB * scaleY;
                        // skip first MoveTo element in path.
                        idx++;
                        // update elements with a x and y
                        LineTo dataPoint = (LineTo) chartArea.getElements().get(idx);
                        dataPoint.setX(freqBarX);
                        dataPoint.setY(freqAxisY - dB);
                        freqBarX += (scaleX * space);
                    }
                }
            });
    }

    /**
     * The close button to exit application.
     * @return node representing a close button
     */
    private Node createCloseButton() {
        StackPane closeButton = new StackPane();
        closeButton.setId("close-button");

        Node closeBackground = new Circle(0, 0, 7);
        closeBackground.setId("close-circle");
        SVGPath closeXMark = new SVGPath();
        closeXMark.setId("close-x-mark");
        closeXMark.setContent("M 0 0 L 6 6 M 6 0 L 0 6");
        closeButton.getChildren().addAll(closeBackground, closeXMark);
        // exit app
        closeButton.setOnMouseClicked(mouseEvent -> {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
            Platform.exit();
        });
        // Filter mouse events from propagating to the parent.
        closeButton.addEventHandler(MouseEvent.ANY, mouseEventConsumer);
        return closeButton;
    }
}
