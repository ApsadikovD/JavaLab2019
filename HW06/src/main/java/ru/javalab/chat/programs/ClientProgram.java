package ru.javalab.chat.programs;

import ru.javalab.chat.client.Client;

public class ClientProgram {

    public static void main(String[] args) {
        int port = 8001;
        String ip = "127.0.0.1";
        for (String param : args) {
            String name = param.split("=")[0];
            String arg = param.split("=")[1];
            if (name.equals("--server-ip")) {
                ip = arg;
            } else if (name.equals("--server-port")) {
                port = Integer.parseInt(arg);
            }
        }
        Client client = new Client();
        client.start(ip, port);
    }
}
