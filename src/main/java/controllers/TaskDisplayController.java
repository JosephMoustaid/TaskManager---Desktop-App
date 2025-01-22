package controllers;

import javafx.fxml.FXML;

import javafx.scene.layout.VBox;

import models.Task;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.layout.HBox;

import java.text.SimpleDateFormat;
import java.util.*;

public class TaskDisplayController {

    @FXML
    private VBox timeChunk1; // 12 AM - 6 AM
    @FXML
    private VBox timeChunk2; // 6 AM - 12 PM
    @FXML
    private VBox timeChunk3; // 12 PM - 6 PM
    @FXML
    private VBox timeChunk4; // 6 PM - 12 AM

    private HashSet<Task> tasks;

    public void setTasks(HashSet<Task> tasks) {
        this.tasks = tasks;
        displayTasks();
    }

    private void displayTasks() {
        // Clear all time chunks before adding new tasks
        timeChunk1.getChildren().clear();
        timeChunk2.getChildren().clear();
        timeChunk3.getChildren().clear();
        timeChunk4.getChildren().clear();

        // Loop through each task and add it to the appropriate time chunk
        for (Task task : tasks) {
            Date startTime = task.getDateTime().get("startTime");
            int hour = startTime.getHours();

            if (hour >= 0 && hour < 6) {
                timeChunk1.getChildren().add(createTaskCard(task));
            } else if (hour >= 6 && hour < 12) {
                timeChunk2.getChildren().add(createTaskCard(task));
            } else if (hour >= 12 && hour < 18) {
                timeChunk3.getChildren().add(createTaskCard(task));
            } else {
                timeChunk4.getChildren().add(createTaskCard(task));
            }
        }
    }

    private HBox createTaskCard(Task task) {
        // Create the task card layout (same as before)
        HBox taskCard = new HBox();
        taskCard.setStyle("-fx-background-color: #444; -fx-padding: 10; -fx-spacing: 10;");

        // Task Title
        Text title = new Text(task.getTitle());
        title.setStyle("-fx-fill: white; -fx-font-size: 14; -fx-font-weight: bold;");

        // Task Description
        Text description = new Text(task.getDescription());
        description.setStyle("-fx-fill: white; -fx-font-size: 12;");

        // Format the start and end dates/times in a simple way
        String dateTimeRange = formatDateTimeSimple(
                task.getDateTime().get("startDate"),
                task.getDateTime().get("startTime"),
                task.getDateTime().get("endDate"),
                task.getDateTime().get("endTime")
        );

        // Task Timestamp
        Text timestamp = new Text(dateTimeRange);
        timestamp.setStyle("-fx-fill: #0077ff; -fx-font-size: 10;");

        // Task Priority
        Text priority = new Text("Priority: " + task.getPriority());
        priority.setStyle("-fx-fill: white; -fx-font-size: 12;");

        // Task Status
        Text status = new Text("Status: " + task.getStatus());
        status.setStyle("-fx-fill: white; -fx-font-size: 12;");

        // Edit Button
        Button editButton = new Button("Edit");
        editButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-cursor: hand;");
        editButton.setOnAction(event -> editTask());

        // Delete Button
        Button deleteButton = new Button("Delete");
        deleteButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-cursor: hand;");
        deleteButton.setOnAction(event -> deleteTask());

        // Add all elements to the task card
        VBox taskDetails = new VBox(title, description, timestamp, priority, status);
        taskDetails.setSpacing(5);

        taskCard.getChildren().addAll(taskDetails, editButton, deleteButton);
        return taskCard;
    }

    private String formatDateTimeSimple(Date startDate, Date startTime, Date endDate, Date endTime) {
        if (startDate == null || startTime == null || endDate == null || endTime == null) {
            return "N/A"; // Handle null dates/times gracefully
        }

        // Format for date: "MMM dd"
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd");
        // Format for time: "HH:mm"
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        // Format start and end dates/times
        String start = dateFormat.format(startDate) + " - " + timeFormat.format(startTime);
        String end = dateFormat.format(endDate) + " - " + timeFormat.format(endTime);

        return start + " -> " + end;
    }

    private void editTask() {
        // Edit task logic
    }

    private void deleteTask() {
        // Delete task logic
    }
}