package com.eseo.mediastock.dao;

import com.eseo.mediastock.model.Exemplaire;
import com.eseo.mediastock.model.Produits.JeuSociete;
import com.eseo.mediastock.model.Produits.Produit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object dédié aux produits de type Produit (Jeu).
 * <p>
 * Gère l'insertion et la récupération des spécificités liées à ce type de média
 * en base de données, souvent en manipulant des jointures entre la table parente 'Produit'
 * et la table enfant correspondante.
 * </p>
 */
public class JeuSocieteDAO {

    /**
     * Add produit.
     *
     * @param p the p
     * @throws SQLException the sql exception
     */
    public static void addProduit(Produit p) throws SQLException {
        JeuSociete j = (JeuSociete) p;
        String sql = "INSERT INTO PRODUIT (type_produit, titre, description, editeur, annee_sortie, nb_joueurs_min,nb_joueurs_max, age_min, duree_partie) VALUES (?,?,?,?,?,?,?,?,?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, "Jeu");
            stmt.setString(2, j.getTitre());
            stmt.setString(3, j.getDescription());
            stmt.setString(4, j.getEditeur());
            stmt.setInt(5, j.getAnneeSortie());
            stmt.setInt(6, j.getNbJoueursMin());
            stmt.setInt(7, j.getNbJoueursMax());
            stmt.setInt(8, j.getAgeMin());
            stmt.setInt(9, j.getDureePartie());

            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    p.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("La création du jeu de société a échoué, aucun ID n'a été retourné.");
                }
            }
        }
    }

    /**
     * Get by id jeu societe.
     *
     * @param id the id
     * @return the jeu societe
     * @throws SQLException the sql exception
     */
    public static JeuSociete GetByID(int id) throws SQLException {
        JeuSociete jeu = null;
        String sql = "SELECT * FROM PRODUIT WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                List<JeuSociete> temp = CreateJeux(rs);
                if (!temp.isEmpty()) {
                    jeu = temp.getFirst();
                    jeu.setExemplaires(ExemplaireDAO.getExemplairesByProduit(jeu));
                }
            }
        }

        return jeu;
    }

    /**
     * Produit object list list.
     *
     * @return the list
     * @throws SQLException the sql exception
     */
    public static List<JeuSociete> ProduitObjectList() throws SQLException {
        String sql = "SELECT * FROM PRODUIT WHERE type_produit = ?";
        List<JeuSociete> jeux;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "Jeu");

            try (ResultSet rs = stmt.executeQuery()) {
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
            List<Exemplaire> exemplaires = new ArrayList<>();
            JeuSociete jeu = new JeuSociete(id, titre, description, editeur, anneeSortie, exemplaires, nbJoueursMin, nbJoueursMax, ageMin, dureePartie);
            jeux.add(jeu);
        }
        return jeux;
    }

    /**
     * Update produit.
     *
     * @param p the p
     * @throws SQLException the sql exception
     */
    public static void updateProduit(Produit p) throws SQLException {
        JeuSociete j = (JeuSociete) p;
        String sql = "UPDATE PRODUIT SET titre = ?, description = ?, editeur = ?, annee_sortie = ?, nb_joueurs_min = ?, nb_joueurs_max = ?, age_min = ?, duree_partie = ? WHERE id = ? AND type_produit = 'Jeu'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, j.getTitre());
            stmt.setString(2, j.getDescription());
            stmt.setString(3, j.getEditeur());
            stmt.setInt(4, j.getAnneeSortie());
            stmt.setInt(5, j.getNbJoueursMin());
            stmt.setInt(6, j.getNbJoueursMax());
            stmt.setInt(7, j.getAgeMin());
            stmt.setInt(8, j.getDureePartie());
            stmt.setInt(9, j.getId());
            stmt.executeUpdate();
        }
    }


    /**
     * Delete produit.
     *
     * @param p the p
     * @throws SQLException the sql exception
     */
    public static void deleteProduit(Produit p) throws SQLException {
        String sql = "UPDATE EXEMPLAIRE SET statut = 'RETIRE' WHERE id_produit = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, p.getId());
            stmt.executeUpdate();
        }
    }

    /**
     * Count jeux int.
     *
     * @return the int
     * @throws SQLException the sql exception
     */
    public int countJeux() throws SQLException {
        String sql = "SELECT COUNT(*) FROM PRODUIT WHERE type_produit = 'Jeu'";
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