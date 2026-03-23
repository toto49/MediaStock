package com.eseo.mediastock.model;

import com.eseo.mediastock.model.Enum.EnumDispo;

import java.time.LocalDate;

/**
 * Entité représentant la liaison temporaire entre un {@link Adherent} et un {@link Exemplaire}.
 * <p>
 * Permet de tracer qui a emprunté quoi, la date du prêt, la date d'échéance attendue,
 * et la date réelle du retour du document.
 * </p>
 */
public class Emprunt {
    private final LocalDate dateDebut;
    private final LocalDate dateRetour;
    private int id;
    private Adherent emprunteur;
    private Exemplaire exemplaire;
    private EnumDispo statut;

    /**
     * Instantiates a new Emprunt.
     */
    public Emprunt() {
        this.dateDebut = LocalDate.now();
        this.dateRetour = LocalDate.now().plusMonths(2);
        this.statut = EnumDispo.EMPRUNTE;
    }

    /**
     * Instantiates a new Emprunt.
     *
     * @param id         the id
     * @param emprunteur the emprunteur
     * @param exemplaire the exemplaire
     * @param dateDebut  the date debut
     * @param dateRetour the date retour
     */
    public Emprunt(int id, Adherent emprunteur, Exemplaire exemplaire, LocalDate dateDebut, LocalDate dateRetour) {
        this.id = id;
        this.emprunteur = emprunteur;
        this.exemplaire = exemplaire;
        this.dateDebut = dateDebut;
        this.dateRetour = dateRetour;
        this.statut = EnumDispo.EMPRUNTE;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets emprunteur.
     *
     * @return the emprunteur
     */
    public Adherent getEmprunteur() {
        return emprunteur;
    }

    /**
     * Sets emprunteur.
     *
     * @param emprunteur the emprunteur
     */
    public void setEmprunteur(Adherent emprunteur) {
        this.emprunteur = emprunteur;
    }

    /**
     * Gets exemplaire.
     *
     * @return the exemplaire
     */
    public Exemplaire getExemplaire() {
        return exemplaire;
    }

    /**
     * Sets exemplaire.
     *
     * @param exemplaire the exemplaire
     */
    public void setExemplaire(Exemplaire exemplaire) {
        this.exemplaire = exemplaire;
    }

    /**
     * Gets date debut.
     *
     * @return the date debut
     */
    public LocalDate getDateDebut() {
        return dateDebut;
    }

    /**
     * Gets date retour.
     *
     * @return the date retour
     */
    public LocalDate getDateRetour() {
        return dateRetour;
    }

    /**
     * Gets status dispo.
     *
     * @return the status dispo
     */
    public EnumDispo getStatusDispo() {
        return statut;
    }

    /**
     * Sets status dispo.
     *
     * @param statusDispo the status dispo
     */
    public void setStatusDispo(EnumDispo statusDispo) {
        this.statut = statusDispo;
    }

    /**
     * Est en retard boolean.
     *
     * @return the boolean
     */
//Méthodes
    public boolean estEnRetard() {
        return dateRetour.isBefore(LocalDate.now()) && this.statut == EnumDispo.EMPRUNTE;
    }

}