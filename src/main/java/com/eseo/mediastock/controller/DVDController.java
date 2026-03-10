package com.eseo.mediastock.controller;

import com.eseo.mediastock.model.Produits.DVD;
import com.eseo.mediastock.model.Produits.Produit;
import com.eseo.mediastock.service.StockService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.List;

public class DVDController {
    private final int LIGNES_PAR_PAGE = 50;
    @FXML
    private TableView<DVD> tableDVD;
    private final ObservableList<DVD> masterData = FXCollections.observableArrayList();

    private StockService stockService;
    @FXML
    private Pagination paginationDVD;

    @FXML
    public void initialize() {
        stockService = new StockService();
        tableDVD.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        configurerPagination();
        chargerDonneesDansTableau();
    }

    private void configurerPagination() {
        if (paginationDVD != null) {
            paginationDVD.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
                afficherPage(newIndex.intValue());
            });
        }
    }

    private void mettreAJourPagination() {
        if (paginationDVD != null) {
            int pageCount = (int) Math.ceil((double) masterData.size() / LIGNES_PAR_PAGE);
            paginationDVD.setPageCount(pageCount == 0 ? 1 : pageCount);
            paginationDVD.setCurrentPageIndex(0);
            afficherPage(0);
        } else {
            tableDVD.setItems(masterData);
        }
    }

    private void afficherPage(int pageIndex) {
        int fromIndex = pageIndex * LIGNES_PAR_PAGE;
        int toIndex = Math.min(fromIndex + LIGNES_PAR_PAGE, masterData.size());

        if (fromIndex <= toIndex && fromIndex >= 0) {
            tableDVD.setItems(FXCollections.observableArrayList(masterData.subList(fromIndex, toIndex)));
        } else {
            tableDVD.setItems(FXCollections.observableArrayList());
        }
    }

    private void chargerDonneesDansTableau() {
        new Thread(() -> {
            try {
                List<Produit> listeProduits = stockService.SearchProduit("", "DVD");
                List<DVD> listeDepuisBdd = new ArrayList<>();
                for (Produit p : listeProduits) {
                    if (p instanceof DVD) {
                        listeDepuisBdd.add((DVD) p);
                    }
                }

                Platform.runLater(() -> {
                    masterData.setAll(listeDepuisBdd);
                    mettreAJourPagination();
                });

            } catch (Exception e) {
                System.err.println("Erreur lors du chargement des DVD via le service !");
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