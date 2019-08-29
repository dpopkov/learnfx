package learnfx.javafx9be.ch03fund;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Random;

public class DrawingText extends Application {

    private static final int COLOR_BOUND = 256;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Drawing Text");
        Group root = new Group();
        Scene scene = new Scene(root, 300, 250, Color.WHITE);
        Random rand = new Random();

        int width = (int) scene.getWidth();
        int height = (int) scene.getHeight();
        for (int i = 0; i < 100; i++) {
            int x = rand.nextInt(width);
            int y = rand.nextInt(height);
            int red = rand.nextInt(COLOR_BOUND);
            int green = rand.nextInt(COLOR_BOUND);
            int blue = rand.nextInt(COLOR_BOUND);
            Text text = new Text(x, y, "JavaFX 9");
            int rot = rand.nextInt(360);
            text.setFill(Color.rgb(red, green, blue, 0.99));
            text.setRotate(rot);
            root.getChildren().add(text);
        }

        stage.setScene(scene);
        stage.show();
    }
}
