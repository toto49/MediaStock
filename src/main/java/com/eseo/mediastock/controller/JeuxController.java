package com.eseo.mediastock.controller;

import com.eseo.mediastock.model.Produits.JeuSociete;
import com.eseo.mediastock.model.Produits.Produit;
import com.eseo.mediastock.service.StockService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.List;

public class JeuxController {

    @FXML
    private TableView<JeuSociete> tableJeux;
    private StockService stockService;

    @FXML
    public void initialize() {
        stockService = new StockService();
        chargerDonneesDansTableau();
    }

    private void chargerDonneesDansTableau() {
        new Thread(() -> {
            try {
                List<Produit> listeProduits = stockService.SearchProduit("", "Jeu");

                List<JeuSociete> listeDepuisBdd = new ArrayList<>();
                for (Produit p : listeProduits) {
                    if (p instanceof JeuSociete) {
                        listeDepuisBdd.add((JeuSociete) p);
                    }
                }

                Platform.runLater(() -> {
                    ObservableList<JeuSociete> observableJeux = FXCollections.observableArrayList(listeDepuisBdd);
                    tableJeux.setItems(observableJeux);
                });

            } catch (Exception e) {
                System.err.println("Erreur lors du chargement des jeux via le service !");
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