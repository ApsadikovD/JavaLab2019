package ru.javalab.chat.programs;

import ru.javalab.chat.client.Client;

import java.util.Scanner;

public class ClientProgram {
    public static void main(String[] args) {
        int port = 8000;
        String ip = "";
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
        Scanner sc = new Scanner(System.in);
        System.out.println("Name:");
        String name = sc.nextLine();
        System.out.println("Password:");
        String password = sc.nextLine();
        client.loginOrSignUp(name, password);

        while (true) {
            String message = sc.nextLine();
            client.sendMessage(message);
        }
    }
}
