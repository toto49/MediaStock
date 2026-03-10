package com.eseo.mediastock.service;

import com.eseo.mediastock.dao.EmpruntDAO;
import com.eseo.mediastock.dao.ExemplaireDAO;
import com.eseo.mediastock.model.Adherent;
import com.eseo.mediastock.model.Emprunt;
import com.eseo.mediastock.model.Enum.EnumDispo;
import com.eseo.mediastock.model.Exemplaire;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class EmpruntServiceTest {

    // Le service qu'on test
    private EmpruntService empruntService;

    // Des mocks (simulateurs) pour les DAO
    // Les mocks sont plus rapide et plus fiable que utiliser les dao qui dependent trop de l'environnement
    private EmpruntDAOMock empruntDAOMock;
    private ExemplaireDAOMock exemplaireDAOMock;

    // Des objets de test
    private Adherent adherent;
    private ExemplaireMock exemplaire;
    private Emprunt emprunt;

    // Constante pour le nombre max d'emprunts
    private static final int MAX_EMPRUNTS = 6;

    /**
     * S'exécute AVANT chaque test
     * Prépare l'environnement de test
     */
    @BeforeEach
    void setUp() {
        System.out.println(" Préparation du test");

        // Crée le service à tester
        empruntService = new EmpruntService();

        // Crée des mocks (simulateurs) pour les DAO
        empruntDAOMock = new EmpruntDAOMock();
        exemplaireDAOMock = new ExemplaireDAOMock();

        // Injecte les mocks dans le service
        try {
            java.lang.reflect.Field empruntDAOField = EmpruntService.class.getDeclaredField("empruntDAO");
            empruntDAOField.setAccessible(true);
            empruntDAOField.set(empruntService, empruntDAOMock);

            java.lang.reflect.Field exemplaireDAOField = EmpruntService.class.getDeclaredField("exemplaireDAO");
            exemplaireDAOField.setAccessible(true);
            exemplaireDAOField.set(empruntService, exemplaireDAOMock);
        } catch (Exception e) {
            fail("Impossible d'injecter les mocks: " + e.getMessage());
        }

        // Crée un adhérent de test (avec ID String)
        adherent = new Adherent("1", "Dupont", "Jean", "jean@email.com", "0123456789");

        // Crée un exemplaire de test
        exemplaire = new ExemplaireMock(); // Notre mock d'exemplaire

        // Crée un emprunt de test (attention : dans le service, on ne crée pas d'emprunt directement)
        // On va plutôt utiliser les méthodes du service

        System.out.println("Test prêt à s'exécuter");
    }

    /**
     * TEST : Vérifie qu'un adhérent peut emprunter dans des conditions normales
     */
    @Test
    void testPeutEmprunter_CasNormal() {
        System.out.println(" TEST : peutEmprunter");

        // Prépare l'exemplaire pour qu'il soit disponible et en bon état
        exemplaire.setDispo(true);
        exemplaire.setBonEtat(true);
        exemplaire.setStatut(EnumDispo.DISPONIBLE);

        // Exécute la méthode à tester
        boolean resultat = empruntService.peutEmprunter(adherent, exemplaire);

        // Vérifie le résultat
        assertTrue(resultat, "L'adhérent devrait pouvoir emprunter");
        System.out.println("Résultat: " + resultat);
    }

    /**
     * TEST : Vérifie qu'un adhérent ne peut pas emprunter s'il a atteint le maximum
     */
    @Test
    void testPeutEmprunter_MaxAtteint() {
        System.out.println("TEST : peutEmprunter - Max atteint");

        // Ajouter des emprunts à l'adhérent (simulés dans la liste)
        for (int i = 0; i < MAX_EMPRUNTS; i++) {
            // On crée des emprunts factices juste pour remplir la liste
            Emprunt e = new Emprunt(i, adherent, exemplaire, LocalDate.now(), LocalDate.now().plusMonths(1));
            adherent.getEmpruntsEnCours().add(e);
        }

        boolean resultat = empruntService.peutEmprunter(adherent, exemplaire);

        assertFalse(resultat, "L'adhérent NE DEVRAIT PAS pouvoir emprunter");
        assertEquals(MAX_EMPRUNTS, adherent.getNombreEmprunts());
        System.out.println("Résultat: " + resultat);
    }

    /**
     * TEST : Vérifie qu'un adhérent avec un emprunt en retard ne peut pas emprunter
     */
    @Test
    void testPeutEmprunter_EmpruntEnRetard() {
        System.out.println("TEST : peutEmprunter - Emprunt en retard");

        // Créer un emprunt avec date de retour passée
        Emprunt empruntEnRetard = new Emprunt(2, adherent, exemplaire,
                LocalDate.now().minusMonths(3),
                LocalDate.now().minusMonths(1));
        adherent.getEmpruntsEnCours().add(empruntEnRetard);

        boolean resultat = empruntService.peutEmprunter(adherent, exemplaire);

        assertFalse(resultat, "L'adhérent NE DEVRAIT PAS pouvoir emprunter");
        System.out.println("Résultat: " + resultat);
    }

    /**
     * TEST : Vérifie qu'on ne peut pas emprunter un exemplaire non disponible
     */
    @Test
    void testPeutEmprunter_ExemplaireNonDisponible() {
        System.out.println("TEST : peutEmprunter - Exemplaire non disponible");

        // L'exemplaire n'est pas disponible
        exemplaire.setDispo(false);
        exemplaire.setBonEtat(true);
        exemplaire.setStatut(EnumDispo.EMPRUNTE);

        boolean resultat = empruntService.peutEmprunter(adherent, exemplaire);

        assertFalse(resultat, "L'adhérent NE DEVRAIT PAS pouvoir emprunter");
        System.out.println("Résultat: " + resultat);
    }

    /**
     * TEST : Vérifie qu'on ne peut pas emprunter un exemplaire en mauvais état
     */
    @Test
    void testPeutEmprunter_ExemplaireMauvaisEtat() {
        System.out.println("TEST : peutEmprunter - Exemplaire en mauvais état");

        // L'exemplaire est disponible mais en mauvais état
        exemplaire.setDispo(true);
        exemplaire.setBonEtat(false);
        exemplaire.setStatut(EnumDispo.DISPONIBLE);

        boolean resultat = empruntService.peutEmprunter(adherent, exemplaire);

        assertFalse(resultat, "L'adhérent NE DEVRAIT PAS pouvoir emprunter");
        System.out.println("Résultat: " + resultat);
    }

    /**
     * TEST : Vérifie qu'on peut enregistrer un emprunt normal
     */
    @Test
    void testEnregistrerEmprunt_Succes() throws SQLException {
        System.out.println("TEST : enregistrerEmprunt - Succès");

        // Prépare l'exemplaire
        exemplaire.setDispo(true);
        exemplaire.setBonEtat(true);
        exemplaire.setStatut(EnumDispo.DISPONIBLE);
        exemplaire.setId(42); // On donne un ID à l'exemplaire

        // Compte le nombre d'emprunts avant
        int nbAvant = adherent.getEmpruntsEnCours().size();

        // Exécute
        empruntService.enregistrerEmprunt(adherent, exemplaire);

        // Vérifie qu'un emprunt a été ajouté à la liste de l'adhérent
        assertEquals(nbAvant + 1, adherent.getEmpruntsEnCours().size(),
                "Un emprunt devrait avoir été ajouté");

        // Vérifie que l'exemplaire a changé de statut
        assertEquals(EnumDispo.EMPRUNTE, exemplaire.getStatut(),
                "L'exemplaire devrait être marqué comme EMPRUNTE");

        // Vérifie que les DAO ont été appelés
        assertTrue(empruntDAOMock.createEmpruntAppele, "createEmprunt devrait être appelé");
        assertTrue(exemplaireDAOMock.updateAppele, "updateExemplaire devrait être appelé");

        // Vérifie les paramètres passés au DAO
        assertEquals(adherent, empruntDAOMock.lastAdherent, "L'adhérent passé devrait être le bon");
        assertEquals(exemplaire, empruntDAOMock.lastExemplaire, "L'exemplaire passé devrait être le bon");

        System.out.println("Emprunt enregistré avec succès");
    }

    /**
     * TEST : Vérifie qu'on ne peut pas enregistrer un emprunt si les conditions ne sont pas remplies
     */
    @Test
    void testEnregistrerEmprunt_NonAutorise() throws SQLException {
        System.out.println("TEST : enregistrerEmprunt - Non autorisé");

        // L'exemplaire n'est pas disponible
        exemplaire.setDispo(false);
        exemplaire.setStatut(EnumDispo.EMPRUNTE);

        // Compte avant
        int nbAvant = adherent.getEmpruntsEnCours().size();

        // Exécute
        empruntService.enregistrerEmprunt(adherent, exemplaire);

        // Vérifie que rien n'a changé
        assertEquals(nbAvant, adherent.getEmpruntsEnCours().size(),
                "Aucun emprunt ne devrait être ajouté");

        // Vérifie que les DAO n'ont PAS été appelés
        assertFalse(empruntDAOMock.createEmpruntAppele, "createEmprunt ne devrait PAS être appelé");
        assertFalse(exemplaireDAOMock.updateAppele, "updateExemplaire ne devrait PAS être appelé");

        System.out.println("Aucun emprunt créé (normal)");
    }

    /**
     * TEST : Vérifie qu'on peut enregistrer le retour d'un emprunt
     */
    @Test
    void testEnregistrerRetour_Succes() throws SQLException {
        System.out.println("TEST : enregistrerRetour - Succès");

        // Crée un emprunt pour l'adhérent
        Emprunt nouvelEmprunt = new Emprunt(10, adherent, exemplaire,
                LocalDate.now().minusMonths(1),
                LocalDate.now().plusMonths(1));
        adherent.getEmpruntsEnCours().add(nouvelEmprunt);

        // L'exemplaire était emprunté
        exemplaire.setStatut(EnumDispo.EMPRUNTE);

        // Compte avant
        int nbAvant = adherent.getEmpruntsEnCours().size();

        // Exécute
        empruntService.enregistrerRetour(adherent, nouvelEmprunt);

        // Vérifie que l'emprunt a été retiré
        assertEquals(nbAvant - 1, adherent.getEmpruntsEnCours().size(),
                "L'emprunt devrait être retiré");

        // Vérifie que l'exemplaire est redevenu disponible
        assertEquals(EnumDispo.DISPONIBLE, exemplaire.getStatut(),
                "L'exemplaire devrait être DISPONIBLE");

        // Vérifie que les DAO ont été appelés
        assertTrue(empruntDAOMock.saveRetourAppele, "saveRetour devrait être appelé");
        assertEquals(nouvelEmprunt, empruntDAOMock.lastEmprunt, "L'emprunt passé devrait être le bon");

        System.out.println("Retour enregistré avec succès");
    }

    /**
     * Mock pour Exemplaire
     * Simule le comportement d'un vrai exemplaire sans base de données
     */
    class ExemplaireMock extends Exemplaire {
        private boolean dispo = true;
        private boolean bonEtat = true;
        private EnumDispo statut = EnumDispo.DISPONIBLE;
        private int id = 1;

        public ExemplaireMock() {
            super(0, null, "codeBarre", null, null);
        }

        @Override
        public boolean estDispo() { return dispo; }

        @Override
        public boolean estBonEtat() { return bonEtat; }

        @Override
        public EnumDispo getStatusDispo() { return statut; }

        @Override
        public void setStatusDispo(EnumDispo nouveauStatut) {
            this.statut = nouveauStatut;
        }

        @Override
        public int getId() { return id; }

        // Méthodes pour configurer le mock
        public void setDispo(boolean d) { this.dispo = d; }
        public void setBonEtat(boolean b) { this.bonEtat = b; }
        public void setStatut(EnumDispo s) { this.statut = s; }
        public EnumDispo getStatut() { return statut; }
        public void setId(int i) { this.id = i; }
    }

    /**
     * Mock pour EmpruntDAO
     * Simule les appels à la base de données
     */
    class EmpruntDAOMock extends EmpruntDAO {
        boolean createEmpruntAppele = false;
        boolean saveRetourAppele = false;

        // Pour vérifier les paramètres
        Adherent lastAdherent = null;
        Exemplaire lastExemplaire = null;
        Emprunt lastEmprunt = null;

        @Override
        public void createEmprunt(Adherent adherent, Exemplaire exemplaire) throws SQLException {
            createEmpruntAppele = true;
            lastAdherent = adherent;
            lastExemplaire = exemplaire;
            System.out.println("Mock: createEmprunt appelé avec adherent=" + adherent.getId() +
                    ", exemplaire=" + exemplaire.getId());
        }

        @Override
        public void saveRetour(Emprunt emprunt) throws SQLException {
            saveRetourAppele = true;
            lastEmprunt = emprunt;
            System.out.println("Mock: saveRetour appelé avec emprunt id=" + emprunt.getId());
        }
    }

    /**
     * Mock pour ExemplaireDAO
     */
    class ExemplaireDAOMock extends ExemplaireDAO {
        boolean updateAppele = false;
        Exemplaire lastExemplaire = null;

        @Override
        public void updateExemplaire(Exemplaire exemplaire) throws SQLException {
            updateAppele = true;
            lastExemplaire = exemplaire;
            System.out.println("Mock: updateExemplaire appelé");
        }
    }
}