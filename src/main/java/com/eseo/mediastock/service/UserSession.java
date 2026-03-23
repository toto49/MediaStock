package com.eseo.mediastock.service;

import com.eseo.mediastock.model.Admin;

/**
 * Classe gérant la session utilisateur (Pattern Singleton).
 * <p>
 * Permet de stocker et de conserver l'instance de l'administrateur connecté
 * tout au long du cycle de vie de l'application. Elle est consultable par n'importe
 * quel contrôleur pour adapter l'interface (affichage du nom, vérification des permissions).
 * </p>
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