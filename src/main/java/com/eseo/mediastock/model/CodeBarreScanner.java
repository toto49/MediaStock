package com.eseo.mediastock.model;

import com.github.sarxos.webcam.Webcam;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.function.Consumer;

public class CodeBarreScanner {

    private Webcam webcam;
    private Thread webcamThread;
    private ImageView imageView;
    private Label resultLabel;
    private boolean isRunning = false;
    private Stage stage;

    // Pour renvoyer le résultat à la fenêtre principale
    private Consumer<String> onCodeScanned;

    // On demande un Consumer (une action à exécuter) en paramètre
    public void ouvrirFenetre(Consumer<String> onCodeScanned) {
        this.onCodeScanned = onCodeScanned;
        this.stage = new Stage();

        imageView = new ImageView();

        imageView.setViewport(new Rectangle2D(0, 140, 640, 200));
        imageView.setFitWidth(450);
        imageView.setFitHeight(250);
        imageView.setPreserveRatio(true);

        VBox imageContainer = new VBox(imageView);
        imageContainer.setAlignment(Pos.CENTER);
        imageContainer.setPadding(new Insets(30, 0, 0, 0));

        resultLabel = new Label("Placez le code-barres devant la caméra...");
        resultLabel.setFont(new Font("Arial", 20));
        resultLabel.setStyle("-fx-text-fill: #333333; -fx-padding: 20px;");

        VBox bottomBox = new VBox(resultLabel);
        bottomBox.setAlignment(Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setCenter(imageContainer);
        root.setBottom(bottomBox);

        Scene scene = new Scene(root, 500, 325);
        stage.setTitle("Scanner l'exemplaire");
        stage.setScene(scene);

        stage.setOnCloseRequest(event -> stopWebcam());

        stage.show();
        startWebcam();
    }

    private void startWebcam() {
        webcam = Webcam.getDefault();
        if (webcam != null) {
            webcam.setViewSize(new Dimension(640, 480));
            webcam.open();
            isRunning = true;

            webcamThread = new Thread(this::captureAndDecodeLoop);
            webcamThread.setDaemon(true);
            webcamThread.start();
        } else {
            resultLabel.setText("Erreur : Aucune webcam détectée.");
            resultLabel.setStyle("-fx-text-fill: red;");
        }
    }

    private void captureAndDecodeLoop() {
        MultiFormatReader reader = new MultiFormatReader();

        while (isRunning) {
            try {
                BufferedImage bImage = webcam.getImage();

                if (bImage != null) {
                    Image fxImage = SwingFXUtils.toFXImage(bImage, null);
                    Platform.runLater(() -> imageView.setImage(fxImage));

                    BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(bImage);
                    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                    try {
                        Result result = reader.decode(bitmap);

                        String codeBarreText = result.getText();
                        if (onCodeScanned != null) {
                            onCodeScanned.accept(codeBarreText);
                        }

                        Platform.runLater(() -> {
                            stopWebcam();
                            stage.close();
                        });

                        break;

                    } catch (NotFoundException e) {
                        // Rien trouvé, on continue de chercher
                    }
                }
                Thread.sleep(33); // ~30 FPS
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void stopWebcam() {
        isRunning = false;
        if (webcam != null && webcam.isOpen()) {
            webcam.close();
        }
    }
}