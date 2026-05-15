package com.example.sportmanagerpro.planificacion.model;

import com.example.sportmanagerpro.planificacion.enums.TipoBloquePlanificacion;
import javafx.beans.property.*;

/**
 * Bloque metodológico configurable por el entrenador.
 */
public class BloqueMacrociclo {

    private final ObjectProperty<TipoBloquePlanificacion> tipoBloque =
            new SimpleObjectProperty<>();

    private final IntegerProperty duracionSemanas =
            new SimpleIntegerProperty();

    private final DoubleProperty porcentajeFisico =
            new SimpleDoubleProperty();

    private final DoubleProperty porcentajeTecnicoTactico =
            new SimpleDoubleProperty();

    public BloqueMacrociclo(TipoBloquePlanificacion tipoBloque,
                            int duracionSemanas,
                            double porcentajeFisico,
                            double porcentajeTecnicoTactico) {
        setTipoBloque(tipoBloque);
        setDuracionSemanas(duracionSemanas);
        setPorcentajeFisico(porcentajeFisico);
        setPorcentajeTecnicoTactico(porcentajeTecnicoTactico);
    }

    public TipoBloquePlanificacion getTipoBloque() {
        return tipoBloque.get();
    }

    public void setTipoBloque(TipoBloquePlanificacion value) {
        tipoBloque.set(value);
    }

    public ObjectProperty<TipoBloquePlanificacion> tipoBloqueProperty() {
        return tipoBloque;
    }

    public int getDuracionSemanas() {
        return duracionSemanas.get();
    }

    public void setDuracionSemanas(int value) {
        duracionSemanas.set(value);
    }

    public IntegerProperty duracionSemanasProperty() {
        return duracionSemanas;
    }

    public double getPorcentajeFisico() {
        return porcentajeFisico.get();
    }

    public void setPorcentajeFisico(double value) {
        porcentajeFisico.set(value);
    }

    public DoubleProperty porcentajeFisicoProperty() {
        return porcentajeFisico;
    }

    public double getPorcentajeTecnicoTactico() {
        return porcentajeTecnicoTactico.get();
    }

    public void setPorcentajeTecnicoTactico(double value) {
        porcentajeTecnicoTactico.set(value);
    }

    public DoubleProperty porcentajeTecnicoTacticoProperty() {
        return porcentajeTecnicoTactico;
    }
}