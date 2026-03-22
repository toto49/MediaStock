package com.eseo.mediastock.dao;

import com.eseo.mediastock.model.Enum.EnumDispo;
import com.eseo.mediastock.model.Enum.EnumEtat;
import com.eseo.mediastock.model.Exemplaire;
import com.eseo.mediastock.model.Produits.Produit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExemplaireDAO {

    public static List<Exemplaire> getExemplairesByProduit(Produit produit) throws SQLException {
        // Pas besoin de jointure ici, on a déjà l'objet Produit !
        String sql = "SELECT id, code_barre, etat, statut FROM EXEMPLAIRE WHERE id_produit = ?";
        List<Exemplaire> exemplaires = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, produit.getId());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String codeBarre = rs.getString("code_barre");
                    EnumEtat etatPhysique = EnumEtat.valueOf(rs.getString("etat"));
                    EnumDispo statusDispo = EnumDispo.valueOf(rs.getString("statut"));

                    Exemplaire exemplaire = new Exemplaire(id, produit, codeBarre, etatPhysique, statusDispo);
                    exemplaires.add(exemplaire);
                }
            }
        }
        return exemplaires;
    }

    public void addExemplaire(Exemplaire exemplaire) throws SQLException {
        String sql = "INSERT INTO EXEMPLAIRE (code_barre, etat, statut, id_produit) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, exemplaire.getCodeBarre());
            stmt.setString(2, exemplaire.getEtatPhysique().name()); // Utilisation de name()
            stmt.setString(3, exemplaire.getStatusDispo().name());  // Utilisation de name()
            stmt.setInt(4, exemplaire.getProduit().getId());

            stmt.executeUpdate();
        }
    }

    public void updateExemplaire(Exemplaire exemplaire) throws SQLException {
        String sql = "UPDATE EXEMPLAIRE SET statut = ?, etat = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, exemplaire.getStatusDispo().name());
            stmt.setString(2, exemplaire.getEtatPhysique().name());
            stmt.setInt(3, exemplaire.getId());

            stmt.executeUpdate();
        }
    }

    public List<Exemplaire> getAllExemplaires() throws SQLException {
        // Fini le SELECT *, on demande spécifiquement les colonnes utiles
        String sql = "SELECT e.id, e.code_barre, e.etat, e.statut, e.id_produit, p.type_produit " +
                "FROM EXEMPLAIRE e " +
                "INNER JOIN PRODUIT p ON e.id_produit = p.id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            return createExemplaires(rs); // Simplifié pour renvoyer directement le résultat
        }
    }

    public Exemplaire getById(int id) throws SQLException {
        String sql = "SELECT e.id, e.code_barre, e.etat, e.statut, e.id_produit, p.type_produit " +
                "FROM EXEMPLAIRE e " +
                "INNER JOIN PRODUIT p ON e.id_produit = p.id " +
                "WHERE e.id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                List<Exemplaire> list = createExemplaires(rs);
                // Utilisation de isEmpty() et get(0) : compatible avec toutes les versions de Java
                return list.isEmpty() ? null : list.get(0);
            }
        }
    }

    public Exemplaire findByCodeBarre(String codeBarre) throws SQLException {
        String sql = "SELECT e.id, e.code_barre, e.etat, e.statut, e.id_produit, p.type_produit " +
                "FROM EXEMPLAIRE e " +
                "INNER JOIN PRODUIT p ON e.id_produit = p.id " +
                "WHERE e.code_barre = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codeBarre);

            try (ResultSet rs = stmt.executeQuery()) {
                List<Exemplaire> list = createExemplaires(rs);
                return list.isEmpty() ? null : list.get(0);
            }
        }
    }

    private List<Exemplaire> createExemplaires(ResultSet rs) throws SQLException {
        List<Exemplaire> exemplaires = new ArrayList<>();

        // On instancie les DAO une seule fois en dehors de la boucle
        LivreDAO livreDAO = new LivreDAO();
        DvdDAO dvdDAO = new DvdDAO();
        JeuSocieteDAO jeuSocieteDAO = new JeuSocieteDAO();

        while (rs.next()) {
            int id = rs.getInt("id");
            String codeBarre = rs.getString("code_barre");
            EnumEtat etatPhysique = EnumEtat.valueOf(rs.getString("etat"));
            EnumDispo statusDispo = EnumDispo.valueOf(rs.getString("statut"));

            int idProduit = rs.getInt("id_produit");
            String typeProduit = rs.getString("type_produit");
            Produit produit = null;

            switch (typeProduit) {
                case "Livre":
                    produit = LivreDAO.GetByID(idProduit); // Idéalement, renomme aussi ces méthodes en getById() !
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

            Exemplaire exemplaire = new Exemplaire(id, produit, codeBarre, etatPhysique, statusDispo);
            exemplaires.add(exemplaire);
        }
        return exemplaires;
    }

    public void deleteExemplaire(Exemplaire exemplaire) throws SQLException {
        String sql = "UPDATE EXEMPLAIRE SET statut = 'RETIRE' WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, exemplaire.getId());
            stmt.executeUpdate();
            exemplaire.setStatusDispo(com.eseo.mediastock.model.Enum.EnumDispo.RETIRE);
        }
    }
}