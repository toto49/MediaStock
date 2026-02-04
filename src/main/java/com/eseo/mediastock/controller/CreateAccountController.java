package com.eseo.mediastock.controller;

import com.eseo.mediastock.Launcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

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
                Launcher.class.getResource("view/bienvenue-view.fxml")
        );
        Scene scene = new Scene(loader.load());

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }

    public void Buttonsend(ActionEvent actionEvent) throws IOException {
        if (field_prenom_co.getText().isEmpty() || field_adresse_co.getText().isEmpty() || field_email_co.getText().isEmpty() || field_nom_co.getText().isEmpty() || field_copassword_co.getText().isEmpty() || field_password_co.getText().isEmpty()) {
            System.out.println("atom");
            error_label.setText("erreur");
        }

    }
}
