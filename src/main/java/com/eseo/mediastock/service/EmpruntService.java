package com.eseo.mediastock.service;

import com.eseo.mediastock.dao.EmpruntDAO;
import com.eseo.mediastock.dao.ExemplaireDAO;
import com.eseo.mediastock.model.Adherent;
import com.eseo.mediastock.model.Emprunt;
import com.eseo.mediastock.model.Enum.EnumDispo;
import com.eseo.mediastock.model.Exemplaire;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class EmpruntService {

    private static final int MAX_EMPRUNTS = 5;
    private final EmpruntDAO empruntDAO = new EmpruntDAO();
    private final ExemplaireDAO exemplaireDAO = new ExemplaireDAO();

    public boolean peutEmprunter(Adherent adherent, Exemplaire exemplaire) {
        if (adherent.getNombreEmprunts() >= MAX_EMPRUNTS) {
            return false;
        }
        for (Emprunt emprunt : adherent.getEmpruntsEnCours()) {
            if (emprunt.estEnRetard()) {
                return false;
            }
        }
        if (!exemplaire.estDispo()) {
            return false;
        }
        return exemplaire.estBonEtat();
    }

    public void enregistrerEmprunt(Adherent adherent, Exemplaire exemplaire) throws SQLException {
        if (peutEmprunter(adherent, exemplaire)) {
            Emprunt emprunt = new Emprunt();
            emprunt.setEmprunteur(adherent);
            emprunt.setExemplaire(exemplaire);
            adherent.ajouterEmprunt(emprunt);
            exemplaire.setStatusDispo(EnumDispo.EMPRUNTE);

            try {
                empruntDAO.addEmprunt(adherent, exemplaire);
                exemplaireDAO.updateExemplaire(exemplaire);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void enregistrerRetour(Adherent adherent, Emprunt emprunt) {
        if (adherent != null && emprunt != null && adherent.getId().equals(emprunt.getEmprunteur().getId())) {

            emprunt.setStatusDispo(EnumDispo.RENDU);
            emprunt.getExemplaire().setStatusDispo(EnumDispo.DISPONIBLE);

            try {
                empruntDAO.saveRetour(emprunt);
                exemplaireDAO.updateExemplaire(emprunt.getExemplaire());
            } catch (SQLException e) {
                throw new RuntimeException("Erreur SQL lors du retour : " + e.getMessage(), e);
            }
        } else {
            throw new IllegalArgumentException("Cet emprunt ne correspond pas à cet adhérent.");
        }
    }

    public List<Emprunt> getEmpruntsFromAdherent(Adherent adherent) throws SQLException {
        return empruntDAO.getEmpruntsByAdherent(adherent);
    }

    public List<Emprunt> getEmpruntsEnRetards() throws SQLException {
        return empruntDAO.trouverRetards(LocalDate.now());
    }

    public Emprunt getEmpruntActif(String codeBarre) throws SQLException {
        return empruntDAO.trouverEmpruntEnCours(codeBarre);
    }
}
