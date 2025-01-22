package controllers;

import models.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import models.Priority;
import models.Status;
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

    private Task task; // Task object to be edited

    public void initialize(Task task) {
        this.task = task;

        // Populate fields with task data
        taskTitle.setText(task.getTitle());
        taskDescription.setText(task.getDescription());
        startDate.setValue(convertToLocalDate(task.getDateTime().get("startDate")));
        startTime.setValue(convertToLocalTime(task.getDateTime().get("startTime")).toString());
        endDate.setValue(convertToLocalDate(task.getDateTime().get("endDate")));
        endTime.setValue(convertToLocalTime(task.getDateTime().get("endTime")).toString());
        taskPriority.setValue(task.getPriority().name());
        taskStatus.setValue(task.getStatus().name());
    }

    @FXML
    private void updateTask() {
        // Update task with new values
        task.setTitle(taskTitle.getText());
        task.setDescription(taskDescription.getText());

        Map<String, Date> dateTime = new HashMap<>();
        dateTime.put("startDate", convertToDate(startDate.getValue()));
        dateTime.put("startTime", convertToDate(startTime.getValue()));
        dateTime.put("endDate", convertToDate(endDate.getValue()));
        dateTime.put("endTime", convertToDate(endTime.getValue()));
        task.setDateTime(dateTime);

        task.setPriority(Priority.valueOf(taskPriority.getValue()));
        task.setStatus(Status.valueOf(taskStatus.getValue()));

        // Save task to the data source or model
        System.out.println("Task updated: " + task);
    }

    private LocalDate convertToLocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private LocalTime convertToLocalTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
    }

    private Date convertToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private Date convertToDate(String time) {
        LocalTime localTime = LocalTime.parse(time);
        return Date.from(localTime.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant());
    }
}