package com.eseo.mediastock;

import atlantafx.base.theme.PrimerDark;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {

    private static Label lblTitreHeader;
    private static Stage mainStage;
    private static VBox rootGlobal;
    private double xOffset = 0;
    private double yOffset = 0;
    private boolean isMaximized = false;
    private double savedX, savedY, savedWidth, savedHeight;

    public static void main(String[] args) {
        launch(args);
    }

    public static void changerTitreGlobal(String nouveauTitre) {
        String titreComplet = "MediaStock - " + nouveauTitre;
        if (lblTitreHeader != null) {
            lblTitreHeader.setText(titreComplet);
        }
        if (mainStage != null) {
            mainStage.setTitle(titreComplet);
        }
    }

    public static void changerPageGlobale(Parent nouvelleVue, String nouveauTitre) {
        VBox.setVgrow(nouvelleVue, Priority.ALWAYS);
        if (rootGlobal != null && rootGlobal.getChildren().size() > 1) {
            rootGlobal.getChildren().set(1, nouvelleVue);
        }
        changerTitreGlobal(nouveauTitre);
    }

    @Override
    public void start(Stage stage) throws IOException {
        mainStage = stage;
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());

        stage.initStyle(StageStyle.UNDECORATED);
        HBox header = creerHeaderPersonnalise(stage);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("view/bienvenue-view.fxml"));
        Parent vuePrincipale = fxmlLoader.load();

        VBox.setVgrow(vuePrincipale, Priority.ALWAYS);
        rootGlobal = new VBox(header, vuePrincipale);
        Scene scene = new Scene(rootGlobal);
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logo.png")));
        stage.getIcons().add(icon);
        stage.setScene(scene);
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        savedWidth = 1024;
        savedHeight = 768;
        savedX = (bounds.getWidth() - savedWidth) / 2;
        savedY = (bounds.getHeight() - savedHeight) / 2;
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
        isMaximized = true;

        changerTitreGlobal("Bienvenue");
        stage.show();
    }

    private void toggleMaximize(Stage stage) {
        if (isMaximized) {
            stage.setX(savedX);
            stage.setY(savedY);
            stage.setWidth(savedWidth);
            stage.setHeight(savedHeight);
            isMaximized = false;
        } else {
            savedX = stage.getX();
            savedY = stage.getY();
            savedWidth = stage.getWidth();
            savedHeight = stage.getHeight();
            Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
            stage.setX(bounds.getMinX());
            stage.setY(bounds.getMinY());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());
            isMaximized = true;
        }
    }

    private HBox creerHeaderPersonnalise(Stage stage) {
        HBox header = new HBox();
        header.setStyle("-fx-background-color: #161b22; -fx-padding: 8 15 8 15;");
        header.setAlignment(Pos.CENTER_LEFT);

        lblTitreHeader = new Label("MediaStock");
        lblTitreHeader.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        String svgReduire = "M 0,5 H 10 V 6 H 0 Z";
        Button btnReduire = creerBoutonHeader(svgReduire, "#30363d");
        btnReduire.setOnAction(e -> stage.setIconified(true));

        String svgAgrandir = "M 0,0 H 10 V 10 H 0 Z M 1,1 V 9 H 9 V 1 Z";
        Button btnAgrandir = creerBoutonHeader(svgAgrandir, "#30363d");
        btnAgrandir.setOnAction(e -> toggleMaximize(stage));

        String svgFermer = "M 1,0 L 5,4 L 9,0 L 10,1 L 6,5 L 10,9 L 9,10 L 5,6 L 1,10 L 0,9 L 4,5 L 0,1 Z";
        Button btnFermer = creerBoutonHeader(svgFermer, "#e81123");
        btnFermer.setOnAction(e -> Platform.exit());

        header.setOnMousePressed(event -> {
            if (!isMaximized) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });

        header.setOnMouseDragged(event -> {
            if (!isMaximized) {
                stage.setX(event.getScreenX() - xOffset);
                stage.setY(event.getScreenY() - yOffset);
            }
        });

        header.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                toggleMaximize(stage);
            }
        });

        header.getChildren().addAll(lblTitreHeader, spacer, btnReduire, btnAgrandir, btnFermer);
        return header;
    }

    private Button creerBoutonHeader(String svgData, String hoverBgColor) {
        Button btn = new Button();
        javafx.scene.shape.SVGPath icon = new javafx.scene.shape.SVGPath();
        icon.setContent(svgData);
        icon.setFill(javafx.scene.paint.Color.web("#8b949e"));
        btn.setGraphic(icon);
        btn.setStyle("-fx-background-color: transparent; -fx-padding: 8 15;");

        btn.setOnMouseEntered(e -> {
            btn.setStyle("-fx-background-color: " + hoverBgColor + "; -fx-padding: 8 15; -fx-cursor: hand;");
            icon.setFill(javafx.scene.paint.Color.WHITE);
        });
        btn.setOnMouseExited(e -> {
            btn.setStyle("-fx-background-color: transparent; -fx-padding: 8 15; -fx-cursor: default;");
            icon.setFill(javafx.scene.paint.Color.web("#8b949e"));
        });

        return btn;
    }
}