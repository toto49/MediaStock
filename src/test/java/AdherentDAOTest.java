import com.eseo.mediastock.dao.AdherentDAO;
import com.eseo.mediastock.model.Adherent;
import org.junit.jupiter.api.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    public class AdherentDAOTest {

        private static AdherentDAO dao;
        private static String idTest;

        //liste pour stocker les id créer pendant les tests
    private static List<String> idsACleaner = new ArrayList<>();

    @BeforeAll
        static void setup() {
            dao = new AdherentDAO();
        }

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

        System.out.println("Bilan: " + supprime + " supprimés, " + erreurs + " erreurs");
        System.out.println(" FIN NETTOYAGE");

        // Vider la liste après nettoyage
        idsACleaner.clear();
    }

    @BeforeEach
    void setUp() {
        System.out.println("\n--- Nouveau test ---");
    }

    @AfterEach
    void tearDown() {
        System.out.println("--- Test terminé ---");
    }

    /**
     * Test : Création d'un adhérent
     */
        @Test
        @Order(1)
        void testCreateAdherent() throws SQLException {
            // Créer avec un Id unique en string
            Adherent a = new Adherent("ADH-2754654", "LicornTest", "MorgianeTest", "email@test.com", "0123456789");
            //sauvegarder dans la db
            dao.createAdherent(a);

            // Vérifier id pas null
            assertNotNull(a.getId());
            idTest = a.getId(); //stock id pour les tests suivant

            //ajout de id a la liste de nettoyage
            idsACleaner.add(idTest);

            System.out.println("adherent créer" + idTest);
            System.out.println("nom : " + a.getNom() + " " + a.getPrenom());
            System.out.println("email " + a.getEmailContact());
            System.out.println("tel : " + a.getNumTel());
        }
    /**
     * Test : Recherche par ID
     * Vérifie qu'on retrouve l'adhérent créé
     */

        @Test
        @Order(2)
        void testFindById() throws SQLException {
            assertNotNull(idTest, "id non disponible ou non créer");

            // Chercher l'adherent par id
            Adherent trouve = dao.findById(idTest);

            // Vérifier qu'on l'a trouver
            assertNotNull(trouve);
            assertEquals(idTest, trouve.getId());
            assertEquals("LicornTest", trouve.getNom());
            assertEquals("MorgianeTest", trouve.getPrenom());
            assertEquals("0123456789", trouve.getNumTel());
            assertEquals("email@test.com", trouve.getEmailContact());

            System.out.println("trouver : " + trouve.getId());
            System.out.println("trouvere : " + trouve.getNom());
        }

    /**
     * Testv : Mise à jour d'un adhérent
     * Modifie les données de l'adhérent créé
     */

        @Test
        @Order(3)
        void testUpdateAdherent() throws SQLException {
            assertNotNull(idTest);

            // Récupérer id adherent
            Adherent a = dao.findById(idTest);
            assertNotNull(a);

            //afficher les anciennes valeur
            System.out.println("anciennes valeurs" );
            System.out.println("nom : " + a.getNom() + " " + a.getPrenom());
            System.out.println("email " + a.getEmailContact());

            // Modifier
            a.setNom("LicornTestmodif");

            // sauvegarder les nouvelles données
            dao.updateAdherent(a);

            // Vérifie les chgmt
            Adherent modifie = dao.findById(idTest);
            assertEquals("LicornTestmodif", modifie.getNom());

            System.out.println("Mise a jour réalisée" );
            System.out.println("nom : " + modifie.getNom());
        }

    /**
     * Test: Test avec différentes valeurs
     */
    @Test
    @Order(4)
    void testValeursSpeciales() throws SQLException {
        System.out.println("📝 Test valeurs spéciales");

        // Test avec des caractères accentués
        Adherent accent = new Adherent("ADH-",
                "Étienne", "François", "etienne@email.com", "+33 6 12 34 56 78");
        dao.createAdherent(accent);
        assertNotNull(accent.getId());
        idsACleaner.add(accent.getId());
        System.out.println("   ✅ Adhérent avec accents créé: " + accent.getId());

        // Test avec des chiffres dans le nom
        Adherent chiffres = new Adherent("ADH-841",
                "User123", "Test456", "chiffres@email.com", "0612345678");
        dao.createAdherent(chiffres);
        assertNotNull(chiffres.getId());
        idsACleaner.add(chiffres.getId());
        System.out.println("   ✅ Adhérent avec chiffres créé: " + chiffres.getId());

        // Test avec email long
        Adherent longEmail = new Adherent("ADH-9849849",
                "Long", "Email", "email.tres.tres.long@test.domain.com", "0612345678");
        dao.createAdherent(longEmail);
        assertNotNull(longEmail.getId());
        idsACleaner.add(longEmail.getId());
        System.out.println("   ✅ Adhérent avec email long créé: " + longEmail.getId());


    }

    /**
     * Test : Création de plusieurs adhérents
     */
    @Test
    @Order(5)
    void testCreationsMultiples() throws SQLException {
        System.out.println("📝 Test créations multiples - Ajout de 3 adhérents");

        // Créer 3 adhérents avec des IDs uniques

        Adherent a1 = new Adherent("ADH-4894",
                "Test1", "User1", "test1@email.com", "1111111111");
        Adherent a2 = new Adherent("ADH-6548465",
                "Test2", "User2", "test2@email.com", "2222222222");
        Adherent a3 = new Adherent("ADH-95591",
                "Test3", "User3", "test3@email.com", "3333333333");

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

        System.out.println("   ✅ 3 adhérents créés avec succès");
        System.out.println("      IDs: " + a1.getId() + ", " + a2.getId() + ", " + a3.getId());

        // Vérifier qu'on peut les retrouver
        Adherent trouve1 = dao.findById(a1.getId());
        Adherent trouve2 = dao.findById(a2.getId());
        Adherent trouve3 = dao.findById(a3.getId());

        assertNotNull(trouve1);
        assertNotNull(trouve2);
        assertNotNull(trouve3);

        System.out.println("   ✅ Tous retrouvés individuellement");

    }

    /**
     * Test : Test de suppression directe
     */
    @Test
    @Order(6)
    void testDeleteAdherent() throws SQLException {
        System.out.println("Test DELETE - Suppression d'un adhérent");

        // Créer un adhérent pour le test de suppression
        String idASupprimer = "ADH-A-SUPPRIMER-" + System.currentTimeMillis();
        Adherent aSupprimer = new Adherent(idASupprimer, "ASupprimer", "Test", "delete@test.com", "0000000000");
        dao.createAdherent(aSupprimer);
        assertNotNull(aSupprimer.getId());
        System.out.println("Adhérent créé pour test suppression: " + aSupprimer.getId());

        // Vérifie qu'il existe
        Adherent avant = dao.findById(aSupprimer.getId());
        assertNotNull(avant);

        // Supprime
        boolean supprime = dao.deleteAdherent(aSupprimer.getId());
        assertTrue(supprime);

        // Vérifie qu'il n'existe plus
        Adherent apres = dao.findById(aSupprimer.getId());
        assertNull(apres);

        System.out.println("Adhérent " + aSupprimer.getId() + " supprimé avec succès");

    }
}



