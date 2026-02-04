package com.eseo.mediastock.model.Produits;

import java.util.List;

public class JeuSociete extends Produit {
    private int nbJoueursMin;
    private int nbJoueursMax;
    private int ageMin;
    private int dureePartie;

    public JeuSociete(int id, String titre, String description, String editeur, int anneeSortie, List<Exemplaire> exemplaires, int nbJoueursMin, int nbJoueursMax, int ageMin, int dureePartie) {
        super(id, titre, description, editeur, anneeSortie, exemplaires);
        this.nbJoueursMin = nbJoueursMin;
        this.nbJoueursMax = nbJoueursMax;
        this.ageMin = ageMin;
        this.dureePartie = dureePartie;
    }

    public int getNbJoueursMin() {
        return nbJoueursMin;
    }

    public void setNbJoueursMin(int nbJoueursMin) {
        this.nbJoueursMin = nbJoueursMin;
    }

    public int getNbJoueursMax() {
        return nbJoueursMax;
    }

    public void setNbJoueursMax(int nbJoueursMax) {
        this.nbJoueursMax = nbJoueursMax;
    }

    public int getAgeMin() {
        return ageMin;
    }

    public void setAgeMin(int ageMin) {
        this.ageMin = ageMin;
    }

    public int getDureePartie() {
        return dureePartie;
    }

    public void setDureePartie(int dureePartie) {
        this.dureePartie = dureePartie;
    }
}
