import java.sql.*;
import java.util.Scanner;

public class MedicineTrackingSystem {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/MedicineDB";
    private static final String JDBC_USER = "root";  // MySQL username
    private static final String JDBC_PASSWORD = "Pooja@125";  // MySQL password

    // Main method to run the application
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            // Menu
	    System.out.println("---------------Medicine Tracking System---------------");
            System.out.println("1. Add Medicine");
            System.out.println("2. View All Medicines");
            System.out.println("3. Update Medicine");
            System.out.println("4. Delete Medicine");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character

            switch (choice) {
                case 1:
                    addMedicine(scanner);
                    break;
                case 2:
                    viewAllMedicines();
                    break;
                case 3:
                    updateMedicine(scanner);
                    break;
                case 4:
                    deleteMedicine(scanner);
                    break;
                case 5:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice, try again.");
            }
        }
    }

    // Method to get a connection to the database
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    // Method to add medicine to the database
    private static void addMedicine(Scanner scanner) {
        System.out.print("Enter medicine name: ");
        String name = scanner.nextLine();
        System.out.print("Enter batch number: ");
        String batchNumber = scanner.nextLine();
        System.out.print("Enter expiry date (yyyy-mm-dd): ");
        String expiryDateStr = scanner.nextLine();
        
        // Use java.sql.Date to call valueOf
        java.sql.Date expiryDate = java.sql.Date.valueOf(expiryDateStr);  // Convert to SQL date
        
        System.out.print("Enter quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        String sql = "INSERT INTO Medicines (name, batchNumber, expiryDate, quantity) VALUES (?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, batchNumber);
            stmt.setDate(3, expiryDate);  // Set SQL date directly
            stmt.setInt(4, quantity);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Medicine added successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to view all medicines from the database
    private static void viewAllMedicines() {
        String sql = "SELECT * FROM Medicines";

        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String batchNumber = rs.getString("batchNumber");
                
                // Get SQL date directly
                java.sql.Date expiryDate = rs.getDate("expiryDate");  
                
                int quantity = rs.getInt("quantity");

                System.out.println("ID: " + id + ", Name: " + name + ", Batch Number: " + batchNumber +
                        ", Expiry Date: " + expiryDate + ", Quantity: " + quantity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update a medicine's information in the database
    private static void updateMedicine(Scanner scanner) {
        System.out.print("Enter medicine ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        System.out.print("Enter new name: ");
        String name = scanner.nextLine();
        System.out.print("Enter new batch number: ");
        String batchNumber = scanner.nextLine();
        System.out.print("Enter new expiry date (yyyy-mm-dd): ");
        String expiryDateStr = scanner.nextLine();
        
        // Use java.sql.Date to call valueOf
        java.sql.Date expiryDate = java.sql.Date.valueOf(expiryDateStr);  // Convert to SQL date
        
        System.out.print("Enter new quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();  // Consume newline

        String sql = "UPDATE Medicines SET name = ?, batchNumber = ?, expiryDate = ?, quantity = ? WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, batchNumber);
            stmt.setDate(3, expiryDate);  // Set SQL date directly
            stmt.setInt(4, quantity);
            stmt.setInt(5, id);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Medicine updated successfully!");
            } else {
                System.out.println("Medicine with ID " + id + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to delete a medicine from the database
    private static void deleteMedicine(Scanner scanner) {
        System.out.print("Enter medicine ID to delete: ");
        int id = scanner.nextInt();

        String sql = "DELETE FROM Medicines WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Medicine deleted successfully!");
            } else {
                System.out.println("Medicine with ID " + id + " not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
