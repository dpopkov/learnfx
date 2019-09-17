package learnfx.javafx9be.ch10web;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.net.URL;

/**
 * Example how to use SVG in web view.
 *
 * VM options:
 * --module-path c:\path-o-javafx-sdk-11\lib
 * --add-modules=javafx.controls,javafx.fxml,javafx.web
 */
public class DisplayingHtml5Content extends Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("Displaying Html5 Content");
        WebView browser = new WebView();
        Scene scene = new Scene(browser, 320, 250, Color.rgb(0, 0, 0, .80));
        stage.setScene(scene);

        URL url = getClass().getResource("/svg/ch10/clock.svg");
        browser.getEngine().load(url.toExternalForm());
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
