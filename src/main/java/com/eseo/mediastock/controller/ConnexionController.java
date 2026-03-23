package com.eseo.mediastock.controller;

import com.eseo.mediastock.HelloApplication;
import com.eseo.mediastock.Launcher;
import com.eseo.mediastock.model.Admin;
import com.eseo.mediastock.service.AdminService;
import com.eseo.mediastock.service.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

/**
 * Contrôleur de la vue d'authentification des administrateurs.
 * <p>
 * Récupère les identifiants saisis par l'utilisateur, demande la vérification
 * au service d'authentification et, en cas de succès, initialise la session globale
 * de l'application avant de rediriger vers l'interface principale.
 * </p>
 */
public class ConnexionController {
    /**
     * The Field mail co.
     */
    @FXML
    public TextField field_mail_co;
    /**
     * The Field password co.
     */
    @FXML
    public PasswordField field_password_co;

    /**
     * Initialize.
     */
    @FXML
    public void initialize() {
        field_password_co.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    lancerConnexion();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    /**
     * Buttonreturnco.
     *
     * @param actionEvent the action event
     * @throws IOException the io exception
     */
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
        lancerConnexion();
    }

    private void lancerConnexion() throws IOException {
        if (field_mail_co.getText().isEmpty() || field_password_co.getText().isEmpty()) {
            afficherAlerte(Alert.AlertType.WARNING, "Erreur", "Veuillez remplir tous les champs !");
        } else {
            String email = field_mail_co.getText();
            String password = field_password_co.getText();

            AdminService adminService = new AdminService();
            Admin adminConnecte = adminService.login(email, password);
            if (adminConnecte != null) {
                UserSession.setAdminConnecte(adminConnecte);
                FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("view/menu-view.fxml"));
                Parent root = fxmlLoader.load();
                HelloApplication.changerPageGlobale(root, "Menu Principal");
            } else {
                afficherAlerte(Alert.AlertType.ERROR, "Erreur d'authentification", "Email ou mot de passe incorrect.");
            }
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
        } catch (NullPointerException e) {
            System.err.println("Attention : Le fichier CSS pour l'alerte est introuvable.");
        }

        Stage stage = (Stage) dialogPane.getScene().getWindow();
        stage.getScene().setFill(Color.TRANSPARENT);
        alert.showAndWait();
    }
}