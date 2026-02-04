package com.eseo.mediastock.model;

import com.eseo.mediastock.model.Produits.Exemplaire;

import java.util.Date;

public class Emprunt {
    private int id;
    private Adherent emprunteur;
    private Exemplaire exemplaireConcerne;
    private Date dateDebut;
    private Date dateRetour;

    public int getId() {
        return id;
    }

    public Adherent getEmprunteur() {
        return emprunteur;
    }

    public Exemplaire getExemplaireConcerne() {
        return exemplaireConcerne;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public Date getDateRetour() {
        return dateRetour;
    }

}