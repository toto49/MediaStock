package com.eseo.mediastock.dao;

import com.eseo.mediastock.model.Admin;
import org.junit.jupiter.api.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AdminDAOTest {

    private static AdminDAO dao;
    private static Integer idTest; // ID de l'admin créé pour les tests

    // Compteur pour des emails uniques
    private static AtomicInteger compteur = new AtomicInteger(1);

    // Liste pour stocker les IDs créés pendant les tests
    private static final List<Integer> idsACleaner = new ArrayList<>();

    @BeforeAll
    static void setup() {
        dao = new AdminDAO();
        System.out.println(" Initialisation du DAO Admin");
    }

    @AfterAll
    static void nettoyage() throws SQLException {
        System.out.println(" NETTOYAGE AUTOMATIQUE ");
        System.out.println("Suppression des " + idsACleaner.size() + " admins de test...");

        int supprime = 0;
        int erreurs = 0;

        for (Integer id : idsACleaner) {
            try {
                dao.delete(id);
                System.out.println("Supprimé: admin ID " + id);
                supprime++;
            } catch (Exception e) {
                System.out.println("Erreur pour admin ID " + id + ": " + e.getMessage());
                erreurs++;
            }
        }

        System.out.println(" Bilan: " + supprime + " supprimés, " + erreurs + " erreurs");
        System.out.println(" FIN NETTOYAGE ");

        idsACleaner.clear();
    }

    @BeforeEach
    void setUp() {
        System.out.println(" Nouveau test");
    }

    @AfterEach
    void tearDown() {
        System.out.println(" Test terminé ");
    }

    /**
     * Génère un email unique pour les tests
     */
    private String genererEmailUnique() {
        return "admin.test" + compteur.getAndIncrement() + "@test.com";
    }

    /**
     * Test : Création d'un admin
     */
    @Test
    @Order(1)
    void testCreate() throws SQLException {
        System.out.println("Test CREATE - Création d'un admin");

        String email = genererEmailUnique();
        String mdp = "password123";

        // Créer un admin avec id=0 (sera généré par la base)
        Admin admin = new Admin(0, email, mdp);

        dao.create(admin);

        // Vérifier que l'ID a été généré
        assertTrue(admin.getId() > 0);
        idTest = admin.getId();
        idsACleaner.add(idTest);

        System.out.println(" Admin créé avec ID: " + idTest);
        System.out.println("Email: " + admin.getEmail());
    }

    /**
     * Test : Authentification
     */
    @Test
    @Order(2)
    void testAuthenticate() throws SQLException {
        System.out.println("Test AUTHENTICATE");

        assertNotNull(idTest, "ID non disponible - le test 1 a échoué");

        // Récupére l'admin créé
        String email = "admin.test1@test.com"; // Email du test 1
        String mdp = "password123";

        Admin trouve = dao.authenticate(email, mdp);

        assertNotNull(trouve);
        assertEquals(email, trouve.getEmail());
        assertEquals(mdp, trouve.getMdp());

        System.out.println("Authentification réussie pour: " + trouve.getEmail());
    }

    /**
     * Test: Authentification avec mauvais mot de passe
     */
    @Test
    @Order(3)
    void testAuthenticateWrongPassword() throws SQLException {
        System.out.println("Test AUTHENTICATE - mauvais mot de passe");

        String email = "admin.test1@test.com";
        String mauvaisMdp = "wrongpassword";

        Admin trouve = dao.authenticate(email, mauvaisMdp);

        assertNull(trouve);

        System.out.println(" Authentification refusée");
    }

    /**
     * Test : Mise à jour d'un admin
     */
    @Test
    @Order(5)
    void testUpdate() throws SQLException {
        System.out.println("Test UPDATE");

        assertNotNull(idTest);

        // Crée un admin avec les nouvelles valeurs
        String nouvelEmail = genererEmailUnique();
        String nouveauMdp = "nouveauPassword456";

        Admin adminModifie = new Admin(idTest, nouvelEmail, nouveauMdp);

        dao.update(adminModifie);

        // Vérifie la mise à jour en s'authentifiant
        Admin verif = dao.authenticate(nouvelEmail, nouveauMdp);
        assertNotNull(verif);
        assertEquals(idTest, verif.getId());
        assertEquals(nouvelEmail, verif.getEmail());
        assertEquals(nouveauMdp, verif.getMdp());

        System.out.println(" Admin mis à jour avec succès");
        System.out.println("  Nouvel email: " + nouvelEmail);
    }

    /**
     * Test : Changement de mot de passe
     */
    @Test
    @Order(6)
    void testChangePassword() throws SQLException {
        System.out.println("Test CHANGE PASSWORD");

        assertNotNull(idTest);

        // Récupére l'email actuel
        String email = "admin.test2@test.com"; // Email après update
        String ancienMdp = "nouveauPassword456";
        String nouveauMdp = "password789";

        // Change le mot de passe
        boolean changed = dao.changePassword(idTest, ancienMdp, nouveauMdp);
        assertTrue(changed);

        // Vérifie avec l'ancien mot de passe (doit échouer)
        Admin ancien = dao.authenticate(email, ancienMdp);
        assertNull(ancien);

        // Vérifie avec le nouveau mot de passe (doit réussir)
        Admin nouveau = dao.authenticate(email, nouveauMdp);
        assertNotNull(nouveau);
        assertEquals(idTest, nouveau.getId());

        System.out.println(" Mot de passe changé avec succès");
    }

    /**
     * Test : Récupérer tous les admins
     */
    @Test
    @Order(7)
    void testFindAll() throws SQLException {
        System.out.println("Test FIND ALL");

        List<Admin> liste = dao.findAll();

        assertNotNull(liste);
        assertTrue(liste.size() > 0);

        System.out.println("   ✅ " + liste.size() + " admin(s) trouvé(s)");

        // Afficher les admins
        int count = 0;
        for (Admin a : liste) {
            if (count < 5) {
                System.out.println("      - ID " + a.getId() + " : " + a.getEmail());
                count++;
            }
        }
    }

    /**
     * Test: Création de plusieurs admins
     */
    @Test
    @Order(8)
    void testCreationsMultiples() throws SQLException {
        System.out.println("Test créations multiples - Ajout de 3 admins");

        // Créer 3 admins
        Admin a1 = new Admin(0, genererEmailUnique(), "mdp1");
        Admin a2 = new Admin(0, genererEmailUnique(), "mdp2");
        Admin a3 = new Admin(0, genererEmailUnique(), "mdp3");

        dao.create(a1);
        dao.create(a2);
        dao.create(a3);

        assertTrue(a1.getId() > 0);
        assertTrue(a2.getId() > 0);
        assertTrue(a3.getId() > 0);

        idsACleaner.add(a1.getId());
        idsACleaner.add(a2.getId());
        idsACleaner.add(a3.getId());

        System.out.println(" 3 admins créés avec succès");
        System.out.println(" IDs : " + a1.getId() + ", " + a2.getId() + ", " + a3.getId());

        // Vérifier qu'ils existent
        assertNotNull(dao.authenticate(a1.getEmail(), "mdp1"));
        assertNotNull(dao.authenticate(a2.getEmail(), "mdp2"));
        assertNotNull(dao.authenticate(a3.getEmail(), "mdp3"));
    }

    /**
     * Test: Test avec email invalide
     */
    @Test
    @Order(9)
    void testEmailInvalide() throws SQLException {
        System.out.println("Test email invalide");

        Admin trouve = dao.authenticate("email.inexistant@test.com", "mdp");

        assertNull(trouve);

        System.out.println("Email inexistant retourne null");
    }

    /**
     * Test : Suppression d'un admin
     */
    @Test
    @Order(10)
    void testDelete() throws SQLException {
        System.out.println("🗑Test DELETE");

        // Crée un admin spécifique pour la suppression
        String emailSuppr = genererEmailUnique();
        Admin aSupprimer = new Admin(0, emailSuppr, "asupprimer");
        dao.create(aSupprimer);
        assertTrue(aSupprimer.getId() > 0);
        System.out.println(" Admin créé pour suppression: ID " + aSupprimer.getId());

        // Vérifie qu'il existe
        Admin avant = dao.authenticate(emailSuppr, "asupprimer");
        assertNotNull(avant);

        // Supprime
        dao.delete(aSupprimer.getId());

        // Vérifie qu'il n'existe plus
        Admin apres = dao.authenticate(emailSuppr, "asupprimer");
        assertNull(apres);

        System.out.println(" Admin supprimé avec succès");

    }
}