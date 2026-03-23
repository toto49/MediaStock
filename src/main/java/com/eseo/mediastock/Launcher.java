package com.eseo.mediastock;

/**
 * Point d'entrée principal de l'application MediaStock.
 * <p>
 * Cette classe sert de lanceur (wrapper) pour contourner les restrictions strictes
 * liées aux modules de JavaFX 11+. Elle appelle la méthode main de {@link HelloApplication}
 * sans nécessiter de configuration complexe du module path au lancement.
 * </p>
 */
public class Launcher {

    /**
     * Main.
     *
     * @param args the args
     */
    static void main(String[] args) {
        HelloApplication.main(args);
    }
}