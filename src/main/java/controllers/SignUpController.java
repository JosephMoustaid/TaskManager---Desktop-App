package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.User;
import utils.Database;

import javafx.event.ActionEvent;

public class SignUpController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Text errorMessage;

    @FXML
    private Button signupButton;

    // Method to handle the sign-up button click
    @FXML
    private void handleSignUp() {
        // Get input values
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();

        // Validate input
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showError("All fields are required.", javafx.scene.paint.Color.RED);
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match.", javafx.scene.paint.Color.RED);
            return;
        }

        if (!isValidEmail(email)) {
            showError("Invalid email address.", javafx.scene.paint.Color.RED);
            return;
        }

        // Create a new User object with a null ID (let the database generate it)
        User newUser = new User(null, username, email, password);

        // Save the user to the database
        boolean isSaved = Database.saveUser(newUser);

        if (isSaved) {
            // Clear the form and show success message
            clearForm();
            showError("Sign-up successful! You can now log in.", javafx.scene.paint.Color.GREEN);
        } else {
            showError("Sign-up failed. Please try again.", javafx.scene.paint.Color.RED);
        }
    }

    // Method to validate email format
    private boolean isValidEmail(String email) {
        // Simple email validation regex
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    // Method to display error messages
    private void showError(String message , javafx.scene.paint.Color color) {
        errorMessage.setText(message);
        errorMessage.setFill(color);
        errorMessage.setVisible(true);
    }

    // Method to clear the form
    private void clearForm() {
        usernameField.clear();
        emailField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        errorMessage.setVisible(false);
    }

    @FXML
    public void handleClickGoLogin(ActionEvent event){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
            Parent parent = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Login");
            stage.setScene(new Scene(parent));
            stage.show();

            // Close the current window
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}