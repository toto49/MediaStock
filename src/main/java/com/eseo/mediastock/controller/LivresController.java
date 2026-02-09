package com.eseo.mediastock.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class LivresController {
    @FXML
    public void ButtonReturn(ActionEvent actionEvent) {
        System.out.println("ButtonReturn");
        if (MenuController.getInstance() != null) {

            MenuController.getInstance().chargerPage("liste");

            MenuController.getInstance().changerTitre("Inventaire");
        } else {
            System.err.println("ERREUR : MenuController est null.");
        }
    }
}
