package com.eseo.mediastock.controller;

import com.eseo.mediastock.service.StockService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class AjouterProduitController implements Initializable {

    @FXML
    private VBox containerFormulaire;
    @FXML
    private ComboBox<String> ChoiceAddProduit;

    private final String STYLE_LABEL = "-fx-font-size: 15px; -fx-text-fill: #ffcc00;";

    // Ajout de l'instance du service
    private StockService stockService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialisation du service
        stockService = new StockService();

        ChoiceAddProduit.valueProperty().addListener((observable, oldValue, newValue) -> {
            detecterSelection(newValue);
        });
    }

    private void detecterSelection(String choix) {
        if (choix == null) return;

        containerFormulaire.getChildren().clear();

        // Champs communs
        Label lblTitre = createLabel("Titre :");
        TextField txtTitre = createTextField("Ex: Titre du produit");

        Label lblDesc = createLabel("Description :");
        TextField txtDesc = createTextField("Ex: Description du produit");

        Label lblEditeur = createLabel("Editeur :");
        TextField txtEditeur = createTextField("Ex: Nintendo / Asmodee / Warner Bros");

        Label lblStock = createLabel("Nombre d'exemplaires (Stock) :");
        Spinner<Integer> spinStock = createSpinner(0, 1000, 1);

        int anneeActuelle = LocalDate.now().getYear();
        Label lblAnnee = createLabel("Année de sortie :");
        Spinner<Integer> spinAnnee = createSpinner(1900, anneeActuelle, anneeActuelle);

        Button btnValider = new Button();
        btnValider.getStyleClass().add("bouton-valider");

        Label lblOutput = new Label();
        lblOutput.setStyle("-fx-text-fill: white; -fx-padding: 10 0 0 0;");

        switch (choix) {
            case "LIVRES":
                btnValider.setText("Ajouter le Livre");

                Label lblAuteur = createLabel("Auteur :");
                TextField txtAuteur = createTextField("Ex: Victor Hugo");

                Label lblPages = createLabel("Nombre de pages :");
                Spinner<Integer> spinPages = createSpinner(1, 5000, 200);

                Label lblIsbn = createLabel("ISBN :");
                TextField txtIsbn = createTextField("Ex: 9782070409228");

                Label lblFormat = createLabel("Format :");
                TextField txtFormat = createTextField("Ex: Poche, Broché...");

                btnValider.setOnAction(event -> {
                    try {
                        // On utilise le StockService au lieu de créer l'objet Livre et d'appeler le DAO
                        stockService.ajouterLivre(
                                txtTitre.getText(),
                                txtDesc.getText(),
                                txtEditeur.getText(),
                                spinAnnee.getValue(),
                                txtIsbn.getText(),
                                txtAuteur.getText(),
                                spinPages.getValue(),
                                txtFormat.getText(),
                        );

                        // TODO: Gérer l'ajout du stock (exemplaires) via spinStock.getValue() plus tard

                        afficherMessage(lblOutput, "Le Livre a été ajouté avec succès !", true);
                        viderChamps(txtTitre, txtDesc, txtEditeur, txtAuteur, txtIsbn, txtFormat);
                    } catch (IllegalArgumentException e) {
                        afficherMessage(lblOutput, "Erreur : " + e.getMessage(), false);
                    } catch (Exception e) {
                        afficherMessage(lblOutput, "Erreur inattendue : " + e.getMessage(), false);
                    }
                });

                containerFormulaire.getChildren().addAll(
                        lblTitre, txtTitre, lblDesc, txtDesc, lblEditeur, txtEditeur,
                        lblAuteur, txtAuteur, lblIsbn, txtIsbn, lblFormat, txtFormat,
                        lblAnnee, spinAnnee, lblPages, spinPages, lblStock, spinStock,
                        btnValider, lblOutput
                );
                break;

            case "DVD":
                btnValider.setText("Ajouter le DVD");

                Label lblReal = createLabel("Réalisateur :");
                TextField txtReal = createTextField("Ex: Christopher Nolan");

                Label lblDuree = createLabel("Durée (minutes) :");
                Spinner<Integer> spinDuree = createSpinner(1, 500, 120);

                Label lblAudio = createLabel("Pistes Audio :");
                TextField txtAudio = createTextField("Ex: Français, Anglais");

                Label lblSousTitres = createLabel("Sous-titres :");
                TextField txtSousTitres = createTextField("Ex: Français, Espagnol");

                btnValider.setOnAction(event -> {
                    try {
                        List<String> pistesAudio = Arrays.asList(txtAudio.getText().split("\\s*,\\s*"));
                        List<String> sousTitres = Arrays.asList(txtSousTitres.getText().split("\\s*,\\s*"));

                        // Appel au service
                        stockService.ajouterDVD(
                                txtTitre.getText(),
                                txtDesc.getText(),
                                txtEditeur.getText(),
                                spinAnnee.getValue(),
                                txtReal.getText(),
                                spinDuree.getValue(),
                                pistesAudio,
                                sousTitres,
                        );

                        // TODO: Gérer l'ajout du stock (exemplaires) via spinStock.getValue() plus tard

                        afficherMessage(lblOutput, "Le DVD a été ajouté avec succès !", true);
                        viderChamps(txtTitre, txtDesc, txtEditeur, txtReal, txtAudio, txtSousTitres);
                    } catch (IllegalArgumentException e) {
                        afficherMessage(lblOutput, "Erreur : " + e.getMessage(), false);
                    } catch (Exception e) {
                        afficherMessage(lblOutput, "Erreur inattendue : " + e.getMessage(), false);
                    }
                });

                containerFormulaire.getChildren().addAll(
                        lblTitre, txtTitre, lblDesc, txtDesc, lblEditeur, txtEditeur,
                        lblReal, txtReal, lblAudio, txtAudio, lblSousTitres, txtSousTitres,
                        lblAnnee, spinAnnee, lblDuree, spinDuree, lblStock, spinStock,
                        btnValider, lblOutput
                );
                break;

            case "JEUX":
                btnValider.setText("Ajouter le Jeu");

                Label lblJMin = createLabel("Joueurs Minimum :");
                Spinner<Integer> spinJMin = createSpinner(1, 100, 1);

                Label lblJMax = createLabel("Joueurs Maximum :");
                Spinner<Integer> spinJMax = createSpinner(1, 100, 4);

                Label lblAgeMin = createLabel("Age Minimum :");
                Spinner<Integer> spinAgeMin = createSpinner(3, 18, 12);

                Label lblDureeJeux = createLabel("Durée moyenne (min) :");
                Spinner<Integer> spinDureeJeux = createSpinner(5, 600, 30);

                btnValider.setOnAction(event -> {
                    try {
                        // Appel au service
                        stockService.ajouterJeuSociete(
                                txtTitre.getText(),
                                txtDesc.getText(),
                                txtEditeur.getText(),
                                spinAnnee.getValue(),
                                spinJMin.getValue(),
                                spinJMax.getValue(),
                                spinAgeMin.getValue(),
                                spinDureeJeux.getValue(),
                        );

                        // TODO: Gérer l'ajout du stock (exemplaires) via spinStock.getValue() plus tard

                        afficherMessage(lblOutput, "Le Jeu a été ajouté avec succès !", true);
                        viderChamps(txtTitre, txtDesc, txtEditeur);
                    } catch (IllegalArgumentException e) {
                        afficherMessage(lblOutput, "Erreur : " + e.getMessage(), false);
                    } catch (Exception e) {
                        afficherMessage(lblOutput, "Erreur inattendue : " + e.getMessage(), false);
                    }
                });

                containerFormulaire.getChildren().addAll(
                        lblTitre, txtTitre, lblDesc, txtDesc, lblEditeur, txtEditeur,
                        lblAnnee, spinAnnee, lblJMin, spinJMin, lblJMax, spinJMax,
                        lblAgeMin, spinAgeMin, lblDureeJeux, spinDureeJeux, lblStock, spinStock,
                        btnValider, lblOutput
                );
                break;
        }
    }

    // --- Méthodes utilitaires ---

    private void afficherMessage(Label label, String message, boolean success) {
        label.setText(message);
        if (success) {
            label.setStyle("-fx-text-fill: #2ecc71; -fx-padding: 10 0 0 0; -fx-font-weight: bold;");
        } else {
            label.setStyle("-fx-text-fill: #e74c3c; -fx-padding: 10 0 0 0; -fx-font-weight: bold;");
        }
    }

    private void viderChamps(TextField... champs) {
        for (TextField champ : champs) {
            champ.clear();
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
        styliserChamp(tf);
        return tf;
    }

    private Spinner<Integer> createSpinner(int min, int max, int initial) {
        Spinner<Integer> spinner = new Spinner<>(min, max, initial);
        spinner.setEditable(true);
        StringConverter<Integer> converter = spinner.getValueFactory().getConverter();
        TextFormatter<Integer> formatter = new TextFormatter<>(converter, initial, c -> {
            if (c.getControlNewText().matches("\\d*")) return c;
            return null;
        });
        spinner.getEditor().setTextFormatter(formatter);
        styliserSpinner(spinner);
        return spinner;
    }

    private void styliserChamp(TextField field) {
        String styleNormal = "-fx-background-color: #383838; -fx-text-fill: white; -fx-background-radius: 10; -fx-border-color: #555; -fx-border-radius: 10; -fx-border-width: 1; -fx-padding: 8 12;";
        String styleFocus = "-fx-background-color: #383838; -fx-text-fill: white; -fx-background-radius: 10; -fx-border-color: #ffcc00; -fx-border-radius: 10; -fx-border-width: 2; -fx-padding: 7 11;";
        field.setStyle(styleNormal);
        field.focusedProperty().addListener((obs, oldVal, newVal) -> field.setStyle(newVal ? styleFocus : styleNormal));
    }

    private void styliserSpinner(Spinner<?> spinner) {
        TextField editor = spinner.getEditor();
        editor.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        String styleNormal = "-fx-background-color: #383838; -fx-text-fill: white; -fx-background-radius: 10; -fx-border-color: #555; -fx-border-radius: 10; -fx-border-width: 1; -fx-padding: 2 5;";
        String styleFocus = "-fx-background-color: #383838; -fx-text-fill: white; -fx-background-radius: 10; -fx-border-color: #ffcc00; -fx-border-radius: 10; -fx-border-width: 2; -fx-padding: 1 4;";
        spinner.setStyle(styleNormal);
        editor.focusedProperty().addListener((obs, oldVal, newVal) -> spinner.setStyle(newVal ? styleFocus : styleNormal));
    }
}