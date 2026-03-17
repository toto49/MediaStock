package com.eseo.mediastock.controller;

import com.eseo.mediastock.model.Adherent;
import com.eseo.mediastock.model.Emprunt;
import com.eseo.mediastock.model.Exemplaire;
import com.eseo.mediastock.service.AdherentService;
import com.eseo.mediastock.service.EmpruntService;
import com.eseo.mediastock.service.StockService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class EmpruntController {

    private final int LIGNES_PAR_PAGE = 50;

    // --- INSTANCIATION EXCLUSIVE DES SERVICES ---
    private final EmpruntService empruntService = new EmpruntService();
    private final AdherentService adherentService = new AdherentService();
    private final StockService stockService = new StockService();

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
        colNumAdherent.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().numAdherent())
        );
        colNomAdherent.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().nomAdherent())
        );
        colExemplaire.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().exemplaire())
        );
        colDateLimite.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().dateLimite())
        );
        colJours.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().joursRetard())
        );

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

    private void afficherPlaceholderChargement() {
        if (tableRetard == null) return;

        VBox loadingBox = new VBox(10);
        loadingBox.setAlignment(Pos.CENTER);

        ProgressIndicator spinner = new ProgressIndicator();
        spinner.setPrefSize(40, 40);
        spinner.setStyle("-fx-progress-color: #ffcc00;");

        Label loadingLabel = new Label("Vérification des retards en cours...");
        loadingLabel.setStyle("-fx-text-fill: #888888; -fx-font-size: 14px;");

        loadingBox.getChildren().addAll(spinner, loadingLabel);
        tableRetard.setPlaceholder(loadingBox);
    }

    private void afficherPlaceholderVide() {
        if (tableRetard == null) return;

        Label emptyLabel = new Label("Super ! Aucun emprunt n'est en retard.");
        emptyLabel.setStyle("-fx-text-fill: #2ecc71; -fx-font-size: 14px; -fx-font-style: italic;");
        tableRetard.setPlaceholder(emptyLabel);
    }

    // --- MODIFICATION DU CHARGEMENT ---

    private void chargerEmpruntsEnRetard() {
        afficherPlaceholderChargement();

        new Thread(() -> {
            try {
                List<Emprunt> empruntsEnRetard = empruntService.getEmpruntsEnRetards();
                List<RetardItem> listeDepuisBdd = new ArrayList<>();

                for (Emprunt emp : empruntsEnRetard) {

                    long joursRetard = ChronoUnit.DAYS.between(emp.getDateRetour(), LocalDate.now());

                    listeDepuisBdd.add(new RetardItem(
                            emp.getEmprunteur().getId(),
                            emp.getEmprunteur().getNom() + " " + emp.getEmprunteur().getPrenom(),
                            emp.getExemplaire().getCodeBarre(),
                            emp.getDateRetour().toString(),
                            (int) joursRetard
                    ));
                }

                Platform.runLater(() -> {
                    masterData.setAll(listeDepuisBdd);
                    mettreAJourPagination();
                    afficherPlaceholderVide();
                });

            } catch (Exception e) {
                Platform.runLater(() -> {
                    Label lblErreur = new Label("Erreur de récupération des retards.");
                    lblErreur.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                    tableRetard.setPlaceholder(lblErreur);
                    afficherMessage("Erreur lors du chargement des retards.", true);
                    e.printStackTrace();
                });
            }
        }).start();
    }

    @FXML
    private void handleEmprunter(ActionEvent event) {
        lblMessage.setText("");

        String idAdherent = fieldAdherent.getText();
        String codeBarreExemplaire = fieldExemplaire.getText();

        if (idAdherent.isEmpty() || codeBarreExemplaire.isEmpty()) {
            afficherMessage("Erreur : Veuillez remplir tous les champs.", true);
            return;
        }

        try {
            Adherent adherent = adherentService.getAdherentById(idAdherent);
            Exemplaire exemplaire = stockService.getExemplaireParCodeBarre(codeBarreExemplaire);

            if (adherent == null) {
                afficherMessage("Erreur : Adhérent introuvable.", true);
                return;
            }
            if (exemplaire == null) {
                afficherMessage("Erreur : Exemplaire introuvable.", true);
                return;
            }

            if (!empruntService.peutEmprunter(adherent, exemplaire)) {
                afficherMessage("Refusé : Quota atteint, retards, ou exemplaire indisponible.", true);
                return;
            }

            empruntService.enregistrerEmprunt(adherent, exemplaire);
            afficherMessage("Emprunt validé pour " + adherent.getNom(), false);

            fieldAdherent.clear();
            fieldExemplaire.clear();

        } catch (Exception e) {
            afficherMessage("Erreur : " + e.getMessage(), true);
        }
    }

    @FXML
    private void handleRendre(ActionEvent event) {
        lblMessage.setText("");
        String codeBarreExemplaire = fieldExemplaire.getText();

        if (codeBarreExemplaire.isEmpty()) {
            afficherMessage("Erreur : Veuillez scanner le code barre de l'exemplaire.", true);
            return;
        }

        try {
            Emprunt empruntActif = empruntService.getEmpruntActif(codeBarreExemplaire);

            if (empruntActif == null) {
                afficherMessage("Erreur : Aucun emprunt en cours pour cet exemplaire.", true);
                return;
            }

            empruntService.enregistrerRetour(empruntActif.getEmprunteur(), empruntActif);
            afficherMessage("Retour validé pour l'exemplaire : " + codeBarreExemplaire, false);

            fieldExemplaire.clear();
            chargerEmpruntsEnRetard();

        } catch (Exception e) {
            afficherMessage("Erreur système : " + e.getMessage(), true);
        }
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