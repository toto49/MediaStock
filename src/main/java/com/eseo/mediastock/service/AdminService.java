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
            Admin admin = adminDAO.authenticate(email, password);
            if (admin != null) {
                System.out.println("Connexion réussie pour: " + email);
            } else {
                System.out.println("Échec de connexion pour: " + email);
            }
            return admin;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la connexion: " + e.getMessage());
            return null;
        }
    }

    /**
     * Crée un nouvel administrateur
     */
    public static void creerAdmin(String email, String mdp, String nom, String prenom, int numTel)
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
        if (numTel <= 0) {
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


        System.out.println("Admin créé avec succès - ID: " + nouvelAdmin.getId());
        System.out.println("Email: " + email);
        System.out.println("Nom: " + nom + " " + prenom);
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
            System.out.println("Aucun admin trouvé avec l'ID: " + id);
            return null;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche: " + e.getMessage());
            return null;
        }
    }

    /**
     * Met à jour les informations d'un admin
     */
    public void mettreAJourAdmin(int id, String nouvelEmail, String nouveauMdp,
                                 String nouveauNom, String nouveauPrenom, int nouveauNumTel)
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
        System.out.println("Admin mis à jour avec succès");
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

        System.out.println("Mot de passe changé avec succès");
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
        System.out.println("Admin supprimé avec succès");
    }
}