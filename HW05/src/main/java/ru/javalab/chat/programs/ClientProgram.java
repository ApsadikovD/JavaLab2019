package ru.javalab.chat.programs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.javalab.chat.client.Client;
import ru.javalab.chat.model.LoginCommand;
import ru.javalab.chat.model.MessageCommand;
import ru.javalab.chat.model.PaginationCommand;
import ru.javalab.chat.model.Request;

import java.util.Scanner;

public class ClientProgram {

    public static void main(String[] args) {
        Client client = initClient(args);

        Scanner sc = new Scanner(System.in);
        System.out.println("/login");
        System.out.println("Name:");
        String name = sc.nextLine();
        System.out.println("Password::");
        String password = sc.nextLine();

        Request<LoginCommand> loginRequest = new Request<>("login", new LoginCommand(name, password));
        client.sendRequest(getJson(loginRequest));

        boolean isFinish = false;
        while (!isFinish) {
            String text = sc.nextLine();
            if (text.equals("/logout")) {
                Request logoutRequest = new Request<>("logout", null);
                client.sendRequest(getJson(logoutRequest));
                isFinish = true;
            } else if (text.contains("/get-messages")) {
                int page = Integer.parseInt(text.substring(text.lastIndexOf("/") + 1, text.length()));
                Request<PaginationCommand> paginationRequest = new Request<>("get-messages",
                        new PaginationCommand(page));
                client.sendRequest(getJson(paginationRequest));
            } else {
                Request<MessageCommand> messageRequest = new Request<>("message", new MessageCommand(text));
                client.sendRequest(getJson(messageRequest));
            }
        }
    }

    private static String getJson(Object o) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Client initClient(String[] args) {
        int port = 8000;
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
        return client;
    }
}
