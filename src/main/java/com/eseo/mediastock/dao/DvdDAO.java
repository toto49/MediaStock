package com.eseo.mediastock.dao;

import com.eseo.mediastock.model.Produits.DVD;
import com.eseo.mediastock.model.Produits.Livre;
import com.eseo.mediastock.model.Produits.Produit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DvdDAO implements ProduitDAO {

    @Override
    public void addProduit(Produit p) throws SQLException {
        DVD d = (DVD) p;
        String sql = "INSERT INTO PRODUIT (titre, description, editeur, annee_sortie, realisateur, duree_minutes, audio_langues, sous_titres) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, d.getTitre());
            stmt.setString(2, d.getDescription());
            stmt.setString(3, d.getEditeur());
            stmt.setInt(4, d.getAnneeSortie());
            stmt.setString(5, d.getRealisateur());
            stmt.setInt(6, d.getDureeMinutes());
            //stmt.setArray(7, d.getAudioLangues());
            //stmt.setArray(8, d.getSousTitres());
            //TODO : Need help, trouve pas de stmt.setList --> Solution = passer les List en Array ?

            //TODO: enlever les lignes en commentaires en dessous quand le code sera corrigé
            //stmt.executeUpdate();
        }
    }
}
