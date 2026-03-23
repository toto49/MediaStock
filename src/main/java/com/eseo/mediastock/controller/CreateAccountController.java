package com.eseo.mediastock.controller;

import com.eseo.mediastock.HelloApplication;
import com.eseo.mediastock.Launcher;
import com.eseo.mediastock.service.AdherentService;
import com.eseo.mediastock.service.AdminService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

/**
 * The type Create account controller.
 */
public class CreateAccountController {

    private final AdherentService adherentService = new AdherentService();
    /**
     * The Field nom co.
     */
    public TextField field_nom_co;
    /**
     * The Field prenom co.
     */
    public TextField field_prenom_co;
    /**
     * The Field email co.
     */
    public TextField field_email_co;
    /**
     * The Field telephone co.
     */
    public TextField field_telephone_co;
    /**
     * The Field password co.
     */
    public PasswordField field_password_co;
    /**
     * The Field copassword co.
     */
    public PasswordField field_copassword_co;
    @FXML
    private Label error_label;

    /**
     * Buttonreturnco.
     *
     * @param actionEvent the action event
     * @throws IOException the io exception
     */
// bouton retour de tom
    public void Buttonreturnco(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                Launcher.class.getResource("view/bienvenue-view.fxml"));
        Parent root = loader.load();

        HelloApplication.changerPageGlobale(root, "Bienvenue");
    }


    /**
     * Buttonsend.
     *
     * @param actionEvent the action event
     * @throws IOException the io exception
     */
    public void Buttonsend(ActionEvent actionEvent) throws IOException {
        if (field_prenom_co.getText().isEmpty() || field_telephone_co.getText().isEmpty() || field_email_co.getText().isEmpty() || field_nom_co.getText().isEmpty() || field_copassword_co.getText().isEmpty() || field_password_co.getText().isEmpty()) {
            showErrorAlert("Erreur", "Veuillez remplir tous les champs ");
            return;
        }

        if (!field_password_co.getText().equals(field_copassword_co.getText())) {
            showErrorAlert("Erreur", "Les mots de passe ne correspondent pas ");
            return;
        }
        if (field_password_co.getText().length() < 6) {
            showErrorAlert("Erreur", "Le mot de passe doit contenir au moins 6 caractères.");
            return;
        }
        if (!field_email_co.getText().contains("@")) {
            showErrorAlert("Erreur", "Format d'email invalide !");
            return;
        }
        if (!field_telephone_co.getText().matches("\\d{10}")) {
            showErrorAlert("Erreur", "Le numéro de téléphone doit contenir exactement 10 chiffres !");
            return;
        }
        try {

            AdminService.creerAdmin(
                    field_email_co.getText(),
                    field_password_co.getText(),
                    field_nom_co.getText(),
                    field_prenom_co.getText(),
                    field_telephone_co.getText()
            );
            showSuccessAlert("Succès",
                    "Compte créé avec succès !\n" +
                            "Vous pouvez maintenant vous connecter.");

            redirectToBienvenue(actionEvent);

        } catch (IllegalArgumentException e) {
            showErrorAlert("Erreur", e.getMessage());
        } catch (Exception e) {
            showErrorAlert("Erreur technique",
                    "Une erreur est survenue lors de la création du compte.\n" +
                            "Veuillez réessayer plus tard.");
            e.printStackTrace();
        }
    }

    private void showErrorAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        stylizeAlert(alert);
        alert.showAndWait();
    }

    private void showSuccessAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        stylizeAlert(alert);
        alert.showAndWait();
    }

    private void stylizeAlert(Alert alert) {
        alert.initStyle(StageStyle.TRANSPARENT);
        DialogPane dialogPane = alert.getDialogPane();
        String cssPath = Objects.requireNonNull(getClass().getResource("/style/alert.css")).toExternalForm();
        dialogPane.getStylesheets().add(cssPath);
        Stage stage = (Stage) dialogPane.getScene().getWindow();
        stage.getScene().setFill(Color.TRANSPARENT);
    }

    private void redirectToBienvenue(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                Launcher.class.getResource("view/bienvenue-view.fxml"));
        Parent root = loader.load();
        HelloApplication.changerPageGlobale(root, "Bienvenue");
    }
}