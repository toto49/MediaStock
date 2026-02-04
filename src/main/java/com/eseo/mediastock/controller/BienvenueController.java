package com.eseo.mediastock.controller;

import com.eseo.mediastock.Launcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import java.io.IOException;

public class BienvenueController {
    @FXML
    public Label labelconnexion;


    public void Buttonconnexion(ActionEvent actionEvent) throws IOException {
        labelconnexion.setText("Connexion");

        FXMLLoader fxmlLoader = new FXMLLoader(
                Launcher.class.getResource("view/connexion-view.fxml")
        );
        Parent root = fxmlLoader.load();
        Scene currentScene = ((Node) actionEvent.getSource()).getScene();


        currentScene.setRoot(root);
    }

    public void ButtonCreateAccount(ActionEvent actionEvent) throws IOException {
        labelconnexion.setText("CreateAccount");


        FXMLLoader fxmlLoader = new FXMLLoader(
                Launcher.class.getResource("view/create-account-view.fxml"));
        Parent root = fxmlLoader.load();
        Scene currentScene = ((Node) actionEvent.getSource()).getScene();

        currentScene.setRoot(root);
    }
}
