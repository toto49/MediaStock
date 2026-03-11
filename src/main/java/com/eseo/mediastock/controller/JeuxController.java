package com.eseo.mediastock.controller;

import com.eseo.mediastock.model.Produits.JeuSociete;
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class JeuxController extends AbstractProduitController<JeuSociete> {

    @FXML
    private TableView<JeuSociete> tableJeux;
    @FXML
    private Pagination paginationJeux;
    @FXML
    private TableColumn<JeuSociete, Void> colAction;

    @FXML
    public void initialize() {
        initCommon(); // Appelle toute la logique du parent !
    }

    @Override
    protected TableView<JeuSociete> getTable() {
        return tableJeux;
    }

    @Override
    protected Pagination getPagination() {
        return paginationJeux;
    }

    @Override
    protected TableColumn<JeuSociete, Void> getColAction() {
        return colAction;
    }

    @Override
    protected String getCategorieProduit() {
        return "Jeu";
    }

    @Override
    protected Class<JeuSociete> getProduitClass() {
        return JeuSociete.class;
    }
}