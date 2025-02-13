package view;

import controller.UserController;
import controller.AdminController;
import model.*;
import validation.InputValidator;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Main {

    private static UserController userController = new UserController();
    private static AdminController adminController = new AdminController();
    private static String loggedInUserToken;
    private static User loggedInUser;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Welcome to the RTO System");
            System.out.println("1. Login");
            System.out.println("2. Sign Up");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    login(scanner);
                    break;
                case 2:
                    signUp(scanner);
                    break;
                case 3:
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

            if (loggedInUserToken != null) {
                if (loggedInUser.getType() == 'A') {
                    showAdminMenu(scanner);
                } else {
                    showUserMenu(scanner);
                }
            }
        }
    }

    private static void login(Scanner scanner) {
        System.out.print("Enter email: ");
        String email = scanner.next();
        System.out.print("Enter password: ");
        String password = scanner.next();
        if (!InputValidator.isValidEmail(email)) {
            System.out.println("Invalid email format. Please try again.");
            return;
        }
        loggedInUserToken = userController.loginUser(email, password);
        if (loggedInUserToken != null) {
            System.out.println("Login successful! Token: " + loggedInUserToken);
            loggedInUser = userController.getUserByEmail(email);
        } else {
            System.out.println("Login failed! Please check your email and password.");
        }
    }

    private static void signUp(Scanner scanner) {
        System.out.print("Enter name: ");
        String name = scanner.next();
        System.out.print("Enter email: ");
        String email = scanner.next();
        System.out.print("Enter password: ");
        String password = scanner.next();
        System.out.print("Enter mobile: ");
        String mobile = scanner.next();

        if (!InputValidator.isValidEmail(email)) {
            System.out.println("Invalid email format. Please try again.");
            return;
        }

        if (!InputValidator.isValidPassword(password)) {
            System.out.println("Password must be at least 6 characters long. Please try again.");
            return;
        }

        if (!InputValidator.isValidMobile(mobile)) {
            System.out.println("Invalid mobile number. Please enter a 10-digit number.");
            return;
        }
        if (userController.isEmailDuplicate(email)) {
            System.out.println("Email is already registered. Please try another email.");
            return;
        }

        User user = new User(name, email, password, 'U', mobile);
        boolean success = userController.registerUser(user);

        if (success) {
            System.out.println("Sign Up successful!");
        } else {
            System.out.println("Sign Up failed! Please try again.");
        }
    }

    private static void showAdminMenu(Scanner scanner) {
        while (true) {
            System.out.println("Admin Menu");
            System.out.println("1. View/Update Vehicle Registrations");
            System.out.println("2. View/Update Driving Licenses");
            System.out.println("3. Generate Challan");
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    viewVehicleRegistrations(scanner);
                    break;
                case 2:
                    viewDrivingLicenses(scanner);
                    break;
                case 3:
                    generateChallan(scanner);
                    break;
                case 4:
                    loggedInUserToken = null;
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void viewVehicleRegistrations(Scanner scanner) {
        while (true) {
            List<Vehicle> vehicles = adminController.getPendingVehicleRegistrations();

            if (vehicles.isEmpty()) {
                System.out.println("No pending vehicle registrations.");
                return;
            } else {
                System.out.printf("%-10s %-20s %-20s %-20s %-10s %-10s %-10s%n", "ID", "Model", "License Number", "Owner Name", "Type", "Status", "Vehicle Number");
                System.out.println("----------------------------------------------------------------------------------------------------");

                for (Vehicle vehicle : vehicles) {
                    System.out.printf("%-10d %-20s %-20s %-20s %-10s %-10s %-10s%n",
                            vehicle.getId(), vehicle.getModel(), vehicle.getLicenseNumber(), vehicle.getOwnerName(), vehicle.getType(), vehicle.getStatus(), vehicle.getVehicleNumber());
                }

                System.out.print("\nEnter vehicle ID to approve/deny (or 0 to return to previous menu): ");
                int vehicleId = scanner.nextInt();
                if (vehicleId == 0) {
                    return;
                }

                System.out.print("Enter 1 to approve, 2 to deny: ");
                int action = scanner.nextInt();

                boolean success;
                if (action == 1) {
                    success = adminController.approveVehicleRegistration(vehicleId);
                    if (success) {
                        System.out.println("Vehicle registration approved.");
                    } else {
                        System.out.println("Failed to approve vehicle registration.");
                    }
                } else if (action == 2) {
                    success = adminController.denyVehicleRegistration(vehicleId);
                    if (success) {
                        System.out.println("Vehicle registration denied.");
                    } else {
                        System.out.println("Failed to deny vehicle registration.");
                    }
                } else {
                    System.out.println("Invalid action. Please enter 1 to approve or 2 to deny.");
                }
            }
        }
    }

    private static void viewDrivingLicenses(Scanner scanner) {
        while (true) {
            List<License> licenses = adminController.getAllLicenses();

            if (licenses.isEmpty()) {
                System.out.println("No pending driving licenses.");
                return;
            }

            System.out.printf("%-10s %-30s %-20s %-10s %-10s%n", "ID", "Name", "Aadhar", "Status", "License Number");
            System.out.println("------------------------------------------------------------------------------------------------");

            for (License license : licenses) {
                System.out.printf("%-10d %-30s %-20s %-10s %-10s%n",
                        license.getId(),
                        license.getName(),
                        license.getAadhar(),
                        license.getStatus(),
                        license.getLicenseNumber() != null ? license.getLicenseNumber() : "N/A");
            }

            System.out.print("Enter license ID to approve/deny (or 0 to return to previous menu): ");
            int licenseId = scanner.nextInt();
            if (licenseId == 0) {
                return;
            }

            System.out.print("Enter 1 to approve, 2 to deny: ");
            int action = scanner.nextInt();

            if (action == 1) {
                boolean success = adminController.approveLicense(licenseId);
                if (success) {
                    System.out.println("License approved and license number generated.");
                } else {
                    System.out.println("Failed to approve license.");
                }
            } else if (action == 2) {
                boolean success = adminController.denyLicense(licenseId);
                if (success) {
                    System.out.println("License denied.");
                } else {
                    System.out.println("Failed to deny license.");
                }
            } else {
                System.out.println("Invalid action.");
            }
        }
    }

    private static void generateChallan(Scanner scanner) {
        System.out.print("Enter vehicle number (enter 0 to return to previous menu): ");
        String vehicleNumber = scanner.next();
        if(vehicleNumber.equals("0")) {
            return;
        }

        System.out.print("Enter challan type: ");
        String challanType = scanner.next();
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        System.out.print("Enter deadline (yyyy-mm-dd): ");
        String deadlineStr = scanner.next();
        Date deadline = Date.valueOf(deadlineStr);

        int userId = userController.getUserIdByVehicleNumber(vehicleNumber);

        if (userId == -1) {
            System.out.println("Invalid vehicle number! Please try again.");
            return;
        }

        Challan challan = new Challan(vehicleNumber, challanType, amount, deadline, "Unpaid", userId);
        boolean success = adminController.generateChallan(challan);

        if (success) {
            System.out.println("Challan generated successfully!");
        } else {
            System.out.println("Failed to generate challan! Please try again.");
        }
    }

    private static void showUserMenu(Scanner scanner) {
        while (true) {
            System.out.println("User Menu");
            System.out.println("1. Vehicle Registration");
            System.out.println("2. Driving License Registration");
            System.out.println("3. Pay Challan");
            System.out.println("4. View Profile");
            System.out.println("5. Print Profile");
            System.out.println("6. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    vehicleRegistration(scanner);
                    break;
                case 2:
                    drivingLicenseRegistration(scanner);
                    break;
                case 3:
                    payChallan(scanner);
                    break;
                case 4:
                    viewProfile(scanner);
                    break;
                case 5:
                    printToFile(scanner);
                    break;
                case 6:
                    loggedInUserToken = null;
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void printToFile(Scanner scanner) {
        String filePath = "user_profile.txt"; // Path to the output file

        // Fetch data
        StringBuilder content = new StringBuilder();

        // Fetch user profile details
        content.append("User Profile\n");
        content.append("Name: ").append(loggedInUser.getName()).append("\n");
        content.append("Email: ").append(loggedInUser.getEmail()).append("\n");
        content.append("Mobile: ").append(loggedInUser.getMobile()).append("\n\n");

        // Fetch vehicles
        content.append("Vehicles:\n");
        List<Vehicle> vehicles = userController.getVehicles(loggedInUser.getId());
        if (vehicles.isEmpty()) {
            content.append("No vehicles registered.\n");
        } else {
            content.append(String.format("%-10s %-20s %-20s %-20s %-20s %-10s %-10s%n", "ID","Vehicle Number", "Model", "License Number", "Owner Name", "Type" ,"Status"));
            content.append("-------------------------------------------------------------------------------------------------------------------------------\n");
            for (Vehicle vehicle : vehicles) {
                content.append(String.format("%-10d %-20s %-20s %-20s %-20s %-10s %-10s%n",
                        vehicle.getId(), vehicle.getVehicleNumber(), vehicle.getModel(), vehicle.getLicenseNumber(), vehicle.getOwnerName(), vehicle.getType(), vehicle.getStatus()));
            }
        }

        // Fetch licenses
        content.append("\nLicenses:\n");
        List<License> licenses = userController.getLicenses(loggedInUser.getId());
        if (licenses.isEmpty()) {
            content.append("No licenses registered.\n");
        } else {
            content.append(String.format("%-10s %-20s %-20s %-5s %-20s %-30s %-10s %-20s %-20s%n", "ID","License No", "Name", "Age", "Aadhar", "Address", "Gender", "Transaction ID", "Status"));
            content.append("--------------------------------------------------------------------------------------------------------------------------------------------------\n");
            for (License license : licenses) {
                content.append(String.format("%-10d %-20s %-20s %-5d %-20s %-30s %-10c %-20s %-20s%n",
                        license.getId(), license.getLicenseNumber(), license.getName(), license.getAge(), license.getAadhar(), license.getAddress(), license.getGender(), license.getTransactionId(), license.getStatus()));
            }
        }

        // Fetch challans
        content.append("\nChallans:\n");
        List<Challan> challans = userController.getChallans(loggedInUser.getId());
        if (challans.isEmpty()) {
            content.append("No challans found.\n");
        } else {
            content.append(String.format("%-10s %-20s %-20s %-10s %-10s %-15s%n", "ID", "Vehicle Number", "Challan Type", "Amount", "Deadline", "Status"));
            content.append("-----------------------------------------------------------------------------------------\n");
            for (Challan challan : challans) {
                content.append(String.format("%-10d %-20s %-20s %-10.2f %-10s %-15s%n",
                        challan.getId(), challan.getVehicleNumber(), challan.getChallanType(), challan.getAmount(), challan.getDeadline(), challan.getStatus()));
            }
        }

        // Write content to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content.toString());
            System.out.println("Profile details have been written to " + filePath);
        } catch (IOException e) {
            System.err.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

    private static void viewProfile(Scanner scanner) {
        System.out.println("User Profile");
        System.out.println("Name: " + loggedInUser.getName());
        System.out.println("Email: " + loggedInUser.getEmail());
        System.out.println("Mobile: " + loggedInUser.getMobile());

        // Display vehicles in table format
        System.out.println("\nVehicles:");
        List<Vehicle> vehicles = userController.getVehicles(loggedInUser.getId());
        if (vehicles.isEmpty()) {
            System.out.println("No vehicles registered.");
        } else {
            System.out.printf("%-10s %-20s %-20s %-20s %-20s %-10s %-10s%n", "ID","Vehicle Number", "Model", "License Number", "Owner Name", "Type" ,"Status");
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------");
            for (Vehicle vehicle : vehicles) {
                System.out.printf("%-10d %-20s %-20s %-20s %-20s %-10s %-10s%n",
                        vehicle.getId(),vehicle.getVehicleNumber(), vehicle.getModel(), vehicle.getLicenseNumber(), vehicle.getOwnerName(), vehicle.getType(),vehicle.getStatus());
            }
        }

        // Display licenses in table format
        System.out.println("\nLicenses:");
        List<License> licenses = userController.getLicenses(loggedInUser.getId());
        if (licenses.isEmpty()) {
            System.out.println("No licenses registered.");
        } else {
            System.out.printf("%-10s %-20s %-20s %-5s %-20s %-30s %-10s %-20s %-20s%n", "ID","License No", "Name", "Age", "Aadhar", "Address", "Gender", "Transaction ID","Status");
            System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------");
            for (License license : licenses) {
                System.out.printf("%-10d %-20s %-20s %-5d %-20s %-30s %-10c %-20s %-20s%n",
                        license.getId(),license.getLicenseNumber(), license.getName(), license.getAge(), license.getAadhar(), license.getAddress(), license.getGender(), license.getTransactionId(),license.getStatus());
            }
        }

        // Display challans in table format
        System.out.println("\nChallans:");
        List<Challan> challans = userController.getChallans(loggedInUser.getId());
        if (challans.isEmpty()) {
            System.out.println("No challans found.");
        } else {
            System.out.printf("%-10s %-20s %-20s %-10s %-10s %-15s%n", "ID", "Vehicle Number", "Challan Type", "Amount", "Deadline", "Status");
            System.out.println("-----------------------------------------------------------------------------------------");
            for (Challan challan : challans) {
                System.out.printf("%-10d %-20s %-20s %-10.2f %-10s %-15s%n",
                        challan.getId(), challan.getVehicleNumber(), challan.getChallanType(), challan.getAmount(), challan.getDeadline(), challan.getStatus());
            }
        }
    }


private static void vehicleRegistration(Scanner scanner) {
        System.out.print("Enter model: ");
        String model = scanner.next();
        System.out.print("Enter license number: ");
        String licenseNumber = scanner.next();
        System.out.print("Enter owner name: ");
        String ownerName = scanner.next();
        System.out.print("Enter type: ");
        String type = scanner.next();

        Vehicle vehicle = new Vehicle(model, licenseNumber, ownerName, type, loggedInUser.getId(),"pending",null);
        boolean success = userController.registerVehicle(vehicle);

        if (success) {
            System.out.println("Vehicle registration submitted successfully. Awaiting approval.");
        } else {
            System.out.println("Failed to submit vehicle registration. Please try again.");
        }
    }

    private static void drivingLicenseRegistration(Scanner scanner) {
        System.out.print("Enter name: ");
        String name = scanner.next();
        System.out.print("Enter age: ");
        int age = scanner.nextInt();
        System.out.print("Enter aadhar: ");
        String aadhar = scanner.next();
        System.out.print("Enter address: ");
        String address = scanner.next();
        System.out.print("Enter gender (M/F): ");
        char gender = scanner.next().charAt(0);
        System.out.print("Enter transaction ID: ");
        String transactionId = scanner.next();
        if (!InputValidator.isValidAadhar(aadhar)) {
            System.out.println("Invalid Aadhar number. Please enter a 12-digit number.");
            return;
        }
        License license = new License(name, age, aadhar, address, gender, transactionId, loggedInUser.getId(),"pending",null);

        boolean success = userController.registerLicense(license);

        if (success) {
            System.out.println("Driving license registration submitted successfully. Awaiting approval.");
        } else {
            System.out.println("Failed to submit driving license registration. Please try again.");
        }
    }

    private static void payChallan(Scanner scanner) {
        System.out.print("Enter challan ID to pay (or 0 to return to previous menu): ");
        int challanId = scanner.nextInt();

        if (challanId == 0) {
            return;
        }

        boolean success = userController.payChallan(challanId);

        if (success) {
            System.out.println("Challan paid successfully!");
        } else {
            System.out.println("Failed to pay challan. Please try again.");
        }
    }
}
