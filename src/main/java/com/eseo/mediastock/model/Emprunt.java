package com.eseo.mediastock.model;

import com.eseo.mediastock.model.Produits.Exemplaire;

import java.time.LocalDate;

public class Emprunt {
    private int id;
    private Adherent emprunteur;
    private Exemplaire exemplaireConcerne;
    private LocalDate dateDebut;
    private LocalDate dateRetour;

    public int getId() {
        return id;
    }

    public Adherent getEmprunteur() {
        return emprunteur;
    }

    public Exemplaire getExemplaireConcerne() {
        return exemplaireConcerne;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public LocalDate getDateRetour() {
        return dateRetour;
    }

    //Méthodes
    public boolean estEnRetard(){
        return dateRetour.isBefore(LocalDate.now());
    }

}