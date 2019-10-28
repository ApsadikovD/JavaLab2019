package ru.javalab.chat.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncrypt {
    private static PasswordEncrypt instance;

    private PasswordEncrypt() {
    }

    public static PasswordEncrypt getInstance() {
        if (instance == null) {
            instance = new PasswordEncrypt();
        }
        return instance;
    }

    public static boolean checkPassword(String password, String hash) {
        return generateHash(password).equals(hash);
    }

    public static String generateHash(String str) {
        String hash = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(str.getBytes(StandardCharsets.UTF_8));
            hash = bytesToHex(encodedhash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash;
    }

    private static String bytesToHex(byte[] hashArr) {
        StringBuilder hexString = new StringBuilder();
        for (byte hash : hashArr) {
            String hex = Integer.toHexString(0xff & hash);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
