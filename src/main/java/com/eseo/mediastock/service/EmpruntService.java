package com.eseo.mediastock.service;

import com.eseo.mediastock.model.Adherent;
import com.eseo.mediastock.model.Emprunt;
import com.eseo.mediastock.model.Enum.EnumDispo;
import com.eseo.mediastock.model.Produits.Exemplaire;

import java.time.LocalDate;

public class EmpruntService {

    private static int MAX_EMPRUNTS = 5 ;

    public boolean peutEmprunter(Adherent adherent, Exemplaire exemplaire){
        if (adherent.getNombreEmpunts() == MAX_EMPRUNTS){return false;}
        for (Emprunt emprunt : adherent.getEmpruntsEnCours()){if (emprunt.estEnRetard()){return false;}}
        if (!exemplaire.estDispo()){return false;}
        if (!exemplaire.estBonEtat()){return false;}
        return true;
    }

    public void enregistrerEmprunt(Adherent adherent, Exemplaire exemplaire){
        if (peutEmprunter(adherent, exemplaire)){
            Emprunt emprunt = new Emprunt();
            emprunt.setEmprunteur(adherent);
            emprunt.setExemplaire(exemplaire);
            exemplaire.setStatusDispo(EnumDispo.EMPRUNTE);
            adherent.getEmpruntsEnCours().add(emprunt);
            // TODO : Ajouter avec DAO
        }
    }


}
