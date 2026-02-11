package com.eseo.mediastock.dao;

import com.eseo.mediastock.model.Produits.JeuSociete;
import com.eseo.mediastock.model.Produits.Produit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JeuSocieteDAO implements ProduitDAO {

    @Override
    public void addProduit(Produit p) throws SQLException {
        JeuSociete j = (JeuSociete) p;
        String sql = "INSERT INTO PRODUIT (titre, description, editeur, annee_sortie, nb_joueurs_min,nb_joueurs_max, age_min, age_max, duree_partie) VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, j.getTitre());
            stmt.setString(2, j.getDescription());
            stmt.setString(3, j.getEditeur());
            stmt.setInt(4, j.getAnneeSortie());
            stmt.setInt(5, j.getNbJoueursMin());
            stmt.setInt(6, j.getNbJoueursMax());
            stmt.setInt(7, j.getAgeMin());
            //TODO : Determiner ce qu'on fait du AgeMax
            //stmt.setInt(8, j.getAgeMax());
            stmt.setInt(9, j.getDureePartie());
        }

    }
}
