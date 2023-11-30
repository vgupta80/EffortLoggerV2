package EffortLoggerHomePage;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class EffortLoggerHomePage extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Effort Logger Home Page");

        GridPane gridPane = createHomePage();

        Scene scene = new Scene(gridPane, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane createHomePage() {
        GridPane gridPane = new GridPane();
        gridPane.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        gridPane.setPadding(new Insets(40, 40, 40, 40));
        gridPane.setVgap(30);
        gridPane.setHgap(30);

        Button planningPokerButton = createStyledButton("Planning Poker", Color.rgb(244, 121, 33));
        planningPokerButton.setOnAction(e -> openPlanningPoker());

        Button effortLoggerClockButton = createStyledButton("EffortLogger Clock", Color.rgb(66, 134, 244));
        effortLoggerClockButton.setOnAction(e -> openEffortLoggerClock());

        Button effortLoggerViewDataButton = createStyledButton("EffortLogger View Data", Color.rgb(46, 204, 113));
        effortLoggerViewDataButton.setOnAction(e -> openEffortLoggerViewData());

        gridPane.add(planningPokerButton, 0, 0);
        gridPane.add(effortLoggerClockButton, 1, 0);
        gridPane.add(effortLoggerViewDataButton, 2, 0);

        return gridPane;
    }

    private Button createStyledButton(String text, Color color) {
        Button button = new Button(text);
        button.setFont(Font.font("Arial", 16));
        button.setStyle("-fx-text-fill: white;");
        button.setBackground(new Background(new BackgroundFill(color, new CornerRadii(10), Insets.EMPTY)));
        button.setMinSize(200, 80);
        button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        return button;
    }

    private void openPlanningPoker() {
        // Add your code to open Planning Poker window
        System.out.println("Opening Planning Poker");
    }

    private void openEffortLoggerClock() {
        // Add your code to open EffortLogger Clock window
        System.out.println("Opening EffortLogger Clock");
    }

    private void openEffortLoggerViewData() {
        // Add your code to open EffortLogger View Data window
        System.out.println("Opening EffortLogger View Data");
    }
}