package com.eseo.mediastock.model;

import com.eseo.mediastock.model.Produits.Exemplaire;

import java.time.LocalDate;

public class Emprunt {
    private final int id;
    private Adherent emprunteur;
    private Exemplaire exemplaireConcerne;
    private LocalDate dateDebut;
    private LocalDate dateRetour;

    public Emprunt(int id, Adherent emprunteur, Exemplaire exemplaireConcerne) {
        this.id = id;
        this.emprunteur = emprunteur;
        this.exemplaireConcerne = exemplaireConcerne;
        this.dateDebut = LocalDate.now();
        this.dateRetour = LocalDate.now().plusMonths(2);
    }

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