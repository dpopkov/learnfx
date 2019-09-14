package learnfx.javafx9be.ch10web;

import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 * Example how to use WebView.
 *
 * VM options:
 * --module-path c:\path-o-javafx-sdk-11\lib
 * --add-modules=javafx.controls,javafx.fxml,javafx.web
 */
public class WebViewUsage extends Application {
    private final WebView webView = new WebView();

    @Override
    public void start(Stage stage) {
        stage.setTitle("WevView usage example");
        BorderPane root = new BorderPane();
        root.setCenter(webView);
        webView.getEngine().getLoadWorker().stateProperty().addListener((ov, old, newState) -> printState(newState));
        Scene scene = new Scene(root, 551, 480, Color.WHITE);
        stage.setScene(scene);

        TextField addressField = new TextField();
        addressField.setPromptText("Enter URL of a page and press Enter");
        addressField.setOnAction(actionEvent -> webView.getEngine().load(addressField.getText()));
        root.setTop(addressField);

        VBox bottomPane = new VBox(10);
        bottomPane.setPadding(new Insets(10));
        TextArea htmlArea = new TextArea();
        htmlArea.setPromptText("Enter html to load into web view component");
        Button loadContentBtn = new Button("Load Content into the web view component");
        loadContentBtn.setOnAction(actionEvent -> webView.getEngine().loadContent(htmlArea.getText()));

        Button showHtmlBtn = new Button("Show HTML of the loaded page");
        showHtmlBtn.setOnAction(ae -> htmlArea.setText(
                (String) webView.getEngine().executeScript("document.documentElement.outerHTML"))
        );

        HBox buttonBox = new HBox(10);
        buttonBox.setPadding(new Insets(10));
        buttonBox.getChildren().addAll(loadContentBtn, showHtmlBtn);
        bottomPane.getChildren().addAll(htmlArea, buttonBox);

        root.setBottom(bottomPane);

        stage.show();
    }

    private void printState(Worker.State newState) {
        System.out.println("Web engine worker state: " + newState);
        if (newState == Worker.State.SUCCEEDED) {
            System.out.println("Finished loading web-page " + webView.getEngine().getLocation());
        }
    }
}
