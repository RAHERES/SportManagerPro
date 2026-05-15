package com.example.sportmanagerpro.planificacion.model;

import com.example.sportmanagerpro.planificacion.enums.TipoMicrociclo;

/**
 * Representa una semana o microciclo dentro de un bloque del macrociclo.
 * Guarda el tipo de microciclo, porcentaje de carga, unidades de entrenamiento
 * y minutos por unidad, para calcular automáticamente la distribución del tiempo.
 */

import javafx.beans.property.*;

import java.time.LocalDate;

/**
 * Representa un microciclo editable desde JavaFX.
 */
public class MicrocicloPlanificado {

    private final IntegerProperty semana = new SimpleIntegerProperty();
    private final ObjectProperty<TipoMicrociclo> tipoMicrociclo = new SimpleObjectProperty<>();
    private final DoubleProperty porcentajeCarga = new SimpleDoubleProperty();
    private final IntegerProperty unidadesEntrenamientoSemana = new SimpleIntegerProperty();
    private final IntegerProperty minutosPorUnidad = new SimpleIntegerProperty();
    private final DoubleProperty minutosPreparacionFisica = new SimpleDoubleProperty();
    private final DoubleProperty minutosPreparacionTecnicoTactica = new SimpleDoubleProperty();

    public MicrocicloPlanificado(int semana,
                                 LocalDate fechaInicio,
                                 TipoMicrociclo tipoMicrociclo,
                                 double porcentajeCarga,
                                 int unidadesEntrenamientoSemana,
                                 int minutosPorUnidad) {
        setSemana(semana);
        setFechaInicio(fechaInicio);
        setTipoMicrociclo(tipoMicrociclo);
        setPorcentajeCarga(porcentajeCarga);
        setUnidadesEntrenamientoSemana(unidadesEntrenamientoSemana);
        setMinutosPorUnidad(minutosPorUnidad);
    }

    private final ObjectProperty<LocalDate> fechaInicio = new SimpleObjectProperty<>();

    public LocalDate getFechaInicio() {
        return fechaInicio.get();
    }

    public void setFechaInicio(LocalDate value) {
        fechaInicio.set(value);
    }

    public ObjectProperty<LocalDate> fechaInicioProperty() {
        return fechaInicio;
    }

    public int getMinutosTotalesSemana() {
        return getUnidadesEntrenamientoSemana() * getMinutosPorUnidad();
    }

    public ReadOnlyIntegerProperty minutosTotalesSemanaProperty() {
        return new SimpleIntegerProperty(getMinutosTotalesSemana());
    }

    public int getSemana() {
        return semana.get();
    }

    public void setSemana(int value) {
        semana.set(value);
    }

    public IntegerProperty semanaProperty() {
        return semana;
    }

    public TipoMicrociclo getTipoMicrociclo() {
        return tipoMicrociclo.get();
    }

    public void setTipoMicrociclo(TipoMicrociclo value) {
        tipoMicrociclo.set(value);
    }

    public ObjectProperty<TipoMicrociclo> tipoMicrocicloProperty() {
        return tipoMicrociclo;
    }

    public double getPorcentajeCarga() {
        return porcentajeCarga.get();
    }

    public void setPorcentajeCarga(double value) {
        porcentajeCarga.set(value);
    }

    public DoubleProperty porcentajeCargaProperty() {
        return porcentajeCarga;
    }

    public int getUnidadesEntrenamientoSemana() {
        return unidadesEntrenamientoSemana.get();
    }

    public void setUnidadesEntrenamientoSemana(int value) {
        unidadesEntrenamientoSemana.set(value);
    }

    public IntegerProperty unidadesEntrenamientoSemanaProperty() {
        return unidadesEntrenamientoSemana;
    }

    public int getMinutosPorUnidad() {
        return minutosPorUnidad.get();
    }

    public void setMinutosPorUnidad(int value) {
        minutosPorUnidad.set(value);
    }

    public IntegerProperty minutosPorUnidadProperty() {
        return minutosPorUnidad;
    }

    public double getMinutosPreparacionFisica() {
        return minutosPreparacionFisica.get();
    }

    public void setMinutosPreparacionFisica(double value) {
        minutosPreparacionFisica.set(value);
    }

    public DoubleProperty minutosPreparacionFisicaProperty() {
        return minutosPreparacionFisica;
    }

    public double getMinutosPreparacionTecnicoTactica() {
        return minutosPreparacionTecnicoTactica.get();
    }

    public void setMinutosPreparacionTecnicoTactica(double value) {
        minutosPreparacionTecnicoTactica.set(value);
    }

    public DoubleProperty minutosPreparacionTecnicoTacticaProperty() {
        return minutosPreparacionTecnicoTactica;
    }
}