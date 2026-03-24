package com.eseo.mediastock.service;

import com.eseo.mediastock.dao.DvdDAO;
import com.eseo.mediastock.dao.JeuSocieteDAO;
import com.eseo.mediastock.dao.LivreDAO;
import com.eseo.mediastock.model.Produits.DVD;
import com.eseo.mediastock.model.Produits.JeuSociete;
import com.eseo.mediastock.model.Produits.Livre;
import com.eseo.mediastock.model.Produits.Produit;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The type Stock service test.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StockServiceTest {

    private static StockService stockService;
    private static LivreDAO livreDAO;
    private static DvdDAO dvdDAO;
    private static JeuSocieteDAO jeuDAO;

    // Listes pour stocker les produits créés
    private static List<Livre> livresCrees = new ArrayList<>();
    private static List<DVD> dvdsCrees = new ArrayList<>();
    private static List<JeuSociete> jeuxCrees = new ArrayList<>();

    /**
     * Sets .
     */
    @BeforeAll
    static void setup() {
        stockService = new StockService();
        livreDAO = new LivreDAO();
        dvdDAO = new DvdDAO();
        jeuDAO = new JeuSocieteDAO();
        System.out.println("TESTS STOCK SERVICE");
    }

    /**
     * Nettoyage final - Supprime tous les produits créés pendant les tests.
     */
    @AfterAll
    static void nettoyageFinal() {
        try {
            System.out.println("\nNETTOYAGE FINAL");
            int compteurSuppression = 0;

            // Supprimer tous les livres créés
            for (Livre livre : livresCrees) {
                try {
                    LivreDAO.deleteProduit(livre);
                    System.out.println("✓ Livre supprimé (ID: " + livre.getId() + ", Titre: " + livre.getTitre() + ")");
                    compteurSuppression++;
                } catch (Exception e) {
                    System.out.println("✗ Erreur suppression livre ID " + livre.getId() + ": " + e.getMessage());
                }
            }

            // Supprimer tous les DVDs créés
            for (DVD dvd : dvdsCrees) {
                try {
                    DvdDAO.deleteProduit(dvd);
                    System.out.println("✓ DVD supprimé (ID: " + dvd.getId() + ", Titre: " + dvd.getTitre() + ")");
                    compteurSuppression++;
                } catch (Exception e) {
                    System.out.println("✗ Erreur suppression DVD ID " + dvd.getId() + ": " + e.getMessage());
                }
            }

            // Supprimer tous les jeux créés
            for (JeuSociete jeu : jeuxCrees) {
                try {
                    JeuSocieteDAO.deleteProduit(jeu);
                    System.out.println("✓ Jeu supprimé (ID: " + jeu.getId() + ", Titre: " + jeu.getTitre() + ")");
                    compteurSuppression++;
                } catch (Exception e) {
                    System.out.println("✗ Erreur suppression jeu ID " + jeu.getId() + ": " + e.getMessage());
                }
            }

            System.out.println("Base nettoyée - " + compteurSuppression + " produit(s) supprimé(s)");

            // Vider les listes après nettoyage
            livresCrees.clear();
            dvdsCrees.clear();
            jeuxCrees.clear();

        } catch (Exception e) {
            System.out.println("Erreur nettoyage: " + e.getMessage());
        }
    }

    /**
     * Test ajouter livre.
     */
    @Test
    @Order(1)
    void testAjouterLivre() {
        System.out.println("Test 1: Ajouter un livre");

        assertDoesNotThrow(() -> {
            stockService.ajouterLivre(
                    "Le Petit Prince",
                    "Conte philosophique",
                    "Gallimard",
                    1943,
                    "972070612",
                    "Saint-Exupéry",
                    96,
                    "Poche",
                    2
            );

            // Récupérer le livre fraîchement ajouté
            List<Livre> livres = LivreDAO.ProduitObjectList();
            for (Livre livre : livres) {
                if ("Le Petit Prince".equals(livre.getTitre())) {
                    livresCrees.add(livre);
                    System.out.println("Livre ajouté avec 2 exemplaires (ID: " + livre.getId() + ")");
                    break;
                }
            }
        });
    }

    /**
     * Test ajouter dvd.
     */
    @Test
    @Order(2)
    void testAjouterDVD() {
        System.out.println("Test 2: Ajouter un DVD");

        assertDoesNotThrow(() -> {
            stockService.ajouterDVD(
                    "Inception",
                    "Film",
                    "Warner Bros",
                    2010,
                    "Nolan",
                    148,
                    List.of("Français", "Anglais"),
                    List.of("Français"),
                    1
            );

            // Récupérer le DVD fraîchement ajouté
            List<DVD> dvds = DvdDAO.ProduitObjectList();
            for (DVD dvd : dvds) {
                if ("Inception".equals(dvd.getTitre())) {
                    dvdsCrees.add(dvd);
                    System.out.println("DVD ajouté (ID: " + dvd.getId() + ")");
                    break;
                }
            }
        });
    }

    /**
     * Test ajouter jeu.
     */
    @Test
    @Order(3)
    void testAjouterJeu() {
        System.out.println("Test 3: Ajouter un jeu");

        assertDoesNotThrow(() -> {
            stockService.ajouterJeuSociete(
                    "Monopoly",
                    "Jeu classique",
                    "Hasbro",
                    1935,
                    2, 6, 8, 120, 1
            );

            // Récupérer le jeu fraîchement ajouté
            List<JeuSociete> jeux = JeuSocieteDAO.ProduitObjectList();
            for (JeuSociete jeu : jeux) {
                if ("Monopoly".equals(jeu.getTitre())) {
                    jeuxCrees.add(jeu);
                    System.out.println("Jeu ajouté (ID: " + jeu.getId() + ")");
                    break;
                }
            }
        });
    }

    /**
     * Test get code type.
     */
    @Test
    @Order(4)
    void testGetCodeType() {
        System.out.println("Test 4: Code type produit");

        Livre livre = new Livre();
        int code = stockService.getCodeType(livre);

        assertEquals(1, code);
        System.out.println("Code type: " + code);
    }

    /**
     * Test get nombre total livres.
     */
    @Test
    @Order(5)
    void testGetNombreTotalLivres() {
        System.out.println("Test 5: Statistiques");

        int nbLivres = stockService.getNombreTotalLivres();
        System.out.println("Livres en base: " + nbLivres);
        assertTrue(nbLivres >= 0);
    }

    /**
     * Test search.
     *
     * @throws SQLException the sql exception
     */
    @Test
    @Order(6)
    void testSearch() throws SQLException {
        System.out.println("Test 6: Recherche");

        var resultats = stockService.SearchProduit("Petit", "Livre");
        assertNotNull(resultats);
        System.out.println("Résultats: " + resultats.size());
    }
}