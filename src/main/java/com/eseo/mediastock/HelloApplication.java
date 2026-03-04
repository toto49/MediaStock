package com.eseo.mediastock;

import atlantafx.base.theme.PrimerDark;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

// 1. On l'appelle HelloApplication (et elle garde le extends Application)
public class HelloApplication extends Application {

    // 3. Il faut ajouter cette méthode pour lancer JavaFX
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());

        // 2. Attention : changer Launcher.class en HelloApplication.class ici
        FXMLLoader fxmlLoader = new FXMLLoader(
                HelloApplication.class.getResource("view/bienvenue-view.fxml")
        );

        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Bienvenue sur MediaStock");
        stage.initStyle(StageStyle.DECORATED);
        stage.setMaximized(true);
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logo.png")));
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
    }
}