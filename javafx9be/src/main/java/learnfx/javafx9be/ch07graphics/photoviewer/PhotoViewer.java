package learnfx.javafx9be.ch07graphics.photoviewer;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Slider;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PhotoViewer extends Application {
    private static final List<String> IMG_TYPES = Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".bmp");
    private final static Logger LOGGER = Logger.getLogger(PhotoViewer.class.getName());
    private static final String CSS_FILE = "css/ch07/photo-viewer.css";

    /** Current image view display. */
    protected ImageView currentImageView;
    /** Rotation transform of the image view. */
    protected Rotate rotate = new Rotate();
    /** Color adjustment. */
    protected ColorAdjust colorAdjust = new ColorAdjust();
    /** A mapping of color adjustment type to a bound slider. */
    protected Map<String, Slider> sliderLookupMap = new HashMap<>();
    /** Custom Button panel to view previous and next images. */
    protected ImageViewButtons buttonPanel;
    /** Single threaded service for loading an image. */
    protected ExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void start(Stage stage) {
        stage.setTitle("Photo Viewer");
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 551, 400, Color.BLACK);
        scene.getStylesheets().add(resourceAsUrlString(CSS_FILE));
        stage.setScene(scene);

        // Anchor Pane
        AnchorPane mainContentPane = new AnchorPane();

        // Group is a container to hold the image view
        Group imageGroup = new Group();
        AnchorPane.setTopAnchor(imageGroup, 0.0);
        AnchorPane.setLeftAnchor(imageGroup, 0.0);

        // Current image view
        currentImageView = createImageView(rotate);
        imageGroup.getChildren().add(currentImageView);

        // Custom ButtonPanel (Next, Previous)
        final List<ImageInfo> IMAGE_FILES = new ArrayList<>();
        buttonPanel = new ImageViewButtons(IMAGE_FILES);

        // Create a progress indicator
        ProgressIndicator progressIndicator = createProgressIndicator();

        // layer items. Items that are last are on top
        mainContentPane.getChildren().addAll(imageGroup, buttonPanel, progressIndicator);

        // Create menus File, Rotate, Color adjust menus
        Menu fileMenu = createFileMenu(stage, progressIndicator);
        // todo

        stage.show();
    }

    private Menu createFileMenu(Stage stage, ProgressIndicator progressIndicator) {
        Menu fileMenu = new Menu("File");

        MenuItem loadItem = new MenuItem("_Open");
        loadItem.setMnemonicParsing(true);
        loadItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        // file chooser to open a file
        wireUpLoadMenuItem(loadItem, stage, progressIndicator);

        MenuItem saveAsMenuItem = new MenuItem("Save _As");
        saveAsMenuItem.setMnemonicParsing(true);
        // file chooser to save image as fil
        // todo: continue
        return null;
    }

    protected void wireUpLoadMenuItem(MenuItem menuItem, Stage stage, ProgressIndicator progressIndicator) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("View Pictures");
        chooser.setInitialDirectory(new File(System.getProperty("user.home")));
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.jpg", "*.jpeg", "*.png", "*.bmp", "*.gif"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("JPEG", "*.jpeg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("BMP", "*.bmp"),
                new FileChooser.ExtensionFilter("GIF", "*.gif")
        );
        menuItem.setOnAction(event -> {
            List<File> list = chooser.showOpenMultipleDialog(stage);
            if (list != null) {
                for (File file : list) {
                    try {
                        String url = file.toURI().toURL().toString();
                        if (isValidImageFile(url)) {
                            buttonPanel.addImage(url);
                            loadAndDisplayImage(progressIndicator);
                        }
                    } catch (MalformedURLException e) {
                        LOGGER.log(Level.WARNING, "Cannot get url for file : " + file, e);
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void loadAndDisplayImage(ProgressIndicator progressIndicator) {
        if (buttonPanel.getCurrentIndex() < 0) {
            return;
        }
        final ImageInfo imageInfo = buttonPanel.getCurrentImageInfo();
        progressIndicator.setVisible(true);
        Task<Image> loadImage = createWorker(imageInfo.getUrl());
        loadImage.setOnSucceeded(workerStateEvent -> {
            try {
                currentImageView.setImage(loadImage.get());
                rotateImageView(imageInfo.getDegrees());
                colorAdjust = imageInfo.getColorAdjust();
                currentImageView.setEffect(colorAdjust);
                updateSliders();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            } finally {
                progressIndicator.setVisible(false);
            }
        });
    }

    /** Update the menu items containing slider controls. */
    private void updateSliders() {
        // todo: implement
    }

    private void rotateImageView(double degrees) {
        // todo: implement
    }

    private Task<Image> createWorker(String url) {
        // todo: implement
        return null;
    }

    private boolean isValidImageFile(String url) {
        return IMG_TYPES.stream().anyMatch(t -> url.toLowerCase().endsWith(t));
    }

    private ProgressIndicator createProgressIndicator() {
        ProgressIndicator progress = new ProgressIndicator();
        progress.setVisible(false);
        progress.setMaxSize(100D, 100D);
        return progress;
    }

    private ImageView createImageView(Rotate rotate) {
        ImageView view = new ImageView();
        view.setPreserveRatio(true);
        view.setSmooth(true);
        view.getTransforms().add(rotate);
        return view;
    }

    @SuppressWarnings("SameParameterValue")
    private String resourceAsUrlString(String name) {
        URL url = getClass().getClassLoader().getResource(name);
        if (url == null) {
            throw new IllegalArgumentException("Cannot load resource: " + name);
        }
        return url.toExternalForm();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
