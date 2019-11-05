package ru.javalab.chat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    public void start(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            new Thread(() -> {
                while (true) {
                    try {
                        String message = reader.readLine();
                        System.out.println(message);
                    } catch (IOException e) {
                        throw new IllegalStateException(e);
                    }
                }
            }).start();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void sendRequest(String json) {
        writer.println(json);
    }
}
