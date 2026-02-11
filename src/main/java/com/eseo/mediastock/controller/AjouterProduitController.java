package com.eseo.mediastock.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class AjouterProduitController implements Initializable {
    @FXML
    private VBox containerFormulaire;
    @FXML
    private ComboBox<String> ChoiceAddProduit;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ChoiceAddProduit.valueProperty().addListener((observable, oldValue, newValue) -> {
            detecterSelection(newValue);
        });
    }


    private void detecterSelection(String choix) {
        if (choix == null) return;
        containerFormulaire.getChildren().clear();
        String styleLabel = "-fx-font-size: 15px; -fx-text-fill: #ffcc00;";
        switch (choix) {
            case "LIVRES":

                // Titre
                Label lblTitre = new Label("Titre :");
                lblTitre.setStyle(styleLabel);
                TextField txtTitre = new TextField();
                txtTitre.setPromptText("Ex: Les Misérables");

                // Auteur
                Label lblAuteur = new Label("Auteur :");
                lblAuteur.setStyle(styleLabel);
                TextField txtAuteur = new TextField();
                txtAuteur.setPromptText("Ex: Victor Hugo");

                // Éditeur
                Label lblEditeur = new Label("Éditeur :");
                lblEditeur.setStyle(styleLabel);
                TextField txtEditeur = new TextField();
                txtEditeur.setPromptText("Ex: Gallimard");

                // Année
                Label lblAnnee = new Label("Année :");
                lblAnnee.setStyle(styleLabel);
                TextField txtAnnee = new TextField();
                txtAnnee.setPromptText("Ex: 1862");

                // ISBN
                Label lblIsbn = new Label("ISBN :");
                lblIsbn.setStyle(styleLabel);
                TextField txtIsbn = new TextField();
                txtIsbn.setPromptText("Ex: 978-2070409228");

                // Pages
                Label lblPages = new Label("Pages :");
                lblPages.setStyle(styleLabel);
                TextField txtPages = new TextField();
                txtPages.setPromptText("Ex: 500");

                // Format
                Label lblFormat = new Label("Format :");
                lblFormat.setStyle(styleLabel);
                TextField txtFormat = new TextField();
                txtFormat.setPromptText("Ex: Poche, Broché...");

                // --- Bouton de validation ---
                Button btnValider = new Button("Ajouter le Livre");
                Label lblOutput = new Label();
                lblOutput.setStyle("-fx-text-fill: white;");

                btnValider.setOnAction(event -> {
                    String resultat = "Livre ajouté : " +
                            txtTitre.getText() + " | " +
                            txtAuteur.getText() + " | " +
                            txtEditeur.getText() + " (" + txtAnnee.getText() + ")";

                    lblOutput.setText(resultat);
                    System.out.println(resultat); // Affiche aussi dans la console

                    // TODO Connecter au ProduitDAO ( STP MORGIANE OU REMI)
                });

                // --- Ajout de TOUT au conteneur ---
                containerFormulaire.getChildren().addAll(
                        lblTitre, txtTitre,
                        lblAuteur, txtAuteur,
                        lblEditeur, txtEditeur,
                        lblAnnee, txtAnnee,
                        lblIsbn, txtIsbn,
                        lblPages, txtPages,
                        lblFormat, txtFormat,
                        btnValider, lblOutput
                );
                break;
            case "DVD":

                // Titre
                Label lblTitreDvd = new Label("Titre :");
                lblTitreDvd.setStyle(styleLabel);
                TextField txtTitreDvd = new TextField();
                txtTitreDvd.setPromptText("Ex: Inception");

                // Réalisateur
                Label lblReal = new Label("Réalisateur :");
                lblReal.setStyle(styleLabel);
                TextField txtReal = new TextField();
                txtReal.setPromptText("Ex: Christopher Nolan");

                // Année
                Label lblAnneeDvd = new Label("Année :");
                lblAnneeDvd.setStyle(styleLabel);
                TextField txtAnneeDvd = new TextField();
                txtAnneeDvd.setPromptText("Ex: 2010");

                // Durée
                Label lblDuree = new Label("Durée (min) :");
                lblDuree.setStyle(styleLabel);
                TextField txtDuree = new TextField();
                txtDuree.setPromptText("Ex: 148");

                // Éditeur
                Label lblEditeurDvd = new Label("Éditeur :");
                lblEditeurDvd.setStyle(styleLabel);
                TextField txtEditeurDvd = new TextField();
                txtEditeurDvd.setPromptText("Ex: Warner Bros");

                // Audio
                Label lblAudio = new Label("Audio :");
                lblAudio.setStyle(styleLabel);
                TextField txtAudio = new TextField();
                txtAudio.setPromptText("Ex: Français, Anglais 5.1");

                // Sous-titres
                Label lblSousTitres = new Label("Sous-titres :");
                lblSousTitres.setStyle(styleLabel);
                TextField txtSousTitres = new TextField();
                txtSousTitres.setPromptText("Ex: Français, Espagnol");

                // --- Bouton de validation ---
                Button btnValiderDvd = new Button("Ajouter le DVD");
                Label lblOutputDvd = new Label();
                lblOutputDvd.setStyle("-fx-text-fill: white;");

                // Action du bouton
                btnValiderDvd.setOnAction(event -> {
                    String resultat = "DVD ajouté : " +
                            txtTitreDvd.getText() + " par " +
                            txtReal.getText() + " (" + txtDuree.getText() + " min)";

                    lblOutputDvd.setText(resultat);
                    System.out.println(resultat);
                });

                // --- Ajout de TOUT au conteneur ---
                containerFormulaire.getChildren().addAll(
                        lblTitreDvd, txtTitreDvd,
                        lblReal, txtReal,
                        lblAnneeDvd, txtAnneeDvd,
                        lblDuree, txtDuree,
                        lblEditeurDvd, txtEditeurDvd,
                        lblAudio, txtAudio,
                        lblSousTitres, txtSousTitres,
                        btnValiderDvd, lblOutputDvd
                );
                break;

            case "JEUX":

                // Titre
                Label lblTitreJeux = new Label("Titre :");
                lblTitreJeux.setStyle(styleLabel);
                TextField txtTitreJeux = new TextField();
                txtTitreJeux.setPromptText("Ex: Catan");

                // Éditeur
                Label lblEditeurJeux = new Label("Éditeur :");
                lblEditeurJeux.setStyle(styleLabel);
                TextField txtEditeurJeux = new TextField();
                txtEditeurJeux.setPromptText("Ex: Filos");

                // Année
                Label lblAnneeJeux = new Label("Année :");
                lblAnneeJeux.setStyle(styleLabel);
                TextField txtAnneeJeux = new TextField();
                txtAnneeJeux.setPromptText("Ex: 1995");

                // Joueurs Min
                Label lblJMin = new Label("Joueurs Min :");
                lblJMin.setStyle(styleLabel);
                TextField txtJMin = new TextField();
                txtJMin.setPromptText("Ex: 2");

                // Joueurs Max
                Label lblJMax = new Label("Joueurs Max :");
                lblJMax.setStyle(styleLabel);
                TextField txtJMax = new TextField();
                txtJMax.setPromptText("Ex: 4");

                // Age Min
                Label lblAgeMin = new Label("Age Minimum :");
                lblAgeMin.setStyle(styleLabel);
                TextField txtAgeMin = new TextField();
                txtAgeMin.setPromptText("Ex: 10");

                // Durée
                Label lblDureeJeux = new Label("Durée (min) :");
                lblDureeJeux.setStyle(styleLabel);
                TextField txtDureeJeux = new TextField();
                txtDureeJeux.setPromptText("Ex: 60");

                // --- Bouton de validation ---
                Button btnValiderJeux = new Button("Ajouter le Jeu");
                Label lblOutputJeux = new Label();
                lblOutputJeux.setStyle("-fx-text-fill: white;");

                // Action du bouton
                btnValiderJeux.setOnAction(event -> {
                    String resultat = "Jeu ajouté : " +
                            txtTitreJeux.getText() + " (" +
                            txtJMin.getText() + " à " + txtJMax.getText() + " joueurs)";

                    lblOutputJeux.setText(resultat);
                    System.out.println(resultat);
                });

                // --- Ajout de TOUT au conteneur ---
                containerFormulaire.getChildren().addAll(
                        lblTitreJeux, txtTitreJeux,
                        lblEditeurJeux, txtEditeurJeux,
                        lblAnneeJeux, txtAnneeJeux,
                        lblJMin, txtJMin,
                        lblJMax, txtJMax,
                        lblAgeMin, txtAgeMin,
                        lblDureeJeux, txtDureeJeux,
                        btnValiderJeux, lblOutputJeux
                );
                break;
            default:
                System.out.println("Choix inconnu");
        }
    }
}