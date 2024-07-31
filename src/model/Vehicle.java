package model;

public class Vehicle {
    private int id;
    private String model;
    private String licenseNumber;
    private String ownerName;
    private String type;
    private int userId;
    private String status; // Added status field
    private String vehicle_number;

    public Vehicle(String model, String licenseNumber, String ownerName, String type, int userId,String status,String vehicle_number) {
        this.model = model;
        this.licenseNumber = licenseNumber;
        this.ownerName = ownerName;
        this.type = type;
        this.userId = userId;
        this.status=status;
        this.vehicle_number=vehicle_number;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public String getVehicle_number() {
        return vehicle_number;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
