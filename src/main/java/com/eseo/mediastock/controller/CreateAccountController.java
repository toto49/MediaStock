package com.eseo.mediastock.controller;

import com.eseo.mediastock.Launcher;
import com.eseo.mediastock.service.AdherentService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class CreateAccountController {

    public TextField field_nom_co;
    public TextField field_prenom_co;
    public TextField field_email_co;
    public TextField field_telephone_co;
    public PasswordField field_password_co;
    public PasswordField field_copassword_co;
    @FXML
    private Label error_label;
//service
    private AdherentService adherentService = new AdherentService();
//bouton retour
    public void Buttonreturnco(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                Launcher.class.getResource("view/bienvenue-view.fxml"));
        Parent root = loader.load();


        Scene currentScene = ((Node) actionEvent.getSource()).getScene();


        currentScene.setRoot(root);

    }
//bouton envoyer incription
    public void Buttonsend(ActionEvent actionEvent) throws IOException {
        if (field_prenom_co.getText().isEmpty() || field_telephone_co.getText().isEmpty() || field_email_co.getText().isEmpty() || field_nom_co.getText().isEmpty() || field_copassword_co.getText().isEmpty() || field_password_co.getText().isEmpty()) {
            showErrorAlert("Erreur", "Veuillez remplir tous les champs ");
            return;
        }

        // verif mdp
        if (!field_password_co.getText().equals(field_copassword_co.getText())) {
            showErrorAlert("Erreur", "Les mots de passe ne correspondent pas ");
            return;
        }

        // verif mail
        if (!field_email_co.getText().contains("@")) {
            showErrorAlert("Erreur", "Format d'email invalide !");
            return;
        }

        //appel service inscire adherent
        try {
            adherentService.inscrireAdherent(
                    field_nom_co.getText(),
                    field_prenom_co.getText(),
                    field_email_co.getText(),
                    field_telephone_co.getText()
            );
            showSuccessAlert("Succès",
                    "Compte créé avec succès !\n" +
                            "Vous pouvez maintenant vous connecter.");

            //redirection
            redirectToBienvenue(actionEvent);

        } catch (Exception e) {
            showErrorAlert("Erreur technique",
                    "Une erreur est survenue lors de la création du compte.\n" +
                            "Veuillez réessayer plus tard.\n\n" +
                            "Détail: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // MÉTHODES UTILITAIRES (inchangées)
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
        Scene currentScene = ((Node) actionEvent.getSource()).getScene();
        currentScene.setRoot(root);
    }
}
