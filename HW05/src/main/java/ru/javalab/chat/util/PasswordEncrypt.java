package ru.javalab.chat.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncrypt {
    private static final String salt = "javalab";

    public static boolean checkPassword(String password, String hash) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(salt + password, hash);
    }

    public static String generateHash(String str) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(salt + str);
    }
}
