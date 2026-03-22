package com.eseo.mediastock.controller;

import com.eseo.mediastock.model.Exemplaire;
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

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractProduitController<T extends Produit> {

    protected final int LIGNES_PAR_PAGE = 50;
    protected final ObservableList<T> masterData = FXCollections.observableArrayList();
    protected StockService stockService;

    protected abstract TableView<T> getTable();

    protected abstract Pagination getPagination();

    protected abstract TableColumn<T, Void> getColAction();

    protected abstract String getCategorieProduit();

    protected abstract Class<T> getProduitClass();

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

    private void chargerDonneesDansTableau() {
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
                    private final Button btn = new Button("Exemplaires");

                    {
                        btn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-cursor: hand;");
                        btn.setOnAction((ActionEvent event) -> {
                            event.consume();
                            T produitSelectionne = getTableView().getItems().get(getIndex());
                            afficherPopupExemplaire(produitSelectionne);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || getIndex() >= getTableView().getItems().size()) {
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
        getColAction().setCellFactory(cellFactory);
    }

    private void afficherPopupExemplaire(T produit) {
        try {
            produit.setExemplaires(com.eseo.mediastock.dao.ExemplaireDAO.getExemplairesByProduit(produit));
        } catch (java.sql.SQLException e) {
            System.err.println("Erreur lors du chargement des exemplaires depuis la base de données.");
            e.printStackTrace();
        }
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Exemplaires - " + getCategorieProduit() + " n°" + produit.getId());

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #2b2b2b; -fx-text-fill: white;");

        Label lblTitre = new Label("Liste des exemplaires pour le " + getCategorieProduit() + " n°" + produit.getId());
        lblTitre.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #ffcc00;");
        List<Exemplaire> listeExemplaires = stockService.getExemplaireFromProduct(produit);

        if (listeExemplaires == null) {
            listeExemplaires = new ArrayList<>();
        }

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
                    Exemplaire item = row.getItem();
                    if (item != null) {
                        final javafx.scene.input.Clipboard clipboard = javafx.scene.input.Clipboard.getSystemClipboard();
                        final javafx.scene.input.ClipboardContent content = new javafx.scene.input.ClipboardContent();
                        content.putString(item.getCodeBarre());
                        clipboard.setContent(content);
                    }
                });
                contextMenu.getItems().add(copyItem);
                row.contextMenuProperty().bind(
                        javafx.beans.binding.Bindings.when(row.emptyProperty())
                                .then((ContextMenu) null)
                                .otherwise(contextMenu)
                );

                return row;
            });

            Label placeholder = new Label("Aucun exemplaire trouvé pour ce " + getCategorieProduit().toLowerCase() + ".");
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

            tablePage.getColumns().addAll(colRef, colEtat, colDispo);

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

        Scene scene = new Scene(root, 500, 450);
        popup.setScene(scene);
        popup.showAndWait();
    }
}