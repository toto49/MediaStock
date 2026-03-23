package com.eseo.mediastock.controller;

import com.eseo.mediastock.model.Adherent;
import com.eseo.mediastock.model.Emprunt;
import com.eseo.mediastock.service.AdherentService;
import com.eseo.mediastock.service.EmpruntService;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.List;

/**
 * Contrôleur gérant la vue des adhérents.
 * Permet l'affichage, la création, la suppression et la consultation de l'historique
 * des emprunts pour les adhérents de la bibliothèque.
 *
 * @author tom
 * @date 2026/03/23
 */
public class AdherentController {

    private final int LIGNES_PAR_PAGE = 50;
    private final ObservableList<Adherent> masterData = FXCollections.observableArrayList();
    @FXML
    private TextField TeldAdherent;
    @FXML
    private TextField NomAdherent;
    @FXML
    private TextField PrenomAdherent;
    @FXML
    private TextField EmailAdherent;
    @FXML
    private Button btnCreer;
    @FXML
    private Button btnSupprimer;
    @FXML
    private Label lblMessage;
    @FXML
    private TextField searchBarAdherent;
    @FXML
    private Pagination paginationAdherents;
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
    private AdherentService adherentService;
    private FilteredList<Adherent> filteredData;

    /**
     * Initialise le contrôleur.
     * Configure les colonnes du tableau, la barre de recherche, la pagination et charge les données initiales.
     */
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

        tableRetard.setRowFactory(tv -> {
            TableRow<Adherent> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem copyItem = new MenuItem("Copier l'ID Adhérent");

            copyItem.setOnAction(event -> {
                Adherent item = row.getItem();
                if (item != null) {
                    final Clipboard clipboard = Clipboard.getSystemClipboard();
                    final ClipboardContent content = new ClipboardContent();
                    content.putString(String.valueOf(item.getId()));
                    clipboard.setContent(content);
                }
            });

            contextMenu.getItems().add(copyItem);

            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(contextMenu)
            );
            return row;
        });
        tableRetard.setOnMouseClicked(event -> {

            if (event.getClickCount() == 2) {
                Adherent adherentSelectionne = tableRetard.getSelectionModel().getSelectedItem();
                if (adherentSelectionne != null) {
                    afficherPopupHistorique(adherentSelectionne);
                }
            }
        });
        tableRetard.setOnKeyPressed(event -> {
            if (event.isShortcutDown() && event.getCode() == KeyCode.C) {
                Adherent selectedItem = tableRetard.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    final Clipboard clipboard = Clipboard.getSystemClipboard();
                    final ClipboardContent content = new ClipboardContent();
                    content.putString(String.valueOf(selectedItem.getId()));
                    clipboard.setContent(content);
                }
            }
        });
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

        Pagination paginationHisto = new Pagination();
        ObservableList<EmpruntHistorique> donneesHistorique = FXCollections.observableArrayList();
        VBox loadingBox = new VBox(10);
        loadingBox.setAlignment(Pos.CENTER);
        ProgressIndicator spinner = new ProgressIndicator();
        spinner.setPrefSize(40, 40);
        spinner.setStyle("-fx-progress-color: #ffcc00;");
        Label loadingLabel = new Label("Chargement de l'historique...");
        loadingLabel.setStyle("-fx-text-fill: #888888; -fx-font-size: 14px;");
        loadingBox.getChildren().addAll(spinner, loadingLabel);
        root.getChildren().addAll(lblTitre, lblContact, loadingBox);

        Button btnFermer = new Button("Fermer");
        btnFermer.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-cursor: hand;");
        btnFermer.setOnAction(e -> popup.close());

        Scene scene = new Scene(root, 550, 500);
        popup.setScene(scene);
        popup.show();

        new Thread(() -> {
            try {
                EmpruntService empruntService = new EmpruntService();
                List<Emprunt> empruntsBruts = empruntService.getEmpruntsFromAdherent(adherent);

                Platform.runLater(() -> {
                    for (Emprunt emp : empruntsBruts) {
                        String produitInfo = emp.getExemplaire() != null ? emp.getExemplaire().getCodeBarre() : "Inconnu";
                        String dateEmp = emp.getDateDebut() != null ? emp.getDateDebut().toString() : "Inconnue";
                        String statut = emp.getStatusDispo() != null ? emp.getStatusDispo().name() : "Inconnu";
                        donneesHistorique.add(new EmpruntHistorique(produitInfo, dateEmp, statut));
                    }

                    int lignesParPagePopup = 10;
                    int nbPages = (int) Math.ceil((double) donneesHistorique.size() / lignesParPagePopup);
                    paginationHisto.setPageCount(nbPages == 0 ? 1 : nbPages);

                    paginationHisto.setPageFactory(pageIndex -> {
                        TableView<EmpruntHistorique> tablePage = new TableView<>();
                        tablePage.setStyle("-fx-background-color: #383838;");

                        tablePage.setRowFactory(tv -> {
                            TableRow<EmpruntHistorique> row = new TableRow<>();
                            ContextMenu contextMenu = new ContextMenu();
                            MenuItem copyItem = new MenuItem("Copier le Code Barre");

                            copyItem.setOnAction(event -> {
                                EmpruntHistorique item = row.getItem();
                                if (item != null) {
                                    final Clipboard clipboard = Clipboard.getSystemClipboard();
                                    final ClipboardContent content = new ClipboardContent();
                                    content.putString(item.titreProduit());
                                    clipboard.setContent(content);
                                }
                            });

                            contextMenu.getItems().add(copyItem);

                            row.contextMenuProperty().bind(
                                    Bindings.when(row.emptyProperty())
                                            .then((ContextMenu) null)
                                            .otherwise(contextMenu)
                            );
                            return row;
                        });
                        tablePage.setOnMouseClicked(event -> {
                            if (event.getClickCount() == 2) {
                                EmpruntHistorique selectedItem = tablePage.getSelectionModel().getSelectedItem();
                                if (selectedItem != null) {
                                    final Clipboard clipboard = Clipboard.getSystemClipboard();
                                    final ClipboardContent content = new ClipboardContent();
                                    content.putString(selectedItem.titreProduit());
                                    clipboard.setContent(content);
                                }
                            }
                        });
                        tablePage.setOnKeyPressed(event -> {
                            if (event.isShortcutDown() && event.getCode() == KeyCode.C) {
                                EmpruntHistorique selectedItem = tablePage.getSelectionModel().getSelectedItem();
                                if (selectedItem != null) {
                                    final Clipboard clipboard = Clipboard.getSystemClipboard();
                                    final ClipboardContent content = new ClipboardContent();
                                    content.putString(selectedItem.titreProduit());
                                    clipboard.setContent(content);
                                }
                            }
                        });

                        Label placeholder = new Label("Aucun historique d'emprunt.");
                        placeholder.setStyle("-fx-text-fill: #aaaaaa; -fx-font-style: italic;");
                        tablePage.setPlaceholder(placeholder);

                        TableColumn<EmpruntHistorique, String> colTitre = new TableColumn<>("Produit (Code Barre)");
                        colTitre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().titreProduit()));
                        colTitre.setPrefWidth(200);

                        TableColumn<EmpruntHistorique, String> colDateEmp = new TableColumn<>("Date Emprunt");
                        colDateEmp.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().dateEmprunt()));
                        colDateEmp.setPrefWidth(120);

                        TableColumn<EmpruntHistorique, String> colStatut = new TableColumn<>("Statut");
                        colStatut.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().statut()));
                        colStatut.setPrefWidth(100);

                        tablePage.getColumns().addAll(colTitre, colDateEmp, colStatut);

                        if (donneesHistorique.isEmpty()) {
                            tablePage.setItems(FXCollections.observableArrayList());
                        } else {
                            int fromIndex = pageIndex * lignesParPagePopup;
                            int toIndex = Math.min(fromIndex + lignesParPagePopup, donneesHistorique.size());
                            tablePage.setItems(FXCollections.observableArrayList(donneesHistorique.subList(fromIndex, toIndex)));
                        }

                        return tablePage;
                    });

                    root.getChildren().remove(loadingBox);
                    root.getChildren().addAll(paginationHisto, btnFermer);
                });

            } catch (Exception e) {
                Platform.runLater(() -> {
                    root.getChildren().remove(loadingBox);
                    Label errorLabel = new Label("Erreur lors du chargement de l'historique.");
                    errorLabel.setStyle("-fx-text-fill: #e74c3c;");
                    root.getChildren().addAll(errorLabel, btnFermer);
                });
            }
        }).start();
    }

    private void chargerAdherents() {
        afficherPlaceholderChargement();

        new Thread(() -> {
            try {
                List<Adherent> listeDepuisBdd = adherentService.getAllAdherents();
                Platform.runLater(() -> {
                    masterData.setAll(listeDepuisBdd);
                    mettreAJourPagination();
                    afficherPlaceholderVide();
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    Label lblErreur = new Label("Erreur de chargement des adhérents.");
                    lblErreur.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                    tableRetard.setPlaceholder(lblErreur);
                    afficherMessage("Erreur de chargement des données : " + e.getMessage(), true);
                    e.printStackTrace();
                });
            }
        }).start();
    }

    private void afficherPlaceholderChargement() {
        if (tableRetard == null) return;

        VBox loadingBox = new VBox(10);
        loadingBox.setAlignment(Pos.CENTER);

        ProgressIndicator spinner = new ProgressIndicator();
        spinner.setPrefSize(40, 40);
        spinner.setStyle("-fx-progress-color: #ffcc00;");

        Label loadingLabel = new Label("Chargement des adhérents en cours...");
        loadingLabel.setStyle("-fx-text-fill: #888888; -fx-font-size: 14px;");

        loadingBox.getChildren().addAll(spinner, loadingLabel);
        tableRetard.setPlaceholder(loadingBox);
    }

    private void afficherPlaceholderVide() {
        if (tableRetard == null) return;

        Label emptyLabel = new Label("Aucun adhérent trouvé.");
        emptyLabel.setStyle("-fx-text-fill: #888888; -fx-font-size: 14px; -fx-font-style: italic;");
        tableRetard.setPlaceholder(emptyLabel);
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
            if (filteredData.isEmpty() && !masterData.isEmpty()) {
                Label noResultLabel = new Label("Aucun adhérent ne correspond à la recherche.");
                noResultLabel.setStyle("-fx-text-fill: #888888; -fx-font-size: 14px; -fx-font-style: italic;");
                tableRetard.setPlaceholder(noResultLabel);
            } else if (filteredData.isEmpty() && masterData.isEmpty()) {
                afficherPlaceholderVide();
            }
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

    /**
     * Gère l'action de création d'un nouvel adhérent.
     * Vérifie les champs obligatoires et enregistre l'adhérent via le service.
     *
     * @param event L'événement déclenché par le clic sur le bouton de création.
     */
    @FXML
    public void handlecreer(ActionEvent event) {
        lblMessage.setText("");

        String tel = TeldAdherent.getText();
        String nom = NomAdherent.getText();
        String prenom = PrenomAdherent.getText();
        String email = EmailAdherent.getText();

        if (nom.isEmpty() || prenom.isEmpty() || tel.isEmpty() || email.isEmpty()) {
            afficherMessage("Veuillez remplir les champs obligatoires.", true);
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

    /**
     * Gère l'action de suppression d'un adhérent.
     * Récupère l'adhérent sélectionné dans le tableau et le supprime via le service.
     *
     * @param event L'événement déclenché par le clic sur le bouton de suppression.
     */
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
            afficherMessage("Veuillez sélectionner un adhérent.", true);
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

    /**
     * Représente l'historique d'un emprunt pour l'affichage dans le tableau dédié.
     *
     * @param titreProduit Le code barre ou l'identifiant du produit emprunté.
     * @param dateEmprunt  La date à laquelle l'emprunt a été effectué.
     * @param statut       Le statut actuel de l'emprunt (ex: EN_COURS, RETOURNE).
     */
    public record EmpruntHistorique(String titreProduit, String dateEmprunt, String statut) {
    }
}