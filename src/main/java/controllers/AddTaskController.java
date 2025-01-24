package controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Board;
import models.Task;
import models.Priority;
import models.Status;
import models.User;
import auth.AuthManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import utils.Database;

public class AddTaskController {

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
    private ChoiceBox<String> boardChoice; // ChoiceBox for selecting a board

    private User connectedUser = AuthManager.getAuthenticatedUser(); // Get the connected user

    @FXML
    public void initialize() {


        // Populate time ChoiceBoxes with time options
        populateTimeChoiceBox(startTime);
        populateTimeChoiceBox(endTime);

        startDate.setValue(LocalDate.now());
        endDate.setValue(LocalDate.now());

        taskPriority.getItems().setAll("HIGH", "MEDIUM", "LOW");
        taskStatus.getItems().setAll("NOT_STARTED", "IN_PROGRESS", "COMPLETE");

        taskPriority.setValue("MEDIUM"); // Default priority
        taskStatus.setValue("NOT_STARTED"); // Default status


        // Populate the board ChoiceBox with boards associated with the connected user
        populateBoardChoiceBox();
    }

    private void populateTimeChoiceBox(ChoiceBox<String> timeChoiceBox) {
        // Define the 48 time options (every 30 minutes, with seconds set to 00)
        List<String> timeOptions = Arrays.asList(
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

        // Add the time options to the ChoiceBox
        timeChoiceBox.getItems().setAll(timeOptions);
    }


    private void populateBoardChoiceBox() {
        // Load boards associated with the connected user
        List<Board> userBoards = new ArrayList<>();
        for (Board board : Database.loadBoards()) {
            if (board.getUserId().equals(connectedUser.getId())) {
                userBoards.add(board);
            }
        }

        // Add boards to the ChoiceBox in the format "board {id}"
        for (Board board : userBoards) {
            boardChoice.getItems().add("board " + board.getId());
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

            // Debug: Print date values
            System.out.println("Start Date: " + startLocalDate);
            System.out.println("End Date: " + endLocalDate);

            // Validate that all required fields are filled out
            if (title == null || title.trim().isEmpty()) {
                throw new NullPointerException("Title must not be empty.");
            }
            if (description == null || description.trim().isEmpty()) {
                throw new NullPointerException("Description must not be empty.");
            }
            if (startLocalDate == null) {
                throw new NullPointerException("Start date must not be null.");
            }
            if (endLocalDate == null) {
                throw new NullPointerException("End date must not be null.");
            }
            if (startTime.getValue() == null) {
                throw new NullPointerException("Start time must not be null.");
            }
            if (endTime.getValue() == null) {
                throw new NullPointerException("End time must not be null.");
            }
            if (taskPriority.getValue() == null) {
                throw new NullPointerException("Priority must not be null.");
            }
            if (taskStatus.getValue() == null) {
                throw new NullPointerException("Status must not be null.");
            }
            if (boardChoice.getValue() == null) {
                throw new NullPointerException("Board must not be null.");
            }

            // Parse time values using the correct format
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss"); // Use "HH:mm:ss" for 24-hour format with seconds
            LocalTime startLocalTime = LocalTime.parse(startTime.getValue(), timeFormatter);
            LocalTime endLocalTime = LocalTime.parse(endTime.getValue(), timeFormatter);

            // Combine date and time into LocalDateTime
            LocalDateTime startDateTime = LocalDateTime.of(startLocalDate, startLocalTime);
            LocalDateTime endDateTime = LocalDateTime.of(endLocalDate, endLocalTime);

            // Convert LocalDateTime to Date
            Date startDateObj = Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant());
            Date startTimeObj = Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant());
            Date endDateObj = Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant());
            Date endTimeObj = Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant());

            // Create a HashMap for date and time
            Map<String, Date> dateTimeMap = new HashMap<>();
            dateTimeMap.put("startDate", startDateObj); // Store as Date
            dateTimeMap.put("startTime", startTimeObj); // Store as Date
            dateTimeMap.put("endDate", endDateObj); // Store as Date
            dateTimeMap.put("endTime", endTimeObj); // Store as Date

            // Retrieve priority and status from ChoiceBox
            String priorityValue = taskPriority.getValue();
            String statusValue = taskStatus.getValue();

            // Debug: Print priority and status values
            System.out.println("Priority Value: " + priorityValue);
            System.out.println("Status Value: " + statusValue);

            // Map priority value to Priority enum
            Priority priority;
            switch (priorityValue) {
                case "HIGH":
                    priority = Priority.HIGH;
                    break;
                case "MEDIUM":
                    priority = Priority.MEDIUM;
                    break;
                case "LOW":
                    priority = Priority.LOW;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid priority value: " + priorityValue);
            }

            // Map status value to Status enum
            Status status;
            switch (statusValue) {
                case "NOT_STARTED":
                    status = Status.NOT_STARTED;
                    break;
                case "IN_PROGRESS":
                    status = Status.IN_PROGRESS;
                    break;
                case "COMPLETE":
                    status = Status.COMPLETE;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid status value: " + statusValue);
            }

            // Debug: Print priority and status
            System.out.println("Priority: " + priority);
            System.out.println("Status: " + status);

            // Retrieve the selected board
            String selectedBoardName = boardChoice.getValue();

            // Load the selected board from the database
            Board selectedBoard = null;
            for (Board board : Database.loadBoards()) {
                if (selectedBoardName.equals("board " + board.getId())) {
                    selectedBoard = board;
                    break;
                }
            }

            if (selectedBoard == null) {
                System.err.println("Selected board not found.");
                return;
            }

            // Create the new task
            Task newTask = new Task();
            newTask.setId(String.valueOf(Database.getMaxTaskId() + 1)); // Set the task ID to max ID + 1
            newTask.setTitle(title);
            newTask.setDescription(description);
            newTask.setDateTime(dateTimeMap); // Set the date and time HashMap
            newTask.setPriority(priority);
            newTask.setStatus(status);
            newTask.setCategoryId("1"); // Associate the task with the selected board
            newTask.setUserId(connectedUser.getId()); // Associate the task with the connected user

            // Debug: Print the new task
            System.out.println("New Task: " + newTask);

            // Save the task to the database
            boolean isSaved = Database.saveTask(newTask);


            if (isSaved) {
                System.out.println("Task saved successfully.");

                //selectedBoard.
                // Close the dialog
                Stage stage = (Stage) taskTitle.getScene().getWindow();
                stage.close();
            } else {
                System.err.println("Failed to save the task.");
            }

        } catch (DateTimeParseException e) {
            System.err.println("Invalid time format. Please use HH:mm:ss (e.g., 14:30:00).");
        } catch (NullPointerException e) {
            System.err.println("Please fill out all fields: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid priority or status value: " + e.getMessage());
        }
    }


}