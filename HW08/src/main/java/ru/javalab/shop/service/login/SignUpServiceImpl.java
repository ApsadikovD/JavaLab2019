package ru.javalab.shop.service.login;

import ru.javalab.di.Component;
import ru.javalab.shop.dto.UserDto;
import ru.javalab.shop.model.User;
import ru.javalab.shop.repository.user.UserRepositoryImpl;
import ru.javalab.shop.util.PasswordEncrypt;

import java.util.Optional;


public class SignUpServiceImpl implements SignUpService, Component {
    private UserRepositoryImpl userRepository;

    public SignUpServiceImpl() {
    }

    @Override
    public UserDto login(String name, String password) {
        Optional<User> user = userRepository.findByName(name);
        if (user.isPresent()) {
            if (PasswordEncrypt.checkPassword(password, user.get().getHash())) {
                return new UserDto(user.get().getId(), user.get().getEmail());
            }
        } else {
            userRepository.save(new User(name, password));
            User newUser = userRepository.findByName(name).get();
            UserDto userDto = new UserDto(newUser.getId(), newUser.getEmail());
            return userDto;
        }
        return null;
    }

    @Override
    public String getName() {
        return "signUpService";
    }
}
