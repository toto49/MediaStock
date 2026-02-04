package com.eseo.mediastock.model;

import com.eseo.mediastock.model.Produits.Exemplaire;

import java.util.List;

public class Adherent {
    private String numAdherent;
    private String nom;
    private String prenom;
    private String emailContact;
    private String numTel;
    private List<Emprunt> empruntsEnCours;
    private List<Emprunt> historique;

    public String getNumAdherent() {
        return numAdherent;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmailContact() {
        return emailContact;
    }

    public void setEmailContact(String emailContact) {
        this.emailContact = emailContact;
    }

    public String getNumTel() {
        return numTel;
    }

    public void setNumTel(String numTel) {
        this.numTel = numTel;
    }

    public List<Emprunt> getEmpruntsEnCours() {
        return empruntsEnCours;
    }

    public List<Emprunt> getHistorique() {
        return historique;
    }

}