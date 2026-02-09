package com.eseo.mediastock.service;

import com.eseo.mediastock.model.Adherent;
import com.eseo.mediastock.dao.AdherentDAO;
import java.sql.SQLException;
import java.time.LocalDate;

public class AdherentService {

    private AdherentDAO adherentDAO;
    //constructeur
    public AdherentService(){
        this.adherentDAO = new AdherentDAO();
    }

    public int ajouterNbrAdherent(){
        Adherent.nbrAdherent += 1;
        return Adherent.nbrAdherent;
    }

    public String createNumAdherent(){
        int annee = LocalDate.now().getYear();
        int abonnes = ajouterNbrAdherent();
        return String.format("ADH-%d-%03d",annee,abonnes);
    }

    public void inscrireAdherent(String nom, String prenom, String email, String telephone){
        //crée un adherent
        Adherent nouvelAdherent = new Adherent(0, nom, prenom, email, telephone);
        // utiliser le dao pour sauvegarder dans la db
        try {
            adherentDAO.create (nouvelAdherent);
            System.out.println("Adhérent créé avec succès !");
            System.out.println("ID attribué: " + nouvelAdherent.getId());

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'inscription: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
