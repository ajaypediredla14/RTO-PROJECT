package controller;

import model.*;
import util.HashUtil;

import java.sql.*;
import java.util.UUID;

public class UserController {
    public boolean registerUser(User user) {
        String sql = "INSERT INTO users(email, password, name, type, mobile) VALUES(?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, HashUtil.hashPassword(user.getPassword()));
            pstmt.setString(3, user.getName());
            pstmt.setString(4, String.valueOf(user.getType()));
            pstmt.setString(5, user.getMobile());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String loginUser(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, HashUtil.hashPassword(password));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return UUID.randomUUID().toString();
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean registerVehicle(Vehicle vehicle) {
        String sql = "INSERT INTO vehicles(model, license_number, owner_name, type, user_id) VALUES(?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, vehicle.getModel());
            pstmt.setString(2, vehicle.getLicenseNumber());
            pstmt.setString(3, vehicle.getOwnerName());
            pstmt.setString(4, vehicle.getType());
            pstmt.setInt(5, vehicle.getUserId());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean registerLicense(License license) {
        String sql = "INSERT INTO licenses(name, age, aadhar, address, gender, transaction_id, user_id) VALUES(?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, license.getName());
            pstmt.setInt(2, license.getAge());
            pstmt.setString(3, license.getAadhar());
            pstmt.setString(4, license.getAddress());
            pstmt.setString(5, String.valueOf(license.getGender()));
            pstmt.setString(6, license.getTransactionId());
            pstmt.setInt(7, license.getUserId());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void viewProfile(int userId) {
        String sqlVehicles = "SELECT * FROM vehicles WHERE user_id = ?";
        String sqlLicenses = "SELECT * FROM licenses WHERE user_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmtVehicles = conn.prepareStatement(sqlVehicles);
             PreparedStatement pstmtLicenses = conn.prepareStatement(sqlLicenses)) {
            pstmtVehicles.setInt(1, userId);
            ResultSet rsVehicles = pstmtVehicles.executeQuery();
            System.out.println("Vehicles:");
            while (rsVehicles.next()) {
                System.out.println("Model: " + rsVehicles.getString("model") +
                        ", License Number: " + rsVehicles.getString("license_number") +
                        ", Owner Name: " + rsVehicles.getString("owner_name") +
                        ", Type: " + rsVehicles.getString("type"));
            }

            pstmtLicenses.setInt(1, userId);
            ResultSet rsLicenses = pstmtLicenses.executeQuery();
            System.out.println("Licenses:");
            while (rsLicenses.next()) {
                System.out.println("Name: " + rsLicenses.getString("name") +
                        ", Age: " + rsLicenses.getInt("age") +
                        ", Aadhar: " + rsLicenses.getString("aadhar") +
                        ", Address: " + rsLicenses.getString("address") +
                        ", Gender: " + rsLicenses.getString("gender") +
                        ", Transaction ID: " + rsLicenses.getString("transaction_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean payChallan(Challan challan) {
        String sql = "INSERT INTO challans(vehicle_number, challan_type, amount, deadline, status, user_id) VALUES(?, ?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, challan.getVehicleNumber());
            pstmt.setString(2, challan.getChallanType());
            pstmt.setDouble(3, challan.getAmount());
            pstmt.setDate(4, challan.getDeadline());
            pstmt.setString(5, challan.getStatus());
            pstmt.setInt(6, challan.getUserId());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
