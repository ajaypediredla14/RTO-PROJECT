package model;

import java.sql.Date;

public class Challan implements IChallan {
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

    @Override
    public int getId() { return id; }

    @Override
    public void setId(int id) { this.id = id; }

    @Override
    public String getVehicleNumber() { return vehicleNumber; }

    @Override
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }

    @Override
    public String getChallanType() { return challanType; }

    @Override
    public void setChallanType(String challanType) { this.challanType = challanType; }

    @Override
    public double getAmount() { return amount; }

    @Override
    public void setAmount(double amount) { this.amount = amount; }

    @Override
    public Date getDeadline() { return deadline; }

    @Override
    public void setDeadline(Date deadline) { this.deadline = deadline; }

    @Override
    public String getStatus() { return status; }

    @Override
    public void setStatus(String status) { this.status = status; }

    @Override
    public int getUserId() { return userId; }

    @Override
    public void setUserId(int userId) { this.userId = userId; }
}
