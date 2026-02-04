module com.eseo.mediastock {
    requires javafx.controls;
    requires javafx.fxml;
    // ... tes autres requires ...

    opens com.eseo.mediastock to javafx.fxml;
    opens com.eseo.mediastock.controller to javafx.fxml; // Indispensable pour tes contrôleurs
    exports com.eseo.mediastock;
}