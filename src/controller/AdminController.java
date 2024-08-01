package controller;

import model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminController implements IAdminController {

    private static final Logger LOGGER = Logger.getLogger(AdminController.class.getName());
    private Database database = new Database(); // Create an instance of Database

    @Override
    public boolean approveLicenseRegistration(int licenseId, String generatedNumber) {
        String sql = "UPDATE licenses SET transaction_id = ? WHERE id = ?";
        try (Connection conn = database.getConnection(); // Use instance method
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, generatedNumber);
            pstmt.setInt(2, licenseId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to approve license registration", e);
            return false;
        }
    }


    @Override
    public List<License> getAllLicenses() {
        List<License> licenses = new ArrayList<>();
        String query = "SELECT * FROM licenses";
        try (Connection connection = database.getConnection();
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



    public List<Vehicle> getPendingVehicleRegistrations() {
        List<Vehicle> vehicles = new ArrayList<>();
        String query = "SELECT * FROM vehicles";
        try (Connection connection = database.getConnection();
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

    @Override
    public boolean approveVehicleRegistration(int vehicleId) {
        String vehicleNumber = generateVehicleNumber();
        String query = "UPDATE vehicles SET status = 'Completed', vehicle_number = ? WHERE id = ?";
        try (Connection connection = database.getConnection(); // Use instance method
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, vehicleNumber);
            stmt.setInt(2, vehicleId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to approve vehicle registration", e);
            return false;
        }
    }

    @Override
    public boolean denyVehicleRegistration(int vehicleId) {
        String query = "UPDATE vehicles SET vehicle_number = NULL, status = 'Denied' WHERE id = ?";
        try (Connection connection = database.getConnection(); // Use instance method
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, vehicleId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to deny vehicle registration", e);
            return false;
        }
    }

    private String generateVehicleNumber() {
        Random random = new Random();
        int number = random.nextInt(90000) + 10000; // 5-digit number
        return "VEH" + number;
    }

    @Override
    public boolean approveLicense(int licenseId) {
        String licenseNumber = generateUniqueLicenseNumber();
        String query = "UPDATE licenses SET status = 'Completed', license_number = ? WHERE id = ?";
        try (Connection connection = database.getConnection(); // Use instance method
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, licenseNumber);
            statement.setInt(2, licenseId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to approve license", e);
            return false;
        }
    }

    private String generateUniqueLicenseNumber() {
        return "LIC-" + System.currentTimeMillis();
    }

    @Override
    public boolean denyLicense(int licenseId) {
        String query = "UPDATE licenses SET status = 'Denied', license_number = NULL WHERE id = ?";
        try (Connection connection = database.getConnection(); // Use instance method
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, licenseId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to deny license", e);
            return false;
        }
    }

    @Override
    public boolean generateChallan(Challan challan) {
        String query = "INSERT INTO challans (vehicle_number, challan_type, amount, deadline, status, user_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = database.getConnection(); // Use instance method
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
            LOGGER.log(Level.SEVERE, "Failed to generate challan", e);
            return false;
        }
    }
}
