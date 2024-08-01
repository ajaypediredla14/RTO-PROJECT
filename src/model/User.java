package model;

public class User implements IUser {
    private int id;
    private String email;
    private String password;
    private String name;
    private char type;
    private String mobile;

    public User(String name, String email, String password, char type, String mobile) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.type = type;
        this.mobile = mobile;
    }

    @Override
    public int getId() { return id; }
    @Override
    public void setId(int id) { this.id = id; }
    @Override
    public String getEmail() { return email; }
    @Override
    public void setEmail(String email) { this.email = email; }
    @Override
    public String getPassword() { return password; }
    @Override
    public void setPassword(String password) { this.password = password; }
    @Override
    public String getName() { return name; }
    @Override
    public void setName(String name) { this.name = name; }
    @Override
    public char getType() { return type; }
    @Override
    public void setType(char type) { this.type = type; }
    @Override
    public String getMobile() { return mobile; }
    @Override
    public void setMobile(String mobile) { this.mobile = mobile; }
}
