package com.eseo.mediastock.controller;

import com.eseo.mediastock.Launcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class BienvenueController {
    @FXML
    public Label labelconnexion;


    public void Buttonconnexion(ActionEvent actionEvent) throws IOException {
        labelconnexion.setText("Connexion");

        FXMLLoader fxmlLoader = new FXMLLoader(
                Launcher.class.getResource("view/connexion-view.fxml")
        );
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) labelconnexion.getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    }

    public void ButtonCreateAccount(ActionEvent actionEvent) throws IOException {
        labelconnexion.setText("CreateAccount");


        FXMLLoader fxmlLoader = new FXMLLoader(
                Launcher.class.getResource("view/create-account-view.fxml")
        );
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) labelconnexion.getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    }
}
