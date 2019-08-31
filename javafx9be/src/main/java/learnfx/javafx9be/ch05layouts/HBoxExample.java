package learnfx.javafx9be.ch05layouts;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class HBoxExample extends Application {
    private static final int RECT_MODULE = 20;
    private static final int SPACING = RECT_MODULE / 2;

    @Override
    public void start(Stage stage) {
        stage.setTitle("HBox Example");
        Group root = new Group();
        Scene scene = new Scene(root, 320, 150);

        // pixels space between child nodes
        HBox hBox = new HBox(SPACING);
         // The border is blue, dashed, 0% radius for all corners, a width of 2 pixels
        BorderStroke[] borderStrokes = new BorderStroke[]{
                new BorderStroke(Color.BLUE, BorderStrokeStyle.DASHED,
                        new CornerRadii(0.0, true), new BorderWidths(2.0))
        };
        hBox.setBorder(new Border(borderStrokes));

        // padding between child nodes only
        hBox.setPadding(new Insets(2));

        // rectangles r1 to r4
        Rectangle r1 = make(4, 2);
        Rectangle r2 = make(4, 4);
        Rectangle r3 = make(1, 4);
        Rectangle r4 = make(4, 1);

        // margin of 2 pixels
        HBox.setMargin(r1, new Insets(5, 5, 5,5));
        hBox.getChildren().addAll(r1, r2, r3, r4);

        // once shown display the dimensions all added up.
        stage.setOnShown((WindowEvent we) -> {
            System.out.println("hBox width " + hBox.getBoundsInParent().getWidth());
            System.out.println("hBox height " + hBox.getBoundsInParent().getHeight());
        });

        root.getChildren().add(hBox);
        stage.setScene(scene);
        stage.show();
    }

    private static Rectangle make(int w, int h) {
        Rectangle rect = new Rectangle(w * RECT_MODULE, h * RECT_MODULE);
        rect.setFill(Color.LIGHTSTEELBLUE);
        return rect;
    }
}
