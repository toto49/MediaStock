package com.eseo.mediastock.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class EmpruntController {

    // --- Champs de formulaire ---
    @FXML
    private TextField fieldAdherent;
    @FXML
    private TextField fieldExemplaire;

    // --- Boutons ---
    @FXML
    private Button btnEmprunter;
    @FXML
    private Button btnRendre;

    // --- TableView et Colonnes ---
    @FXML
    private TableView<RetardItem> tableRetard;
    @FXML
    private TableColumn<RetardItem, String> colAdherent;
    @FXML
    private TableColumn<RetardItem, String> colExemplaire;
    @FXML
    private TableColumn<RetardItem, String> colDateLimite;
    @FXML
    private TableColumn<RetardItem, Integer> colJours;

    @FXML
    public void initialize() {
        colAdherent.setCellValueFactory(new PropertyValueFactory<>("adherent"));
        colExemplaire.setCellValueFactory(new PropertyValueFactory<>("exemplaire"));
        colDateLimite.setCellValueFactory(new PropertyValueFactory<>("dateLimite"));
        colJours.setCellValueFactory(new PropertyValueFactory<>("joursRetard"));
        chargerDonneesTest();
    }

    @FXML
    private void handleEmprunter(ActionEvent event) {
        String numAdherent = fieldAdherent.getText();
        String codeExemplaire = fieldExemplaire.getText();

        if (numAdherent.isEmpty() || codeExemplaire.isEmpty()) {
            afficherAlerte("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        // TODO: Ajouter la logique pour enregistrer l'emprunt en Base de Données
        System.out.println("Emprunt validé pour l'adhérent " + numAdherent + " (Exemplaire: " + codeExemplaire + ")");

        fieldAdherent.clear();
        fieldExemplaire.clear();
    }

    @FXML
    private void handleRendre(ActionEvent event) {
        String codeExemplaire = fieldExemplaire.getText();

        if (codeExemplaire.isEmpty()) {
            afficherAlerte("Erreur", "Veuillez scanner ou saisir le code de l'exemplaire pour le rendre.");
            return;
        }

        // TODO: Ajouter la logique pour enregistrer le retour
        System.out.println("Retour validé pour l'exemplaire: " + codeExemplaire);
        fieldExemplaire.clear();
    }

    private void afficherAlerte(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void chargerDonneesTest() {
        ObservableList<RetardItem> listeRetards = FXCollections.observableArrayList(
                new RetardItem("1001", "La Peste - Camus", "01/03/2026", 3),
                new RetardItem("1042", "Seigneur des Anneaux", "25/02/2026", 7)
        );
        tableRetard.setItems(listeRetards);
    }

    public static class RetardItem {
        private final String adherent;
        private final String exemplaire;
        private final String dateLimite;
        private final int joursRetard;

        public RetardItem(String adherent, String exemplaire, String dateLimite, int joursRetard) {
            this.adherent = adherent;
            this.exemplaire = exemplaire;
            this.dateLimite = dateLimite;
            this.joursRetard = joursRetard;
        }


        public String getAdherent() {
            return adherent;
        }

        public String getExemplaire() {
            return exemplaire;
        }

        public String getDateLimite() {
            return dateLimite;
        }

        public int getJoursRetard() {
            return joursRetard;
        }
    }
}