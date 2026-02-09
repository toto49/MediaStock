package com.eseo.mediastock.model;

import java.util.ArrayList;
import java.util.List;

public class Adherent {
    //
    public static int nbrAdherent;
    // Déclarations Variables
    private String numAdherent;
    private String nom;
    private String prenom;
    private String emailContact;
    private String numTel;
    private List<Emprunt> empruntsEnCours;
    private List<Emprunt> historique;

    public Adherent(String numAdherent, String nom, String prenom, String emailContact, String numTel) {
        this.numAdherent = numAdherent;
        this.nom = nom;
        this.prenom = prenom;
        this.emailContact = emailContact;
        this.numTel = numTel;
        this.empruntsEnCours = new ArrayList<>();
        this.historique = new ArrayList<>();
    }

    // Getter/Setter
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

    //Methodes
    public int getNombreEmprunts(){
        return empruntsEnCours.size();
    }

    public void ajouterEmprunt(Emprunt emprunt) {
        this.empruntsEnCours.add(emprunt);
        this.historique.add(emprunt);
    }

    public void cloturerEmprunt(Emprunt emprunt) {
        this.empruntsEnCours.remove(emprunt);
    }

}