package com.eseo.mediastock.dao;

import com.eseo.mediastock.model.Admin;
import com.eseo.mediastock.service.PasswordUtilService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {

    public Admin authenticate(String email, String password) throws SQLException {
        email = email != null ? email.trim() : null;
        password = password != null ? password.trim() : null;

        if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
            return null;
        }

        String sql = "SELECT * FROM ADMINISTRATEUR WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String hash = rs.getString("mdp");

                    if (PasswordUtilService.verify(hash, password)) {
                        //constructeur
                        Admin admin = new Admin(
                                rs.getInt("id"),
                                rs.getString("nom"),
                                rs.getString("prenom"),
                                rs.getString("email"),
                                rs.getString("num_tel"),
                                hash
                        );
                        return admin;
                    }
                }
            }
        }
        return null;
    }

    public void create(Admin admin) throws SQLException {
        String sql = "INSERT INTO ADMINISTRATEUR (nom, prenom, email, num_tel, mdp) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, admin.getNom());
            stmt.setString(2, admin.getPrenom());
            stmt.setString(3, admin.getEmail());
            stmt.setString(4, admin.getNumTel());
            stmt.setString(5, PasswordUtilService.hash(admin.getPlainPassword()));

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int generatedId = rs.getInt(1);
                    admin.setId(generatedId);
                }
            }
        }
    }

    public void update(Admin admin) throws SQLException {
        String sql = "UPDATE ADMINISTRATEUR SET nom = ?, prenom = ?, email = ?, num_tel = ?, mdp = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, admin.getNom());
            stmt.setString(2, admin.getPrenom());
            stmt.setString(3, admin.getEmail());
            stmt.setString(4, admin.getNumTel());

            // Gestion du mot de passe
            if (admin.getPlainPassword() != null && !admin.getPlainPassword().isEmpty()) {
                stmt.setString(5, PasswordUtilService.hash(admin.getPlainPassword()));
            } else {
                stmt.setString(5, admin.getPasswordHash()); // Garde l'ancien hash
            }

            stmt.setInt(6, admin.getId());

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
            }
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM ADMINISTRATEUR WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);


            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Aucun admin trouvé avec l'ID: " + id);
            }
        }
    }

    public List<Admin> findAll() throws SQLException {
        List<Admin> admins = new ArrayList<>();
        String sql = "SELECT * FROM ADMINISTRATEUR ORDER BY nom, prenom";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                admins.add(new Admin(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("num_tel"),
                        rs.getString("mdp")
                ));
            }
        }
        return admins;
    }

    public Admin findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM ADMINISTRATEUR WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Admin(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            rs.getString("email"),
                            rs.getString("num_tel"),
                            rs.getString("mdp")
                    );
                }
            }
        }
        return null;
    }

    public boolean changePassword(int id, String ancienMdp, String nouveauMdp) throws SQLException {
        String selectSql = "SELECT mdp FROM ADMINISTRATEUR WHERE id = ?";
        String updateSql = "UPDATE ADMINISTRATEUR SET mdp = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            try {
                // Vérifie l'ancien mot de passe
                try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
                    selectStmt.setInt(1, id);

                    try (ResultSet rs = selectStmt.executeQuery()) {
                        if (!rs.next()) {
                            conn.rollback();
                            return false;
                        }

                        String hash = rs.getString("mdp");
                        if (!PasswordUtilService.verify(hash, ancienMdp)) {
                            conn.rollback();
                            return false; // Ancien mot de passe incorrect
                        }
                    }
                }

                // Mettre à jour le nouveau mot de passe
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setString(1, PasswordUtilService.hash(nouveauMdp));
                    updateStmt.setInt(2, id);
                    int rowsAffected = updateStmt.executeUpdate();

                    conn.commit();
                    return rowsAffected > 0;
                }

            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
}