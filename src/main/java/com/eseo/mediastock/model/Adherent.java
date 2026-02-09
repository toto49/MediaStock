package com.eseo.mediastock.model;

import com.eseo.mediastock.model.Produits.Exemplaire;

import java.util.ArrayList;
import java.util.List;

public class Adherent {
    //
    public static int nbrAdherent;
    // Déclarations Variables
    private int id;
    private String nom;
    private String prenom;
    private String emailContact;
    private String numTel;
    private List<Emprunt> empruntsEnCours;
    private List<Emprunt> historique;

    public Adherent(int id, String nom, String prenom, String emailContact, String numTel) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.emailContact = emailContact;
        this.numTel = numTel;
        this.empruntsEnCours = new ArrayList<>();
        this.historique = new ArrayList<>();
    }

    // Getter/Setter
    public int getId() {
        return id;
    }
    public void setId(int id){this.id = id;}

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

    //Methodes
    public int getNombreEmpunts(){
        return empruntsEnCours.size();
    }

}