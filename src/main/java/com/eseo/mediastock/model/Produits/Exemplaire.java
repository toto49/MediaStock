package com.eseo.mediastock.model.Produits;

import com.eseo.mediastock.model.Enum.EnumDispo;
import com.eseo.mediastock.model.Enum.EnumEtat;

import java.util.Date;

public class Exemplaire {
    private int id;
    private Produit produit;
    private String codeBare;
    private Date dateAcquisition;
    private EnumEtat etatPhysique;
    private EnumDispo statusDispo;
}
