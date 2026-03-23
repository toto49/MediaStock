package com.eseo.mediastock.service;

import com.eseo.mediastock.model.Admin;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The type Admin service test.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AdminServiceTest {

    private static final String MDP_TEST = "password123";
    private static final String NOM_TEST = "Dupont";
    private static final String PRENOM_TEST = "Jean";
    private static final String TEL_TEST = "0612345678";
    private static AdminService adminService;
    private static int idAdminTest = 0;
    private static String emailTest; // Email unique pour le test

    /**
     * Sets .
     */
    @BeforeAll
    static void setup() {
        adminService = new AdminService();
        // Générer un email unique avec timestamp
        emailTest = "test_" + System.currentTimeMillis() + "@admin.com";
        System.out.println("DÉMARRAGE DES TESTS ADMIN SERVICE");
        System.out.println("Email de test: " + emailTest);
    }

    /**
     * Cleanup.
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
     * Test creer admin.
     */
    @Test
    @Order(1)
    void testCreerAdmin() {
        System.out.println("Test 1: Création admin");

        assertDoesNotThrow(() -> {
            adminService.creerAdmin(emailTest, MDP_TEST, NOM_TEST, PRENOM_TEST, TEL_TEST);
        }, "La création ne devrait pas lancer d'exception");

        System.out.println("Admin créé avec email: " + emailTest);

        // Récupération via login
        Admin admin = adminService.login(emailTest, MDP_TEST);
        assertNotNull(admin, "L'admin devrait exister après création");
        assertEquals(emailTest, admin.getEmail());

        // Récupération de l'ID
        idAdminTest = admin.getId();
        assertTrue(idAdminTest > 0, "L'ID devrait être positif");
        System.out.println("ID généré: " + idAdminTest);
    }

    /**
     * Test login.
     */
    @Test
    @Order(2)
    void testLogin() {
        System.out.println("Test 2: Connexion");

        // Connexion réussie
        Admin admin = adminService.login(emailTest, MDP_TEST);
        assertNotNull(admin, "Connexion devrait réussir");
        assertEquals(emailTest, admin.getEmail());
        System.out.println("Connexion réussie");

        // Échec de connexion
        Admin adminFail = adminService.login(emailTest, "mauvaisMdp");
        assertNull(adminFail, "Connexion devrait échouer");
        System.out.println("Échec avec mauvais mot de passe");
    }

    /**
     * Test get admin par id.
     */
    @Test
    @Order(3)
    void testGetAdminParId() {
        System.out.println("Test 3: Récupération par ID ");

        assertTrue(idAdminTest > 0, "L'ID de test devrait être défini");

        Admin admin = adminService.getAdminParId(idAdminTest);
        assertNotNull(admin, "L'admin devrait être trouvé par son ID");
        assertEquals(emailTest, admin.getEmail());
        assertEquals(NOM_TEST, admin.getNom());
        assertEquals(PRENOM_TEST, admin.getPrenom());
        assertEquals(TEL_TEST, admin.getNumTel());
        System.out.println("Admin trouvé avec ID: " + idAdminTest);
    }

    /**
     * Test changer mot de passe.
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
        Admin admin = adminService.login(emailTest, "nouveauMdp123");
        assertNotNull(admin, "Connexion avec nouveau mdp devrait réussir");
        assertEquals(emailTest, admin.getEmail());
        System.out.println("Connexion avec nouveau mot de passe réussie");

        // Remettre l'ancien mot de passe pour les tests suivants
        assertDoesNotThrow(() -> {
            adminService.changerMotDePasse(idAdminTest, "nouveauMdp123", MDP_TEST);
        }, "Le retour à l'ancien mot de passe devrait réussir");

        System.out.println("Mot de passe remis à l'original");
    }

    /**
     * Test email existant.
     */
    @Test
    @Order(5)
    void testEmailExistant() {
        System.out.println("Test 5: Vérification email existant");

        // Tenter de créer un admin avec le même email
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            adminService.creerAdmin(emailTest, "autreMdp", "Autre", "Nom", "0698765432");
        });

        assertTrue(exception.getMessage().contains("existe déjà"));
        System.out.println("Email existant détecté: " + exception.getMessage());
    }

    /**
     * Test donnees invalides.
     */
    @Test
    @Order(6)
    void testDonneesInvalides() {
        System.out.println("Test 6: Données invalides");

        // Test email vide
        Exception e1 = assertThrows(IllegalArgumentException.class, () -> {
            adminService.creerAdmin("", MDP_TEST, NOM_TEST, PRENOM_TEST, TEL_TEST);
        });
        System.out.println("Email vide détecté: " + e1.getMessage());

        // Test téléphone invalide (trop court)
        Exception e2 = assertThrows(IllegalArgumentException.class, () -> {
            adminService.creerAdmin("autre@test.com", MDP_TEST, NOM_TEST, PRENOM_TEST, "123");
        });
        System.out.println("Téléphone invalide détecté: " + e2.getMessage());

        // Test téléphone avec lettres
        Exception e3 = assertThrows(IllegalArgumentException.class, () -> {
            adminService.creerAdmin("autre@test.com", MDP_TEST, NOM_TEST, PRENOM_TEST, "abcdefghij");
        });
        System.out.println("Téléphone avec lettres détecté: " + e3.getMessage());

        // Test nom vide
        Exception e4 = assertThrows(IllegalArgumentException.class, () -> {
            adminService.creerAdmin("autre@test.com", MDP_TEST, "", PRENOM_TEST, TEL_TEST);
        });
        System.out.println("Nom vide détecté: " + e4.getMessage());

        // Test prénom vide
        Exception e5 = assertThrows(IllegalArgumentException.class, () -> {
            adminService.creerAdmin("autre@test.com", MDP_TEST, NOM_TEST, "", TEL_TEST);
        });
        System.out.println("Prénom vide détecté: " + e5.getMessage());
    }

    /**
     * Test mettre a jour admin.
     */
    @Test
    @Order(7)
    void testMettreAJourAdmin() {
        System.out.println("Test 7: Mise à jour admin");

        assertTrue(idAdminTest > 0, "L'ID de test devrait être défini");

        String nouveauNom = "Martin";
        String nouveauPrenom = "Pierre";
        String nouvelEmail = "nouveau_" + System.currentTimeMillis() + "@test.com";
        String nouveauMdp = "nouveauMdp";
        String nouveauTel = "0987654321";

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

        // Remettre les données d'origine
        assertDoesNotThrow(() -> {
            adminService.mettreAJourAdmin(
                    idAdminTest,
                    emailTest,
                    MDP_TEST,
                    NOM_TEST,
                    PRENOM_TEST,
                    TEL_TEST
            );
        }, "Le retour aux données d'origine devrait réussir");
        System.out.println("Données remises à l'original");
    }

    /**
     * Test supprimer admin.
     */
    @Test
    @Order(8)
    void testSupprimerAdmin() {
        System.out.println("Test 8: Suppression admin ---");

        // Créer un admin temporaire pour le supprimer
        String emailTemp = "temp_suppression_" + System.currentTimeMillis() + "@test.com";
        String telTemp = "0611111111";

        try {
            adminService.creerAdmin(emailTemp, "tempMdp123", "Temp", "Admin", telTemp);
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