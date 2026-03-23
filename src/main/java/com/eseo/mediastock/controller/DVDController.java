package com.eseo.mediastock.controller;

import com.eseo.mediastock.model.Produits.DVD;
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * Contrôleur spécifique à la vue d'inventaire des DVD.
 * <p>
 * Hérite de {@link AbstractProduitController} et définit les colonnes spécifiques
 * au tableau des DVD (réalisateur, durée, langues, etc.) ainsi que le type d'entité à manipuler.
 * </p>
 */
public class DVDController extends AbstractProduitController<DVD> {

    @FXML
    private TableView<DVD> tableDVD;
    @FXML
    private Pagination paginationDVD;
    @FXML
    private TableColumn<DVD, Void> colAction;

    /**
     * Initialize.
     */
    @FXML
    public void initialize() {
        initCommon();
    }

    @Override
    protected TableView<DVD> getTable() {
        return tableDVD;
    }

    @Override
    protected Pagination getPagination() {
        return paginationDVD;
    }

    @Override
    protected TableColumn<DVD, Void> getColAction() {
        return colAction;
    }

    @Override
    protected String getCategorieProduit() {
        return "DVD";
    }

    @Override
    protected Class<DVD> getProduitClass() {
        return DVD.class;
    }
}