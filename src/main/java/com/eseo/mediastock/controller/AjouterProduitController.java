package com.eseo.mediastock.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ResourceBundle;

public class AjouterProduitController implements Initializable {

    @FXML
    private VBox containerFormulaire;
    @FXML
    private ComboBox<String> ChoiceAddProduit;

    // Style commun pour les labels
    private final String STYLE_LABEL = "-fx-font-size: 15px; -fx-text-fill: #ffcc00;";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ChoiceAddProduit.valueProperty().addListener((observable, oldValue, newValue) -> {
            detecterSelection(newValue);
        });
    }

    private void styliserChamp(TextField field, String promptText) {
        field.setPromptText(promptText);
        String styleNormal = "-fx-background-color: #383838; " +
                "-fx-text-fill: white; " +
                "-fx-background-radius: 10; " +
                "-fx-border-color: #555; " +
                "-fx-border-radius: 10; " +
                "-fx-border-width: 1; " +
                "-fx-padding: 8 12;";

        String styleFocus = "-fx-background-color: #383838; " +
                "-fx-text-fill: white; " +
                "-fx-background-radius: 10; " +
                "-fx-border-color: #ffcc00; " +
                "-fx-border-radius: 10; " +
                "-fx-border-width: 2; " +
                "-fx-padding: 7 11;";

        field.setStyle(styleNormal);
        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                field.setStyle(styleFocus);
            } else {
                field.setStyle(styleNormal);
            }
        });
    }
    private void detecterSelection(String choix) {
        if (choix == null) return;

        containerFormulaire.getChildren().clear();

        // Variables communes
        Label lblTitre = createLabel("Titre :");
        TextField txtTitre = new TextField();
        styliserChamp(txtTitre, "Ex: Titre du produit");

        Label lblDesc = createLabel("Description :");
        TextField txtDesc = new TextField();
        styliserChamp(txtDesc, "Ex: Description du produit");
        Label lblEditeur = createLabel("Editeur :");
        TextField txtEditeur = new TextField();
        styliserChamp(txtEditeur, "Ex: Nintendo / Asmodee/ Warnes Bros");

        Label lblStock = createLabel("Nombre d'exemplaires (Stock) :");
        Spinner<Integer> spinStock = createSpinner(0, 1000, 1); // Min 0, Max 1000, Défaut 1

        Button btnValider = new Button();
        btnValider.getStyleClass().add("buttonvalid"); // Votre classe CSS
        Label lblOutput = new Label();
        lblOutput.setStyle("-fx-text-fill: white; -fx-padding: 10 0 0 0;");

        switch (choix) {
            case "LIVRES":
                System.out.println("Formulaire : LIVRES");
                btnValider.setText("Ajouter le Livre");

                // --- Champs Spécifiques LIVRES ---
                Label lblAuteur = createLabel("Auteur :");
                TextField txtAuteur = createTextField("Ex: Victor Hugo");

                Label lblAnnee = createLabel("Année de parution :");
                Spinner<Integer> spinAnnee = createSpinner(1000, 2030, 2020);

                Label lblPages = createLabel("Nombre de pages :");
                Spinner<Integer> spinPages = createSpinner(1, 5000, 200);

                Label lblIsbn = createLabel("ISBN :");
                TextField txtIsbn = createTextField("Ex: 978-2070409228");

                Label lblFormat = createLabel("Format :");
                TextField txtFormat = createTextField("Ex: Poche, Broché...");

                // Action du bouton
                btnValider.setOnAction(event -> {
                    String resultat = "LIVRE AJOUTÉ :\n" +
                            "Titre: " + txtTitre.getText() + "\n" +
                            "Auteur: " + txtAuteur.getText() + "\n" +
                            "Stock: " + spinStock.getValue();
                    lblOutput.setText(resultat);
                    System.out.println(resultat);
                });
                containerFormulaire.getChildren().addAll(
                        lblTitre, txtTitre,
                        lblDesc, txtDesc,
                        lblEditeur, txtEditeur,
                        lblAuteur, txtAuteur,
                        lblAnnee, spinAnnee,
                        lblIsbn, txtIsbn,
                        lblPages, spinPages,
                        lblFormat, txtFormat,
                        lblStock, spinStock,
                        btnValider, lblOutput
                );
                break;

            case "DVD":
                System.out.println("Formulaire : DVD");
                btnValider.setText("Ajouter le DVD");

                // --- Champs Spécifiques DVD ---
                Label lblReal = createLabel("Réalisateur :");
                TextField txtReal = createTextField("Ex: Christopher Nolan");

                Label lblAnneeDvd = createLabel("Année de sortie :");
                Spinner<Integer> spinAnneeDvd = createSpinner(1900, 2030, 2010);

                Label lblDuree = createLabel("Durée (minutes) :");
                Spinner<Integer> spinDuree = createSpinner(1, 500, 120);

                Label lblAudio = createLabel("Pistes Audio :");
                TextField txtAudio = createTextField("Ex: Français, Anglais 5.1");

                Label lblSousTitres = createLabel("Sous-titres :");
                TextField txtSousTitres = createTextField("Ex: Français, Espagnol");

                // Action du bouton
                btnValider.setOnAction(event -> {
                    String resultat = "DVD AJOUTÉ :\n" +
                            "Titre: " + txtTitre.getText() + "\n" +
                            "Réalisateur: " + txtReal.getText() + "\n" +
                            "Stock: " + spinStock.getValue();
                    lblOutput.setText(resultat);
                    System.out.println(resultat);
                });

                containerFormulaire.getChildren().addAll(
                        lblTitre, txtTitre,
                        lblDesc, txtDesc,
                        lblEditeur, txtEditeur,
                        lblReal, txtReal,
                        lblAnneeDvd, spinAnneeDvd,
                        lblDuree, spinDuree,
                        lblAudio, txtAudio,
                        lblSousTitres, txtSousTitres,
                        lblStock, spinStock,
                        btnValider, lblOutput
                );
                break;

            case "JEUX":
                System.out.println("Formulaire : JEUX");
                btnValider.setText("Ajouter le Jeu");

                // --- Champs Spécifiques JEUX ---

                Label lblAnneeJeux = createLabel("Année de sortie :");
                Spinner<Integer> spinAnneeJeux = createSpinner(1980, 2030, 2024);

                Label lblJMin = createLabel("Joueurs Minimum :");
                Spinner<Integer> spinJMin = createSpinner(1, 100, 1);

                Label lblJMax = createLabel("Joueurs Maximum :");
                Spinner<Integer> spinJMax = createSpinner(1, 100, 4);

                Label lblAgeMin = createLabel("Age Minimum (PEGI) :");
                Spinner<Integer> spinAgeMin = createSpinner(3, 18, 12);

                Label lblDureeJeux = createLabel("Durée moyenne (min) :");
                Spinner<Integer> spinDureeJeux = createSpinner(5, 600, 30);

                // Action du bouton
                btnValider.setOnAction(event -> {
                    String resultat = "JEU AJOUTÉ :\n" +
                            "Titre: " + txtTitre.getText() + "\n" +
                            "Joueurs: " + spinJMin.getValue() + "-" + spinJMax.getValue() + "\n" +
                            "Stock: " + spinStock.getValue();
                    lblOutput.setText(resultat);
                    System.out.println(resultat);
                });

                containerFormulaire.getChildren().addAll(
                        lblTitre, txtTitre,
                        lblDesc, txtDesc,
                        lblEditeur, txtEditeur,
                        lblAnneeJeux, spinAnneeJeux,
                        lblJMin, spinJMin,
                        lblJMax, spinJMax,
                        lblAgeMin, spinAgeMin,
                        lblDureeJeux, spinDureeJeux,
                        lblStock, spinStock,
                        btnValider, lblOutput
                );
                break;

            default:
                System.out.println("Choix inconnu");
        }
    }


    private Label createLabel(String text) {
        Label lbl = new Label(text);
        lbl.setStyle(STYLE_LABEL);
        return lbl;
    }


    private TextField createTextField(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        return tf;
    }


    private Spinner<Integer> createSpinner(int min, int max, int initial) {
        Spinner<Integer> spinner = new Spinner<>(min, max, initial);
        spinner.setEditable(true);
        StringConverter<Integer> converter = spinner.getValueFactory().getConverter();
        TextFormatter<Integer> formatter = new TextFormatter<>(converter, initial, c -> {
            if (c.getControlNewText().matches("\\d*")) {
                return c;
            }
            return null;
        });
        spinner.getEditor().setTextFormatter(formatter);

        return spinner;
    }
}