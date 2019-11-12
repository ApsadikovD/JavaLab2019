package ru.javalab.chat.server;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.javalab.chat.db.dao.CartDao;
import ru.javalab.chat.db.dao.MessageDao;
import ru.javalab.chat.db.dao.ProductDao;
import ru.javalab.chat.db.dao.UserDao;
import ru.javalab.chat.db.model.Cart;
import ru.javalab.chat.db.model.Message;
import ru.javalab.chat.db.model.Product;
import ru.javalab.chat.db.model.User;
import ru.javalab.chat.model.base.Request;
import ru.javalab.chat.model.base.Response;
import ru.javalab.chat.model.command.*;
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
    private PrintWriter writer;
    private int userId;
    private String userName;

    public ClientThread(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        writer = new PrintWriter(clientSocket.getOutputStream(), true);
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
            String line;
            while ((line = reader.readLine()) != null) {
                ObjectMapper mapper = new ObjectMapper();
                Request request = mapper.readValue(line, Request.class);
                switch (request.getHeader()) {
                    case "login":
                        Request<CmdLogin> login = mapper.readValue(line, new TypeReference<Request<CmdLogin>>() {
                        });
                        login(login);
                        break;
                    case "logout":
                        Server.logoutUser(this);
                        break;
                    case "message/send":
                        Request<CmdMessage> message = mapper.readValue(line, new TypeReference<Request<CmdMessage>>() {
                        });
                        messageAdd(message);
                        break;
                    case "product/add":
                        Request<CmdAddProduct> addProductRequest = mapper.readValue(line,
                                new TypeReference<Request<CmdAddProduct>>() {
                                });
                        productAdd(addProductRequest);
                        break;
                    case "product/delete":
                        Request<CmdDeleteProduct> deleteProductRequest = mapper.readValue(line,
                                new TypeReference<Request<CmdDeleteProduct>>() {
                                });
                        productDelete(deleteProductRequest);
                        break;
                    case "message/get":
                        Request<CmdPagination> pagination = mapper.readValue(line, new TypeReference<Request<CmdPagination>>() {
                        });
                        messageGet(pagination);
                        break;
                    case "product/get":
                        pagination = mapper.readValue(line, new TypeReference<Request<CmdPagination>>() {
                        });
                        productGet(pagination);
                        break;
                    case "cart/add":
                        Request<CmdAddCart> cart = mapper.readValue(line, new TypeReference<Request<CmdAddCart>>() {
                        });
                        addCart(cart);
                        break;
                }
            }
            reader.close();
            clientSocket.close();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void addCart(Request<CmdAddCart> cart) {
        if (cart.getToken() == null) return;
        Algorithm algorithm = Algorithm.HMAC256("QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm");
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(cart.getToken());
            CartDao cartDao = new CartDao(DBConnection.getInstance());
            cartDao.save(new Cart(userId, cart.getPayload().getProductId()));
        } catch (JWTVerificationException exception) {
        }
    }

    private void messageGet(Request<CmdPagination> pagination) throws IOException {
        MessageDao messageDao = new MessageDao(DBConnection.getInstance());
        Optional<List<Message>> messageList = messageDao.find(pagination.getPayload().getPage());
        write(getJson(new Response<>(messageList.get())));
    }

    private void productGet(Request<CmdPagination> pagination) throws IOException {
        ProductDao productDao = new ProductDao(DBConnection.getInstance());
        Optional<List<Product>> productList = productDao.find(pagination.getPayload().getPage());
        write(getJson(new Response<>(productList.get())));
    }

    private void messageAdd(Request<CmdMessage> message) throws IOException {
        for (ClientThread client : Server.getClientList()) {
            if (!client.equals(this)) {
                write(userName + ": " + message.getPayload().getMessage());
            }
        }
        MessageDao messageDao = new MessageDao(DBConnection.getInstance());
        messageDao.save(new Message(0, message.getPayload().getMessage(), userId, null));
    }

    private void login(Request<CmdLogin> login) throws IOException {
        UserDao userDao = new UserDao(DBConnection.getInstance());
        Optional<User> user = userDao.find(login.getPayload().getName());
        if (user.isPresent()) {
            if (PasswordEncrypt.checkPassword(login.getPayload().getPassword(), user.get().getPassword())) {
                userId = user.get().getId();
                userName = user.get().getName();
                Server.getClientList().add(this);
                write(getJson(new Response<>(generateToken(user.get().getRole() == 1))));
            } else {
                write(getJson(new Response<>(null)));
            }
        } else {
            userDao.save(new User(0, login.getPayload().getName(), login.getPayload().getPassword(), 0));
            User newUser = userDao.find(login.getPayload().getName()).get();
            userId = newUser.getId();
            userName = newUser.getName();
            Server.getClientList().add(this);
            write(getJson(new Response<>(generateToken(newUser.getRole() == 1))));
        }
    }

    private void productAdd(Request<CmdAddProduct> product) {
        if (product.getToken() == null) return;
        Algorithm algorithm = Algorithm.HMAC256("QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm");
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(product.getToken());
            if (jwt.getClaim("role").asBoolean()) {
                ProductDao productDao = new ProductDao(DBConnection.getInstance());
                productDao.save(new Product(0, product.getPayload().getName(), Float.valueOf(product.getPayload().getPrice())));
            }
        } catch (JWTVerificationException exception) {
        }
    }

    private void productDelete(Request<CmdDeleteProduct> product) {
        if (product.getToken() == null) return;
        Algorithm algorithm = Algorithm.HMAC256("QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm");
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(product.getToken());
            if (jwt.getClaim("role").asBoolean()) {
                ProductDao productDao = new ProductDao(DBConnection.getInstance());
                productDao.delete(product.getPayload().getId());
            }
        } catch (JWTVerificationException exception) {
        }
    }

    private String generateToken(boolean isAdmin) {
        Algorithm algorithm = Algorithm.HMAC256("QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm");
        String token = JWT.create()
                .withSubject(String.valueOf(userId))
                .withClaim("role", isAdmin)
                .sign(algorithm);
        return token;
    }

    public void write(String response) {
        writer.println(response);
    }
}
