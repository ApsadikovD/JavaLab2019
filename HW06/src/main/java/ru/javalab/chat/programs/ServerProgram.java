package ru.javalab.chat.programs;

import ru.javalab.chat.server.Server;
import ru.javalab.chat.util.DBConnection;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ServerProgram {
    public static void main(String[] args) {
        int port = 8001;
        String pathToProperties = "-port";
        for (String param : args) {
            String name = param.split("=")[0];
            String arg = param.split("=")[1];
            if (name.equals("--port")) {
                port = Integer.parseInt(arg);
            } else if (name.equals("--db-properties-path")) {
                pathToProperties = arg;
            }
        }
        try {
            InputStream file = new FileInputStream(pathToProperties);
            Properties properties = new Properties();
            properties.load(file);
            DBConnection.createInstance(properties);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Server server = new Server();
        server.start(port);
    }
}
