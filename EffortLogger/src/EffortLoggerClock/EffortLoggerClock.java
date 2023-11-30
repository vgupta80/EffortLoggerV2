package EffortLoggerClock;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.geometry.Pos;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class EffortLoggerClock extends Application {
    private Timeline timeline;
    private long startTime = 0;
    private long pauseTime = 0;
    private boolean isRunning = false;

    @Override
    public void start(Stage primaryStage) {
        // Text to show the time
        Text textTime = new Text("00:00:00.000");

        // Start button
        Button startButton = new Button("Start");
        startButton.setOnAction(e -> {
            if (!isRunning) {
                startTime = startTime == 0 ? System.currentTimeMillis() - pauseTime : startTime;
                timeline.play();
                isRunning = true;
            }
        });

        // Pause button
        Button pauseButton = new Button("Pause");
        pauseButton.setOnAction(e -> {
            if (isRunning) {
                pauseTime = System.currentTimeMillis() - startTime;
                timeline.pause();
                isRunning = false;
            }
        });

        // Stop button
        Button stopButton = new Button("Stop");
        stopButton.setOnAction(e -> {
            if (timeline != null) {
                timeline.stop();
            }
            long stopTime = System.currentTimeMillis();
            long recordedTime = stopTime - startTime;
            System.out.println("Time recorded: " + formatTime(recordedTime));

            // Resetting the start and pause times
            startTime = 0;
            pauseTime = 0;
            textTime.setText("00:00:00.000");
            isRunning = false;
        });

        // Timeline that updates the textTime
        timeline = new Timeline(new KeyFrame(Duration.millis(100), ev -> {
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - startTime + pauseTime;
            textTime.setText(formatTime(elapsedTime));
        }));
        timeline.setCycleCount(Animation.INDEFINITE);

        // Layout
        HBox root = new HBox(10, startButton, pauseButton, stopButton, textTime);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 400, 200);
        primaryStage.setTitle("EffortLogger Clock");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private String formatTime(long time) {
        long hours = time / 3600000;
        long minutes = (time % 3600000) / 60000;
        long seconds = (time % 60000) / 1000;
        long millis = time % 1000;
        return String.format("%02d:%02d:%02d.%03d", hours, minutes, seconds, millis);
    }

    public static void main(String[] args) {
        launch(args);
    }
}