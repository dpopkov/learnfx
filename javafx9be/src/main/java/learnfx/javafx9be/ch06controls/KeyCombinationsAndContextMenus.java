package learnfx.javafx9be.ch06controls;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class KeyCombinationsAndContextMenus extends Application {
    private final StringProperty statusProperty = new SimpleStringProperty();
    private String osAccelerator = "";

    @Override
    public void start(Stage stage) {
        if (System.getProperty("os.name").toLowerCase().startsWith("mac")) {
            osAccelerator = "⌘";
        } else {
            osAccelerator = "Ctrl";
        }
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 530, 350, Color.WHITE);

        InnerShadow iShadow = new InnerShadow();
        iShadow.setOffsetX(3.5f);
        iShadow.setOffsetY(3.5f);
        final Text status = new Text();
        status.setEffect(iShadow);
        status.setX(100);
        status.setY(50);
        status.setFill(Color.LIME);
        status.setFont(Font.font(null, FontWeight.BOLD, 35));
        status.textProperty().bind(statusProperty);
        statusProperty.set("Keyboard Shortcuts\n"
                + osAccelerator + "-N, \n"
                + osAccelerator + "-S, \n"
                + osAccelerator + "-X \n"
                + osAccelerator + "-Shift-E");
        root.setCenter(status);

        MenuBar menuBar = new MenuBar();
        menuBar.prefWidthProperty().bind(stage.widthProperty());
        root.setTop(menuBar);

        Menu fileMenu = new Menu("_File");
        fileMenu.setMnemonicParsing(true);
        menuBar.getMenus().add(fileMenu);

        MenuItem newItem = createMenuItem("_New", new KeyCodeCombination(KeyCode.N, KeyCombination.SHORTCUT_DOWN),
                event -> statusProperty.set(osAccelerator + "-N"));
        MenuItem saveItem = createMenuItem("_Save", new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN),
                event -> statusProperty.set(osAccelerator + "-S"));
        KeyCodeCombination eraseKeyCombo = new KeyCodeCombination(KeyCode.E,
                KeyCombination.SHORTCUT_DOWN, KeyCombination.SHIFT_DOWN);
        MenuItem eraseItem = createMenuItem("_Erase", eraseKeyCombo,
                event -> statusProperty.set(osAccelerator + "-Shift-E"));
        MenuItem exitItem = createMenuItem("E_xit", new KeyCodeCombination(KeyCode.X, KeyCombination.SHORTCUT_DOWN),
                event -> {
                    statusProperty.set(osAccelerator + "-X");
                    Platform.exit();
                });
        fileMenu.getItems().addAll(newItem, saveItem, eraseItem, new SeparatorMenuItem(), exitItem);

        MenuItem newContextItem = createMenuItemLike(newItem);
        MenuItem saveContextItem = createMenuItemLike(saveItem);
        MenuItem eraseContextItem = createMenuItemLike(eraseItem);
        MenuItem exitContextItem = createMenuItemLike(exitItem);
        ContextMenu contextFileMenu = new ContextMenu();
        contextFileMenu.getItems().addAll(newContextItem, saveContextItem, eraseContextItem,
                new SeparatorMenuItem(), exitContextItem);
        stage.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                contextFileMenu.show(root, mouseEvent.getScreenX(), mouseEvent.getScreenY());
            } else {
                contextFileMenu.hide();
            }
        });

        stage.setScene(scene);
        stage.show();
    }

    private static MenuItem createMenuItem(String name, KeyCombination keyCombination, EventHandler<ActionEvent> handler) {
        MenuItem item = new MenuItem(name);
        item.setMnemonicParsing(true);
        item.setAccelerator(keyCombination);
        item.setOnAction(handler);
        return item;
    }

    private static MenuItem createMenuItemLike(MenuItem menuItem) {
        return createMenuItem(menuItem.getText(), menuItem.getAccelerator(), menuItem.getOnAction());
    }
}
