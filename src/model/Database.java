package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import credentials.db;

public class Database implements IDatabase {
    private static final String URL = db.URL;
    private static final String USER = db.USER;
    private static final String PASSWORD = db.PASSWORD;

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
