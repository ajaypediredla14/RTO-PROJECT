package controller;

import model.*;

import java.sql.*;

public class AdminController {
    public void viewAppliedVehicleRegistrations() {
        String sql = "SELECT * FROM vehicles";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        ", Model: " + rs.getString("model") +
                        ", License Number: " + rs.getString("license_number") +
                        ", Owner Name: " + rs.getString("owner_name") +
                        ", Type: " + rs.getString("type"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean approveVehicleRegistration(int vehicleId, String generatedNumber) {
        String sql = "UPDATE vehicles SET license_number = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, generatedNumber);
            pstmt.setInt(2, vehicleId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean denyVehicleRegistration(int vehicleId) {
        String sql = "DELETE FROM vehicles WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, vehicleId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void viewAppliedLicenseRegistrations() {
        String sql = "SELECT * FROM licenses";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        ", Name: " + rs.getString("name") +
                        ", Age: " + rs.getInt("age") +
                        ", Aadhar: " + rs.getString("aadhar") +
                        ", Address: " + rs.getString("address") +
                        ", Gender: " + rs.getString("gender") +
                        ", Transaction ID: " + rs.getString("transaction_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean approveLicenseRegistration(int licenseId, String generatedNumber) {
        String sql = "UPDATE licenses SET transaction_id = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, generatedNumber);
            pstmt.setInt(2, licenseId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean denyLicenseRegistration(int licenseId) {
        String sql = "DELETE FROM licenses WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, licenseId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean generateChallan(Challan challan) {
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
