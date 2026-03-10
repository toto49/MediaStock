package com.eseo.mediastock.dao;

import com.eseo.mediastock.model.Exemplaire;
import com.eseo.mediastock.model.Produits.JeuSociete;
import com.eseo.mediastock.model.Produits.Livre;
import com.eseo.mediastock.model.Produits.Produit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JeuSocieteDAO {

    public static void addProduit(Produit p) throws SQLException {
        JeuSociete j = (JeuSociete) p;
        String sql = "INSERT INTO PRODUIT (type_produit, titre, description, editeur, annee_sortie, nb_joueurs_min,nb_joueurs_max, age_min, duree_partie) VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, "JeuSociete");
            stmt.setString(2, j.getTitre());
            stmt.setString(3, j.getDescription());
            stmt.setString(4, j.getEditeur());
            stmt.setInt(5, j.getAnneeSortie());
            stmt.setInt(6, j.getNbJoueursMin());
            stmt.setInt(7, j.getNbJoueursMax());
            stmt.setInt(8, j.getAgeMin());
            stmt.setInt(9, j.getDureePartie());
            stmt.executeUpdate();
        }

    }

    public void updateProduit(Produit p) throws SQLException {
        JeuSociete j = (JeuSociete) p;
        String sql = "UPDATE PRODUIT SET titre = ?, description = ?, annee_sortie = ?, nb_joueurs_min = ?, nb_joueurs_max = ?, age_min = ?, duree_partie = ? WHERE id = ? AND type_produit = 'Jeu'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, j.getTitre());
            stmt.setString(2, j.getDescription());
            stmt.setInt(3, j.getAnneeSortie());
            stmt.setInt(4, j.getNbJoueursMin());
            stmt.setInt(5, j.getNbJoueursMax());
            stmt.setInt(6, j.getAgeMin());
            stmt.setInt(7, j.getDureePartie());
            stmt.setInt(8, j.getId());
            stmt.executeUpdate();

        }
    }

    public static List<JeuSociete> ProduitObjectList() throws SQLException {
        String sql = "SELECT * FROM PRODUIT WHERE type_produit = ?";
        List<JeuSociete> jeux;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "Jeu");

            try (ResultSet rs = stmt.executeQuery()) {
                // On transforme le ResultSet en liste d'objets AVANT de fermer la connexion
                jeux = CreateJeux(rs);
            }
        }

        return jeux;
    }

    private static List<JeuSociete> CreateJeux(ResultSet rs) throws SQLException {
        List<JeuSociete> jeux = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            String titre = rs.getString("titre");
            String description = rs.getString("description");
            String editeur = rs.getString("editeur");
            int anneeSortie = rs.getInt("annee_sortie");

            int nbJoueursMin = rs.getInt("nb_joueurs_min");
            int nbJoueursMax = rs.getInt("nb_joueurs_max");
            int ageMin = rs.getInt("age_min");
            int dureePartie = rs.getInt("duree_partie");

            // EN attendant
            List<Exemplaire> exemplaires = null;

            // Création de l'objet
            JeuSociete jeu = new JeuSociete(id, titre, description, editeur, anneeSortie , exemplaires, nbJoueursMin, nbJoueursMax, ageMin, dureePartie);
            jeux.add(jeu);
        }
        return jeux;
    }

    public static JeuSociete GetByID(int id) throws SQLException {
        JeuSociete jeu = null;
        String sql = "SELECT * FROM PRODUIT WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            try (ResultSet rs = stmt.executeQuery()) {
                jeu = CreateJeux(rs).getFirst();
            }
        }

        return jeu;
    }
}
