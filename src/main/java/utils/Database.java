
package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import models.User;
import models.Status;
import models.Priority;
import models.Category;
import models.Task;
import models.Notification;
import models.Board;
import models.TaskManager;

public class database {
    private static final String URL = "jdbc:postgresql://localhost:5432/TaskManager";
    private static final String USER = "postgres";
    private static final String PASSWORD = "youssef.05.";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Method to load all users from the database
    public static Set<User> loadUsers() {
        Set<User> users = new HashSet<>();
        String query = "SELECT * FROM users";
        try (Connection connection = getConnection()) {
            var statement = connection.createStatement();
            var resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                User user = new User(resultSet.getString("id"), resultSet.getString("username"), resultSet.getString("password"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    /*

    public static void main(String[] args){
        try{
            if(getConnection().isValid(1)){
                System.out.println("Connected to the database");
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    */

}
