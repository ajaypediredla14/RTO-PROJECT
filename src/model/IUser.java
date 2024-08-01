package model;

public interface IUser {
    int getId();
    void setId(int id);

    String getEmail();
    void setEmail(String email);

    String getPassword();
    void setPassword(String password);

    String getName();
    void setName(String name);

    char getType();
    void setType(char type);

    String getMobile();
    void setMobile(String mobile);
}
