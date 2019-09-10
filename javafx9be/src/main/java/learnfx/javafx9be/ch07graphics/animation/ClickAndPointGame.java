package learnfx.javafx9be.ch07graphics.animation;

import javafx.animation.*;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ClickAndPointGame extends Application {
    private static final String SVG_FOLDER = "svg/ch07/game-assets/";

    @Override
    public void start(Stage stage) {
        stage.setTitle("Click And Point Game");
        AnchorPane root = new AnchorPane();
        Scene scene = new Scene(root, 551, 400, Color.WHITE);

        SVGPath plane = createSvgPath("plane-svg-path.txt");
        SVGPath windmill = createSvgPath("windmill-svg-path.txt");
        AnchorPane.setBottomAnchor(windmill, 50.0);
        AnchorPane.setRightAnchor(windmill, 100.0);
        SVGPath rotorBlades = createSvgPath("rotor-blades-svg-path.txt");
        AnchorPane.setBottomAnchor(rotorBlades, 58.0);
        AnchorPane.setRightAnchor(rotorBlades, 86.0);
        SVGPath cloud1 = createSvgPath("cloud-svg-path.txt");

        Path flightPath = new Path();
        PathElement startPath = new MoveTo(-200, 100);
        QuadCurveTo quadCurveTo = new QuadCurveTo(100, -50, 500, 100);
        flightPath.getElements().addAll(startPath, quadCurveTo);
        flightPath.setVisible(false);

        PathTransition flyPlane = new PathTransition(Duration.millis(8000), flightPath, plane);
        flyPlane.setCycleCount(Animation.INDEFINITE);
        flyPlane.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);

        RotateTransition rotateBlade = new RotateTransition(Duration.millis(8000), rotorBlades);
        rotateBlade.setCycleCount(Animation.INDEFINITE);
        rotateBlade.setFromAngle(0);
        rotateBlade.setToAngle(360);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(500), plane);
        scaleTransition.setCycleCount(4);
        scaleTransition.setAutoReverse(true);
        scaleTransition.setFromX(1);
        scaleTransition.setFromY(1);
        scaleTransition.setByX(1.5);
        scaleTransition.setByY(1.5);

        TranslateTransition moveCloud = new TranslateTransition(Duration.seconds(15), cloud1);
        moveCloud.setFromX(-200);
        moveCloud.setFromY(100);
        moveCloud.setCycleCount(Animation.INDEFINITE);
        moveCloud.setAutoReverse(true);
        moveCloud.setToX(scene.getWidth() + 200);

        FadeTransition fadeCloud = new FadeTransition(Duration.millis(1000), cloud1);
        fadeCloud.setCycleCount(4);
        fadeCloud.setFromValue(1);
        fadeCloud.setToValue(0);
        fadeCloud.setOnFinished(actionEvent -> cloud1.setOpacity(1));

        scene.widthProperty().addListener( observable -> {
            quadCurveTo.setControlX(scene.getWidth()/2);
            quadCurveTo.setX(scene.getWidth() + 200);
            flyPlane.playFromStart();
            moveCloud.setToX(scene.getWidth() + 200);
            moveCloud.playFromStart();
        });

        scene.setOnMouseClicked( mouseEvent -> {
            boolean isCloudClicked = cloud1.getBoundsInParent().contains(mouseEvent.getX(), mouseEvent.getY());
            if (isCloudClicked) {
                if (fadeCloud.getStatus() == Animation.Status.STOPPED) {
                    fadeCloud.playFromStart();
                }
            }
            boolean isPlaneClicked = plane.getBoundsInParent().contains(mouseEvent.getX(), mouseEvent.getY());
            if (isPlaneClicked) {
                if (scaleTransition.getStatus() == Animation.Status.STOPPED) {
                    scaleTransition.playFromStart();
                }
            }
        });

        root.getChildren().addAll(flightPath, plane, cloud1, windmill, rotorBlades);
        stage.setScene(scene);
        stage.setOnShowing( windowEvent -> {
            quadCurveTo.setControlX(scene.getWidth()/2);
            quadCurveTo.setX(scene.getWidth() + 200);
            flyPlane.playFromStart();
            rotateBlade.playFromStart();
            moveCloud.playFromStart();
        });
        stage.show();
    }

    private SVGPath createSvgPath(String fileName) {
        SVGPath svgPath = new SVGPath();
        Task<String> svgLoadWorker = createSvgLoadWorker(SVG_FOLDER + fileName);
        svgLoadWorker.setOnSucceeded(stateEvent -> svgPath.setContent(svgLoadWorker.getValue()));
        new Thread(svgLoadWorker).start();
        return svgPath;
    }

    private Task<String> createSvgLoadWorker(String pathDataUrl) {
        return new Task<>() {
            @Override
            protected String call() throws Exception {
                String pathData = null;
                try (InputStream in = this.getClass().getClassLoader().getResourceAsStream(pathDataUrl)) {
                    if (in != null) {
                        pathData = new Scanner(in, StandardCharsets.UTF_8).useDelimiter("\\A").next();
                    }
                }
                return pathData;
            }
        };
    }
}
