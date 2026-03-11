package com.eseo.mediastock.controller;

import com.eseo.mediastock.service.StockService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AccueilController {

    // --- Labels injectés depuis le FXML ---
    @FXML
    private Label labelNombreLivres;
    @FXML
    private Label labelNombredvd;
    @FXML
    private Label labelNombrejeux;

    // --- VARIABLES STATIQUES (Le Cache) ---
    // On les initialise à -1 pour savoir si c'est le tout premier lancement
    private static int cacheLivres = -1;
    private static int cacheDVDs = -1;
    private static int cacheJeux = -1;

    // --- Service ---
    private StockService stockService;

    @FXML
    public void initialize() {
        stockService = new StockService();

        // 1. On affiche le cache IMMÉDIATEMENT si on a déjà chargé les données au moins une fois
        if (cacheLivres != -1) {
            labelNombreLivres.setText(String.valueOf(cacheLivres));
            labelNombredvd.setText(String.valueOf(cacheDVDs));
            labelNombrejeux.setText(String.valueOf(cacheJeux));
        } else {
            // Si c'est le premier lancement de l'appli, on met 0 en attendant
            labelNombreLivres.setText("0");
            labelNombredvd.setText("0");
            labelNombrejeux.setText("0");
        }

        // 2. On lance la vérification en base de données en arrière-plan
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