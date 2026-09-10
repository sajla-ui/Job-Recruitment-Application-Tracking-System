package com.jobportal.shared.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility class for password hashing using BCrypt.
 * Keeps authentication modular without forcing full Spring Security yet.
 */
public class PasswordUtil {

    public static String hashPassword(String plainTextPassword) {
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt(12));
    }

    public static boolean checkPassword(String plainTextPassword, String hashedPassword) {
        if (plainTextPassword == null || hashedPassword == null) {
            return false;
        }
        try {
            return BCrypt.checkpw(plainTextPassword, hashedPassword);
        } catch (Exception e) {
            return false;
        }
    }
}
