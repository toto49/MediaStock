package com.eseo.mediastock.service;

import com.eseo.mediastock.model.Admin;

public class UserSession {
    private static Admin adminConnecte;

    public static Admin getAdminConnecte() {
        return adminConnecte;
    }

    public static void setAdminConnecte(Admin admin) {
        adminConnecte = admin;
    }

    // Utile pour la déconnexion plus tard
    public static void cleanUserSession() {
        adminConnecte = null;
    }
}