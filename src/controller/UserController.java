package controller;

import model.User;
import model.Vehicle;
import model.License;
import model.Challan;
import model.Database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import util.HashUtil;

public class UserController {

    public boolean registerUser(User user) {
        String query = "INSERT INTO users (email, password, name, type, mobile) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, user.getEmail());
            stmt.setString(2, HashUtil.hashPassword(user.getPassword())); // Make sure the password is hashed before storing
            stmt.setString(3, user.getName());
            stmt.setString(4, String.valueOf(user.getType()));
            stmt.setString(5, user.getMobile());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Add this method in UserController
    public User getUserByEmail(String email) {
        User user = null;
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE email = ?")) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new User(
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("type").charAt(0),
                            rs.getString("mobile")
                    );
                    // Assuming you have a method to get user ID
                    user.setId(rs.getInt("id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }


    public String loginUser(String email, String password) {
        String query = "SELECT password FROM users WHERE email = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                if (storedPassword.equals(HashUtil.hashPassword(password))) { // Use hashed password comparison
                    return java.util.UUID.randomUUID().toString(); // Generate a token
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
}

    public boolean isEmailDuplicate(String email) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0; // Return true if email is found
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean registerVehicle(Vehicle vehicle) {
        String query = "INSERT INTO vehicles (model, license_number, owner_name, type, user_id, status) VALUES (?, ?, ?, ?, ?, 'Pending')";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, vehicle.getModel());
            stmt.setString(2, vehicle.getLicenseNumber());
            stmt.setString(3, vehicle.getOwnerName());
            stmt.setString(4, vehicle.getType());
            stmt.setInt(5, vehicle.getUserId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean registerLicense(License license) {
        String query = "INSERT INTO licenses (name, age, aadhar, address, gender, transaction_id, user_id,status) VALUES (?, ?, ?, ?, ?, ?, ? ,?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, license.getName());
            stmt.setInt(2, license.getAge());
            stmt.setString(3, license.getAadhar());
            stmt.setString(4, license.getAddress());
            stmt.setString(5, String.valueOf(license.getGender()));
            stmt.setString(6, license.getTransactionId());
            stmt.setInt(7, license.getUserId());
            stmt.setString(8, license.getStatus());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean payChallan(int challanId) {
        String checkStatusQuery = "SELECT status FROM challans WHERE id = ?";
        String updateStatusQuery = "UPDATE challans SET status = 'Paid' WHERE id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement checkStatusStmt = connection.prepareStatement(checkStatusQuery);
             PreparedStatement updateStatusStmt = connection.prepareStatement(updateStatusQuery)) {

            // Check the current status of the challan
            checkStatusStmt.setInt(1, challanId);
            ResultSet rs = checkStatusStmt.executeQuery();

            if (rs.next()) {
                String currentStatus = rs.getString("status");

                if ("Paid".equalsIgnoreCase(currentStatus)) {
                    System.out.println("Challan is already paid.");
                    return false; // Challan is already paid
                } else {
                    // Update the status to 'Paid'
                    updateStatusStmt.setInt(1, challanId);
                    int rowsAffected = updateStatusStmt.executeUpdate();
                    return rowsAffected > 0; // Challan is successfully paid
                }
            } else {
                System.out.println("Challan not found.");
                return false; // Challan not found
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Vehicle> getPendingVehicleRegistrations() {
        List<Vehicle> vehicles = new ArrayList<>();
        String query = "SELECT * FROM vehicles";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Vehicle vehicle = new Vehicle(
                        rs.getString("model"),
                        rs.getString("license_number"),
                        rs.getString("owner_name"),
                        rs.getString("type"),
                        rs.getInt("user_id"),
                        rs.getString("status"),
                        rs.getString("vehicle_number")
                );
                vehicle.setId(rs.getInt("id"));
                vehicle.setStatus(rs.getString("status"));
                vehicles.add(vehicle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicles;
    }

    public boolean approveVehicleRegistration(int vehicleId) {
        String vehicleNumber = generateVehicleNumber();
        String query = "UPDATE vehicles SET status = 'Completed', vehicle_number = ? WHERE id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, vehicleNumber);
            stmt.setInt(2, vehicleId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean denyVehicleRegistration(int vehicleId) {
        String query = "UPDATE vehicles SET vehicle_number=NULL, status = 'Denied' WHERE id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, vehicleId);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getUserIdByVehicleNumber(String vehicleNumber) {
        String query = "SELECT user_id FROM vehicles WHERE vehicle_number = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, vehicleNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("user_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if vehicle number is not found
    }


    private String generateVehicleNumber() {
        // Generate a random vehicle number
        Random random = new Random();
        int number = random.nextInt(90000) + 10000; // 5-digit number
        return "VEH" + number;
    }

    // Approve license and generate license number
    public boolean approveLicense(int licenseId) {
        // Generate a unique license number
        String licenseNumber = generateUniqueLicenseNumber();

        // Update the license in the database
        String query = "UPDATE licenses SET status = 'Completed', license_number = ? WHERE id = ?";;
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, licenseNumber);
            statement.setInt(2, licenseId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Deny license
    public boolean denyLicense(int licenseId) {
        String query = "UPDATE licenses SET status='denied', license_number = NULL WHERE id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, licenseId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Generate a unique license number (this is a placeholder, implement accordingly)
    private String generateUniqueLicenseNumber() {
        // Generate a unique number or use a UUID
        return "LIC-" + System.currentTimeMillis();
    }


    public List<License> getLicenses(int userId) {
        List<License> licenses = new ArrayList<>();
        String query = "SELECT * FROM licenses WHERE user_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                License license = new License(
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("aadhar"),
                        rs.getString("address"),
                        rs.getString("gender").charAt(0),
                        rs.getString("transaction_id"),
                        rs.getInt("user_id"),
                        rs.getString("status"),
                        rs.getString("license_number")
                );
                license.setId(rs.getInt("id"));
                license.setStatus(rs.getString("status"));

                licenses.add(license);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return licenses;
    }

    public List<License> getAllLicenses() {
        List<License> licenses = new ArrayList<>();
        String query = "SELECT * FROM licenses";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

//            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                License license = new License(
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("aadhar"),
                        rs.getString("address"),
                        rs.getString("gender").charAt(0),
                        rs.getString("transaction_id"),
                        rs.getInt("user_id"),
                        rs.getString("status"),
                        rs.getString("license_number")
                );
                license.setId(rs.getInt("id"));
                license.setStatus(rs.getString("status"));
                licenses.add(license);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return licenses;
    }

    public List<Vehicle> getVehicles(int userId) {
        List<Vehicle> vehicles = new ArrayList<>();
        String query = "SELECT * FROM vehicles WHERE user_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Vehicle vehicle = new Vehicle(
                        rs.getString("model"),
                        rs.getString("license_number"),
                        rs.getString("owner_name"),
                        rs.getString("type"),
                        rs.getInt("user_id"),
                        rs.getString("status"),
                        rs.getString("vehicle_number")

                );
                vehicle.setId(rs.getInt("id"));
                vehicle.setStatus(rs.getString("status"));
                vehicles.add(vehicle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicles;
    }

    public List<Challan> getChallans(int userId) {
        List<Challan> challans = new ArrayList<>();
//        System.out.println("checking+"+userId);
        String query = "SELECT * FROM challans WHERE user_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Challan challan = new Challan(
                        rs.getString("vehicle_number"),
                        rs.getString("challan_type"),
                        rs.getDouble("amount"),
                        rs.getDate("deadline"),
                        rs.getString("status"),
                        rs.getInt("user_id")
                );
                challan.setId(rs.getInt("id"));
                challan.setStatus(rs.getString("status"));
                challans.add(challan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return challans;
    }

    public List<Challan> getChallansByVehicleNumber(String vehicleNumber) {
        List<Challan> challans = new ArrayList<>();
        String query = "SELECT * FROM challans WHERE vehicle_number = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, vehicleNumber);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Challan challan = new Challan(
                        rs.getString("vehicle_number"),
                        rs.getString("challan_type"),
                        rs.getDouble("amount"),
                        rs.getDate("deadline"),
                        rs.getString("status"),
                        rs.getInt("user_id")
                );
                challan.setId(rs.getInt("id"));
                challan.setStatus(rs.getString("status"));
                challans.add(challan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return challans;
    }


    public boolean generateChallan(Challan challan) {
        String query = "INSERT INTO challans (vehicle_number, challan_type, amount, deadline, status, user_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, challan.getVehicleNumber());
            stmt.setString(2, challan.getChallanType());
            stmt.setDouble(3, challan.getAmount());
            stmt.setDate(4, challan.getDeadline());
            stmt.setString(5, challan.getStatus());
            stmt.setInt(6, challan.getUserId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }




}
