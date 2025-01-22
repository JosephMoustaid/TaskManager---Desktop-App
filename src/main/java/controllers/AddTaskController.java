package controllers;

import auth.AuthManager;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Priority;
import models.Status;
import models.Task;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import auth.AuthManager;
import models.User;

public class AddTaskController {

    private User connectedUser = AuthManager.getAuthenticatedUser();

    @FXML
    private TextField taskTitle;

    @FXML
    private TextField taskDescription;

    @FXML
    private DatePicker startDate;

    @FXML
    private ChoiceBox<String> startTime;

    @FXML
    private DatePicker endDate;

    @FXML
    private ChoiceBox<String> endTime;

    @FXML
    private ChoiceBox<String> taskPriority;

    @FXML
    private ChoiceBox<String> taskStatus;

    private Task newTask; // To store the new task

    @FXML
    public void initialize() {
        // Populate time ChoiceBoxes with time options
        populateTimeChoiceBox(startTime);
        populateTimeChoiceBox(endTime);

        // Populate priority and status ChoiceBoxes
        taskPriority.getItems().addAll("HIGH", "MEDIUM", "LOW");
        taskStatus.getItems().addAll("NOT_STARTED", "IN_PROGRESS", "COMPLETE");
    }

    private void populateTimeChoiceBox(ChoiceBox<String> timeChoiceBox) {
        for (int hour = 0; hour < 24; hour++) {
            for (int minute = 0; minute < 60; minute += 30) { // 30-minute intervals
                String time = String.format("%02d:%02d", hour, minute);
                timeChoiceBox.getItems().add(time);
            }
        }
    }

    @FXML
    public void saveTask() {
        try {
            // Retrieve task details
            String title = taskTitle.getText();
            String description = taskDescription.getText();
            LocalDate startLocalDate = startDate.getValue();
            LocalDate endLocalDate = endDate.getValue();
            LocalTime startLocalTime = LocalTime.parse(startTime.getValue(), DateTimeFormatter.ofPattern("HH:mm"));
            LocalTime endLocalTime = LocalTime.parse(endTime.getValue(), DateTimeFormatter.ofPattern("HH:mm"));

            // Convert LocalDate and LocalTime to Date
            Date startDateObj = Date.from(startLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date startTimeObj = Date.from(startLocalDate.atTime(startLocalTime).atZone(ZoneId.systemDefault()).toInstant());
            Date endDateObj = Date.from(endLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date endTimeObj = Date.from(endLocalDate.atTime(endLocalTime).atZone(ZoneId.systemDefault()).toInstant());

            // Create a map for date and time
            Map<String, Date> dateTimeMap = new HashMap<>();
            dateTimeMap.put("startDate", startDateObj);
            dateTimeMap.put("startTime", startTimeObj);
            dateTimeMap.put("endDate", endDateObj);
            dateTimeMap.put("endTime", endTimeObj);

            // Retrieve priority and status
            Priority priority = Priority.valueOf(taskPriority.getValue());
            Status status = Status.valueOf(taskStatus.getValue());

            // Generate a unique ID for the task (for now, use a simple counter or UUID)
            String taskId = String.valueOf(System.currentTimeMillis()); // Example: Use timestamp as ID

            // Create the new task
            newTask = new Task(taskId, title, description, dateTimeMap, priority, status, connectedUser.getId()); // Replace "userId" with actual user ID

            // Close the dialog
            Stage stage = (Stage) taskTitle.getScene().getWindow();
            stage.close();

        } catch (DateTimeParseException e) {
            System.err.println("Invalid time format. Please use HH:mm (e.g., 14:30).");
        } catch (NullPointerException e) {
            System.err.println("Please fill out all fields.");
        }
    }

    // Method to return the new task
    public Task getNewTask() {
        return newTask;
    }
}