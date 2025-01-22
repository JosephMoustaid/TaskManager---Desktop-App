package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Priority;
import models.Status;
import models.Task;

import utils.Database;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EditTaskController {

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

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;


    private ObservableList<String> timeOptions;

    public void initialize() {
        // Manually define the 48 time options (every 30 minutes, seconds set to 00)
        timeOptions = FXCollections.observableArrayList(
                "00:00:00", "00:30:00",
                "01:00:00", "01:30:00",
                "02:00:00", "02:30:00",
                "03:00:00", "03:30:00",
                "04:00:00", "04:30:00",
                "05:00:00", "05:30:00",
                "06:00:00", "06:30:00",
                "07:00:00", "07:30:00",
                "08:00:00", "08:30:00",
                "09:00:00", "09:30:00",
                "10:00:00", "10:30:00",
                "11:00:00", "11:30:00",
                "12:00:00", "12:30:00",
                "13:00:00", "13:30:00",
                "14:00:00", "14:30:00",
                "15:00:00", "15:30:00",
                "16:00:00", "16:30:00",
                "17:00:00", "17:30:00",
                "18:00:00", "18:30:00",
                "19:00:00", "19:30:00",
                "20:00:00", "20:30:00",
                "21:00:00", "21:30:00",
                "22:00:00", "22:30:00",
                "23:00:00", "23:30:00"
        );

        // Set the time options for the ChoiceBoxes
        startTime.setItems(timeOptions);
        endTime.setItems(timeOptions);

        // Set default values
        startTime.setValue("09:00:00");
        endTime.setValue("17:00:00");
    }


    private Task task; // Task object to be edited

    public void setTask(Task task) {
        this.task = task;
        populateFields();
    }

    private void populateFields() {
        // Populate fields with task data
        taskTitle.setText(task.getTitle());
        taskDescription.setText(task.getDescription());

        // Populate date and time fields
        Map<String, Date> dateTime = task.getDateTime();
        startDate.setValue(convertToLocalDate(dateTime.get("start_date")));
        startTime.setValue(convertToLocalTime(dateTime.get("start_time")).toString());
        endDate.setValue(convertToLocalDate(dateTime.get("end_date")));
        endTime.setValue(convertToLocalTime(dateTime.get("end_time")).toString());

        // Populate priority and status choice boxes
        taskPriority.getItems().setAll("HIGH", "MEDIUM", "LOW");
        taskPriority.setValue(task.getPriority().name());

        taskStatus.getItems().setAll("NOT_STARTED", "IN_PROGRESS", "COMPLETE");
        taskStatus.setValue(task.getStatus().name());
    }

    @FXML
    private void handleSave() {
        // Update task with new values
        task.setTitle(taskTitle.getText());
        task.setDescription(taskDescription.getText());

        // Update date and time
        Map<String, Date> dateTime = new HashMap<>();
        dateTime.put("start_date", convertToDate(startDate.getValue()));
        dateTime.put("start_time", convertToDate(startTime.getValue()));
        dateTime.put("end_date", convertToDate(endDate.getValue()));
        dateTime.put("end_time", convertToDate(endTime.getValue()));
        task.setDateTime(dateTime);

        // Update priority and status
        task.setPriority(Priority.valueOf(taskPriority.getValue()));
        task.setStatus(Status.valueOf(taskStatus.getValue()));

        // Debug: Print the updated task
        System.out.println("Task updated: " + task);

        Database.updateTask(task); // Save the updated task to the database
        // Close the dialog
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleCancel() {
        // Close the dialog without saving
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }


    private LocalDate convertToLocalDate(Date date) {
        if (date instanceof java.sql.Date) {
            // Handle java.sql.Date
            return ((java.sql.Date) date).toLocalDate();
        } else {
            // Handle java.util.Date
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
    }

    private LocalTime convertToLocalTime(Date date) {
        if (date instanceof java.sql.Time) {
            // Handle java.sql.Time
            return ((java.sql.Time) date).toLocalTime();
        } else if (date instanceof java.sql.Date) {
            // Handle java.sql.Date (no time information)
            return LocalTime.MIDNIGHT; // Default to midnight
        } else {
            // Handle java.util.Date
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
        }
    }

    private Date convertToDate(LocalDate localDate) {
        return java.sql.Date.valueOf(localDate);
    }

    private Date convertToDate(String time) {
        LocalTime localTime = LocalTime.parse(time);
        return java.sql.Time.valueOf(localTime);
    }
}