package com.eseo.mediastock.dao;

import com.eseo.mediastock.model.Produits.DVD;
import com.eseo.mediastock.model.Produits.JeuSociete;
import com.eseo.mediastock.model.Produits.Livre;
import com.eseo.mediastock.model.Produits.Produit;

import java.sql.Connection;
import java.sql.SQLException;

public class ProduitDAOFactory {
    public static ProduitDAO getDAO(Produit p) throws SQLException {
        if (p instanceof Livre) return new LivreDAO();
        if (p instanceof DVD) return new DvdDAO();
        if (p instanceof JeuSociete) return new JeuSocieteDAO();

        throw new IllegalArgumentException("Type de produit inconnu");
    }
}

//TODO : Vérifier l'exécution de tout ça !