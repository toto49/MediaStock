package com.eseo.mediastock.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant un membre inscrit à la médiathèque.
 * Stocke les informations de contact et l'identifiant unique permettant
 * d'associer des emprunts et des historiques de prêt.
 */
public class Adherent {
    private final List<Emprunt> empruntsEnCours;
    private final List<Emprunt> historique;
    /**
     * The Nbr adherent.
     */
//
    public int nbrAdherent;
    // Déclarations Variables
    private String id;
    private String nom;
    private String prenom;
    private String emailContact;
    private String numTel;

    /**
     * Instantiates a new Adherent.
     *
     * @param id           the id
     * @param nom          the nom
     * @param prenom       the prenom
     * @param emailContact the email contact
     * @param numTel       the num tel
     */
    public Adherent(String id, String nom, String prenom, String emailContact, String numTel) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.emailContact = emailContact;
        this.numTel = numTel;
        this.empruntsEnCours = new ArrayList<>();
        this.historique = new ArrayList<>();

        //initialiser à 0 pour chaque nvel adhrent
        this.nbrAdherent = 0;
    }


    /**
     * Gets id.
     *
     * @return the id
     */
// Getter/Setter
    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets nom.
     *
     * @return the nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * Sets nom.
     *
     * @param nom the nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Gets prenom.
     *
     * @return the prenom
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Sets prenom.
     *
     * @param prenom the prenom
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * Gets email contact.
     *
     * @return the email contact
     */
    public String getEmailContact() {
        return emailContact;
    }

    /**
     * Sets email contact.
     *
     * @param emailContact the email contact
     */
    public void setEmailContact(String emailContact) {
        this.emailContact = emailContact;
    }

    /**
     * Gets num tel.
     *
     * @return the num tel
     */
    public String getNumTel() {
        return numTel;
    }

    /**
     * Sets num tel.
     *
     * @param numTel the num tel
     */
    public void setNumTel(String numTel) {
        this.numTel = numTel;
    }

    /**
     * Gets emprunts en cours.
     *
     * @return the emprunts en cours
     */
    public List<Emprunt> getEmpruntsEnCours() {
        return empruntsEnCours;
    }

    /**
     * Gets historique.
     *
     * @return the historique
     */
    public List<Emprunt> getHistorique() {
        return historique;
    }

    /**
     * Gets nombre emprunts.
     *
     * @return the nombre emprunts
     */
//Methodes
    public int getNombreEmprunts() {
        return empruntsEnCours.size();
    }

    /**
     * Ajouter emprunt.
     *
     * @param emprunt the emprunt
     */
    public void ajouterEmprunt(Emprunt emprunt) {
        this.empruntsEnCours.add(emprunt);
        this.historique.add(emprunt);
    }

    /**
     * Cloturer emprunt.
     *
     * @param emprunt the emprunt
     */
    public void cloturerEmprunt(Emprunt emprunt) {
        this.empruntsEnCours.remove(emprunt);
    }

}