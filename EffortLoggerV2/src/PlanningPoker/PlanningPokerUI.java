package PlanningPoker;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import EffortLoggerHomePage.EffortLoggerHomePage;
import EffortLoggerViewData.EffortLoggerViewData;

public class PlanningPokerUI extends Application {

    private static final String CSV_FILE_PATH = "planning_poker_data.csv";

    private int numberOfUsers;
    private String projectName;
    private List<List<Integer>> userEstimates;
    private List<String> taskNames;
    private int currentRound = 1;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Planning Poker");

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #E0E0E0;");

        TextField usersField = new TextField();
        usersField.setPromptText("Enter Number of Users");

        TextField projectField = new TextField();
        projectField.setPromptText("Enter Project Name");

        Button startButton = new Button("Start Planning Poker");
        startButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        startButton.setOnAction(e -> {
            try {
                numberOfUsers = Integer.parseInt(usersField.getText());
                projectName = projectField.getText();
                initializeUserEstimates();

                startPlanningPoker(primaryStage);
            } catch (NumberFormatException ex) {
                showAlert("Invalid input. Please enter a valid number of users.");
            }
        });

        Button viewDataButton = new Button("View Data");
        viewDataButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        viewDataButton.setOnAction(e -> openEffortLoggerViewData(primaryStage));

        root.getChildren().addAll(usersField, projectField, startButton, viewDataButton);

        Scene scene = new Scene(root, 400, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initializeUserEstimates() {
        userEstimates = new ArrayList<>();
        taskNames = new ArrayList<>();

        for (int i = 0; i < numberOfUsers; i++) {
            userEstimates.add(new ArrayList<>());
        }
    }

    private void startPlanningPoker(Stage primaryStage) {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #E0E0E0;");

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label roundLabel = new Label("Round " + currentRound);
        roundLabel.setStyle("-fx-font-size: 1.5em; "
        		+ "-fx-font-weight: bold;");
        gridPane.add(roundLabel, 0, 0, 3, 1);

        Label taskLabel = new Label("Enter Task Name:");
        gridPane.add(taskLabel, 0, 1);

        TextField taskNameField = new TextField();
        taskNameField.setPromptText("Task Name");
        gridPane.add(taskNameField, 1, 1, 2, 1);

        for (int i = 0; i < numberOfUsers; i++) {
            Label userLabel = new Label("User " + (i + 1));
            gridPane.add(userLabel, 0, i + 2);

            TextField estimateField = new TextField();
            estimateField.setPromptText("Enter Estimate");
            gridPane.add(estimateField, 1, i + 2);

            int userIndex = i;
            estimateField.textProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    int estimate = Integer.parseInt(newValue);
                    userEstimates.get(userIndex).add(estimate);
                } catch (NumberFormatException e) {
                    // Handle invalid input
                    showAlert("Invalid estimate. Please enter a valid number.");
                }
            });
        }

        Button nextRoundButton = new Button("Next Round");
        nextRoundButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        nextRoundButton.setOnAction(e -> {
            if (validateRoundInputs(taskNameField.getText())) {
                taskNames.add(taskNameField.getText());
                currentRound++;
                layout.getChildren().remove(gridPane);
                startPlanningPoker(primaryStage);
            } else {
                showAlert("Please make sure all users have entered an estimate for the current task.");
            }
        });

        Button endGameButton = new Button("End Game");
        endGameButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white;");
        endGameButton.setOnAction(e -> {
            if (validateRoundInputs(taskNameField.getText())) {
                taskNames.add(taskNameField.getText());
            }
            writeEstimatesToCSV();
            showAlert("Game ended. Estimates recorded successfully.");
            primaryStage.close();
        });

        Button backButton = new Button("Back to EffortLoggerHomePage");
        backButton.setStyle("-fx-background-color: #0000FF; -fx-text-fill: white;");
        backButton.setOnAction(e -> openEffortLoggerHomePage(primaryStage));

        layout.getChildren().addAll(gridPane, nextRoundButton, endGameButton, backButton);

        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);
    }

    private boolean validateRoundInputs(String taskName) {
        if (taskName.isEmpty()) {
            return false;
        }

        for (List<Integer> estimates : userEstimates) {
            if (estimates.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    private void writeEstimatesToCSV() {
        try (Writer writer = new FileWriter(CSV_FILE_PATH, true)) {
            for (int i = 0; i < numberOfUsers; i++) {
                StringBuilder line = new StringBuilder(projectName + "," + taskNames.get(0) + ",Round " + currentRound);
                for (int estimate : userEstimates.get(i)) {
                    line.append(",").append(estimate);
                }
                writer.append(line.toString()).append("\n");
            }
        } catch (IOException e) {
            showAlert("Error writing to CSV file.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void openEffortLoggerHomePage(Stage primaryStage) {
        EffortLoggerHomePage effortLoggerHomePage = new EffortLoggerHomePage();
        Stage homePageStage = new Stage();
        homePageStage.setTitle("EffortLogger Home Page");

        // Call the start method of EffortLoggerHomePage
        effortLoggerHomePage.start(homePageStage);

        // Close the planning poker stage
        primaryStage.close();
    }

    private void openEffortLoggerViewData(Stage primaryStage) {
        EffortLoggerViewData effortLoggerViewData = new EffortLoggerViewData();
        Stage viewDataStage = new Stage();
        viewDataStage.setTitle("EffortLogger View Data");

        // Call the start method of EffortLoggerViewData
        effortLoggerViewData.start(viewDataStage);
    
}
}
