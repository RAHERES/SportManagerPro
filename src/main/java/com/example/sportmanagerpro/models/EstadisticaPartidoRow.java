package com.example.sportmanagerpro.models;

import javafx.beans.property.*;

public class EstadisticaPartidoRow {

    private final Deportista deportista;
    private final BooleanProperty titular;
    private final IntegerProperty minutosJugados;
    private final IntegerProperty goles;
    private final IntegerProperty asistencias;
    private final IntegerProperty tarjetasAmarillas;
    private final IntegerProperty tarjetasRojas;
    private final StringProperty observaciones;

    public EstadisticaPartidoRow(Deportista deportista) {
        this.deportista = deportista;
        this.titular = new SimpleBooleanProperty(false);
        this.minutosJugados = new SimpleIntegerProperty(0);
        this.goles = new SimpleIntegerProperty(0);
        this.asistencias = new SimpleIntegerProperty(0);
        this.tarjetasAmarillas = new SimpleIntegerProperty(0);
        this.tarjetasRojas = new SimpleIntegerProperty(0);
        this.observaciones = new SimpleStringProperty("");
    }

    public Deportista getDeportista() {
        return deportista;
    }

    public boolean isTitular() {
        return titular.get();
    }

    public BooleanProperty titularProperty() {
        return titular;
    }

    public int getMinutosJugados() {
        return minutosJugados.get();
    }

    public IntegerProperty minutosJugadosProperty() {
        return minutosJugados;
    }

    public int getGoles() {
        return goles.get();
    }

    public IntegerProperty golesProperty() {
        return goles;
    }

    public int getAsistencias() {
        return asistencias.get();
    }

    public IntegerProperty asistenciasProperty() {
        return asistencias;
    }

    public int getTarjetasAmarillas() {
        return tarjetasAmarillas.get();
    }

    public IntegerProperty tarjetasAmarillasProperty() {
        return tarjetasAmarillas;
    }

    public int getTarjetasRojas() {
        return tarjetasRojas.get();
    }

    public IntegerProperty tarjetasRojasProperty() {
        return tarjetasRojas;
    }

    public String getObservaciones() {
        return observaciones.get();
    }

    public StringProperty observacionesProperty() {
        return observaciones;
    }
}