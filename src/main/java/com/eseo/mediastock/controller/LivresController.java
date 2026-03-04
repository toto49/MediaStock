package com.eseo.mediastock.controller;

import com.eseo.mediastock.dao.LivreDAO;
import com.eseo.mediastock.model.Produits.Livre;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

import java.sql.SQLException;
import java.util.List;

public class LivresController {
    @FXML
    private TableView<Livre> tableLivres;
    private LivreDAO livreDAO;

    @FXML
    public void initialize() {
        livreDAO = new LivreDAO();
        chargerDonneesDansTableau();
    }

    private void chargerDonneesDansTableau() {
        try {
            List<Livre> listeDepuisBdd = livreDAO.ProduitObjectList();
            ObservableList<Livre> observableLivres = FXCollections.observableArrayList(listeDepuisBdd);
            tableLivres.setItems(observableLivres);

        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des livres depuis la base de données !");
            e.printStackTrace();

        }
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