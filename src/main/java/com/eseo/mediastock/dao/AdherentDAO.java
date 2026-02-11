package com.eseo.mediastock.dao;

import com.eseo.mediastock.model.Adherent;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdherentDAO {

    /**
     * MÉTHODE CREATE Adherent - Insère un nouvel adhérent avec son ID manuel
     * @param adherent - L'objet Adherent à sauvegarder
     * @throws SQLException - Si l'insertion échoue
     **/
    public void createAdherent (Adherent adherent) throws SQLException {
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
     * MÉTHODE READ (findById) - Récupère un adhérent par son ID
     * @param id - L'identifiant de l'adhérent dans la base
     * @return Adherent - L'objet Adherent correspondant, ou null si non trouvé
     * @throws SQLException - Si la requête échoue**/

    public Adherent findById (String id) throws SQLException {
        String sql = "SELECT * FROM ADHERENT WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){
         stmt.setString(1,id);
         //resultat de la requete
         try (ResultSet rs = stmt.executeQuery()){
             if (rs.next()){
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
     * MÉTHODE READ ALL (findAll) - Récupère TOUS les adhérents
     * @return List<Adherent> - Liste de tous les adhérents
     * @throws SQLException - Si la requête échoue
     * **/
    public List<Adherent> findAll() throws SQLException{
        //liste pour stocker les resultats
        List<Adherent> adherents = new ArrayList<>();

        String sql = "SELECT * FROM ADHERENT ORDER BY nom, prenom";
        try (Connection conn = DatabaseConnection.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){

            //parcour et créer les lignes du resultat
            while (rs.next()){
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

    /**
     * MÉTHODE UPDATE - Met à jour un adhérent existant
     * @param adherent - L'objet Adherent avec les nouvelles valeurs
     * @throws SQLException - Si la mise à jour échoue
     * **/

    public void updateAdherent (Adherent adherent) throws SQLException{
        String sql = "UPDATE ADHERENT SET num_tel = ?, nom = ?, prenom =?, email=? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, adherent.getNumTel());
            stmt.setString(2, adherent.getNom());
            stmt.setString(3, adherent.getPrenom());
            stmt.setString(4, adherent.getEmailContact());
            stmt.setString(5, adherent.getId()); //id pour la clause where

            //execute la mise a jour
            int rowsAffected = stmt.executeUpdate();
        }
    }

    /**
     * MÉTHODE COUNT - Compte le nombre d'adhérents inscrit l'année en cour
     * @return int - Le nombre de lignes dans la table ADHERENT qui répondent a la condition
     * @throws SQLException - Si la requête échoue
     * **/
    public int countAdherents (int annee) throws SQLException {
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
}