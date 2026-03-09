package com.eseo.mediastock.controller;

import com.eseo.mediastock.model.Produits.Livre;
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

public class LivresController {
    @FXML
    private TableView<Livre> tableLivres;
    private StockService stockService;

    @FXML
    public void initialize() {
        stockService = new StockService();
        chargerDonneesDansTableau();
    }

    private void chargerDonneesDansTableau() {

        new Thread(() -> {
            try {
                List<Produit> listeProduits = stockService.SearchProduit("", "Livre");
                List<Livre> listeDepuisBdd = new ArrayList<>();
                for (Produit p : listeProduits) {
                    if (p instanceof Livre) {
                        listeDepuisBdd.add((Livre) p);
                    }
                }
                Platform.runLater(() -> {
                    ObservableList<Livre> observableLivres = FXCollections.observableArrayList(listeDepuisBdd);
                    tableLivres.setItems(observableLivres);
                });

            } catch (Exception e) {
                System.err.println("Erreur lors du chargement des livres via le service !");
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