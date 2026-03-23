package com.eseo.mediastock.service;

import com.eseo.mediastock.dao.EmpruntDAO;
import com.eseo.mediastock.dao.ExemplaireDAO;
import com.eseo.mediastock.model.Adherent;
import com.eseo.mediastock.model.Emprunt;
import com.eseo.mediastock.model.Enum.EnumDispo;
import com.eseo.mediastock.model.Enum.EnumEtat;
import com.eseo.mediastock.model.Exemplaire;
import com.eseo.mediastock.model.Produits.Produit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class EmpruntServiceTest {

    // Constante pour le nombre max d'emprunts
    private static final int MAX_EMPRUNTS = 6;
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
        adherent = new Adherent("test-1", "Dupont", "Jean", "jean@email.com", "0123456789");

        // Crée un exemplaire de test
        exemplaire = new ExemplaireMock(); // Notre mock d'exemplaire

        System.out.println("Test prêt à s'exécuter");
    }

    @Test
    void testPeutEmprunter_CasNormal() {
        System.out.println(" TEST : peutEmprunter - Cas normal");

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
        assertTrue(empruntDAOMock.addEmpruntAppele, "addEmprunt devrait être appelé");
        assertTrue(exemplaireDAOMock.updateAppele, "updateExemplaire devrait être appelé");

        // Vérifie les paramètres passés au DAO
        assertEquals(adherent, empruntDAOMock.lastAdherent, "L'adhérent passé devrait être le bon");
        assertEquals(exemplaire, empruntDAOMock.lastExemplaire, "L'exemplaire passé devrait être le bon");

        System.out.println("Emprunt enregistré avec succès");
    }

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
        assertFalse(empruntDAOMock.addEmpruntAppele, "addEmprunt ne devrait PAS être appelé");
        assertFalse(exemplaireDAOMock.updateAppele, "updateExemplaire ne devrait PAS être appelé");

        System.out.println("Aucun emprunt créé (normal)");
    }

    // ================ MOCKS ================

    class ExemplaireMock extends Exemplaire {
        private boolean dispo = true;
        private boolean bonEtat = true;
        private EnumDispo statut = EnumDispo.DISPONIBLE;
        private int id = 1;

        public ExemplaireMock() {
            super(0, new Produit() {
            }, "codeBarre", EnumEtat.BON, EnumDispo.DISPONIBLE);
        }

        @Override
        public boolean estDispo() {
            return dispo;
        }

        @Override
        public boolean estBonEtat() {
            return bonEtat;
        }

        @Override
        public EnumDispo getStatusDispo() {
            return statut;
        }

        @Override
        public void setStatusDispo(EnumDispo nouveauStatut) {
            this.statut = nouveauStatut;
        }

        @Override
        public int getId() {
            return id;
        }

        public void setId(int i) {
            this.id = i;
        }

        // Méthodes pour configurer le mock
        public void setDispo(boolean d) {
            this.dispo = d;
        }

        public void setBonEtat(boolean b) {
            this.bonEtat = b;
        }

        public EnumDispo getStatut() {
            return statut;
        }

        public void setStatut(EnumDispo s) {
            this.statut = s;
        }
    }

    class EmpruntDAOMock extends EmpruntDAO {
        boolean addEmpruntAppele = false;
        boolean saveRetourAppele = false;

        // Pour vérifier les paramètres
        Adherent lastAdherent = null;
        Exemplaire lastExemplaire = null;
        Emprunt lastEmprunt = null;

        @Override
        public void addEmprunt(Adherent adherent, Exemplaire exemplaire) throws SQLException {
            addEmpruntAppele = true;
            lastAdherent = adherent;
            lastExemplaire = exemplaire;
            System.out.println("Mock: addEmprunt appelé avec adherent=" + adherent.getId() +
                    ", exemplaire=" + exemplaire.getId());
        }

        @Override
        public void saveRetour(Emprunt emprunt) throws SQLException {
            saveRetourAppele = true;
            lastEmprunt = emprunt;
            System.out.println("Mock: saveRetour appelé avec emprunt id=" + emprunt.getId());
        }
    }

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