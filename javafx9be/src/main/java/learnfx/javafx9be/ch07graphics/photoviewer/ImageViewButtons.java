package learnfx.javafx9be.ch07graphics.photoviewer;

import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;

import java.util.List;

/**
 * A simple custom control that manages a list of string URLs mapped to image files
 */
public class ImageViewButtons extends Pane {
    /** The current index into the IMAGE_FILES list. */
    private int currentIndex = -1;
    /** Enumeration of next and previous button directions. */
    public enum ButtonMove {
        NEXT,
        PREV
    }
    /** List of ImageInfo instances. */
    private List<ImageInfo> imageFiles;

    private Pane leftButton;
    private Pane rightButton;

    public ImageViewButtons(List<ImageInfo> imageFiles) {
        this.imageFiles = imageFiles;
        initButtons();
        addButtonsToPane();
    }

    private void initButtons() {
        leftButton = makeArrowButton("left-arrow",
                new Arc(0, 12, 15, 15, -30, 60));
        rightButton = makeArrowButton("right-arrow",
                new Arc(15, 12, 15, 15, 180 - 30, 60));
        HBox.setHgrow(leftButton, Priority.ALWAYS);
        HBox.setHgrow(rightButton, Priority.ALWAYS);
        HBox.setMargin(leftButton, new Insets(0, 5, 0, 5));
        HBox.setMargin(rightButton, new Insets(0, 5, 0, 5));
    }

    private void addButtonsToPane() {
        HBox box = new HBox();
        box.getStyleClass().add("button-panel");
        box.getChildren().addAll(leftButton, rightButton);

        Pane buttonStackPane = new StackPane();
        buttonStackPane.getStyleClass().add("button-pane");
        buttonStackPane.getChildren().add(box);
        getChildren().add(buttonStackPane);
    }

    private Pane makeArrowButton(String style, Arc arc) {
        Pane button = new Pane();
        button.getChildren().add(arc);
        arc.setType(ArcType.ROUND);
        arc.getStyleClass().add(style);
        return button;
    }

    public boolean isAtBeginning() {
        return currentIndex == 0;
    }

    public boolean isAtEnd() {
        return currentIndex == imageFiles.size() - 1;
    }

    public void goPrevious() {
        gotImageIndex(ButtonMove.PREV);
    }

    public void goNext() {
        gotImageIndex(ButtonMove.NEXT);
    }

    private void gotImageIndex(ButtonMove direction) {
        int size = imageFiles.size();
        if (size == 0) {
            currentIndex = -1;
        } else {
            if (direction == ButtonMove.NEXT && currentIndex < size - 1) {
                currentIndex++;
            } else if (direction == ButtonMove.PREV && currentIndex > 0) {
                currentIndex--;
            }
        }
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public ImageInfo getCurrentImageInfo() {
        return imageFiles.get(getCurrentIndex());
    }

    /**
     * Adds the URL string representation of the path to the image file.
     * Based on a URL the method will check if it matches supported image format.
     * @param url string representation of the path to the image file.
     */
    public void addImage(String url) {
        currentIndex++;
        imageFiles.add(currentIndex, new ImageInfo(url));
    }
}
