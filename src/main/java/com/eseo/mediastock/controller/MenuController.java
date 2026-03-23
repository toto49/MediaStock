package com.eseo.mediastock.controller;

import com.eseo.mediastock.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {

    private static MenuController instance;
    @FXML
    public Button btnListView;
    @FXML
    private BorderPane mainPane;
    @FXML
    private Button btnAccueil;
    @FXML
    private Button btnParametres;
    @FXML
    private Button btnAddProduit;
    @FXML
    private Button btnEmprunt;
    @FXML
    private Button btnadherent;

    private Stage mainStage;
    private Label lblTitreHeader;

    public static MenuController getInstance() {
        return instance;
    }

    @FXML
    public void initialize() {
        instance = this;
        chargerPage("accueil");
        if (btnAccueil != null) updateButtonStyles(btnAccueil);
    }

    public void setComposantsFenetre(Stage stage, Label labelTitre) {
        this.mainStage = stage;
        this.lblTitreHeader = labelTitre;
    }

    public void chargerPage(String nomFichier) {
        try {
            String chemin = "/com/eseo/mediastock/view/" + nomFichier + "-view.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(chemin));
            Parent vue = loader.load();

            if (mainPane != null) {

                mainPane.setCenter(vue);
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("ERREUR CRITIQUE : Impossible de charger " + nomFichier);
        } catch (NullPointerException e) {
            e.printStackTrace();
            System.err.println("ERREUR : Le chemin " + nomFichier + " semble incorrect ou le fichier n'existe pas.");
        }
    }

    @FXML
    void afficherAccueil(ActionEvent event) {
        chargerPage("accueil");
        updateButtonStyles(btnAccueil);
        changerTitre("Accueil");
    }

    @FXML
    void afficherEmprunt(ActionEvent event) {
        chargerPage("emprunt");
        updateButtonStyles(btnEmprunt);
        changerTitre("Emprunter");
    }

    @FXML
    void afficheradherent(ActionEvent event) {
        chargerPage("adherent");
        updateButtonStyles(btnadherent);
        changerTitre("Gestion Adhérent");
    }

    @FXML
    void afficherAddProduit(ActionEvent event) {
        chargerPage("ajouter-produit");
        updateButtonStyles(btnAddProduit);
        changerTitre("Ajouter Produit");
    }

    @FXML
    void afficherParametres(ActionEvent event) {
        chargerPage("parametre");
        updateButtonStyles(btnParametres);
        changerTitre("Paramètres");
    }

    @FXML
    void afficherListeView(ActionEvent event) {
        chargerPage("liste");
        updateButtonStyles(btnListView);
        changerTitre("Inventaire");
    }

    private void updateButtonStyles(Button boutonActif) {
        String STYLE_INACTIF = "-fx-cursor: hand; -fx-background-color: transparent; -fx-text-fill: white;";
        String STYLE_ACTIF = "-fx-cursor: hand; -fx-background-color: #444; -fx-text-fill: #ffcc00; -fx-font-weight: bold; -fx-border-width: 0 0 0 5; -fx-border-color: #ffcc00;";

        if (btnAccueil != null) btnAccueil.setStyle(STYLE_INACTIF);
        if (btnParametres != null) btnParametres.setStyle(STYLE_INACTIF);
        if (btnListView != null) btnListView.setStyle(STYLE_INACTIF);
        if (btnAddProduit != null) btnAddProduit.setStyle(STYLE_INACTIF);
        if (btnEmprunt != null) btnEmprunt.setStyle(STYLE_INACTIF);
        if (btnadherent != null) btnadherent.setStyle(STYLE_INACTIF);

        if (boutonActif != null) boutonActif.setStyle(STYLE_ACTIF);
    }

    public void changerTitre(String nouveauTitre) {
        HelloApplication.changerTitreGlobal(nouveauTitre);
    }
}