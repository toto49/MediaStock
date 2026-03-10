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

    private final AdherentDAO adherentDAO = new AdherentDAO();
    private final ExemplaireDAO exemplaireDAO = new ExemplaireDAO();

    public List<Emprunt> findAllEmprunts() throws SQLException {
        String sql = "SELECT * FROM EMPRUNT";
        List<Emprunt> emprunts = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                emprunts.add(createEmprunt(rs));
            }
        }
        return emprunts;
    }

    // Méthode utilitaire pour construire un Emprunt de façon claire
    private Emprunt createEmprunt(ResultSet rs) throws SQLException {
        // 1. Récupération des données basiques de la table EMPRUNT
        int id = rs.getInt("id");

        // Gestion sécurisée des dates (évite les NullPointerException si la date est vide en base)
        java.sql.Date sqlDateDebut = rs.getDate("date_debut");
        java.sql.Date sqlDateRetour = rs.getDate("date_retour");
        LocalDate dateDebut = sqlDateDebut != null ? sqlDateDebut.toLocalDate() : null;
        LocalDate dateRetour = sqlDateRetour != null ? sqlDateRetour.toLocalDate() : null;

        String statutStr = rs.getString("statut");
        EnumDispo statut = statutStr != null ? EnumDispo.valueOf(statutStr) : EnumDispo.EMPRUNTE;

        // 2. Appel aux autres DAO (Lisible et bien séparé !)
        String idAdherent = rs.getString("id_adherent");
        int idExemplaire = rs.getInt("id_exemplaire");

        Adherent adherent = adherentDAO.GetByID(idAdherent);
        Exemplaire exemplaire = exemplaireDAO.GetByID(idExemplaire);

        // 3. Construction de l'objet
        Emprunt emprunt = new Emprunt(id, adherent, exemplaire, dateDebut, dateRetour);
        emprunt.setStatusDispo(statut);

        return emprunt;
    }

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
            stmt.setString(2, EnumDispo.EMPRUNTE.name());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    retards.add(createEmprunt(rs));
                }
            }
        }
        return retards;
    }
}
