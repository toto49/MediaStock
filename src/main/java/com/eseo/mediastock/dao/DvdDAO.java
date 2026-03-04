package com.eseo.mediastock.dao;

import com.eseo.mediastock.model.Exemplaire;
import com.eseo.mediastock.model.Produits.DVD;
import com.eseo.mediastock.model.Produits.Livre;
import com.eseo.mediastock.model.Produits.Produit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DvdDAO implements ProduitDAO {

    @Override
    public void addProduit(Produit p) throws SQLException {
        DVD d = (DVD) p;
        String sql = "INSERT INTO PRODUIT (titre, description, editeur, annee_sortie, realisateur, duree_minutes, audio_langues, sous_titres) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, d.getTitre());
            stmt.setString(2, d.getDescription());
            stmt.setString(3, d.getEditeur());
            stmt.setInt(4, d.getAnneeSortie());
            stmt.setString(5, d.getRealisateur());
            stmt.setInt(6, d.getDureeMinutes());
            stmt.setString(7, String.join(",", d.getAudioLangues()));
            stmt.setString(8, d.getSousTitres() == null ? null : String.join(",", d.getSousTitres()));
            stmt.executeUpdate();
        }
    }

    public List<DVD> ProduitObjectList() throws SQLException {
        String sql = "SELECT * FROM PRODUIT WHERE type_produit = ?";
        List<DVD> dvds;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "DVD");

            try (ResultSet rs = stmt.executeQuery()) {
                // On transforme le ResultSet en liste d'objets AVANT de fermer la connexion
                dvds = CreateLivres(rs);
            }
        }

        return dvds;
    }

    private List<DVD> CreateLivres(ResultSet rs) throws SQLException {
        List<DVD> dvds = new ArrayList<>();
        while (rs.next()) {
            int id = rs.getInt("id_produit");
            String titre = rs.getString("titre");
            String description = rs.getString("description");
            String editeur = rs.getString("editeur");
            int anneeSortie = rs.getInt("annee_sortie");

            String realisateur = rs.getString("realisateur");
            int dureeMinutes = rs.getInt("duree_minutes");

            // Conversion du String de la base de données en List<String>
            String rawAudio = rs.getString("audio_langues");
            List<String> audioLangues = (rawAudio == null || rawAudio.isEmpty()) ? new ArrayList<>() : new ArrayList<>(Arrays.asList(rawAudio.split(",")));

            String rawSub = rs.getString("sous_titres");
            List<String> sousTitres = (rawSub == null || rawSub.isEmpty()) ? new ArrayList<>() : new ArrayList<>(Arrays.asList(rawSub.split(",")));

            // EN attendant
            List<Exemplaire> exemplaires = null;

            // Création de l'objet
            DVD dvd = new DVD(id, titre, description, editeur, anneeSortie , exemplaires,realisateur, dureeMinutes, audioLangues, sousTitres);
            dvds.add(dvd);
        }
        return dvds;
    }
}
