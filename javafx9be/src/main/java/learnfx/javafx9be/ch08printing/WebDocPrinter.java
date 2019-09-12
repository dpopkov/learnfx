package learnfx.javafx9be.ch08printing;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.concurrent.Worker;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * Display an HTML page and sends it to a default printer.
 * Allows the display node to be resized to fit onto the printed page.
 */
public class WebDocPrinter extends Application {
    private static final String PRINT_MODE_MENU = "Print Mode";
    private static final String NODE_ONLY = "Node Only";
    private static final String WHOLE_WEB_DOC = "Whole Web Document";

    private final ToggleGroup printModeGroup = new ToggleGroup();

    @Override
    public void start(Stage stage) {
        stage.setTitle("WebDocPrinter");
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 551, 400, Color.WHITE);
        stage.setScene(scene);

        MenuBar menuBar = new MenuBar();
        Menu printModeMenu = new Menu("Print Mode");
        RadioMenuItem printOnePage = createRadioMenuitem(NODE_ONLY, true);
        RadioMenuItem multiPages = createRadioMenuitem(WHOLE_WEB_DOC, false);
        printModeMenu.setText(PRINT_MODE_MENU + " (" + NODE_ONLY + ")");
        printModeMenu.getItems().addAll(printOnePage, multiPages);
        menuBar.getMenus().add(printModeMenu);
        root.setTop(menuBar);

        BorderPane contentPane = new BorderPane();
        root.setCenter(contentPane);

        WebView browserDisplay = new WebView();

        Slider zoomSlider = new Slider(0.05, 3.0, 1.0);
        zoomSlider.setBlockIncrement(0.05);
        zoomSlider.valueProperty().addListener(listener -> {
            System.out.println("zoom" + browserDisplay.getZoom());
            browserDisplay.setZoom(zoomSlider.getValue());
        });

        Label zoomValueLabel = new Label();
        Bindings.bindBidirectional(zoomValueLabel.textProperty(), zoomSlider.valueProperty(), createStringConverter());

        browserDisplay.widthProperty().addListener(listener -> {
            Printer printer = Printer.getDefaultPrinter();
            System.out.println("printer width: " + printer.getDefaultPageLayout().getPrintableWidth());
            System.out.println("width: " + browserDisplay.widthProperty().get());
        });

        WebEngine webEngine = browserDisplay.getEngine();
        webEngine.getLoadWorker().stateProperty().addListener((obsValue, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                System.out.println("finished loading web-page: " + webEngine.getLocation());
            }
        });

        TextField urlAddressField = new TextField();
        urlAddressField.setPromptText("Enter URL of a page to print");
        urlAddressField.setOnAction(actionEvent -> webEngine.load(urlAddressField.getText()));

        Button printButton = new Button("Print");
        printButton.setOnAction(actionEvent -> printContent(stage, browserDisplay));

        // Assemble print button, zoom slider, zoom label
        VBox vBox = new VBox();
        HBox hBox = new HBox(10);
        hBox.setPadding(new Insets(5));
        hBox.setAlignment(Pos.CENTER_LEFT);
        hBox.getChildren().addAll(printButton, zoomSlider, zoomValueLabel);
        vBox.getChildren().addAll(urlAddressField, hBox);
        contentPane.setTop(vBox);

        StackPane centerArea = new StackPane(browserDisplay);
        Path printPerimeter = createPrintPerimeter();
        StackPane.setAlignment(printPerimeter, Pos.TOP_LEFT);
        centerArea.getChildren().add(printPerimeter);
        contentPane.setCenter(centerArea);

        printModeGroup.selectedToggleProperty().addListener((observableValue) -> {
            Toggle selected = printModeGroup.getSelectedToggle();
            if (selected != null) {
                String mode = String.valueOf(selected.getUserData());
                printModeMenu.setText(PRINT_MODE_MENU + " (" + mode + ")");
                printPerimeter.setVisible(NODE_ONLY.equals(mode));
            }
        });
        stage.setOnShown( eventHandler -> printButton.requestFocus());

        stage.show();
    }

    private StringConverter<Number> createStringConverter() {
        return new StringConverter<>() {
            @Override
            public String toString(Number object) {
                if (object == null) {
                    return "";
                }
                double percent = object.doubleValue() * 100;
                return String.format("%.2f", percent) + "%";
            }

            @Override
            public Number fromString(String string) {
                if (string == null) {
                    return null;
                }
                string = string.trim().replace("%", "");
                if (string.length() < 1) {
                    return null;
                }
                return Double.parseDouble(string) / 100;
            }
        };
    }

    private RadioMenuItem createRadioMenuitem(String text, boolean selected) {
        RadioMenuItem radioMenuItem = new RadioMenuItem(text);
        radioMenuItem.setUserData(text);
        radioMenuItem.setToggleGroup(printModeGroup);
        if (selected) {
            printModeGroup.selectToggle(radioMenuItem);
        }
        return radioMenuItem;
    }

    private void printContent(Stage stage, WebView browserDisplay) {
        PrinterJob job = PrinterJob.createPrinterJob();
        Toggle selected = printModeGroup.getSelectedToggle();
        if (selected != null) {
            String mode = (String) selected.getUserData();
            if (NODE_ONLY.equals(mode)) {
                boolean success = job.printPage(browserDisplay);
                if (success) {
                    job.endJob();
                }
            } else {
                boolean printIt = job.showPrintDialog(stage);
                if (printIt) {
                    browserDisplay.getEngine().print(job);
                    job.endJob();
                }
            }
        }
    }

    private Path createPrintPerimeter() {
        Path printPerimeter = new Path();
        Printer printer = Printer.getDefaultPrinter();
        double printWidth = printer.getDefaultPageLayout().getPrintableWidth();
        double printHeight = printer.getDefaultPageLayout().getPrintableHeight();
        PathElement[] corners = {
                new MoveTo(0, 0),
                new LineTo(printWidth, 0),
                new LineTo(printWidth, printHeight),
                new LineTo(0, printHeight),
                new ClosePath()
        };
        printPerimeter.getElements().addAll(corners);
        printPerimeter.setStroke(Color.RED);
        return printPerimeter;
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
