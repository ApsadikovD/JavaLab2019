package ru.javalab.chat.util;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static Connection instance;
    private static String ip;
    private static int port;
    private static String user;
    private static String password;

    private DBConnection() {
    }

    public static Connection getInstance() {
        if (instance == null) {
            try {
                instance = DriverManager.getConnection("jdbc:mysql://" + ip + ":" + port + "/javalab_chat?" +
                        "user=" + user + "&" +
                        "password=" + password + "&" +
                        "useUnicode=true&" +
                        "serverTimezone=UTC");
            } catch (SQLException e) {
                System.out.println(e);
                System.out.println("Failed to connect to database");
            }
        }
        return instance;
    }

    public static Connection createInstance(Properties properties) {
        ip = properties.getProperty("ip");
        port = Integer.parseInt(properties.getProperty("port"));
        user = properties.getProperty("user");
        password = properties.getProperty("password");
        return getInstance();
    }
}
