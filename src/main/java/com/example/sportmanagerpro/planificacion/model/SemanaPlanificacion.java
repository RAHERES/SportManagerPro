package com.example.sportmanagerpro.planificacion.model;

import javafx.beans.property.*;
import java.time.LocalDate;

/**
 * Representa una semana dentro del macrociclo.
 * Guarda el número de semana, fecha inicial, fecha final
 * y el bloque asignado visualmente.
 */
public class SemanaPlanificacion {

    private final IntegerProperty numeroSemana = new SimpleIntegerProperty();
    private final ObjectProperty<LocalDate> fechaInicio = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> fechaFin = new SimpleObjectProperty<>();
    private final StringProperty nombreBloque = new SimpleStringProperty("");

    public SemanaPlanificacion(int numeroSemana, LocalDate fechaInicio, LocalDate fechaFin) {
        setNumeroSemana(numeroSemana);
        setFechaInicio(fechaInicio);
        setFechaFin(fechaFin);
    }

    public int getNumeroSemana() {
        return numeroSemana.get();
    }

    public void setNumeroSemana(int value) {
        numeroSemana.set(value);
    }

    public LocalDate getFechaInicio() {
        return fechaInicio.get();
    }

    public void setFechaInicio(LocalDate value) {
        fechaInicio.set(value);
    }

    public LocalDate getFechaFin() {
        return fechaFin.get();
    }

    public void setFechaFin(LocalDate value) {
        fechaFin.set(value);
    }

    public String getNombreBloque() {
        return nombreBloque.get();
    }

    public void setNombreBloque(String value) {
        nombreBloque.set(value);
    }
}