package com.eseo.mediastock.controller;

import com.eseo.mediastock.model.Adherent;
import com.eseo.mediastock.service.AdherentService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class AdherentController {

    private final int LIGNES_PAR_PAGE = 15;
    // --- Champs du formulaire ---
    @FXML
    private TextField TeldAdherent;
    @FXML
    private TextField NomAdherent;
    @FXML
    private TextField PrenomAdherent;
    @FXML
    private TextField EmailAdherent;
    // --- Boutons et Messages ---
    @FXML
    private Button btnCreer;
    @FXML
    private Button btnSupprimer;
    @FXML
    private Label lblMessage; // Nouveau label pour les retours utilisateur
    // --- Nouveaux champs pour la Recherche et Pagination ---
    @FXML
    private TextField searchBarAdherent;
    @FXML
    private Pagination paginationAdherents;
    // --- Tableau et Colonnes ---
    @FXML
    private TableView<Adherent> tableRetard;
    @FXML
    private TableColumn<Adherent, String> colidAdherent;
    @FXML
    private TableColumn<Adherent, String> colTelAdherent;
    @FXML
    private TableColumn<Adherent, String> colNomAdherent;
    @FXML
    private TableColumn<Adherent, String> colPrenomAdherent;
    @FXML
    private TableColumn<Adherent, String> colEmailAdherent;
    // --- Service et Gestion des Données ---
    private AdherentService adherentService;
    private final ObservableList<Adherent> masterData = FXCollections.observableArrayList();
    private FilteredList<Adherent> filteredData;

    @FXML
    public void initialize() {
        adherentService = new AdherentService();
        colidAdherent.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTelAdherent.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        colNomAdherent.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenomAdherent.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmailAdherent.setCellValueFactory(new PropertyValueFactory<>("email"));
        configurerRechercheEtPagination();
        chargerAdherents();
    }

    private void chargerAdherents() {

        try {
            List<Adherent> listeDepuisBdd = adherentService.getAllAdherents();
            masterData.setAll(listeDepuisBdd);
            mettreAJourPagination();
        } catch (Exception e) {
            afficherMessage("Erreur de chargement des données : " + e.getMessage(), true);
            e.printStackTrace();
        }
    }


    private void configurerRechercheEtPagination() {
        filteredData = new FilteredList<>(masterData, p -> true);

        searchBarAdherent.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(adherent -> {
                if (newValue == null || newValue.isEmpty()) return true;

                String lowerCaseFilter = newValue.toLowerCase();
                if (adherent.getNom().toLowerCase().contains(lowerCaseFilter)) return true;
                return adherent.getPrenom().toLowerCase().contains(lowerCaseFilter);
            });
            mettreAJourPagination();
        });

        paginationAdherents.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            afficherPage(newIndex.intValue());
        });
    }

    private void mettreAJourPagination() {
        int pageCount = (int) Math.ceil((double) filteredData.size() / LIGNES_PAR_PAGE);
        paginationAdherents.setPageCount(pageCount == 0 ? 1 : pageCount);
        paginationAdherents.setCurrentPageIndex(0);
        afficherPage(0);
    }

    private void afficherPage(int pageIndex) {
        int fromIndex = pageIndex * LIGNES_PAR_PAGE;
        int toIndex = Math.min(fromIndex + LIGNES_PAR_PAGE, filteredData.size());

        if (fromIndex <= toIndex && fromIndex >= 0) {
            tableRetard.setItems(FXCollections.observableArrayList(filteredData.subList(fromIndex, toIndex)));
        } else {
            tableRetard.setItems(FXCollections.observableArrayList());
        }
    }

    // --- ACTIONS DES BOUTONS ---

    @FXML
    public void handlecreer(ActionEvent event) {
        lblMessage.setText("");

        String tel = TeldAdherent.getText();
        String nom = NomAdherent.getText();
        String prenom = PrenomAdherent.getText();
        String email = EmailAdherent.getText();

        if (nom.isEmpty() || prenom.isEmpty() || tel.isEmpty() || email.isEmpty()) {
            afficherMessage("Veuillez remplir les champs obligatoires (Nom, Prénom, Téléphone, Email).", true);
            return;
        }

        try {

            adherentService.inscrireAdherent(nom, prenom, email, tel);
            afficherMessage("L'adhérent a été créé avec succès !", false);
            chargerAdherents();
            viderChamps();
        } catch (Exception e) {
            afficherMessage("Erreur lors de la création : " + e.getMessage(), true);
        }
    }

    @FXML
    public void handlesupprimer(ActionEvent event) {
        lblMessage.setText("");
        Adherent adherentSelectionne = tableRetard.getSelectionModel().getSelectedItem();

        if (adherentSelectionne != null) {
            try {
                adherentService.supprimerAdherent(adherentSelectionne.getId());
                afficherMessage("L'adhérent a été supprimé.", false);
                chargerAdherents();
            } catch (Exception e) {
                afficherMessage("Erreur lors de la suppression : " + e.getMessage(), true);
            }
        } else {
            afficherMessage("Veuillez sélectionner un adhérent dans le tableau pour le supprimer.", true);
        }
    }


    private void viderChamps() {
        TeldAdherent.clear();
        NomAdherent.clear();
        PrenomAdherent.clear();
        EmailAdherent.clear();
    }


    private void afficherMessage(String message, boolean estErreur) {
        lblMessage.setText(message);
        if (estErreur) {
            lblMessage.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold; -fx-padding: 10 0 0 0;"); // Rouge
        } else {
            lblMessage.setStyle("-fx-text-fill: #2ecc71; -fx-font-weight: bold; -fx-padding: 10 0 0 0;"); // Vert
        }
    }
}