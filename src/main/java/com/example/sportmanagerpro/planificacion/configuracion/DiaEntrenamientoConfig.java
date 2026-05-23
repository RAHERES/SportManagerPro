package com.example.sportmanagerpro.planificacion.configuracion;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * Configura un día de entrenamiento antes de generar el plan gráfico.
 */
public class DiaEntrenamientoConfig {

    private DayOfWeek diaSemana;
    private boolean entrena;
    private LocalTime horaInicio;
    private int duracionMinutos;
    private int unidadesEntrenamiento;

    public DiaEntrenamientoConfig(DayOfWeek diaSemana,
                                  boolean entrena,
                                  LocalTime horaInicio,
                                  int duracionMinutos,
                                  int unidadesEntrenamiento) {
        this.diaSemana = diaSemana;
        this.entrena = entrena;
        this.horaInicio = horaInicio;
        this.duracionMinutos = duracionMinutos;
        this.unidadesEntrenamiento = unidadesEntrenamiento;
    }

    public DayOfWeek getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(DayOfWeek diaSemana) {
        this.diaSemana = diaSemana;
    }

    public boolean isEntrena() {
        return entrena;
    }

    public void setEntrena(boolean entrena) {
        this.entrena = entrena;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(int duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public int getUnidadesEntrenamiento() {
        return unidadesEntrenamiento;
    }

    public void setUnidadesEntrenamiento(int unidadesEntrenamiento) {
        this.unidadesEntrenamiento = unidadesEntrenamiento;
    }
}