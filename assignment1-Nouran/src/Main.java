import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean isLoggedIn = false;
        String username = null;

        while (true) {
            if (!isLoggedIn) {
                System.out.println("Bookstore Menu:");
                System.out.println("1.Login");
                System.out.println("2.Register");
                System.out.println("0.Exit");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine();

                if (choice == 1) {
                    System.out.print("Enter your username: ");
                    String loginUsername = scanner.nextLine();
                    System.out.print("Enter your password: ");
                    String password = scanner.nextLine();

                    if (isValidLogin(loginUsername, password)) {
                        isLoggedIn = true;
                        username = loginUsername;
                        System.out.println("Login successful!");
                    } else {
                        System.out.println("Invalid username or password.");
                    }
                }
                else if (choice == 2) {
                    System.out.print("Set your username: ");
                    String loginUsername = scanner.nextLine();
                    System.out.print("Set your password: ");
                    String password = scanner.nextLine();
                    System.out.print("Set your Role(client/admin): ");
                    String role = scanner.nextLine();
                }
                else if (choice == 0) {
                    System.out.println("Exiting the Bookstore Application. Goodbye!");
                    System.exit(0);
                }
                else {
                    System.out.println("Invalid choice. Please try again.");
                }
            }
            else {
                System.out.println("Welcome, " + username + "!");
                System.out.println("Bookstore Menu:");
                System.out.println("1. Browse and Search Books");
                System.out.println("2. Add Book");
                System.out.println("3. Remove Book");
                System.out.println("4. Make Request");
                System.out.println("5. Accept/Reject Request");
                System.out.println("6. Request History");
                System.out.println("7. Library Overall Statistics");
                System.out.println("8. Logout");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                if (choice == 1) {
                    // Handle browsing and searching books
                }
                else if (choice == 2) {
                    // Handle adding a book
                }
                else if (choice == 3) {
                    // Handle removing a book
                }
                else if (choice == 4) {
                    // Handle submitting a request
                }
                else if (choice == 5) {
                    // Handle accepting/rejecting a request
                }
                else if (choice == 6) {
                    // Handle request history
                }
                else if (choice == 7) {
                    // Handle library overall statistics
                }
                else if (choice == 8) {
                    isLoggedIn = false;
                    username = null;
                    System.out.println("Logged out successfully.");
                }
                else {
                    System.out.println("Invalid choice. Please try again.");
                }
            }
        }
    }

    public static boolean isValidLogin(String username, String password) {
        return true;
    }
}