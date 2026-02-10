package com.eseo.mediastock.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {

    private static MenuController instance;

    @FXML
    private BorderPane mainPane;
    @FXML
    private Button btnAccueil;
    @FXML
    private Button btnParametres;
    @FXML
    public Button btnListView;

    public static MenuController getInstance() {
        return instance;
    }

    @FXML
    public void initialize() {
        instance = this;
        chargerPage("accueil");

        // 3. Style par défaut
        if (btnAccueil != null) updateButtonStyles(btnAccueil);
    }

    public void chargerPage(String nomFichier) {
        try {

            String chemin = "/com/eseo/mediastock/view/" + nomFichier + "-view.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(chemin));
            Parent vue = loader.load();
            Object controller = loader.getController();


            if (controller instanceof ListeViewController) {

            }
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
    void afficherParametres(ActionEvent event) {
        chargerPage("parametre");
        updateButtonStyles(btnParametres);
        changerTitre("Paramètres");
    }

    @FXML
    void afficherListeView(ActionEvent event) {
        chargerPage("liste"); // Charge la liste générique par défaut
        updateButtonStyles(btnListView);
        changerTitre("Inventaire");
    }


    private void updateButtonStyles(Button boutonActif) {
        String STYLE_INACTIF = "-fx-cursor: hand; -fx-background-color: transparent; -fx-text-fill: white;";
        String STYLE_ACTIF = "-fx-cursor: hand; -fx-background-color: #444; -fx-text-fill: #ffcc00; -fx-font-weight: bold; -fx-border-width: 0 0 0 5; -fx-border-color: #ffcc00;";

        // Sécurité : on vérifie que les boutons ne sont pas null avant de changer le style
        if (btnAccueil != null) btnAccueil.setStyle(STYLE_INACTIF);
        if (btnParametres != null) btnParametres.setStyle(STYLE_INACTIF);
        if (btnListView != null) btnListView.setStyle(STYLE_INACTIF);

        if (boutonActif != null) boutonActif.setStyle(STYLE_ACTIF);
    }

    public void changerTitre(String nouveauTitre) {
        // On récupère la fenêtre (Stage) à partir du panneau principal
        if (mainPane != null && mainPane.getScene() != null) {
            Stage stage = (Stage) mainPane.getScene().getWindow();
            stage.setTitle("MediaStock - " + nouveauTitre);
        }
    }

}