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

    // --- Boutons et Message ---
    @FXML
    private Button btnRendre;
    @FXML
    private Button btnEmprunter;
    @FXML
    private Label lblMessage;

    // --- TableView, Pagination et Colonnes ---
    @FXML
    private TableView<RetardItem> tableRetard;
    @FXML
    private TableColumn<RetardItem, String> colNumAdherent;
    @FXML
    private TableColumn<RetardItem, String> colNomAdherent;
    @FXML
    private TableColumn<RetardItem, String> colExemplaire;
    @FXML
    private TableColumn<RetardItem, String> colDateLimite;
    @FXML
    private TableColumn<RetardItem, Integer> colJours;
    @FXML
    private Pagination paginationRetards;

    @FXML
    public void initialize() {
        colNumAdherent.setCellValueFactory(new PropertyValueFactory<>("numAdherent"));
        colNomAdherent.setCellValueFactory(new PropertyValueFactory<>("nomAdherent"));
        colExemplaire.setCellValueFactory(new PropertyValueFactory<>("exemplaire"));
        colDateLimite.setCellValueFactory(new PropertyValueFactory<>("dateLimite"));
        colJours.setCellValueFactory(new PropertyValueFactory<>("joursRetard"));

        // Message affiché quand le tableau est vide
        Label placeholder = new Label("Aucun emprunt en retard.");
        placeholder.setStyle("-fx-text-fill: #aaaaaa; -fx-font-style: italic; -fx-font-size: 14px;");
        tableRetard.setPlaceholder(placeholder);

        configurerPagination();
        chargerEmpruntsEnRetard();
    }

    private void configurerPagination() {
        if (paginationRetards != null) {
            paginationRetards.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
                afficherPage(newIndex.intValue());
            });
        }
    }

    private void mettreAJourPagination() {
        if (paginationRetards != null) {
            int pageCount = (int) Math.ceil((double) masterData.size() / LIGNES_PAR_PAGE);
            paginationRetards.setPageCount(pageCount == 0 ? 1 : pageCount);
            paginationRetards.setCurrentPageIndex(0);
            afficherPage(0);
        } else {
            tableRetard.setItems(masterData);
        }
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

    // --- PLUS DE FAUSSES DONNÉES ICI ---
    private void chargerEmpruntsEnRetard() {
        new Thread(() -> {
            try {
                // TODO: Appeler plus tard ton EmpruntService ici :
                // List<RetardItem> listeDepuisBdd = empruntService.getEmpruntsEnRetards();

                // Pour l'instant, la liste est vide au démarrage
                List<RetardItem> listeDepuisBdd = new ArrayList<>();

                Platform.runLater(() -> {
                    masterData.setAll(listeDepuisBdd);
                    mettreAJourPagination();
                });

            } catch (Exception e) {
                Platform.runLater(() -> {
                    afficherMessage("Erreur de chargement des retards.", true);
                    e.printStackTrace();
                });
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

        // TODO: Appeler ton EmpruntService (enregistrerRetour)
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


    public record RetardItem(String numAdherent, String nomAdherent, String exemplaire, String dateLimite,
                             int joursRetard) {
    }
}