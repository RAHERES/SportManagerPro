package com.example.sportmanagerpro.planificacion.model;

import com.example.sportmanagerpro.planificacion.enums.TipoMesociclo;

import java.time.LocalDate;

/**
 * Representa un mesociclo editable dentro del plan gráfico.
 */
public class MesocicloPlanificado {

    private TipoMesociclo tipoMesociclo;
    private String nombre;
    private int semanaInicio;
    private int duracionSemanas;
    private int semanaFin;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String colorHex;

    public MesocicloPlanificado(TipoMesociclo tipoMesociclo,
                                String nombre,
                                int semanaInicio,
                                int duracionSemanas,
                                LocalDate fechaInicio,
                                LocalDate fechaFin,
                                String colorHex) {
        this.tipoMesociclo = tipoMesociclo;
        this.nombre = nombre;
        this.semanaInicio = semanaInicio;
        this.duracionSemanas = duracionSemanas;
        this.semanaFin = semanaInicio + duracionSemanas - 1;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.colorHex = colorHex;
    }

    public TipoMesociclo getTipoMesociclo() {
        return tipoMesociclo;
    }

    public void setTipoMesociclo(TipoMesociclo tipoMesociclo) {
        this.tipoMesociclo = tipoMesociclo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getSemanaInicio() {
        return semanaInicio;
    }

    public int getDuracionSemanas() {
        return duracionSemanas;
    }

    public int getSemanaFin() {
        return semanaFin;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public String getColorHex() {
        return colorHex;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setSemanaInicio(int semanaInicio) {
        this.semanaInicio = semanaInicio;
        this.semanaFin = semanaInicio + duracionSemanas - 1;
    }

    public void setDuracionSemanas(int duracionSemanas) {
        this.duracionSemanas = duracionSemanas;
        this.semanaFin = semanaInicio + duracionSemanas - 1;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public void setColorHex(String colorHex) {
        this.colorHex = colorHex;
    }
}