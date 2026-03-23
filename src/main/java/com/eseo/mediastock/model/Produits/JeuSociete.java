package com.eseo.mediastock.model.Produits;

import com.eseo.mediastock.model.Exemplaire;

import java.util.List;

/**
 * Entité représentant un Jeu héritant de Produit
 * Classe abstraite définissant la structure de base de tout média stocké dans la base.
 * <p>
 * Factorise les attributs communs : identifiant global, titre, description, éditeur
 * et année de sortie. Elle est héritée par les entités spécifiques comme {@link Livre},
 * {@link DVD} et {@link JeuSociete}.
 * </p>
 */
public class JeuSociete extends Produit {
    private int nbJoueursMin;
    private int nbJoueursMax;
    private int ageMin;
    private int dureePartie;

    /**
     * Instantiates a new Jeu societe.
     */
// Constructeurs
    public JeuSociete() {
    }

    /**
     * Instantiates a new Jeu societe.
     *
     * @param id           the id
     * @param titre        the titre
     * @param description  the description
     * @param editeur      the editeur
     * @param anneeSortie  the annee sortie
     * @param exemplaires  the exemplaires
     * @param nbJoueursMin the nb joueurs min
     * @param nbJoueursMax the nb joueurs max
     * @param ageMin       the age min
     * @param dureePartie  the duree partie
     */
    public JeuSociete(int id, String titre, String description, String editeur, int anneeSortie, List<Exemplaire> exemplaires, int nbJoueursMin, int nbJoueursMax, int ageMin, int dureePartie) {
        super(id, titre, description, editeur, anneeSortie, exemplaires);
        this.nbJoueursMin = nbJoueursMin;
        this.nbJoueursMax = nbJoueursMax;
        this.ageMin = ageMin;
        this.dureePartie = dureePartie;
    }

    /**
     * Gets nb joueurs min.
     *
     * @return the nb joueurs min
     */
    public int getNbJoueursMin() {
        return nbJoueursMin;
    }

    /**
     * Sets nb joueurs min.
     *
     * @param nbJoueursMin the nb joueurs min
     */
    public void setNbJoueursMin(int nbJoueursMin) {
        this.nbJoueursMin = nbJoueursMin;
    }

    /**
     * Gets nb joueurs max.
     *
     * @return the nb joueurs max
     */
    public int getNbJoueursMax() {
        return nbJoueursMax;
    }

    /**
     * Sets nb joueurs max.
     *
     * @param nbJoueursMax the nb joueurs max
     */
    public void setNbJoueursMax(int nbJoueursMax) {
        this.nbJoueursMax = nbJoueursMax;
    }

    /**
     * Gets age min.
     *
     * @return the age min
     */
    public int getAgeMin() {
        return ageMin;
    }

    /**
     * Sets age min.
     *
     * @param ageMin the age min
     */
    public void setAgeMin(int ageMin) {
        this.ageMin = ageMin;
    }

    /**
     * Gets duree partie.
     *
     * @return the duree partie
     */
    public int getDureePartie() {
        return dureePartie;
    }

    /**
     * Sets duree partie.
     *
     * @param dureePartie the duree partie
     */
    public void setDureePartie(int dureePartie) {
        this.dureePartie = dureePartie;
    }
}
