package EffortLoggerViewData;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import models.TaskData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import EffortLoggerHomePage.EffortLoggerHomePage;

public class EffortLoggerViewData extends Application {

    private static final String CSV_FILE_PATH = "task_data.csv";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("EffortLogger View Data");
        
        TableView<TaskData> table = createTableView();
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white;");
        backButton.setOnAction(e -> openEffortLoggerHomePage(primaryStage));
        BorderPane layout = new BorderPane();
        layout.setCenter(table);
        layout.setTop(backButton);

        // Set background color for the entire scene
        layout.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));

        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setScene(scene);

        // Load task data into the table
        loadTaskData(table);

        primaryStage.show();
    }

    private TableView<TaskData> createTableView() {
        TableView<TaskData> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<TaskData, String> taskNameColumn = new TableColumn<>("Task Name");
        taskNameColumn.setCellValueFactory(new PropertyValueFactory<>("taskName"));

        TableColumn<TaskData, String> formattedTimeColumn = new TableColumn<>("Formatted Time");
        formattedTimeColumn.setCellValueFactory(new PropertyValueFactory<>("formattedTime"));

        table.getColumns().addAll(taskNameColumn, formattedTimeColumn);

        return table;
    }

    private void loadTaskData(TableView<TaskData> table) {
        ObservableList<TaskData> taskDataList = FXCollections.observableArrayList();

        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String taskName = parts[0];
                    String formattedTime = parts[1];
                    taskDataList.add(new TaskData(taskName, formattedTime));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        table.setItems(taskDataList);
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
