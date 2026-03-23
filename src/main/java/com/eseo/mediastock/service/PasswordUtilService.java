package com.eseo.mediastock.service;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtilService {

    public static String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    public static boolean verify(String hash, String password) {
        return BCrypt.checkpw(password, hash);
    }
}


