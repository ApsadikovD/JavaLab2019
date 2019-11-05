package ru.javalab.chat.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.javalab.chat.db.dao.MessageDao;
import ru.javalab.chat.db.dao.UserDao;
import ru.javalab.chat.db.model.Message;
import ru.javalab.chat.db.model.User;
import ru.javalab.chat.model.*;
import ru.javalab.chat.util.DBConnection;
import ru.javalab.chat.util.PasswordEncrypt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Optional;

public class ClientThread extends Thread {
    private Socket clientSocket;
    private BufferedReader reader;
    private int userId;
    private String userName;

    public ClientThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
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

    @Override
    public void run() {
        try {
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
                ObjectMapper mapper = new ObjectMapper();
                Request request = mapper.readValue(line, Request.class);
                switch (request.getHeader()) {
                    case "login":
                        Request<LoginCommand> login = mapper.readValue(line, new TypeReference<Request<LoginCommand>>() {
                        });
                        UserDao userDao = new UserDao(DBConnection.getInstance());
                        Optional<User> user = userDao.find(login.getPayload().getName());
                        if (user.isPresent()) {
                            if (PasswordEncrypt.checkPassword(login.getPayload().getPassword(), user.get().getPassword())) {
                                userId = user.get().getId();
                                userName = user.get().getName();
                                Server.getClientList().add(this);
                                writer.println("You entered in chat");
                            } else {
                                writer.println("Invalid Password");
                            }
                        } else {
                            userDao.save(new User(0, login.getPayload().getName(), login.getPayload().getPassword()));
                            User newUser = userDao.find(login.getPayload().getName()).get();
                            userId = newUser.getId();
                            userName = newUser.getName();
                            Server.getClientList().add(this);
                            writer.println("You entered in chat");
                        }
                        break;
                    case "logout":
                        Server.logoutUser(this);
                        break;
                    case "message":
                        Request<MessageCommand> message = mapper.readValue(line, new TypeReference<Request<MessageCommand>>() {
                        });
                        for (ClientThread client : Server.getClientList()) {
                            if (!client.equals(this)) {
                                writer = new PrintWriter(client.clientSocket.getOutputStream(), true);
                                writer.println(userName + ": " + message.getPayload().getMessage());
                            }
                        }
                        MessageDao messageDao = new MessageDao(DBConnection.getInstance());
                        messageDao.save(new Message(0, message.getPayload().getMessage(), userId, null));
                        break;
                    case "get-messages":
                        Request<PaginationCommand> pagination = mapper.readValue(line,
                                new TypeReference<Request<PaginationCommand>>() {
                                });
                        messageDao = new MessageDao(DBConnection.getInstance());
                        Optional<List<Message>> messageList = messageDao.find(pagination.getPayload().getPage());
                        if (messageList.isPresent()) {
                            writer.println(getJson(new PaginationResponse(messageList.get())));
                        } else {
                            writer.println("");
                        }
                        break;
                }
            }
            reader.close();
            clientSocket.close();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
