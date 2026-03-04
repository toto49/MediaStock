package com.eseo.mediastock.controller;

import com.eseo.mediastock.dao.DvdDAO;
import com.eseo.mediastock.model.Produits.DVD;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

import java.sql.SQLException;
import java.util.List;

public class DVDController {

    @FXML
    private TableView<DVD> tableDVD;
    private DvdDAO dvdDAO;

    @FXML
    public void initialize() {
        dvdDAO = new DvdDAO();
        chargerDonneesDansTableau();
    }


    private void chargerDonneesDansTableau() {
        new Thread(() -> {
            try {
                List<DVD> listeDepuisBdd = dvdDAO.ProduitObjectList();
                Platform.runLater(() -> {
                    ObservableList<DVD> observableDVDs = FXCollections.observableArrayList(listeDepuisBdd);
                    tableDVD.setItems(observableDVDs);
                });

            } catch (SQLException e) {
                System.err.println("Erreur lors du chargement des DVD depuis la base de données !");
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