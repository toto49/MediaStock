package com.eseo.mediastock.model;

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
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;

import java.awt.image.BufferedImage;
import java.util.function.Consumer;

/**
 * The type Code barre scanner.
 */
public class CodeBarreScanner {

    private OpenCVFrameGrabber grabber;
    private Thread webcamThread;
    private ImageView imageView;
    private Label resultLabel;
    private volatile boolean isRunning = false;
    private Stage stage;

    // Pour renvoyer le résultat à la fenêtre principale
    private Consumer<String> onCodeScanned;

    /**
     * Ouvrir fenetre.
     *
     * @param onCodeScanned the on code scanned
     */
    public void ouvrirFenetre(Consumer<String> onCodeScanned) {
        this.onCodeScanned = onCodeScanned;
        this.stage = new Stage();

        imageView = new ImageView();

        // Format "bandeau" pour le code-barres
        imageView.setViewport(new Rectangle2D(0, 140, 640, 200));
        imageView.setFitWidth(450);
        imageView.setFitHeight(250);
        imageView.setPreserveRatio(true);

        VBox imageContainer = new VBox(imageView);
        imageContainer.setAlignment(Pos.CENTER);
        imageContainer.setPadding(new Insets(30, 0, 0, 0));

        resultLabel = new Label("Démarrage de la caméra...");
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

        // La fenêtre s'affiche IMMÉDIATEMENT
        stage.show();

        // Puis on lance l'allumage en arrière-plan
        startWebcam();
    }

    private void startWebcam() {
        // On crée le Thread dès maintenant, tout le processus lourd se fera dedans
        webcamThread = new Thread(() -> {
            grabber = new OpenCVFrameGrabber(0);
            grabber.setImageWidth(640);
            grabber.setImageHeight(480);

            try {
                // L'opération bloquante est maintenant isolée de l'interface !
                grabber.start();
                isRunning = true;

                // On prévient l'interface que c'est prêt
                Platform.runLater(() -> resultLabel.setText("Placez le code-barres devant la caméra..."));

                // On enchaîne directement avec la boucle de capture
                captureAndDecodeLoop();

            } catch (FrameGrabber.Exception e) {
                Platform.runLater(() -> {
                    resultLabel.setText("Erreur : Impossible d'accéder à la caméra.");
                    resultLabel.setStyle("-fx-text-fill: red;");
                });
                e.printStackTrace();
            }
        });

        webcamThread.setDaemon(true);
        webcamThread.start();
    }

    private void captureAndDecodeLoop() {
        MultiFormatReader reader = new MultiFormatReader();

        try (Java2DFrameConverter converter = new Java2DFrameConverter()) {

            while (isRunning) {
                try {
                    Frame frame = grabber.grab();

                    if (frame != null && frame.image != null) {
                        BufferedImage bImage = converter.getBufferedImage(frame);

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
                                // Aucun code
                            }
                        }
                    }

                    Thread.sleep(30);

                } catch (Exception e) {
                    if (isRunning) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Stop webcam.
     */
    public void stopWebcam() {
        isRunning = false;
        if (grabber != null) {
            try {
                grabber.stop();
                grabber.release();
            } catch (FrameGrabber.Exception e) {
                e.printStackTrace();
            }
        }
    }
}