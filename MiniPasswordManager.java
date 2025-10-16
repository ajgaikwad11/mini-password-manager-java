import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class MiniPasswordManager {

    // User database: username -> [hashedPassword, salt]
    private static Map<String, String[]> userDatabase = new HashMap<>();
    // User credentials storage: username -> Map<account, password>
    private static Map<String, Map<String, String>> credentialDatabase = new HashMap<>();
    private static Scanner scanner = new Scanner(System.in);
    private static final int MAX_ATTEMPTS = 3;

    public static void main(String[] args) {
        System.out.println("=== Welcome to Mini Password Manager ===");

        while (true) {
            System.out.println("\n1. Register\n2. Login\n3. Exit");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> register();
                case "2" -> login();
                case "3" -> {
                    System.out.println("Exiting... Stay secure!");
                    return;
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // Register a new user
    private static void register() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        if (userDatabase.containsKey(username)) {
            System.out.println("Username already exists!");
            return;
        }

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        String salt = generateSalt();
        String hashedPassword = hashPassword(password, salt);

        userDatabase.put(username, new String[]{hashedPassword, salt});
        credentialDatabase.put(username, new HashMap<>());

        System.out.println("User registered successfully!");
    }

    // Login a user
    private static void login() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        if (!userDatabase.containsKey(username)) {
            System.out.println("User not found!");
            return;
        }

        int attempts = 0;
        while (attempts < MAX_ATTEMPTS) {
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            String[] stored = userDatabase.get(username);
            String hashedPassword = stored[0];
            String salt = stored[1];

            if (hashedPassword.equals(hashPassword(password, salt))) {
                System.out.println("Password correct!");

                if (simulate2FA()) {
                    System.out.println("Login successful! Welcome, " + username + "!");
                    credentialMenu(username);
                } else {
                    System.out.println("2FA verification failed!");
                }
                return;
            } else {
                attempts++;
                System.out.println("Incorrect password! Attempts left: " + (MAX_ATTEMPTS - attempts));
            }
        }
        System.out.println("Maximum login attempts reached. Try again later.");
    }

    // Menu to manage user credentials
    private static void credentialMenu(String username) {
        while (true) {
            System.out.println("\n1. Add Credential\n2. View Credentials\n3. Logout");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> addCredential(username);
                case "2" -> viewCredentials(username);
                case "3" -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void addCredential(String username) {
        System.out.print("Enter account name (e.g., Gmail, GitHub): ");
        String account = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        // Hash the stored password for security
        String salt = generateSalt();
        String hashedPassword = hashPassword(password, salt);

        credentialDatabase.get(username).put(account, hashedPassword + ":" + salt);
        System.out.println("Credential added successfully!");
    }

    private static void viewCredentials(String username) {
        Map<String, String> creds = credentialDatabase.get(username);
        if (creds.isEmpty()) {
            System.out.println("No credentials stored.");
            return;
        }

        System.out.println("\nStored credentials (passwords are hashed):");
        for (Map.Entry<String, String> entry : creds.entrySet()) {
            System.out.println("Account: " + entry.getKey() + " | Hashed Password: " + entry.getValue().split(":")[0]);
        }
    }

    // Generate random salt
    private static String generateSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    // Hash password with SHA-256 + salt
    private static String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] hashed = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashed);
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    // Simulate a simple 2FA
    private static boolean simulate2FA() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        System.out.println("2FA Code (simulated): " + code);
        System.out.print("Enter 2FA code: ");
        int userInput = scanner.nextInt();
        scanner.nextLine(); // consume newline
        return userInput == code;
    }
}
