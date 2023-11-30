package EffortLoggerClock;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class EffortLoggerClock extends Application {

    private long startTime = 0;
    private boolean timerRunning = false;
    private long elapsedTime = 0;
    private boolean taskNamePrompted = false;
    private String taskName = "";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Enhanced EffortLogger Clock");

        Label timerLabel = new Label("00:00:00");
        timerLabel.setStyle("-fx-font-size: 3em;");

        Button startButton = new Button("Start");
        Button pauseButton = new Button("Pause");
        Button stopButton = new Button("Stop");

        // Style buttons with background color
        startButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        pauseButton.setStyle("-fx-background-color: #FFC107; -fx-text-fill: white;");
        stopButton.setStyle("-fx-background-color: #E57373; -fx-text-fill: white;");

        startButton.setOnAction(e -> startTimer(primaryStage, timerLabel));
        pauseButton.setOnAction(e -> pauseTimer());
        stopButton.setOnAction(e -> stopTimer(primaryStage, timerLabel));

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(startButton, pauseButton, stopButton);

        BorderPane layout = new BorderPane();
        layout.setCenter(timerLabel);
        layout.setBottom(buttonBox);
        BorderPane.setAlignment(timerLabel, Pos.CENTER);

        // Set background color for the entire scene
        layout.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));

        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    private void startTimer(Stage primaryStage, Label timerLabel) {
        if (!timerRunning) {
            if (!taskNamePrompted) {
                // Prompt for task name only once after the first "Start" click
                taskNamePrompted = true;
                promptForTaskName(primaryStage);
            }

            if (elapsedTime == 0) {
                startTime = System.currentTimeMillis();
            } else {
                startTime = System.currentTimeMillis() - elapsedTime;
            }

            timerRunning = true;

            // Use a Timeline to update the timer label every second
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(1), event -> {
                        if (timerRunning) {
                            elapsedTime = System.currentTimeMillis() - startTime;
                            updateTimerLabel(timerLabel, elapsedTime);
                        }
                    })
            );
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
        }
    }

    private void pauseTimer() {
        timerRunning = false;
    }

    private void stopTimer(Stage primaryStage, Label timerLabel) {
        if (timerRunning) {
            timerRunning = false;
            // Reset the timer
            startTime = 0;
            elapsedTime = 0;
            taskNamePrompted = false;
            updateTimerLabel(timerLabel, elapsedTime);
        } else {
            // Prompt for task name when Stop is clicked without starting the timer
            promptForTaskName(primaryStage);
        }
    }

    private void updateTimerLabel(Label timerLabel, long elapsedTime) {
        long seconds = elapsedTime / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        seconds %= 60;
        minutes %= 60;

        String time = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        timerLabel.setText(time);
    }

    private void promptForTaskName(Stage primaryStage) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);

        VBox dialogVBox = new VBox(20);
        dialogVBox.setAlignment(Pos.CENTER);
        dialogVBox.setPadding(new Insets(20));

        Label promptLabel = new Label("Enter Task Name:");
        TextField taskNameField = new TextField();
        Button submitButton = new Button("Submit");

        submitButton.setOnAction(e -> {
            taskName = taskNameField.getText();
            dialogStage.close();
        });

        dialogVBox.getChildren().addAll(promptLabel, taskNameField, submitButton);

        Scene dialogScene = new Scene(dialogVBox, 250, 150);
        dialogStage.setScene(dialogScene);

        // Set background color for the prompt dialog
        dialogVBox.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));

        dialogStage.showAndWait();
    }
}