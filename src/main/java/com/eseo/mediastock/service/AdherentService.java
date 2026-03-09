package com.eseo.mediastock.service;

import com.eseo.mediastock.model.Adherent;
import com.eseo.mediastock.dao.AdherentDAO;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class AdherentService {

    private AdherentDAO adherentDAO;

    // Constructeur
    public AdherentService() {
        this.adherentDAO = new AdherentDAO();
    }

    /**
     * Récupère tous les adhérents
     */
    public List<Adherent> getAllAdherents() throws SQLException {
        return adherentDAO.findAll();
    }

    /**
     * Génère un numéro d'adhérent unique au format ADH-ANNEE-NUMERO
     */
    public String generateNumAdherent() {
        int annee = LocalDate.now().getYear();
        int totalAdherents = 0;

        try {
            totalAdherents = adherentDAO.countAdherents(annee);
        } catch (SQLException e) {
            System.err.println("Erreur lors du comptage : " + e.getMessage());
            e.printStackTrace();
        }

        int numeroSequence = totalAdherents + 1;
        return String.format("ADH-%d-%03d", annee, numeroSequence);
    }

    /**
     * Inscrit un nouvel adhérent
     */
    public void inscrireAdherent(String nom, String prenom, String email, String telephone) throws SQLException {
        // Génération de l'ID unique
        String numAdherent = generateNumAdherent();

        // Création de l'objet Adherent
        Adherent nouvelAdherent = new Adherent(numAdherent, nom, prenom, email, telephone);

        // Insertion en base de données
        adherentDAO.createAdherent(nouvelAdherent);

        System.out.println("✅ Adhérent créé avec succès !");
        System.out.println("   ID attribué: " + nouvelAdherent.getId());
    }

    /**
     * Supprime un adhérent par son ID
     */
    public void supprimerAdherent(String id) throws SQLException {
        adherentDAO.deleteAdherent(id);
        System.out.println("✅ Adhérent " + id + " supprimé avec succès");
    }

    /**
     * Met à jour les informations d'un adhérent
     */
    public void mettreAJourAdherent(Adherent adherent) throws SQLException {
        adherentDAO.updateAdherent(adherent);
        System.out.println("✅ Adhérent " + adherent.getId() + " mis à jour avec succès");
    }

    /**
     * Recherche un adhérent par son ID
     */
    public Adherent trouverAdherentParId(String id) throws SQLException {
        return adherentDAO.findById(id);
    }
}