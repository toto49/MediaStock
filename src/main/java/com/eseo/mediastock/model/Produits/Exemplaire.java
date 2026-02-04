package com.eseo.mediastock.model.Produits;

import java.util.Date;

public class Exemplaire {
    private int id;
    private Produit produit;
    private String codeBare;
    private Date dateAcquisition;
    private EnumEtat etatPhysique;
    private EnumDispo statusDispo;
}
