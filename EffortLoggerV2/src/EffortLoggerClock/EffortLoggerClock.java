package EffortLoggerClock;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
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
import models.TaskData;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;

import EffortLoggerHomePage.EffortLoggerHomePage;

public class EffortLoggerClock extends Application {

    private long startTime = 0;
    private boolean timerRunning = false;
    private long elapsedTime = 0;
    private String taskName = "";

    private static final String CSV_FILE_PATH = "task_data.csv";

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("EffortLogger Clock");

        Label timerLabel = new Label("00:00:00");
        timerLabel.setStyle("-fx-font-size: 3em;");

        Button startButton = new Button("Start");
        Button pauseButton = new Button("Pause");
        Button stopButton = new Button("Stop");
        Button backButton = new Button("Back");
       


        // Style buttons with background color
        startButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        pauseButton.setStyle("-fx-background-color: #FFC107; -fx-text-fill: white;");
        stopButton.setStyle("-fx-background-color: #E57373; -fx-text-fill: white;");
        backButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white;");

        startButton.setOnAction(e -> startTimer(timerLabel));
        pauseButton.setOnAction(e -> pauseTimer());
        stopButton.setOnAction(e -> stopTimer(primaryStage, timerLabel));
        backButton.setOnAction(e -> openEffortLoggerHomePage(primaryStage));

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(startButton, pauseButton, stopButton);
        
        VBox backButtonBox = new VBox(5);
        backButtonBox.setAlignment(Pos.CENTER_RIGHT);
        backButtonBox.getChildren().addAll(backButton);

        BorderPane layout1 = new BorderPane();
        layout1.setCenter(timerLabel);
        layout1.setBottom(buttonBox);
        layout1.setTop(backButtonBox);
        BorderPane.setAlignment(timerLabel, Pos.CENTER);

        // Set background color for the entire scene
        layout1.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));

        Scene scene = new Scene(layout1, 600, 400);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    private void startTimer(Label timerLabel) {
        if (!timerRunning) {
            startTime = System.currentTimeMillis() - elapsedTime;

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
            // Stop the timer
            elapsedTime = System.currentTimeMillis() - startTime;
            startTime = 0;

            // Prompt for task name when Stop is clicked after starting the timer
            TaskData taskData = promptForTaskNameAndCreateTaskData(primaryStage);
            writeTaskDataToCSV(taskData);

            taskName = "";
            updateTimerLabel(timerLabel, elapsedTime);
        } else {
            // Prompt for task name when Stop is clicked without starting the timer
            TaskData taskData = promptForTaskNameAndCreateTaskData(primaryStage);
            writeTaskDataToCSV(taskData);
        }

        // Reset the timer to 0
        elapsedTime = 0;
        updateTimerLabel(timerLabel, elapsedTime);
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

    private TaskData promptForTaskNameAndCreateTaskData(Stage primaryStage) {
        String taskName = promptForTaskName(primaryStage);
        String formattedTime = formatTime((int) (elapsedTime / 1000));
        return new TaskData(taskName, formattedTime);
    }

    private String formatTime(int totalSeconds) {
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private String promptForTaskName(Stage primaryStage) {
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

        return taskName;
    }

    private void writeTaskDataToCSV(TaskData taskData) {
        try (Writer writer = new FileWriter(CSV_FILE_PATH, true)) {
            // Append task data to CSV file
            writer.append(taskData.getTaskName()).append(",").append(taskData.getFormattedTime()).append("\n");
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception gracefully (e.g., log the error or display an alert)
        }
    }
    
    private void openEffortLoggerHomePage(Stage primaryStage) {
        EffortLoggerHomePage effortLoggerHomePage = new EffortLoggerHomePage();
        Stage clockStage = new Stage();
        clockStage.setTitle("EffortLogger Home Page");

        // Call the start method of EffortLoggerClock
        effortLoggerHomePage.start(clockStage);

        // Close the home page stage
        primaryStage.close();
    }
    
    
}
