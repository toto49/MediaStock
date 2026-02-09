package com.eseo.mediastock.model.Produits;

import com.eseo.mediastock.model.Enum.EnumDispo;
import com.eseo.mediastock.model.Enum.EnumEtat;

import java.time.LocalDate;
import java.util.Date;

public class Exemplaire {
    // Déclaration des variables
    private int id;
    private Produit produit;
    private String codeBarre;
    private LocalDate dateAcquisition;
    private EnumEtat etatPhysique;
    private EnumDispo statusDispo;

    // Constructeurs
    public Exemplaire(){
    }

    public Exemplaire(int id, Produit produit, String codeBarre, LocalDate dateAcquisition, EnumEtat etatPhysique, EnumDispo statusDispo) {
        this.id = id;
        this.produit = produit;
        this.codeBarre = codeBarre;
        this.dateAcquisition = dateAcquisition;
        this.etatPhysique = etatPhysique;
        this.statusDispo = statusDispo;
    }

    // Getter/Setter
    public int getId() {
        return id;
    }

    public Produit getProduit() {
        return produit;
    }

    public String getCodeBarre() {
        return codeBarre;
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

    public void setProduit(Produit produit) {this.produit = produit;}

    public void setCodeBarre(String codeBarre) {this.codeBarre = codeBarre;}

    // Méthodes
    public boolean estDispo(){
        return (statusDispo == EnumDispo.DISPONIBLE);
    }

    public boolean estBonEtat(){
        return (etatPhysique != EnumEtat.ABIME);
    }
}
