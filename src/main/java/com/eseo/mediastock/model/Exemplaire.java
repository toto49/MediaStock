package com.eseo.mediastock.model;

import com.eseo.mediastock.model.Enum.EnumDispo;
import com.eseo.mediastock.model.Enum.EnumEtat;
import com.eseo.mediastock.model.Produits.Produit;

/**
 * Représentation d'une unité physique stockée en médiathèque.
 * Contient les informations de traçabilité unique (code-barres) et
 * l'état actuel (neuf, usagé, perdu) ainsi que le statut de prêt.
 */
public class Exemplaire {
    // Déclaration des variables
    private int id;
    private Produit produit;
    private String codeBarre;
    private EnumEtat etatPhysique;
    private EnumDispo statusDispo;

    /**
     * Instantiates a new Exemplaire.
     */
// Constructeurs
    public Exemplaire() {
    }

    /**
     * Instantiates a new Exemplaire.
     *
     * @param id           the id
     * @param produit      the produit
     * @param codeBarre    the code barre
     * @param etatPhysique the etat physique
     * @param statusDispo  the status dispo
     */
    public Exemplaire(int id, Produit produit, String codeBarre, EnumEtat etatPhysique, EnumDispo statusDispo) {
        this.id = id;
        this.produit = produit;
        this.codeBarre = codeBarre;
        this.etatPhysique = etatPhysique;
        this.statusDispo = statusDispo;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
// Getter/Setter
    public int getId() {
        return id;
    }

    /**
     * Gets produit.
     *
     * @return the produit
     */
    public Produit getProduit() {
        return produit;
    }

    /**
     * Sets produit.
     *
     * @param produit the produit
     */
    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    /**
     * Gets code barre.
     *
     * @return the code barre
     */
    public String getCodeBarre() {
        return codeBarre;
    }

    /**
     * Sets code barre.
     *
     * @param codeBarre the code barre
     */
    public void setCodeBarre(String codeBarre) {
        this.codeBarre = codeBarre;
    }

    /**
     * Gets etat physique.
     *
     * @return the etat physique
     */
    public EnumEtat getEtatPhysique() {
        return etatPhysique;
    }

    /**
     * Sets etat physique.
     *
     * @param etatPhysique the etat physique
     */
    public void setEtatPhysique(EnumEtat etatPhysique) {
        this.etatPhysique = etatPhysique;
    }

    /**
     * Gets status dispo.
     *
     * @return the status dispo
     */
    public EnumDispo getStatusDispo() {
        return statusDispo;
    }

    /**
     * Sets status dispo.
     *
     * @param statusDispo the status dispo
     */
    public void setStatusDispo(EnumDispo statusDispo) {
        this.statusDispo = statusDispo;
    }

    /**
     * Est dispo boolean.
     *
     * @return the boolean
     */
// Méthodes
    public boolean estDispo() {
        return (statusDispo == EnumDispo.DISPONIBLE);
    }

    /**
     * Est bon etat boolean.
     *
     * @return the boolean
     */
    public boolean estBonEtat() {
        return (etatPhysique != EnumEtat.ABIME);
    }
}
