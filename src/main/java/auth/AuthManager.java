package auth.Main;

import java.util.UUID;

public class AuthManager {
    private static String authToken = null;

    // Generate a token
    public static String generateToken() {
        authToken = UUID.randomUUID().toString();
        return authToken;
    }

    // Get the current token
    public static String getToken() {
        return authToken;
    }

    // Verify the token
    public static boolean verifyToken(String token) {
        return authToken != null && authToken.equals(token);
    }

    // Clear the token (e.g., on sign-out)
    public static void clearToken() {
        authToken = null;
    }
}
