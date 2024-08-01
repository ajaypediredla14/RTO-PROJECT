package model;

import java.sql.Date;

public interface IChallan {
    int getId();
    void setId(int id);
    String getVehicleNumber();
    void setVehicleNumber(String vehicleNumber);
    String getChallanType();
    void setChallanType(String challanType);
    double getAmount();
    void setAmount(double amount);
    Date getDeadline();
    void setDeadline(Date deadline);
    String getStatus();
    void setStatus(String status);
    int getUserId();
    void setUserId(int userId);
}
