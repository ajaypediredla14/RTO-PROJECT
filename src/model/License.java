package model;

public class License implements ILicense {
    private int id;
    private String name;
    private int age;
    private String aadhar;
    private String address;
    private char gender;
    private String transactionId;
    private int userId;
    private String status;
    private String license_number;

    public License(String name, int age, String aadhar, String address, char gender, String transactionId, int userId, String status, String license_number) {
        this.name = name;
        this.age = age;
        this.aadhar = aadhar;
        this.address = address;
        this.gender = gender;
        this.transactionId = transactionId;
        this.userId = userId;
        this.status = status;
        this.license_number = license_number;
    }

    @Override
    public int getId() { return id; }
    @Override
    public void setId(int id) { this.id = id; }
    @Override
    public String getName() { return name; }
    @Override
    public void setName(String name) { this.name = name; }
    @Override
    public int getAge() { return age; }
    @Override
    public void setAge(int age) { this.age = age; }
    @Override
    public String getAadhar() { return aadhar; }
    @Override
    public void setAadhar(String aadhar) { this.aadhar = aadhar; }
    @Override
    public String getAddress() { return address; }
    @Override
    public void setAddress(String address) { this.address = address; }
    @Override
    public char getGender() { return gender; }
    @Override
    public void setGender(char gender) { this.gender = gender; }
    @Override
    public String getTransactionId() { return transactionId; }
    @Override
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    @Override
    public int getUserId() { return userId; }
    @Override
    public void setUserId(int userId) { this.userId = userId; }
    @Override
    public String getStatus() { return status; }
    @Override
    public void setStatus(String status) { this.status = status; }
    @Override
    public String getLicenseNumber() { return license_number; }
    @Override
    public void setLicenseNumber(String license_number) { this.license_number = license_number; }
}
