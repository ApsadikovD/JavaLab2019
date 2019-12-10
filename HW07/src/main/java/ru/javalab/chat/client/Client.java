package ru.javalab.chat.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.javalab.chat.dto.*;
import ru.javalab.chat.protocol.*;
import ru.javalab.chat.protocol.base.Request;
import ru.javalab.chat.protocol.base.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    private Boolean isFinished;
    private String command;
    private String token;

    public Client() {
        isFinished = false;
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

    public void start(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            writer = new PrintWriter(socket.getOutputStream(), true);
            reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            new Thread(() -> {
                ObjectMapper mapper = new ObjectMapper();
                String line;
                try {
                    while (!isFinished && (line = reader.readLine()) != null) {
                        if (command.equals("login")) {
                            Response<String> auth = mapper.readValue(line,
                                    new TypeReference<Response<String>>() {
                                    });
                            if (auth.getData() != null) {
                                token = auth.getData();
                                System.out.println("You entered in chat");
                                startMessage();
                            } else {
                                System.out.println("Password invalid");
                                auth();
                            }
                        } else if (command.equals("message/get")) {
                            Response<List<MessageDto>> messages = mapper.readValue(line,
                                    new TypeReference<Response<List<MessageDto>>>() {
                                    });
                            messages.getData()
                                    .forEach(message -> System.out.println(message.getUsername()
                                            + ": " + message.getMessage()));
                        } else if (command.equals("product/get")) {
                            Response<List<ProductDto>> products = mapper.readValue(line,
                                    new TypeReference<Response<List<ProductDto>>>() {
                                    });
                            products.getData()
                                    .forEach(product -> System.out.println("Id: " + product.getId()
                                            + ", Name: " + product.getName()
                                            + ", Price:" + product.getPrice())
                                    );
                        } else {
                            System.out.println(line);
                            break;
                        }
                    }
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }).start();
            auth();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void startMessage() {
        Scanner sc = new Scanner(System.in);
        new Thread(() -> {
            while (!isFinished) {
                String text = sc.nextLine();
                if (text.equals("/logout")) {
                    isFinished = true;
                    Request logoutRequest = new Request<>("logout", null);
                    logoutRequest.setToken(token);
                    sendRequest(getJson(logoutRequest));
                } else if (text.contains("/message/get")) {
                    command = "message/get";
                    int page = Integer.parseInt(text.substring(text.lastIndexOf("/") + 1, text.length()));
                    Request<CmdPagination> paginationRequest = new Request<>("message/get",
                            new CmdPagination(page));
                    paginationRequest.setToken(token);
                    sendRequest(getJson(paginationRequest));
                } else if (text.contains("/product/get")) {
                    command = "product/get";
                    int page = Integer.parseInt(text.substring(text.lastIndexOf("/") + 1, text.length()));
                    Request<CmdPagination> paginationRequest = new Request<>("product/get",
                            new CmdPagination(page));
                    paginationRequest.setToken(token);
                    sendRequest(getJson(paginationRequest));
                } else if (text.equals("/product/add")) {
                    System.out.println("Name: ");
                    String name = sc.nextLine();
                    System.out.println("Price: ");
                    String price = sc.nextLine();
                    Request<CmdAddProduct> addProductRequest = new Request<>("product/add",
                            new CmdAddProduct(price, name));
                    addProductRequest.setToken(token);
                    sendRequest(getJson(addProductRequest));
                } else if (text.equals("/product/delete")) {
                    System.out.println("id: ");
                    String id = sc.nextLine();
                    Request<CmdDeleteProduct> deleteProductRequest = new Request<>("product/delete",
                            new CmdDeleteProduct(Integer.valueOf(id)));
                    deleteProductRequest.setToken(token);
                    sendRequest(getJson(deleteProductRequest));
                } else if (text.equals("/cart/add")) {
                    System.out.println("Product id:");
                    String id = sc.nextLine();
                    Request<CmdAddCart> addCartRequest = new Request<>("cart/add",
                            new CmdAddCart(Integer.valueOf(id)));
                    addCartRequest.setToken(token);
                    sendRequest(getJson(addCartRequest));
                } else {
                    Request<CmdMessage> messageRequest = new Request<>("message/send", new CmdMessage(text));
                    messageRequest.setToken(token);
                    sendRequest(getJson(messageRequest));
                }
            }
        }).start();
    }

    private void auth() {
        Scanner sc = new Scanner(System.in);
        command = "login";
        System.out.println("/login");
        System.out.println("Name:");
        String name = sc.nextLine();
        System.out.println("Password:");
        String password = sc.nextLine();

        Request<CmdLogin> loginRequest = new Request<>("login", new CmdLogin(name, password));
        sendRequest(getJson(loginRequest));
    }

    private void sendRequest(String json) {
        writer.println(json);
    }
}
