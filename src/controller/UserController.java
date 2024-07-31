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
        String query = "INSERT INTO licenses (name, age, aadhar, address, gender, transaction_id, user_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, license.getName());
            stmt.setInt(2, license.getAge());
            stmt.setString(3, license.getAadhar());
            stmt.setString(4, license.getAddress());
            stmt.setString(5, String.valueOf(license.getGender()));
            stmt.setString(6, license.getTransactionId());
            stmt.setInt(7, license.getUserId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean payChallan(Challan challan) {
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

    public List<Vehicle> getPendingVehicleRegistrations() {
        List<Vehicle> vehicles = new ArrayList<>();
        String query = "SELECT * FROM vehicles WHERE status = 'Pending'";
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
                        rs.getString("status")
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
        String query = "UPDATE vehicles SET status = 'Denied' WHERE id = ?";
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

    private String generateVehicleNumber() {
        // Generate a random vehicle number
        Random random = new Random();
        int number = random.nextInt(90000) + 10000; // 5-digit number
        return "VEH" + number;
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
                        rs.getString("status")
                );
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
                        rs.getString("status")

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
