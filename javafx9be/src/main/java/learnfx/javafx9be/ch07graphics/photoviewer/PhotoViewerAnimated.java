package learnfx.javafx9be.ch07graphics.photoviewer;

import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.util.Duration;

import java.util.concurrent.ExecutionException;

public class PhotoViewerAnimated extends PhotoViewer {

    @Override
    protected void loadAndDisplayImage(ProgressIndicator progressIndicator) {
        if (buttonPanel.getCurrentIndex() < 0) {
            return;
        }
        final ImageInfo imageInfo = buttonPanel.getCurrentImageInfo();
        progressIndicator.setVisible(true);
        Task<Image> loadImage = createWorker(imageInfo.getUrl());
        loadImage.setOnSucceeded(workerStateEvent -> {
            try {
                Image nextImage = loadImage.get();
                SequentialTransition fadeIntoNext = transitionByFading(nextImage, imageInfo);
                fadeIntoNext.playFromStart();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            } finally {
                progressIndicator.setVisible(false);
            }
        });
        loadImage.setOnFailed(workerStateEvent -> progressIndicator.setVisible(false));
        executorService.submit(loadImage);
    }

    private SequentialTransition transitionByFading(Image nextImage, ImageInfo imageInfo) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), currentImageView);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(actionEvent -> {
            currentImageView.setImage(nextImage);
            rotateImageView(imageInfo.getDegrees());
            colorAdjust = imageInfo.getColorAdjust();
            currentImageView.setEffect(colorAdjust);
            updateSliders();
        });
        FadeTransition fadeIn = new FadeTransition(Duration.millis(500), currentImageView);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        return new SequentialTransition(fadeOut, fadeIn);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
