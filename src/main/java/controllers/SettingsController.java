package controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import auth.AuthManager;
import models.Board;
import models.User;
import utils.Database;

import java.util.HashSet;
import java.util.Set;

public class SettingsController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private VBox boardsContainer; // VBox to hold dynamic board buttons

    private Set<Board> boards; // Stores all boards for the connected user
    private User connectedUser; // Stores the currently connected user

    @FXML
    public void initialize() {
        if (!AuthManager.verifyToken(AuthManager.getToken())) {
            showAlert("Access Denied", "You are not signed in.");
            // Delay the window closure
            Platform.runLater(() -> {
                closeWindow(); // This will execute after the UI is fully initialized
                openLoginWindow();
            });
        } else {
            // Rest of the logic for authenticated users
            connectedUser = AuthManager.getAuthenticatedUser();
            if (connectedUser != null) {
                System.out.println("Authenticated User: " + connectedUser.getUsername());
                System.out.println("Email: " + connectedUser.getEmail());

                loadBoardsForUser();
                displayBoards();
            } else {
                System.out.println("No authenticated user found.");
            }
        }
    }

    private void loadBoardsForUser() {
        // Load all boards from the database
        Set<Board> allBoards = Database.loadBoards();

        // Filter boards to include only those that belong to the connected user
        boards = new HashSet<>();
        for (Board board : allBoards) {
            if (board.getUserId().equals(connectedUser.getId())) {
                boards.add(board);
            }
        }

        // Debugging: Print the boards
        System.out.println("Boards for user " + connectedUser.getUsername() + ":");
        for (Board board : boards) {
            System.out.println(board);
        }
    }

    private void displayBoards() {
        // Clear only the dynamic board buttons (preserve the title, "New Board" button, and "Sign Out" button)
        // The first 3 children are: Title, "New Board" button, and an empty Text node (spacer)
        // The last 2 children are: "Settings" button and "Sign Out" button
        // We only want to remove the dynamic board buttons added between these elements.

        // Find the index where dynamic board buttons should be added
        int startIndex = 2; // After the title and "New Board" button
        int endIndex = boardsContainer.getChildren().size() - 2; // Before the "Settings" and "Sign Out" buttons

        // Remove only the dynamic board buttons
        boardsContainer.getChildren().remove(startIndex, endIndex);

        // Add a button for each board
        for (Board board : boards) {
            Button boardButton = new Button("Board " + board.getId());
            boardButton.setStyle("-fx-border-radius: 0;");
            boardButton.getStyleClass().add("board-button"); // Add the CSS class
            boardButton.setTextFill(Color.WHITE); // Set text color
            boardButton.setPrefHeight(40.0); // Set preferred height
            boardButton.setPrefWidth(184.0); // Set preferred width
            boardButton.setAlignment(Pos.CENTER_RIGHT); // Align text to the right
            boardButton.setOnAction(event -> handleBoardClick(board));

            // Add icon to the board button
            ImageView boardIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/icons8-board-50.png")));
            boardIcon.setFitHeight(20.0);
            boardIcon.setFitWidth(20.0);
            boardIcon.setPreserveRatio(true);
            boardIcon.setPickOnBounds(true);
            boardButton.setGraphic(boardIcon);

            // Add the board button to the correct position in the VBox
            boardsContainer.getChildren().add(startIndex, boardButton);
        }
    }

    private void handleBoardClick(Board board) {
        System.out.println("Board clicked: " + " board " + board.getId());
        // You can add logic here to handle board clicks, such as displaying tasks for the selected board
    }

    @FXML
    private void handleSaveChanges() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        // Validate inputs
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "All fields are required.");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert("Error", "Invalid email format.");
            return;
        }

        // Save changes (e.g., update database or user object)
        // For now, just print the updated values
        System.out.println("Username: " + username);
        System.out.println("Email: " + email);
        System.out.println("Password: " + password);

        showAlert("Success", "Your information has been updated.");
    }

    @FXML
    private void handleCancel() {
        // Close the settings window
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }

    private boolean isValidEmail(String email) {
        // Simple email validation regex
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleClickSignOut(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
            Parent parent = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Login");
            stage.setScene(new Scene(parent));
            stage.show();

            AuthManager.clearToken();
            System.out.println("Token cleared. User signed out.");

            // Close the current window
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }

    private void openLoginWindow() {
        try {
            Stage currentStage = (Stage) usernameField.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/views/login.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());

            currentStage.setScene(scene);
            currentStage.setTitle("Task Manager - Login");
            currentStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/_51c75ae5-dbc9-4096-a010-a1357c9b9d94-removebg-preview.png")));
            currentStage.setWidth(600);
            currentStage.setHeight(400);
            currentStage.setResizable(false);
            currentStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}