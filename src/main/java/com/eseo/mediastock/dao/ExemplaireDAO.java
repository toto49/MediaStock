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

            stmt.setString(1, exemplaire.getStatusDispo().toString());
            stmt.setString(2, exemplaire.getEtatPhysique().toString());
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

            // On transforme le ResultSet en liste d'objets
            exemplaires = createExemplaires(rs);
        }

        return exemplaires;
    }

    public Exemplaire GetByID(int id) throws SQLException {
        Exemplaire exemplaire = null;
        String sql = "SELECT * FROM EXEMPLAIRE WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                exemplaire = createExemplaires(rs).getFirst();
            }
        }

        return exemplaire;
    }

    public List<Exemplaire> createExemplaires(ResultSet rs) throws SQLException {
        List<Exemplaire> exemplaires = new ArrayList<>();

        while (rs.next()) {
            // 1. Récupération des données basiques
            int id = rs.getInt("id");
            String codeBarre = rs.getString("code_barre");

            // 3. Récupération et conversion des Enums
            EnumEtat etatPhysique = EnumEtat.valueOf(rs.getString("etat"));
            EnumDispo statusDispo = EnumDispo.valueOf(rs.getString("statut"));

            // 4. Gestion de l'objet Produit
            int idProduit = rs.getInt("id_produit");
            String typeProduit = rs.getString("type_produit");
            Produit produit = null;

            // On choisit le bon DAO en fonction du type récupéré en base
            switch (typeProduit) {
                case "Livre":
                    produit = LivreDAO.GetByID(idProduit);
                    break;
                case "DVD":
                    produit = DvdDAO.GetByID(idProduit);
                    break;
                case "JeuSociete": // ou "Jeu", voir note ci-dessous !
                    produit = JeuSocieteDAO.GetByID(idProduit);
                    break;
                default:
                    throw new SQLException("Type de produit inconnu en base : " + typeProduit);
            }

            // 5. Création de l'objet
            Exemplaire exemplaire = new Exemplaire(id, produit, codeBarre, etatPhysique, statusDispo);

            // 6. Ajout à la liste
            exemplaires.add(exemplaire);
        }
        return exemplaires;
    }


}
