package com.eseo.mediastock.dao;

import com.eseo.mediastock.model.Admin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class AdminDAO {
/**
 * AUTHENTIFICATION DE L'ADMIN
 * @param email - L'email de l'admin
 * @param password - Le mot de passe
 * @return Admin si trouvé, null sinon
 * @throws SQLException - Si erreur SQL
 */
public Admin authenticate(String email, String password) throws SQLException {
    String sql = "SELECT * FROM ADMINISTRATEUR WHERE email = ? AND mdp = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, email);
        stmt.setString(2, password);

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return new Admin(
                        rs.getInt("id"),
                        rs.getString("email"),
                        rs.getString("mdp")
                );
            }
        }
    }
    return null;
}

/**
 * METHODE CREATE = Crée un nouvel administrateur
 * @param admin - L'admin à ajouter
 * @throws SQLException - Si erreur
 */
public void create(Admin admin) throws SQLException {
    String sql = "INSERT INTO ADMINISTRATEUR (email, mdp) VALUES (?, ?)";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

        stmt.setString(1, admin.getEmail());
        stmt.setString(2, admin.getMdp());
        stmt.executeUpdate();

        // Récupérer l'ID généré
        try (ResultSet rs = stmt.getGeneratedKeys()) {
            if (rs.next()) {
                // pourmettre à jour l'objet avec l'ID
            }
        }
    }
}
/**
 * METHODE UPDATE = Met à jour un administrateur
 * @param admin - L'admin avec les nouvelles valeurs
 * @throws SQLException - Si erreur
 */
public void update(Admin admin) throws SQLException {
    String sql = "UPDATE ADMINISTRATEUR SET email = ?, mdp = ? WHERE id = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, admin.getEmail());
        stmt.setString(2, admin.getMdp());
        stmt.setInt(3, admin.getId());

        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected == 0) {
            System.out.println("Aucun admin trouvé avec l'ID: " + admin.getId());
        }
    }
}

/**
 * METHODE DELETE = Supprime un admin
 * @param id - L'ID de l'admin à supprimer
 * @throws SQLException - Si erreur
 */
public void delete(int id) throws SQLException {
    String sql = "DELETE FROM ADMINISTRATEUR WHERE id = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, id);

        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected == 0) {
            System.out.println("Aucun admin trouvé avec l'ID: " + id);
        } else {
            System.out.println("Admin supprimé avec succès");
        }
    }
}

/**
 * METHODE READ = findall = Récupère tous les administrateurs
 * @return List<Admin> - Liste des admins
 * @throws SQLException - Si erreur
 */
public List<Admin> findAll() throws SQLException {
    List<Admin> admins = new ArrayList<>();
    String sql = "SELECT * FROM ADMINISTRATEUR ORDER BY email";

    try (Connection conn = DatabaseConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        while (rs.next()) {
            admins.add(new Admin(
                    rs.getInt("id"),
                    rs.getString("email"),
                    rs.getString("mdp")
            ));
        }
    }
    return admins;
}

/**
 * Change le mot de passe d'un admin METHODE UPDATE
 * @param id - L'ID de l'admin
 * @param ancienMdp - Ancien mot de passe (vérification)
 * @param nouveauMdp - Nouveau mot de passe
 * @return boolean - true si changement réussi
 * @throws SQLException - Si erreur
 */
public boolean changePassword(int id, String ancienMdp, String nouveauMdp) throws SQLException {
    // Vérifier d'abord l'ancien mot de passe
    String checkSql = "SELECT * FROM ADMINISTRATEUR WHERE id = ? AND mdp = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

        checkStmt.setInt(1, id);
        checkStmt.setString(2, ancienMdp);

        try (ResultSet rs = checkStmt.executeQuery()) {
            if (!rs.next()) {
                return false; // Ancien mot de passe incorrect
            }
        }
    }

    String updateSql = "UPDATE ADMINISTRATEUR SET mdp = ? WHERE id = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {

        updateStmt.setString(1, nouveauMdp);
        updateStmt.setInt(2, id);

        int rowsAffected = updateStmt.executeUpdate();
        return rowsAffected > 0;
    }
}
}