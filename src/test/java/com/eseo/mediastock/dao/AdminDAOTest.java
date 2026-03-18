package com.eseo.mediastock.dao;

import com.eseo.mediastock.model.Admin;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AdminDAOTest {

    private static AdminDAO adminDAO;
    private static int idAdminTest = 0;

    // Constantes pour les tests
    private static final String EMAIL_TEST = "testdao@admin.com";
    private static final String MDP_TEST = "password123";
    private static final String NOM_TEST = "Test";
    private static final String PRENOM_TEST = "DAO";
    private static final String TEL_TEST = "123456789";

    @BeforeAll
    static void setup() {
        adminDAO = new AdminDAO();
        System.out.println("DÉMARRAGE DES TESTS ADMIN DAO ");
    }

    @AfterAll
    static void cleanup() {
        System.out.println("NETTOYAGE ");
        if (idAdminTest > 0) {
            try {
                adminDAO.delete(idAdminTest);
                System.out.println("Admin " + idAdminTest + " supprimé");
            } catch (SQLException e) {
                System.out.println("Erreur nettoyage: " + e.getMessage());
            }
        }
    }

    @Test
    @Order(1)
    void testCreate() throws SQLException {
        System.out.println("Test 1: Création admin");

        // Créer un admin avec le mot de passe en clair (sera hashé par le DAO)
        Admin admin = new Admin(0, NOM_TEST, PRENOM_TEST, EMAIL_TEST, TEL_TEST, MDP_TEST);
        admin.setPlainPassword(MDP_TEST);

        adminDAO.create(admin);

        assertTrue(admin.getId() > 0, "L'ID devrait être généré");
        idAdminTest = admin.getId();

        System.out.println("Admin créé avec ID: " + idAdminTest);
    }

    @Test
    @Order(2)
    void testFindByEmail() throws SQLException {
        System.out.println("Test 2: Recherche par email");

        Admin admin = adminDAO.findByEmail(EMAIL_TEST);

        assertNotNull(admin, "L'admin devrait être trouvé");
        assertEquals(EMAIL_TEST, admin.getEmail());
        assertEquals(NOM_TEST, admin.getNom());
        assertEquals(PRENOM_TEST, admin.getPrenom());
        assertEquals(TEL_TEST, admin.getNumTel());

        System.out.println("Admin trouvé: " + admin.getEmail());
    }

    @Test
    @Order(3)
    void testAuthenticate() throws SQLException {
        System.out.println("Test 3: Authentification");

        // Test avec bons identifiants
        Admin admin = adminDAO.authenticate(EMAIL_TEST, MDP_TEST);
        assertNotNull(admin, "Authentification devrait réussir");
        assertEquals(EMAIL_TEST, admin.getEmail());
        System.out.println("Authentification réussie");

        // Test avec mauvais mot de passe
        Admin adminFail = adminDAO.authenticate(EMAIL_TEST, "mauvaisMdp");
        assertNull(adminFail, "Authentification devrait échouer");
        System.out.println("Échec authentification avec mauvais mdp");
    }

    @Test
    @Order(4)
    void testFindAll() throws SQLException {
        System.out.println("Test 4: Liste tous les admins");

        var admins = adminDAO.findAll();
        assertNotNull(admins);
        assertTrue(admins.size() > 0, "La liste ne devrait pas être vide");

        boolean trouve = admins.stream()
                .anyMatch(a -> a.getId() == idAdminTest);
        assertTrue(trouve, "L'admin de test devrait être dans la liste");

        System.out.println("✅ " + admins.size() + " admin(s) trouvé(s)");
    }

    @Test
    @Order(5)
    void testChangePassword() throws SQLException {
        System.out.println("Test 5: Changement mot de passe");

        String nouveauMdp = "nouveauMdp123";

        boolean change = adminDAO.changePassword(idAdminTest, MDP_TEST, nouveauMdp);
        assertTrue(change, "Le changement devrait réussir");
        System.out.println("Mot de passe changé");

        // Vérifie avec nouveau mot de passe
        Admin admin = adminDAO.authenticate(EMAIL_TEST, nouveauMdp);
        assertNotNull(admin, "Authentification avec nouveau mdp devrait réussir");
        System.out.println("Authentification avec nouveau mdp réussie");
    }

    @Test
    @Order(6)
    void testUpdate() throws SQLException {
        System.out.println("Test 6: Mise à jour admin");

        // Récupére l'admin
        Admin admin = adminDAO.findByEmail(EMAIL_TEST);
        assertNotNull(admin);

        // Modifie les champs
        String nouveauNom = "TestUpdate";
        String nouveauPrenom = "Update";
        String nouveauTel = "987654321";

        admin.setNom(nouveauNom);
        admin.setPrenom(nouveauPrenom);
        admin.setNumTel(nouveauTel);
        admin.setPlainPassword("nouveauMdpUpdate");

        adminDAO.update(admin);
        System.out.println("✅ Admin mis à jour");

        // Vérifie les modifications
        Admin adminModifie = adminDAO.findByEmail(EMAIL_TEST);
        assertEquals(nouveauNom, adminModifie.getNom());
        assertEquals(nouveauPrenom, adminModifie.getPrenom());
        assertEquals(nouveauTel, adminModifie.getNumTel());

        // Vérifie que le nouveau mot de passe fonctionne
        Admin loginTest = adminDAO.authenticate(EMAIL_TEST, "nouveauMdpUpdate");
        assertNotNull(loginTest, "Nouveau mot de passe devrait fonctionner");

        System.out.println("Vérifications réussies");
    }

    @Test
    @Order(7)
    void testEmailExistant() throws SQLException {
        System.out.println("Test 7: Vérification email existant");

        Admin existant = adminDAO.findByEmail(EMAIL_TEST);
        assertNotNull(existant, "L'email devrait exister");

        Admin inexistant = adminDAO.findByEmail("email@inexistant.com");
        assertNull(inexistant, "L'email ne devrait pas exister");

        System.out.println("Vérification email existant OK");
    }

    @Test
    @Order(8)
    void testDelete() throws SQLException {
        System.out.println("Test 8: Suppression admin");

        // Créer un admin temporaire
        String emailTemp = "temp@delete.com";
        Admin tempAdmin = new Admin(0, "Temp", "Delete", emailTemp, "111222333", "tempMdp");
        tempAdmin.setPlainPassword("tempMdp");
        adminDAO.create(tempAdmin);

        int idTemp = tempAdmin.getId();
        System.out.println("Admin temporaire créé avec ID: " + idTemp);

        // Supprimer
        adminDAO.delete(idTemp);
        System.out.println("Admin temporaire supprimé");

        // Vérifier que l'admin n'existe plus
        Admin verif = adminDAO.findByEmail(emailTemp);
        assertNull(verif, "L'admin ne devrait plus exister");

        System.out.println("Suppression vérifiée");
    }
}