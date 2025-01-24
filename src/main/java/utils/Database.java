
package utils;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import models.User;
import models.Task;
import models.Board;
import models.Status;
import models.Priority;
import models.Notification;
import models.TaskManager;

import org.json.JSONObject;
import java.sql.*;
import java.util.*;

public class Database {


    // Method to load all users from the database
    public static Set<User> loadUsers() {
        Set<User> users = new HashSet<>();
        String query = "SELECT * FROM users";
        try (Connection connection = DatabaseConnection.getConnection()) {
            var statement = connection.createStatement();
            var resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                User user = new User(resultSet.getString("id"), resultSet.getString("username"), resultSet.getString("email"), resultSet.getString("password"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    // Method to load all tasks from the database
    public static Set<Task> loadTasks() {
        Set<Task> tasks = new HashSet<>();
        String query = "SELECT * FROM tasks";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Task task = new Task();

                // Set ID (convert int to String)
                task.setId(String.valueOf(resultSet.getInt("id")));

                // Set title and description
                task.setTitle(resultSet.getString("title"));
                task.setDescription(resultSet.getString("description"));

                // Parse date_time JSONB into Map<String, Date>
                String dateTimeJson = resultSet.getString("date_time");
                if (dateTimeJson != null) {
                    JSONObject jsonObject = new JSONObject(dateTimeJson);
                    Map<String, Date> dateTime = new HashMap<>();
                    dateTime.put("start_date", java.sql.Date.valueOf(jsonObject.getString("start_date")));
                    dateTime.put("start_time", java.sql.Time.valueOf(jsonObject.getString("start_time")));
                    dateTime.put("end_date", java.sql.Date.valueOf(jsonObject.getString("end_date")));
                    dateTime.put("end_time", java.sql.Time.valueOf(jsonObject.getString("end_time")));
                    task.setDateTime(dateTime);
                }

                // Set priority (convert string to enum)
                String priorityString = resultSet.getString("priority");
                Priority priority = Priority.valueOf(priorityString);
                task.setPriority(priority);

                // Set status (convert string to enum)
                String statusString = resultSet.getString("status");
                Status status = Status.valueOf(statusString);
                task.setStatus(status);

                // Set categoryId (convert int to String)
                task.setCategoryId(String.valueOf(resultSet.getInt("category_id")));


                task.setUserId(String.valueOf(resultSet.getInt("user_id")));
                // Add task to the set
                tasks.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tasks;
    }

    public static Set<Board> loadBoards() {
        Set<Board> boards = new HashSet<>();
        Map<Integer, List<Task>> boardTasksMap = new HashMap<>();

        // Load all tasks and group them by their board_id
        Set<Task> allTasks = loadTasks();
        for (Task task : allTasks) {
            int boardId = Integer.parseInt(task.getCategoryId()); // Assuming categoryId represents board_id
            boardTasksMap.computeIfAbsent(boardId, k -> new ArrayList<>()).add(task);
        }

        String query = "SELECT * FROM board";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                // Create a new Board
                Board board = new Board();
                board.setId(String.valueOf(resultSet.getInt("id")));

                board.setUserId(String.valueOf(resultSet.getInt("userid")));
                // Retrieve tasks for the board from the map
                List<Task> tasksForBoard = boardTasksMap.getOrDefault(resultSet.getInt("id"), new ArrayList<>());
                for (Task task : tasksForBoard) {
                    board.addTask(task);
                }

                // Add the board to the set
                boards.add(board);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return boards;
    }


    private static Task loadTaskById(int taskId) {
        String query = "SELECT * FROM tasks WHERE id = ?";
        Task task = null;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, taskId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                task = new Task();
                task.setId(String.valueOf(resultSet.getInt("id")));
                task.setTitle(resultSet.getString("title"));
                task.setDescription(resultSet.getString("description"));

                // Parse date_time JSONB into Map<String, Date>
                String dateTimeJson = resultSet.getString("date_time");
                if (dateTimeJson != null) {
                    JSONObject jsonObject = new JSONObject(dateTimeJson);
                    Map<String, Date> dateTime = new HashMap<>();
                    dateTime.put("start_date", java.sql.Date.valueOf(jsonObject.getString("start_date")));
                    dateTime.put("start_time", java.sql.Time.valueOf(jsonObject.getString("start_time")));
                    dateTime.put("end_date", java.sql.Date.valueOf(jsonObject.getString("end_date")));
                    dateTime.put("end_time", java.sql.Time.valueOf(jsonObject.getString("end_time")));
                    task.setDateTime(dateTime);
                }

                // Set priority (convert string to enum)
                String priorityString = resultSet.getString("priority");
                Priority priority = Priority.valueOf(priorityString);
                task.setPriority(priority);

                // Set status (convert string to enum)
                String statusString = resultSet.getString("status");
                Status status = Status.valueOf(statusString);
                task.setStatus(status);

                task.setUserId(String.valueOf(resultSet.getInt("user_id")));
                // Set categoryId (convert int to String)
                task.setCategoryId(String.valueOf(resultSet.getInt("category_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return task;
    }

    // Method to save User to the database
    public static boolean saveUser(User user) {
        if (user == null) {
            return false;
        }

        String query;
        if (user.getId() != null && userExists(user.getId())) {
            // Update existing user
            query = "UPDATE users SET username = ?, email = ?, password = ? WHERE id = ?";
        } else {
            // Insert new user
            query = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
        }

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            if (user.getId() != null && userExists(user.getId())) {
                // For UPDATE, set parameters in the correct order
                statement.setString(1, user.getUsername());
                statement.setString(2, user.getEmail());
                statement.setString(3, user.getPassword());
                statement.setInt(4, Integer.parseInt(user.getId()));
            } else {
                // For INSERT, set parameters in the correct order
                statement.setString(1, user.getUsername());
                statement.setString(2, user.getEmail());
                statement.setString(3, user.getPassword());
            }

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0; // Return true if the operation was successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean userExists(String userId) {
        if (userId == null) {
            return false; // If userId is null, the user does not exist
        }

        String query = "SELECT id FROM users WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, Integer.parseInt(userId)); // Convert userId to integer
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Method to save Board the database
    public static boolean saveBoard(Board board) {
        if (board == null) {
            return false;
        }

        // Save or update each task in the board
        for (Task task : board.getTasks()) {
            if (!saveTask(task)) { // Save or update the task
                return false; // If any task fails to save, return false
            }
        }

        // Save or update the board itself
        int option = 0;
        // 0 -> adding
        // 1 -> updating

        String query;
        if (boardExists(board.getId())) {
            // Update existing board
            query = "UPDATE board SET tasks = ?, userid = ? WHERE id = ?";
            option = 1;
        } else {
            // Insert new board
            query = "INSERT INTO board (tasks, userid) VALUES (?, ?)";
            option = 0;
        }

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Convert task IDs to an array
            Set<Task> tasks = board.getTasks();
            Integer[] taskIds = tasks.stream()
                    .map(task -> Integer.parseInt(task.getId()))
                    .toArray(Integer[]::new);

            // Create a SQL array for the task IDs
            Array taskIdArray = connection.createArrayOf("integer", taskIds);

            // Debugging: Print the values being set
            if (option == 0) {
                System.out.println("Adding new board to the database");
                statement.setArray(1, taskIdArray); // Set tasks (integer[])
                statement.setInt(2, Integer.parseInt(board.getUserId())); // Set user ID (integer)
            } else {
                System.out.println("Updating existing board in the database");
                statement.setArray(1, taskIdArray); // Set tasks (integer[])
                statement.setInt(2, Integer.parseInt(board.getUserId())); // Set user ID (integer)
                statement.setInt(3, Integer.parseInt(board.getId())); // Set board ID (integer)
            }

            System.out.println("Task IDs: " + Arrays.toString(taskIds));
            System.out.println("Board ID: " + board.getId());

            // Execute the query
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0; // Return true if the operation was successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to check if  Board exists already the database
    private static boolean boardExists(String boardId) {
        int id = Integer.parseInt(boardId);

        String query = "SELECT id FROM board WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id); // Set id (integer)
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // Return true if the board exists
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean saveTask(Task task) {
        if (task == null) {
            return false;
        }

        String query;
        if (taskExists(task.getId())) {
            // Update existing task
            query = "UPDATE tasks SET title = ?, description = ?, date_time = ?::jsonb, priority = ?::priority, status = ?::status, category_id = ?, user_id = ? WHERE id = ?";
        } else {
            // Insert new task
            query = "INSERT INTO tasks (title, description, date_time, priority, status, category_id, user_id) VALUES (?, ?, ?::jsonb, ?::priority, ?::status, ?, ?)";
        }

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Set task details
            statement.setString(1, task.getTitle());
            statement.setString(2, task.getDescription());

            // Convert date_time map to JSON
            JSONObject dateTimeJson = new JSONObject();
            Map<String, Date> dateTime = task.getDateTime();
            if (dateTime != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

                dateTimeJson.put("start_date", dateFormat.format(dateTime.get("startDate")));
                dateTimeJson.put("start_time", timeFormat.format(dateTime.get("startTime")));
                dateTimeJson.put("end_date", dateFormat.format(dateTime.get("endDate")));
                dateTimeJson.put("end_time", timeFormat.format(dateTime.get("endTime")));
            }
            statement.setString(3, dateTimeJson.toString()); // Set JSONB data as a string

            // Cast priority and status to their respective enum types
            statement.setString(4, task.getPriority().toString());
            statement.setString(5, task.getStatus().toString());
            statement.setInt(6, Integer.parseInt(task.getCategoryId()));
            statement.setInt(7, Integer.parseInt(task.getUserId())); // Set user_id

            if (taskExists(task.getId())) {
                statement.setInt(8, Integer.parseInt(task.getId())); // For UPDATE, set the task ID
            }

            // Execute the query
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0; // Return true if the operation was successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateTask(Task task) {
        if (task == null) {
            return false;
        }

        String query;
        if (taskExists(task.getId())) {
            // Update existing task
            query = "UPDATE tasks SET title = ?, description = ?, date_time = ?::jsonb, priority = ?::priority, status = ?::status, category_id = ?, user_id = ? WHERE id = ?";
        } else {
            // Insert new task
            query = "INSERT INTO tasks (title, description, date_time, priority, status, category_id, user_id) VALUES (?, ?, ?::jsonb, ?::priority, ?::status, ?, ?)";
        }

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Set task details
            statement.setString(1, task.getTitle());
            statement.setString(2, task.getDescription());

            // Convert date_time map to JSON
            JSONObject dateTimeJson = new JSONObject();
            Map<String, Date> dateTime = task.getDateTime();
            if (dateTime != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

                dateTimeJson.put("start_date", dateFormat.format(dateTime.get("start_date")));
                dateTimeJson.put("start_time", timeFormat.format(dateTime.get("start_time")));
                dateTimeJson.put("end_date", dateFormat.format(dateTime.get("end_date")));
                dateTimeJson.put("end_time", timeFormat.format(dateTime.get("end_time")));
            }
            statement.setString(3, dateTimeJson.toString()); // Set JSONB data as a string

            // Cast priority and status to their respective enum types
            statement.setString(4, task.getPriority().toString());
            statement.setString(5, task.getStatus().toString());
            statement.setInt(6, Integer.parseInt(task.getCategoryId()));
            statement.setInt(7, Integer.parseInt(task.getUserId())); // Set user_id

            if (taskExists(task.getId())) {
                statement.setInt(8, Integer.parseInt(task.getId())); // For UPDATE, set the task ID
            }

            // Execute the query
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0; // Return true if the operation was successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteTask(Task task) {
        if (task == null) {
            return false;
        }

        // Step 1: Delete the task from the tasks table
        String deleteTaskQuery = "DELETE FROM tasks WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(deleteTaskQuery)) {

            statement.setInt(1, Integer.parseInt(task.getId())); // Set the task ID
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                // Step 2: Remove the task from all boards
                Set<Board> boards = loadBoards(); // Load all boards
                for (Board board : boards) {
                    // Check if the board contains the task
                    if (board.getTasks().removeIf(t -> t.getId().equals(task.getId()))) {
                        // If the task was removed, update the board in the database
                        saveBoard(board);
                    }
                }

                return true; // Task deleted successfully
            } else {
                return false; // Task not found in the database
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean taskExists(String taskId) {
        String query = "SELECT id FROM tasks WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, Integer.parseInt(taskId));
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // Return true if the task exists
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int getMaxTaskId() {
        int maxId = 0;
        String query = "SELECT MAX(id) AS max_id FROM tasks"; // Adjust the table name if necessary

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                maxId = resultSet.getInt("max_id");
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving max task ID: " + e.getMessage());
        }

        return maxId;
    }



    public static boolean updateUser( User user){
        if (user == null) {
            return false;
        }

        String query;

        query = "UPDATE users SET email = ?, password = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Set task details
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());

            statement.setInt(3, Integer.parseInt(user.getId()));


            // Execute the query
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0; // Return true if the operation was successful
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
