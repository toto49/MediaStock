package com.eseo.mediastock.dao;

import com.eseo.mediastock.model.Admin;
import com.eseo.mediastock.service.PasswordUtilService;
import org.junit.jupiter.api.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AdminDAOTest {

    private static AdminDAO dao;
    private static Integer idTest;
    private static AtomicInteger compteur = new AtomicInteger(1);
    private static final List<Integer> idsACleaner = new ArrayList<>();
    private static String emailTest;

    @BeforeAll
    static void setup() throws SQLException {
        dao = new AdminDAO();
        System.out.println(" Initialisation du DAO Admin");

        // NETTOYAGE : Supprimer tous les anciens admins de test
        List<Admin> tous = dao.findAll();
        System.out.println("Nettoyage des anciens admins de test...");
        for (Admin a : tous) {
            if (a.getEmail().startsWith("admin.test")) {
                dao.delete(a.getId());
                System.out.println("  Supprimé ancien admin: ID=" + a.getId() + " Email=" + a.getEmail());
            }
        }
    }

    @AfterAll
    static void nettoyage() throws SQLException {
        System.out.println(" NETTOYAGE AUTOMATIQUE ");
        for (Integer id : idsACleaner) {
            try {
                dao.delete(id);
                System.out.println("Supprimé: admin ID " + id);
            } catch (Exception e) {
                System.out.println("Erreur pour admin ID " + id);
            }
        }
        idsACleaner.clear();
        System.out.println(" FIN NETTOYAGE ");
    }

    private String genererEmailUnique() {
        // Ajoute un timestamp pour garantir l'unicité
        long timestamp = System.currentTimeMillis();
        return "admin.test" + compteur.getAndIncrement() + "_" + timestamp + "@test.com";
    }

    @Test
    @Order(1)
    void testCreate() throws SQLException {
        System.out.println("Test CREATE");
        emailTest = genererEmailUnique();
        Admin admin = new Admin(0, emailTest, "password123");
        dao.create(admin);
        assertTrue(admin.getId() > 0);
        idTest = admin.getId();
        idsACleaner.add(idTest);
        System.out.println(" Admin créé ID: " + idTest + " Email: " + emailTest);
    }

    @Test
    @Order(2)
    void testAuthenticate() throws SQLException {
        System.out.println("Test AUTHENTICATE");

        // Petite pause
        try { Thread.sleep(100); } catch (InterruptedException e) {}

        Admin trouve = dao.authenticate(emailTest, "password123");
        assertNotNull(trouve, "L'authentification a échoué pour " + emailTest);
        assertEquals(emailTest, trouve.getEmail());
        System.out.println(" Authentification OK");
    }

    @Test
    @Order(3)
    void testAuthenticateWrongPassword() throws SQLException {
        System.out.println("Test AUTHENTICATE wrong password");
        assertNull(dao.authenticate(emailTest, "wrongpassword"));
        System.out.println(" Authentification refusée OK");
    }

    @Test
    @Order(4)
    void testUpdate() throws SQLException {
        System.out.println("Test UPDATE");
        String nouvelEmail = genererEmailUnique();
        Admin adminModifie = new Admin(idTest, nouvelEmail, "nouveauPassword456");
        dao.update(adminModifie);

        // Petite pause
        try { Thread.sleep(100); } catch (InterruptedException e) {}

        Admin verif = dao.authenticate(nouvelEmail, "nouveauPassword456");
        assertNotNull(verif, "La mise à jour a échoué");
        assertEquals(idTest, verif.getId());
        assertEquals(nouvelEmail, verif.getEmail());
        emailTest = nouvelEmail;
        System.out.println(" Update OK - Nouvel email: " + nouvelEmail);
    }

    @Test
    @Order(5)
    void testChangePassword() throws SQLException {
        System.out.println("Test CHANGE PASSWORD");

        // Vérifier d'abord que l'ancien mot de passe fonctionne
        Admin avant = dao.authenticate(emailTest, "nouveauPassword456");
        assertNotNull(avant, "Impossible de s'authentifier avant changement");

        boolean changed = dao.changePassword(idTest, "nouveauPassword456", "password789");
        assertTrue(changed, "Le changement de mot de passe a échoué");

        // Petite pause
        try { Thread.sleep(100); } catch (InterruptedException e) {}

        assertNull(dao.authenticate(emailTest, "nouveauPassword456"), "L'ancien mot de passe fonctionne encore");
        assertNotNull(dao.authenticate(emailTest, "password789"), "Le nouveau mot de passe ne fonctionne pas");
        System.out.println(" Changement mot de passe OK");
    }

    @Test
    @Order(6)
    void testFindAll() throws SQLException {
        System.out.println("Test FIND ALL");
        List<Admin> liste = dao.findAll();
        assertNotNull(liste);
        assertTrue(liste.size() > 0);
        System.out.println(" " + liste.size() + " admin(s) trouvé(s)");
    }

    @Test
    @Order(7)
    void testCreationsMultiples() throws SQLException {
        System.out.println("Test créations multiples");
        Admin a1 = new Admin(0, genererEmailUnique(), "mdp1");
        Admin a2 = new Admin(0, genererEmailUnique(), "mdp2");
        Admin a3 = new Admin(0, genererEmailUnique(), "mdp3");

        dao.create(a1);
        dao.create(a2);
        dao.create(a3);

        idsACleaner.add(a1.getId());
        idsACleaner.add(a2.getId());
        idsACleaner.add(a3.getId());

        assertNotNull(dao.authenticate(a1.getEmail(), "mdp1"), "Admin 1 non trouvé");
        assertNotNull(dao.authenticate(a2.getEmail(), "mdp2"), "Admin 2 non trouvé");
        assertNotNull(dao.authenticate(a3.getEmail(), "mdp3"), "Admin 3 non trouvé");
        System.out.println(" 3 admins créés avec succès");
    }

    @Test
    @Order(8)
    void testEmailInvalide() throws SQLException {
        System.out.println("Test email invalide");
        assertNull(dao.authenticate("email.inexistant@test.com", "mdp"));
        System.out.println(" OK");
    }

    @Test
    @Order(9)
    void testDelete() throws SQLException {
        System.out.println("Test DELETE");
        String emailSuppr = genererEmailUnique();
        Admin aSupprimer = new Admin(0, emailSuppr, "asupprimer");
        dao.create(aSupprimer);
        int idSuppr = aSupprimer.getId();

        assertNotNull(dao.authenticate(emailSuppr, "asupprimer"), "Admin non trouvé avant suppression");
        dao.delete(idSuppr);

        assertNull(dao.authenticate(emailSuppr, "asupprimer"), "Admin encore trouvé après suppression");
        System.out.println(" Suppression OK - ID: " + idSuppr);
    }

    @Test
    @Order(0)
    void testPasswordUtil() {
        System.out.println("Test PASSWORD UTIL");

        String mdpClair = "password123";
        String hash = PasswordUtilService.hash(mdpClair);

        System.out.println("Mot de passe clair: '" + mdpClair + "'");
        System.out.println("Hash généré: '" + hash + "'");

        boolean verification = PasswordUtilService.verify(hash, mdpClair);
        System.out.println("Vérification: " + (verification ? "RÉUSSIE" : "ÉCHEC"));

        assertTrue(verification, "PasswordUtil ne fonctionne pas correctement");
    }
}