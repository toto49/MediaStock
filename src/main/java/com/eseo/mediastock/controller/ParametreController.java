package com.eseo.mediastock.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Objects;
import java.util.Optional;

public class ParametreController {

    // Méthode utilitaire pour styliser les champs de texte
    private void styliserChamp(TextField field, String promptText) {
        field.setPromptText(promptText);
        field.getStyleClass().add("text_input");
    }

    // Méthode utilitaire pour créer et styliser les labels (nouveau !)
    private Label creerLabel(String texte) {
        Label label = new Label(texte);
        label.getStyleClass().add("label-or"); // On utilise le CSS au lieu du style en ligne
        return label;
    }

    // Méthode utilitaire pour styliser les boutons avec le CSS
    private void styliserBoutonsDialog(DialogPane dialogPane, ButtonType loginButtonType) {
        // --- Bouton Valider ---
        Button validerButton = (Button) dialogPane.lookupButton(loginButtonType);
        if (validerButton != null) {
            validerButton.getStyleClass().add("bouton-valider");
        }

        // --- Bouton Annuler ---
        Button annulerButton = (Button) dialogPane.lookupButton(ButtonType.CANCEL);
        if (annulerButton != null) {
            annulerButton.getStyleClass().add("bouton-annuler");
        }
    }

    @FXML
    public void ButtonEditParam(ActionEvent actionEvent) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Modifier les Paramètres");
        DialogPane dialogPane = dialog.getDialogPane();

        // On lie le CSS et on met le fond
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style/button.css")).toExternalForm());
        dialogPane.setStyle("-fx-background-color: #2b2b2b;");

        ButtonType loginButtonType = new ButtonType("Valider", ButtonBar.ButtonData.OK_DONE);
        dialogPane.getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        dialog.setOnShown(e -> styliserBoutonsDialog(dialogPane, loginButtonType));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        grid.setStyle("-fx-background-color: #2b2b2b;");

        TextField username = new TextField();
        TextField prenom = new TextField();
        TextField email = new TextField();
        TextField tel = new TextField();

        styliserChamp(username, "Votre nom");
        styliserChamp(prenom, "Votre prénom");
        styliserChamp(email, "Votre email");
        styliserChamp(tel, "Votre téléphone");

        grid.add(creerLabel("Nom d'utilisateur :"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(creerLabel("Prénom d'utilisateur :"), 0, 1);
        grid.add(prenom, 1, 1);
        grid.add(creerLabel("Email :"), 0, 2);
        grid.add(email, 1, 2);
        grid.add(creerLabel("Tel :"), 0, 3);
        grid.add(tel, 1, 3);

        dialogPane.setContent(grid);
        Platform.runLater(username::requestFocus);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == loginButtonType) {
            String nomSaisi = username.getText();
            String emailSaisi = email.getText();
            String prenomSaisi = prenom.getText();
            String telSaisi = tel.getText();

            System.out.println("Sauvegarde : " + nomSaisi + ", " + prenomSaisi);
        }
    }

    @FXML
    public void ButtonEditPassword(ActionEvent actionEvent) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Modifier le mot de passe");
        DialogPane dialogPane = dialog.getDialogPane();

        // IMPORTANT : Liaison du CSS pour la deuxième popup
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style/button.css")).toExternalForm());
        dialogPane.setStyle("-fx-background-color: #2b2b2b;");

        ButtonType loginButtonType = new ButtonType("Valider", ButtonBar.ButtonData.OK_DONE);
        dialogPane.getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        dialog.setOnShown(e -> styliserBoutonsDialog(dialogPane, loginButtonType));

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        grid.setStyle("-fx-background-color: #2b2b2b;");

        PasswordField password = new PasswordField();
        PasswordField new_password = new PasswordField();
        PasswordField confirm_password = new PasswordField();

        styliserChamp(password, "Ancien mot de passe");
        styliserChamp(new_password, "Nouveau mot de passe");
        styliserChamp(confirm_password, "Confirmer le mot de passe");

        grid.add(creerLabel("Ancien mot de passe : "), 0, 0);
        grid.add(password, 1, 0);
        grid.add(creerLabel("Nouveau mot de passe : "), 0, 1);
        grid.add(new_password, 1, 1);
        grid.add(creerLabel("Confirmer mot de passe :"), 0, 2);
        grid.add(confirm_password, 1, 2);

        dialogPane.setContent(grid);
        Platform.runLater(password::requestFocus);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == loginButtonType) {
            String ancienMdp = password.getText();
            String nouveauMdp = new_password.getText();
            String confirmMdp = confirm_password.getText();

            if (nouveauMdp.equals(confirmMdp)) {
                System.out.println("Mot de passe changé !");
            } else {
                System.out.println("Les mots de passe ne correspondent pas.");
            }
        }
    }
}