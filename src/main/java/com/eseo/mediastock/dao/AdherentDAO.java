package com.eseo.mediastock.dao;

import com.eseo.mediastock.model.Adherent;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) pour l'entité Adherent.
 * Cette classe gère toutes les opérations CRUD (Create, Read, Update, Delete)
 * en base de données pour les membres de la médiathèque en utilisant JDBC.
 */
public class AdherentDAO {

    /**
     * Create adherent.
     *
     * @param adherent the adherent
     * @throws SQLException the sql exception
     */
    public void createAdherent(Adherent adherent) throws SQLException {
        String sql = "INSERT INTO ADHERENT (id, num_tel, nom, prenom, email) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, adherent.getId()); // "ADH-2026-001"
            stmt.setString(2, adherent.getNumTel());
            stmt.setString(3, adherent.getNom());
            stmt.setString(4, adherent.getPrenom());
            stmt.setString(5, adherent.getEmailContact());

            stmt.executeUpdate();
        }
    }

    /**
     * Update adherent.
     *
     * @param adherent the adherent
     * @throws SQLException the sql exception
     */
    public void updateAdherent(Adherent adherent) throws SQLException {
        String sql = "UPDATE ADHERENT SET num_tel = ?, nom = ?, prenom =?, email=? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, adherent.getNumTel());
            stmt.setString(2, adherent.getNom());
            stmt.setString(3, adherent.getPrenom());
            stmt.setString(4, adherent.getEmailContact());
            stmt.setString(5, adherent.getId()); //id pour la clause where

            //execute la mise a jour
            stmt.executeUpdate();
        }
    }

    /**
     * Count adherents int.
     *
     * @param annee the annee
     * @return the int
     * @throws SQLException the sql exception
     */
    public int countAdherents(int annee) throws SQLException {
        String sql = "SELECT COUNT(*) FROM ADHERENT WHERE id LIKE ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "ADH-" + annee + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    /**
     * Delete adherent boolean.
     *
     * @param id the id
     * @return the boolean
     * @throws SQLException the sql exception
     */
    public boolean deleteAdherent(String id) throws SQLException {
        String sql = "DELETE FROM ADHERENT WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }

    /**
     * Get by id adherent.
     *
     * @param id the id
     * @return the adherent
     * @throws SQLException the sql exception
     */
    public Adherent GetByID(String id) throws SQLException {
        String sql = "SELECT * FROM ADHERENT WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);

            //resultat de la requete
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Adherent(
                            rs.getString("id"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            rs.getString("email"),
                            rs.getString("num_tel")
                    );
                }
                return null; //si pas de resultat
            }
        }
    }

    /**
     * Find all list.
     *
     * @return the list
     * @throws SQLException the sql exception
     */
    public List<Adherent> findAll() throws SQLException {
        //liste pour stocker les resultats
        List<Adherent> adherents = new ArrayList<>();

        String sql = "SELECT * FROM ADHERENT ORDER BY nom, prenom";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            //parcour et créer les lignes du resultat
            while (rs.next()) {
                Adherent adherent = new Adherent(
                        rs.getString("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("num_tel")
                );
                //ajout a la liste
                adherents.add(adherent);
            }
        }
        return adherents; // retourne la liste complete
    }

    private List<Adherent> createAdherents(ResultSet rs) throws SQLException {
        List<Adherent> adherents = new ArrayList<>();

        while (rs.next()) {
            //  Récupération des données selon le nom des colonnes en BDD
            String id = rs.getString("id");
            String numTel = rs.getString("num_tel");
            String nom = rs.getString("nom");
            String prenom = rs.getString("prenom");
            String email = rs.getString("email");

            // création de l'objet Adherent
            Adherent adherent = new Adherent(id, nom, prenom, email, numTel);

            // Ajout à la liste
            adherents.add(adherent);
        }
        return adherents;
    }
}