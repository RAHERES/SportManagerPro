package com.example.sportmanagerpro.models;

import java.time.LocalDate;

public class Competencia {

    private int idCompetencia;
    private String nombre;
    private String tipo;
    private String sede;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String descripcion;
    private String observaciones;

    public int getIdCompetencia() { return idCompetencia; }
    public void setIdCompetencia(int idCompetencia) { this.idCompetencia = idCompetencia; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getSede() { return sede; }
    public void setSede(String sede) { this.sede = sede; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    @Override
    public String toString() {
        return nombre;
    }
}