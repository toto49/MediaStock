package com.eseo.mediastock.service;

import com.eseo.mediastock.dao.AdherentDAO;
import com.eseo.mediastock.model.Adherent;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class AdherentService {

    private final AdherentDAO adherentDAO;

    // Constructeur
    public AdherentService() {
        this.adherentDAO = new AdherentDAO();
    }

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

    public void inscrireAdherent(String nom, String prenom, String email, String telephone) throws SQLException {
        // Génération de l'ID unique
        String numAdherent = generateNumAdherent();

        // Création de l'objet Adherent
        Adherent nouvelAdherent = new Adherent(numAdherent, nom, prenom, email, telephone);

        // Insertion en base de données
        adherentDAO.createAdherent(nouvelAdherent);

        System.out.println(" Adhérent créé avec succès !");
        System.out.println("   ID attribué: " + nouvelAdherent.getId());
    }

    public void supprimerAdherent(String id) throws SQLException {
        adherentDAO.deleteAdherent(id);
        System.out.println("Adhérent " + id + " supprimé avec succès");
    }

    public void mettreAJourAdherent(Adherent adherent) throws SQLException {
        adherentDAO.updateAdherent(adherent);
        System.out.println("Adhérent " + adherent.getId() + " mis à jour avec succès");
    }

    public List<Adherent> getAllAdherents() throws SQLException {
        return adherentDAO.findAll();
    }

    public Adherent getAdherentById(String id) throws SQLException {
        for (Adherent a : adherentDAO.findAll()) {
            if (a.getId().equals(id)) {
                return a;
            }
        }
        return null;
    }
}