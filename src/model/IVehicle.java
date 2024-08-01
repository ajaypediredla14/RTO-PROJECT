package model;

public interface IVehicle {
    int getId();
    void setId(int id);

    String getModel();
    void setModel(String model);

    String getLicenseNumber();
    void setLicenseNumber(String licenseNumber);

    String getOwnerName();
    void setOwnerName(String ownerName);

    String getType();
    void setType(String type);

    int getUserId();
    void setUserId(int userId);

    String getStatus();
    void setStatus(String status);

    String getVehicleNumber();
    void setVehicleNumber(String vehicleNumber);
}
