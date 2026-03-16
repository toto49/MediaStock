package com.eseo.mediastock.service;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtilService {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String hash(String password) {
        return encoder.encode(password);
    }

    public static boolean verify(String hash, String password) {
        return encoder.matches(password, hash);
    }
}

