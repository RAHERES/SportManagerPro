package com.example.sportmanagerpro.planificacion.model;

import java.time.LocalDate;

/**
 * Representa una temporada deportiva completa.
 */
public class Temporada {

    private Integer id;

    private String nombre;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    private String categoria;

    private String objetivoGeneral;

    public Temporada() {
    }

    public Temporada(Integer id,
                      String nombre,
                      LocalDate fechaInicio,
                      LocalDate fechaFin,
                      String categoria,
                      String objetivoGeneral) {

        this.id = id;
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.categoria = categoria;
        this.objetivoGeneral = objetivoGeneral;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getObjetivoGeneral() {
        return objetivoGeneral;
    }

    public void setObjetivoGeneral(String objetivoGeneral) {
        this.objetivoGeneral = objetivoGeneral;
    }
}