package com.example.sportmanagerpro.planificacion.model;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * Configura si un día de la semana tiene entrenamiento y cuánto dura.
 */
public class ConfiguracionDiaEntrenamiento {

    private DayOfWeek diaSemana;
    private boolean entrena;
    private LocalTime horaInicio;
    private int duracionMin;

    public ConfiguracionDiaEntrenamiento(DayOfWeek diaSemana, boolean entrena,
                                         LocalTime horaInicio, int duracionMin) {
        this.diaSemana = diaSemana;
        this.entrena = entrena;
        this.horaInicio = horaInicio;
        this.duracionMin = duracionMin;
    }

    public DayOfWeek getDiaSemana() {
        return diaSemana;
    }

    public boolean isEntrena() {
        return entrena;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public int getDuracionMin() {
        return duracionMin;
    }
}