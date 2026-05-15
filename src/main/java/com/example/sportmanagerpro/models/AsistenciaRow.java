package com.example.sportmanagerpro.models;

import javafx.beans.property.SimpleStringProperty;

public class AsistenciaRow {

    private Deportista deportista;
    private SimpleStringProperty estado;

    public AsistenciaRow(Deportista deportista, String estado) {
        this.deportista = deportista;
        this.estado = new SimpleStringProperty(estado);
    }

    public Deportista getDeportista() {
        return deportista;
    }

    public String getEstado() {
        return estado.get();
    }

    public void setEstado(String estado) {
        this.estado.set(estado);
    }

    public SimpleStringProperty estadoProperty() {
        return estado;
    }
}