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

/**
 * Data Access Object dédié à la gestion des transactions d'emprunt.
 * <p>
 * Gère l'historique des prêts, met à jour les dates de retour, et permet de récupérer
 * la liste des emprunts actifs ou en retard pour un adhérent spécifique.
 * </p>
 */
public class EmpruntDAO {

    /**
     * Find all emprunts list.
     *
     * @return the list
     * @throws SQLException the sql exception
     */
    public List<Emprunt> findAllEmprunts() throws SQLException {
        // Ajout de pr.type_produit à la fin du SELECT
        String sql = "SELECT " +
                "emp.id AS emp_id, emp.date_debut, emp.date_retour, emp.statut AS emp_statut, " +
                "ad.id AS ad_id, ad.nom, ad.prenom, ad.email, ad.num_tel, " +
                "ex.id AS ex_id, ex.code_barre, ex.etat, ex.statut AS ex_statut, " +
                "pr.id AS pr_id, pr.type_produit " +
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

    /**
     * Trouver retards list.
     *
     * @param date the date
     * @return the list
     * @throws SQLException the sql exception
     */
    public List<Emprunt> trouverRetards(LocalDate date) throws SQLException {
        // Ajout de pr.type_produit à la fin du SELECT
        String sql = "SELECT " +
                "emp.id AS emp_id, emp.date_debut, emp.date_retour, emp.statut AS emp_statut, " +
                "ad.id AS ad_id, ad.nom, ad.prenom, ad.email, ad.num_tel, " +
                "ex.id AS ex_id, ex.code_barre, ex.etat, ex.statut AS ex_statut, " +
                "pr.id AS pr_id, pr.type_produit " +
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

    /**
     * Gets emprunts by adherent.
     *
     * @param adherent the adherent
     * @return the emprunts by adherent
     * @throws SQLException the sql exception
     */
    public List<Emprunt> getEmpruntsByAdherent(Adherent adherent) throws SQLException {
        // Ajout de pr.type_produit dans le SELECT
        String sql = "SELECT " +
                "emp.id AS emp_id, emp.date_debut, emp.date_retour, emp.statut AS emp_statut, " +
                "ex.id AS ex_id, ex.code_barre, ex.etat, ex.statut AS ex_statut, " +
                "pr.id AS pr_id, pr.type_produit " +
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
                    // 1. Récupération du VRAI Produit grâce à son type
                    int idProduit = rs.getInt("pr_id");
                    String typeProduit = rs.getString("type_produit");
                    Produit produit = null;

                    switch (typeProduit) {
                        case "Livre":
                            produit = LivreDAO.GetByID(idProduit);
                            break;
                        case "DVD":
                            produit = DvdDAO.GetByID(idProduit);
                            break;
                        case "JeuSociete":
                            produit = JeuSocieteDAO.GetByID(idProduit);
                            break;
                        default:
                            throw new SQLException("Type de produit inconnu en base : " + typeProduit);
                    }

                    // 2. Instanciation de l'Exemplaire avec le produit complet
                    int idExemplaire = rs.getInt("ex_id");
                    String codeBarre = rs.getString("code_barre");
                    EnumEtat etatPhysique = EnumEtat.valueOf(rs.getString("etat"));
                    EnumDispo statutExemplaire = EnumDispo.valueOf(rs.getString("ex_statut"));

                    Exemplaire exemplaire = new Exemplaire(idExemplaire, produit, codeBarre, etatPhysique, statutExemplaire);

                    // 3. Création de l'Emprunt
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

    /**
     * Add emprunt.
     *
     * @param adherent   the adherent
     * @param exemplaire the exemplaire
     * @throws SQLException the sql exception
     */
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

    /**
     * Trouver emprunt en cours emprunt.
     *
     * @param codeBarre the code barre
     * @return the emprunt
     * @throws SQLException the sql exception
     */
    public Emprunt trouverEmpruntEnCours(String codeBarre) throws SQLException {
        // Ajout de pr.type_produit à la fin du SELECT
        String sql = "SELECT " +
                "emp.id AS emp_id, emp.date_debut, emp.date_retour, emp.statut AS emp_statut, " +
                "ad.id AS ad_id, ad.nom, ad.prenom, ad.email, ad.num_tel, " +
                "ex.id AS ex_id, ex.code_barre, ex.etat, ex.statut AS ex_statut, " +
                "pr.id AS pr_id, pr.type_produit " +
                "FROM EMPRUNT emp " +
                "INNER JOIN ADHERENT ad ON emp.id_adherent = ad.id " +
                "INNER JOIN EXEMPLAIRE ex ON emp.id_exemplaire = ex.id " +
                "INNER JOIN PRODUIT pr ON ex.id_produit = pr.id " +
                "WHERE ex.code_barre = ? AND emp.statut = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codeBarre);
            stmt.setString(2, EnumDispo.EMPRUNTE.name());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return createEmpruntFromJoin(rs);
                }
            }
        }
        return null;
    }

    /**
     * Save retour.
     *
     * @param emprunt the emprunt
     * @throws SQLException the sql exception
     */
    public void saveRetour(Emprunt emprunt) throws SQLException {
        String sql = "UPDATE EMPRUNT SET statut = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, EnumDispo.RENDU.name());
            stmt.setInt(2, emprunt.getId());

            stmt.executeUpdate();
        }
    }

    // --- LA MÉTHODE COMMUNE DE CRÉATION ---
    private Emprunt createEmpruntFromJoin(ResultSet rs) throws SQLException {

        // 1. Instanciation de l'Adhérent
        String idAdherent = rs.getString("ad_id");
        String nomAdherent = rs.getString("nom");
        String prenomAdherent = rs.getString("prenom");
        String emailAdherent = rs.getString("email");
        String telAdherent = rs.getString("num_tel");

        Adherent adherent = new Adherent(idAdherent, nomAdherent, prenomAdherent, emailAdherent, telAdherent);

        int idProduit = rs.getInt("pr_id");
        String typeProduit = rs.getString("type_produit");
        Produit produit = null;

        switch (typeProduit) {
            case "Livre":
                produit = LivreDAO.GetByID(idProduit);
                break;
            case "DVD":
                produit = DvdDAO.GetByID(idProduit);
                break;
            case "JeuSociete":
                produit = JeuSocieteDAO.GetByID(idProduit);
                break;
            default:
                throw new SQLException("Type de produit inconnu en base : " + typeProduit);
        }

        // 3. Instanciation de l'Exemplaire avec le produit complet
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