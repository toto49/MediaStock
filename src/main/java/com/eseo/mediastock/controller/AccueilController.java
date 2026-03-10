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

    // --- Service ---
    private StockService stockService;

    @FXML
    public void initialize() {
        stockService = new StockService();

        // On affiche un texte de chargement provisoire
        labelNombreLivres.setText("0");
        labelNombredvd.setText("0");
        labelNombrejeux.setText("0");

        chargerStatistiques();
    }

    private void chargerStatistiques() {

        new Thread(() -> {
            try {
                int totalLivres = stockService.getNombreTotalLivres();
                int totalDVDs = stockService.getNombreTotalDVDs();
                int totalJeux = stockService.getNombreTotalJeux();
                Platform.runLater(() -> {
                    labelNombreLivres.setText(String.valueOf(totalLivres));
                    labelNombredvd.setText(String.valueOf(totalDVDs));
                    labelNombrejeux.setText(String.valueOf(totalJeux));

                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}