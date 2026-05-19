package com.example.sportmanagerpro.planificacion.model;

import com.example.sportmanagerpro.planificacion.enums.TipoMicrociclo;

import java.time.LocalDate;

/**
 * Representa un microciclo editable dentro del plan gráfico.
 */
public class MicrocicloGraficoPlanificado {

    private int minutosPlanificados;
    private int pesoCiclaje;
    private Integer mesocicloPadreSemanaInicio;

    private TipoMicrociclo tipoMicrociclo;
    private String nombre;
    private int semanaInicio;
    private int duracionSemanas;
    private int semanaFin;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String colorHex;

    public MicrocicloGraficoPlanificado(TipoMicrociclo tipoMicrociclo,
                                        String nombre,
                                        int semanaInicio,
                                        int duracionSemanas,
                                        LocalDate fechaInicio,
                                        LocalDate fechaFin,
                                        String colorHex) {
        this.tipoMicrociclo = tipoMicrociclo;
        this.nombre = nombre;
        this.semanaInicio = semanaInicio;
        this.duracionSemanas = duracionSemanas;
        this.semanaFin = semanaInicio + duracionSemanas - 1;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.colorHex = colorHex;
    }

    public int getMinutosPlanificados() {
        return minutosPlanificados;
    }

    public void setMinutosPlanificados(int minutosPlanificados) {
        this.minutosPlanificados = minutosPlanificados;
    }

    public int getPesoCiclaje() {
        return pesoCiclaje;
    }

    public void setPesoCiclaje(int pesoCiclaje) {
        this.pesoCiclaje = pesoCiclaje;
    }

    public Integer getMesocicloPadreSemanaInicio() {
        return mesocicloPadreSemanaInicio;
    }

    public void setMesocicloPadreSemanaInicio(Integer mesocicloPadreSemanaInicio) {
        this.mesocicloPadreSemanaInicio = mesocicloPadreSemanaInicio;
    }

    public TipoMicrociclo getTipoMicrociclo() {
        return tipoMicrociclo;
    }

    public void setTipoMicrociclo(TipoMicrociclo tipoMicrociclo) {
        this.tipoMicrociclo = tipoMicrociclo;
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