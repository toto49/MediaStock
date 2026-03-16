package com.eseo.mediastock.dao;

import com.eseo.mediastock.model.Enum.EnumDispo;
import com.eseo.mediastock.model.Enum.EnumEtat;
import com.eseo.mediastock.model.Exemplaire;
import com.eseo.mediastock.model.Produits.Livre;
import com.eseo.mediastock.model.Produits.Produit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExemplaireDAO {

    public static List<Exemplaire> getExemplairesByProduit(Produit produit) throws SQLException {
        String sql = "SELECT * FROM EXEMPLAIRE WHERE id_produit = ?";
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
            stmt.setString(2, exemplaire.getEtatPhysique().toString());
            stmt.setString(3, exemplaire.getStatusDispo().toString());
            stmt.setInt(4, exemplaire.getProduit().getId());

            stmt.executeUpdate();
        }
    }

    public void updateExemplaire(Exemplaire exemplaire) throws SQLException {
        String sql = "UPDATE EXEMPLAIRE SET statut = ?, etat = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // L'utilisation de name() est plus sûre que toString() pour enregistrer les Enums en BDD
            stmt.setString(1, exemplaire.getStatusDispo().name());
            stmt.setString(2, exemplaire.getEtatPhysique().name());
            stmt.setInt(3, exemplaire.getId());

            stmt.executeUpdate();
        }
    }

    public List<Exemplaire> getAllExemplaires() throws SQLException {
        String sql = "SELECT e.*, p.type_produit FROM EXEMPLAIRE e JOIN PRODUIT p ON e.id_produit = p.id";
        List<Exemplaire> exemplaires = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            exemplaires = createExemplaires(rs);
        }

        return exemplaires;
    }

    public Exemplaire GetByID(int id) throws SQLException {
        Exemplaire exemplaire = null;
        // CORRECTION : Ajout de la jointure pour pouvoir récupérer le 'type_produit'
        String sql = "SELECT e.*, p.type_produit FROM EXEMPLAIRE e JOIN PRODUIT p ON e.id_produit = p.id WHERE e.id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                List<Exemplaire> list = createExemplaires(rs);
                if (!list.isEmpty()) {
                    exemplaire = list.getFirst(); // Récupère le premier élément
                }
            }
        }

        return exemplaire;
    }

    // --- NOUVELLE MÉTHODE --- (Nécessaire pour le EmpruntService)
    public Exemplaire findByCodeBarre(String codeBarre) throws SQLException {
        Exemplaire exemplaire = null;
        // Jointure obligatoire ici aussi
        String sql = "SELECT e.*, p.type_produit FROM EXEMPLAIRE e JOIN PRODUIT p ON e.id_produit = p.id WHERE e.code_barre = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codeBarre);

            try (ResultSet rs = stmt.executeQuery()) {
                List<Exemplaire> list = createExemplaires(rs);
                if (!list.isEmpty()) {
                    exemplaire = list.getFirst();
                }
            }
        }
        return exemplaire;
    }

    // --- TA MÉTHODE UTILITAIRE (inchangée) ---
    public List<Exemplaire> createExemplaires(ResultSet rs) throws SQLException {
        List<Exemplaire> exemplaires = new ArrayList<>();

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
                    produit = LivreDAO.GetByID(idProduit);
                    break;
                case "DVD":
                    produit = DvdDAO.GetByID(idProduit);
                    break;
                case "JeuSociete": // Vérifie bien que c'est l'orthographe exacte dans ta base de données
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

}