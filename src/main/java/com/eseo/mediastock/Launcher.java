package com.eseo.mediastock;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class Launcher extends Application {

    static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                Launcher.class.getResource("view/bienvenue-view.fxml")
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