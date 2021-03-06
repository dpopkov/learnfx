package learnfx.javafx9be.ch06controls;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MenuExample extends Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("Menus Example");
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 300, 250, Color.WHITE);

        MenuBar menuBar = new MenuBar();
        root.setTop(menuBar);

        Menu fileMenu = new Menu("File");
        MenuItem newMenuItem = new MenuItem("New");
        MenuItem saveMenuItem = new MenuItem("Save");
        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(event -> Platform.exit());
        fileMenu.getItems().addAll(newMenuItem, saveMenuItem, new SeparatorMenuItem(), exitMenuItem);

        Menu cameraMenu = new Menu("Cameras");
        CheckMenuItem cam1MenuItem = new CheckMenuItem("Show Camera 1");
        cam1MenuItem.setSelected(true);
        cameraMenu.getItems().add(cam1MenuItem);
        CheckMenuItem cam2MenuItem = new CheckMenuItem("Show Camera 2");
        cam2MenuItem.setSelected(true);
        cameraMenu.getItems().add(cam2MenuItem);

        Menu alarmMenu = new Menu("Alarm");
        ToggleGroup tGroup = new ToggleGroup();
        RadioMenuItem soundAlarmItem = new RadioMenuItem("Sound Alarm");
        soundAlarmItem.setToggleGroup(tGroup);
        RadioMenuItem stopAlarmItem = new RadioMenuItem("Alarm Off");
        stopAlarmItem.setToggleGroup(tGroup);
        stopAlarmItem.setSelected(true);
        alarmMenu.getItems().addAll(soundAlarmItem, stopAlarmItem, new SeparatorMenuItem());
        Menu contingencyPlans = new Menu("Contingent Plans");
        contingencyPlans.getItems().addAll(
                new CheckMenuItem("Self Destruct in T minus 50"),
                new CheckMenuItem("Turn off the coffee machine "),
                new CheckMenuItem("Run for your lives! "));
        alarmMenu.getItems().add(contingencyPlans);

        menuBar.getMenus().addAll(fileMenu, cameraMenu, alarmMenu);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
