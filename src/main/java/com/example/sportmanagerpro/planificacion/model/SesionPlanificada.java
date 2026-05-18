package com.example.sportmanagerpro.planificacion.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Representa una sesión que fue programada previamente.
 * No debe modificarse al registrar lo que realmente ocurrió.
 */
public class SesionPlanificada {

    private LocalDate fecha;
    private DayOfWeek diaSemana;
    private LocalTime horaInicio;
    private int duracionPlanificadaMin;
    private String objetivo;
    private int rpePrevisto;

    private SesionRealizada sesionRealizada;

    public SesionPlanificada(LocalDate fecha, LocalTime horaInicio,
                             int duracionPlanificadaMin, String objetivo, int rpePrevisto) {
        this.fecha = fecha;
        this.diaSemana = fecha.getDayOfWeek();
        this.horaInicio = horaInicio;
        this.duracionPlanificadaMin = duracionPlanificadaMin;
        this.objetivo = objetivo;
        this.rpePrevisto = rpePrevisto;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public DayOfWeek getDiaSemana() {
        return diaSemana;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public int getDuracionPlanificadaMin() {
        return duracionPlanificadaMin;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public int getRpePrevisto() {
        return rpePrevisto;
    }

    public SesionRealizada getSesionRealizada() {
        return sesionRealizada;
    }

    public void setSesionRealizada(SesionRealizada sesionRealizada) {
        this.sesionRealizada = sesionRealizada;
    }
}