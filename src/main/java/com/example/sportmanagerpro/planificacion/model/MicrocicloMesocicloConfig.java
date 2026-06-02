package com.example.sportmanagerpro.planificacion.model;

import com.example.sportmanagerpro.planificacion.enums.TipoMicrociclo;

/**
 * Configuración interna de cada microciclo dentro de un mesociclo.
 */
public class MicrocicloMesocicloConfig {

    private TipoMicrociclo tipoMicrociclo;
    private double porcentajeCarga;
    private int unidadesEntrenamientoSemana;
    private int minutosPorUnidad;

    public MicrocicloMesocicloConfig() {
        this.tipoMicrociclo = TipoMicrociclo.CARGA;
        this.porcentajeCarga = 60;
        this.unidadesEntrenamientoSemana = 5;
        this.minutosPorUnidad = 120;
    }

    public MicrocicloMesocicloConfig(TipoMicrociclo tipoMicrociclo,
                                     double porcentajeCarga,
                                     int unidadesEntrenamientoSemana,
                                     int minutosPorUnidad) {
        this.tipoMicrociclo = tipoMicrociclo;
        this.porcentajeCarga = porcentajeCarga;
        this.unidadesEntrenamientoSemana = unidadesEntrenamientoSemana;
        this.minutosPorUnidad = minutosPorUnidad;
    }

    public TipoMicrociclo getTipoMicrociclo() {
        return tipoMicrociclo;
    }

    public void setTipoMicrociclo(TipoMicrociclo tipoMicrociclo) {
        this.tipoMicrociclo = tipoMicrociclo;
    }

    public double getPorcentajeCarga() {
        return porcentajeCarga;
    }

    public void setPorcentajeCarga(double porcentajeCarga) {
        this.porcentajeCarga = porcentajeCarga;
    }

    public int getUnidadesEntrenamientoSemana() {
        return unidadesEntrenamientoSemana;
    }

    public void setUnidadesEntrenamientoSemana(int unidadesEntrenamientoSemana) {
        this.unidadesEntrenamientoSemana = unidadesEntrenamientoSemana;
    }

    public int getMinutosPorUnidad() {
        return minutosPorUnidad;
    }

    public void setMinutosPorUnidad(int minutosPorUnidad) {
        this.minutosPorUnidad = minutosPorUnidad;
    }

    public int getMinutosTotalesMicrociclo() {
        return unidadesEntrenamientoSemana * minutosPorUnidad;
    }
}