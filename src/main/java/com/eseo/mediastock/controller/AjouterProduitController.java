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
    private void styliserSpinner(Spinner<?> spinner, String promptText) {

        TextField editor = spinner.getEditor();
        editor.setPromptText(promptText);
        editor.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        String styleNormal = "-fx-background-color: #383838; " +
                "-fx-text-fill: white; " +
                "-fx-background-radius: 10; " +
                "-fx-border-color: #555; " +
                "-fx-border-radius: 10; " +
                "-fx-border-width: 1; " +
                "-fx-padding: 2 5;";

        String styleFocus = "-fx-background-color: #383838; " +
                "-fx-text-fill: white; " +
                "-fx-background-radius: 10; " +
                "-fx-border-color: #ffcc00; " +
                "-fx-border-radius: 10; " +
                "-fx-border-width: 2; " +
                "-fx-padding: 1 4;";
        spinner.setStyle(styleNormal);
        editor.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                spinner.setStyle(styleFocus);
            } else {
                spinner.setStyle(styleNormal);
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
        Spinner<Integer> spinStock = new Spinner<>(0, 1000, 1); // Min 0, Max 1000, Défaut 1
        spinStock.setEditable(true);
        styliserSpinner(spinStock, "Ex: Stock du produit");

        Button btnValider = new Button();
        btnValider.getStyleClass().add("buttonvalid"); // Votre classe CSS
        Label lblOutput = new Label();
        lblOutput.setStyle("-fx-text-fill: white; -fx-padding: 10 0 0 0;");

        switch (choix) {
            case "LIVRES":
                btnValider.setText("Ajouter le Livre");


                // 1. Auteur
                Label lblAuteur = createLabel("Auteur :");
                TextField txtAuteur = new TextField();
                styliserChamp(txtAuteur, "Ex: Victor Hugo");

                // 2. Année (Spinner)
                Label lblAnnee = createLabel("Année de parution :");
                // Constructeur : min, max, valeur initiale
                Spinner<Integer> spinAnnee = new Spinner<>(1000, 2030, 2020);
                spinAnnee.setEditable(true); // Permet d'écrire dedans
                styliserSpinner(spinAnnee, "Année");

                // 3. Pages (Spinner)
                Label lblPages = createLabel("Nombre de pages :");
                // Constructeur : min, max, valeur initiale
                Spinner<Integer> spinPages = new Spinner<>(1, 5000, 200);
                spinPages.setEditable(true);
                styliserSpinner(spinPages, "Nb Pages");

                // 4. ISBN
                Label lblIsbn = createLabel("ISBN :");
                TextField txtIsbn = new TextField();
                styliserChamp(txtIsbn, "Ex: 978-2070409228");

                // 5. Format
                Label lblFormat = createLabel("Format :");
                TextField txtFormat = new TextField();
                styliserChamp(txtFormat, "Ex: Poche, Broché...");


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
                btnValider.setText("Ajouter le DVD");

                // --- Champs Spécifiques DVD ---
                Label lblReal = createLabel("Réalisateur :");
                TextField txtReal = new TextField();
                styliserChamp(txtReal, "Ex: Christopher Nolan");

                Label lblAnneeDvd = createLabel("Année de sortie :");
                Spinner<Integer> spinAnneeDvd = new Spinner<>(1900, 2030, 2010);
                spinAnneeDvd.setEditable(true);
                styliserSpinner(spinAnneeDvd, "Année");

                Label lblDuree = createLabel("Durée (minutes) :");
                Spinner<Integer> spinDuree = new Spinner<>(1, 500, 120);
                spinDuree.setEditable(true);
                styliserSpinner(spinDuree, "Duree");

                Label lblAudio = createLabel("Pistes Audio :");
                TextField txtAudio = new TextField();
                styliserChamp(txtAudio, "Ex: Français, Anglais 5.1");

                Label lblSousTitres = createLabel("Sous-titres :");
                TextField txtSousTitres = new TextField();
                styliserChamp(txtSousTitres, "Ex: Français, Espagnol");


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
                btnValider.setText("Ajouter le Jeu");

                // --- Champs Spécifiques JEUX ---

                Label lblAnneeJeux = createLabel("Année de sortie :");
                Spinner<Integer> spinAnneeJeux = new Spinner<>(1980, 2030, 2024);
                spinAnneeJeux.setEditable(true);
                styliserSpinner(spinAnneeJeux, "Année");

                Label lblJMin = createLabel("Joueurs Minimum :");
                Spinner<Integer> spinJMin = new Spinner<>(1, 100, 1);
                spinJMin.setEditable(true);
                styliserSpinner(spinJMin, "Joueurs Minimum");

                Label lblJMax = createLabel("Joueurs Maximum :");
                Spinner<Integer> spinJMax = new Spinner<>(1, 100, 4);
                spinJMax.setEditable(true);
                styliserSpinner(spinJMax, "Joueurs Maximum");

                Label lblAgeMin = createLabel("Age Minimum :");
                Spinner<Integer> spinAgeMin = new Spinner<>(3, 18, 12);
                spinAgeMin.setEditable(true);
                styliserSpinner(spinAgeMin, "Age Minimum ");

                Label lblDureeJeux = createLabel("Durée moyenne (min) :");
                Spinner<Integer> spinDureeJeux = new Spinner<>(5, 600, 30);
                spinDureeJeux.setEditable(true);
                styliserSpinner(spinDureeJeux, "Duree moyenne (min) ");

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