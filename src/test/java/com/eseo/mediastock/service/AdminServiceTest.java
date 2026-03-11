package com.eseo.mediastock.service;

import com.eseo.mediastock.model.Admin;
import org.junit.jupiter.api.*;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AdminServiceTest {

    private static AdminService adminService;
    private static int idAdminTest = 0;
    private static final String EMAIL_TEST = "test@admin.com";
    private static final String MDP_TEST = "password123";
    private static final String NOM_TEST = "Dupont";
    private static final String PRENOM_TEST = "Jean";
    private static final int TEL_TEST = 612345678;

    @BeforeAll
    static void setup() {
        adminService = new AdminService();
        System.out.println("DÉMARRAGE DES TESTS ADMIN SERVICE");
    }

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

    @Test
    @Order(1)
    void testCreerAdmin() {
        System.out.println("Test 1: Création admin");

        assertDoesNotThrow(() -> {
            adminService.creerAdmin(EMAIL_TEST, MDP_TEST, NOM_TEST, PRENOM_TEST, TEL_TEST);
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

    @Test
    @Order(5)
    void testEmailExistant() {
        System.out.println("Test 5: Vérification email existant");

        // Tenter de créer un admin avec le même email
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            adminService.creerAdmin(EMAIL_TEST, "autreMdp", "Autre", "Nom", 123456789);
        });

        assertTrue(exception.getMessage().contains("existe déjà"));
        System.out.println("Email existant détecté: " + exception.getMessage());
    }

    @Test
    @Order(6)
    void testDonneesInvalides() {
        System.out.println("Test 6: Données invalides");

        // Test email vide
        Exception e1 = assertThrows(IllegalArgumentException.class, () -> {
            adminService.creerAdmin("", MDP_TEST, NOM_TEST, PRENOM_TEST, TEL_TEST);
        });
        System.out.println("Email vide détecté: " + e1.getMessage());

        // Test téléphone invalide
        Exception e3 = assertThrows(IllegalArgumentException.class, () -> {
            adminService.creerAdmin("autre@test.com", MDP_TEST, NOM_TEST, PRENOM_TEST, 0);
        });
        System.out.println("Téléphone invalide détecté: " + e3.getMessage());

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

    @Test
    @Order(7)
    void testMettreAJourAdmin() {
        System.out.println("Test 7: Mise à jour admin");

        assertTrue(idAdminTest > 0, "L'ID de test devrait être défini");

        String nouveauNom = "Martin";
        String nouveauPrenom = "Pierre";
        String nouvelEmail = "nouveau@test.com";
        String nouveauMdp = "nouveauMdp";
        int nouveauTel = 987654321;

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

    @Test
    @Order(8)
    void testSupprimerAdmin() {
        System.out.println("Test 8: Suppression admin ---");

        // Créer un admin temporaire pour le supprimer
        String emailTemp = "temp@suppression.com";

        try {
            adminService.creerAdmin(emailTemp, "tempMdp123", "Temp", "Admin", 111222333);
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