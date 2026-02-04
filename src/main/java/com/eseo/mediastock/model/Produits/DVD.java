package com.eseo.mediastock.model.Produits;

import java.util.List;

public class DVD extends Produit {
    private String realisateur;
    private int dureeMinutes;
    private List<String> audioLangues;
    private List<String> sousTitres;

    public DVD(int id, String titre, String description, String editeur, int anneeSortie, List<Exemplaire> exemplaires, String realisateur, int dureeMinutes, List<String> audioLangues, List<String> sousTitres) {
        super(id, titre, description, editeur, anneeSortie, exemplaires);
        this.realisateur = realisateur;
        this.dureeMinutes = dureeMinutes;
        this.audioLangues = audioLangues;
        this.sousTitres = sousTitres;
    }

    public String getRealisateur() {
        return realisateur;
    }

    public void setRealisateur(String realisateur) {
        this.realisateur = realisateur;
    }

    public int getDureeMinutes() {
        return dureeMinutes;
    }

    public void setDureeMinutes(int dureeMinutes) {
        this.dureeMinutes = dureeMinutes;
    }

    public List<String> getAudioLangues() {
        return audioLangues;
    }

    public void setAudioLangues(List<String> audioLangues) {
        this.audioLangues = audioLangues;
    }

    public List<String> getSousTitres() {
        return sousTitres;
    }

    public void setSousTitres(List<String> sousTitres) {
        this.sousTitres = sousTitres;
    }
}
