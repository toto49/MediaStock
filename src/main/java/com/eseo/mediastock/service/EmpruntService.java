package com.eseo.mediastock.service;

import com.eseo.mediastock.dao.AdherentDAO;
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
    // Instanciation de TOUS les DAO nécessaires
    private final EmpruntDAO empruntDAO = new EmpruntDAO();
    private final ExemplaireDAO exemplaireDAO = new ExemplaireDAO();
    private final AdherentDAO adherentDAO = new AdherentDAO();

    public boolean peutEmprunter(Adherent adherent, Exemplaire exemplaire) {
        if (adherent.getNombreEmprunts() >= MAX_EMPRUNTS) return false;
        for (Emprunt emprunt : adherent.getEmpruntsEnCours()) {
            if (emprunt.estEnRetard()) return false;
        }
        if (!exemplaire.estDispo()) return false;
        return exemplaire.estBonEtat();
    }

    public void enregistrerEmprunt(Adherent adherent, Exemplaire exemplaire) throws SQLException {
        if (peutEmprunter(adherent, exemplaire)) {
            Emprunt emprunt = new Emprunt();
            emprunt.setEmprunteur(adherent);
            emprunt.setExemplaire(exemplaire);
            adherent.ajouterEmprunt(emprunt);
            exemplaire.setStatusDispo(EnumDispo.EMPRUNTE);

            empruntDAO.createEmprunt(adherent, exemplaire);
            exemplaireDAO.updateExemplaire(exemplaire);
        } else {
            throw new RuntimeException("L'adhérent ne remplit pas les conditions ou l'exemplaire est indisponible.");
        }
    }

    public void enregistrerRetour(Adherent adherent, Emprunt emprunt) throws SQLException {
        if (adherent.getEmpruntsEnCours().contains(emprunt)) {
            adherent.cloturerEmprunt(emprunt);
            emprunt.setStatusDispo(EnumDispo.RENDU);
            emprunt.getExemplaire().setStatusDispo(EnumDispo.DISPONIBLE);

            empruntDAO.saveRetour(emprunt);
            exemplaireDAO.updateExemplaire(emprunt.getExemplaire());
        }
    }

    public List<Emprunt> getEmpruntsEnRetards() throws SQLException {
        return empruntDAO.trouverRetards(LocalDate.now());
    }

    // --- Les passerelles pour le contrôleur graphique ---

    public void emprunterViaCode(String idAdherent, String codeExemplaire) throws Exception {
        Adherent adherent = adherentDAO.GetByID(idAdherent);
        Exemplaire exemplaire = exemplaireDAO.findByCodeBarre(codeExemplaire);

        if (adherent == null) throw new Exception("Numéro d'adhérent introuvable.");
        if (exemplaire == null) throw new Exception("Code exemplaire introuvable.");

        enregistrerEmprunt(adherent, exemplaire);
    }

    public void rendreViaCode(String codeExemplaire) throws Exception {
        Emprunt empruntEnCours = empruntDAO.trouverEmpruntEnCours(codeExemplaire);

        if (empruntEnCours == null) {
            throw new Exception("Aucun emprunt en cours trouvé pour cet exemplaire.");
        }

        Adherent emprunteur = empruntEnCours.getEmprunteur();
        enregistrerRetour(emprunteur, empruntEnCours);
    }
}