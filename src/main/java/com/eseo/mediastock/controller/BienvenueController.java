package com.eseo.mediastock.controller;

import com.eseo.mediastock.HelloApplication;
import com.eseo.mediastock.Launcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;

import java.io.IOException;

public class BienvenueController {

    @FXML
    public Label labelconnexion;

    public void Buttonconnexion(ActionEvent actionEvent) throws IOException {
        labelconnexion.setText("Connexion en cours...");

        FXMLLoader fxmlLoader = new FXMLLoader(
                Launcher.class.getResource("view/connexion-view.fxml")
        );
        Parent nouvelleVue = fxmlLoader.load();


        HelloApplication.changerPageGlobale(nouvelleVue, "Connexion");
    }

    public void ButtonCreateAccount(ActionEvent actionEvent) throws IOException {
        labelconnexion.setText("Création de compte...");

        FXMLLoader fxmlLoader = new FXMLLoader(
                Launcher.class.getResource("view/create-account-view.fxml")
        );
        Parent nouvelleVue = fxmlLoader.load();
        HelloApplication.changerPageGlobale(nouvelleVue, "Créer un compte");
    }
}