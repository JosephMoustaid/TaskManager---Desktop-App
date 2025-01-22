package auth.Main;

import models.User;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    private static User currentUser = null; // Track the currently logged-in user

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\nWelcome to the Authentication System!");
            System.out.println("1. Sign Up");
            System.out.println("2. Sign In");
            System.out.println("3. Sign Out");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    handleSignUp(scanner);
                    break;
                case 2:
                    handleSignIn(scanner);
                    break;
                case 3:
                    handleSignOut();
                    break;
                case 4:
                    running = false;
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close();
    }

    // Handle Sign-Up
    private static void handleSignUp(Scanner scanner) {
        System.out.println("\n--- Sign Up ---");

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        boolean isSignedUp = signUp(username , email, password);

        if (isSignedUp) {
            System.out.println("Sign-Up Successful! You can now sign in.");
        } else {
            System.out.println("Sign-Up Failed. The username or email may already be in use.");
        }
    }

    // Handle Sign-In
    private static void handleSignIn(Scanner scanner) {
        if (currentUser != null) {
            System.out.println("You are already signed in as: " + currentUser.getUsername());
            return;
        }

        System.out.println("\n--- Sign In ---");
        System.out.print("Enter email: ");
        String email = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User authenticatedUser = signIn(email, password);

        if (authenticatedUser != null) {
            currentUser = authenticatedUser;
            System.out.println("Sign-In Successful! Welcome, " + currentUser.getUsername() + ".");
        } else {
            System.out.println("Sign-In Failed. Invalid email or password.");
        }
    }

    // Handle Sign-Out
    private static void handleSignOut() {
        if (currentUser == null) {
            System.out.println("No user is currently signed in.");
        } else {
            System.out.println("Signing out user: " + currentUser.getUsername());
            currentUser = null;
            System.out.println("Sign-Out Successful!");
        }
    }

    // Sign-Up Logic
    private static boolean signUp(String username, String email, String password) {
        String query = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            statement.setString(2, email);
            statement.setString(3, password); // In a real app, hash the password before saving

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0; // Return true if the user was successfully registered
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Sign-In Logic
    private static User signIn(String email, String password) {
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, email);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getString("id"));
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                return user; // Return the authenticated user
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if authentication fails
    }
}