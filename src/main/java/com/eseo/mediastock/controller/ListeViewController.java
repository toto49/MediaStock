package com.eseo.mediastock.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * Contrôleur de la vue intermédiaire de sélection du catalogue.
 * <p>
 * Affiche des cartes ou des boutons permettant à l'utilisateur de choisir quelle
 * catégorie de produits il souhaite consulter (Livres, DVD, ou Jeux de société),
 * et demande au {@link MenuController} de charger la vue correspondante.
 * </p>
 */
public class ListeViewController {

    /**
     * Button view livre.
     *
     * @param actionEvent the action event
     */
    @FXML
    public void ButtonViewLivre(ActionEvent actionEvent) {
        // On passe le nom du fichier FXML ET le titre à afficher
        naviguerVers("livres", "Gestion des Livres");
    }

    /**
     * Button view dvd.
     *
     * @param actionEvent the action event
     */
    @FXML
    public void ButtonViewDVD(ActionEvent actionEvent) {
        naviguerVers("dvd", "DVDthèque");
    }

    /**
     * Button view jeux.
     *
     * @param actionEvent the action event
     */
    @FXML
    public void ButtonViewJeux(ActionEvent actionEvent) {
        naviguerVers("jeux", "Ludothèque");
    }

    private void naviguerVers(String nomPage, String titreFenetre) {
        if (MenuController.getInstance() != null) {

            MenuController.getInstance().chargerPage(nomPage);

            MenuController.getInstance().changerTitre(titreFenetre);
        } else {
            System.err.println("ERREUR : MenuController est null.");
        }
    }
}