module com.eseo.mediastock {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.eseo.mediastock to javafx.fxml;
    exports com.eseo.mediastock;
}