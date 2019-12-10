package ru.javalab.chat.services.login;

import ru.javalab.chat.dto.UserDto;

public interface LoginService {
    UserDto login(String name, String password);
}
