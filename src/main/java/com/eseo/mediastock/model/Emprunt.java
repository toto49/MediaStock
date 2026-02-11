package com.eseo.mediastock.model;

import com.eseo.mediastock.model.Enum.EnumDispo;

import java.time.LocalDate;

public class Emprunt {
    private int id;
    private Adherent emprunteur;
    private Exemplaire exemplaire;
    private LocalDate dateDebut;
    private LocalDate dateRetour;
    private EnumDispo status;

    public Emprunt(){
        this.dateDebut = LocalDate.now();
        this.dateRetour = LocalDate.now().plusMonths(2);
        this.status = EnumDispo.EMPRUNTE;
    }

    public Emprunt(int id, Adherent emprunteur, Exemplaire exemplaire,LocalDate dateDebut,LocalDate dateRetour) {
        this.id = id;
        this.emprunteur = emprunteur;
        this.exemplaire = exemplaire;
        this.dateDebut = dateDebut;
        this.dateRetour = dateRetour;
        this.status = EnumDispo.EMPRUNTE;
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

    public  EnumDispo getStatusDispo() {return status;}

    public void setStatusDispo(EnumDispo statusDispo) {
        this.status = statusDispo;
    }

    //Méthodes
    public boolean estEnRetard(){
        return dateRetour.isBefore(LocalDate.now()) && this.status == EnumDispo.EMPRUNTE;
    }

}