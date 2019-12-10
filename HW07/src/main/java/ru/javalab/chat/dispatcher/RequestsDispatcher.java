package ru.javalab.chat.dispatcher;

import ru.javalab.chat.dto.Dto;
import ru.javalab.chat.protocol.CmdLogin;
import ru.javalab.chat.protocol.base.Request;
import ru.javalab.chat.services.login.LoginService;

import java.util.HashMap;

public class RequestsDispatcher {
    private HashMap<String, Object> serviceMap;

    public RequestsDispatcher() {
        serviceMap = new HashMap<>();
    }

    public void addService(String serviceKey, Object service) {
        serviceMap.put(serviceKey, service);
    }

    public Dto doDispatch(Request request) {
        if (request.getHeader().equals("login")) {
            CmdLogin requestLogin = ((Request<CmdLogin>) request).getPayload();
            return ((LoginService) serviceMap.get("login")).login(requestLogin.getName(), requestLogin.getPassword());
        } else {
            throw new IllegalArgumentException();
        }
    }
}
