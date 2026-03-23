package com.eseo.mediastock.model.Produits;

import com.eseo.mediastock.model.Exemplaire;

import java.util.List;

/**
 * The type Dvd.
 */
public class DVD extends Produit {
    private String realisateur;
    private int dureeMinutes;
    private List<String> audioLangues;
    private List<String> sousTitres;

    /**
     * Instantiates a new Dvd.
     */
// Constructeurs
    public DVD() {
    }

    /**
     * Instantiates a new Dvd.
     *
     * @param id           the id
     * @param titre        the titre
     * @param description  the description
     * @param editeur      the editeur
     * @param anneeSortie  the annee sortie
     * @param exemplaires  the exemplaires
     * @param realisateur  the realisateur
     * @param dureeMinutes the duree minutes
     * @param audioLangues the audio langues
     * @param sousTitres   the sous titres
     */
    public DVD(int id, String titre, String description, String editeur, int anneeSortie, List<Exemplaire> exemplaires, String realisateur, int dureeMinutes, List<String> audioLangues, List<String> sousTitres) {
        super(id, titre, description, editeur, anneeSortie, exemplaires);
        this.realisateur = realisateur;
        this.dureeMinutes = dureeMinutes;
        this.audioLangues = audioLangues;
        this.sousTitres = sousTitres;
    }

    /**
     * Gets realisateur.
     *
     * @return the realisateur
     */
    public String getRealisateur() {
        return realisateur;
    }

    /**
     * Sets realisateur.
     *
     * @param realisateur the realisateur
     */
    public void setRealisateur(String realisateur) {
        this.realisateur = realisateur;
    }

    /**
     * Gets duree minutes.
     *
     * @return the duree minutes
     */
    public int getDureeMinutes() {
        return dureeMinutes;
    }

    /**
     * Sets duree minutes.
     *
     * @param dureeMinutes the duree minutes
     */
    public void setDureeMinutes(int dureeMinutes) {
        this.dureeMinutes = dureeMinutes;
    }

    /**
     * Gets audio langues.
     *
     * @return the audio langues
     */
    public List<String> getAudioLangues() {
        return audioLangues;
    }

    /**
     * Sets audio langues.
     *
     * @param audioLangues the audio langues
     */
    public void setAudioLangues(List<String> audioLangues) {
        this.audioLangues = audioLangues;
    }

    /**
     * Gets sous titres.
     *
     * @return the sous titres
     */
    public List<String> getSousTitres() {
        return sousTitres;
    }

    /**
     * Sets sous titres.
     *
     * @param sousTitres the sous titres
     */
    public void setSousTitres(List<String> sousTitres) {
        this.sousTitres = sousTitres;
    }
}
