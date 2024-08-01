package controller;

import model.User;
import model.Vehicle;
import model.License;
import model.Challan;
import model.Database;
import util.HashUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserController implements IUserController {

    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());
    private Database database = new Database(); // Create an instance of Database

    @Override
    public boolean registerUser(User user) {
        String query = "INSERT INTO users (email, password, name, type, mobile) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = database.getConnection(); // Use instance method
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, user.getEmail());
            stmt.setString(2, HashUtil.hashPassword(user.getPassword())); // Hash password
            stmt.setString(3, user.getName());
            stmt.setString(4, String.valueOf(user.getType()));
            stmt.setString(5, user.getMobile());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to register user", e);
            return false;
        }
    }

    @Override
    public User getUserByEmail(String email) {
        User user = null;
        String query = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = database.getConnection(); // Use instance method
             PreparedStatement ps = conn.prepareStatement(query)) {
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
                    user.setId(rs.getInt("id"));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to get user by email", e);
        }
        return user;
    }



    @Override
    public String loginUser(String email, String password) {
        String query = "SELECT password FROM users WHERE email = ?";
        try (Connection connection = database.getConnection(); // Use instance method
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                if (storedPassword.equals(HashUtil.hashPassword(password))) { // Compare hashed passwords
                    return java.util.UUID.randomUUID().toString(); // Generate a token
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to login user", e);
        }
        return null;
    }

    @Override
    public boolean isEmailDuplicate(String email) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (Connection connection = database.getConnection(); // Use instance method
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0; // Email exists
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to check email duplication", e);
        }
        return false;
    }

    @Override
    public boolean registerVehicle(Vehicle vehicle) {
        String query = "INSERT INTO vehicles (model, license_number, owner_name, type, user_id, status) VALUES (?, ?, ?, ?, ?, 'Pending')";
        try (Connection connection = database.getConnection(); // Use instance method
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, vehicle.getModel());
            stmt.setString(2, vehicle.getLicenseNumber());
            stmt.setString(3, vehicle.getOwnerName());
            stmt.setString(4, vehicle.getType());
            stmt.setInt(5, vehicle.getUserId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to register vehicle", e);
            return false;
        }
    }

    @Override
    public int getUserIdByVehicleNumber(String vehicleNumber) {
        String query = "SELECT user_id FROM vehicles WHERE vehicle_number = ?";
        try (Connection connection = database.getConnection(); // Use instance method
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, vehicleNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("user_id");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to get user ID by vehicle number", e);
        }
        return -1; // Not found
    }

    @Override
    public boolean registerLicense(License license) {
        String query = "INSERT INTO licenses (name, age, aadhar, address, gender, transaction_id, user_id, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = database.getConnection(); // Use instance method
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
            LOGGER.log(Level.SEVERE, "Failed to register license", e);
            return false;
        }
    }

    @Override
    public boolean payChallan(int challanId) {
        String checkStatusQuery = "SELECT status FROM challans WHERE id = ?";
        String updateStatusQuery = "UPDATE challans SET status = 'Paid' WHERE id = ?";
        try (Connection connection = database.getConnection(); // Use instance method
             PreparedStatement checkStatusStmt = connection.prepareStatement(checkStatusQuery);
             PreparedStatement updateStatusStmt = connection.prepareStatement(updateStatusQuery)) {

            // Check current status
            checkStatusStmt.setInt(1, challanId);
            ResultSet rs = checkStatusStmt.executeQuery();

            if (rs.next()) {
                String currentStatus = rs.getString("status");

                if ("Paid".equalsIgnoreCase(currentStatus)) {
                    System.out.println("Challan is already paid.");
                    return false; // Already paid
                } else {
                    // Update status to 'Paid'
                    updateStatusStmt.setInt(1, challanId);
                    int rowsAffected = updateStatusStmt.executeUpdate();
                    return rowsAffected > 0; // Successfully paid
                }
            } else {
                System.out.println("Challan not found.");
                return false; // Not found
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to pay challan", e);
            return false;
        }
    }

    @Override
    public List<License> getLicenses(int userId) {
        List<License> licenses = new ArrayList<>();
        String query = "SELECT * FROM licenses WHERE user_id = ?";
        try (Connection connection = database.getConnection(); // Use instance method
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
            LOGGER.log(Level.SEVERE, "Failed to get licenses", e);
        }
        return licenses;
    }



    @Override
    public List<Vehicle> getVehicles(int userId) {
        List<Vehicle> vehicles = new ArrayList<>();
        String query = "SELECT * FROM vehicles WHERE user_id = ?";
        try (Connection connection = database.getConnection(); // Use instance method
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
            LOGGER.log(Level.SEVERE, "Failed to get vehicles", e);
        }
        return vehicles;
    }

    @Override
    public List<Challan> getChallans(int userId) {
        List<Challan> challans = new ArrayList<>();
        String query = "SELECT * FROM challans WHERE user_id = ?";
        try (Connection connection = database.getConnection(); // Use instance method
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
            LOGGER.log(Level.SEVERE, "Failed to get challans", e);
        }
        return challans;
    }

    @Override
    public List<Challan> getChallansByVehicleNumber(String vehicleNumber) {
        List<Challan> challans = new ArrayList<>();
        String query = "SELECT * FROM challans WHERE vehicle_number = ?";
        try (Connection connection = database.getConnection(); // Use instance method
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
            LOGGER.log(Level.SEVERE, "Failed to get challans by vehicle number", e);
        }
        return challans;
    }
}
