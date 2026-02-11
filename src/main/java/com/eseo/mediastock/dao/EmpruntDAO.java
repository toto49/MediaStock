package com.eseo.mediastock.dao;

import com.eseo.mediastock.model.Adherent;
import com.eseo.mediastock.model.Emprunt;
import com.eseo.mediastock.model.Exemplaire;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class EmpruntDAO {

    public void createEmprunt(Adherent adherent, Exemplaire exemplaire) throws SQLException {
        String sql = "INSERT INTO EMPRUNT (date_debut, date_retour, id_adherent, id_exemplaire) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // 1. date_debut : On utilise la date du jour
            stmt.setDate(1, Date.valueOf(LocalDate.now()));
            // 2. date_retour : On définit une date de retour
            stmt.setDate(2, Date.valueOf(LocalDate.now().plusMonths(2)));
            // 3. id_adherent : CHAR(12) dans la BDD, on récupère l'ID de l'objet Adherent
            stmt.setString(3, adherent.getId());
            // 4. id_exemplaire : INT(11) dans la BDD, on récupère l'ID de l'objet Exemplaire
            stmt.setInt(4, exemplaire.getId());

            stmt.executeUpdate();
        }
    }

    public void saveRetour(Emprunt emprunt) throws SQLException {
        String sql = "UPDATE EMPRUNT SET date_retour = ? WHERE id = ?"; // Ajouter status rendu

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // 1. Date du jour = date de rendu réel
            stmt.setDate(1, Date.valueOf(LocalDate.now()));
            // 2. ID de l'emprunt (clé primaire)
            stmt.setInt(2, emprunt.getId());

            stmt.executeUpdate();
        }
    }
}
