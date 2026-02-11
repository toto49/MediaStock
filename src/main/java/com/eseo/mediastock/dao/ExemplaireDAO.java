package com.eseo.mediastock.dao;

import com.eseo.mediastock.model.Exemplaire;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ExemplaireDAO {

    public void updateExemplaire(Exemplaire exemplaire) throws SQLException {
        String sql = "UPDATE EXEMPLAIRE SET statut = ?, etat = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, exemplaire.getStatusDispo().toString());
            stmt.setString(2, exemplaire.getEtatPhysique().toString());
            stmt.setInt(3, exemplaire.getId());

            stmt.executeUpdate();
        }
    }
}
