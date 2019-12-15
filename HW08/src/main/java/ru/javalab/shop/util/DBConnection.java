package ru.javalab.shop.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection instance;

    private DBConnection() {
    }

    public static Connection getInstance() {
        if (instance == null) {
            try {
                DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
                instance = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/lavalab_shop?" +
                        "user=root&" +
                        "password=1234&" +
                        "useUnicode=true&" +
                        "serverTimezone=UTC");
            } catch (SQLException e) {
                System.out.println(e);
                System.out.println("Failed to connect to database");
            }
        }
        return instance;
    }
}
