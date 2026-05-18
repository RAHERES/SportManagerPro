package com.example.sportmanagerpro.planificacion.model;

import com.example.sportmanagerpro.planificacion.enums.TipoEtapaPlanificacion;
import com.example.sportmanagerpro.planificacion.enums.TipoPeriodoPlanificacion;

import java.time.LocalDate;

/**
 * Representa una etapa metodológica dentro de un periodo o bloque de planificación.
 */
public class EtapaPlanificada {

    private TipoEtapaPlanificacion tipoEtapa;
    private TipoPeriodoPlanificacion periodoPadre;
    private int semanaInicio;
    private int semanaFin;
    private int duracionSemanas;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private double porcentajeDentroPeriodo;

    public EtapaPlanificada(TipoEtapaPlanificacion tipoEtapa,
                            TipoPeriodoPlanificacion periodoPadre,
                            int semanaInicio,
                            int semanaFin,
                            LocalDate fechaInicio,
                            LocalDate fechaFin,
                            double porcentajeDentroPeriodo) {

        this.tipoEtapa = tipoEtapa;
        this.periodoPadre = periodoPadre;
        this.semanaInicio = semanaInicio;
        this.semanaFin = semanaFin;
        this.duracionSemanas = semanaFin - semanaInicio + 1;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.porcentajeDentroPeriodo = porcentajeDentroPeriodo;
    }

    public TipoEtapaPlanificacion getTipoEtapa() {
        return tipoEtapa;
    }

    public TipoPeriodoPlanificacion getPeriodoPadre() {
        return periodoPadre;
    }

    public int getSemanaInicio() {
        return semanaInicio;
    }

    public int getSemanaFin() {
        return semanaFin;
    }

    public int getDuracionSemanas() {
        return duracionSemanas;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public double getPorcentajeDentroPeriodo() {
        return porcentajeDentroPeriodo;
    }
}