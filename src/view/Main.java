package view;

import controller.UserController;
import model.*;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static UserController userController = new UserController();
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
//                System.out.println("checking"+loggedInUser.getType());
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
//        System.out.print("Enter type (A for Admin, U for User): ");
//        char type = scanner.next().charAt(0);

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
            System.out.println("2. View/Update Driving Licenses");  // New option for licenses
            System.out.println("3. Generate Challan");
            System.out.println("4. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    viewVehicleRegistrations(scanner);
                    break;
                case 2:
                    viewDrivingLicenses(scanner);  // New method for licenses
                    break;
                case 3:
                    generateChallan(scanner);
                    break;
                case 4:
                    loggedInUserToken = null;
                    return;  // Exit admin menu
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void viewVehicleRegistrations(Scanner scanner) {
        List<Vehicle> vehicles = userController.getPendingVehicleRegistrations();

        // Check if there are any vehicles to display
        if (vehicles.isEmpty()) {
            System.out.println("No pending vehicle registrations.");
        } else {
            // Print the table header
            System.out.printf("%-10s %-20s %-20s %-20s %-10s %-10s %-10s%n", "ID", "Model", "License Number", "Owner Name", "Type", "Status","vehicle_number");
            System.out.println("----------------------------------------------------------------------------------------------------");

            // Print each vehicle in a row
            for (Vehicle vehicle : vehicles) {
                System.out.printf("%-10d %-20s %-20s %-20s %-10s %-10s %-10s%n",
                        vehicle.getId(), vehicle.getModel(), vehicle.getLicenseNumber(), vehicle.getOwnerName(), vehicle.getType(), vehicle.getStatus(),vehicle.getVehicle_number());
            }

            // Prompt user to approve or deny
            System.out.print("\nEnter vehicle ID to approve/deny: ");
            int vehicleId = scanner.nextInt();
            System.out.print("Enter 1 to approve, 2 to deny: ");
            int action = scanner.nextInt();

            boolean success;
            if (action == 1) {
                success = userController.approveVehicleRegistration(vehicleId);
                if (success) {
                    System.out.println("Vehicle registration approved.");
                } else {
                    System.out.println("Failed to approve vehicle registration.");
                }
            } else if (action == 2) {
                success = userController.denyVehicleRegistration(vehicleId);
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

    private static void viewDrivingLicenses(Scanner scanner) {
        List<License> licenses = userController.getAllLicenses();

        if (licenses.isEmpty()) {
            System.out.println("No pending licenses.");
            return;
        }

        // Print table header
        System.out.printf("%-10s %-30s %-20s %-10s %-10s\n", "ID", "Name", "Aadhar", "Status", "License Number" );
        System.out.println("------------------------------------------------------------------------------------------------");

        // Print each license in a table format
        for (License license : licenses) {
            System.out.printf("%-10d %-30s %-20s %-10s %-10s\n",
                    license.getId(),
                    license.getName(),
                    license.getAadhar(),
                    license.getStatus(),
                    license.getLicense_number() != null ? license.getLicense_number() : "N/A");
        }

        System.out.print("Enter license ID to approve/deny: ");
        int licenseId = scanner.nextInt();
        System.out.print("Enter 1 to approve, 2 to deny: ");
        int action = scanner.nextInt();

        if (action == 1) {
            boolean success = userController.approveLicense(licenseId);
            if (success) {
                System.out.println("License approved and license number generated.");
            } else {
                System.out.println("Failed to approve license.");
            }
        } else if (action == 2) {
            boolean success = userController.denyLicense(licenseId);
            if (success) {
                System.out.println("License denied.");
            } else {
                System.out.println("Failed to deny license.");
            }
        } else {
            System.out.println("Invalid action.");
        }
    }



    private static void generateChallan(Scanner scanner) {
        System.out.print("Enter vehicle number: ");
        String vehicleNumber = scanner.next();
        System.out.print("Enter challan type: ");
        String challanType = scanner.next();
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        System.out.print("Enter deadline (yyyy-mm-dd): ");
        String deadlineStr = scanner.next();
        Date deadline = Date.valueOf(deadlineStr);

        Challan challan = new Challan(vehicleNumber, challanType, amount, deadline, "Unpaid", loggedInUser.getId());
        boolean success = userController.generateChallan(challan);

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
            System.out.println("3. Pay Challans");
            System.out.println("4. View Profile");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    registerVehicle(scanner);
                    break;
                case 2:
                    registerLicense(scanner);
                    break;
                case 3:
                    payChallan(scanner);
                    break;
                case 4:
                    viewProfile(scanner);
                    break;
                case 5:
                    loggedInUserToken = null;
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void registerVehicle(Scanner scanner) {
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
            System.out.println("Vehicle registration successful!");
        } else {
            System.out.println("Vehicle registration failed! Please try again.");
        }
    }

    private static void registerLicense(Scanner scanner) {
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

        License license = new License(name, age, aadhar, address, gender, transactionId, loggedInUser.getId(),"pending",null);

        boolean success = userController.registerLicense(license);

        if (success) {
            System.out.println("License registration successful!");
        } else {
            System.out.println("License registration failed! Please try again.");
        }
    }

    private static void payChallan(Scanner scanner) {
        System.out.print("Enter vehicle number: ");
        String vehicleNumber = scanner.next();
        System.out.print("Enter challan type: ");
        String challanType = scanner.next();
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        System.out.print("Enter deadline (yyyy-mm-dd): ");
        String deadlineStr = scanner.next();
        Date deadline = Date.valueOf(deadlineStr);

        Challan challan = new Challan(vehicleNumber, challanType, amount, deadline, "Unpaid", loggedInUser.getId());
        boolean success = userController.payChallan(challan);

        if (success) {
            System.out.println("Challan paid successfully!");
        } else {
            System.out.println("Failed to pay challan! Please try again.");
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
                        vehicle.getId(),vehicle.getVehicle_number(), vehicle.getModel(), vehicle.getLicenseNumber(), vehicle.getOwnerName(), vehicle.getType(),vehicle.getStatus());
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
                        license.getId(),license.getLicense_number(), license.getName(), license.getAge(), license.getAadhar(), license.getAddress(), license.getGender(), license.getTransactionId(),license.getStatus());
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
    }}

