package com.eseo.mediastock.service;

import com.eseo.mediastock.model.Admin;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PLAN DE TEST - AdminServiceTest
 * ===================================================
 * <p>
 * OBJECTIF : Vérifier le bon fonctionnement du service AdminService
 * qui gère la logique métier des administrateurs
 * <p>
 * FONCTIONNALITÉS TESTÉES :
 * - Création d'admin avec validation
 * - Authentification (login)
 * - Récupération par ID
 * - Changement de mot de passe
 * - Vérification d'email existant
 * - Validation des données
 * - Mise à jour complète
 * - Suppression
 * <p>
 * ENVIRONNEMENT DE TEST :
 * - Base de données : test
 * - Données : constantes définies (EMAIL_TEST, MDP_TEST, etc.)
 * - Nettoyage : suppression de l'admin de test après tous les tests
 * - Ordre : défini par @Order (dépendances entre tests)
 * <p>
 * COUVERTURE DES TESTS :
 * - testCreerAdmin          → CREATE (réussite)
 * - testLogin               → AUTHENTIFICATION (succès/échec)
 * - testGetAdminParId       → READ (par ID)
 * - testChangerMotDePasse   → UPDATE (mot de passe)
 * - testEmailExistant       → VALIDATION (email dupliqué)
 * - testDonneesInvalides    → VALIDATION (données incorrectes)
 * - testMettreAJourAdmin    → UPDATE (tous champs)
 * - testSupprimerAdmin      → DELETE
 * ===================================================
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AdminServiceTest {

    private static AdminService adminService;
    private static int idAdminTest = 0;
    private static final String EMAIL_TEST = "test@admin.com";
    private static final String MDP_TEST = "password123";
    private static final String NOM_TEST = "Dupont";
    private static final String PRENOM_TEST = "Jean";
    private static final String TEL_TEST = "612345678";

    /**
     * Sets .
     */
    @BeforeAll
    static void setup() {
        adminService = new AdminService();
        System.out.println("DÉMARRAGE DES TESTS ADMIN SERVICE");
    }

    /**
     * NETTOYAGE FINAL
     * Supprime l'admin créé pendant les tests
     * Exécuté une seule fois après tous les tests
     */
    @AfterAll
    static void cleanup() {
        System.out.println("NETTOYAGE ");
        if (idAdminTest > 0) {
            try {
                adminService.supprimerAdmin(idAdminTest);
                System.out.println("Admin " + idAdminTest + " supprimé");
            } catch (Exception e) {
                System.out.println("Erreur lors du nettoyage: " + e.getMessage());
            }
        }
    }

    /**
     * ===================================================
     * TEST 1 : CREATE - Création d'un administrateur
     * ===================================================
     * <p>
     * OBJECTIF : Vérifier la création d'un admin avec toutes ses données
     * <p>
     * DONNÉES :
     * - Email : EMAIL_TEST ("test@admin.com")
     * - Mot de passe : MDP_TEST ("password123")
     * - Nom : NOM_TEST ("Dupont")
     * - Prénom : PRENOM_TEST ("Jean")
     * - Téléphone : TEL_TEST ("612345678")
     * <p>
     * RÉSULTATS ATTENDUS :
     * - Aucune exception lancée
     * - Admin créé avec succès
     * - ID généré > 0
     * - Login réussi après création
     * <p>
     * VÉRIFICATIONS :
     * - assertDoesNotThrow
     * - assertNotNull(admin)
     * - assertEquals(EMAIL_TEST, admin.getEmail())
     * - assertTrue(idAdminTest > 0)
     * <p>
     * DÉPENDANCES :
     * - Aucune (premier test)
     * ===================================================
     */
    @Test
    @Order(1)
    void testCreerAdmin() {
        System.out.println("Test 1: Création admin");

        assertDoesNotThrow(() -> {
            AdminService.creerAdmin(EMAIL_TEST, MDP_TEST, NOM_TEST, PRENOM_TEST, TEL_TEST);
        }, "La création ne devrait pas lancer d'exception");

        System.out.println("Admin créé avec email: " + EMAIL_TEST);

        // Récupération via login
        Admin admin = adminService.login(EMAIL_TEST, MDP_TEST);
        assertNotNull(admin, "L'admin devrait exister après création");
        assertEquals(EMAIL_TEST, admin.getEmail());

        // Récupération de l'ID
        idAdminTest = admin.getId();
        assertTrue(idAdminTest > 0, "L'ID devrait être positif");
        System.out.println("ID généré: " + idAdminTest);
    }

    /**
     * ===================================================
     * TEST 2 : AUTHENTIFICATION - Login
     * ===================================================
     * <p>
     * OBJECTIF : Vérifier l'authentification avec bons et mauvais identifiants
     * <p>
     * SCÉNARIOS TESTÉS :
     * 1. Connexion réussie avec EMAIL_TEST et MDP_TEST
     * 2. Échec de connexion avec EMAIL_TEST et "mauvaisMdp"
     * <p>
     * RÉSULTATS ATTENDUS :
     * - Bons identifiants → admin retourné
     * - Mauvais mot de passe → null
     * <p>
     * VÉRIFICATIONS :
     * - assertNotNull(admin)
     * - assertEquals(EMAIL_TEST, admin.getEmail())
     * - assertNull(adminFail)
     * <p>
     * PRÉ-REQUIS :
     * - Le test 1 doit avoir réussi (admin créé avec EMAIL_TEST)
     * ===================================================
     */
    @Test
    @Order(2)
    void testLogin() {
        System.out.println("Test 2: Connexion");

        // Connexion réussie
        Admin admin = adminService.login(EMAIL_TEST, MDP_TEST);
        assertNotNull(admin, "Connexion devrait réussir");
        assertEquals(EMAIL_TEST, admin.getEmail());
        System.out.println("Connexion réussie");

        // Échec de connexion
        Admin adminFail = adminService.login(EMAIL_TEST, "mauvaisMdp");
        assertNull(adminFail, "Connexion devrait échouer");
        System.out.println("Échec avec mauvais mot de passe");
    }

    /**
     * ===================================================
     * TEST 3 : READ - Récupération par ID
     * ===================================================
     * <p>
     * OBJECTIF : Vérifier la récupération d'un admin par son ID
     * <p>
     * DONNÉES :
     * - ID : idAdminTest (créé au test 1)
     * <p>
     * RÉSULTATS ATTENDUS :
     * - Admin trouvé
     * - Toutes ses données correspondent à la création
     * <p>
     * VÉRIFICATIONS :
     * - assertNotNull(admin)
     * - assertEquals sur email, nom, prénom, téléphone
     * <p>
     * PRÉ-REQUIS :
     * - Le test 1 doit avoir réussi (idAdminTest défini)
     * ===================================================
     */
    @Test
    @Order(3)
    void testGetAdminParId() {
        System.out.println("Test 3: Récupération par ID ");

        assertTrue(idAdminTest > 0, "L'ID de test devrait être défini");

        Admin admin = adminService.getAdminParId(idAdminTest);
        assertNotNull(admin, "L'admin devrait être trouvé par son ID");
        assertEquals(EMAIL_TEST, admin.getEmail());
        assertEquals(NOM_TEST, admin.getNom());
        assertEquals(PRENOM_TEST, admin.getPrenom());
        assertEquals(TEL_TEST, admin.getNumTel());
        System.out.println("Admin trouvé avec ID: " + idAdminTest);
    }

    /**
     * ===================================================
     * TEST 4 : UPDATE - Changement de mot de passe
     * ===================================================
     * <p>
     * OBJECTIF : Vérifier le changement de mot de passe
     * <p>
     * DONNÉES :
     * - ID : idAdminTest
     * - Ancien mot de passe : MDP_TEST
     * - Nouveau mot de passe : "nouveauMdp123"
     * <p>
     * PROCÉDURE :
     * 1. Changer le mot de passe
     * 2. Vérifier la connexion avec nouveau mot de passe
     * 3. Remettre l'ancien mot de passe
     * <p>
     * RÉSULTATS ATTENDUS :
     * - Aucune exception
     * - Login réussi avec nouveau mot de passe
     * <p>
     * VÉRIFICATIONS :
     * - assertDoesNotThrow
     * - assertNotNull(admin) avec nouveau mdp
     * <p>
     * PRÉ-REQUIS :
     * - Le test 1 doit avoir réussi (idAdminTest défini)
     * ===================================================
     */
    @Test
    @Order(4)
    void testChangerMotDePasse() {
        System.out.println("Test 4: Changement mot de passe");

        assertTrue(idAdminTest > 0, "L'ID de test devrait être défini");

        // Changer le mot de passe
        assertDoesNotThrow(() -> {
            adminService.changerMotDePasse(idAdminTest, MDP_TEST, "nouveauMdp123");
        }, "Le changement ne devrait pas lancer d'exception");

        System.out.println("Mot de passe changé");

        // Vérifie avec nouveau mot de passe
        Admin admin = adminService.login(EMAIL_TEST, "nouveauMdp123");
        assertNotNull(admin, "Connexion avec nouveau mdp devrait réussir");
        assertEquals(EMAIL_TEST, admin.getEmail());
        System.out.println("Connexion avec nouveau mot de passe réussie");

        // Remettre l'ancien mot de passe pour les tests suivants
        assertDoesNotThrow(() -> {
            adminService.changerMotDePasse(idAdminTest, "nouveauMdp123", MDP_TEST);
        }, "Le retour à l'ancien mot de passe devrait réussir");

        System.out.println("Mot de passe remis à l'original");
    }

    /**
     * ===================================================
     * TEST 5 : VALIDATION - Email existant
     * ===================================================
     * <p>
     * OBJECTIF : Vérifier qu'on ne peut pas créer deux admins avec le même email
     * <p>
     * DONNÉES :
     * - Email existant : EMAIL_TEST
     * - Autres données différentes
     * <p>
     * RÉSULTAT ATTENDU :
     * - Exception IllegalArgumentException avec message "existe déjà"
     * <p>
     * VÉRIFICATIONS :
     * - assertThrows(IllegalArgumentException.class)
     * - exception.getMessage().contains("existe déjà")
     * <p>
     * PRÉ-REQUIS :
     * - Le test 1 doit avoir réussi (admin avec EMAIL_TEST créé)
     * ===================================================
     */
    @Test
    @Order(5)
    void testEmailExistant() {
        System.out.println("Test 5: Vérification email existant");

        // Tenter de créer un admin avec le même email
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            AdminService.creerAdmin(EMAIL_TEST, "autreMdp", "Autre", "Nom", "123456789");
        });

        assertTrue(exception.getMessage().contains("existe déjà"));
        System.out.println("Email existant détecté: " + exception.getMessage());
    }

    /**
     * ===================================================
     * TEST 6 : VALIDATION - Données invalides
     * ===================================================
     * <p>
     * OBJECTIF : Vérifier que les validations fonctionnent pour
     * toutes les données incorrectes
     * <p>
     * SCÉNARIOS TESTÉS :
     * 1. Email vide → IllegalArgumentException
     * 2. Téléphone invalide → IllegalArgumentException
     * 3. Nom vide → IllegalArgumentException
     * 4. Prénom vide → IllegalArgumentException
     * <p>
     * RÉSULTATS ATTENDUS :
     * - Chaque cas lance une exception avec message approprié
     * <p>
     * VÉRIFICATIONS :
     * - assertThrows pour chaque cas
     * <p>
     * DÉPENDANCES :
     * - Aucune (test indépendant)
     * ===================================================
     */
    @Test
    @Order(6)
    void testDonneesInvalides() {
        System.out.println("Test 6: Données invalides");

        // Test email vide
        Exception e1 = assertThrows(IllegalArgumentException.class, () -> {
            AdminService.creerAdmin("", MDP_TEST, NOM_TEST, PRENOM_TEST, TEL_TEST);
        });
        System.out.println("Email vide détecté: " + e1.getMessage());

        // Test téléphone invalide
        Exception e3 = assertThrows(IllegalArgumentException.class, () -> {
            AdminService.creerAdmin("autre@test.com", MDP_TEST, NOM_TEST, PRENOM_TEST, "0");
        });
        System.out.println("Téléphone invalide détecté: " + e3.getMessage());

        // Test nom vide
        Exception e4 = assertThrows(IllegalArgumentException.class, () -> {
            AdminService.creerAdmin("autre@test.com", MDP_TEST, "", PRENOM_TEST, TEL_TEST);
        });
        System.out.println("Nom vide détecté: " + e4.getMessage());

        // Test prénom vide
        Exception e5 = assertThrows(IllegalArgumentException.class, () -> {
            AdminService.creerAdmin("autre@test.com", MDP_TEST, NOM_TEST, "", TEL_TEST);
        });
        System.out.println("Prénom vide détecté: " + e5.getMessage());
    }

    /**
     * ===================================================
     * TEST 7 : UPDATE - Mise à jour complète
     * ===================================================
     * <p>
     * OBJECTIF : Vérifier la mise à jour de toutes les informations d'un admin
     * <p>
     * DONNÉES MODIFIÉES :
     * - Email : "nouveau@test.com"
     * - Mot de passe : "nouveauMdp"
     * - Nom : "Martin"
     * - Prénom : "Pierre"
     * - Téléphone : "987654321"
     * <p>
     * PROCÉDURE :
     * 1. Mettre à jour l'admin
     * 2. Vérifier que toutes les données ont changé
     * 3. Vérifier la connexion avec nouveau mot de passe
     * <p>
     * RÉSULTATS ATTENDUS :
     * - Aucune exception
     * - Tous les champs modifiés
     * - Login réussi avec nouveau mot de passe
     * <p>
     * VÉRIFICATIONS :
     * - assertDoesNotThrow
     * - assertEquals sur tous les champs
     * - assertNotNull(loginTest)
     * <p>
     * PRÉ-REQUIS :
     * - Le test 1 doit avoir réussi (idAdminTest défini)
     * ===================================================
     */
    @Test
    @Order(7)
    void testMettreAJourAdmin() {
        System.out.println("Test 7: Mise à jour admin");

        assertTrue(idAdminTest > 0, "L'ID de test devrait être défini");

        String nouveauNom = "Martin";
        String nouveauPrenom = "Pierre";
        String nouvelEmail = "nouveau@test.com";
        String nouveauMdp = "nouveauMdp";
        String nouveauTel = "987654321";

        // Mettre à jour - ne doit pas lancer d'exception
        assertDoesNotThrow(() -> {
            adminService.mettreAJourAdmin(
                    idAdminTest,
                    nouvelEmail,
                    nouveauMdp,
                    nouveauNom,
                    nouveauPrenom,
                    nouveauTel
            );
        }, "La mise à jour ne devrait pas lancer d'exception");

        System.out.println("Admin mis à jour");

        // Vérifie les modifications
        Admin admin = adminService.getAdminParId(idAdminTest);
        assertNotNull(admin);
        assertEquals(nouveauNom, admin.getNom());
        assertEquals(nouveauPrenom, admin.getPrenom());
        assertEquals(nouvelEmail, admin.getEmail());
        assertEquals(nouveauTel, admin.getNumTel());
        System.out.println("Vérifications réussies");

        // Vérifie la connexion avec nouveau mot de passe
        Admin loginTest = adminService.login(nouvelEmail, nouveauMdp);
        assertNotNull(loginTest, "Connexion avec nouveau mdp devrait réussir");
        System.out.println("Connexion avec nouveau mdp réussie");

    }

    /**
     * ===================================================
     * TEST 8 : DELETE - Suppression d'un administrateur
     * ===================================================
     * <p>
     * OBJECTIF : Vérifier la suppression d'un admin
     * <p>
     * PROCÉDURE :
     * 1. Créer un admin temporaire avec email unique
     * 2. Vérifier qu'il existe (login)
     * 3. Le supprimer
     * 4. Vérifier qu'il n'existe plus (login null)
     * <p>
     * DONNÉES TEMPORAIRES :
     * - Email : "temp@suppression.com"
     * - Mot de passe : "tempMdp123"
     * - Nom : "Temp", Prénom : "Admin", Tél : "111222333"
     * <p>
     * RÉSULTATS ATTENDUS :
     * - Suppression réussie (pas d'exception)
     * - Admin introuvable après suppression
     * <p>
     * VÉRIFICATIONS :
     * - assertNotNull(adminTemp) avant suppression
     * - assertDoesNotThrow lors de la suppression
     * - assertNull(adminSupprime) après suppression
     * <p>
     * DÉPENDANCES :
     * - Aucune (test indépendant)
     * ===================================================
     */
    @Test
    @Order(8)
    void testSupprimerAdmin() {
        System.out.println("Test 8: Suppression admin ---");

        // Créer un admin temporaire pour le supprimer
        String emailTemp = "temp@suppression.com";

        try {
            AdminService.creerAdmin(emailTemp, "tempMdp123", "Temp", "Admin", "111222333");
            System.out.println("Admin temporaire créé");

            // Récupére l'admin créé via login
            Admin adminTemp = adminService.login(emailTemp, "tempMdp123");
            assertNotNull(adminTemp, "L'admin temporaire devrait exister");

            int idTemp = adminTemp.getId();
            System.out.println("   ID temporaire: " + idTemp);

            // Supprimer - ne doit pas lancer d'exception
            assertDoesNotThrow(() -> {
                adminService.supprimerAdmin(idTemp);
            }, "La suppression ne devrait pas lancer d'exception");

            System.out.println("Admin temporaire supprimé");

            // Vérifier que l'admin n'existe plus
            Admin adminSupprime = adminService.login(emailTemp, "tempMdp123");
            assertNull(adminSupprime, "L'admin ne devrait plus exister après suppression");
            System.out.println("Vérification réussie");

        } catch (Exception e) {
            fail("Erreur lors du test de suppression: " + e.getMessage());
        }
    }
}