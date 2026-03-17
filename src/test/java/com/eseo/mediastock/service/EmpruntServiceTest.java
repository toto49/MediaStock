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

    private EmpruntService empruntService;
    private EmpruntDAOMock empruntDAOMock;
    private ExemplaireDAOMock exemplaireDAOMock;
    private Adherent adherent;
    private ExemplaireMock exemplaire;

    private static final int MAX_EMPRUNTS = 6;

    @BeforeEach
    void setUp() {
        System.out.println(" Préparation du test");

        empruntService = new EmpruntService();
        empruntDAOMock = new EmpruntDAOMock();
        exemplaireDAOMock = new ExemplaireDAOMock();

        // Injection des mocks dans le service
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

        // Création des objets de test
        adherent = new Adherent("test-1", "Dupont", "Jean", "jean@test.com", "0123456789");
        exemplaire = new ExemplaireMock();

        System.out.println(" Test prêt");
    }

    @Test
    void testPeutEmprunter_CasNormal() {
        System.out.println(" TEST : peutEmprunter - Cas normal");

        exemplaire.setDispo(true);
        exemplaire.setBonEtat(true);
        exemplaire.setStatut(EnumDispo.DISPONIBLE);

        boolean resultat = empruntService.peutEmprunter(adherent, exemplaire);

        assertTrue(resultat);
    }

    @Test
    void testPeutEmprunter_MaxAtteint() {
        System.out.println("TEST : peutEmprunter - Max atteint");

        for (int i = 0; i < MAX_EMPRUNTS; i++) {
            Emprunt e = new Emprunt(i, adherent, exemplaire, LocalDate.now(), LocalDate.now().plusMonths(1));
            adherent.getEmpruntsEnCours().add(e);
        }

        boolean resultat = empruntService.peutEmprunter(adherent, exemplaire);

        assertFalse(resultat);
        assertEquals(MAX_EMPRUNTS, adherent.getNombreEmprunts());
    }

    @Test
    void testPeutEmprunter_EmpruntEnRetard() {
        System.out.println("TEST : peutEmprunter - Emprunt en retard");

        Emprunt empruntEnRetard = new Emprunt(2, adherent, exemplaire,
                LocalDate.now().minusMonths(3),
                LocalDate.now().minusMonths(1));
        adherent.getEmpruntsEnCours().add(empruntEnRetard);

        boolean resultat = empruntService.peutEmprunter(adherent, exemplaire);

        assertFalse(resultat);
    }

    @Test
    void testPeutEmprunter_ExemplaireNonDisponible() {
        System.out.println("TEST : peutEmprunter - Exemplaire non disponible");

        exemplaire.setDispo(false);
        exemplaire.setStatut(EnumDispo.EMPRUNTE);

        boolean resultat = empruntService.peutEmprunter(adherent, exemplaire);

        assertFalse(resultat);
    }

    @Test
    void testPeutEmprunter_ExemplaireMauvaisEtat() {
        System.out.println("TEST : peutEmprunter - Exemplaire en mauvais état");

        exemplaire.setDispo(true);
        exemplaire.setBonEtat(false);
        exemplaire.setStatut(EnumDispo.DISPONIBLE);

        boolean resultat = empruntService.peutEmprunter(adherent, exemplaire);

        assertFalse(resultat);
    }

    @Test
    void testEnregistrerEmprunt_Succes() throws SQLException {
        System.out.println("TEST : enregistrerEmprunt - Succès");

        exemplaire.setDispo(true);
        exemplaire.setBonEtat(true);
        exemplaire.setStatut(EnumDispo.DISPONIBLE);
        exemplaire.setId(42);

        int nbAvant = adherent.getEmpruntsEnCours().size();

        empruntService.enregistrerEmprunt(adherent, exemplaire);

        assertEquals(nbAvant + 1, adherent.getEmpruntsEnCours().size());
        assertEquals(EnumDispo.EMPRUNTE, exemplaire.getStatut());

        //Vérification avec le bon nom de méthode (addEmprunt)
        assertTrue(empruntDAOMock.addEmpruntAppele);
        assertTrue(exemplaireDAOMock.updateAppele);
        assertEquals(adherent, empruntDAOMock.lastAdherent);
        assertEquals(exemplaire, empruntDAOMock.lastExemplaire);
    }

    @Test
    void testEnregistrerEmprunt_NonAutorise() throws SQLException {
        System.out.println("TEST : enregistrerEmprunt - Non autorisé");

        exemplaire.setDispo(false);
        exemplaire.setStatut(EnumDispo.EMPRUNTE);

        int nbAvant = adherent.getEmpruntsEnCours().size();

        empruntService.enregistrerEmprunt(adherent, exemplaire);

        assertEquals(nbAvant, adherent.getEmpruntsEnCours().size());
        assertFalse(empruntDAOMock.addEmpruntAppele);
        assertFalse(exemplaireDAOMock.updateAppele);
    }

    @Test
    void testEnregistrerRetour_Succes() throws SQLException {
        System.out.println("TEST : enregistrerRetour - Succès");

        Emprunt nouvelEmprunt = new Emprunt(10, adherent, exemplaire,
                LocalDate.now().minusMonths(1),
                LocalDate.now().plusMonths(1));
        adherent.getEmpruntsEnCours().add(nouvelEmprunt);
        exemplaire.setStatut(EnumDispo.EMPRUNTE);

        int nbAvant = adherent.getEmpruntsEnCours().size();

        empruntService.enregistrerRetour(adherent, nouvelEmprunt);

        assertEquals(nbAvant - 1, adherent.getEmpruntsEnCours().size());
        assertEquals(EnumDispo.DISPONIBLE, exemplaire.getStatut());

        assertTrue(empruntDAOMock.saveRetourAppele);
        assertEquals(nouvelEmprunt, empruntDAOMock.lastEmprunt);
    }

    //  MOCKS = pour eviter d'etre dependant du dao

    class ExemplaireMock extends Exemplaire {
        private boolean dispo = true;
        private boolean bonEtat = true;
        private EnumDispo statut = EnumDispo.DISPONIBLE;
        private int id = 1;

        public ExemplaireMock() {
            super(0, new Produit() {}, "codeBarre", EnumEtat.BON, EnumDispo.DISPONIBLE);
        }

        @Override
        public boolean estDispo() { return dispo; }
        @Override
        public boolean estBonEtat() { return bonEtat; }
        @Override
        public EnumDispo getStatusDispo() { return statut; }
        @Override
        public void setStatusDispo(EnumDispo nouveauStatut) { this.statut = nouveauStatut; }
        @Override
        public int getId() { return id; }

        public void setDispo(boolean d) { this.dispo = d; }
        public void setBonEtat(boolean b) { this.bonEtat = b; }
        public void setStatut(EnumDispo s) { this.statut = s; }
        public EnumDispo getStatut() { return statut; }
        public void setId(int i) { this.id = i; }
    }

    class EmpruntDAOMock extends EmpruntDAO {
        boolean addEmpruntAppele = false;
        boolean saveRetourAppele = false;
        Adherent lastAdherent = null;
        Exemplaire lastExemplaire = null;
        Emprunt lastEmprunt = null;

        @Override
        public void addEmprunt(Adherent adherent, Exemplaire exemplaire) throws SQLException {
            addEmpruntAppele = true;
            lastAdherent = adherent;
            lastExemplaire = exemplaire;
            System.out.println("Mock: addEmprunt appelé");
        }

        @Override
        public void saveRetour(Emprunt emprunt) throws SQLException {
            saveRetourAppele = true;
            lastEmprunt = emprunt;
            System.out.println("Mock: saveRetour appelé");
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