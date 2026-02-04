package com.eseo.mediastock.controller;

import com.eseo.mediastock.Launcher;
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
    public TextField field_adresse_co;
    public PasswordField field_password_co;
    public PasswordField field_copassword_co;
    @FXML
    private Label error_label;


    public void Buttonreturnco(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                Launcher.class.getResource("view/bienvenue-view.fxml"));
        Parent root = loader.load();


        Scene currentScene = ((Node) actionEvent.getSource()).getScene();


        currentScene.setRoot(root);

    }

    public void Buttonsend(ActionEvent actionEvent) throws IOException {
        if (field_prenom_co.getText().isEmpty() || field_adresse_co.getText().isEmpty() || field_email_co.getText().isEmpty() || field_nom_co.getText().isEmpty() || field_copassword_co.getText().isEmpty() || field_password_co.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Erreur");
            alert.setContentText("Veuillez remplir tous les champs !");
            alert.initStyle(StageStyle.TRANSPARENT);
            DialogPane dialogPane = alert.getDialogPane();
            String cssPath = Objects.requireNonNull(getClass().getResource("/style/alert.css")).toExternalForm();
            dialogPane.getStylesheets().add(cssPath);
            Stage stage = (Stage) dialogPane.getScene().getWindow();
            stage.getScene().setFill(Color.TRANSPARENT);
            alert.showAndWait();
        }

    }
}
