module org.inf.galacticoddissey.main {
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
    requires jdk.unsupported.desktop;
    requires org.json;
    requires javafx.media;

    opens org.inf.galacticoddissey to javafx.fxml;
    exports org.inf.galacticoddissey.main;
}