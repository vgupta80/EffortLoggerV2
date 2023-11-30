package Login;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.util.List;
import java.util.Objects;

public class LoginUI extends Application {

    private static final String CSV_FILE_PATH = "users.csv";

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login Page");

        GridPane gridPane = createLoginPage(primaryStage);

        Scene scene = new Scene(gridPane, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane createLoginPage(Stage primaryStage) {
        GridPane gridPane = new GridPane();
        gridPane.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Button loginButton = new Button("Login");
        Button createAccountButton = new Button("Create Account");

        loginButton.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        createAccountButton.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        gridPane.add(usernameLabel, 0, 0);
        gridPane.add(usernameField, 1, 0);
        gridPane.add(passwordLabel, 0, 1);
        gridPane.add(passwordField, 1, 1);
        gridPane.add(loginButton, 1, 2);
        gridPane.add(createAccountButton, 1, 3);

        loginButton.setOnAction(e -> handleLogin(primaryStage, usernameField.getText(), passwordField.getText()));
        createAccountButton.setOnAction(e -> openCreateAccountWindow());

        return gridPane;
    }

    private void handleLogin(Stage primaryStage, String enteredUsername, String enteredPassword) {
        List<User> users = CsvHandler.readUsersFromCsv(CSV_FILE_PATH);

        for (User user : users) {
            if (user.getUsername().equals(enteredUsername) &&
                    Objects.hash(enteredPassword) == user.getHashedPassword()) {
                showAlert("Login Successful", "Welcome, " + enteredUsername + "!");
                return;
            }
        }

        showAlert("Login Failed", "Invalid username or password. Please try again.");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void openCreateAccountWindow() {
        Stage createAccountStage = new Stage();
        createAccountStage.setTitle("Create Account");

        GridPane gridPane = createCreateAccountPage(createAccountStage);

        Scene scene = new Scene(gridPane, 300, 200);
        createAccountStage.setScene(scene);
        createAccountStage.initModality(Modality.WINDOW_MODAL);
        createAccountStage.show();
    }

    private GridPane createCreateAccountPage(Stage createAccountStage) {
        GridPane gridPane = new GridPane();
        gridPane.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        Label usernameLabel = new Label("Username:");
        TextField usernameField = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        Button createAccountButton = new Button("Create Account");

        createAccountButton.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        gridPane.add(usernameLabel, 0, 0);
        gridPane.add(usernameField, 1, 0);
        gridPane.add(passwordLabel, 0, 1);
        gridPane.add(passwordField, 1, 1);
        gridPane.add(createAccountButton, 1, 2);

        createAccountButton.setOnAction(e -> handleCreateAccount(createAccountStage, usernameField.getText(), passwordField.getText()));

        return gridPane;
    }

    private void handleCreateAccount(Stage createAccountStage, String newUsername, String newPassword) {
        User newUser = new User(newUsername, newPassword);
        CsvHandler.saveUserToCsv(newUser, CSV_FILE_PATH);

        showAlert("Account Created", "New account created:\nUsername: " + newUsername);

        createAccountStage.close();
    }

    static class User implements Serializable {
        private static final long serialVersionUID = 1L;

        private String username;
        private int hashedPassword;

        public User(String username, String password) {
            this.username = username;
            this.hashedPassword = Objects.hash(password);
        }

        public String getUsername() {
            return username;
        }

        public int getHashedPassword() {
            return hashedPassword;
        }
    }

    static class CsvHandler {
        public static void saveUserToCsv(User user, String filePath) {
            List<User> users = readUsersFromCsv(filePath);
            users.add(user);
            writeUsersToCsv(users, filePath);
        }

        public static List<User> readUsersFromCsv(String filePath) {
            List<User> users = null;

            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
                users = (List<User>) ois.readObject();
            } catch (FileNotFoundException e) {
                // File doesn't exist, create a new list
                users = new java.util.ArrayList<>();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            return users;
        }

        public static void writeUsersToCsv(List<User> users, String filePath) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
                oos.writeObject(users);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
