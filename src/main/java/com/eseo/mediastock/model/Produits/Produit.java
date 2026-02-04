package com.eseo.mediastock.model.Produits;

import java.util.List;

abstract class  Produit {
    // Variables communes accessibles aux enfants
    protected int id;
    protected String titre;
    protected String description;
    protected String editeur;
    protected int anneeSortie;
    // Listes des exemplaires
    private List<Exemplaire> exemplaires;

    public Produit(int id, String titre, String description, String editeur, int anneeSortie, List<Exemplaire> exemplaires) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.editeur = editeur;
        this.anneeSortie = anneeSortie;
        this.exemplaires = exemplaires;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEditeur() {
        return editeur;
    }

    public void setEditeur(String editeur) {
        this.editeur = editeur;
    }

    public int getAnneeSortie() {
        return anneeSortie;
    }

    public void setAnneeSortie(int anneeSortie) {
        this.anneeSortie = anneeSortie;
    }

    public List<Exemplaire> getExemplaires() {
        return exemplaires;
    }

    public void setExemplaires(List<Exemplaire> exemplaires) {
        this.exemplaires = exemplaires;
    }
}
