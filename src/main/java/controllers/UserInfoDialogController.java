package controllers;

import auth.AuthManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.User;
import utils.Database;

public class UserInfoDialogController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    private Stage dialogStage;
    private boolean saveClicked = false;

    User connectedUser ;

    public void initialize() {
        if (AuthManager.verifyToken(AuthManager.getToken())) {
            // Rest of the logic for authenticated users
            connectedUser = AuthManager.getAuthenticatedUser();
            if (connectedUser != null) {
                System.out.println("Authenticated User: " + connectedUser.getUsername());
                System.out.println("Email: " + connectedUser.getEmail());

                // Set the user info fields
                usernameField.setText(connectedUser.getUsername());
                usernameField.setEditable(false);
                emailField.setText(connectedUser.getEmail());

                passwordField.setText(connectedUser.getPassword());

            } else {
                System.out.println("No authenticated user found.");
            }
        }
    }
    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage the stage for this dialog
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Returns true if the user clicked Save, false otherwise.
     *
     * @return true if Save was clicked, false otherwise
     */
    public boolean isSaveClicked() {
        return saveClicked;
    }

    /**
     * Called when the user clicks Save.
     */
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

        saveClicked = true;

        // Update the connected user
        connectedUser.setEmail(email);
        connectedUser.setPassword(password);

        // Save the updated user to the database
        AuthManager.updateUser(connectedUser);

        Database.updateUser(connectedUser);

        showAlert("Success", "Your information has been updated.");

        dialogStage.close();
    }

    /**
     * Called when the user clicks Cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Validates the email format.
     *
     * @param email the email to validate
     * @return true if the email is valid, false otherwise
     */
    private boolean isValidEmail(String email) {
        // Simple email validation regex
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    /**
     * Shows an alert dialog with the given title and message.
     *
     * @param title   the title of the alert
     * @param message the message to display
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}