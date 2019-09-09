package learnfx.javafx9be.ch07graphics.photoviewer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PhotoViewer extends Application {
    private static final List<String> IMG_TYPES = Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".bmp");
    private final static Logger LOGGER = Logger.getLogger(PhotoViewer.class.getName());
    private static final String CSS_FILE = "css/ch07/photo-viewer.css";

    /** Current image view display. */
    protected ImageView currentImageView;
    /** Rotation transform of the image view. */
    protected final Rotate rotate = new Rotate();
    /** Color adjustment allowing to change the image's color attributes of the currently displayed image. */
    protected ColorAdjust colorAdjust = new ColorAdjust();
    /** A mapping of color adjustment type to a bound slider. */
    protected final Map<String, Slider> sliderLookupMap = new HashMap<>();
    /** Custom Button panel to view previous and next images. */
    protected ImageViewButtons buttonPanel;
    /** Single threaded service for loading an image. */
    protected final ExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

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
        Menu rotateMenu = createRotateMenu();
        Menu colorAdjustMenu = createColorAdjustMenu();
        MenuBar menuBar = new MenuBar(fileMenu, rotateMenu, colorAdjustMenu);
        root.setTop(menuBar);

        // Create the center content of the root pane (Border)
        // Make sure the center content is under the menu bar
        BorderPane.setAlignment(mainContentPane, Pos.TOP_CENTER);
        root.setCenter(mainContentPane);

        // When nodes are visible they can be repositioned
        stage.setOnShown(event -> wireUpUIBehavior(stage, progressIndicator));

        stage.show();
    }

    private ImageView createImageView(Rotate rotate) {
        ImageView view = new ImageView();
        view.setPreserveRatio(true);
        view.setSmooth(true);
        view.getTransforms().add(rotate);
        return view;
    }

    private ProgressIndicator createProgressIndicator() {
        ProgressIndicator progress = new ProgressIndicator();
        progress.setVisible(false);
        progress.setMaxSize(100D, 100D);
        return progress;
    }

    private Menu createFileMenu(Stage stage, ProgressIndicator progressIndicator) {
        Menu fileMenu = new Menu("File");

        MenuItem loadMenuItem = new MenuItem("_Open");
        loadMenuItem.setMnemonicParsing(true);
        loadMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.SHORTCUT_DOWN));
        wireUpLoadMenuItem(loadMenuItem, stage, progressIndicator);

        MenuItem saveAsMenuItem = new MenuItem("Save _As");
        saveAsMenuItem.setMnemonicParsing(true);
        wireUpSaveMenuItem(saveAsMenuItem, stage);

        MenuItem exitMenuItem = new MenuItem("_Quit");
        exitMenuItem.setMnemonicParsing(true);
        exitMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.SHORTCUT_DOWN));
        exitMenuItem.setOnAction(event -> Platform.exit());

        fileMenu.getItems().addAll(loadMenuItem, saveAsMenuItem, exitMenuItem);
        return fileMenu;
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
        loadImage.setOnFailed(workerStateEvent -> progressIndicator.setVisible(false));
        executorService.submit(loadImage);
    }

    private Task<Image> createWorker(String url) {
        return new Task<>() {
            @Override
            protected Image call() {
                return new Image(url, false);
            }
        };
    }

    private void wireUpSaveMenuItem(MenuItem menuItem, Stage stage) {
        menuItem.setOnAction(event -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Save As");
            chooser.setInitialDirectory(new File(System.getProperty("user.home")));
            File fileSave = chooser.showSaveDialog(stage);
            if (fileSave != null) {
                WritableImage image = currentImageView.snapshot(new SnapshotParameters(), null);
                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", fileSave);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Menu createRotateMenu() {
        Menu rotateMenu = new Menu("Rotate");
        MenuItem rotateLeft = new MenuItem("Rotate 90 Left");
        rotateLeft.setAccelerator(new KeyCodeCombination(KeyCode.LEFT, KeyCombination.SHORTCUT_DOWN));
        wireUpRotateAngleBy(rotateLeft, -90);
        MenuItem rotateRight = new MenuItem("Rotate 90 Right");
        rotateRight.setAccelerator(new KeyCodeCombination(KeyCode.RIGHT, KeyCombination.SHORTCUT_DOWN));
        wireUpRotateAngleBy(rotateRight, 90);
        rotateMenu.getItems().addAll(rotateLeft, rotateRight);
        return rotateMenu;
    }

    private void wireUpRotateAngleBy(MenuItem menuItem, int angleDegrees) {
        menuItem.setOnAction(event -> {
            ImageInfo imageInfo = buttonPanel.getCurrentImageInfo();
            imageInfo.addDegrees(angleDegrees);
            rotateImageView(imageInfo.getDegrees());
        });
    }

    private Menu createColorAdjustMenu() {
        Menu colorAdjustMenu = new Menu("Color Adjust");
        Consumer<Double> hueConsumer = (value) -> colorAdjust.hueProperty().set(value);
        MenuItem hueMenuItem = createSliderMenuItem("Hue", hueConsumer);
        Consumer<Double> saturationConsumer = (value) -> colorAdjust.setSaturation(value);
        MenuItem saturationMenuItem = createSliderMenuItem("Saturation", saturationConsumer);
        Consumer<Double> brightnessConsumer = (value) -> colorAdjust.setBrightness(value);
        MenuItem brightnessMenuItem = createSliderMenuItem("Brightness", brightnessConsumer);
        Consumer<Double> contrastConsumer = (value) -> colorAdjust.setContrast(value);
        MenuItem contrastMenuItem = createSliderMenuItem("Contrast", contrastConsumer);
        MenuItem resetMenuItem = new MenuItem("Restore to Original");
        resetMenuItem.setOnAction(event -> {
            colorAdjust.setHue(0);
            colorAdjust.setContrast(0);
            colorAdjust.setBrightness(0);
            colorAdjust.setSaturation(0);
            updateSliders();
        });
        colorAdjustMenu.getItems().addAll(hueMenuItem, saturationMenuItem,
                brightnessMenuItem, contrastMenuItem, resetMenuItem);
        return colorAdjustMenu;
    }

    private MenuItem createSliderMenuItem(String name, Consumer<Double> valueConsumer) {
        Slider slider = new Slider(-1, 1, 0);
        sliderLookupMap.put(name, slider);
        slider.valueProperty().addListener(ob -> valueConsumer.accept(slider.getValue()));
        Label label = new Label(name, slider);
        label.setContentDisplay(ContentDisplay.LEFT);
        return new CustomMenuItem(label);
    }

    /** Update the menu items containing slider controls. */
    private void updateSliders() {
        sliderLookupMap.forEach((id, slider) -> {
            switch (id) {
                case "Hue": slider.setValue(colorAdjust.getHue()); break;
                case "Brightness": slider.setValue(colorAdjust.getBrightness()); break;
                case "Saturation": slider.setValue(colorAdjust.getSaturation()); break;
                case "Contrast": slider.setValue(colorAdjust.getContrast()); break;
                default:
                    slider.setValue(0);
            }
        });
    }

    private void wireUpUIBehavior(Stage stage, ProgressIndicator progressIndicator) {
        Scene scene = stage.getScene();
        Runnable repositionButtonPanel = () -> {
            buttonPanel.setTranslateX(scene.getWidth() - 75);
            buttonPanel.setTranslateY(scene.getHeight() - 75);
        };
        Runnable repositionProgressIndicator = () -> {
            progressIndicator.setTranslateX(scene.getWidth() / 2 - progressIndicator.getWidth() - 2);
            progressIndicator.setTranslateY(scene.getHeight() / 2 - progressIndicator.getHeight() - 2);
        };
        Runnable repositionCode = () -> {
            repositionButtonPanel.run();
            repositionProgressIndicator.run();
        };
        scene.widthProperty().addListener(ov -> repositionCode.run());
        scene.heightProperty().addListener(ov -> repositionCode.run());
        repositionCode.run();
        currentImageView.fitWidthProperty().bind(scene.widthProperty());
        Runnable viewPreviousAction = () -> {
            if (buttonPanel.isAtBeginning()) {
                return;
            } else {
                buttonPanel.goPrevious();
            }
            loadAndDisplayImage(progressIndicator);
        };
        buttonPanel.setLeftButtonAction( mouseEvent -> viewPreviousAction.run());
        scene.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (keyEvent.getCode() == KeyCode.LEFT && !keyEvent.isShortcutDown()) {
                viewPreviousAction.run();
            }
        });
        Runnable viewNextAction = () -> {
            if (buttonPanel.isAtEnd()) {
                return;
            } else {
                buttonPanel.goNext();
            }
            loadAndDisplayImage(progressIndicator);
        };
        buttonPanel.setRightButtonAction( mouseEvent -> viewNextAction.run());
        scene.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            if (keyEvent.getCode() == KeyCode.RIGHT && !keyEvent.isShortcutDown()) {
                viewNextAction.run();
            }
        });
        setupDragNDrop(stage, progressIndicator);
    }

    private void setupDragNDrop(Stage stage, ProgressIndicator progressIndicator) {
        Scene scene = stage.getScene();
        scene.setOnDragOver((DragEvent event) -> {
            Dragboard db = event.getDragboard();
            if (db.hasFiles() || (db.hasUrl() && isValidImageFile(db.getUrl()))) {
                LOGGER.log(Level.INFO, "url " + db.getUrl());
                event.acceptTransferModes(TransferMode.LINK);
            } else {
                event.consume();
            }
        });
        scene.setOnDragDropped((DragEvent event) -> {
            Dragboard db = event.getDragboard();
            if (db.hasFiles() && !db.hasUrl()) {
                db.getFiles().forEach(file -> {
                    try {
                        String url = file.toURI().toURL().toString();
                        if (isValidImageFile(url)) {
                            buttonPanel.addImage(url);
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                });
            } else {
                String url = db.getUrl();
                LOGGER.log(Level.FINE, "dropped url: " + db.getUrl());
                if (isValidImageFile(url)) {
                    buttonPanel.addImage(url);
                }
            }
            loadAndDisplayImage(progressIndicator);
            event.setDropCompleted(true);
            event.consume();
        });
    }

    private void rotateImageView(double degrees) {
        rotate.setPivotX(currentImageView.getFitWidth() / 2);
        rotate.setPivotY(currentImageView.getFitHeight() / 2);
        rotate.setAngle(degrees);
    }

    private boolean isValidImageFile(String url) {
        return IMG_TYPES.stream().anyMatch(t -> url.toLowerCase().endsWith(t));
    }

    @SuppressWarnings("SameParameterValue")
    private String resourceAsUrlString(String name) {
        URL url = getClass().getClassLoader().getResource(name);
        if (url == null) {
            throw new IllegalArgumentException("Cannot load resource: " + name);
        }
        return url.toExternalForm();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        executorService.shutdown();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
