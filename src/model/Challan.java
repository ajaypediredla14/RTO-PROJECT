package model;

import java.sql.Date;

public class Challan {
    private int id;
    private String vehicleNumber;
    private String challanType;
    private double amount;
    private Date deadline;
    private String status;
    private int userId;

    public Challan(String vehicleNumber, String challanType, double amount, Date deadline, String status, int userId) {
        this.vehicleNumber = vehicleNumber;
        this.challanType = challanType;
        this.amount = amount;
        this.deadline = deadline;
        this.status = status;
        this.userId = userId;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }
    public String getChallanType() { return challanType; }
    public void setChallanType(String challanType) { this.challanType = challanType; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public Date getDeadline() { return deadline; }
    public void setDeadline(Date deadline) { this.deadline = deadline; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
}
