package com.eseo.mediastock.service;

import com.eseo.mediastock.dao.EmpruntDAO;
import com.eseo.mediastock.dao.ExemplaireDAO;
import com.eseo.mediastock.model.Adherent;
import com.eseo.mediastock.model.Emprunt;
import com.eseo.mediastock.model.Enum.EnumDispo;
import com.eseo.mediastock.model.Exemplaire;

import java.sql.SQLException;
import java.util.List;

public class EmpruntService {

    private EmpruntDAO empruntDAO = new EmpruntDAO();
    private ExemplaireDAO exemplaireDAO = new ExemplaireDAO();
    private static final int MAX_EMPRUNTS = 5 ;

    public boolean peutEmprunter(Adherent adherent, Exemplaire exemplaire){
        if (adherent.getNombreEmprunts() >= MAX_EMPRUNTS){return false;}
        for (Emprunt emprunt : adherent.getEmpruntsEnCours()){if (emprunt.estEnRetard()){return false;}}
        if (!exemplaire.estDispo()){return false;}
        return exemplaire.estBonEtat();
    }

    public void enregistrerEmprunt(Adherent adherent, Exemplaire exemplaire) throws SQLException {
        if (peutEmprunter(adherent, exemplaire)){
            Emprunt emprunt = new Emprunt();
            emprunt.setEmprunteur(adherent);
            emprunt.setExemplaire(exemplaire);
            adherent.ajouterEmprunt(emprunt);
            exemplaire.setStatusDispo(EnumDispo.EMPRUNTE);

            try{
                empruntDAO.createEmprunt(adherent,exemplaire);
                exemplaireDAO.updateExemplaire(exemplaire);
            }
            catch (SQLException e){
                throw new RuntimeException(e);
            }
        }
    }

    public void enregistrerRetour(Adherent adherent, Emprunt emprunt){
        if (adherent.getEmpruntsEnCours().contains(emprunt)){
            adherent.cloturerEmprunt(emprunt);
            emprunt.setStatusDispo(EnumDispo.RENDU);
            emprunt.getExemplaire().setStatusDispo(EnumDispo.DISPONIBLE);

            try {
                empruntDAO.saveRetour(emprunt);
                exemplaireDAO.updateExemplaire(emprunt.getExemplaire());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public List<Emprunt> getEmpruntsEnRetards(){
        // TODO : empruntDao.trouverRetards(LocalDate.now()); stp morgiane (fait juste la fonction dans tes fichiers je m'occupe de l'appeler)
        return List.of();
    }

}
