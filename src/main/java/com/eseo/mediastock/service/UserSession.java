package com.eseo.mediastock.service;

import com.eseo.mediastock.model.Admin;

/**
 * The type User session.
 */
public class UserSession {
    private static Admin adminConnecte;

    /**
     * Gets admin connecte.
     *
     * @return the admin connecte
     */
    public static Admin getAdminConnecte() {
        return adminConnecte;
    }

    /**
     * Sets admin connecte.
     *
     * @param admin the admin
     */
    public static void setAdminConnecte(Admin admin) {
        adminConnecte = admin;
    }

    /**
     * Clean user session.
     */
// Utile pour la déconnexion plus tard
    public static void cleanUserSession() {
        adminConnecte = null;
    }
}