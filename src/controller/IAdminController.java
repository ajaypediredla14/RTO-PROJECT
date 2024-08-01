package controller;

import model.*;

import java.util.List;

public interface IAdminController {

    boolean approveLicenseRegistration(int licenseId, String generatedNumber);

    List<Vehicle> getPendingVehicleRegistrations();

    boolean approveVehicleRegistration(int vehicleId);

    boolean denyVehicleRegistration(int vehicleId);

    boolean approveLicense(int licenseId);

    boolean denyLicense(int licenseId);

    List<License> getAllLicenses();

    boolean generateChallan(Challan challan);
}
