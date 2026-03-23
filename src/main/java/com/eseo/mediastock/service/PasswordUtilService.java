package com.eseo.mediastock.service;

import org.mindrot.jbcrypt.BCrypt;

/**
 * The type Password util service.
 */
public class PasswordUtilService {

    /**
     * Hash string.
     *
     * @param password the password
     * @return the string
     */
    public static String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    /**
     * Verify boolean.
     *
     * @param hash     the hash
     * @param password the password
     * @return the boolean
     */
    public static boolean verify(String hash, String password) {
        return BCrypt.checkpw(password, hash);
    }
}


