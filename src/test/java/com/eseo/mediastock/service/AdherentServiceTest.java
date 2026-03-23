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
 * PLAN DE TEST - AdherentServiceTest
 * ===================================================
 * <p>
 * OBJECTIF : Vérifier le bon fonctionnement du service AdherentService
 * qui gère la logique métier des adhérents (création d'ID,
 * inscriptions, gestion des données)
 * <p>
 * ENVIRONNEMENT DE TEST :
 * - Base de données : test (nettoyage automatique après exécution)
 * - Données : emails uniques générés dynamiquement
 * - Nettoyage : automatique via @AfterAll (idsACleaner)
 * - Ordre : défini par @Order (dépendances logiques entre tests)
 * <p>
 * COUVERTURE DES TESTS :
 * - testGenerateNumAdherent        → Génération d'ID (format ADH-YYYY-XXX)
 * - testInscrireAdherent           → Inscription normale
 * - testInscrireAdherentDonneesSpeciales → Inscription avec caractères spéciaux
 * - testInscriptionsMultiples      → Création de plusieurs adhérents
 * - testNumerosUniques             → Unicité des IDs générés
 * - testRecuperationApresCreation  → Récupération après création
 * <p>
 * NETTOYAGE AUTOMATIQUE :
 * - Tous les adhérents créés sont stockés dans idsACleaner
 * - Supprimés automatiquement après tous les tests
 * ===================================================
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
     * NETTOYAGE FINAL
     * Supprime tous les adhérents créés pendant les tests
     * Exécuté une seule fois après tous les tests
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

    /**
     * Génère un email unique pour les tests
     */
    private String genererEmailUnique() {
        return "test" + compteur.getAndIncrement() + "@email.com";
    }

    /**
     * ===================================================
     * TEST 1 : GÉNÉRATION DE NUMÉRO D'ADHÉRENT
     * ===================================================
     * <p>
     * OBJECTIF : Vérifier la génération du numéro d'adhérent unique
     * au format ADH-ANNÉE-NUMÉRO (ex: ADH-2026-001)
     * <p>
     * DONNÉES :
     * - Aucune (méthode sans paramètre)
     * <p>
     * RÉSULTAT ATTENDU :
     * - Format : ADH-YYYY-XXX
     * - Commence par "ADH-"
     * - Contient l'année courante
     * - Le numéro de séquence a 3 chiffres
     * <p>
     * VÉRIFICATIONS :
     * - assertNotNull(numAdherent)
     * - assertTrue(numAdherent.startsWith("ADH-"))
     * - assertTrue(numAdherent.contains(anneeCourante))
     * - assertEquals(3, parties[2].length())
     * <p>
     * DÉPENDANCES :
     * - Aucune (test indépendant)
     * ===================================================
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
     * ===================================================
     * TEST 2 : INSCRIPTION D'UN ADHÉRENT (NORMAL)
     * ===================================================
     * <p>
     * OBJECTIF : Vérifier l'inscription d'un adhérent avec des données standards
     * <p>
     * DONNÉES :
     * - Nom : "Martin"
     * - Prénom : "Sophie"
     * - Email : unique (généré automatiquement)
     * - Téléphone : "0612345678"
     * <p>
     * PROCÉDURE :
     * 1. Inscrire l'adhérent via le service
     * 2. Récupérer tous les adhérents
     * 3. Chercher celui avec l'email créé
     * <p>
     * RÉSULTATS ATTENDUS :
     * - Adhérent trouvé
     * - Toutes ses données correspondent
     * - L'ID généré commence par "ADH-"
     * <p>
     * VÉRIFICATIONS :
     * - assertNotNull(trouve)
     * - assertEquals sur nom, prénom, email, téléphone
     * - assertTrue(numAdherent.startsWith("ADH-"))
     * <p>
     * DÉPENDANCES :
     * - Aucune (test indépendant)
     * ===================================================
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
     * ===================================================
     * TEST 3 : INSCRIPTION AVEC DONNÉES SPÉCIALES
     * ===================================================
     * <p>
     * OBJECTIF : Vérifier que les données avec caractères spéciaux
     * (accents, tirets, espaces) sont correctement gérées
     * <p>
     * DONNÉES :
     * - Nom : "Dupont-Étienne" (tiret et accent)
     * - Prénom : "Jean-Claude" (tiret)
     * - Téléphone : "+33 6 12 34 56 78" (espaces, +)
     * <p>
     * RÉSULTATS ATTENDUS :
     * - Adhérent créé avec succès
     * - Toutes les données spéciales sont conservées
     * <p>
     * VÉRIFICATIONS :
     * - assertNotNull(trouve)
     * - assertEquals sur nom, prénom, téléphone
     * <p>
     * DÉPENDANCES :
     * - Aucune (test indépendant)
     * ===================================================
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
     * ===================================================
     * TEST 4 : INSCRIPTIONS MULTIPLES
     * ===================================================
     * <p>
     * OBJECTIF : Vérifier la création de plusieurs adhérents à la suite
     * <p>
     * DONNÉES :
     * - 3 adhérents avec noms, prénoms, téléphones différents
     * ["Dupont/Jean/0102030405", "Martin/Sophie/0203040506", "Bernard/Pierre/0304050607"]
     * <p>
     * RÉSULTATS ATTENDUS :
     * - Les 3 adhérents sont créés
     * - Tous sont retrouvables par leur email
     * <p>
     * VÉRIFICATIONS :
     * - assertTrue(trouve) pour chaque email
     * <p>
     * DÉPENDANCES :
     * - Aucune (test indépendant)
     * ===================================================
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
     * ===================================================
     * TEST 5 : UNICITÉ DES NUMÉROS D'ADHÉRENT
     * ===================================================
     * <p>
     * OBJECTIF : Vérifier que chaque adhérent reçoit un ID unique
     * <p>
     * DONNÉES :
     * - 3 adhérents avec des données différentes
     * <p>
     * PROCÉDURE :
     * 1. Créer 3 adhérents
     * 2. Récupérer leurs IDs
     * 3. Comparer tous les IDs entre eux
     * <p>
     * RÉSULTATS ATTENDUS :
     * - Tous les IDs sont différents
     * <p>
     * VÉRIFICATIONS :
     * - assertNotEquals pour chaque paire d'IDs
     * <p>
     * DÉPENDANCES :
     * - Aucune (test indépendant)
     * ===================================================
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
     * ===================================================
     * TEST 6 : RÉCUPÉRATION APRÈS CRÉATION
     * ===================================================
     * <p>
     * OBJECTIF : Vérifier qu'un adhérent créé peut être retrouvé
     * et que ses données sont intactes
     * <p>
     * DONNÉES :
     * - Nom : "Recuperation"
     * - Prénom : "Test"
     * - Téléphone : "0999999999"
     * - Email : unique
     * <p>
     * PROCÉDURE :
     * 1. Créer l'adhérent
     * 2. Rechercher par email
     * 3. Vérifier toutes les données
     * <p>
     * RÉSULTATS ATTENDUS :
     * - Adhérent trouvé
     * - Toutes les données correspondent
     * <p>
     * VÉRIFICATIONS :
     * - assertNotNull(trouve)
     * - assertEquals sur tous les champs
     * <p>
     * DÉPENDANCES :
     * - Aucune (test indépendant)
     * ===================================================
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