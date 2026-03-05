package com.eseo.mediastock.service;

import com.eseo.mediastock.model.Adherent;
import com.eseo.mediastock.dao.AdherentDAO;
import org.junit.jupiter.api.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AdherentServiceTest {

    private static AdherentService adherentService;
    private static AdherentDAO adherentDAO;
    private static String idTest;

    // Compteur pour des IDs uniques
    private static AtomicInteger compteur = new AtomicInteger(1);

    // Liste pour stocker les IDs créés pendant les tests
    private static final List<String> idsACleaner = new ArrayList<>();

    @BeforeAll
    static void setup() {
        adherentService = new AdherentService();
        adherentDAO = new AdherentDAO();
        System.out.println("Initialisation du Service Adherent");
    }

    @AfterAll
    static void nettoyage() throws SQLException {
        System.out.println("NETTOYAGE AUTOMATIQUE");
        System.out.println("Suppression des " + idsACleaner.size() + " adhérents de test...");

        int supprime = 0;
        int erreurs = 0;

        for (String id : idsACleaner) {
            try {
                if (adherentDAO.deleteAdherent(id)) {
                    System.out.println("Supprimé: " + id);
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

        System.out.println(" Bilan: " + supprime + " supprimés, " + erreurs + " erreurs");
        System.out.println(" FIN NETTOYAGE ");

        idsACleaner.clear();
    }

    /**
     * Génère un email unique pour les tests
     */
    private String genererEmailUnique() {
        return "test" + compteur.getAndIncrement() + "@email.com";
    }

    /**
     * Test: Test de la méthode nbrAdherent
     */
    @Test
    @Order(1)
    void testNbrAdherent() {
        System.out.println("Test nbrAdherent");

        int valeurInitiale = Adherent.nbrAdherent;

        int nouvelleValeur = adherentService.nbrAdherent();

        assertEquals(valeurInitiale + 1, nouvelleValeur);

        System.out.println("nbrAdherent incrémenté: " + nouvelleValeur);
    }

    /**
     * Test: Test de la méthode createNumAdherent
     */
    @Test
    @Order(2)
    void testCreateNumAdherent() {
        System.out.println("Test createNumAdherent");

        String numAdherent = adherentService.createNumAdherent();

        assertNotNull(numAdherent);
        assertTrue(numAdherent.startsWith("ADH-"));

        int anneeCourante = LocalDate.now().getYear();
        assertTrue(numAdherent.contains(String.valueOf(anneeCourante)));

        // Vérifie le format: ADH-YYYY-XXX
        String[] parties = numAdherent.split("-");
        assertEquals(3, parties.length);
        assertEquals("ADH", parties[0]);
        assertEquals(String.valueOf(anneeCourante), parties[1]);
        assertEquals(3, parties[2].length()); // 3 chiffres

        System.out.println(" Numéro généré: " + numAdherent);
    }

    /**
     * Test : Inscription d'un adhérent
     */
    @Test
    @Order(3)
    void testInscrireAdherent() throws SQLException {
        System.out.println("Test inscrireAdherent");

        String nom = "Martin";
        String prenom = "Sophie";
        String email = genererEmailUnique();
        String telephone = "0612345678";

        // Utilise le service pour inscrire
        adherentService.inscrireAdherent(nom, prenom, email, telephone);

        // Récupére l'adhérent par email
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
        assertTrue(numAdherent.startsWith("ADH-"));

        // Ajoute à la liste de nettoyage
        idsACleaner.add(trouve.getId());
        idTest = trouve.getId();

        System.out.println("Adhérent créé via service avec ID: " + trouve.getId());
        System.out.println("Email: " + trouve.getEmailContact());
        System.out.println("Numéro: " + trouve.getId());
    }

    /**
     * Test : Inscription avec des données spéciales
     */
    @Test
    @Order(4)
    void testInscrireAdherentDonneesSpeciales() throws SQLException {
        System.out.println("Test inscrireAdherent - données spéciales");

        String nom = "Dupont-Étienne";
        String prenom = "Jean-Claude";
        String email = genererEmailUnique();
        String telephone = "+33 6 12 34 56 78";

        adherentService.inscrireAdherent(nom, prenom, email, telephone);

        // Récupére et vérifie
        List<Adherent> tous = adherentDAO.findAll();
        Adherent trouve = null;

        for (Adherent a : tous) {
            if (email.equals(a.getEmailContact())) {
                trouve = a;
                break;
            }
        }

        assertNotNull(trouve);
        assertEquals(nom, trouve.getNom());
        assertEquals(prenom, trouve.getPrenom());
        assertEquals(telephone, trouve.getNumTel());

        idsACleaner.add(trouve.getId());

        System.out.println("Adhérent avec données spéciales créé: " + trouve.getId());
    }

    /**
     * Test: Inscriptions multiples
     */
    @Test
    @Order(5)
    void testInscriptionsMultiples() throws SQLException {
        System.out.println("📝 Test inscriptions multiples");

        String[] noms = {"Dupont", "Martin", "Bernard"};
        String[] prenoms = {"Jean", "Sophie", "Pierre"};
        String[] telephones = {"0102030405", "0203040506", "0304050607"};

        List<String> emails = new ArrayList<>();

        for (int i = 0; i < noms.length; i++) {
            String email = genererEmailUnique();
            emails.add(email);
            adherentService.inscrireAdherent(noms[i], prenoms[i], email, telephones[i]);
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

        System.out.println("   ✅ " + noms.length + " adhérents créés avec succès");
    }

    /**
     * Test : Vérification que les numéros sont uniques
     */
    @Test
    @Order(6)
    void testNumerosUniques() throws SQLException {
        System.out.println("Test numéros uniques");

        // Crée plusieurs adhérents
        for (int i = 0; i < 3; i++) {
            adherentService.inscrireAdherent(
                    "Nom" + i,
                    "Prenom" + i,
                    genererEmailUnique(),
                    "000000000" + i
            );
        }

        // Récupére tous les adhérents
        List<Adherent> tous = adherentDAO.findAll();

        // Récupére les derniers créés (ceux avec nos emails de test)
        List<Adherent> recents = new ArrayList<>();
        for (Adherent a : tous) {
            if (a.getEmailContact().startsWith("test") && a.getEmailContact().endsWith("@email.com")) {
                recents.add(a);
                idsACleaner.add(a.getId());
            }
        }

        // Vérifie que les IDs sont différents
        for (int i = 0; i < recents.size(); i++) {
            for (int j = i + 1; j < recents.size(); j++) {
                assertNotEquals(recents.get(i).getId(), recents.get(j).getId(),
                        "Les IDs devraient être uniques");
            }
        }

        System.out.println("Tous les numéros sont uniques");
        for (Adherent a : recents) {
            System.out.println(" - " + a.getId());
        }
    }

    /**
     * Test: Test de récupération après création
     */
    @Test
    @Order(7)
    void testRecuperationApresCreation() throws SQLException {
        System.out.println("Test récupération après création");

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

        assertNotNull(trouve);
        assertEquals(nom, trouve.getNom());
        assertEquals(prenom, trouve.getPrenom());
        assertEquals(email, trouve.getEmailContact());
        assertEquals(telephone, trouve.getNumTel());

        System.out.println("Adhérent retrouvé: " + trouve.getId());
    }
}