package com.eseo.mediastock.model.Produits;

import com.eseo.mediastock.model.Exemplaire;

import java.util.List;

/**
 * Classe de base abstraite représentant une référence du catalogue.
 * Regroupe les propriétés communes (titre, éditeur, année) et définit
 * la structure polymorphique utilisée par les Livres, DVD et Jeux de Société.
 */
public abstract class Produit {
    // Variables de classes
    private static int NbrReference;

    /**
     * The Id.
     */
    // Variables d'instance accessibles aux enfants
    protected int id;

    /**
     * The Titre.
     */
    protected String titre;

    /**
     * The Description.
     */
    protected String description;

    /**
     * The Editeur.
     */
    protected String editeur;

    /**
     * The Annee sortie.
     */
    protected int anneeSortie;

    // Listes des exemplaires
    private List<Exemplaire> exemplaires;

    // Constructeurs

    /**
     * Instantiates a new Produit.
     */
    public Produit() {
    }

    /**
     * Instantiates a new Produit.
     *
     * @param id          the id
     * @param titre       the titre
     * @param description the description
     * @param editeur     the editeur
     * @param anneeSortie the annee sortie
     * @param exemplaires the exemplaires
     */
    public Produit(int id, String titre, String description, String editeur, int anneeSortie, List<Exemplaire> exemplaires) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.editeur = editeur;
        this.anneeSortie = anneeSortie;
        this.exemplaires = exemplaires;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets titre.
     *
     * @return the titre
     */
    public String getTitre() {
        return titre;
    }

    /**
     * Sets titre.
     *
     * @param titre the titre
     */
    public void setTitre(String titre) {
        this.titre = titre;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets editeur.
     *
     * @return the editeur
     */
    public String getEditeur() {
        return editeur;
    }

    /**
     * Sets editeur.
     *
     * @param editeur the editeur
     */
    public void setEditeur(String editeur) {
        this.editeur = editeur;
    }

    /**
     * Gets annee sortie.
     *
     * @return the annee sortie
     */
    public int getAnneeSortie() {
        return anneeSortie;
    }

    /**
     * Sets annee sortie.
     *
     * @param anneeSortie the annee sortie
     */
    public void setAnneeSortie(int anneeSortie) {
        this.anneeSortie = anneeSortie;
    }

    /**
     * Gets exemplaires.
     *
     * @return the exemplaires
     */
    public List<Exemplaire> getExemplaires() {
        return exemplaires;
    }

    /**
     * Sets exemplaires.
     *
     * @param exemplaires the exemplaires
     */
    public void setExemplaires(List<Exemplaire> exemplaires) {
        this.exemplaires = exemplaires;
    }

    // Méthodes de classe

}