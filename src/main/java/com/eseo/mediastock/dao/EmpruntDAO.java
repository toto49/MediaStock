package com.eseo.mediastock.dao;

import com.eseo.mediastock.model.Adherent;
import com.eseo.mediastock.model.Emprunt;
import com.eseo.mediastock.model.Enum.EnumDispo;
import com.eseo.mediastock.model.Enum.EnumEtat;
import com.eseo.mediastock.model.Exemplaire;
import com.eseo.mediastock.model.Produits.Produit;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmpruntDAO {

    public List<Emprunt> findAllEmprunts() throws SQLException {
        String sql = "SELECT " +
                "emp.id AS emp_id, emp.date_debut, emp.date_retour, emp.statut AS emp_statut, " +
                "ad.id AS ad_id, ad.nom, ad.prenom, ad.email, ad.num_tel, " +
                "ex.id AS ex_id, ex.code_barre, ex.etat, ex.statut AS ex_statut, " +
                "pr.id AS pr_id " +
                "FROM EMPRUNT emp " +
                "INNER JOIN ADHERENT ad ON emp.id_adherent = ad.id " +
                "INNER JOIN EXEMPLAIRE ex ON emp.id_exemplaire = ex.id " +
                "INNER JOIN PRODUIT pr ON ex.id_produit = pr.id";

        List<Emprunt> emprunts = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                emprunts.add(createEmpruntFromJoin(rs));
            }
        }
        return emprunts;
    }

    public List<Emprunt> trouverRetards(LocalDate date) throws SQLException {
        String sql = "SELECT " +
                "emp.id AS emp_id, emp.date_debut, emp.date_retour, emp.statut AS emp_statut, " +
                "ad.id AS ad_id, ad.nom, ad.prenom, ad.email, ad.num_tel, " +
                "ex.id AS ex_id, ex.code_barre, ex.etat, ex.statut AS ex_statut, " +
                "pr.id AS pr_id " +
                "FROM EMPRUNT emp " +
                "INNER JOIN ADHERENT ad ON emp.id_adherent = ad.id " +
                "INNER JOIN EXEMPLAIRE ex ON emp.id_exemplaire = ex.id " +
                "INNER JOIN PRODUIT pr ON ex.id_produit = pr.id " +
                "WHERE emp.date_retour < ? AND emp.statut = ?";

        List<Emprunt> retards = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, java.sql.Date.valueOf(date));
            stmt.setString(2, EnumDispo.EMPRUNTE.name());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    retards.add(createEmpruntFromJoin(rs));
                }
            }
        }
        return retards;
    }

    // --- MÉTHODE CORRIGÉE ---
    public List<Emprunt> getEmpruntsByAdherent(Adherent adherent) throws SQLException {
        // Suppression de la jointure sur ADHERENT, on n'en a plus besoin !
        String sql = "SELECT " +
                "emp.id AS emp_id, emp.date_debut, emp.date_retour, emp.statut AS emp_statut, " +
                "ex.id AS ex_id, ex.code_barre, ex.etat, ex.statut AS ex_statut, " +
                "pr.id AS pr_id " +
                "FROM EMPRUNT emp " +
                "INNER JOIN EXEMPLAIRE ex ON emp.id_exemplaire = ex.id " +
                "INNER JOIN PRODUIT pr ON ex.id_produit = pr.id " +
                "WHERE emp.id_adherent = ?";

        List<Emprunt> emprunts = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, adherent.getId());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // 1. Instanciation du Produit (Classe anonyme)
                    int idProduit = rs.getInt("pr_id");
                    Produit produit = new Produit() {};
                    produit.setId(idProduit);

                    // 2. Instanciation de l'Exemplaire
                    int idExemplaire = rs.getInt("ex_id");
                    String codeBarre = rs.getString("code_barre");
                    EnumEtat etatPhysique = EnumEtat.valueOf(rs.getString("etat"));
                    EnumDispo statutExemplaire = EnumDispo.valueOf(rs.getString("ex_statut"));

                    Exemplaire exemplaire = new Exemplaire(idExemplaire, produit, codeBarre, etatPhysique, statutExemplaire);

                    // 3. Création de l'Emprunt en utilisant l'Adherent passé en paramètre !
                    int idEmprunt = rs.getInt("emp_id");

                    java.sql.Date sqlDateDebut = rs.getDate("date_debut");
                    java.sql.Date sqlDateRetour = rs.getDate("date_retour");
                    LocalDate dateDebut = sqlDateDebut != null ? sqlDateDebut.toLocalDate() : null;
                    LocalDate dateRetour = sqlDateRetour != null ? sqlDateRetour.toLocalDate() : null;

                    String statutEmpStr = rs.getString("emp_statut");
                    EnumDispo statutEmprunt = statutEmpStr != null ? EnumDispo.valueOf(statutEmpStr) : EnumDispo.EMPRUNTE;

                    Emprunt emprunt = new Emprunt(idEmprunt, adherent, exemplaire, dateDebut, dateRetour);
                    emprunt.setStatusDispo(statutEmprunt);

                    emprunts.add(emprunt);
                }
            }
        }
        return emprunts;
    }

    public void addEmprunt(Adherent adherent, Exemplaire exemplaire) throws SQLException {
        String sql = "INSERT INTO EMPRUNT (date_debut, date_retour, id_adherent, id_exemplaire, statut) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(LocalDate.now()));
            stmt.setDate(2, Date.valueOf(LocalDate.now().plusMonths(2)));
            stmt.setString(3, adherent.getId());
            stmt.setInt(4, exemplaire.getId());
            stmt.setString(5, EnumDispo.EMPRUNTE.name());

            stmt.executeUpdate();
        }
    }

    public void saveRetour(Emprunt emprunt) throws SQLException {
        String sql = "UPDATE EMPRUNT SET date_retour = ?, statut = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
            stmt.setString(2, EnumDispo.RENDU.name());
            stmt.setInt(3, emprunt.getId());

            stmt.executeUpdate();
        }
    }

    // --- LA MÉTHODE COMMUNE DE CRÉATION (pour findAll et trouverRetards) ---
    private Emprunt createEmpruntFromJoin(ResultSet rs) throws SQLException {

        // 1. Instanciation de l'Adhérent
        String idAdherent = rs.getString("ad_id");
        String nomAdherent = rs.getString("nom");
        String prenomAdherent = rs.getString("prenom");
        String emailAdherent = rs.getString("email");
        String telAdherent = rs.getString("num_tel");

        Adherent adherent = new Adherent(idAdherent, nomAdherent, prenomAdherent, emailAdherent, telAdherent);

        // 2. Instanciation du Produit avec une classe anonyme (Solution pour classe abstraite)
        int idProduit = rs.getInt("pr_id");
        Produit produit = new Produit() {};
        produit.setId(idProduit);

        // 3. Instanciation de l'Exemplaire
        int idExemplaire = rs.getInt("ex_id");
        String codeBarre = rs.getString("code_barre");
        EnumEtat etatPhysique = EnumEtat.valueOf(rs.getString("etat"));
        EnumDispo statutExemplaire = EnumDispo.valueOf(rs.getString("ex_statut"));

        Exemplaire exemplaire = new Exemplaire(idExemplaire, produit, codeBarre, etatPhysique, statutExemplaire);

        // 4. Création finale de l'Emprunt
        int idEmprunt = rs.getInt("emp_id");

        java.sql.Date sqlDateDebut = rs.getDate("date_debut");
        java.sql.Date sqlDateRetour = rs.getDate("date_retour");
        LocalDate dateDebut = sqlDateDebut != null ? sqlDateDebut.toLocalDate() : null;
        LocalDate dateRetour = sqlDateRetour != null ? sqlDateRetour.toLocalDate() : null;

        String statutEmpStr = rs.getString("emp_statut");
        EnumDispo statutEmprunt = statutEmpStr != null ? EnumDispo.valueOf(statutEmpStr) : EnumDispo.EMPRUNTE;

        Emprunt emprunt = new Emprunt(idEmprunt, adherent, exemplaire, dateDebut, dateRetour);
        emprunt.setStatusDispo(statutEmprunt);

        return emprunt;
    }
}