package com.eseo.mediastock.model;

import com.eseo.mediastock.model.Enum.EnumDispo;
import com.eseo.mediastock.model.Produits.Exemplaire;

import java.time.LocalDate;

public class Emprunt {
    private int id;
    private Adherent emprunteur;
    private Exemplaire exemplaire;
    private LocalDate dateDebut;
    private LocalDate dateRetour;
    private EnumDispo statusDispo;

    public Emprunt(){
        this.dateDebut = LocalDate.now();
        this.dateRetour = LocalDate.now().plusMonths(2);
        this.statusDispo = EnumDispo.EMPRUNTE;
    }

    public Emprunt(int id, Adherent emprunteur, Exemplaire exemplaire,LocalDate dateDebut,LocalDate dateRetour) {
        this.id = id;
        this.emprunteur = emprunteur;
        this.exemplaire = exemplaire;
        this.dateDebut = dateDebut;
        this.dateRetour = dateRetour;
        this.statusDispo = EnumDispo.EMPRUNTE;
    }

    public void setEmprunteur(Adherent emprunteur) {
        this.emprunteur = emprunteur;
    }

    public void setExemplaire(Exemplaire exemplaire) {
        this.exemplaire = exemplaire;
    }

    public int getId() {return id;}

    public Adherent getEmprunteur() {return emprunteur;}

    public Exemplaire getExemplaire() {return exemplaire;}

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public LocalDate getDateRetour() {
        return dateRetour;
    }

    public  EnumDispo getStatusDispo() {return statusDispo;}

    public void setStatusDispo(EnumDispo statusDispo) {
        this.statusDispo = statusDispo;
    }

    //Méthodes
    public boolean estEnRetard(){
        return dateRetour.isBefore(LocalDate.now()) && this.statusDispo == EnumDispo.EMPRUNTE;
    }

}