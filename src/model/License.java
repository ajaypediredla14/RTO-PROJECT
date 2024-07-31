package model;

public class License {
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

    public License(String name, int age, String aadhar, String address, char gender, String transactionId, int userId,String status,String license_number) {
        this.name = name;
        this.age = age;
        this.aadhar = aadhar;
        this.address = address;
        this.gender = gender;
        this.transactionId = transactionId;
        this.userId = userId;
        this.status=status;
        this.license_number=license_number;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public String getAadhar() { return aadhar; }
    public void setAadhar(String aadhar) { this.aadhar = aadhar; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public char getGender() { return gender; }
    public void setGender(char gender) { this.gender = gender; }
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getLicense_number() {
        return license_number;
    }
}
