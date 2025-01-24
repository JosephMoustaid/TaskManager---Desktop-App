package controllers;

import TaskManager.Login;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import models.Priority;
import models.Status;
import models.Task;

import java.text.SimpleDateFormat;
import java.util.*;


import auth.AuthManager;
import models.User;
import models.Board;

import utils.Database;

import TaskManager.Login;

public class TaskController {

    @FXML
    private  Button cancelButton;

    @FXML
    private Text selectedBord;

    @FXML
    private VBox boardsContainer; // VBox to hold dynamic board buttons

    private Set<Board> boards; // Stores all boards for the connected user
    private User connectedUser; // Stores the currently connected user


    @FXML
    private VBox timeChunk1; // 12 AM - 6 AM
    @FXML
    private VBox timeChunk2; // 6 AM - 12 PM
    @FXML
    private VBox timeChunk3; // 12 PM - 6 PM
    @FXML
    private VBox timeChunk4; // 6 PM - 12 AM

    // Helper method to create a Map<String, Date> with unique date and time values
    private Map<String, Date> createDateTime(int year, int month, int day, int startHour, int startMinute, int endHour, int endMinute) {
        Map<String, Date> dateTime = new HashMap<>();
        Calendar calendar = Calendar.getInstance();

        // Set start date and time
        calendar.set(year, month - 1, day, startHour, startMinute); // month is 0-based in Calendar
        Date startDate = calendar.getTime();
        Date startTime = calendar.getTime();

        // Set end date and time
        calendar.set(year, month - 1, day, endHour, endMinute); // month is 0-based in Calendar
        Date endDate = calendar.getTime();
        Date endTime = calendar.getTime();

        dateTime.put("startDate", startDate);
        dateTime.put("startTime", startTime);
        dateTime.put("endDate", endDate);
        dateTime.put("endTime", endTime);

        return dateTime;
    }



    @FXML
    public void initialize() {
        if (!AuthManager.verifyToken(AuthManager.getToken())) {
            showAlert("Access Denied", "You are not signed in.");

            // Delay the window closure
            Platform.runLater(() -> {
                closeWindow(); // This will execute after the UI is fully initialized
                openLoginWindow();
            });
        } else {
            // Rest of the logic for authenticated users
            connectedUser = AuthManager.getAuthenticatedUser();
            if (connectedUser != null) {
                System.out.println("Authenticated User: " + connectedUser.getUsername());
                System.out.println("Email: " + connectedUser.getEmail());

                loadBoardsForUser();
                displayBoards();

                if (!boards.isEmpty()) {
                    displayTasksForBoard(boards.iterator().next());
                }
            } else {
                System.out.println("No authenticated user found.");
            }
        }
    }
    private void loadBoardsForUser() {
        // Load all boards from the database
        Set<Board> allBoards = Database.loadBoards();

        // Filter boards to include only those that belong to the connected user
        boards = new HashSet<>();
        for (Board board : allBoards) {
            if (board.getUserId().equals(connectedUser.getId())) {
                boards.add(board);
            }
        }

        // Debugging: Print the boards
        System.out.println("Boards for user " + connectedUser.getUsername() + ":");
        for (Board board : boards) {
            System.out.println(board);
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeWindow() {
        Stage stage = (Stage) timeChunk1.getScene().getWindow();
        stage.close();
    }

    private void displayTasks() {
        if (boards == null || boards.isEmpty()) {
            System.out.println("No boards found for the user.");
            return;
        }

        // For now, display tasks from the first board
        Board firstBoard = boards.iterator().next();
        Set<Task> tasks = firstBoard.getTasks();

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

    private void displayBoards() {
        // Clear only the dynamic board buttons (preserve the title, "New Board" button, and "Sign Out" button)
        // The first 3 children are: Title, "New Board" button, and an empty Text node (spacer)
        // The last 2 children are: "Settings" button and "Sign Out" button
        // We only want to remove the dynamic board buttons added between these elements.

        // Find the index where dynamic board buttons should be added
        int startIndex = 2; // After the title and "New Board" button
        int endIndex = boardsContainer.getChildren().size() - 2; // Before the "Settings" and "Sign Out" buttons

        // Remove only the dynamic board buttons
        boardsContainer.getChildren().remove(startIndex, endIndex);

        // Add a button for each board
        for (Board board : boards) {
            Button boardButton = new Button("Board " + board.getId());
            boardButton.setStyle("-fx-border-radius: 0;");
            boardButton.getStyleClass().add("board-button"); // Add the CSS class
            boardButton.setTextFill(Color.WHITE); // Set text color
            boardButton.setPrefHeight(40.0); // Set preferred height
            boardButton.setPrefWidth(184.0); // Set preferred width
            boardButton.setAlignment(Pos.CENTER_RIGHT); // Align text to the right
            boardButton.setOnAction(event -> handleBoardClick(board));

            // Add icon to the board button
            ImageView boardIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/icons8-board-50.png")));
            boardIcon.setFitHeight(20.0);
            boardIcon.setFitWidth(20.0);
            boardIcon.setPreserveRatio(true);
            boardIcon.setPickOnBounds(true);
            boardButton.setGraphic(boardIcon);

            // Add the board button to the correct position in the VBox
            boardsContainer.getChildren().add(startIndex, boardButton);
        }
    }


    private void displayTasksForBoard(Board board) {
        if (board == null || board.getTasks() == null || board.getTasks().isEmpty()) {
            System.out.println("No tasks found for the board: " + "board" + board.getId());

            selectedBord.setText("Board " + board.getId()); // Update the selected board text

            timeChunk1.getChildren().clear(); // Clear 12 AM - 6 AM
            timeChunk2.getChildren().clear(); // Clear 6 AM - 12 PM
            timeChunk3.getChildren().clear(); // Clear 12 PM - 6 PM
            timeChunk4.getChildren().clear();
            return;
        }

        // Debug: Print the number of tasks in the board
        System.out.println("Number of tasks in board " + board.getId() + ": " + board.getTasks().size());

        selectedBord.setText("Board " + board.getId()); // Update the selected board text

        // Clear all time chunks before adding new tasks
        timeChunk1.getChildren().clear(); // Clear 12 AM - 6 AM
        timeChunk2.getChildren().clear(); // Clear 6 AM - 12 PM
        timeChunk3.getChildren().clear(); // Clear 12 PM - 6 PM
        timeChunk4.getChildren().clear(); // Clear 6 PM - 12 AM

        // Loop through each task and add it to the appropriate time chunk
        for (Task task : board.getTasks()) {
            Map<String, Date> dateTime = task.getDateTime();

            // Debug: Print the task and its dateTime map
            System.out.println("Task: " + task.getTitle());
            System.out.println("DateTime: " + dateTime);

            if (dateTime != null) {
                System.out.println("startDate: " + dateTime.get("start_date"));
                System.out.println("startTime: " + dateTime.get("start_time"));
                System.out.println("endDate: " + dateTime.get("end_date"));
                System.out.println("endTime: " + dateTime.get("end_time"));
            }

            Date startTime = dateTime != null ? dateTime.get("start_time") : null;

            // Skip tasks with null startTime
            if (startTime == null) {
                System.out.println("Skipping task with null startTime: " + task.getTitle());
                continue;
            }

            int hour = startTime.getHours();

            // Debug: Print task details
            System.out.println("Task: " + task.getTitle() + ", Start Time: " + startTime);


            if (hour >= 0 && hour < 6) {
                timeChunk1.getChildren().add(createTaskCard(task)); // Add to 12 AM - 6 AM
            } else if (hour >= 6 && hour < 12) {
                timeChunk2.getChildren().add(createTaskCard(task)); // Add to 6 AM - 12 PM
            } else if (hour >= 12 && hour < 18) {
                timeChunk3.getChildren().add(createTaskCard(task)); // Add to 12 PM - 6 PM
            } else {
                timeChunk4.getChildren().add(createTaskCard(task)); // Add to 6 PM - 12 AM
            }
        }
    }

    private void handleCreateNewBoard() {
        // Logic to create a new board
        System.out.println("Create New Board clicked");
        // You can open a dialog or another view to create a new board
    }

    private void handleBoardClick(Board board) {
        System.out.println("Board clicked: " + " board " + board.getId());
        displayTasksForBoard(board); // Ensure this is called
    }


    private HBox createTaskCard( Task task) {
        // Create the task card layout
        HBox taskCard = new HBox();
        taskCard.setStyle("-fx-background-color: #444; -fx-padding: 10; -fx-spacing: 10; -fx-border-radius: 5; -fx-background-radius: 5;");
        taskCard.setMaxWidth(Double.MAX_VALUE); // Allow the card to grow

        javafx.scene.layout.HBox.setHgrow(taskCard, javafx.scene.layout.Priority.ALWAYS); // Use fully qualified Priority

        // Priority Rectangle (color based on priority)
        Rectangle priorityRectangle = new Rectangle(10, 60); // Width: 10, Height: 60
        switch (task.getPriority()) {
            case HIGH:
                priorityRectangle.setFill(Color.RED);
                break;
            case MEDIUM:
                priorityRectangle.setFill(Color.YELLOW);
                break;
            case LOW:
                priorityRectangle.setFill(Color.GREEN);
                break;
        }
        priorityRectangle.setStroke(Color.TRANSPARENT); // No border

        // Task Details (Title, Description, Timestamp, Priority, Status)
        VBox taskDetails = new VBox();
        taskDetails.setSpacing(5);
        taskDetails.setMaxWidth(Double.MAX_VALUE); // Allow the details to grow

        // Task Title
        Text title = new Text(task.getTitle());
        title.setStyle("-fx-fill: white; -fx-font-size: 14; -fx-font-weight: bold;");

        // Task Description
        Text description = new Text(task.getDescription());
        description.setStyle("-fx-fill: white; -fx-font-size: 12;");
        description.setWrappingWidth(200); // Set a wrapping width for the description text

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

        // Add check emoji if the task is complete
        if (task.getStatus() == Status.COMPLETE) {
            Text checkEmoji = new Text(" ✅"); // Add a check emoji
            checkEmoji.setStyle("-fx-fill: white; -fx-font-size: 12;");
            HBox statusContainer = new HBox(status, checkEmoji); // Combine status and emoji
            statusContainer.setSpacing(5); // Add spacing between status and emoji
            taskDetails.getChildren().add(statusContainer); // Add the combined container to taskDetails
        } else {
            taskDetails.getChildren().add(status); // Add only the status if not complete
        }

        // Add details to the VBox
        taskDetails.getChildren().addAll(title, description, timestamp, priority);

        // Buttons (Edit and Delete)
        HBox buttonContainer = new HBox();
        buttonContainer.setSpacing(5);
        buttonContainer.setAlignment(Pos.BOTTOM_CENTER); // Center buttons at the bottom

        // Edit Button
        Button editButton = new Button("Edit");
        editButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 5 10;");
        editButton.setOnAction(event -> editTask(task));

        // Delete Button
        Button deleteButton = new Button("Delete");
        deleteButton.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-cursor: hand; -fx-padding: 5 10;");
        deleteButton.setOnAction(event -> deleteTask(task));

        // Add buttons to the button container
        buttonContainer.getChildren().addAll(editButton, deleteButton);

        // Create a VBox to hold task details and buttons
        VBox mainContainer = new VBox();
        mainContainer.setSpacing(10);
        mainContainer.getChildren().addAll(taskDetails, buttonContainer); // Add buttons at the bottom

        // Add priority rectangle and main container to the task card
        taskCard.getChildren().addAll(priorityRectangle, mainContainer);

        // Set spacing and alignment
        taskCard.setSpacing(10);
        taskCard.setAlignment(Pos.CENTER_LEFT);
        taskCard.getStyleClass().add("shadow");

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

    @FXML
    public void handleTaskClick(MouseEvent event) {
        System.out.println("Task card clicked");
    }

    @FXML
    public void handleClickAddTask(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/AddTaskDialog.fxml"));
            Parent parent = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Add Task");
            stage.setScene(new Scene(parent));
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void editTask(Task task) {
        try {
            // Load the FXML file for the edit task dialog
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/editTaskDialog.fxml"));
            Parent parent = fxmlLoader.load();

            // Get the controller for the edit task dialog
            EditTaskController editTaskDialogController = fxmlLoader.getController();

            // Pass the selected task to the edit task dialog
            editTaskDialogController.setTask(task);

            // Set up the stage
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Edit Task Dialog");
            stage.setScene(new Scene(parent));
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteTask(Task task) {
        // Show a confirmation dialog before deleting the task
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Task");
        alert.setHeaderText("Are you sure you want to delete this task?");
        alert.setContentText("This action cannot be undone.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // User confirmed deletion, proceed to delete the task
            for(Board board : boards){
                if(board.getTasks().contains(task)){
                    board.getTasks().remove(task);
                    break;
                }
            }
            boolean isDeleted = Database.deleteTask(task); // Delete the task from the database
            if (isDeleted) {

                System.out.println("Task deleted successfully.");
                // Close the dialog
                Stage stage = (Stage) cancelButton.getScene().getWindow();
                stage.close();
            } else {
                // Show an error message if the task could not be deleted
                System.out.println("Failed to delete the task.");
            }
        } else {
            // User canceled the deletion
            System.out.println("Deletion canceled.");
        }
    }

    @FXML
    private void handleClickSettings(ActionEvent event){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/settings.fxml"));
            Parent parent = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Settings");
            stage.setScene(new Scene(parent));

            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

            // Set the stage size to the screen size
            stage.setWidth(screenBounds.getWidth());
            stage.setHeight(screenBounds.getHeight());

            stage.show();

            // Close the current window
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleClickSignOut(ActionEvent event){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
            Parent parent = fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Login");
            stage.setScene(new Scene(parent));
            stage.show();

            AuthManager.clearToken();
            System.out.println("Token cleared. User signed out.");

            // Close the current window
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }






    private void openLoginWindow() {
        try {
            Stage currentStage = (Stage) timeChunk1.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/views/login.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/styles/style.css").toExternalForm());

            currentStage.setScene(scene);
            currentStage.setTitle("Task Manager - Login");
            currentStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/_51c75ae5-dbc9-4096-a010-a1357c9b9d94-removebg-preview.png")));
            currentStage.setWidth(600);
            currentStage.setHeight(400);
            currentStage.setResizable(false);
            currentStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleOpenUserInfoDialog() {
        try {
            // Load the FXML file for the dialog
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UserInfoDialog.fxml"));
            Parent root = loader.load();

            // Get the controller for the dialog
            UserInfoDialogController controller = loader.getController();

            // Create a new stage for the dialog
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Update User Information");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(root));


            dialogStage.setResizable(false);

            //dialogStage.setHeight(600);
            // Set the dialog stage in the controller
            controller.setDialogStage(dialogStage);

            // Show the dialog and wait for the user to close it
            dialogStage.showAndWait();

            // Check if the user clicked Save
            if (controller.isSaveClicked()) {
                // Perform any necessary actions after saving (e.g., update UI or database)
                System.out.println("User information updated.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


