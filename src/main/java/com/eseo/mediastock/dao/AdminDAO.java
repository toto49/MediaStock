package com.eseo.mediastock.dao;

import com.eseo.mediastock.model.Admin;
import com.eseo.mediastock.service.PasswordUtil;

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
        // Nettoie les entrées (supprime les espaces invisibles)
        email = email != null ? email.trim() : null;
        password = password != null ? password.trim() : null;

        if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
            System.out.println("Email ou mot de passe vide");
            return null;
        }

        String sql = "SELECT * FROM ADMINISTRATEUR WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String hash = rs.getString("mdp");

                    // Vérification avec le mot de passe en clair
                    if (PasswordUtil.verify(hash, password)) {
                        Admin admin = new Admin(
                                rs.getInt("id"),
                                rs.getString("email"),
                                hash  // Retourne le hash, pas le mot de passe
                        );
                        System.out.println(" Authentification réussie pour: " + email);
                        return admin;
                    } else {
                        System.out.println(" Échec: mot de passe incorrect pour: " + email);
                    }
                } else {
                    System.out.println(" Échec: email non trouvé: " + email);
                }
            }
        } catch (SQLException e) {
            System.err.println(" Erreur SQL dans authenticate: " + e.getMessage());
            throw e;
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
            stmt.setString(2, PasswordUtil.hash(admin.getPasswordHash()));
            stmt.executeUpdate();

            // Récupérer l'ID généré
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                   int generatedId = rs.getInt(1);
                   admin.setId(generatedId);
                   System.out.println("id generee pour admin " +  generatedId);
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
            stmt.setString(2, PasswordUtil.hash(admin.getPasswordHash()));
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

        String sql = "SELECT mdp FROM ADMINISTRATEUR WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next())
                    return false; // Ancien mot de passe incorrect

                String hashActuel = rs.getString("mdp");

                if (!PasswordUtil.verify(hashActuel, ancienMdp)) {
                    return false;
                }
            }
        }

        String updateSql = "UPDATE ADMINISTRATEUR SET mdp = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {

            updateStmt.setString(1, PasswordUtil.hash(nouveauMdp));
            updateStmt.setInt(2, id);

            int rowsAffected = updateStmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
}
