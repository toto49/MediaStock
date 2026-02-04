package com.eseo.mediastock;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

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
        stage.setScene(scene);
        stage.show();
    }
}