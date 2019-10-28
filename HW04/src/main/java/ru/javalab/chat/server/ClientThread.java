package ru.javalab.chat.server;

import ru.javalab.chat.db.dao.MessageDao;
import ru.javalab.chat.db.dao.UserDao;
import ru.javalab.chat.db.model.Message;
import ru.javalab.chat.db.model.User;
import ru.javalab.chat.util.DBConnection;
import ru.javalab.chat.util.PasswordEncrypt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Optional;

public class ClientThread extends Thread {
    private Socket clientSocket;
    private BufferedReader reader;
    private int userId;
    private String userName;

    public ClientThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
                if (!Server.getClientList().contains(this)) {
                    String name = line.split(" ")[0];
                    String password = line.split(" ")[1];
                    UserDao userDao = new UserDao(DBConnection.getInstance());
                    Optional<User> user = userDao.find(name);
                    if (user.isPresent()) {
                        if (PasswordEncrypt.checkPassword(password, user.get().getPassword())) {
                            userId = user.get().getId();
                            userName = user.get().getName();
                            Server.getClientList().add(this);
                            writer.println("You entered in chat");
                        } else {
                            System.out.println("Invalid Password");
                        }
                    } else {
                        userDao.save(new User(0, name, password));
                        userId = userDao.find(name).get().getId();
                        userName = user.get().getName();
                        Server.getClientList().add(this);
                        writer.println("You entered in chat");
                    }
                } else {
                    for (ClientThread client : Server.getClientList()) {
                        if (!client.equals(this)) {
                            writer = new PrintWriter(client.clientSocket.getOutputStream(), true);
                            writer.println(userName + ": " + line);
                        }
                    }
                    MessageDao messageDao = new MessageDao(DBConnection.getInstance());
                    messageDao.save(new Message(0, line, userId, null));
                }
            }
            reader.close();
            clientSocket.close();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
