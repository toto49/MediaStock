package com.eseo.mediastock.controller;

import com.eseo.mediastock.Launcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class ConnexionController {
    public TextField field_mail_co;
    public PasswordField field_password_co;

    public void Buttonreturnco(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                Launcher.class.getResource("view/bienvenue-view.fxml"));
        Parent root = loader.load();
        Scene currentScene = ((Node) actionEvent.getSource()).getScene();
        currentScene.setRoot(root);

    }

    public void Buttonsend(ActionEvent actionEvent) {
        if (field_mail_co.getText().isEmpty() || field_password_co.getText().isEmpty()) {
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
