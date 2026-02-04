package com.eseo.mediastock.model.Produits;

import java.util.List;

public class Livre extends Produit {

    // Déclaration de variables
    private String isbn;
    private String auteur;
    private int nbPages;
    private String format;

    // Constructeur
    public Livre(int id, String titre, String description, String editeur, int anneeSortie, List<Exemplaire> exemplaires, String isbn, String auteur, int nbPages, String format) {
        super(id, titre, description, editeur, anneeSortie, exemplaires);
        this.isbn = isbn;
        this.auteur = auteur;
        this.nbPages = nbPages;
        this.format = format;
    }

    // Getters/Setters
    public String getIsbn() {
        return isbn;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public int getNbPages() {
        return nbPages;
    }

    public void setNbPages(int nbPages) {
        this.nbPages = nbPages;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    // Methodes

}
