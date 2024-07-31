package controller;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.*;
import model.Challan;
import model.Database;

public class ChallanController {

    // Method to update the status of a challan
    public static boolean updateChallanStatus(Challan challan) {
        String query = "UPDATE challans SET status = ? WHERE id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            // Set parameters for the SQL query
            stmt.setString(1, challan.getStatus()); // New status (e.g., "Paid")
            stmt.setInt(2, challan.getId()); // Challan ID

            // Execute the update
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0; // Return true if at least one row was updated

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false if an exception occurred
        }
    }

    public static List<Challan> getChallansByVehicle(int vehicleId) {
        List<Challan> challans = new ArrayList<>();
        String query = "SELECT * FROM challans WHERE vehicle_number = (SELECT vehicle_number FROM vehicle WHERE id = ?)";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, vehicleId); // Set vehicle ID parameter

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Challan challan = new Challan(
                            rs.getString("vehicle_number"),
                            rs.getString("challan_type"),
                            rs.getString("amount"),
                            rs.getString("deadline"),
                            rs.getString("status"),
                            vehicleId
                    );
                    challan.setId(rs.getInt("id")); // Set the ID field from the result set
                    challans.add(challan);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return challans; // Return the list of challans
    }
}
