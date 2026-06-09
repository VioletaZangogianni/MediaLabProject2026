module gr.medialab.medialabproject {
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
    requires java.desktop;
    requires java.management;
    requires java.json;

    opens gr.medialab.medialabproject to javafx.fxml;
    opens gr.medialab.medialabproject.controllers to javafx.fxml;
    opens gr.medialab.medialabproject.model to javafx.base;
    exports gr.medialab.medialabproject;
}