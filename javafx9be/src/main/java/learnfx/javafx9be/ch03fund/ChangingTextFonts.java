package learnfx.javafx9be.ch03fund;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ChangingTextFonts extends Application {

    private static final String TEXT = "JavaFX by Example";
    private static final int SIZE = 30;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Changing Text Fonts");
        printFonts();

        Group root = new Group();
        Scene scene = new Scene(root, 580, 260, Color.WHITE);
        final int posX = 50;
        int posY = 50;

        // Serif with drop shadow
        Text text2 = new Text(posX, posY, TEXT);
        Font serif = Font.font("Serif", SIZE);
        text2.setFont(serif);
        text2.setFill(Color.RED);
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(2.0F);
        dropShadow.setOffsetY(2.0F);
        dropShadow.setColor(Color.rgb(50, 50, 50, 0.588));
        text2.setEffect(dropShadow);
        root.getChildren().add(text2);

        // SanSerif
        Text text3 = new Text(posX, posY += 50, TEXT);
        Font sanSerif = Font.font("SanSerif", SIZE);
        text3.setFont(sanSerif);
        text3.setFill(Color.BLUE);
        root.getChildren().add(text3);

        // Dialog
        Text text4 = new Text(posX, posY += 50, TEXT);
        Font dialogFont = Font.font("Dialog", SIZE);
        text4.setFont(dialogFont);
        text4.setFill(Color.rgb(0, 255, 0));
        root.getChildren().add(text4);

        // Dialog
        Text text5 = new Text(posX, posY + 50, TEXT);
        Font monoFont = Font.font("Monospaced", SIZE);
        text5.setFont(monoFont);
        text5.setFill(Color.BLACK);
        root.getChildren().add(text5);

        // Reflection
        Reflection reflection = new Reflection();
        reflection.setFraction(0.8F);
        reflection.setTopOffset(5);
        text5.setEffect(reflection);

        stage.setScene(scene);
        stage.show();
    }

    private void printFonts() {
        System.out.println("Font families:");
        Font.getFamilies().forEach(System.out::println);
        System.out.println();
        System.out.println("Font names:");
        Font.getFontNames().forEach(System.out::println);
    }
}
