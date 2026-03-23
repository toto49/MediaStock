package com.eseo.mediastock.service;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Service utilitaire dédié à la sécurité et au cryptage.
 * <p>
 * Fournit des algorithmes de hachage (par exemple BCrypt ou SHA) pour chiffrer
 * les mots de passe avant insertion en base, et des méthodes pour vérifier
 * un mot de passe en clair par rapport à un hash stocké.
 * </p>
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


