package com.eseo.mediastock.service;

import com.eseo.mediastock.dao.DvdDAO;
import com.eseo.mediastock.dao.ExemplaireDAO;
import com.eseo.mediastock.dao.JeuSocieteDAO;
import com.eseo.mediastock.dao.LivreDAO;
import com.eseo.mediastock.model.Enum.EnumDispo;
import com.eseo.mediastock.model.Enum.EnumEtat;
import com.eseo.mediastock.model.Exemplaire;
import com.eseo.mediastock.model.Produits.DVD;
import com.eseo.mediastock.model.Produits.JeuSociete;
import com.eseo.mediastock.model.Produits.Livre;
import com.eseo.mediastock.model.Produits.Produit;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StockService {

    ExemplaireDAO exemplaireDAO = new ExemplaireDAO();

    // --- AJOUT DES PRODUITS ---

    public void ajouterLivre(String titre, String description, String editeur, int annee, String isbn, String auteur, int nbPages, String format, int nbExemplaires) throws SQLException {
        if (titre == null || titre.trim().isEmpty()) throw new IllegalArgumentException("Le titre est obligatoire");
        Livre livre = new Livre();
        livre.setTitre(titre);
        livre.setDescription(description);
        livre.setEditeur(editeur);
        livre.setAnneeSortie(annee);
        livre.setIsbn(isbn);
        livre.setAuteur(auteur);
        livre.setNbPages(nbPages);
        livre.setFormat(format);

        LivreDAO.addProduit(livre);

        for (int i = 0; i < nbExemplaires; i++) {
            ajouterExemplaire(livre);
        }
    }

    public void ajouterDVD(String titre, String description, String editeur, int annee, String realisateur, int duree, List<String> audio, List<String> sousTitres, int nbExemplaires) throws SQLException {
        if (titre == null || titre.trim().isEmpty()) throw new IllegalArgumentException("Le titre est obligatoire");

        DVD dvd = new DVD();
        dvd.setTitre(titre);
        dvd.setDescription(description);
        dvd.setEditeur(editeur);
        dvd.setAnneeSortie(annee);
        dvd.setRealisateur(realisateur);
        dvd.setDureeMinutes(duree);
        dvd.setAudioLangues(audio);

        DvdDAO.addProduit(dvd);
        for (int i = 0; i < nbExemplaires; i++) {
            ajouterExemplaire(dvd);
        }

    }

    public void ajouterJeuSociete(String titre, String description, String editeur, int annee, int nbJoueursMin, int nbJoueursMax, int ageMin, int dureePartie, int nbExemplaires) throws SQLException {
        if (titre == null || titre.trim().isEmpty()) throw new IllegalArgumentException("Le titre est obligatoire");

        JeuSociete jeu = new JeuSociete();
        jeu.setTitre(titre);
        jeu.setDescription(description);
        jeu.setEditeur(editeur);
        jeu.setAnneeSortie(annee);
        jeu.setNbJoueursMin(nbJoueursMin);
        jeu.setNbJoueursMax(nbJoueursMax);
        jeu.setAgeMin(ageMin);
        jeu.setDureePartie(dureePartie);

        JeuSocieteDAO.addProduit(jeu);
        for (int i = 0; i < nbExemplaires; i++) {
            ajouterExemplaire(jeu);
        }
    }

    public void modifierProduit(Produit produit) throws SQLException {
        if (produit == null || produit.getId() == 0) {
            throw new IllegalArgumentException("Le produit à modifier n'est pas valide.");
        }

        if (produit instanceof Livre) {
            LivreDAO.updateProduit(produit);
        } else if (produit instanceof DVD) {
            DvdDAO.updateProduit(produit);
        } else if (produit instanceof JeuSociete) {
            JeuSocieteDAO.updateProduit(produit);
        } else {
            throw new IllegalArgumentException("Type de produit non pris en charge.");
        }
    }

    public void supprimerProduit(Produit produit) throws SQLException {
        if (produit == null || produit.getId() == 0) {
            throw new IllegalArgumentException("Le produit à supprimer n'est pas valide.");
        }

        if (produit instanceof Livre) {
            LivreDAO.deleteProduit(produit);
        } else if (produit instanceof DVD) {
            DvdDAO.deleteProduit(produit);
        } else if (produit instanceof JeuSociete) {
            JeuSocieteDAO.deleteProduit(produit);
        } else {
            throw new IllegalArgumentException("Type de produit non pris en charge.");
        }
    }

    public int getCodeType(Produit produit) {
        if (produit instanceof Livre) return 1;
        if (produit instanceof DVD) return 2;
        if (produit instanceof JeuSociete) return 3;
        return 9;
    }

    // --- CODE BARRE ---

    private int calculerCleEAN13(String code12) {
        int somme = 0;
        for (int i = 0; i < code12.length(); i++) {
            int chiffre = Character.getNumericValue(code12.charAt(i));
            int poids = (i % 2 == 0) ? 1 : 3;
            somme += chiffre * poids;
        }
        int reste = somme % 10;
        return (reste == 0) ? 0 : (10 - reste);
    }

    public String creerCodeBarreUnique(Produit produit) {
        int type = getCodeType(produit);
        long randomPart = (long) (Math.random() * 9_000_000_000L);

        String codeSansCle = String.format("2%d%010d", type, randomPart);
        int key = calculerCleEAN13(codeSansCle);

        return codeSansCle + key;
    }

    // --- GESTION DES EXEMPLAIRES ---

    public void ajouterExemplaire(Produit produit) throws SQLException {
        Exemplaire ex = new Exemplaire();
        ex.setProduit(produit);

        String codeBarre = creerCodeBarreUnique(produit);
        ex.setCodeBarre(codeBarre);

        ex.setEtatPhysique(EnumEtat.NEUF);
        ex.setStatusDispo(EnumDispo.DISPONIBLE);

        exemplaireDAO.addExemplaire(ex);
    }

    public void modifierExemplaire(Exemplaire exemplaire) throws SQLException {
        if (exemplaire == null || exemplaire.getId() == 0) {
            throw new IllegalArgumentException("L'exemplaire à modifier n'est pas valide.");
        }
        exemplaireDAO.updateExemplaire(exemplaire);
    }

    public void supprimerExemplaire(Exemplaire exemplaire) throws SQLException {
        if (exemplaire == null || exemplaire.getId() == 0) {
            throw new IllegalArgumentException("L'exemplaire à supprimer n'est pas valide.");
        }
        exemplaireDAO.deleteExemplaire(exemplaire);
    }

    // --- RECHERCHER PRODUITS ---
    public List<Produit> SearchProduit(String searchbar, String typeProduit) throws SQLException {
        List<Produit> produits = new ArrayList<>();
        List<? extends Produit> Allproducts = new ArrayList<>();

        if (typeProduit.equals("DVD")) {
            Allproducts = DvdDAO.ProduitObjectList();
        } else if (typeProduit.equals("Jeu")) {
            Allproducts = JeuSocieteDAO.ProduitObjectList();
        } else if (typeProduit.equals("Livre")) {
            Allproducts = LivreDAO.ProduitObjectList();
        }

        for (Produit produit : Allproducts) {
            if (produit.getTitre().toLowerCase().contains(searchbar.toLowerCase())) {
                produits.add(produit);
            }
        }
        return produits;
    }

    // --- LISTE EXEMPLAIRES PRODUITS
    public List<Exemplaire> getExemplaireFromProduct(Produit produit) {
        return produit.getExemplaires();
    }

    // --- STATISTIQUES ---

    public int getNombreTotalLivres() {
        try {
            LivreDAO livreDAO = new LivreDAO();
            return livreDAO.countLivres();
        } catch (SQLException e) {
            return 0;
        }
    }

    public int getNombreTotalDVDs() {
        try {
            DvdDAO dvdDAO = new DvdDAO();
            return dvdDAO.countDVDs();
        } catch (SQLException e) {
            return 0;
        }
    }

    public int getNombreTotalJeux() {
        try {
            JeuSocieteDAO jeuxDAO = new JeuSocieteDAO();
            return jeuxDAO.countJeux();
        } catch (SQLException e) {
            return 0;
        }
    }

    public Exemplaire getExemplaireParCodeBarre(String codeBarre) throws SQLException {
        return exemplaireDAO.findByCodeBarre(codeBarre);
    }
}