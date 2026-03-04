package com.eseo.mediastock.controller;

import com.eseo.mediastock.dao.JeuSocieteDAO;
import com.eseo.mediastock.model.Produits.JeuSociete;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

import java.sql.SQLException;
import java.util.List;

public class JeuxController {

    @FXML
    private TableView<JeuSociete> tableJeux;
    private JeuSocieteDAO jeuSocieteDAO;

    @FXML
    public void initialize() {
        jeuSocieteDAO = new JeuSocieteDAO();
        chargerDonneesDansTableau();
    }

    private void chargerDonneesDansTableau() {
        new Thread(() -> {
            try {
                List<JeuSociete> listeDepuisBdd = jeuSocieteDAO.ProduitObjectList();

                Platform.runLater(() -> {
                    ObservableList<JeuSociete> observableJeux = FXCollections.observableArrayList(listeDepuisBdd);
                    tableJeux.setItems(observableJeux);
                });

            } catch (SQLException e) {
                System.err.println("Erreur lors du chargement des jeux depuis la BDD !");
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    public void ButtonReturn(ActionEvent actionEvent) {
        if (MenuController.getInstance() != null) {
            MenuController.getInstance().chargerPage("liste");
            MenuController.getInstance().changerTitre("Inventaire");
        } else {
            System.err.println("ERREUR : MenuController est null.");
        }
    }
}