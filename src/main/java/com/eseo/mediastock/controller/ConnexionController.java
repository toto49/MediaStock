package com.eseo.mediastock.controller;

import com.eseo.mediastock.Launcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ConnexionController {
    public TextField field_mail_co;
    public PasswordField field_password_co;

    public void Buttonreturnco(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                Launcher.class.getResource("view/bienvenue-view.fxml")
        );
        Scene scene = new Scene(loader.load());

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }

    public void Buttonsend(ActionEvent actionEvent) {
        if (field_mail_co.getText().isEmpty() || field_password_co.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur");
            alert.showAndWait();


        }
    }
}
