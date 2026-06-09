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
    requires com.fasterxml.jackson.databind;


    opens com.example.sportmanagerpro to javafx.fxml;
    opens com.example.sportmanagerpro.controllers to javafx.fxml;
    opens com.example.sportmanagerpro.planificacion.controller to javafx.fxml;
    opens com.example.sportmanagerpro.planificacion.model to javafx.fxml;
    opens com.example.sportmanagerpro.planificacion.configuracion to javafx.fxml;
    opens com.example.sportmanagerpro.planificacion.persistencia to javafx.fxml;
    opens com.example.sportmanagerpro.nutricion.ui.components to javafx.fxml;
    opens com.example.sportmanagerpro.pacientes to javafx.fxml;
    opens com.example.sportmanagerpro.sportnutri.app to javafx.fxml;

    exports com.example.sportmanagerpro;
    exports com.example.sportmanagerpro.controllers;
    exports com.example.sportmanagerpro.planificacion.controller;
    exports com.example.sportmanagerpro.planificacion.model;
    exports com.example.sportmanagerpro.planificacion.configuracion;
    exports com.example.sportmanagerpro.planificacion.persistencia;
    exports com.example.sportmanagerpro.nutricion.ui to javafx.graphics;
    exports com.example.sportmanagerpro.pacientes to javafx.graphics;
    exports com.example.sportmanagerpro.sportnutri.app to javafx.graphics;
}