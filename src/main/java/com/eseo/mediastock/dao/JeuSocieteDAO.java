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

public class JeuSocieteDAO implements ProduitDAO {

    @Override
    public void addProduit(Produit p) throws SQLException {
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
        }

    }

    public List<JeuSociete> ProduitObjectList() throws SQLException {
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

    private List<JeuSociete> CreateJeux(ResultSet rs) throws SQLException {
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
}
