package com.eseo.mediastock.controller;

import com.eseo.mediastock.model.Enum.EnumDispo;
import com.eseo.mediastock.model.Enum.EnumEtat;
import com.eseo.mediastock.model.Exemplaire;
import com.eseo.mediastock.model.Produits.DVD;
import com.eseo.mediastock.model.Produits.JeuSociete;
import com.eseo.mediastock.model.Produits.Livre;
import com.eseo.mediastock.model.Produits.Produit;
import com.eseo.mediastock.service.StockService;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The type Abstract produit controller.
 *
 * @param <T> the type parameter
 */
public abstract class AbstractProduitController<T extends Produit> {

    /**
     * The Lignes par page.
     */
    protected final int LIGNES_PAR_PAGE = 50;
    /**
     * The Master data.
     */
    protected final ObservableList<T> masterData = FXCollections.observableArrayList();
    /**
     * The Stock service.
     */
    protected StockService stockService;

    /**
     * Gets table.
     *
     * @return the table
     */
    protected abstract TableView<T> getTable();

    /**
     * Gets pagination.
     *
     * @return the pagination
     */
    protected abstract Pagination getPagination();

    /**
     * Gets col action.
     *
     * @return the col action
     */
    protected abstract TableColumn<T, Void> getColAction();

    /**
     * Gets categorie produit.
     *
     * @return the categorie produit
     */
    protected abstract String getCategorieProduit();

    /**
     * Gets produit class.
     *
     * @return the produit class
     */
    protected abstract Class<T> getProduitClass();

    /**
     * Init common.
     */
    protected void initCommon() {
        stockService = new StockService();
        if (getTable() != null) {
            getTable().setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        }

        configurerPagination();
        chargerDonneesDansTableau();
        configurerClicSurLigne();
        configurerColonneAction();
    }

    private void configurerPagination() {
        if (getPagination() != null) {
            getPagination().currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
                afficherPage(newIndex.intValue());
            });
        }
    }

    private void mettreAJourPagination() {
        if (getPagination() != null) {
            int pageCount = (int) Math.ceil((double) masterData.size() / LIGNES_PAR_PAGE);
            getPagination().setPageCount(pageCount == 0 ? 1 : pageCount);
            getPagination().setCurrentPageIndex(0);
            afficherPage(0);
        } else if (getTable() != null) {
            getTable().setItems(masterData);
        }
    }

    private void afficherPage(int pageIndex) {
        int fromIndex = pageIndex * LIGNES_PAR_PAGE;
        int toIndex = Math.min(fromIndex + LIGNES_PAR_PAGE, masterData.size());

        if (fromIndex <= toIndex && fromIndex >= 0 && getTable() != null) {
            getTable().setItems(FXCollections.observableArrayList(masterData.subList(fromIndex, toIndex)));
        } else if (getTable() != null) {
            getTable().setItems(FXCollections.observableArrayList());
        }
    }

    /**
     * Charger donnees dans tableau.
     */
    protected void chargerDonneesDansTableau() {
        afficherPlaceholderChargement();

        new Thread(() -> {
            try {
                List<Produit> listeProduits = stockService.SearchProduit("", getCategorieProduit());
                List<T> listeDepuisBdd = new ArrayList<>();
                for (Produit p : listeProduits) {
                    if (getProduitClass().isInstance(p)) {
                        listeDepuisBdd.add(getProduitClass().cast(p));
                    }
                }

                Platform.runLater(() -> {
                    masterData.setAll(listeDepuisBdd);
                    mettreAJourPagination();
                    afficherPlaceholderVide();
                });

            } catch (Exception e) {
                System.err.println("Erreur lors du chargement des " + getCategorieProduit() + " via le service !");
                e.printStackTrace();
                Platform.runLater(() -> {
                    if (getTable() != null) {
                        Label lblErreur = new Label("Erreur de connexion à la base de données.");
                        lblErreur.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                        getTable().setPlaceholder(lblErreur);
                    }
                });
            }
        }).start();
    }

    private void afficherPlaceholderChargement() {
        if (getTable() == null) return;
        VBox loadingBox = new VBox(10);
        loadingBox.setAlignment(Pos.CENTER);
        ProgressIndicator spinner = new ProgressIndicator();
        spinner.setPrefSize(40, 40);
        spinner.setStyle("-fx-progress-color: #ffcc00;");
        Label loadingLabel = new Label("Chargement des " + getCategorieProduit().toLowerCase() + " en cours...");
        loadingLabel.setStyle("-fx-text-fill: #888888; -fx-font-size: 14px;");
        loadingBox.getChildren().addAll(spinner, loadingLabel);
        getTable().setPlaceholder(loadingBox);
    }

    private void afficherPlaceholderVide() {
        if (getTable() == null) return;
        Label emptyLabel = new Label("Aucun " + getCategorieProduit().toLowerCase() + " trouvé dans la bibliothèque.");
        emptyLabel.setStyle("-fx-text-fill: #888888; -fx-font-size: 14px; -fx-font-style: italic;");
        getTable().setPlaceholder(emptyLabel);
    }

    /**
     * Button return.
     *
     * @param actionEvent the action event
     */
    @FXML
    public void ButtonReturn(ActionEvent actionEvent) {
        if (MenuController.getInstance() != null) {
            MenuController.getInstance().chargerPage("liste");
            MenuController.getInstance().changerTitre("Inventaire");
        } else {
            System.err.println("ERREUR : MenuController est null.");
        }
    }

    private void configurerClicSurLigne() {
        if (getTable() == null) return;
        getTable().setRowFactory(tv -> {
            TableRow<T> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 1) {
                    T produitSelectionne = row.getItem();
                    afficherPopupExemplaire(produitSelectionne);
                }
            });
            return row;
        });
    }

    private void configurerColonneAction() {
        if (getColAction() == null) return;
        Callback<TableColumn<T, Void>, TableCell<T, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<T, Void> call(final TableColumn<T, Void> param) {
                return new TableCell<>() {
                    private final Button btnExemplaires = new Button();
                    private final Button btnModifier = new Button();
                    private final Button btnSupprimer = new Button();

                    {
                        javafx.scene.shape.SVGPath iconBook = new javafx.scene.shape.SVGPath();
                        iconBook.setContent("M4 6H2v14c0 1.1.9 2 2 2h14v-2H4V6zm16-4H8c-1.1 0-2 .9-2 2v12c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V4c0-1.1-.9-2-2-2zm-1 9H9V9h10v2zm-4 4H9v-2h6v2zm4-8H9V5h10v2z");
                        iconBook.setFill(javafx.scene.paint.Color.WHITE);

                        javafx.scene.shape.SVGPath iconEdit = new javafx.scene.shape.SVGPath();
                        iconEdit.setContent("M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25zM20.71 7.04c.39-.39.39-1.02 0-1.41l-2.34-2.34c-.39-.39-1.02-.39-1.41 0l-1.83 1.83 3.75 3.75 1.83-1.83z");
                        iconEdit.setFill(javafx.scene.paint.Color.WHITE);

                        javafx.scene.shape.SVGPath iconDelete = new javafx.scene.shape.SVGPath();
                        iconDelete.setContent("M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z");
                        iconDelete.setFill(javafx.scene.paint.Color.WHITE);

                        btnExemplaires.setGraphic(iconBook);
                        btnModifier.setGraphic(iconEdit);
                        btnSupprimer.setGraphic(iconDelete);

                        btnExemplaires.setTooltip(new Tooltip("Voir les exemplaires"));
                        btnModifier.setTooltip(new Tooltip("Modifier le produit"));
                        btnSupprimer.setTooltip(new Tooltip("Retirer ce produit"));

                        String baseStyle = "-fx-cursor: hand; -fx-background-radius: 4; -fx-min-width: 30px; -fx-min-height: 30px; -fx-max-width: 30px; -fx-max-height: 30px; -fx-padding: 0;";
                        btnExemplaires.setStyle("-fx-background-color: #3498db; " + baseStyle);
                        btnModifier.setStyle("-fx-background-color: #f39c12; " + baseStyle);
                        btnSupprimer.setStyle("-fx-background-color: #e74c3c; " + baseStyle);

                        btnExemplaires.setOnAction(event -> {
                            event.consume();
                            afficherPopupExemplaire(getTableView().getItems().get(getIndex()));
                        });

                        btnModifier.setOnAction(event -> {
                            event.consume();
                            T produitSelectionne = getTableView().getItems().get(getIndex());
                            modifierProduit(produitSelectionne);
                        });

                        btnSupprimer.setOnAction(event -> {
                            event.consume();
                            T produitSelectionne = getTableView().getItems().get(getIndex());

                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Confirmation de retrait");
                            alert.setHeaderText("Retirer le " + getCategorieProduit().toLowerCase() + " : " + produitSelectionne.getTitre());
                            alert.setContentText("Attention : Ce produit n'est plus actif, et tous ses exemplaires passeront en statut 'RETIRE'. Continuer ?");

                            if (alert.showAndWait().get() == ButtonType.OK) {
                                try {
                                    stockService.supprimerProduit(produitSelectionne);
                                    // On ne retire plus l'élément de la liste pour le garder affiché
                                    // On rafraîchit simplement le tableau
                                    getTable().refresh();
                                } catch (SQLException sqlException) {
                                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                                    errorAlert.setTitle("Erreur de retrait");
                                    errorAlert.setHeaderText("Impossible de retirer le produit");
                                    errorAlert.setContentText(sqlException.getMessage());
                                    errorAlert.showAndWait();
                                }
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || getIndex() >= getTableView().getItems().size()) {
                            setGraphic(null);
                        } else {
                            HBox box = new HBox(8, btnModifier, btnSupprimer, btnExemplaires);
                            box.setAlignment(Pos.CENTER);
                            setGraphic(box);
                        }
                    }
                };
            }
        };
        getColAction().setCellFactory(cellFactory);
    }

    /**
     * Modifier produit.
     *
     * @param produit the produit
     */
    protected void modifierProduit(T produit) {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Modifier : " + produit.getTitre());

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #2b2b2b; -fx-text-fill: white;");

        Label lblTitre = new Label("Modification du produit");
        lblTitre.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #ffcc00;");
        TextField txtTitre = new TextField(produit.getTitre());
        TextField txtDescription = new TextField(produit.getDescription());
        TextField txtEditeur = new TextField(produit.getEditeur());
        TextField txtAnnee = new TextField(String.valueOf(produit.getAnneeSortie()));

        VBox formBox = new VBox(10);
        formBox.getChildren().addAll(
                new Label("Titre :"), txtTitre,
                new Label("Description :"), txtDescription,
                new Label("Éditeur :"), txtEditeur,
                new Label("Année de sortie :"), txtAnnee
        );
        TextField txtAuteur = new TextField();
        TextField txtIsbn = new TextField();
        TextField txtPages = new TextField();
        TextField txtFormat = new TextField();
        TextField txtRealisateur = new TextField();
        TextField txtDuree = new TextField();
        TextField txtAudio = new TextField();
        TextField txtSousTitres = new TextField();
        TextField txtJoueursMin = new TextField();
        TextField txtJoueursMax = new TextField();
        TextField txtAgeMin = new TextField();
        TextField txtDureePartie = new TextField();
        if (produit instanceof Livre l) {
            txtAuteur.setText(l.getAuteur());
            txtIsbn.setText(l.getIsbn());
            txtPages.setText(String.valueOf(l.getNbPages()));
            txtFormat.setText(l.getFormat());
            formBox.getChildren().addAll(
                    new Label("Auteur :"), txtAuteur,
                    new Label("ISBN :"), txtIsbn,
                    new Label("Nombre de pages :"), txtPages,
                    new Label("Format :"), txtFormat
            );
        } else if (produit instanceof DVD d) {
            txtRealisateur.setText(d.getRealisateur());
            txtDuree.setText(String.valueOf(d.getDureeMinutes()));
            txtAudio.setText(String.join(",", d.getAudioLangues()));
            txtSousTitres.setText(d.getSousTitres() != null ? String.join(",", d.getSousTitres()) : "");
            formBox.getChildren().addAll(
                    new Label("Réalisateur :"), txtRealisateur,
                    new Label("Durée (minutes) :"), txtDuree,
                    new Label("Langues audio (séparées par des virgules) :"), txtAudio,
                    new Label("Sous-titres (séparés par des virgules) :"), txtSousTitres
            );
        } else if (produit instanceof JeuSociete j) {
            txtJoueursMin.setText(String.valueOf(j.getNbJoueursMin()));
            txtJoueursMax.setText(String.valueOf(j.getNbJoueursMax()));
            txtAgeMin.setText(String.valueOf(j.getAgeMin()));
            txtDureePartie.setText(String.valueOf(j.getDureePartie()));
            formBox.getChildren().addAll(
                    new Label("Joueurs Minimum :"), txtJoueursMin,
                    new Label("Joueurs Maximum :"), txtJoueursMax,
                    new Label("Âge Minimum :"), txtAgeMin,
                    new Label("Durée de partie (minutes) :"), txtDureePartie
            );
        }

        Button btnEnregistrer = new Button("Enregistrer les modifications");
        btnEnregistrer.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-cursor: hand; -fx-font-weight: bold;");

        btnEnregistrer.setOnAction(event -> {
            try {
                produit.setTitre(txtTitre.getText());
                produit.setDescription(txtDescription.getText());
                produit.setEditeur(txtEditeur.getText());
                produit.setAnneeSortie(Integer.parseInt(txtAnnee.getText()));
                if (produit instanceof Livre l) {
                    l.setAuteur(txtAuteur.getText());
                    l.setIsbn(txtIsbn.getText());
                    l.setNbPages(Integer.parseInt(txtPages.getText()));
                    l.setFormat(txtFormat.getText());
                } else if (produit instanceof DVD d) {
                    d.setRealisateur(txtRealisateur.getText());
                    d.setDureeMinutes(Integer.parseInt(txtDuree.getText()));
                    d.setAudioLangues(Arrays.asList(txtAudio.getText().split(",")));
                    if (!txtSousTitres.getText().isEmpty()) {
                        d.setSousTitres(Arrays.asList(txtSousTitres.getText().split(",")));
                    } else {
                        d.setSousTitres(new ArrayList<>());
                    }
                } else if (produit instanceof JeuSociete j) {
                    j.setNbJoueursMin(Integer.parseInt(txtJoueursMin.getText()));
                    j.setNbJoueursMax(Integer.parseInt(txtJoueursMax.getText()));
                    j.setAgeMin(Integer.parseInt(txtAgeMin.getText()));
                    j.setDureePartie(Integer.parseInt(txtDureePartie.getText()));
                }
                stockService.modifierProduit(produit);

                getTable().refresh();
                popup.close();

            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de saisie");
                alert.setHeaderText("Les champs numériques ne sont pas valides.");
                alert.setContentText("Veuillez vérifier les nombres saisis (Année, Pages, Durée, etc.).");
                alert.showAndWait();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur BDD");
                alert.setHeaderText("Impossible de sauvegarder les modifications.");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        });

        Button btnAnnuler = new Button("Annuler");
        btnAnnuler.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-cursor: hand;");
        btnAnnuler.setOnAction(event -> popup.close());

        HBox boutonsBox = new HBox(15, btnEnregistrer, btnAnnuler);
        boutonsBox.setAlignment(Pos.CENTER);
        boutonsBox.setPadding(new Insets(15, 0, 0, 0));

        root.getChildren().addAll(lblTitre, formBox, boutonsBox);

        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #2b2b2b; -fx-border-color: transparent;");

        Scene scene = new Scene(scrollPane, 450, 650);
        popup.setScene(scene);
        popup.showAndWait();
    }
    private void afficherPopupExemplaire(T produit) {
        try {
            produit.setExemplaires(com.eseo.mediastock.dao.ExemplaireDAO.getExemplairesByProduit(produit));
        } catch (java.sql.SQLException e) {
            System.err.println("Erreur chargement exemplaires.");
        }
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Exemplaires - " + getCategorieProduit() + " n°" + produit.getId());

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #2b2b2b; -fx-text-fill: white;");

        Label lblTitre = new Label("Liste des exemplaires : " + produit.getTitre());
        lblTitre.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #ffcc00;");
        List<Exemplaire> listeExemplaires = stockService.getExemplaireFromProduct(produit);
        if (listeExemplaires == null) listeExemplaires = new ArrayList<>();
        ObservableList<Exemplaire> donneesExemplaires = FXCollections.observableArrayList(listeExemplaires);

        Pagination paginationExemplaire = new Pagination();
        int lignesParPagePopup = 10;
        int nbPages = (int) Math.ceil((double) donneesExemplaires.size() / lignesParPagePopup);
        paginationExemplaire.setPageCount(nbPages == 0 ? 1 : nbPages);

        paginationExemplaire.setPageFactory(pageIndex -> {
            TableView<Exemplaire> tablePage = new TableView<>();
            tablePage.setStyle("-fx-background-color: #383838;");
            tablePage.setOnKeyPressed(event -> {
                if (event.isShortcutDown() && event.getCode() == javafx.scene.input.KeyCode.C) {
                    Exemplaire selectedItem = tablePage.getSelectionModel().getSelectedItem();
                    if (selectedItem != null) {
                        final javafx.scene.input.Clipboard clipboard = javafx.scene.input.Clipboard.getSystemClipboard();
                        final javafx.scene.input.ClipboardContent content = new javafx.scene.input.ClipboardContent();
                        content.putString(selectedItem.getCodeBarre());
                        clipboard.setContent(content);
                    }
                }
            });
            tablePage.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    Exemplaire selectedItem = tablePage.getSelectionModel().getSelectedItem();
                    if (selectedItem != null) {
                        final javafx.scene.input.Clipboard clipboard = javafx.scene.input.Clipboard.getSystemClipboard();
                        final javafx.scene.input.ClipboardContent content = new javafx.scene.input.ClipboardContent();
                        content.putString(selectedItem.getCodeBarre());
                        clipboard.setContent(content);
                    }
                }
            });
            tablePage.setRowFactory(tv -> {
                TableRow<Exemplaire> row = new TableRow<>();
                ContextMenu contextMenu = new ContextMenu();
                MenuItem copyItem = new MenuItem("Copier le Code Barre");
                copyItem.setOnAction(event -> {
                    if (row.getItem() != null) {
                        final javafx.scene.input.Clipboard clipboard = javafx.scene.input.Clipboard.getSystemClipboard();
                        final javafx.scene.input.ClipboardContent content = new javafx.scene.input.ClipboardContent();
                        content.putString(row.getItem().getCodeBarre());
                        clipboard.setContent(content);
                    }
                });
                contextMenu.getItems().add(copyItem);
                row.contextMenuProperty().bind(
                        javafx.beans.binding.Bindings.when(row.emptyProperty()).then((ContextMenu) null).otherwise(contextMenu)
                );
                return row;
            });

            Label placeholder = new Label("Aucun exemplaire trouvé.");
            placeholder.setStyle("-fx-text-fill: #aaaaaa; -fx-font-style: italic;");
            tablePage.setPlaceholder(placeholder);

            TableColumn<Exemplaire, String> colRef = new TableColumn<>("Code Barre");
            colRef.setCellValueFactory(new PropertyValueFactory<>("codeBarre"));
            colRef.setPrefWidth(150);

            TableColumn<Exemplaire, String> colEtat = new TableColumn<>("État Physique");
            colEtat.setCellValueFactory(new PropertyValueFactory<>("etatPhysique"));
            colEtat.setPrefWidth(120);

            TableColumn<Exemplaire, String> colDispo = new TableColumn<>("Disponibilité");
            colDispo.setCellValueFactory(new PropertyValueFactory<>("statusDispo"));
            colDispo.setPrefWidth(120);
            TableColumn<Exemplaire, Void> colActionEx = new TableColumn<>("Actions");
            colActionEx.setPrefWidth(100);
            colActionEx.setCellFactory(param -> new TableCell<>() {
                private final Button btnEdit = new Button();
                private final Button btnDel = new Button();

                {
                    javafx.scene.shape.SVGPath iconEdit = new javafx.scene.shape.SVGPath();
                    iconEdit.setContent("M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25zM20.71 7.04c.39-.39.39-1.02 0-1.41l-2.34-2.34c-.39-.39-1.02-.39-1.41 0l-1.83 1.83 3.75 3.75 1.83-1.83z");
                    iconEdit.setFill(javafx.scene.paint.Color.WHITE);

                    javafx.scene.shape.SVGPath iconDelete = new javafx.scene.shape.SVGPath();
                    iconDelete.setContent("M6 19c0 1.1.9 2 2 2h8c1.1 0 2-.9 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z");
                    iconDelete.setFill(javafx.scene.paint.Color.WHITE);

                    btnEdit.setGraphic(iconEdit);
                    btnDel.setGraphic(iconDelete);

                    String baseStyle = "-fx-cursor: hand; -fx-background-radius: 4; -fx-min-width: 30px; -fx-min-height: 30px; -fx-max-width: 30px; -fx-max-height: 30px; -fx-padding: 0;";
                    btnEdit.setStyle("-fx-background-color: #f39c12; " + baseStyle);
                    btnDel.setStyle("-fx-background-color: #e74c3c; " + baseStyle);

                    btnEdit.setOnAction(e -> {
                        e.consume();
                        Exemplaire ex = getTableView().getItems().get(getIndex());
                        afficherPopupModifierExemplaire(ex, tablePage);
                    });

                    btnDel.setOnAction(e -> {
                        e.consume();
                        Exemplaire ex = getTableView().getItems().get(getIndex());

                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Confirmation de retrait");
                        alert.setHeaderText("Retirer l'exemplaire : " + ex.getCodeBarre() + " ?");
                        alert.setContentText("Êtes-vous sûr de vouloir passer cet exemplaire en statut 'RETIRE' ?");

                        if (alert.showAndWait().get() == ButtonType.OK) {
                            try {
                                stockService.supprimerExemplaire(ex);
                                // On ne supprime plus la ligne du tableau
                                // On met à jour l'état local pour refléter la base de données
                                ex.setStatusDispo(EnumDispo.RETIRE);
                                tablePage.refresh();
                            } catch (SQLException sqlException) {
                                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                                errorAlert.setTitle("Erreur");
                                errorAlert.setHeaderText("Échec du retrait");
                                errorAlert.setContentText(sqlException.getMessage());
                                errorAlert.showAndWait();
                            }
                        }
                    });
                }

                @Override
                public void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || getIndex() >= getTableView().getItems().size()) {
                        setGraphic(null);
                    } else {
                        HBox box = new HBox(8, btnEdit, btnDel);
                        box.setAlignment(Pos.CENTER);
                        setGraphic(box);
                    }
                }
            });

            tablePage.getColumns().addAll(colRef, colEtat, colDispo, colActionEx);

            if (donneesExemplaires.isEmpty()) {
                tablePage.setItems(FXCollections.observableArrayList());
            } else {
                int fromIndex = pageIndex * lignesParPagePopup;
                int toIndex = Math.min(fromIndex + lignesParPagePopup, donneesExemplaires.size());
                tablePage.setItems(FXCollections.observableArrayList(donneesExemplaires.subList(fromIndex, toIndex)));
            }

            return tablePage;
        });

        Button btnFermer = new Button("Fermer");
        btnFermer.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-cursor: hand;");
        btnFermer.setOnAction(e -> popup.close());

        root.getChildren().addAll(lblTitre, paginationExemplaire, btnFermer);

        Scene scene = new Scene(root, 600, 450);
        popup.setScene(scene);
        popup.showAndWait();
    }

    private void afficherPopupModifierExemplaire(Exemplaire exemplaire, TableView<Exemplaire> tableParent) {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Modifier l'exemplaire : " + exemplaire.getCodeBarre());

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #2b2b2b; -fx-text-fill: white;");

        Label lblTitre = new Label("Modification de l'exemplaire");
        lblTitre.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #ffcc00;");

        ComboBox<EnumEtat> cbEtat = new ComboBox<>(FXCollections.observableArrayList(EnumEtat.values()));
        cbEtat.setValue(exemplaire.getEtatPhysique());
        cbEtat.setStyle("-fx-font-size: 14px;");

        ComboBox<EnumDispo> cbDispo = new ComboBox<>(FXCollections.observableArrayList(EnumDispo.values()));
        cbDispo.setValue(exemplaire.getStatusDispo());
        cbDispo.setStyle("-fx-font-size: 14px;");

        Button btnEnregistrer = new Button("Enregistrer");
        btnEnregistrer.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-cursor: hand; -fx-font-weight: bold;");
        btnEnregistrer.setOnAction(e -> {
            try {
                exemplaire.setEtatPhysique(cbEtat.getValue());
                exemplaire.setStatusDispo(cbDispo.getValue());
                stockService.modifierExemplaire(exemplaire);
                tableParent.refresh();
                popup.close();

            } catch (SQLException sqlException) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur BDD");
                alert.setHeaderText("Impossible de modifier l'exemplaire");
                alert.setContentText(sqlException.getMessage());
                alert.showAndWait();
            }
        });

        root.getChildren().addAll(
                lblTitre,
                new Label("État Physique :"), cbEtat,
                new Label("Statut :"), cbDispo,
                btnEnregistrer
        );

        Scene scene = new Scene(root, 350, 320);
        popup.setScene(scene);
        popup.showAndWait();
    }
}