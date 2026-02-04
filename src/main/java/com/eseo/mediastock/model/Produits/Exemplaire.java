package com.eseo.mediastock.model.Produits;

import com.eseo.mediastock.model.Enum.EnumDispo;
import com.eseo.mediastock.model.Enum.EnumEtat;

import java.util.Date;

public class Exemplaire {
    // Déclaration des variables
    private int id;
    private Produit produit;
    private String codeBare;
    private Date dateAcquisition;
    private EnumEtat etatPhysique;
    private EnumDispo statusDispo;
    // Getter/Setter
    public int getId() {
        return id;
    }

    public Produit getProduit() {
        return produit;
    }

    public String getCodeBare() {
        return codeBare;
    }

    public Date getDateAcquisition() {
        return dateAcquisition;
    }

    public EnumEtat getEtatPhysique() {
        return etatPhysique;
    }

    public void setEtatPhysique(EnumEtat etatPhysique) {
        this.etatPhysique = etatPhysique;
    }

    public EnumDispo getStatusDispo() {
        return statusDispo;
    }

    public void setStatusDispo(EnumDispo statusDispo) {
        this.statusDispo = statusDispo;
    }

    public boolean estEmpruntable(){
        return (statusDispo == EnumDispo.DISPONIBLE);
    }
}
