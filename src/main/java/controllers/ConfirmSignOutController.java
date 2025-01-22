package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ConfirmSignOutController {

    @FXML
    private Button confirmButton;

    @FXML
    private Button cancelButton;

    @FXML
    private void initialize() {
        confirmButton.setOnAction(this::confirmSignOut);
        cancelButton.setOnAction(this::closeDialog);
    }

    @FXML
    public void confirmSignOut(ActionEvent event) {
        // Logic to handle sign out
        System.out.println("User signed out.");
        closeDialog(event);
    }

    @FXML
    public void closeDialog(ActionEvent event) {
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
    }
}