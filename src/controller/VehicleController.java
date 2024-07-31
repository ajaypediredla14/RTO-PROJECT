package controller;

import model.Database;
import model.User;
import model.Vehicle;
import util.HashUtil;

import java.sql.*;

public class VehicleController {
    public static Vehicle userVehicle(int userId) {
        String query = "SELECT * FROM vehicle WHERE user_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            // Set the user_id parameter in the query
            stmt.setInt(1, userId);

            // Execute the query
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Create a Vehicle object and populate it with data from the ResultSet
                    Vehicle vehicle = new Vehicle();
                    vehicle.setId(rs.getInt("id"));
                    vehicle.setModel(rs.getString("model"));
                    vehicle.setLicenseNumber(rs.getString("licenseNumber"));
                    vehicle.setOwnerName(rs.getString("ownerName"));
                    vehicle.setType(rs.getString("type"));
                    vehicle.setUserId(rs.getInt("userId"));
                    vehicle.setStatus(rs.getString("status"));

                    return vehicle;
                } else {
                    // Return null if no vehicle is found for the user
                    return null;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
