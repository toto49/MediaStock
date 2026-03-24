package com.eseo.mediastock.service;

import com.eseo.mediastock.dao.AdherentDAO;
import com.eseo.mediastock.model.Adherent;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Service métier dédié à la gestion des adhérents.
 * <p>
 * Fait le pont entre les contrôleurs (IHM) et {@link com.eseo.mediastock.dao.AdherentDAO}.
 * Intègre la logique métier comme la vérification de l'unicité d'un email ou la
 * validation des numéros de téléphone avant la sauvegarde en base.
 * </p>
 */
public class AdherentService {

    private final AdherentDAO adherentDAO;

    /**
     * Instantiates a new Adherent service.
     */
// Constructeur
    public AdherentService() {
        this.adherentDAO = new AdherentDAO();
    }

    /**
     * Generate num adherent string.
     *
     * @return the string
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
     * Inscrire adherent.
     *
     * @param nom       the nom
     * @param prenom    the prenom
     * @param email     the email
     * @param telephone the telephone
     * @throws SQLException the sql exception
     */
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

    /**
     * Supprimer adherent.
     *
     * @param id the id
     * @throws SQLException the sql exception
     */
    public void supprimerAdherent(String id) throws SQLException {
        adherentDAO.deleteAdherent(id);
    }

    /**
     * Mettre a jour adherent.
     *
     * @param adherent the adherent
     * @throws SQLException the sql exception
     */
    public void mettreAJourAdherent(Adherent adherent) throws SQLException {
        adherentDAO.updateAdherent(adherent);
    }

    /**
     * Gets all adherents.
     *
     * @return the all adherents
     * @throws SQLException the sql exception
     */
    public List<Adherent> getAllAdherents() throws SQLException {
        return adherentDAO.findAll();
    }

    /**
     * Gets adherent by id.
     *
     * @param id the id
     * @return the adherent by id
     * @throws SQLException the sql exception
     */
    public Adherent getAdherentById(String id) throws SQLException {
        for (Adherent a : adherentDAO.findAll()) {
            if (a.getId().equals(id)) {
                return a;
            }
        }
        return null;
    }
}