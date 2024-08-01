package controller;

import model.User;
import model.Vehicle;
import model.License;
import model.Challan;

import java.util.List;

public interface IUserController {

    // User-related methods
    boolean registerUser(User user);
    User getUserByEmail(String email);
    String loginUser(String email, String password);
    boolean isEmailDuplicate(String email);

    // Vehicle-related methods
    boolean registerVehicle(Vehicle vehicle);
    int getUserIdByVehicleNumber(String vehicleNumber);
    List<Vehicle> getVehicles(int userId);

    // License-related methods
    boolean registerLicense(License license);
    List<License> getLicenses(int userId);



    // Challan-related methods
    boolean payChallan(int challanId);
    List<Challan> getChallans(int userId);
    List<Challan> getChallansByVehicleNumber(String vehicleNumber);
}
