package com.eseo.mediastock.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ListeViewController {

    @FXML
    public void ButtonViewLivre(ActionEvent actionEvent) {
        // On passe le nom du fichier FXML ET le titre à afficher
        naviguerVers("livre", "Gestion des Livres");
    }

    @FXML
    public void ButtonViewDVD(ActionEvent actionEvent) {
        naviguerVers("dvd", "DVDthèque");
    }

    @FXML
    public void ButtonViewJeux(ActionEvent actionEvent) {
        naviguerVers("jeux", "Ludothèque");
    }

    /**
     * Méthode mise à jour pour gérer la page ET le titre
     */
    private void naviguerVers(String nomPage, String titreFenetre) {
        if (MenuController.getInstance() != null) {

            MenuController.getInstance().chargerPage(nomPage);

            MenuController.getInstance().changerTitre(titreFenetre);
        } else {
            System.err.println("ERREUR : MenuController est null.");
        }
    }
}