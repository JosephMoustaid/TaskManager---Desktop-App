package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.User;
import utils.Database;
import auth.AuthManager; // Import the AuthManager

import java.util.Set;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Text errorMessage;

    @FXML
    private void handleLogin() {
        // Get input values
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        // Validate input
        if (username.isEmpty() || password.isEmpty()) {
            showError("Username and password are required.", javafx.scene.paint.Color.RED);
            return;
        }

        // Check if the user exists in the database
        User authenticatedUser = authenticateUser(username, password);

        if (authenticatedUser != null) {
            // Login successful
            showError("Login successful! Redirecting...", javafx.scene.paint.Color.GREEN);

            // Generate and save the token, and associate it with the user
            String token = AuthManager.generateToken(authenticatedUser);
            System.out.println("Token generated: " + token);

            // Redirect to the main application window
            openMainAppWindow();

            // Close the login window
            Stage currentStage = (Stage) usernameField.getScene().getWindow();
            currentStage.close();
        } else {
            // Login failed
            showError("Invalid username or password.", javafx.scene.paint.Color.RED);
        }
    }

    // Method to authenticate the user
    private User authenticateUser(String username, String password) {
        Set<User> users = Database.loadUsers();

        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user; // Return the authenticated user
            }
        }

        return null; // Return null if no matching user is found
    }

    // Method to display error messages
    private void showError(String message, javafx.scene.paint.Color color) {
        errorMessage.setText(message);
        errorMessage.setFill(color);
        errorMessage.setVisible(true);
    }

    @FXML
    public void handleGoToSignUp(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/signup.fxml"));
            Parent parent = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Sign Up");
            stage.setScene(new Scene(parent));
            stage.show();

            // Close the current window
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Open the main application window
    private void openMainAppWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/index.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Task Manager");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}