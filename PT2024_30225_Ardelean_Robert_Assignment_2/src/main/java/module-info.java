module org.example.pt2024_30225_ardelean_robert_assignment_2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.logging;
    requires javafx.media;
    requires java.desktop;

    opens org.example.pt2024_30225_ardelean_robert_assignment_2 to javafx.fxml;
    exports org.example.pt2024_30225_ardelean_robert_assignment_2;
}