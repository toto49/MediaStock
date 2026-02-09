package com.eseo.mediastock.dao;

import com.eseo.mediastock.model.Adherent;

import java.sql.*;

public class AdherentDAO {
/**
 * MÉTHODE CREATE - Insère un nouvel adhérent en base
 * @param adherent - L'objet Adherent à sauvegarder
 * @throws SQLException - Si l'insertion échoue**/
    public void create(Adherent adherent) throws SQLException{
        String sql = "INSERT INTO ADHERENT (num_tel, nom, prenom, email) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            stmt.setString(1, adherent.getNumTel());
            stmt.setString(2, adherent.getNom());
            stmt.setString(3, adherent.getPrenom());
            stmt.setString(4, adherent.getEmailContact());
            //execute la requete
            stmt.executeUpdate();

        // recupere le id genere par sql
        try (ResultSet rs = stmt.getGeneratedKeys()){
            if (rs.next()){
                int generatedId = rs.getInt(1);

            }
        }
        }
    }
}
/**
 * MÉTHODE READ (findById) - Récupère un adhérent par son ID
 * @param id - L'identifiant de l'adhérent dans la base
 * @return Adherent - L'objet Adherent correspondant, ou null si non trouvé
 * @throws SQLException - Si la requête échoue**/

public Adherent findById (int id) throws SQLException {
    String sql = "SELECT * FROM ADHRENT WHERE id = ?";

    try (Connection conn = DatabaseConnection.getConnection();
    PreparedStatement stmt = conn.prepareStatement(sql)){
     stmt.setInt(1,id);
     //resultat de la requete
     try (ResultSet rs = stmt.executeQuery()){
         if (rs.next()){
             return new Adherent(String.valueOf(rs.getInt("id")),
                     rs.getString("nom"),
                     rs.getString("prenom"),
                     rs.getString("email"),
                     rs.getString("num_tel"));
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
                    String.valueOf(rs.getInt("id")),
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

public void update (Adherent adherent) throws SQLException{
    String sql = "UPDATE ADHERENT SET num_tel = ?, nom = ?, prenom =?, email=? WHERE id = ?";
    try (Connection conn = DatabaseConnection.getConnection();
    PreparedStatement stmt = conn.prepareStatement(sql)){
        stmt.setString(1, adherent.getNumTel());
        stmt.setString(2, adherent.getNom());
        stmt.setString(3, adherent.getPrenom());
        stmt.setString(4, adherent.getEmailContact());
        stmt.setInt(5, Integer.parseInt(adherent.getNumAdherent())); //id pour la clause where

        //execute la mise a jour
        int rowsAffected = stmt.executeUpdate();
    }
}

/**
 * MÉTHODE DELETE - Supprime un adhérent par son ID
 * @param id - L'ID de l'adhérent à supprimer
 * @throws SQLException - Si la suppression échoue
 * **/
//a utiliser trés trés precautionneusement

public void delete (int id) throws SQLException{
    String sql = "DELETE FROM ADHERENT WHERE id = ?";

    try (Connection conn = DatabaseConnection.getConnection();
    PreparedStatement stmt = conn.prepareStatement(sql)){
        stmt.setInt(1,id);

        int rowsAffected = stmt.executeUpdate();

        if (rowsAffected == 0){
            System.out.println("Aucun adherent trouvé : " + id);
        } else {
            System.out.println("adherent supprimé " + rowsAffected);
        }
    }
}