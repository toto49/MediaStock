package com.eseo.mediastock.service;

import com.eseo.mediastock.dao.AdminDAO;
import com.eseo.mediastock.model.Admin;

import java.sql.SQLException;
import java.util.List;

public class AdminService {

    private static final AdminDAO adminDAO = new AdminDAO();


    /**
     * Authentifie un administrateur
     */
    public Admin login(String email, String password) {
        try {
            return adminDAO.authenticate(email, password);
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * Crée un nouvel administrateur
     */
    public static void creerAdmin(String email, String mdp, String nom, String prenom, String numTel)
            throws SQLException, IllegalArgumentException {

        // Validation
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("L'email ne peut pas être vide");
        }
        if (mdp == null || mdp.length() < 6) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 6 caractères");
        }
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide");
        }
        if (prenom == null || prenom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom ne peut pas être vide");
        }
        if (numTel == null || numTel.length() < 10) {
            throw new IllegalArgumentException("Le numéro de téléphone doit être valide");
        }

        // Vérifie si l'email existe déjà
        Admin existant = adminDAO.findByEmail(email);
        if (existant != null) {
            throw new IllegalArgumentException("Un admin avec cet email existe déjà: " + email);
        }

        // Création de l'admin
        Admin nouvelAdmin = new Admin(0, nom.trim(), prenom.trim(), email.trim(), numTel, mdp);
        nouvelAdmin.setPlainPassword(mdp);//pour definir le mot de passe avant hashage
        adminDAO.create(nouvelAdmin);
    }

    /**
     * Récupère un admin par son ID
     */
    public Admin getAdminParId(int id) {
        try {
            List<Admin> admins = adminDAO.findAll();
            for (Admin a : admins) {
                if (a.getId() == id) {
                    return a;
                }
            }
            return null;
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * Met à jour les informations d'un admin
     */
    public void mettreAJourAdmin(int id, String nouvelEmail, String nouveauMdp,
                                 String nouveauNom, String nouveauPrenom, String nouveauNumTel)
            throws SQLException, IllegalArgumentException {

        Admin admin = getAdminParId(id);
        if (admin == null) {
            throw new IllegalArgumentException("Aucun admin trouvé avec l'ID: " + id);
        }

        // Mise à jour des champs
        admin.setEmail(nouvelEmail);
        admin.setPlainPassword(nouveauMdp);
        admin.setNom(nouveauNom);
        admin.setPrenom(nouveauPrenom);
        admin.setNumTel(nouveauNumTel);

        adminDAO.update(admin);
    }

    /**
     * Change le mot de passe d'un admin
     */
    public void changerMotDePasse(int id, String ancienMdp, String nouveauMdp)
            throws SQLException, IllegalArgumentException {

        boolean success = adminDAO.changePassword(id, ancienMdp, nouveauMdp);
        if (!success) {
            throw new IllegalArgumentException("Ancien mot de passe incorrect");
        }
    }

    /**
     * Supprime un administrateur
     */
    public void supprimerAdmin(int id) throws SQLException, IllegalArgumentException {

        // Récupérer l'admin pour vérifier s'il existe
        Admin admin = getAdminParId(id);
        if (admin == null) {
            throw new IllegalArgumentException("Aucun admin trouvé avec l'ID: " + id);
        }
        adminDAO.delete(id);
    }
}