package learnfx.javafx9be.ch03fund;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.*;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class PaintingColors extends Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("Painting Colors");
        Group root = new Group();
        Scene scene = new Scene(root, 350, 300, Color.WHITE);

        // Red ellipse with radial gradient color
        Ellipse ellipse = new Ellipse(100, 50 + 70 / 2.0, 50, 70 / 2.0);
        RadialGradient gradient1 = new RadialGradient(0, .1, 80, 45, 120,
                false, CycleMethod.NO_CYCLE, new Stop(0, Color.RED), new Stop(1, Color.BLACK)
        );
        ellipse.setFill(gradient1);
        root.getChildren().add(ellipse);
        double ellipseHeight = ellipse.getBoundsInParent().getHeight();

        // thick black line behind second shape
        Line blackLine = new Line();
        blackLine.setStartX(170);
        blackLine.setStartY(30);
        blackLine.setEndX(20);
        blackLine.setEndY(140);
        blackLine.setFill(Color.BLACK);
        blackLine.setStrokeWidth(10.0f);
        blackLine.setTranslateY(ellipseHeight + 10);
        root.getChildren().add(blackLine);

        // A rectangle filled with a linear gradient with a translucent color.
        Rectangle rectangle = new Rectangle();
        rectangle.setX(50);
        rectangle.setY(50);
        rectangle.setWidth(100);
        rectangle.setHeight(70);
        rectangle.setTranslateY(ellipseHeight + 10);
        LinearGradient linearGrad = new LinearGradient(0, 0, 0, 1,
                true, CycleMethod.NO_CYCLE,
                new Stop(0.1f, Color.rgb(255, 200, 0, .784)),
                new Stop(1.0f, Color.rgb(0, 0, 0, .784)));
        rectangle.setFill(linearGrad);
        root.getChildren().add(rectangle);

        // A rectangle filled with a linear gradient with a reflective cycle.
        Rectangle roundRect = new Rectangle();
        roundRect.setX(50);
        roundRect.setY(50);
        roundRect.setWidth(100);
        roundRect.setHeight(70);
        roundRect.setArcWidth(20);
        roundRect.setArcHeight(20);
        roundRect.setTranslateY(ellipseHeight + 10 + rectangle.getHeight() + 10);
        LinearGradient cycleGrad = new LinearGradient(50, 50, 70, 70,
                false, CycleMethod.REFLECT,
                new Stop(0f, Color.rgb(0, 255, 0, .784)),
                new Stop(1.0f, Color.rgb(0, 0, 0, .784))
        );
        roundRect.setFill(cycleGrad);
        root.getChildren().add(roundRect);

        stage.setScene(scene);
        stage.show();
    }
}
