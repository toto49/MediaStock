package com.eseo.mediastock.controller;

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
                });

            } catch (Exception e) {
                System.err.println("Erreur lors du chargement des " + getCategorieProduit() + " via le service !");
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
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Exemplaires - " + getCategorieProduit() + " n°" + produit.getId());

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #2b2b2b; -fx-text-fill: white;");

        Label lblTitre = new Label("Liste des exemplaires pour le " + getCategorieProduit() + " n°" + produit.getId());
        lblTitre.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #ffcc00;");

        // TODO: Remplacer Object par Exemplaire
        ObservableList<Object> donneesExemplaires = FXCollections.observableArrayList();

        Pagination paginationExemplaire = new Pagination();
        int lignesParPagePopup = 10;
        int nbPages = (int) Math.ceil((double) donneesExemplaires.size() / lignesParPagePopup);
        paginationExemplaire.setPageCount(nbPages == 0 ? 1 : nbPages);

        paginationExemplaire.setPageFactory(pageIndex -> {
            TableView<Object> tablePage = new TableView<>();
            tablePage.setStyle("-fx-background-color: #383838;");

            Label placeholder = new Label("Aucun exemplaire trouvé pour ce " + getCategorieProduit().toLowerCase() + ".");
            placeholder.setStyle("-fx-text-fill: #aaaaaa; -fx-font-style: italic;");
            tablePage.setPlaceholder(placeholder);

            TableColumn<Object, String> colRef = new TableColumn<>("Référence");
            colRef.setCellValueFactory(new PropertyValueFactory<>("reference"));
            colRef.setPrefWidth(150);

            TableColumn<Object, String> colEtat = new TableColumn<>("État");
            colEtat.setCellValueFactory(new PropertyValueFactory<>("etat"));
            colEtat.setPrefWidth(120);

            tablePage.getColumns().addAll(colRef, colEtat);

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