package com.eseo.mediastock.controller;

import com.eseo.mediastock.model.Produits.JeuSociete;
import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * Contrôleur responsable de la vue de gestion des emprunts et des retours.
 * <p>
 * Ce contrôleur est au cœur de l'activité quotidienne de la médiathèque. Il gère :
 * <ul>
 * <li>La recherche et la sélection d'un adhérent.</li>
 * <li>Le scan (ou la saisie) du code-barres d'un exemplaire physique.</li>
 * <li>La validation des transactions d'emprunt (vérification des limites) et de retour.</li>
 * <li>L'affichage de l'historique et des retards éventuels.</li>
 * </ul>
 * </p>
 */
public class JeuxController extends AbstractProduitController<JeuSociete> {

    @FXML
    private TableView<JeuSociete> tableJeux;
    @FXML
    private Pagination paginationJeux;
    @FXML
    private TableColumn<JeuSociete, Void> colAction;

    /**
     * Initialize.
     */
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