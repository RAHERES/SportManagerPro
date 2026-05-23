module com.example.sportmanagerpro {
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
    requires java.sql;
    requires org.apache.pdfbox;


    opens com.example.sportmanagerpro to javafx.fxml;
    opens com.example.sportmanagerpro.controllers to javafx.fxml;
    opens com.example.sportmanagerpro.planificacion.controller to javafx.fxml;
    opens com.example.sportmanagerpro.planificacion.model to javafx.fxml;
    opens com.example.sportmanagerpro.planificacion.configuracion to javafx.fxml;
    exports com.example.sportmanagerpro;
    exports com.example.sportmanagerpro.controllers;
    exports com.example.sportmanagerpro.planificacion.controller;
    exports com.example.sportmanagerpro.planificacion.model;
    exports com.example.sportmanagerpro.planificacion.configuracion;
}