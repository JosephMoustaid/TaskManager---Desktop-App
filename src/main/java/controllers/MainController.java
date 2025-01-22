package controllers;

import models.Board;
import models.Task;
import models.User;
import utils.Database;
import auth.AuthManager;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class MainController {

    private Set<Board> boards; // Stores all boards
    private User connectedUser; // Stores the currently connected user

    public MainController() {
        this.boards = new HashSet<>();
        loadData(); // Load data from the database when the controller is initialized
    }

    // Load data from the database
    private void loadData() {
        // Load the connected user from AuthManager
        connectedUser = AuthManager.getAuthenticatedUser();

        // Load boards and tasks from the database
        boards = Database.loadBoards();
    }

    // Get the connected user
    public User getConnectedUser() {
        return connectedUser;
    }

    // Add a new board
    public boolean addBoard(Board board) {
        if (board == null) {
            throw new IllegalArgumentException("Board cannot be null");
        }
        if (Database.saveBoard(board)) { // Save the board to the database
            boards.add(board); // Add the board to the local set
            return true;
        }
        return false;
    }

    // Remove a board
    public boolean removeBoard(Board board) {
        if (board == null) {
            throw new IllegalArgumentException("Board cannot be null");
        }
        if (Database.saveBoard(board)) { // Save the board to the database
            return boards.remove(board); // Remove the board from the local set
        }
        return false;
    }

    // Get all boards
    public Set<Board> getBoards() {
        return boards;
    }

    // Add a task to a specific board
    public boolean addTaskToBoard(String boardId, Task task) {
        Board board = findBoardById(boardId);
        if (board != null) {
            task.setId(UUID.randomUUID().toString()); // Generate a unique ID for the task
            if (board.addTask(task)) { // Add the task to the local board
                return Database.saveBoard(board); // Save the updated board to the database
            }
        }
        return false;
    }

    // Edit a task in a specific board
    public boolean editTaskInBoard(String boardId, Task oldTask, Task newTask) {
        Board board = findBoardById(boardId);
        if (board != null) {
            Task task = board.getTasks().stream()
                    .filter(t -> t.equals(oldTask))
                    .findFirst()
                    .orElse(null);
            if (task != null) {
                copyTaskFields(newTask, task); // Copy fields from newTask to task
                return Database.saveBoard(board); // Save the updated board to the database
            }
        }
        return false;
    }

    // Remove a task from a specific board
    public boolean removeTaskFromBoard(String boardId, Task task) {
        Board board = findBoardById(boardId);
        if (board != null) {
            if (board.removeTask(task)) { // Remove the task from the local board
                return Database.saveBoard(board); // Save the updated board to the database
            }
        }
        return false;
    }

    // Copy fields from one task to another
    private void copyTaskFields(Task source, Task target) {
        target.setTitle(source.getTitle());
        target.setDescription(source.getDescription());
        target.setPriority(source.getPriority());
        target.setDateTime(source.getDateTime());
        target.setStatus(source.getStatus());
    }

    // Find a board by its ID
    private Board findBoardById(String boardId) {
        for (Board board : boards) {
            if (board.getId().equals(boardId)) {
                return board;
            }
        }
        return null;
    }

    // Get all tasks from a specific board
    public Set<Task> getTasksFromBoard(String boardId) {
        Board board = findBoardById(boardId);
        if (board != null) {
            return board.getTasks();
        }
        return new HashSet<>();
    }

    // Get tasks by status from a specific board
    public Set<Task> getTasksByStatus(String boardId, String status) {
        Board board = findBoardById(boardId);
        if (board != null) {
            switch (status.toUpperCase()) {
                case "NOT_STARTED":
                    return board.getNotStartedTasks();
                case "IN_PROGRESS":
                    return board.getIinProgressTasks();
                case "COMPLETE":
                    return board.getCompleteTasks();
                default:
                    throw new IllegalArgumentException("Invalid status: " + status);
            }
        }
        return new HashSet<>();
    }

    // Get tasks by priority from a specific board
    public Set<Task> getTasksByPriority(String boardId, String priority) {
        Board board = findBoardById(boardId);
        if (board != null) {
            switch (priority.toUpperCase()) {
                case "LOW":
                    return board.getLowPriorityTasks();
                case "MEDIUM":
                    return board.getMediumPriorityTasks();
                case "HIGH":
                    return board.getHighPriorityTasks();
                default:
                    throw new IllegalArgumentException("Invalid priority: " + priority);
            }
        }
        return new HashSet<>();
    }
}