package learnfx.javafx9be.ch06controls;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BackgroundProcesses extends Application {
    private static final int NUM_FILES = 30;
    private static Task<Boolean> copyWorker;

    private ExecutorService threadPool;

    @Override
    public void init() throws Exception {
        super.init();
        threadPool = Executors.newFixedThreadPool(1);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        threadPool.shutdown();
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("BackgroundProcesses: Background Processes");
        Group root = new Group();
        Scene scene = new Scene(root, 330, 120, Color.WHITE);

        BorderPane mainPane = new BorderPane();
        mainPane.layoutXProperty().bind(scene.widthProperty().subtract(mainPane.widthProperty()).divide(2));
        root.getChildren().add(mainPane);

        Label label = new Label("Files Transfer:");
        ProgressBar progressBar = new ProgressBar(0);
        ProgressIndicator progressIndicator = new ProgressIndicator(0);

        HBox hb = new HBox();
        hb.setSpacing(5);
        hb.setAlignment(Pos.CENTER);
        hb.getChildren().addAll(label, progressBar, progressIndicator);
        mainPane.setTop(hb);

        Button startBtn = new Button("Start");
        Button cancelBtn = new Button("Cancel");
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setPrefSize(200, 70);

        HBox hb2 = new HBox();
        hb2.setSpacing(5);
        hb2.setAlignment(Pos.CENTER);
        hb2.getChildren().addAll(startBtn, cancelBtn, textArea);
        mainPane.setBottom(hb2);

        startBtn.setOnAction(event -> {
            startBtn.setDisable(true);
            progressBar.setProgress(0);
            progressIndicator.setProgress(0);
            textArea.setText("");
            cancelBtn.setDisable(false);
            copyWorker = createWorker(NUM_FILES);

            progressBar.progressProperty().unbind();
            progressBar.progressProperty().bind(copyWorker.progressProperty());
            progressIndicator.progressProperty().unbind();
            progressIndicator.progressProperty().bind(copyWorker.progressProperty());

            copyWorker.messageProperty().addListener((observable, oldValue, newValue)
                    -> textArea.appendText(newValue + "\n"));
            threadPool.submit(copyWorker);
        });

        cancelBtn.setOnAction(event -> {
            startBtn.setDisable(false);
            cancelBtn.setDisable(true);
            copyWorker.cancel(true);

            progressBar.progressProperty().unbind();
            progressBar.setProgress(0);
            progressIndicator.progressProperty().unbind();
            progressIndicator.setProgress(0);
            textArea.appendText("File transfer was cancelled.");
        });
        cancelBtn.setDisable(true);

        stage.setScene(scene);
        stage.show();
    }

    @SuppressWarnings("SameParameterValue")
    private Task<Boolean> createWorker(final int numFiles) {
        return new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                for (int i = 0; i < numFiles; i++) {
                    long elapsedTime = System.currentTimeMillis();
                    copyFile("some file", "some dest file");
                    elapsedTime = System.currentTimeMillis() - elapsedTime;
                    String status = elapsedTime + " milliseconds";
                    updateMessage(status);
                    updateProgress(i + 1, numFiles);
                }
                return true;
            }
        };
    }

    @SuppressWarnings({"SameParameterValue", "unused"})
    private void copyFile(String src, String dest_file) throws InterruptedException {
        simulateLongTime();
    }

    private void simulateLongTime() throws InterruptedException {
        Random rnd = new Random();
        long millis = rnd.nextInt(1000);
        Thread.sleep(millis);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
