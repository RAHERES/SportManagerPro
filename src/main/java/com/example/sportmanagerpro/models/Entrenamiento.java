package com.example.sportmanagerpro.models;

import java.time.LocalDate;
import java.time.LocalTime;

public class Entrenamiento {

    private int idEntrenamiento;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private int idCategoria;
    private String objetivo;
    private String tipoEntrenamiento;
    private String intensidad;
    private int duracionMinutos;
    private String contenidoTrabajado;
    private String materialUtilizado;
    private String observaciones;

    public int getIdEntrenamiento() { return idEntrenamiento; }
    public void setIdEntrenamiento(int idEntrenamiento) { this.idEntrenamiento = idEntrenamiento; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }

    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }

    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }

    public String getObjetivo() { return objetivo; }
    public void setObjetivo(String objetivo) { this.objetivo = objetivo; }

    public String getTipoEntrenamiento() { return tipoEntrenamiento; }
    public void setTipoEntrenamiento(String tipoEntrenamiento) { this.tipoEntrenamiento = tipoEntrenamiento; }

    public String getIntensidad() { return intensidad; }
    public void setIntensidad(String intensidad) { this.intensidad = intensidad; }

    public int getDuracionMinutos() { return duracionMinutos; }
    public void setDuracionMinutos(int duracionMinutos) { this.duracionMinutos = duracionMinutos; }

    public String getContenidoTrabajado() { return contenidoTrabajado; }
    public void setContenidoTrabajado(String contenidoTrabajado) { this.contenidoTrabajado = contenidoTrabajado; }

    public String getMaterialUtilizado() { return materialUtilizado; }
    public void setMaterialUtilizado(String materialUtilizado) { this.materialUtilizado = materialUtilizado; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    @Override
    public String toString() {
        return fecha + " - " + tipoEntrenamiento;
    }
}