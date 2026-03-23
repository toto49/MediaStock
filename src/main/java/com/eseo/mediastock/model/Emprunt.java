package com.eseo.mediastock.model;

import com.eseo.mediastock.model.Enum.EnumDispo;

import java.time.LocalDate;

public class Emprunt {
    private final LocalDate dateDebut;
    private final LocalDate dateRetour;
    private int id;
    private Adherent emprunteur;
    private Exemplaire exemplaire;
    private EnumDispo statut;

    public Emprunt() {
        this.dateDebut = LocalDate.now();
        this.dateRetour = LocalDate.now().plusMonths(2);
        this.statut = EnumDispo.EMPRUNTE;
    }

    public Emprunt(int id, Adherent emprunteur, Exemplaire exemplaire, LocalDate dateDebut, LocalDate dateRetour) {
        this.id = id;
        this.emprunteur = emprunteur;
        this.exemplaire = exemplaire;
        this.dateDebut = dateDebut;
        this.dateRetour = dateRetour;
        this.statut = EnumDispo.EMPRUNTE;
    }

    public int getId() {
        return id;
    }

    public Adherent getEmprunteur() {
        return emprunteur;
    }

    public void setEmprunteur(Adherent emprunteur) {
        this.emprunteur = emprunteur;
    }

    public Exemplaire getExemplaire() {
        return exemplaire;
    }

    public void setExemplaire(Exemplaire exemplaire) {
        this.exemplaire = exemplaire;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public LocalDate getDateRetour() {
        return dateRetour;
    }

    public EnumDispo getStatusDispo() {
        return statut;
    }

    public void setStatusDispo(EnumDispo statusDispo) {
        this.statut = statusDispo;
    }

    //Méthodes
    public boolean estEnRetard() {
        return dateRetour.isBefore(LocalDate.now()) && this.statut == EnumDispo.EMPRUNTE;
    }

}