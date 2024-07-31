package model;

public class User {
    private int id;
    private String email;
    private String password;
    private String name;
    private char type;
    private String mobile;

    public User( String name,String email, String password, char type, String mobile) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.type = type;
        this.mobile = mobile;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public char getType() { return type; }
    public void setType(char type) { this.type = type; }
    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
}
