package com.garnbutik.security;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    public static String hashPassword(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        if (null == hashedPassword || !hashedPassword.startsWith("$2a$")) {
            throw new RuntimeException("Hashed password is invalid");
        }
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }
}
