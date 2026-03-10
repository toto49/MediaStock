package com.eseo.mediastock.controller;

import com.eseo.mediastock.model.Adherent;
import com.eseo.mediastock.service.AdherentService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.List;

public class AdherentController {

    private final int LIGNES_PAR_PAGE = 50;

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
    private Label lblMessage;

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
    @FXML
    private TableColumn<Adherent, Void> colAction;

    // --- Service et Gestion des Données ---
    private AdherentService adherentService;
    private final ObservableList<Adherent> masterData = FXCollections.observableArrayList();
    private FilteredList<Adherent> filteredData;

    @FXML
    public void initialize() {
        adherentService = new AdherentService();
        colidAdherent.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTelAdherent.setCellValueFactory(new PropertyValueFactory<>("numTel"));
        colNomAdherent.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenomAdherent.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmailAdherent.setCellValueFactory(new PropertyValueFactory<>("emailContact"));

        configurerColonneAction();
        configurerRechercheEtPagination();
        chargerAdherents();
    }

    private void configurerColonneAction() {
        Callback<TableColumn<Adherent, Void>, TableCell<Adherent, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Adherent, Void> call(final TableColumn<Adherent, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Détails");

                    {
                        btn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-cursor: hand;");
                        btn.setOnAction((ActionEvent event) -> {
                            Adherent adherentSelectionne = getTableView().getItems().get(getIndex());
                            afficherPopupHistorique(adherentSelectionne);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox box = new HBox(btn);
                            box.setAlignment(Pos.CENTER);
                            setGraphic(box);
                        }
                    }
                };
            }
        };
        colAction.setCellFactory(cellFactory);
    }

    // --- LE POPUP SANS SIMULATION DE DONNÉES ---
    private void afficherPopupHistorique(Adherent adherent) {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Profil de " + adherent.getNom() + " " + adherent.getPrenom());

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #2b2b2b; -fx-text-fill: white;");

        Label lblTitre = new Label("Historique d'emprunts - " + adherent.getId());
        lblTitre.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #ffcc00;");
        Label lblContact = new Label("Tel: " + adherent.getNumTel() + " | Email: " + adherent.getEmailContact());
        lblContact.setStyle("-fx-text-fill: white;");

        // TODO: C'est ici que tu appelleras ton service plus tard pour remplir cette liste
        // Exemple : ObservableList<EmpruntHistorique> donneesHistorique = FXCollections.observableArrayList(adherentService.getHistorique(adherent.getId()));
        ObservableList<EmpruntHistorique> donneesHistorique = FXCollections.observableArrayList();

        Pagination paginationHisto = new Pagination();
        int lignesParPagePopup = 10;
        int nbPages = (int) Math.ceil((double) donneesHistorique.size() / lignesParPagePopup);
        paginationHisto.setPageCount(nbPages == 0 ? 1 : nbPages);

        paginationHisto.setPageFactory(pageIndex -> {
            TableView<EmpruntHistorique> tablePage = new TableView<>();
            tablePage.setStyle("-fx-background-color: #383838;");

            // Message affiché quand le tableau est vide
            Label placeholder = new Label("Aucun historique d'emprunt.");
            placeholder.setStyle("-fx-text-fill: #aaaaaa; -fx-font-style: italic;");
            tablePage.setPlaceholder(placeholder);

            TableColumn<EmpruntHistorique, String> colTitre = new TableColumn<>("Produit");
            colTitre.setCellValueFactory(new PropertyValueFactory<>("titreProduit"));
            colTitre.setPrefWidth(200);

            TableColumn<EmpruntHistorique, String> colDateEmp = new TableColumn<>("Date Emprunt");
            colDateEmp.setCellValueFactory(new PropertyValueFactory<>("dateEmprunt"));
            colDateEmp.setPrefWidth(120);

            TableColumn<EmpruntHistorique, String> colStatut = new TableColumn<>("Statut");
            colStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
            colStatut.setPrefWidth(100);

            tablePage.getColumns().addAll(colTitre, colDateEmp, colStatut);

            // Gestion propre d'une liste vide pour éviter les erreurs
            if (donneesHistorique.isEmpty()) {
                tablePage.setItems(FXCollections.observableArrayList());
            } else {
                int fromIndex = pageIndex * lignesParPagePopup;
                int toIndex = Math.min(fromIndex + lignesParPagePopup, donneesHistorique.size());
                tablePage.setItems(FXCollections.observableArrayList(donneesHistorique.subList(fromIndex, toIndex)));
            }

            return tablePage;
        });

        Button btnFermer = new Button("Fermer");
        btnFermer.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-cursor: hand;");
        btnFermer.setOnAction(e -> popup.close());

        root.getChildren().addAll(lblTitre, lblContact, paginationHisto, btnFermer);

        Scene scene = new Scene(root, 550, 500);
        popup.setScene(scene);
        popup.showAndWait();
    }

    // --- LE RESTE DU CODE NE CHANGE PAS ---

    private void chargerAdherents() {
        new Thread(() -> {
            try {
                List<Adherent> listeDepuisBdd = adherentService.getAllAdherents();
                Platform.runLater(() -> {
                    masterData.setAll(listeDepuisBdd);
                    mettreAJourPagination();
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    afficherMessage("Erreur de chargement des données : " + e.getMessage(), true);
                    e.printStackTrace();
                });
            }
        }).start();
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
            lblMessage.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold; -fx-padding: 10 0 0 0;");
        } else {
            lblMessage.setStyle("-fx-text-fill: #2ecc71; -fx-font-weight: bold; -fx-padding: 10 0 0 0;");
        }
    }

    public record EmpruntHistorique(String titreProduit, String dateEmprunt, String statut) {
    }
}