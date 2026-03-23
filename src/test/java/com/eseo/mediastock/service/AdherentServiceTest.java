package com.eseo.mediastock.service;

import com.eseo.mediastock.dao.AdherentDAO;
import com.eseo.mediastock.model.Adherent;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The type Adherent service test.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AdherentServiceTest {

    // Compteur pour des IDs uniques
    private static final AtomicInteger compteur = new AtomicInteger(1);
    // Liste pour stocker les IDs créés pendant les tests
    private static final List<String> idsACleaner = new ArrayList<>();
    private static AdherentService adherentService;
    private static AdherentDAO adherentDAO;
    private static String idTest;

    /**
     * Sets .
     */
    @BeforeAll
    static void setup() {
        adherentService = new AdherentService();
        adherentDAO = new AdherentDAO();
        System.out.println("=== INITIALISATION DES TESTS ===");
    }

    /**
     * Nettoyage.
     *
     * @throws SQLException the sql exception
     */
    @AfterAll
    static void nettoyage() throws SQLException {
        System.out.println(" NETTOYAGE AUTOMATIQUE");
        System.out.println("Suppression des " + idsACleaner.size() + " adhérents de test...");

        int supprime = 0;
        int erreurs = 0;

        for (String id : idsACleaner) {
            try {
                if (adherentDAO.deleteAdherent(id)) {
                    System.out.println(" Supprimé: " + id);
                    supprime++;
                } else {
                    System.out.println("Non trouvé: " + id);
                    erreurs++;
                }
            } catch (Exception e) {
                System.out.println("Erreur pour " + id + ": " + e.getMessage());
                erreurs++;
            }
        }

        System.out.println("BILAN NETTOYAGE");
        System.out.println("  Supprimés: " + supprime);
        System.out.println("  Erreurs: " + erreurs);
        System.out.println("  Total: " + idsACleaner.size());

        idsACleaner.clear();
    }

    private String genererEmailUnique() {
        return "test" + compteur.getAndIncrement() + "@email.com";
    }

    /**
     * Test generate num adherent.
     */
    @Test
    @Order(1)
    void testGenerateNumAdherent() {
        System.out.println(" TEST 1: generateNumAdherent");

        String numAdherent = adherentService.generateNumAdherent();

        assertNotNull(numAdherent);
        assertTrue(numAdherent.startsWith("ADH-"), "Devrait commencer par ADH-");

        int anneeCourante = LocalDate.now().getYear();
        assertTrue(numAdherent.contains(String.valueOf(anneeCourante)),
                "Devrait contenir l'année courante");

        // Vérifie le format: ADH-YYYY-XXX
        String[] parties = numAdherent.split("-");
        assertEquals(3, parties.length, "Format devrait être ADH-YYYY-XXX");
        assertEquals("ADH", parties[0]);
        assertEquals(String.valueOf(anneeCourante), parties[1]);
        assertEquals(3, parties[2].length(), "Le numéro de séquence doit avoir 3 chiffres");

        System.out.println("Numéro généré: " + numAdherent);
    }

    /**
     * Test inscrire adherent.
     *
     * @throws SQLException the sql exception
     */
    @Test
    @Order(2)
    void testInscrireAdherent() throws SQLException {
        System.out.println(" TEST 2: inscrireAdherent");

        String nom = "Martin";
        String prenom = "Sophie";
        String email = genererEmailUnique();
        String telephone = "0612345678";

        // Utilise le service pour inscrire
        adherentService.inscrireAdherent(nom, prenom, email, telephone);

        // Récupère l'adhérent par email
        List<Adherent> tous = adherentDAO.findAll();
        Adherent trouve = null;

        for (Adherent a : tous) {
            if (email.equals(a.getEmailContact())) {
                trouve = a;
                break;
            }
        }

        assertNotNull(trouve, "L'adhérent devrait avoir été créé");
        assertEquals(nom, trouve.getNom());
        assertEquals(prenom, trouve.getPrenom());
        assertEquals(email, trouve.getEmailContact());
        assertEquals(telephone, trouve.getNumTel());

        // Vérifie le format du numéro d'adhérent
        String numAdherent = trouve.getId();
        assertTrue(numAdherent.startsWith("ADH-"), "ID devrait commencer par ADH-");

        // Ajoute à la liste de nettoyage
        idsACleaner.add(trouve.getId());
        idTest = trouve.getId();

        System.out.println(" Adhérent créé avec ID: " + trouve.getId());
        System.out.println(" Email: " + trouve.getEmailContact());
    }

    /**
     * Test inscrire adherent donnees speciales.
     *
     * @throws SQLException the sql exception
     */
    @Test
    @Order(3)
    void testInscrireAdherentDonneesSpeciales() throws SQLException {
        System.out.println("TEST 3: inscrireAdherent - données spéciales");

        String nom = "Dupont-Étienne";
        String prenom = "Jean-Claude";
        String email = genererEmailUnique();
        String telephone = "+33 6 12 34 56 78";

        adherentService.inscrireAdherent(nom, prenom, email, telephone);

        // Récupère et vérifie
        List<Adherent> tous = adherentDAO.findAll();
        Adherent trouve = null;

        for (Adherent a : tous) {
            if (email.equals(a.getEmailContact())) {
                trouve = a;
                break;
            }
        }

        assertNotNull(trouve, "L'adhérent devrait avoir été créé");
        assertEquals(nom, trouve.getNom());
        assertEquals(prenom, trouve.getPrenom());
        assertEquals(telephone, trouve.getNumTel());

        idsACleaner.add(trouve.getId());

        System.out.println(" Adhérent avec données spéciales créé: " + trouve.getId());
    }

    /**
     * Test inscriptions multiples.
     *
     * @throws SQLException the sql exception
     */
    @Test
    @Order(4)
    void testInscriptionsMultiples() throws SQLException {
        System.out.println(" TEST 4: inscriptions multiples");

        String[] noms = {"Dupont", "Martin", "Bernard"};
        String[] prenoms = {"Jean", "Sophie", "Pierre"};
        String[] telephones = {"0102030405", "0203040506", "0304050607"};

        List<String> emails = new ArrayList<>();

        for (int i = 0; i < noms.length; i++) {
            String email = genererEmailUnique();
            emails.add(email);
            adherentService.inscrireAdherent(noms[i], prenoms[i], email, telephones[i]);
            System.out.println("  Création " + (i + 1) + ": " + noms[i] + " " + prenoms[i]);
        }

        // Vérifie que tous ont été créés
        List<Adherent> tous = adherentDAO.findAll();

        for (String email : emails) {
            boolean trouve = false;
            for (Adherent a : tous) {
                if (email.equals(a.getEmailContact())) {
                    trouve = true;
                    idsACleaner.add(a.getId());
                    break;
                }
            }
            assertTrue(trouve, "L'email " + email + " devrait exister");
        }

        System.out.println("  ✓ " + noms.length + " adhérents créés avec succès");
    }

    /**
     * Test numeros uniques.
     *
     * @throws SQLException the sql exception
     */
    @Test
    @Order(5)
    void testNumerosUniques() throws SQLException {
        System.out.println("TEST 5: numéros uniques");

        // Crée plusieurs adhérents
        List<String> idsCrees = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            String email = genererEmailUnique();
            adherentService.inscrireAdherent(
                    "Nom" + i,
                    "Prenom" + i,
                    email,
                    "000000000" + i
            );

            // Récupère l'ID du dernier créé
            List<Adherent> tous = adherentDAO.findAll();
            for (Adherent a : tous) {
                if (email.equals(a.getEmailContact())) {
                    idsCrees.add(a.getId());
                    idsACleaner.add(a.getId());
                    break;
                }
            }
        }

        // Vérifie que les IDs sont différents
        for (int i = 0; i < idsCrees.size(); i++) {
            for (int j = i + 1; j < idsCrees.size(); j++) {
                assertNotEquals(idsCrees.get(i), idsCrees.get(j),
                        "Les IDs devraient être uniques");
            }
        }

        System.out.println("  ✓ Tous les numéros sont uniques:");
        for (String id : idsCrees) {
            System.out.println("     - " + id);
        }
    }

    /**
     * Test recuperation apres creation.
     *
     * @throws SQLException the sql exception
     */
    @Test
    @Order(6)
    void testRecuperationApresCreation() throws SQLException {
        System.out.println("TEST 6: récupération après création");

        String nom = "Recuperation";
        String prenom = "Test";
        String email = genererEmailUnique();
        String telephone = "0999999999";

        adherentService.inscrireAdherent(nom, prenom, email, telephone);

        // Retrouve par email
        List<Adherent> tous = adherentDAO.findAll();
        Adherent trouve = null;
        for (Adherent a : tous) {
            if (email.equals(a.getEmailContact())) {
                trouve = a;
                idsACleaner.add(a.getId());
                break;
            }
        }

        assertNotNull(trouve, "L'adhérent devrait être trouvé");
        assertEquals(nom, trouve.getNom());
        assertEquals(prenom, trouve.getPrenom());
        assertEquals(email, trouve.getEmailContact());
        assertEquals(telephone, trouve.getNumTel());

        System.out.println(" Adhérent retrouvé: " + trouve.getId());
        System.out.println("Nom: " + trouve.getNom() + " " + trouve.getPrenom());
    }
}