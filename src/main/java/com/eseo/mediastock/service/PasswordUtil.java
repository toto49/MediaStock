package com.eseo.mediastock.service;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class PasswordUtil {

    private static final Argon2 argon2 =
            Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);

    public static String hash(String password) {
        return argon2.hash(3,65536,1,password);
    }

    public static boolean verify(String hash, String password){
        return argon2.verify(hash,password);
    }
}
