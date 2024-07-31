package view;

import controller.UserController;
import model.User;
import model.Vehicle;
import model.License;
import model.Challan;
import model.Database;

import java.sql.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class Main {

    private static UserController userController = new UserController();
    private static Scanner scanner = new Scanner(System.in);
    private static String token = null;
    private static int userId;
    private static char userType;

    public static void main(String[] args) {
        while (true) {
            System.out.println("1. Sign Up");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    signUp();
                    break;
                case 2:
                    login();
                    break;
                case 3:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void signUp() {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter type (A for Admin, U for User): ");
        char type = scanner.nextLine().charAt(0);
        System.out.print("Enter mobile: ");
        String mobile = scanner.nextLine();
        System.out.print("Confirm password: ");
        String confirmPassword = scanner.nextLine();

        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords do not match.");
            return;
        }

        User user = new User(email, password, name, type, mobile);
        if (userController.registerUser(user)) {
            System.out.println("User registered successfully.");
        } else {
            System.out.println("Registration failed.");
        }
    }

    private static void login() {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        token = userController.loginUser(email, password);
        if (token != null) {
            userId = retrieveUserId(email);
            userType = getUserType(email);
            System.out.println("Login successful! Token: " + token);
            if (userType == 'A') {
                adminMenu();
            } else if (userType == 'U') {
                userMenu();
            }
        } else {
            System.out.println("Login failed.");
        }
    }

    private static void adminMenu() {
        while (true) {
            System.out.println("Admin Menu:");
            System.out.println("1. View Applied Vehicle Registrations");
            System.out.println("2. View Applied Driving License Registrations");
            System.out.println("3. Generate Challans");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    viewAppliedVehicleRegistrations();
                    break;
                case 2:
                    viewAppliedDrivingLicenses();
                    break;
                case 3:
                    generateChallan();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void userMenu() {
        while (true) {
            System.out.println("User Menu:");
            System.out.println("1. Vehicle Registration");
            System.out.println("2. Driving License Registration");
            System.out.println("3. Pay Challans");
            System.out.println("4. View Profile");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    registerVehicle();
                    break;
                case 2:
                    registerLicense();
                    break;
                case 3:
                    payChallan();
                    break;
                case 4:
                    viewProfile();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void registerVehicle() {
        System.out.print("Enter vehicle model: ");
        String model = scanner.nextLine();
        System.out.print("Enter driving license number: ");
        String licenseNumber = scanner.nextLine();
        System.out.print("Enter vehicle owner name: ");
        String ownerName = scanner.nextLine();
        System.out.print("Enter vehicle type: ");
        String type = scanner.nextLine();

        Vehicle vehicle = new Vehicle(model, licenseNumber, ownerName, type, userId);
        if (userController.registerVehicle(vehicle)) {
            System.out.println("Vehicle registered successfully.");
        } else {
            System.out.println("Vehicle registration failed.");
        }
    }

    private static void registerLicense() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter age: ");
        int age = scanner.nextInt();
        scanner.nextLine();  // Consume newline
        System.out.print("Enter aadhar: ");
        String aadhar = scanner.nextLine();
        System.out.print("Enter address: ");
        String address = scanner.nextLine();
        System.out.print("Enter gender (M/F): ");
        char gender = scanner.nextLine().charAt(0);
        System.out.print("Enter transaction ID: ");
        String transactionId = scanner.nextLine();

        License license = new License(name, age, aadhar, address, gender, transactionId, userId);
        if (userController.registerLicense(license)) {
            System.out.println("License registered successfully.");
        } else {
            System.out.println("License registration failed.");
        }
    }

    private static void payChallan() {
        System.out.print("Enter vehicle number: ");
        String vehicleNumber = scanner.nextLine();
        System.out.print("Enter challan type: ");
        String challanType = scanner.nextLine();
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        System.out.print("Enter deadline (yyyy-mm-dd): ");
        String deadlineStr = scanner.next();
        Date deadline = Date.valueOf(deadlineStr);
        scanner.nextLine();  // Consume newline
        System.out.print("Enter status: ");
        String status = scanner.nextLine();

        Challan challan = new Challan(vehicleNumber, challanType, amount, deadline, status, userId);
        if (userController.payChallan(challan)) {
            System.out.println("Challan paid successfully.");
        } else {
            System.out.println("Challan payment failed.");
        }
    }

    private static void viewProfile() {
        List<Vehicle> vehicles = userController.getVehicles(userId);
        List<License> licenses = userController.getLicenses(userId);

        System.out.println("Vehicles:");
        System.out.printf("%-20s %-20s %-20s %-20s\n", "Model", "License Number", "Owner Name", "Type");
        for (Vehicle vehicle : vehicles) {
            System.out.printf("%-20s %-20s %-20s %-20s\n",
                    vehicle.getModel(), vehicle.getLicenseNumber(), vehicle.getOwnerName(), vehicle.getType());
        }

        System.out.println("Driving Licenses:");
        System.out.printf("%-20s %-5s %-20s %-20s %-10s %-20s\n", "Name", "Age", "Aadhar", "Address", "Gender", "Transaction ID");
        for (License license : licenses) {
            System.out.printf("%-20s %-5d %-20s %-20s %-10c %-20s\n",
                    license.getName(), license.getAge(), license.getAadhar(), license.getAddress(), license.getGender(), license.getTransactionId());
        }
    }

    private static void viewAppliedVehicleRegistrations() {
        String query = "SELECT * FROM vehicles WHERE user_id IS NULL";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("Applied Vehicle Registrations:");
            System.out.printf("%-10s %-20s %-20s %-20s %-20s\n", "ID", "Model", "License Number", "Owner Name", "Type");
            while (rs.next()) {
                System.out.printf("%-10d %-20s %-20s %-20s %-20s\n",
                        rs.getInt("id"), rs.getString("model"), rs.getString("license_number"),
                        rs.getString("owner_name"), rs.getString("type"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void viewAppliedDrivingLicenses() {
        String query = "SELECT * FROM licenses WHERE user_id IS NULL";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("Applied Driving Licenses:");
            System.out.printf("%-10s %-20s %-5s %-20s %-20s %-10s %-20s\n", "ID", "Name", "Age", "Aadhar", "Address", "Gender", "Transaction ID");
            while (rs.next()) {
                System.out.printf("%-10d %-20s %-5d %-20s %-20s %-10c %-20s\n",
                        rs.getInt("id"), rs.getString("name"), rs.getInt("age"), rs.getString("aadhar"),
                        rs.getString("address"), rs.getString("gender").charAt(0), rs.getString("transaction_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void generateChallan() {
        System.out.print("Enter vehicle number: ");
        String vehicleNumber = scanner.nextLine();
        System.out.print("Enter challan type: ");
        String challanType = scanner.nextLine();
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        System.out.print("Enter deadline (yyyy-mm-dd): ");
        String deadlineStr = scanner.next();
        Date deadline = Date.valueOf(deadlineStr);
        scanner.nextLine();  // Consume newline
        System.out.print("Enter status: ");
        String status = scanner.nextLine();

        Challan challan = new Challan(vehicleNumber, challanType, amount, deadline, status, userId);
        if (userController.generateChallan(challan)) {
            System.out.println("Challan generated successfully.");
        } else {
            System.out.println("Challan generation failed.");
        }
    }

    private static int retrieveUserId(String email) {
        int id = 0;
        String query = "SELECT id FROM users WHERE email = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                id = rs.getInt("id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    private static char getUserType(String email) {
        char type = 'U'; // Default to 'User'
        String query = "SELECT type FROM users WHERE email = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                type = rs.getString("type").charAt(0);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return type;
    }
}
