package ru.javalab.chat.services.login;

import ru.javalab.chat.context.Component;
import ru.javalab.chat.dto.UserDto;
import ru.javalab.chat.model.User;
import ru.javalab.chat.repositories.user.UserRepositoryImpl;
import ru.javalab.chat.util.PasswordEncrypt;
import ru.javalab.chat.util.TokenHandler;

import java.util.Optional;

public class LoginServiceImpl implements LoginService, Component {
    private UserRepositoryImpl userRepository;

    public LoginServiceImpl() {
    }

    @Override
    public UserDto login(String name, String password) {
        Optional<User> user = userRepository.findByName(name);
        if (user.isPresent()) {
            if (PasswordEncrypt.checkPassword(password, user.get().getPassword())) {
                UserDto userDto = new UserDto(user.get().getId(), user.get().getName(),
                        TokenHandler.generateToken(user.get().getId(), user.get().getRole() == 1));
                return userDto;
            }
        } else {
            userRepository.save(new User(name, password, 0));
            User newUser = userRepository.findByName(name).get();
            UserDto userDto = new UserDto(newUser.getId(), newUser.getName(),
                    TokenHandler.generateToken(newUser.getId(), newUser.getRole() == 1));
            return userDto;
        }
        return null;
    }

    @Override
    public String getName() {
        return "loginService";
    }
}
