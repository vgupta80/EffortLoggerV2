package Login;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginUI extends Application {

    private static final String LOGIN_PAGE = "login";
    private static final String CREATE_ACCOUNT_PAGE = "createAccount";

    private String currentPage = LOGIN_PAGE;
    private StackPane stackPane;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Enhanced Login Page");

        // Create pages
        GridPane loginPage = createLoginPage();
        GridPane createAccountPage = createCreateAccountPage();

        // Create a stack pane to hold pages
        stackPane = new StackPane(loginPage, createAccountPage);

        // Show the initial page
        showPage(stackPane, LOGIN_PAGE);

        // Create a scene and set it to the stage
        Scene scene = new Scene(stackPane, 300, 200);

        // Set background colors for pages
        loginPage.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        createAccountPage.setBackground(new Background(new BackgroundFill(javafx.scene.paint.Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));

        primaryStage.setScene(scene);

        // Show the stage
        primaryStage.show();
    }

    private GridPane createLoginPage() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");
        Button createAccountButton = new Button("Create Account");

        gridPane.add(usernameLabel, 0, 0);
        gridPane.add(usernameField, 1, 0);
        gridPane.add(passwordLabel, 0, 1);
        gridPane.add(passwordField, 1, 1);
        gridPane.add(loginButton, 1, 2);
        gridPane.add(createAccountButton, 1, 3);

        loginButton.setOnAction(e -> handleLogin(usernameField.getText(), passwordField.getText()));
        createAccountButton.setOnAction(e -> showPage(stackPane, CREATE_ACCOUNT_PAGE));

        return gridPane;
    }

    private GridPane createCreateAccountPage() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        Label newUsernameLabel = new Label("New Username:");
        TextField newUsernameField = new TextField();
        Label newPasswordLabel = new Label("New Password:");
        PasswordField newPasswordField = new PasswordField();
        Button createAccountButton = new Button("Create Account");

        gridPane.add(newUsernameLabel, 0, 0);
        gridPane.add(newUsernameField, 1, 0);
        gridPane.add(newPasswordLabel, 0, 1);
        gridPane.add(newPasswordField, 1, 1);
        gridPane.add(createAccountButton, 1, 2);

        createAccountButton.setOnAction(e -> handleCreateAccount(newUsernameField.getText(), newPasswordField.getText()));

        return gridPane;
    }

    private void handleLogin(String username, String password) {
        // Basic login validation
        if ("user".equals(username) && "pass".equals(password)) {
            showAlert("Login Successful", "Welcome, " + username + "!");
        } else {
            showAlert("Login Failed", "Invalid username or password. Please try again.");
        }
    }

    private void handleCreateAccount(String newUsername, String newPassword) {
        // Add your logic for creating a new account
        showAlert("Create Account", "New account created:\nUsername: " + newUsername + "\nPassword: " + newPassword);
        // Optionally, navigate back to the login page after creating a new account
        showPage(stackPane, LOGIN_PAGE);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showPage(StackPane stackPane, String pageName) {
        stackPane.getChildren().forEach(node -> node.setVisible(false));

        switch (pageName) {
            case LOGIN_PAGE:
                stackPane.getChildren().get(0).setVisible(true);
                break;
            case CREATE_ACCOUNT_PAGE:
                stackPane.getChildren().get(1).setVisible(true);
                break;
            default:
                throw new IllegalArgumentException("Invalid page name: " + pageName);
        }
        currentPage = pageName;
    }
}