package ru.javalab.chat.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

public class TokenHandler {
    public static String generateToken(int userId, boolean isAdmin) {
        Algorithm algorithm = Algorithm.HMAC256("QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm");
        String token = JWT.create()
                .withSubject(String.valueOf(userId))
                .withClaim("role", isAdmin)
                .sign(algorithm);
        return token;
    }

    public static boolean verifyToken(String token, boolean isMustBeAdmin) {
        Algorithm algorithm = Algorithm.HMAC256("QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm");
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            if (!isMustBeAdmin) return true;
            if (jwt.getClaim("role").asBoolean()) {
                return true;
            } else {
                return false;
            }
        } catch (JWTVerificationException exception) {
            return false;
        }
    }
}
