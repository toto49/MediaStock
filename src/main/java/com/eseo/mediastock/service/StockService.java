package com.eseo.mediastock.service;

import com.eseo.mediastock.dao.DvdDAO;
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

    // --- AJOUT DES PRODUITS ---

    public void ajouterLivre(String titre, String description, String editeur, int annee, String isbn, String auteur, int nbPages, String format) throws SQLException {
        if (titre == null || titre.trim().isEmpty()) throw new IllegalArgumentException("Le titre est obligatoire");

        Livre livre = new Livre();
        // Champs parents (Produit)
        livre.setTitre(titre);
        livre.setDescription(description);
        livre.setEditeur(editeur);
        livre.setAnneeSortie(annee);
        // Champs enfants (Livre)
        livre.setIsbn(isbn);
        livre.setAuteur(auteur);
        livre.setNbPages(nbPages);
        livre.setFormat(format);

        LivreDAO.addProduit(livre);
    }

    public void ajouterDVD(String titre, String description,String editeur, int annee, String realisateur, int duree, List<String> audio,List<String> sousTitres) throws SQLException {
        if (titre == null || titre.trim().isEmpty()) throw new IllegalArgumentException("Le titre est obligatoire");

        DVD dvd = new DVD();
        // Champs parents
        dvd.setTitre(titre);
        dvd.setDescription(description);
        dvd.setEditeur(editeur);
        dvd.setAnneeSortie(annee);
        // Champs enfants
        dvd.setRealisateur(realisateur);
        dvd.setDureeMinutes(duree);
        dvd.setAudioLangues(audio);

        DvdDAO.addProduit(dvd);
    }

    public void ajouterJeuSociete(String titre, String description,String editeur, int annee, int nbJoueursMin, int nbJoueursMax, int ageMin, int dureePartie) throws SQLException {
        if (titre == null || titre.trim().isEmpty()) throw new IllegalArgumentException("Le titre est obligatoire");

        JeuSociete jeu = new JeuSociete();
        // Champs parents
        jeu.setTitre(titre);
        jeu.setDescription(description);
        jeu.setEditeur(editeur);
        jeu.setAnneeSortie(annee);
        // Champs enfants
        jeu.setNbJoueursMin(nbJoueursMin);
        jeu.setNbJoueursMax(nbJoueursMax);
        jeu.setAgeMin(ageMin);
        jeu.setDureePartie(dureePartie);

        JeuSocieteDAO.addProduit(jeu);
    }

    // --- GESTION DES TYPES ---

    public int getCodeType(Produit produit){
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

    /**
     * Génère un code unique pour un EXEMPLAIRE
     * Format : 2 + Type (1) + Aléatoire (10) + Clé (1)
     */
    public String creerCodeBarreUnique(Produit produit){
        int type = getCodeType(produit);
        long randomPart = (long) (Math.random() * 9_000_000_000L);

        String codeSansCle = String.format("2%d%010d", type, randomPart);
        int key = calculerCleEAN13(codeSansCle);

        return codeSansCle + key;
    }

    // --- GESTION DES EXEMPLAIRES ---

    public void ajouterExemplaire(Produit produit){

        // On crée l'exemplaire physique
        Exemplaire ex = new Exemplaire();
        ex.setProduit(produit);

        // On génère son étiquette unique
        String codeBarre = creerCodeBarreUnique(produit);
        ex.setCodeBarre(codeBarre);

        // Valeurs par défaut obligatoires
        ex.setEtatPhysique(EnumEtat.NEUF);
        ex.setStatusDispo(EnumDispo.DISPONIBLE);

        // TODO : Sauvegarder dans la BDD via ExemplaireDAO stp morgiane (fait juste la fonction dans tes fichiers je m'occupe de l'appeler)
    }

    public void changerStatus(Exemplaire exemplaire, EnumDispo newStatus){
        exemplaire.setStatusDispo(newStatus);
        // TODO : Update en BDD stp morgiane (fait juste la fonction dans tes fichiers je m'occupe de l'appeler)
    }

    // --- RECHERCHER PRODUITS ---
    public List<Produit> SearchProduit (String searchbar,String typeProduit) throws SQLException {
        List<Produit> produits = new ArrayList<>();
        List<? extends Produit> Allproducts = new ArrayList<>();

        if (typeProduit.equals("DVD")){
            Allproducts = DvdDAO.ProduitObjectList();
        }else if (typeProduit.equals("Jeu")){
            Allproducts = JeuSocieteDAO.ProduitObjectList();
        }else if (typeProduit.equals("Livre")){
            Allproducts = LivreDAO.ProduitObjectList();
        }

        for (Produit produit : Allproducts){
            if (produit.getTitre().toLowerCase().contains(searchbar.toLowerCase())){
                produits.add(produit);
            }
        }
        return produits;
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
}