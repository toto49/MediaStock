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

/**
 * The type Livre dao.
 */
public class LivreDAO {

    /**
     * Add produit.
     *
     * @param p the p
     * @throws SQLException the sql exception
     */
    public static void addProduit(Produit p) throws SQLException {
        Livre l = (Livre) p;
        String sql = "INSERT INTO PRODUIT (type_produit, titre, description, editeur, annee_sortie, isbn, auteur, nb_pages, format) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, "Livre");
            stmt.setString(2, l.getTitre());
            stmt.setString(3, l.getDescription());
            stmt.setString(4, l.getEditeur());
            stmt.setInt(5, l.getAnneeSortie());
            stmt.setString(6, l.getIsbn());
            stmt.setString(7, l.getAuteur());
            stmt.setInt(8, l.getNbPages());
            stmt.setString(9, l.getFormat());

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    p.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("La création du livre a échoué, aucun ID n'a été retourné.");
                }
            }
        }
    }

    /**
     * Get by id livre.
     *
     * @param id the id
     * @return the livre
     * @throws SQLException the sql exception
     */
    public static Livre GetByID(int id) throws SQLException {
        Livre livre = null;
        String sql = "SELECT * FROM PRODUIT WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                List<Livre> temp = CreateLivres(rs);
                if (!temp.isEmpty()) {
                    livre = temp.getFirst();
                    livre.setExemplaires(ExemplaireDAO.getExemplairesByProduit(livre));
                }
            }
        }

        return livre;
    }

    /**
     * Produit object list list.
     *
     * @return the list
     * @throws SQLException the sql exception
     */
    public static List<Livre> ProduitObjectList() throws SQLException {
        String sql = "SELECT * FROM PRODUIT WHERE type_produit = ?";
        List<Livre> livres;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "Livre");

            try (ResultSet rs = stmt.executeQuery()) {
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

            String isbn = rs.getString("isbn");
            String auteur = rs.getString("auteur");
            int nb_pages = rs.getInt("nb_pages");
            String format = rs.getString("format");
            List<Exemplaire> exemplaires = new ArrayList<>();
            Livre livre = new Livre(id, titre, description, editeur, anneeSortie , exemplaires, isbn, auteur, nb_pages, format);
            livres.add(livre);
        }
        return livres;
    }

    /**
     * Update produit.
     *
     * @param p the p
     * @throws SQLException the sql exception
     */
    public static void updateProduit(Produit p) throws SQLException {
        Livre l = (Livre) p;
        String sql = "UPDATE PRODUIT SET titre = ?, description = ?, editeur = ?, annee_sortie = ?, isbn = ?, auteur = ?, nb_pages = ?, format = ? WHERE id = ? AND type_produit = 'Livre'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, l.getTitre());
            stmt.setString(2, l.getDescription());
            stmt.setString(3, l.getEditeur());
            stmt.setInt(4, l.getAnneeSortie());
            stmt.setString(5, l.getIsbn());
            stmt.setString(6, l.getAuteur());
            stmt.setInt(7, l.getNbPages());
            stmt.setString(8, l.getFormat());
            stmt.setInt(9, l.getId());
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
     * Count livres int.
     *
     * @return the int
     * @throws SQLException the sql exception
     */
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