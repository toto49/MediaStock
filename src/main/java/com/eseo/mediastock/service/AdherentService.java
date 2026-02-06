package com.eseo.mediastock.service;

import com.eseo.mediastock.model.Adherent;

import java.time.LocalDate;

public class AdherentService {

    public int ajouterNbrAdherent(){
        Adherent.nbrAdherent += 1;
        return Adherent.nbrAdherent;
    }

    public String createNumAdherent(){
        int annee = LocalDate.now().getYear();
        int abonnes = ajouterNbrAdherent();
        return "ADH-" + annee + "-" + abonnes;
    }

    public void inscrireAdherent(String nom, String prenom, String adresse, String telephone){
        String numAdherent = createNumAdherent();

    }

}
