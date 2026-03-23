package com.eseo.mediastock.controller;

import com.eseo.mediastock.model.Admin;
import com.eseo.mediastock.service.AdminService;
import com.eseo.mediastock.service.UserSession;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;

import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

/**
 * Contrôleur de la vue des paramètres de l'application.
 * <p>
 * Gère les préférences de l'utilisateur ou de l'application (comme le basculement
 * de thème clair/sombre, la configuration des délais d'emprunt, etc.).
 * </p>
 */
public class ParametreController {

    private final AdminService adminService = new AdminService();

    private void styliserChamp(TextField field, String promptText) {
        field.setPromptText(promptText);
        field.getStyleClass().add("text_input");
    }

    private Label creerLabel(String texte) {
        Label label = new Label(texte);
        label.getStyleClass().add("label-or");
        return label;
    }

    private void styliserBoutonsDialog(DialogPane dialogPane, ButtonType loginButtonType) {
        Button validerButton = (Button) dialogPane.lookupButton(loginButtonType);
        if (validerButton != null) {
            validerButton.getStyleClass().add("bouton-valider");
        }

        Button annulerButton = (Button) dialogPane.lookupButton(ButtonType.CANCEL);
        if (annulerButton != null) {
            annulerButton.getStyleClass().add("bouton-annuler");
        }
    }

    private void afficherAlerte(Alert.AlertType type, String titre, String message) {
        Alert alert = new Alert(type);
        alert.setHeaderText(titre);
        alert.setContentText(message);
        alert.initStyle(StageStyle.TRANSPARENT);
        DialogPane dialogPane = alert.getDialogPane();

        try {
            String cssPath = Objects.requireNonNull(getClass().getResource("/style/alert.css")).toExternalForm();
            dialogPane.getStylesheets().add(cssPath);
        } catch (Exception ignored) {
        }

        if (dialogPane.getScene() != null && dialogPane.getScene().getWindow() != null) {
            dialogPane.getScene().setFill(Color.TRANSPARENT);
        }
        alert.showAndWait();
    }


    /**
     * Button edit param.
     *
     * @param actionEvent the action event
     */
    @FXML
    public void ButtonEditParam(ActionEvent actionEvent) {
        Admin adminActuel = UserSession.getAdminConnecte();
        if (adminActuel == null) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Aucun utilisateur n'est connecté.");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Modifier les Paramètres");
        DialogPane dialogPane = dialog.getDialogPane();

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

        username.setText(adminActuel.getNom());
        prenom.setText(adminActuel.getPrenom());
        email.setText(adminActuel.getEmail());
        tel.setText(String.valueOf(adminActuel.getNumTel()));

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
            try {

                adminService.mettreAJourAdmin(
                        adminActuel.getId(),
                        email.getText(),
                        null,
                        username.getText(),
                        prenom.getText(),
                        tel.getText()
                );

                adminActuel.setNom(username.getText());
                adminActuel.setPrenom(prenom.getText());
                adminActuel.setEmail(email.getText());
                adminActuel.setNumTel(tel.getText());

                afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Vos informations ont été mises à jour.");

            } catch (NumberFormatException e) {
                afficherAlerte(Alert.AlertType.ERROR, "Erreur de saisie", "Le numéro de téléphone doit contenir uniquement des chiffres.");
            } catch (IllegalArgumentException | SQLException e) {
                afficherAlerte(Alert.AlertType.ERROR, "Erreur de mise à jour", e.getMessage());
            }
        }
    }

    /**
     * Button edit password.
     *
     * @param actionEvent the action event
     */
    @FXML
    public void ButtonEditPassword(ActionEvent actionEvent) {
        Admin adminActuel = UserSession.getAdminConnecte();
        if (adminActuel == null) {
            afficherAlerte(Alert.AlertType.ERROR, "Erreur", "Aucun utilisateur n'est connecté.");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Modifier le mot de passe");
        DialogPane dialogPane = dialog.getDialogPane();

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


            if (!nouveauMdp.equals(confirmMdp)) {
                afficherAlerte(Alert.AlertType.WARNING, "Erreur", "Les nouveaux mots de passe ne correspondent pas.");
                return;
            }


            if (nouveauMdp.length() < 6) {
                afficherAlerte(Alert.AlertType.WARNING, "Erreur", "Le nouveau mot de passe doit faire au moins 6 caractères.");
                return;
            }


            try {
                adminService.changerMotDePasse(adminActuel.getId(), ancienMdp, nouveauMdp);
                afficherAlerte(Alert.AlertType.INFORMATION, "Succès", "Votre mot de passe a été modifié avec succès.");
            } catch (IllegalArgumentException e) {
                afficherAlerte(Alert.AlertType.ERROR, "Erreur", "L'ancien mot de passe est incorrect.");
            } catch (SQLException e) {
                afficherAlerte(Alert.AlertType.ERROR, "Erreur système", "Impossible de mettre à jour le mot de passe.");
            }
        }
    }
}