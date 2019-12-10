package ru.javalab.chat.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.javalab.chat.context.ApplicationContext;
import ru.javalab.chat.context.ApplicationContextReflectionBased;
import ru.javalab.chat.dispatcher.RequestsDispatcher;
import ru.javalab.chat.dto.MessageDto;
import ru.javalab.chat.dto.ProductDto;
import ru.javalab.chat.dto.UserDto;
import ru.javalab.chat.model.Cart;
import ru.javalab.chat.protocol.*;
import ru.javalab.chat.protocol.base.Request;
import ru.javalab.chat.protocol.base.Response;
import ru.javalab.chat.repositories.cart.CartRepositoryImpl;
import ru.javalab.chat.repositories.message.MessageRepositoryImpl;
import ru.javalab.chat.repositories.product.ProductRepositoryImpl;
import ru.javalab.chat.services.cart.AddProductToCartService;
import ru.javalab.chat.services.cart.AddProductToCartServiceImpl;
import ru.javalab.chat.services.login.LoginServiceImpl;
import ru.javalab.chat.services.message.AddMessageService;
import ru.javalab.chat.services.message.AddMessageServiceImpl;
import ru.javalab.chat.services.message.PaginationMessageService;
import ru.javalab.chat.services.message.PaginationMessageServiceImpl;
import ru.javalab.chat.services.product.*;
import ru.javalab.chat.util.TokenHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ClientThread extends Thread {
    private Socket clientSocket;
    private BufferedReader reader;
    private PrintWriter writer;
    private int userId;
    private String userName;
    private boolean isLogout;
    private RequestsDispatcher requestsDispatcher;

    public ClientThread(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        writer = new PrintWriter(clientSocket.getOutputStream(), true);
        isLogout = false;

        ApplicationContext applicationContext = new ApplicationContextReflectionBased();
        LoginServiceImpl loginService = applicationContext.getComponent(LoginServiceImpl.class, "loginService");
        requestsDispatcher = new RequestsDispatcher();
        requestsDispatcher.addService("loginService", loginService);
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
            while (!isLogout && (line = reader.readLine()) != null) {
                ObjectMapper mapper = new ObjectMapper();
                Request request = mapper.readValue(line, Request.class);
                if (request.getHeader().equals("login") || TokenHandler.verifyToken(request.getToken(), false)) {
                    switch (request.getHeader()) {
                        case "login":
                            Request<CmdLogin> login = mapper.readValue(line, new TypeReference<Request<CmdLogin>>() {
                            });
                            UserDto user = (UserDto) requestsDispatcher.doDispatch(login);
                            if (user != null) {
                                Server.getClientList().add(this);
                                write(getJson(Response.build(user.getToken())));
                            } else {
                                write(getJson(Response.build(null)));
                            }
                            break;
                        case "logout":
                            Server.getClientList().remove(this);
                            isLogout = true;
                            break;
                        case "message/send":
                            Request<CmdMessage> message = mapper.readValue(line, new TypeReference<Request<CmdMessage>>() {
                            });
                            messageAdd(message);
                            break;
                        case "product/add":
                            if (TokenHandler.verifyToken(request.getToken(), true)) {
                                Request<CmdAddProduct> addProductRequest = mapper.readValue(line,
                                        new TypeReference<Request<CmdAddProduct>>() {
                                        });
                                productAdd(addProductRequest);
                            }
                            break;
                        case "product/delete":
                            if (TokenHandler.verifyToken(request.getToken(), false)) {
                                Request<CmdDeleteProduct> deleteProductRequest = mapper.readValue(line,
                                        new TypeReference<Request<CmdDeleteProduct>>() {
                                        });
                                productDelete(deleteProductRequest);
                            }
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
            }
            reader.close();
            clientSocket.close();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void addCart(Request<CmdAddCart> cart) {
        AddProductToCartService addProductToCartService = new AddProductToCartServiceImpl(new CartRepositoryImpl());
        addProductToCartService.addProductToCart(new Cart(userId, cart.getPayload().getProductId()));
    }

    private void messageGet(Request<CmdPagination> pagination) {
        PaginationMessageService paginationMessageService = new PaginationMessageServiceImpl(new MessageRepositoryImpl());
        List<MessageDto> messageDtoList = paginationMessageService.find(pagination.getPayload().getPage());
        write(getJson(Response.build(messageDtoList)));
    }

    private void messageAdd(Request<CmdMessage> message) {
        for (ClientThread client : Server.getClientList()) {
            if (!client.equals(this)) {
                write(userName + ": " + message.getPayload().getMessage());
            }
        }
        AddMessageService addMessageService = new AddMessageServiceImpl(new MessageRepositoryImpl());
        addMessageService.save(message.getPayload().getMessage(), userId);
    }

    private void productGet(Request<CmdPagination> pagination) {
        PaginationProductService paginationProductService = new PaginationProductServiceImpl(new ProductRepositoryImpl());
        List<ProductDto> productList = paginationProductService.find(pagination.getPayload().getPage());
        write(getJson(Response.build(productList)));
    }

    private void productDelete(Request<CmdDeleteProduct> product) {
        DeleteProductServiceImpl productService = new DeleteProductServiceImpl(new ProductRepositoryImpl());
        productService.deleteProduct(product.getPayload().getId());
    }

    private void productAdd(Request<CmdAddProduct> product) {
        AddProductService addProductService = new AddProductServiceImpl(new ProductRepositoryImpl());
        addProductService.addProduct(product.getPayload().getName(), Float.parseFloat(product.getPayload().getPrice()));
    }

    private void write(String response) {
        writer.println(response);
    }
}
