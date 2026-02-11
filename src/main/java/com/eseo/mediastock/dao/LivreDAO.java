package com.eseo.mediastock.dao;

import com.eseo.mediastock.model.Produits.Livre;
import com.eseo.mediastock.model.Produits.Produit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LivreDAO implements ProduitDAO {

    @Override
    public void addProduit(Produit p) throws SQLException {
        Livre l = (Livre) p;
        String sql = "INSERT INTO PRODUIT (titre, description, editeur, annee_sortie, isbn, auteur, nb_pages, format)VALUES (?, ?, ?, ?, ?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, l.getTitre());
            stmt.setString(2, l.getDescription());
            stmt.setString(3, l.getEditeur());
            stmt.setInt(4, l.getAnneeSortie());
            stmt.setInt(5, l.getIsbn());
            stmt.setString(6, l.getAuteur());
            stmt.setInt(7, l.getNbPages());
            stmt.setString(8, l.getFormat());
            stmt.executeUpdate();
        }
    }

}