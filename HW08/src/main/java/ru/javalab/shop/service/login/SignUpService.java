package ru.javalab.shop.service.login;

import ru.javalab.shop.dto.UserDto;

public interface SignUpService {
    UserDto login(String email, String password);
}
