package com.eseo.mediastock.controller;

import com.eseo.mediastock.HelloApplication;
import com.eseo.mediastock.Launcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;

import java.io.IOException;

/**
 * The type Bienvenue controller.
 */
public class BienvenueController {

    /**
     * The Labelconnexion.
     */
    @FXML
    public Label labelconnexion;

    /**
     * Buttonconnexion.
     *
     * @param actionEvent the action event
     * @throws IOException the io exception
     */
    public void Buttonconnexion(ActionEvent actionEvent) throws IOException {
        labelconnexion.setText("Connexion en cours...");

        FXMLLoader fxmlLoader = new FXMLLoader(
                Launcher.class.getResource("view/connexion-view.fxml")
        );
        Parent nouvelleVue = fxmlLoader.load();


        HelloApplication.changerPageGlobale(nouvelleVue, "Connexion");
    }

    /**
     * Button create account.
     *
     * @param actionEvent the action event
     * @throws IOException the io exception
     */
    public void ButtonCreateAccount(ActionEvent actionEvent) throws IOException {
        labelconnexion.setText("Création de compte...");

        FXMLLoader fxmlLoader = new FXMLLoader(
                Launcher.class.getResource("view/create-account-view.fxml")
        );
        Parent nouvelleVue = fxmlLoader.load();
        HelloApplication.changerPageGlobale(nouvelleVue, "Créer un compte");
    }
}