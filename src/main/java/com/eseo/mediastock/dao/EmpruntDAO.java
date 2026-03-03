package com.eseo.mediastock.dao;

import com.eseo.mediastock.model.Adherent;
import com.eseo.mediastock.model.Emprunt;
import com.eseo.mediastock.model.Enum.EnumDispo;
import com.eseo.mediastock.model.Exemplaire;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmpruntDAO {

//    public ResultSet FindById(int id) throws SQLException {
//        String sql = "SELECT * FROM EMPRUNT WHERE id = ?";
//
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//
//            stmt.setInt(1, id);
//
//            try (ResultSet rs = stmt.executeQuery()) {
//                return rs;
//            }
//        }
//        return null;
//    }
//
//    public List<Emprunt> intoEmpruntList(ResultSet rs) throws SQLException {
//        List<Emprunt> emprunts = new ArrayList<>();
//
//        while (rs.next()) {
//            // 1. Récupération des données basiques
//            int id = rs.getInt("id");
//
//            // Attention aux valeurs nulles possibles en base pour les dates
//            LocalDate dateDebut = rs.getDate("date_debut") != null ? rs.getDate("date_debut").toLocalDate() : null;
//            LocalDate dateRetour = rs.getDate("date_retour") != null ? rs.getDate("date_retour").toLocalDate() : null;
//
//            // 2. Gestion de l'Enum
//            String statutStr = rs.getString("statut");
//            EnumDispo statut = statutStr != null ? EnumDispo.valueOf(statutStr) : EnumDispo.EMPRUNTE;
//
//            // 3. Récupération des objets liés via leurs DAOs respectifs
//            Adherent adherent = adherentDAO.findById(rs.getString("id_adherent"));
//            Exemplaire exemplaire = exemplaireDAO.findById(rs.getInt("id_exemplaire"));
//
//            // 4. Construction de l'objet
//            Emprunt emprunt = new Emprunt(id, adherent, exemplaire, dateDebut, dateRetour);
//            emprunt.setStatusDispo(statut);
//
//            // 5. Ajout à la liste
//            emprunts.add(emprunt);
//        }
//
//        return emprunts;
//    }

    public void createEmprunt(Adherent adherent, Exemplaire exemplaire) throws SQLException {
        String sql = "INSERT INTO EMPRUNT (date_debut, date_retour, id_adherent, id_exemplaire,statut) VALUES (?, ?, ? , ?, ?)";

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
            // 5. statut : On définit le statut à EMPRUNTE
            stmt.setString(5, EnumDispo.EMPRUNTE.name());

            stmt.executeUpdate();
        }
    }

    public void saveRetour(Emprunt emprunt) throws SQLException {
        String sql = "UPDATE EMPRUNT SET date_retour = ?, status = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // 1. Date du jour (Conversion LocalDate vers java.sql.Date)
            stmt.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
            // 2. Status : On utilise .name() pour enregistrer le String de l'Enum en base
            stmt.setString(2, EnumDispo.RENDU.name());
            // 3. ID de l'emprunt
            stmt.setInt(3, emprunt.getId());

            stmt.executeUpdate();
        }
    }

    public List<Emprunt> trouverRetards(LocalDate date) throws SQLException {
        List<Emprunt> retards = new ArrayList<>();
        String sql = "SELECT * FROM emprunt WHERE date_retour_prevue < ? AND statut = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, java.sql.Date.valueOf(date));
            stmt.setString(2, EnumDispo.EMPRUNTE.name()); // On cherche ceux non rendus

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
//                    retards.add(intoEmpruntObject(rs));
                }
            }
        }
        return retards;
    }
}
