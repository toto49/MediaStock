package com.eseo.mediastock.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.Objects;

public class MenuController {
    @FXML
    private BorderPane mainPane;
    @FXML
    private Button btnAccueil;
    @FXML
    private Button btnParametres;

    @FXML
    public void initialize() {
        chargerPage("accueil");
        updateButtonStyles(btnAccueil);
    }

    @FXML
    void afficherAccueil(ActionEvent event) {
        chargerPage("accueil");
        updateButtonStyles(btnAccueil);
    }

    @FXML
    void afficherParametres(ActionEvent event) {
        chargerPage("parametre");
        updateButtonStyles(btnParametres);
    }


    private void updateButtonStyles(Button boutonActif) {

        String STYLE_INACTIF = "-fx-cursor: hand; -fx-background-color: transparent; -fx-text-fill: white;";
        btnAccueil.setStyle(STYLE_INACTIF);
        btnParametres.setStyle(STYLE_INACTIF);
        String STYLE_ACTIF = "-fx-cursor: hand; -fx-background-color: #444; -fx-text-fill: #ffcc00; -fx-font-weight: bold;";
        boutonActif.setStyle(STYLE_ACTIF);
    }

    private void chargerPage(String nomFichier) {
        try {
            String chemin = "/com/eseo/mediastock/view/" + nomFichier + "-view" + ".fxml";
            Parent vue = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(chemin)));
            mainPane.setCenter(vue);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}