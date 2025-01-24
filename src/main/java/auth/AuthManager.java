package auth;

import models.User;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

public class AuthManager {
    private static String authToken = null;
    private static User authenticatedUser = null; // Store the authenticated user
    private static final String TOKEN_FILE = "auth_token.txt"; // File to store the token
    private static final String USER_FILE = "auth_user.ser"; // File to store the user

    // Generate a token and associate it with the user
    public static String generateToken(User user) {
        authToken = UUID.randomUUID().toString();
        authenticatedUser = user; // Store the authenticated user
        saveToken(authToken); // Save the token to a file
        saveUser(user); // Save the user to a file
        return authToken;
    }

    // Get the current token
    public static String getToken() {
        if (authToken == null) {
            authToken = loadToken(); // Load the token from a file
        }
        return authToken;
    }

    // Get the authenticated user
    public static User getAuthenticatedUser() {
        if (authenticatedUser == null) {
            authenticatedUser = loadUser(); // Load the user from a file
        }
        return authenticatedUser;
    }

    // Verify the token
    public static boolean verifyToken(String token) {
        return authToken != null && authToken.equals(token);
    }

    // Clear the token and user (e.g., on sign-out)
    public static void clearToken() {
        authToken = null;
        authenticatedUser = null;
        new File(TOKEN_FILE).delete(); // Delete the token file
        new File(USER_FILE).delete(); // Delete the user file
    }

    // Save the token to a file
    private static void saveToken(String token) {
        try (FileWriter writer = new FileWriter(TOKEN_FILE)) {
            writer.write(token);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load the token from a file
    private static String loadToken() {
        try {
            return new String(Files.readAllBytes(Paths.get(TOKEN_FILE)));
        } catch (IOException e) {
            return null; // Return null if the file doesn't exist or cannot be read
        }
    }

    // Save the user to a file
    private static void saveUser(User user) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USER_FILE))) {
            oos.writeObject(user); // Serialize the user object
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load the user from a file
    private static User loadUser() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USER_FILE))) {
            return (User) ois.readObject(); // Deserialize the user object
        } catch (IOException | ClassNotFoundException e) {
            return null; // Return null if the file doesn't exist or cannot be read
        }
    }

    public static void updateUser(User user) {
        authenticatedUser = user;
        saveUser(user);
    }
}