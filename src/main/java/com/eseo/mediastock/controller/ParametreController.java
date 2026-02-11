package com.eseo.mediastock.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Optional;

public class ParametreController {

    // Méthode utilitaire pour styliser les champs de texte
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

    // Méthode utilitaire pour styliser les boutons (pour éviter la duplication de code)
    private void styliserBoutonsDialog(DialogPane dialogPane, ButtonType loginButtonType) {
        // --- Bouton Valider ---
        Button validerButton = (Button) dialogPane.lookupButton(loginButtonType);
        if (validerButton != null) {
            validerButton.setStyle(
                    "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-padding: 8 16;"
            );
            validerButton.setOnMouseEntered(ev -> validerButton.setStyle("-fx-background-color: #45a049; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-padding: 8 16;"));
            validerButton.setOnMouseExited(ev -> validerButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-padding: 8 16;"));
        }

        // --- Bouton Annuler ---
        Button annulerButton = (Button) dialogPane.lookupButton(ButtonType.CANCEL);
        if (annulerButton != null) {
            annulerButton.setStyle(
                    "-fx-background-color: #b33a3a; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-padding: 8 16;"
            );
            annulerButton.setOnMouseEntered(ev -> annulerButton.setStyle("-fx-background-color: #a83232; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-padding: 8 16;"));
            annulerButton.setOnMouseExited(ev -> annulerButton.setStyle("-fx-background-color: #b33a3a; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-padding: 8 16;"));
        }
    }

    @FXML
    public void ButtonEditParam(ActionEvent actionEvent) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Modifier les Paramètres");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #2b2b2b;");

        ButtonType loginButtonType = new ButtonType("Valider", ButtonBar.ButtonData.OK_DONE);
        dialogPane.getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Ajout du style aux boutons (optionnel, pour la cohérence avec l'autre fenêtre)
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

        Label lblUser = new Label("Nom d'utilisateur :");
        lblUser.setStyle("-fx-text-fill: #ffcc00;");
        Label lb_prenom_user = new Label("Prénom d'utilisateur :");
        lb_prenom_user.setStyle("-fx-text-fill: #ffcc00;");
        Label lblEmail = new Label("Email :");
        lblEmail.setStyle("-fx-text-fill: #ffcc00;");
        Label tel_user = new Label("Tel :");
        tel_user.setStyle("-fx-text-fill: #ffcc00;");

        grid.add(lblUser, 0, 0);
        grid.add(username, 1, 0);
        grid.add(lb_prenom_user, 0, 1);
        grid.add(prenom, 1, 1);
        grid.add(lblEmail, 0, 2);
        grid.add(email, 1, 2);
        grid.add(tel_user, 0, 3);
        grid.add(tel, 1, 3);

        dialogPane.setContent(grid);
        Platform.runLater(username::requestFocus);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == loginButtonType) {
            // CORRECTION ICI : Récupérer le texte des TextFields, pas des Labels
            String nomSaisi = username.getText();
            String emailSaisi = email.getText();
            String prenomSaisi = prenom.getText(); // Corrigé (était lb_prenom_user.getText())
            String telSaisi = tel.getText();       // Corrigé (était tel_user.getText())

            // TODO: Ajouter ici la logique de sauvegarde (ex: appel à un Service ou DAO)
            System.out.println("Sauvegarde : " + nomSaisi + ", " + prenomSaisi);
        }
    }

    @FXML
    public void ButtonEditPassword(ActionEvent actionEvent) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Modifier le mot de passe");
        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #2b2b2b;");

        ButtonType loginButtonType = new ButtonType("Valider", ButtonBar.ButtonData.OK_DONE);
        dialogPane.getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Appel de la méthode de style refactorisée
        dialog.setOnShown(e -> styliserBoutonsDialog(dialogPane, loginButtonType));

        // CORRECTION : Suppression du deuxième 'dialogPane.getButtonTypes().addAll(...)' qui était ici

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

        Label password_user = new Label("Ancien mot de passe : ");
        password_user.setStyle("-fx-text-fill: #ffcc00;");
        Label new_password_user = new Label("Nouveau mot de passe : ");
        new_password_user.setStyle("-fx-text-fill: #ffcc00;");
        Label confirm_password_user = new Label("Confirmer mot de passe :");
        confirm_password_user.setStyle("-fx-text-fill: #ffcc00;");

        grid.add(password_user, 0, 0);
        grid.add(password, 1, 0);
        grid.add(new_password_user, 0, 1);
        grid.add(new_password, 1, 1);
        grid.add(confirm_password_user, 0, 2);
        grid.add(confirm_password, 1, 2);

        dialogPane.setContent(grid);
        Platform.runLater(password::requestFocus);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == loginButtonType) {
            // CORRECTION : Renommage des variables pour la clarté
            String ancienMdp = password.getText();
            String nouveauMdp = new_password.getText();
            String confirmMdp = confirm_password.getText();

            if (nouveauMdp.equals(confirmMdp)) {
                // TODO: Logique de changement de mot de passe
                System.out.println("Mot de passe changé !");
            } else {
                // Gestion d'erreur si les mots de passe ne correspondent pas
                System.out.println("Les mots de passe ne correspondent pas.");
            }
        }
    }
}