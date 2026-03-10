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

public class LivreDAO {

    public static void addProduit(Produit p) throws SQLException {
        Livre l = (Livre) p;
        String sql = "INSERT INTO PRODUIT (type_produit, titre, description, editeur, annee_sortie, isbn, auteur, nb_pages, format)VALUES (?, ?, ?, ?, ?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "Livre");
            stmt.setString(2, l.getTitre());
            stmt.setString(3, l.getDescription());
            stmt.setString(4, l.getEditeur());
            stmt.setInt(5, l.getAnneeSortie());
            stmt.setInt(6, l.getIsbn());
            stmt.setString(7, l.getAuteur());
            stmt.setInt(8, l.getNbPages());
            stmt.setString(9, l.getFormat());
            stmt.executeUpdate();
        }
    }

    public void updateProduit(Produit p) throws SQLException {
        Livre l = (Livre) p;
        String sql = "UPDATE PRODUIT SET titre = ?, description = ?, annee_sortie = ?, isbn = ?, auteur = ?, nb_pages = ?, format = ? WHERE id = ? AND type_produit = 'Livre'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, l.getTitre());
            stmt.setString(2, l.getDescription());
            stmt.setInt(3, l.getAnneeSortie());
            stmt.setInt(4, l.getIsbn());
            stmt.setString(5, l.getAuteur());
            stmt.setInt(6, l.getNbPages());
            stmt.setString(7, l.getFormat());
            stmt.setInt(8, l.getId());
            stmt.executeUpdate();
        }
    }

    public static List<Livre> ProduitObjectList() throws SQLException {
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

    private static List<Livre> CreateLivres(ResultSet rs) throws SQLException {
        List<Livre> livres = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("id");
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

    public int countLivres() throws SQLException {
        String sql = "SELECT COUNT(*) FROM PRODUIT WHERE type_produit = 'Livre'";
        int count = 0;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                count = rs.getInt(1);
            }
        }
        return count;
    }
}