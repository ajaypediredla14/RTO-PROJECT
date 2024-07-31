package view;

import controller.AdminController;
import controller.UserController;
import model.Challan;
import model.License;
import model.User;
import model.Vehicle;
import java.sql.Date;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    private static String token;
    private static int userId;
    private static char userType;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserController userController = new UserController();
        AdminController adminController = new AdminController();

        while (true) {
            System.out.println("1. Signup");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("Email:");
                    String email = scanner.nextLine();
                    System.out.println("Password:");
                    String password = scanner.nextLine();
                    System.out.println("Confirm Password:");
                    String confirmPassword = scanner.nextLine();
                    System.out.println("Name:");
                    String name = scanner.nextLine();
                    System.out.println("Type (A for admin, U for user):");
                    char type = scanner.nextLine().charAt(0);
                    System.out.println("Mobile:");
                    String mobile = scanner.nextLine();

                    if (!password.equals(confirmPassword)) {
                        System.out.println("Passwords do not match!");
                        break;
                    }

                    User user = new User(email, password, name, type, mobile);
                    if (userController.registerUser(user)) {
                        System.out.println("User registered successfully!");
                    } else {
                        System.out.println("Error in user registration!");
                    }
                    break;
                case 2:
                    System.out.println("Email:");
                    email = scanner.nextLine();
                    System.out.println("Password:");
                    password = scanner.nextLine();
                    token = userController.loginUser(email, password);
                    if (token != null) {
                        System.out.println("Login successful! Token: " + token);
                        // Retrieve user ID and type from the database
                        userId = retrieveUserId(email);
                        userType = retrieveUserType(email);
                        if (userType == 'U') {
                            userMenu(scanner, userController);
                        } else if (userType == 'A') {
                            adminMenu(scanner, adminController);
                        } else {
                            System.out.println("Invalid user type!");
                        }
                    } else {
                        System.out.println("Invalid email or password!");
                    }
                    break;
                case 3:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice!");
                    break;
            }
        }
    }

    private static int retrieveUserId(String email) {
        // Implement logic to retrieve user ID from the database using the email
        return 0; // Placeholder return value
    }

    private static char retrieveUserType(String email) {
        // Implement logic to retrieve user type from the database using the email
        return 'U'; // Placeholder return value
    }

    private static void userMenu(Scanner scanner, UserController userController) {
        while (true) {
            System.out.println("1. Vehicle Registration");
            System.out.println("2. Driving License Registration");
            System.out.println("3. Pay Challans");
            System.out.println("4. View Profile");
            System.out.println("5. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("Vehicle Model:");
                    String model = scanner.nextLine();
                    System.out.println("Driving License Number:");
                    String licenseNumber = scanner.nextLine();
                    System.out.println("Vehicle Owner Name:");
                    String ownerName = scanner.nextLine();
                    System.out.println("Vehicle Type:");
                    String type = scanner.nextLine();

                    Vehicle vehicle = new Vehicle(model, licenseNumber, ownerName, type, userId);
                    if (userController.registerVehicle(vehicle)) {
                        System.out.println("Vehicle registered successfully!");
                    } else {
                        System.out.println("Error in vehicle registration!");
                    }
                    break;
                case 2:
                    System.out.println("Name:");
                    String name = scanner.nextLine();
                    System.out.println("Age:");
                    int age = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Aadhar:");
                    String aadhar = scanner.nextLine();
                    System.out.println("Address:");
                    String address = scanner.nextLine();
                    System.out.println("Gender:");
                    char gender = scanner.nextLine().charAt(0);
                    System.out.println("Transaction ID:");
                    String transactionId = scanner.nextLine();

                    License license = new License(name, age, aadhar, address, gender, transactionId, userId);
                    if (userController.registerLicense(license)) {
                        System.out.println("License registered successfully!");
                    } else {
                        System.out.println("Error in license registration!");
                    }
                    break;
                case 3:
                    System.out.println("Vehicle Number:");
                    String vehicleNumber = scanner.nextLine();
                    System.out.println("Challan Type:");
                    String challanType = scanner.nextLine();
                    System.out.println("Amount:");
                    double amount = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.println("Deadline (YYYY-MM-DD):");
                    String deadline = scanner.nextLine();
                    System.out.println("Status:");
                    String status = scanner.nextLine();

                    Challan challan = new Challan(vehicleNumber, challanType, amount, Date.valueOf(deadline), status, userId);
                    if (userController.payChallan(challan)) {
                        System.out.println("Challan paid successfully!");
                    } else {
                        System.out.println("Error in paying challan!");
                    }
                    break;
                case 4:
                    userController.viewProfile(userId);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice!");
                    break;
            }
        }
    }

    private static void adminMenu(Scanner scanner, AdminController adminController) {
        while (true) {
            System.out.println("1. View Applied Vehicle Registrations");
            System.out.println("2. View Applied Driving License Registrations");
            System.out.println("3. Generate Challans");
            System.out.println("4. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    adminController.viewAppliedVehicleRegistrations();
                    System.out.println("Enter Vehicle ID to approve/deny:");
                    int vehicleId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Approve (A) or Deny (D):");
                    char decision = scanner.nextLine().charAt(0);
                    if (decision == 'A') {
                        String generatedNumber = UUID.randomUUID().toString();
                        if (adminController.approveVehicleRegistration(vehicleId, generatedNumber)) {
                            System.out.println("Vehicle registration approved with number: " + generatedNumber);
                        } else {
                            System.out.println("Error in approving vehicle registration!");
                        }
                    } else {
                        if (adminController.denyVehicleRegistration(vehicleId)) {
                            System.out.println("Vehicle registration denied!");
                        } else {
                            System.out.println("Error in denying vehicle registration!");
                        }
                    }
                    break;
                case 2:
                    adminController.viewAppliedLicenseRegistrations();
                    System.out.println("Enter License ID to approve/deny:");
                    int licenseId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.println("Approve (A) or Deny (D):");
                    decision = scanner.nextLine().charAt(0);
                    if (decision == 'A') {
                        String generatedNumber = UUID.randomUUID().toString();
                        if (adminController.approveLicenseRegistration(licenseId, generatedNumber)) {
                            System.out.println("License registration approved with number: " + generatedNumber);
                        } else {
                            System.out.println("Error in approving license registration!");
                        }
                    } else {
                        if (adminController.denyLicenseRegistration(licenseId)) {
                            System.out.println("License registration denied!");
                        } else {
                            System.out.println("Error in denying license registration!");
                        }
                    }
                    break;
                case 3:
                    System.out.println("Vehicle Number:");
                    String vehicleNumber = scanner.nextLine();
                    System.out.println("Challan Type:");
                    String challanType = scanner.nextLine();
                    System.out.println("Amount:");
                    double amount = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.println("Deadline (YYYY-MM-DD):");
                    String deadline = scanner.nextLine();
                    System.out.println("Status:");
                    String status = scanner.nextLine();

                    Challan challan = new Challan(vehicleNumber, challanType, amount, Date.valueOf(deadline), status, userId);
                    if (adminController.generateChallan(challan)) {
                        System.out.println("Challan generated successfully!");
                    } else {
                        System.out.println("Error in generating challan!");
                    }
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice!");
                    break;
            }
        }
    }
}
