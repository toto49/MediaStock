package com.eseo.mediastock.dao;

import com.eseo.mediastock.model.Exemplaire;
import com.eseo.mediastock.model.Produits.Livre;
import com.eseo.mediastock.model.Produits.Produit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public List<Livre> ProduitObjectList() throws SQLException {
        String sql = "SELECT * FROM PRODUIT WHERE type_produit = ?";
        List<Livre> livres;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "Livre");

            try (ResultSet rs = stmt.executeQuery()) {
                // On transforme le ResultSet en liste d'objets AVANT de fermer la connexion
                livres = CreateLivres(rs);
            }
        }

        return livres;
    }

    private List<Livre> CreateLivres(ResultSet rs) throws SQLException {
        List<Livre> livres = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("id_produit");
            String titre = rs.getString("titre");
            String description = rs.getString("description");
            String editeur = rs.getString("editeur");
            int anneeSortie = rs.getInt("annee_sortie");

            int isbn = rs.getInt("isbn");
            String auteur = rs.getString("auteur");
            int nb_pages = rs.getInt("nb_pages");
            String format = rs.getString("format");

            // EN attendant
            List<Exemplaire> exemplaires = null;

            // Création de l'objet
            Livre livre = new Livre(id, titre, description, editeur, anneeSortie , exemplaires, isbn, auteur, nb_pages, format);
            livres.add(livre);
        }
        return livres;
    }

}