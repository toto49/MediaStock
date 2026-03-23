package com.eseo.mediastock.dao;

import com.eseo.mediastock.model.Admin;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PLAN DE TEST - AdminDAOTest
 * ===================================================
 * <p>
 * OBJECTIF : Vérifier le bon fonctionnement de toutes les méthodes CRUD
 * de la classe AdminDAO (Create, Read, Update, Delete, Authentification)
 * <p>
 * ENVIRONNEMENT DE TEST :
 * - Base de données : test (nettoyage après exécution)
 * - Données : constantes définies (EMAIL_TEST, MDP_TEST, etc.)
 * - Nettoyage : suppression de l'admin de test après tous les tests
 * - Ordre : défini par @Order (dépendances entre tests)
 * <p>
 * COUVERTURE DES TESTS :
 * - testCreate           → CREATE
 * - testFindByEmail      → READ (par email)
 * - testAuthenticate     → AUTHENTIFICATION (réussite/échec)
 * - testFindAll          → READ (tous)
 * - testChangePassword   → UPDATE (mot de passe)
 * - testUpdate           → UPDATE (tous champs)
 * - testEmailExistant    → READ (email existant/inexistant)
 * - testDelete           → DELETE
 * <p>
 * NETTOYAGE AUTOMATIQUE :
 * - L'admin créé au test 1 est supprimé à la fin
 * - Un admin temporaire est créé et supprimé dans testDelete
 * ===================================================
 */
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

    /**
     * Sets .
     */
    @BeforeAll
    static void setup() {
        adminDAO = new AdminDAO();
        System.out.println("DÉMARRAGE DES TESTS ADMIN DAO ");
    }

    /**
     * NETTOYAGE FINAL
     * Supprime l'admin créé pendant les tests (si existant)
     * Exécuté une seule fois après tous les tests
     */
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

    /**
     * ===================================================
     * TEST 1 : CREATE - Création d'un administrateur
     * ===================================================
     * <p>
     * OBJECTIF : Vérifier l'insertion d'un nouvel administrateur
     * <p>
     * DONNÉES :
     * - ID : 0 (auto-généré par la base)
     * - Nom : NOM_TEST ("Test")
     * - Prénom : PRENOM_TEST ("DAO")
     * - Email : EMAIL_TEST ("testdao@admin.com")
     * - Téléphone : TEL_TEST (123456789)
     * - Mot de passe : MDP_TEST ("password123") + setPlainPassword()
     * <p>
     * RÉSULTAT ATTENDU :
     * - L'admin est créé avec un ID > 0 généré par la base
     * <p>
     * VÉRIFICATIONS :
     * - assertTrue(admin.getId() > 0) → ID généré
     * <p>
     * DÉPENDANCES :
     * - Aucune (premier test)
     * ===================================================
     *
     * @throws SQLException the sql exception
     */
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

    /**
     * ===================================================
     * TEST 2 : READ - Recherche par email
     * ===================================================
     * <p>
     * OBJECTIF : Vérifier la récupération d'un admin par son email
     * <p>
     * DONNÉES :
     * - Email : EMAIL_TEST ("testdao@admin.com")
     * <p>
     * RÉSULTAT ATTENDU :
     * - Admin trouvé avec toutes ses données intactes
     * <p>
     * VÉRIFICATIONS :
     * - assertNotNull(admin) → admin trouvé
     * - assertEquals sur tous les champs (email, nom, prénom, téléphone)
     * <p>
     * PRÉ-REQUIS :
     * - Le test 1 doit avoir réussi (admin créé avec EMAIL_TEST)
     * ===================================================
     *
     * @throws SQLException the sql exception
     */
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

    /**
     * ===================================================
     * TEST 3 : AUTHENTIFICATION - Connexion admin
     * ===================================================
     * <p>
     * OBJECTIF : Vérifier l'authentification avec hash de mot de passe
     * <p>
     * SCÉNARIOS TESTÉS :
     * 1. Authentification réussie (bons identifiants)
     * 2. Authentification échouée (mauvais mot de passe)
     * <p>
     * DONNÉES :
     * - Email correct : EMAIL_TEST
     * - Bon mot de passe : MDP_TEST
     * - Mauvais mot de passe : "mauvaisMdp"
     * <p>
     * RÉSULTATS ATTENDUS :
     * - Bons identifiants → admin retourné
     * - Mauvais mot de passe → null
     * <p>
     * VÉRIFICATIONS :
     * - assertNotNull(admin)
     * - assertNull(adminFail)
     * <p>
     * PRÉ-REQUIS :
     * - Le test 1 doit avoir réussi (admin créé)
     * ===================================================
     *
     * @throws SQLException the sql exception
     */
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

    /**
     * ===================================================
     * TEST 4 : READ - Liste de tous les administrateurs
     * ===================================================
     * <p>
     * OBJECTIF : Vérifier la récupération de tous les admins
     * <p>
     * RÉSULTAT ATTENDU :
     * - Liste non null
     * - Liste non vide
     * - L'admin de test est présent dans la liste
     * <p>
     * VÉRIFICATIONS :
     * - assertNotNull(admins)
     * - assertTrue(admins.size() > 0)
     * - anyMatch(a -> a.getId() == idAdminTest)
     * <p>
     * PRÉ-REQUIS :
     * - Le test 1 doit avoir réussi (au moins un admin en base)
     * ===================================================
     *
     * @throws SQLException the sql exception
     */
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

    /**
     * ===================================================
     * TEST 5 : UPDATE - Changement de mot de passe
     * ===================================================
     * <p>
     * OBJECTIF : Vérifier le changement de mot de passe avec hash
     * <p>
     * DONNÉES :
     * - ID : idAdminTest
     * - Ancien mot de passe : MDP_TEST
     * - Nouveau mot de passe : "nouveauMdp123"
     * <p>
     * PROCÉDURE :
     * 1. Changer le mot de passe
     * 2. Vérifier le succès (true)
     * 3. Authentifier avec le nouveau mot de passe
     * 4. Vérifier que ça fonctionne
     * <p>
     * RÉSULTATS ATTENDUS :
     * - change = true
     * - Authentification avec nouveau mdp réussie
     * <p>
     * VÉRIFICATIONS :
     * - assertTrue(change)
     * - assertNotNull(admin) après auth avec nouveau mdp
     * <p>
     * PRÉ-REQUIS :
     * - Le test 1 doit avoir réussi (idAdminTest défini)
     * ===================================================
     *
     * @throws SQLException the sql exception
     */
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

    /**
     * ===================================================
     * TEST 6 : UPDATE - Mise à jour de tous les champs
     * ===================================================
     * <p>
     * OBJECTIF : Vérifier la mise à jour de toutes les informations d'un admin
     * <p>
     * DONNÉES MODIFIÉES :
     * - Nom : "TestUpdate" (au lieu de NOM_TEST)
     * - Prénom : "Update" (au lieu de PRENOM_TEST)
     * - Téléphone : 987654321 (au lieu de TEL_TEST)
     * - Mot de passe : "nouveauMdpUpdate"
     * <p>
     * PROCÉDURE :
     * 1. Récupérer l'admin
     * 2. Modifier tous les champs
     * 3. Appeler update()
     * 4. Vérifier que les modifications sont persistées
     * <p>
     * VÉRIFICATIONS :
     * - assertEquals sur nom, prénom, téléphone
     * - Authentification réussie avec nouveau mot de passe
     * <p>
     * PRÉ-REQUIS :
     * - Le test 1 doit avoir réussi (admin existant)
     * ===================================================
     *
     * @throws SQLException the sql exception
     */
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

    /**
     * ===================================================
     * TEST 7 : READ - Vérification email existant/inexistant
     * ===================================================
     * <p>
     * OBJECTIF : Vérifier le comportement de findByEmail avec
     * emails existants et inexistants
     * <p>
     * DONNÉES :
     * - Email existant : EMAIL_TEST
     * - Email inexistant : "email@inexistant.com"
     * <p>
     * RÉSULTATS ATTENDUS :
     * - Email existant → admin trouvé
     * - Email inexistant → null
     * <p>
     * VÉRIFICATIONS :
     * - assertNotNull(existant)
     * - assertNull(inexistant)
     * <p>
     * PRÉ-REQUIS :
     * - Le test 1 doit avoir réussi (admin avec EMAIL_TEST créé)
     * ===================================================
     *
     * @throws SQLException the sql exception
     */
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

    /**
     * ===================================================
     * TEST 8 : DELETE - Suppression d'un administrateur
     * ===================================================
     * <p>
     * OBJECTIF : Vérifier la suppression d'un admin
     * <p>
     * PROCÉDURE :
     * 1. Créer un admin temporaire (emailTemp)
     * 2. Vérifier qu'il existe
     * 3. Le supprimer avec delete()
     * 4. Vérifier qu'il n'existe plus
     * <p>
     * DONNÉES TEMPORAIRES :
     * - Email : "temp@delete.com"
     * - Nom : "Temp", Prénom : "Delete"
     * - Téléphone : 111222333
     * - Mot de passe : "tempMdp"
     * <p>
     * RÉSULTATS ATTENDUS :
     * - Suppression réussie (pas d'exception)
     * - Admin introuvable après suppression
     * <p>
     * VÉRIFICATIONS :
     * - assertNull(verif) après suppression
     * <p>
     * PRÉ-REQUIS :
     * - Aucun (test indépendant)
     * ===================================================
     *
     * @throws SQLException the sql exception
     */
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