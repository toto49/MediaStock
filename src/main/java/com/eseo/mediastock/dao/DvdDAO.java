package com.eseo.mediastock.dao;

import com.eseo.mediastock.model.Exemplaire;
import com.eseo.mediastock.model.Produits.DVD;
import com.eseo.mediastock.model.Produits.Produit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Data Access Object dédié aux produits de type Produit (DVD/).
 * <p>
 * Gère l'insertion et la récupération des spécificités liées à ce type de média
 * en base de données, souvent en manipulant des jointures entre la table parente 'Produit'
 * et la table enfant correspondante.
 * </p>
 */
public class DvdDAO {

    /**
     * Add produit.
     *
     * @param p the p
     * @throws SQLException the sql exception
     */
    public static void addProduit(Produit p) throws SQLException {
        DVD d = (DVD) p;
        String sql = "INSERT INTO PRODUIT (type_produit, titre, description, editeur, annee_sortie, realisateur, duree_minutes, audio_langues, sous_titres) VALUES (?,?,?,?,?,?,?,?,?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, "DVD");
            stmt.setString(2, d.getTitre());
            stmt.setString(3, d.getDescription());
            stmt.setString(4, d.getEditeur());
            stmt.setInt(5, d.getAnneeSortie());
            stmt.setString(6, d.getRealisateur());
            stmt.setInt(7, d.getDureeMinutes());
            stmt.setString(8, String.join(",", d.getAudioLangues()));
            stmt.setString(9, d.getSousTitres() == null ? null : String.join(",", d.getSousTitres()));

            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    p.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("La création du DVD a échoué, aucun ID n'a été retourné.");
                }
            }
        }
    }

    /**
     * Get by id dvd.
     *
     * @param id the id
     * @return the dvd
     * @throws SQLException the sql exception
     */
    public static DVD GetByID(int id) throws SQLException {
        DVD dvd = null;
        String sql = "SELECT * FROM PRODUIT WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                List<DVD> temp = CreateDVD(rs);
                if (!temp.isEmpty()) {
                    dvd = temp.getFirst();
                    dvd.setExemplaires(ExemplaireDAO.getExemplairesByProduit(dvd));
                }
            }
        }

        return dvd;
    }

    /**
     * Produit object list list.
     *
     * @return the list
     * @throws SQLException the sql exception
     */
    public static List<DVD> ProduitObjectList() throws SQLException {
        String sql = "SELECT * FROM PRODUIT WHERE type_produit = ?";
        List<DVD> dvds;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "DVD");

            try (ResultSet rs = stmt.executeQuery()) {
                dvds = CreateDVD(rs);
            }
        }

        return dvds;
    }

    private static List<DVD> CreateDVD(ResultSet rs) throws SQLException {
        List<DVD> dvds = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("id");
            String titre = rs.getString("titre");
            String description = rs.getString("description");
            String editeur = rs.getString("editeur");
            int anneeSortie = rs.getInt("annee_sortie");
            String realisateur = rs.getString("realisateur");
            int dureeMinutes = rs.getInt("duree_minutes");
            String rawAudio = rs.getString("audio_langues");
            List<String> audioLangues = (rawAudio == null || rawAudio.isEmpty()) ? new ArrayList<>() : new ArrayList<>(Arrays.asList(rawAudio.split(",")));
            String rawSub = rs.getString("sous_titres");
            List<String> sousTitres = (rawSub == null || rawSub.isEmpty()) ? new ArrayList<>() : new ArrayList<>(Arrays.asList(rawSub.split(",")));
            List<Exemplaire> exemplaires = new ArrayList<>();
            DVD dvd = new DVD(id, titre, description, editeur, anneeSortie, exemplaires, realisateur, dureeMinutes, audioLangues, sousTitres);
            dvds.add(dvd);
        }
        return dvds;
    }

    /**
     * Update produit.
     *
     * @param p the p
     * @throws SQLException the sql exception
     */
    public static void updateProduit(Produit p) throws SQLException {
        DVD d = (DVD) p;
        String sql = "UPDATE PRODUIT SET titre = ?, description = ?, editeur = ?, annee_sortie = ?, realisateur = ?, duree_minutes = ?, audio_langues = ?, sous_titres = ? WHERE id = ? AND type_produit = 'DVD'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, d.getTitre());
            stmt.setString(2, d.getDescription());
            stmt.setString(3, d.getEditeur());
            stmt.setInt(4, d.getAnneeSortie());
            stmt.setString(5, d.getRealisateur());
            stmt.setInt(6, d.getDureeMinutes());
            stmt.setString(7, String.join(",", d.getAudioLangues()));
            stmt.setString(8, d.getSousTitres() == null ? null : String.join(",", d.getSousTitres()));
            stmt.setInt(9, d.getId());
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
     * Count dv ds int.
     *
     * @return the int
     * @throws SQLException the sql exception
     */
    public int countDVDs() throws SQLException {
        String sql = "SELECT COUNT(*) FROM PRODUIT WHERE type_produit = 'DVD'";
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