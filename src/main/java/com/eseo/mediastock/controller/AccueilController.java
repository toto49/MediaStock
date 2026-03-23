package com.eseo.mediastock.controller;

import com.eseo.mediastock.model.Admin;
import com.eseo.mediastock.service.StockService;
import com.eseo.mediastock.service.UserSession;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * The type Accueil controller.
 */
public class AccueilController {
    private static int cacheLivres = -1;
    private static int cacheDVDs = -1;
    private static int cacheJeux = -1;
    @FXML
    private Label labelBonjour;
    @FXML
    private Label labelNombreLivres;
    @FXML
    private Label labelNombredvd;
    @FXML
    private Label labelNombrejeux;
    private StockService stockService;

    /**
     * Initialize.
     */
    @FXML
    public void initialize() {
        stockService = new StockService();

        Admin admin = UserSession.getAdminConnecte();
        if (admin != null) {
            labelBonjour.setText("Bonjour " + admin.getPrenom() + " " + admin.getNom());
        }

        if (cacheLivres != -1) {
            labelNombreLivres.setText(String.valueOf(cacheLivres));
            labelNombredvd.setText(String.valueOf(cacheDVDs));
            labelNombrejeux.setText(String.valueOf(cacheJeux));
        } else {
            labelNombreLivres.setText("0");
            labelNombredvd.setText("0");
            labelNombrejeux.setText("0");
        }

        // 3. Lancer la vérification
        chargerStatistiques();
    }

    private void chargerStatistiques() {
        new Thread(() -> {
            try {
                // On va chercher les données fraîches
                int totalLivres = stockService.getNombreTotalLivres();
                int totalDVDs = stockService.getNombreTotalDVDs();
                int totalJeux = stockService.getNombreTotalJeux();

                Platform.runLater(() -> {
                    // 3. On sauvegarde les nouvelles données dans notre cache statique
                    cacheLivres = totalLivres;
                    cacheDVDs = totalDVDs;
                    cacheJeux = totalJeux;

                    // 4. On met à jour l'interface visuelle "en douce"
                    labelNombreLivres.setText(String.valueOf(cacheLivres));
                    labelNombredvd.setText(String.valueOf(cacheDVDs));
                    labelNombrejeux.setText(String.valueOf(cacheJeux));
                });
            } catch (Exception e) {
                System.err.println("Erreur lors de la mise à jour des statistiques !");
                e.printStackTrace();
            }
        }).start();
    }
}