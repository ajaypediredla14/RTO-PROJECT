package model;

public interface ILicense {
    int getId();
    void setId(int id);

    String getName();
    void setName(String name);

    int getAge();
    void setAge(int age);

    String getAadhar();
    void setAadhar(String aadhar);

    String getAddress();
    void setAddress(String address);

    char getGender();
    void setGender(char gender);

    String getTransactionId();
    void setTransactionId(String transactionId);

    int getUserId();
    void setUserId(int userId);

    String getStatus();
    void setStatus(String status);

    String getLicenseNumber();
    void setLicenseNumber(String licenseNumber);
}
