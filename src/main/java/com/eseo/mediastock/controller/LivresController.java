package com.eseo.mediastock.controller;

import com.eseo.mediastock.model.Produits.Livre;
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * The type Livres controller.
 */
public class LivresController extends AbstractProduitController<Livre> {

    @FXML
    private TableView<Livre> tableLivres;
    @FXML
    private Pagination paginationLivres;
    @FXML
    private TableColumn<Livre, Void> colAction;

    /**
     * Initialize.
     */
    @FXML
    public void initialize() {
        initCommon();
    }

    @Override
    protected TableView<Livre> getTable() {
        return tableLivres;
    }

    @Override
    protected Pagination getPagination() {
        return paginationLivres;
    }

    @Override
    protected TableColumn<Livre, Void> getColAction() {
        return colAction;
    }

    @Override
    protected String getCategorieProduit() {
        return "Livre";
    }

    @Override
    protected Class<Livre> getProduitClass() {
        return Livre.class;
    }
}