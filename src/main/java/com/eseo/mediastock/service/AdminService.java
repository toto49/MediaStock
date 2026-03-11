package com.eseo.mediastock.service;

import com.eseo.mediastock.dao.AdminDAO;
import com.eseo.mediastock.model.Admin;
import java.sql.SQLException;
import java.util.List;

/**
 * Service pour la gestion des administrateurs
 */
public class AdminService {

    private AdminDAO adminDAO;

    /**
     * Constructeur = Initialise le DAO
     */
    public AdminService() {
        this.adminDAO = new AdminDAO();
    }

    /**
     * Authentifie un administrateur
     * @param email l'email de l'admin
     * @param password le mot de passe
     * @return l'admin connecté ou null si échec
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
     * @param email l'email
     * @param mdp le mot de passe
     * @return true si création réussie
     */
    public boolean creerAdmin(String email, String mdp) {
        try {
            // Vérifie si l'email existe déjà
            Admin existant = adminDAO.findByEmail(email);
            if (existant != null) {
                System.out.println("Un admin avec cet email existe déjà");
                return false;
            }

            Admin nouvelAdmin = new Admin(0, email, null);
            nouvelAdmin.setPlainPassword(mdp);
            adminDAO.create(nouvelAdmin);

            System.out.println("Admin créé avec succès - ID: " + nouvelAdmin.getId());
            return true;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création: " + e.getMessage());
            return false;
        }
    }

    /**
     * Récupère un admin par son ID
     * @param id l'ID de l'admin
     * @return l'admin ou null si non trouvé
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
     * Récupère un admin par son email
     * @param email l'email de l'admin
     * @return l'admin ou null si non trouvé
     */
    public Admin getAdminParEmail(String email) {
        try {
            return adminDAO.findByEmail(email);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche: " + e.getMessage());
            return null;
        }
    }

    /**
     * Met à jour les informations d'un admin
     * @param id l'ID de l'admin
     * @param nouvelEmail le nouvel email
     * @param nouveauMdp le nouveau mot de passe
     * @return true si mise à jour réussie
     */
    public boolean mettreAJourAdmin(int id, String nouvelEmail, String nouveauMdp) {
        try {
            Admin admin = getAdminParId(id);
            if (admin == null) {
                return false;
            }

            admin.setEmail(nouvelEmail);
            admin.setPlainPassword(nouveauMdp);
            adminDAO.update(admin);

            System.out.println("Admin mis à jour avec succès");
            return true;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour: " + e.getMessage());
            return false;
        }
    }

    /**
     * Change le mot de passe d'un admin
     * @param id l'ID de l'admin
     * @param ancienMdp l'ancien mot de passe
     * @param nouveauMdp le nouveau mot de passe
     * @return true si changement réussi
     */
    public boolean changerMotDePasse(int id, String ancienMdp, String nouveauMdp) {
        try {
            boolean success = adminDAO.changePassword(id, ancienMdp, nouveauMdp);
            if (success) {
                System.out.println("Mot de passe changé avec succès");
            } else {
                System.out.println("Ancien mot de passe incorrect");
            }
            return success;
        } catch (SQLException e) {
            System.err.println("Erreur lors du changement: " + e.getMessage());
            return false;
        }
    }

    /**
     * Supprime un administrateur
     * @param id l'ID de l'admin à supprimer
     * @return true si suppression réussie
     */
    public boolean supprimerAdmin(int id) {
        try {
            adminDAO.delete(id);
            System.out.println("Admin supprimé avec succès");
            return true;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression: " + e.getMessage());
            return false;
        }
    }

    /**
     * Liste tous les administrateurs
     * @return liste des admins
     */
    public List<Admin> listerTousLesAdmins() {
        try {
            return adminDAO.findAll();
        } catch (SQLException e) {
            System.err.println("Erreur lors du listage: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Compte le nombre total d'administrateurs
     * @return le nombre d'admins
     */
    public int compterAdmins() {
        try {
            return adminDAO.findAll().size();
        } catch (SQLException e) {
            System.err.println("Erreur lors du comptage: " + e.getMessage());
            return 0;
        }
    }
}