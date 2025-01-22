package controllers;

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

        // Populate priority and status ChoiceBoxes
        taskPriority.getItems().addAll("High", "Medium", "Low");
        taskStatus.getItems().addAll("In progress", "Done", "Not started yet");

        // Populate the board ChoiceBox with boards associated with the connected user
        populateBoardChoiceBox();
    }

    private void populateTimeChoiceBox(ChoiceBox<String> timeChoiceBox) {
        for (int hour = 0; hour < 24; hour++) {
            for (int minute = 0; minute < 60; minute += 30) { // 30-minute intervals
                String time = String.format("%02d:%02d", hour, minute); // Format: "HH:mm"
                timeChoiceBox.getItems().add(time);
            }
        }
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

            // Parse time values using the correct format
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm"); // Use "HH:mm" for 24-hour format
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
            dateTimeMap.put("start_date", startDateObj); // Store as Date
            dateTimeMap.put("start_time", startTimeObj); // Store as Date
            dateTimeMap.put("end_date", endDateObj); // Store as Date
            dateTimeMap.put("end_time", endTimeObj); // Store as Date

            // Retrieve priority and status
            Priority priority = Priority.valueOf(taskPriority.getValue().toUpperCase()); // No need for .toUpperCase()
            Status status = Status.valueOf(taskStatus.getValue().toUpperCase()); // No need for .replace(" ", "_")

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
            newTask.setTitle(title);
            newTask.setDescription(description);
            newTask.setDateTime(dateTimeMap); // Set the date and time HashMap
            newTask.setPriority(priority);
            newTask.setStatus(status);
            newTask.setCategoryId(selectedBoard.getId()); // Associate the task with the selected board
            newTask.setUserId(connectedUser.getId()); // Associate the task with the connected user

            // Save the task to the database
            boolean isSaved = Database.saveTask(newTask);
            if (isSaved) {
                System.out.println("Task saved successfully.");
                // Close the dialog
                Stage stage = (Stage) taskTitle.getScene().getWindow();
                stage.close();
            } else {
                System.err.println("Failed to save the task.");
            }

        } catch (DateTimeParseException e) {
            System.err.println("Invalid time format. Please use HH:mm (e.g., 14:30).");
        } catch (NullPointerException e) {
            System.err.println("Please fill out all fields.");
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid priority or status value.");
        }
    }}