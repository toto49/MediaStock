package com.eseo.mediastock.dao;

import com.eseo.mediastock.model.Adherent;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PLAN DE TEST - AdherentDAOTest
 * ===================================================
 * <p>
 * OBJECTIF : Vérifier le bon fonctionnement de toutes les méthodes CRUD
 * de la classe AdherentDAO (Create, Read, Update, Delete)
 * <p>
 * ENVIRONNEMENT DE TEST :
 * - Base de données : test (nettoyage automatique après exécution)
 * - Données : générées dynamiquement avec timestamps uniques
 * - Nettoyage : automatique via @AfterAll
 * - Ordre : défini par @Order (dépendances entre tests)
 * <p>
 * COUVERTURE DES TESTS :
 * - testCreateAdherent    → CREATE
 * - testGetByID           → READ (existant)
 * - testGetByIDInexistant → READ (inexistant)
 * - testUpdateAdherent    → UPDATE
 * - testValeursSpeciales  → CREATE (cas limites)
 * - testCreationsMultiples→ CREATE (batch)
 * - testDeleteAdherent    → DELETE
 * - testFindAll           → READ (tous)
 * <p>
 * NETTOYAGE AUTOMATIQUE :
 * Les IDs créés sont stockés dans idsACleaner et supprimés après tous les tests
 * ===================================================
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AdherentDAOTest {

    // Liste pour stocker les IDs créés pendant les tests
    private static final List<String> idsACleaner = new ArrayList<>();
    private static AdherentDAO dao;
    private static String idTest;

    /**
     * Sets .
     */
    @BeforeAll
    static void setup() {
        dao = new AdherentDAO();
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
        System.out.println("NETTOYAGE AUTOMATIQUE");
        System.out.println("Suppression des " + idsACleaner.size() + " adhérents de test...");

        int supprime = 0;
        int erreurs = 0;

        for (String id : idsACleaner) {
            try {
                if (dao.deleteAdherent(id)) {
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
        System.out.println("FIN NETTOYAGE ");

        // Vide la liste après nettoyage
        idsACleaner.clear();
    }

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        System.out.println(" Nouveau test");
    }

    /**
     * Tear down.
     */
    @AfterEach
    void tearDown() {
        System.out.println("Test terminé ");
    }

    /**
     * ===================================================
     * TEST 1 : CREATE - Création d'un adhérent
     * ===================================================
     * <p>
     * OBJECTIF : Vérifier l'insertion d'un nouvel adhérent
     * <p>
     * DONNÉES :
     * - ID : généré automatiquement (timestamp)
     * - Nom : "LicornTest"
     * - Prénom : "MorgianeTest"
     * - Email : "email@test.com"
     * - Téléphone : "0123456789"
     * <p>
     * RÉSULTAT ATTENDU :
     * - L'adhérent est créé avec un ID non null
     * - Toutes les données sont correctement sauvegardées
     * <p>
     * VÉRIFICATIONS :
     * - assertNotNull(a.getId()) → ID généré
     * <p>
     * DÉPENDANCES :
     * - Aucune (premier test)
     * ===================================================
     *
     * @throws SQLException the sql exception
     */
    @Test
    @Order(1)
    void testCreateAdherent() throws SQLException {
        System.out.println(" Test CREATE - Création d'un adhérent");

        // Créer avec un Id unique basé sur le timestamp
        String shortTimestamp = String.valueOf(System.currentTimeMillis()).substring(8);
        String uniqueId = "T" + shortTimestamp;  // Ex: "T12345678" (9 caractères)

        Adherent a = new Adherent(uniqueId, "LicornTest", "MorgianeTest", "email@test.com", "0123456789");
        // Sauvegarder dans la db
        dao.createAdherent(a);

        // Vérifier id pas null
        assertNotNull(a.getId());
        idTest = a.getId(); // Stock id pour les tests suivants

        // Ajout de l'ID à la liste de nettoyage
        idsACleaner.add(idTest);

        System.out.println(" Adhérent créé avec ID: " + idTest);
        System.out.println(" Nom : " + a.getNom() + " " + a.getPrenom());
        System.out.println("Email : " + a.getEmailContact());
        System.out.println(" Tél : " + a.getNumTel());
    }

    /**
     * ===================================================
     * TEST 2 : READ - Recherche par ID (existant)
     * ===================================================
     * <p>
     * OBJECTIF : Vérifier la récupération d'un adhérent existant
     * <p>
     * DONNÉES :
     * - ID : idTest (créé au test 1)
     * <p>
     * RÉSULTAT ATTENDU :
     * - Adhérent trouvé
     * - Toutes ses données sont identiques à la création
     * <p>
     * VÉRIFICATIONS :
     * - assertNotNull(trouve) → adhérent trouvé
     * - assertEquals sur tous les champs (id, nom, prénom, email, tel)
     * <p>
     * PRÉ-REQUIS :
     * - Le test 1 doit avoir réussi (idTest défini)
     * ===================================================
     *
     * @throws SQLException the sql exception
     */
    @Test
    @Order(2)
    void testGetByID() throws SQLException {
        System.out.println(" Test FIND BY ID");

        assertNotNull(idTest, "ID non disponible ou non créé");

        // Cherche l'adhérent par id
        Adherent trouve = dao.GetByID(idTest);

        // Vérifie qu'on l'a trouvé
        assertNotNull(trouve);
        assertEquals(idTest, trouve.getId());
        assertEquals("LicornTest", trouve.getNom());
        assertEquals("MorgianeTest", trouve.getPrenom());
        assertEquals("0123456789", trouve.getNumTel());
        assertEquals("email@test.com", trouve.getEmailContact());

        System.out.println(" Adhérent trouvé : " + trouve.getId());
        System.out.println(" Nom : " + trouve.getNom());
    }

    /**
     * ===================================================
     * TEST 3 : READ - Recherche par ID (inexistant)
     * ===================================================
     * <p>
     * OBJECTIF : Vérifier le comportement avec un ID inexistant
     * <p>
     * DONNÉES :
     * - ID : "ID-QUI-NEXISTE-PAS" (ID fictif)
     * <p>
     * RÉSULTAT ATTENDU :
     * - Retour null (pas d'exception)
     * <p>
     * VÉRIFICATIONS :
     * - assertNull(trouve) → aucun adhérent trouvé
     * <p>
     * PRÉ-REQUIS :
     * - Aucun (test indépendant)
     * ===================================================
     *
     * @throws SQLException the sql exception
     */
    @Test
    @Order(3)
    void testGetByIDInexistant() throws SQLException {
        System.out.println(" Test FIND BY ID - inexistant");

        Adherent trouve = dao.GetByID("ID-QUI-NEXISTE-PAS");

        assertNull(trouve);

        System.out.println(" ID inexistant retourne null (normal)");
    }

    /**
     * ===================================================
     * TEST 4 : UPDATE - Mise à jour d'un adhérent
     * ===================================================
     * <p>
     * OBJECTIF : Vérifier la modification des données d'un adhérent
     * <p>
     * DONNÉES :
     * - ID : idTest (créé au test 1)
     * - Modification : nom → "LicornTestModif"
     * <p>
     * RÉSULTAT ATTENDU :
     * - Nom modifié avec succès
     * - Les autres données restent inchangées
     * <p>
     * VÉRIFICATIONS :
     * - assertEquals("LicornTestModif", modifie.getNom())
     * <p>
     * PRÉ-REQUIS :
     * - Le test 1 doit avoir réussi (idTest défini)
     * ===================================================
     *
     * @throws SQLException the sql exception
     */
    @Test
    @Order(4)
    void testUpdateAdherent() throws SQLException {
        System.out.println(" Test UPDATE");

        assertNotNull(idTest);

        // Récupérer l'adhérent
        Adherent a = dao.GetByID(idTest);
        assertNotNull(a);

        // Afficher les anciennes valeurs
        System.out.println("Anciennes valeurs :");
        System.out.println(" Nom : " + a.getNom());

        // Modifier
        a.setNom("LicornTestModif");

        // Sauvegarder les nouvelles données
        dao.updateAdherent(a);

        // Vérifier les changements
        Adherent modifie = dao.GetByID(idTest);
        assertEquals("LicornTestModif", modifie.getNom());

        System.out.println("Mise à jour réussie");
        System.out.println(" Nouveau nom : " + modifie.getNom());
    }

    /**
     * ===================================================
     * TEST 5 : CREATE - Données spéciales (accents, chiffres, longs)
     * ===================================================
     * <p>
     * OBJECTIF : Vérifier que les données avec caractères spéciaux sont bien gérées
     * <p>
     * SCÉNARIOS TESTÉS :
     * 1. Caractères accentués (Étienne, François)
     * 2. Chiffres dans le nom (User123, Test456)
     * 3. Email très long (email.tres.tres.long@test.domain.com)
     * 4. Téléphone avec format spécial (+33 6 12 34 56 78)
     * <p>
     * RÉSULTAT ATTENDU :
     * - Tous les adhérents sont créés avec succès
     * - Leurs données sont correctement sauvegardées
     * <p>
     * VÉRIFICATIONS :
     * - assertNotNull(id) pour chaque création
     * <p>
     * PRÉ-REQUIS :
     * - Aucun (test indépendant)
     * ===================================================
     *
     * @throws SQLException the sql exception
     */
    @Test
    @Order(5)
    void testValeursSpeciales() throws SQLException {
        System.out.println("📝 Test valeurs spéciales");

        String shortTs = String.valueOf(System.currentTimeMillis()).substring(8);

        // Test avec des caractères accentués
        Adherent accent = new Adherent("AC-" + shortTs, "Étienne", "François",
                "etienne@email.com", "+33 6 12 34 56 78");
        dao.createAdherent(accent);
        assertNotNull(accent.getId());
        idsACleaner.add(accent.getId());
        System.out.println("   ✅ Adhérent avec accents créé : " + accent.getId());

        // Test avec des chiffres dans le nom
        Adherent chiffres = new Adherent("CH-" + shortTs, "User123", "Test456",
                "chiffres@email.com", "0612345678");
        dao.createAdherent(chiffres);
        assertNotNull(chiffres.getId());
        idsACleaner.add(chiffres.getId());
        System.out.println(" Adhérent avec chiffres créé : " + chiffres.getId());

        // Test avec email long
        Adherent longEmail = new Adherent("LONG-" + shortTs, "Long", "Email",
                "email.tres.tres.long@test.domain.com", "0612345678");
        dao.createAdherent(longEmail);
        assertNotNull(longEmail.getId());
        idsACleaner.add(longEmail.getId());
        System.out.println(" Adhérent avec email long créé : " + longEmail.getId());
    }

    /**
     * ===================================================
     * TEST 6 : CREATE - Créations multiples
     * ===================================================
     * <p>
     * OBJECTIF : Vérifier l'insertion de plusieurs adhérents à la suite
     * <p>
     * DONNÉES :
     * - 3 adhérents avec des IDs et données différents
     * <p>
     * RÉSULTAT ATTENDU :
     * - Les 3 adhérents sont créés avec des IDs non null
     * - On peut les retrouver individuellement
     * <p>
     * VÉRIFICATIONS :
     * - assertNotNull pour chaque ID
     * - Récupération individuelle réussie pour chacun
     * <p>
     * PRÉ-REQUIS :
     * - Aucun (test indépendant)
     * ===================================================
     *
     * @throws SQLException the sql exception
     */
    @Test
    @Order(6)
    void testCreationsMultiples() throws SQLException {
        System.out.println("Test créations multiples - Ajout de 3 adhérents");

        String shortTs = String.valueOf(System.currentTimeMillis()).substring(8);

        Adherent a1 = new Adherent("MULTI" + shortTs, "Test1", "User1",
                "test1@email.com", "1111111111");
        Adherent a2 = new Adherent("MULT" + shortTs, "Test2", "User2",
                "test2@email.com", "2222222222");
        Adherent a3 = new Adherent("MULT-" + shortTs, "Test3", "User3",
                "test3@email.com", "3333333333");

        // Sauvegarder
        dao.createAdherent(a1);
        dao.createAdherent(a2);
        dao.createAdherent(a3);

        // Vérifier que les IDs sont non null
        assertNotNull(a1.getId());
        assertNotNull(a2.getId());
        assertNotNull(a3.getId());

        idsACleaner.add(a1.getId());
        idsACleaner.add(a2.getId());
        idsACleaner.add(a3.getId());

        System.out.println(" 3 adhérents créés avec succès");
        System.out.println(" IDs : " + a1.getId() + ", " + a2.getId() + ", " + a3.getId());

        // Vérifier qu'on peut les retrouver
        Adherent trouve1 = dao.GetByID(a1.getId());
        Adherent trouve2 = dao.GetByID(a2.getId());
        Adherent trouve3 = dao.GetByID(a3.getId());

        assertNotNull(trouve1);
        assertNotNull(trouve2);
        assertNotNull(trouve3);

        System.out.println(" Tous retrouvés individuellement");
    }

    /**
     * ===================================================
     * TEST 7 : DELETE - Suppression d'un adhérent
     * ===================================================
     * <p>
     * OBJECTIF : Vérifier la suppression d'un adhérent
     * <p>
     * PROCÉDURE :
     * 1. Créer un adhérent temporaire
     * 2. Vérifier qu'il existe
     * 3. Le supprimer
     * 4. Vérifier qu'il n'existe plus
     * <p>
     * RÉSULTAT ATTENDU :
     * - Suppression réussie (true)
     * - Adhérent introuvable après suppression
     * <p>
     * VÉRIFICATIONS :
     * - assertTrue(supprime)
     * - assertNull(apres)
     * <p>
     * PRÉ-REQUIS :
     * - Aucun (test indépendant)
     * ===================================================
     *
     * @throws SQLException the sql exception
     */
    @Test
    @Order(7)
    void testDeleteAdherent() throws SQLException {
        System.out.println("Test DELETE - Suppression d'un adhérent");

        // Créer un adhérent pour le test de suppression
        String idASupprimer = "SUPPR-" + (System.currentTimeMillis() % 10000);
        Adherent aSupprimer = new Adherent(idASupprimer, "ASupprimer", "Test",
                "delete@test.com", "0000000000");
        dao.createAdherent(aSupprimer);
        assertNotNull(aSupprimer.getId());
        System.out.println("Adhérent créé pour test suppression : " + aSupprimer.getId());

        // Vérifier qu'il existe
        Adherent avant = dao.GetByID(aSupprimer.getId());
        assertNotNull(avant);

        // Supprimer
        boolean supprime = dao.deleteAdherent(aSupprimer.getId());
        assertTrue(supprime);

        // Vérifier qu'il n'existe plus
        Adherent apres = dao.GetByID(aSupprimer.getId());
        assertNull(apres);

        System.out.println("Adhérent " + aSupprimer.getId() + " supprimé avec succès");
    }

    /**
     * ===================================================
     * TEST 8 : READ - Liste de tous les adhérents
     * ===================================================
     * <p>
     * OBJECTIF : Vérifier la récupération de tous les adhérents
     * <p>
     * RÉSULTAT ATTENDU :
     * - Liste non null
     * - Liste non vide (contient au moins l'adhérent du test 1)
     * <p>
     * VÉRIFICATIONS :
     * - assertNotNull(liste)
     * - assertTrue(liste.size() > 0)
     * <p>
     * PRÉ-REQUIS :
     * - Le test 1 doit avoir réussi (au moins un adhérent en base)
     * ===================================================
     *
     * @throws SQLException the sql exception
     */
    @Test
    @Order(8)
    void testFindAll() throws SQLException {
        System.out.println("Test FIND ALL - Liste de tous les adhérents");

        List<Adherent> liste = dao.findAll();

        assertNotNull(liste);
        assertTrue(liste.size() > 0);

        System.out.println(liste.size() + " adhérent(s) trouvé(s) dans la base");

        // Afficher les 5 premiers
        int affiches = 0;
        for (Adherent a : liste) {
            if (affiches < 5) {
                System.out.println("      - " + a.getId() + " : " + a.getPrenom() + " " + a.getNom());
                affiches++;
            }
        }
        if (liste.size() > 5) {
            System.out.println("      ... et " + (liste.size() - 5) + " autre(s)");
        }
    }
}