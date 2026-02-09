package com.eseo.mediastock.service;

import com.eseo.mediastock.model.Adherent;
import com.eseo.mediastock.model.Emprunt;
import com.eseo.mediastock.model.Enum.EnumDispo;
import com.eseo.mediastock.model.Produits.Exemplaire;

import java.util.List;

public class EmpruntService {

    private static final int MAX_EMPRUNTS = 5 ;

    public boolean peutEmprunter(Adherent adherent, Exemplaire exemplaire){
        if (adherent.getNombreEmprunts() >= MAX_EMPRUNTS){return false;}
        for (Emprunt emprunt : adherent.getEmpruntsEnCours()){if (emprunt.estEnRetard()){return false;}}
        if (!exemplaire.estDispo()){return false;}
        return exemplaire.estBonEtat();
    }

    public void enregistrerEmprunt(Adherent adherent, Exemplaire exemplaire){
        if (peutEmprunter(adherent, exemplaire)){
            Emprunt emprunt = new Emprunt();
            emprunt.setEmprunteur(adherent);
            emprunt.setExemplaire(exemplaire);
            adherent.ajouterEmprunt(emprunt);
            exemplaire.setStatusDispo(EnumDispo.EMPRUNTE);
            // TODO : Enregistrer dans le DAO stp Morgiane
        }
    }

    public void enregistrerRetour(Adherent adherent, Emprunt emprunt){
        if (adherent.getEmpruntsEnCours().contains(emprunt)){
            adherent.cloturerEmprunt(emprunt);
            emprunt.setStatusDispo(EnumDispo.RENDU);
            emprunt.getExemplaire().setStatusDispo(EnumDispo.DISPONIBLE);
            // TODO : Enregistrer dans le DAO stp Morgiane
        }
    }


    public List<Emprunt> getEmpruntsEnRetards(){
        // TODO : empruntDao.trouverRetards(LocalDate.now()); stp morgiane
        return List.of();
    }

}
