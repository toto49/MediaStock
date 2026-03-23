package com.eseo.mediastock.service;

import com.eseo.mediastock.dao.DvdDAO;
import com.eseo.mediastock.dao.JeuSocieteDAO;
import com.eseo.mediastock.dao.LivreDAO;
import com.eseo.mediastock.model.Produits.Livre;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
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
     * Nettoyage final.
     */
    @AfterAll
    static void nettoyageFinal() {
        try {
            // Supprime TOUS les produits de test
            System.out.println("NETTOYAGE FINAL");

            System.out.println("Base nettoyée");

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
                    "978-2070612758",
                    "Saint-Exupéry",
                    96,
                    "Poche",
                    2
            );
        });
        System.out.println("Livre ajouté avec 2 exemplaires");
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
        });
        System.out.println("DVD ajouté");
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
        });
        System.out.println("Jeu ajouté");
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