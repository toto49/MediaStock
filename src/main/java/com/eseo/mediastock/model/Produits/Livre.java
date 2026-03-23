package com.eseo.mediastock.model.Produits;

import com.eseo.mediastock.model.Exemplaire;

import java.util.List;

/**
 * The type Livre.
 */
public class Livre extends Produit {

    // Déclaration de variables
    private String isbn;
    private String auteur;
    private int nbPages;
    private String format;

    /**
     * Instantiates a new Livre.
     */
// Constructeurs
    public Livre(){
    }

    /**
     * Instantiates a new Livre.
     *
     * @param id          the id
     * @param titre       the titre
     * @param description the description
     * @param editeur     the editeur
     * @param anneeSortie the annee sortie
     * @param exemplaires the exemplaires
     * @param isbn        the isbn
     * @param auteur      the auteur
     * @param nbPages     the nb pages
     * @param format      the format
     */
    public Livre(int id, String titre, String description, String editeur, int anneeSortie, List<Exemplaire> exemplaires, String isbn, String auteur, int nbPages, String format) {
        super(id, titre, description, editeur, anneeSortie, exemplaires);
        this.isbn = isbn;
        this.auteur = auteur;
        this.nbPages = nbPages;
        this.format = format;
    }

    /**
     * Gets isbn.
     *
     * @return the isbn
     */
// Getters/Setters
    public String getIsbn() {
        return isbn;
    }

    /**
     * Sets isbn.
     *
     * @param isbn the isbn
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Gets auteur.
     *
     * @return the auteur
     */
    public String getAuteur() {
        return auteur;
    }

    /**
     * Sets auteur.
     *
     * @param auteur the auteur
     */
    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    /**
     * Gets nb pages.
     *
     * @return the nb pages
     */
    public int getNbPages() {
        return nbPages;
    }

    /**
     * Sets nb pages.
     *
     * @param nbPages the nb pages
     */
    public void setNbPages(int nbPages) {
        this.nbPages = nbPages;
    }

    /**
     * Gets format.
     *
     * @return the format
     */
    public String getFormat() {
        return format;
    }

    /**
     * Sets format.
     *
     * @param format the format
     */
    public void setFormat(String format) {
        this.format = format;
    }
}
