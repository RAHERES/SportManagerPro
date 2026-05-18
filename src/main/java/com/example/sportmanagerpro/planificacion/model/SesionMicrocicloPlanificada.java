package com.example.sportmanagerpro.planificacion.model;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * Representa una unidad de entrenamiento dentro de un microciclo.
 * Un mismo día puede tener una o varias unidades.
 */
public class SesionMicrocicloPlanificada {

    private int semana;
    private DayOfWeek diaSemana;
    private LocalTime horaInicio;
    private int duracionMinutos;
    private boolean extra;
    private String observaciones;

    public SesionMicrocicloPlanificada(int semana,
                                       DayOfWeek diaSemana,
                                       LocalTime horaInicio,
                                       int duracionMinutos,
                                       boolean extra,
                                       String observaciones) {
        this.semana = semana;
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.duracionMinutos = duracionMinutos;
        this.extra = extra;
        this.observaciones = observaciones;
    }

    public int getSemana() {
        return semana;
    }

    public DayOfWeek getDiaSemana() {
        return diaSemana;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public boolean isExtra() {
        return extra;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setSemana(int semana) {
        this.semana = semana;
    }

    public void setDiaSemana(DayOfWeek diaSemana) {
        this.diaSemana = diaSemana;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public void setDuracionMinutos(int duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public void setExtra(boolean extra) {
        this.extra = extra;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}