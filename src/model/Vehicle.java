package model;

public class Vehicle implements IVehicle {
    private int id;
    private String model;
    private String licenseNumber;
    private String ownerName;
    private String type;
    private int userId;
    private String status;
    private String vehicle_number;

    public Vehicle(String model, String licenseNumber, String ownerName, String type, int userId, String status, String vehicle_number) {
        this.model = model;
        this.licenseNumber = licenseNumber;
        this.ownerName = ownerName;
        this.type = type;
        this.userId = userId;
        this.status = status;
        this.vehicle_number = vehicle_number;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public String getLicenseNumber() {
        return licenseNumber;
    }

    @Override
    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    @Override
    public String getOwnerName() {
        return ownerName;
    }

    @Override
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int getUserId() {
        return userId;
    }

    @Override
    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getVehicleNumber() {
        return vehicle_number;
    }

    @Override
    public void setVehicleNumber(String vehicle_number) {
        this.vehicle_number = vehicle_number;
    }
}
