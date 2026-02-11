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

    public int nbrAdherent(){
        Adherent.nbrAdherent += 1;
        return Adherent.nbrAdherent;
    }

    public String createNumAdherent() {
        int annee = LocalDate.now().getYear();
        int totalAdherents = 0;

        try {
            totalAdherents = adherentDAO.count(annee);
        } catch (SQLException e) {
            System.err.println("Erreur lors du comptage : " + e.getMessage());
            e.printStackTrace();
        }

        int numeroSequence = totalAdherents + 1;

        return String.format("ADH-%d-%03d", annee, numeroSequence);
    }

    public void inscrireAdherent(String nom, String prenom, String email, String telephone){
        String numAdherent = createNumAdherent();
        Adherent nouvelAdherent = new Adherent(numAdherent, nom, prenom, email, telephone);

        try {
            adherentDAO.create(nouvelAdherent);
            System.out.println("Adhérent créé avec succès !");
            System.out.println("ID attribué: " + nouvelAdherent.getId());

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'inscription: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
