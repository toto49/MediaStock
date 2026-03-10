package com.eseo.mediastock.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;

public class EmpruntController {

    private final int LIGNES_PAR_PAGE = 50;

    // --- Champs de formulaire ---
    @FXML
    private TextField fieldAdherent;
    @FXML
    private TextField fieldExemplaire;
    // --- Données ---
    private final ObservableList<RetardItem> masterData = FXCollections.observableArrayList();
    @FXML
    private Button btnRendre;
    // --- Boutons et Message ---
    @FXML
    private Button btnEmprunter;
    @FXML
    private Label lblMessage;
    // --- TableView, Pagination et Colonnes ---
    @FXML
    private TableView<RetardItem> tableRetard;
    // NOUVEAU : Les deux colonnes séparées
    @FXML
    private TableColumn<RetardItem, String> colNumAdherent;

    @FXML
    private TableColumn<RetardItem, String> colExemplaire;
    @FXML
    private TableColumn<RetardItem, String> colDateLimite;
    @FXML
    private TableColumn<RetardItem, Integer> colJours;
    @FXML
    private TableColumn<RetardItem, String> colNomAdherent;
    @FXML
    private Pagination paginationRetards;

    @FXML
    public void initialize() {
        // Lien avec les méthodes getNumAdherent() et getNomAdherent() de RetardItem
        colNumAdherent.setCellValueFactory(new PropertyValueFactory<>("numAdherent"));
        colNomAdherent.setCellValueFactory(new PropertyValueFactory<>("nomAdherent"));

        colExemplaire.setCellValueFactory(new PropertyValueFactory<>("exemplaire"));
        colDateLimite.setCellValueFactory(new PropertyValueFactory<>("dateLimite"));
        colJours.setCellValueFactory(new PropertyValueFactory<>("joursRetard"));

        configurerPagination();
        chargerDonneesTest();
    }

    private void configurerPagination() {
        paginationRetards.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            afficherPage(newIndex.intValue());
        });
    }

    private void mettreAJourPagination() {
        int pageCount = (int) Math.ceil((double) masterData.size() / LIGNES_PAR_PAGE);
        paginationRetards.setPageCount(pageCount == 0 ? 1 : pageCount);
        paginationRetards.setCurrentPageIndex(0);
        afficherPage(0);
    }

    private void afficherPage(int pageIndex) {
        int fromIndex = pageIndex * LIGNES_PAR_PAGE;
        int toIndex = Math.min(fromIndex + LIGNES_PAR_PAGE, masterData.size());

        if (fromIndex <= toIndex && fromIndex >= 0) {
            tableRetard.setItems(FXCollections.observableArrayList(masterData.subList(fromIndex, toIndex)));
        } else {
            tableRetard.setItems(FXCollections.observableArrayList());
        }
    }

    private void chargerDonneesTest() {
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                List<RetardItem> listeDepuisBdd = new ArrayList<>();

                for (int i = 1; i <= 120; i++) {
                    // Création de fausses données avec le Numéro ET le Nom
                    listeDepuisBdd.add(new RetardItem("ADH-" + i, "Utilisateur " + i, "Titre test " + i, "01/03/2026", i));
                }

                Platform.runLater(() -> {
                    masterData.setAll(listeDepuisBdd);
                    mettreAJourPagination();
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    private void handleEmprunter(ActionEvent event) {
        lblMessage.setText("");

        String numAdherent = fieldAdherent.getText();
        String codeExemplaire = fieldExemplaire.getText();

        if (numAdherent.isEmpty() || codeExemplaire.isEmpty()) {
            afficherMessage("Erreur : Veuillez remplir tous les champs.", true);
            return;
        }

        // TODO: Ajouter la logique BDD
        afficherMessage("Emprunt validé pour l'adhérent " + numAdherent, false);
        fieldAdherent.clear();
        fieldExemplaire.clear();
    }

    @FXML
    private void handleRendre(ActionEvent event) {
        lblMessage.setText("");

        String codeExemplaire = fieldExemplaire.getText();

        if (codeExemplaire.isEmpty()) {
            afficherMessage("Erreur : Veuillez scanner le code de l'exemplaire pour le rendre.", true);
            return;
        }

        // TODO: Ajouter la logique BDD
        afficherMessage("Retour validé pour l'exemplaire : " + codeExemplaire, false);
        fieldExemplaire.clear();
    }

    private void afficherMessage(String message, boolean estErreur) {
        lblMessage.setText(message);
        if (estErreur) {
            lblMessage.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold; -fx-padding: 10 0 0 0;");
        } else {
            lblMessage.setStyle("-fx-text-fill: #2ecc71; -fx-font-weight: bold; -fx-padding: 10 0 0 0;");
        }
    }

    // --- La classe RetardItem ---
        public record RetardItem(String numAdherent, String nomAdherent, String exemplaire, String dateLimite,
                                 int joursRetard) {
    }
}