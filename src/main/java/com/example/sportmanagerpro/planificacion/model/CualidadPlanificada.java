package com.example.sportmanagerpro.planificacion.model;

import com.example.sportmanagerpro.planificacion.enums.TipoPreparacion;

import java.util.UUID;

/**
 * Representa una cualidad, contenido o dirección de entrenamiento
 * que puede agregarse libremente al plan gráfico.
 */
public class CualidadPlanificada {

    private String id;
    private TipoPreparacion tipoPreparacion;
    private String nombre;
    private String unidadMedida;

    private double intensidadMinima;
    private double intensidadMaxima;

    private int sesionesMinimas;
    private int sesionesMaximas;

    private double volumenSesionMinimo;
    private double volumenSesionMaximo;

    private double volumenMicroMinimo;
    private double volumenMicroMaximo;

    private boolean generarFilasPlanGrafico;

    public CualidadPlanificada() {
        this.id = UUID.randomUUID().toString();
        this.tipoPreparacion = TipoPreparacion.PFG;
        this.nombre = "Nueva cualidad";
        this.unidadMedida = "min";
        this.intensidadMinima = 0;
        this.intensidadMaxima = 100;
        this.sesionesMinimas = 1;
        this.sesionesMaximas = 3;
        this.volumenSesionMinimo = 0;
        this.volumenSesionMaximo = 0;
        this.volumenMicroMinimo = 0;
        this.volumenMicroMaximo = 0;
        this.generarFilasPlanGrafico = true;
    }

    public String getId() {
        return id;
    }

    public TipoPreparacion getTipoPreparacion() {
        return tipoPreparacion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public double getIntensidadMinima() {
        return intensidadMinima;
    }

    public double getIntensidadMaxima() {
        return intensidadMaxima;
    }

    public int getSesionesMinimas() {
        return sesionesMinimas;
    }

    public int getSesionesMaximas() {
        return sesionesMaximas;
    }

    public double getVolumenSesionMinimo() {
        return volumenSesionMinimo;
    }

    public double getVolumenSesionMaximo() {
        return volumenSesionMaximo;
    }

    public double getVolumenMicroMinimo() {
        return volumenMicroMinimo;
    }

    public double getVolumenMicroMaximo() {
        return volumenMicroMaximo;
    }

    public boolean isGenerarFilasPlanGrafico() {
        return generarFilasPlanGrafico;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTipoPreparacion(TipoPreparacion tipoPreparacion) {
        this.tipoPreparacion = tipoPreparacion;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public void setIntensidadMinima(double intensidadMinima) {
        this.intensidadMinima = intensidadMinima;
    }

    public void setIntensidadMaxima(double intensidadMaxima) {
        this.intensidadMaxima = intensidadMaxima;
    }

    public void setSesionesMinimas(int sesionesMinimas) {
        this.sesionesMinimas = sesionesMinimas;
    }

    public void setSesionesMaximas(int sesionesMaximas) {
        this.sesionesMaximas = sesionesMaximas;
    }

    public void setVolumenSesionMinimo(double volumenSesionMinimo) {
        this.volumenSesionMinimo = volumenSesionMinimo;
    }

    public void setVolumenSesionMaximo(double volumenSesionMaximo) {
        this.volumenSesionMaximo = volumenSesionMaximo;
    }

    public void setVolumenMicroMinimo(double volumenMicroMinimo) {
        this.volumenMicroMinimo = volumenMicroMinimo;
    }

    public void setVolumenMicroMaximo(double volumenMicroMaximo) {
        this.volumenMicroMaximo = volumenMicroMaximo;
    }

    public void setGenerarFilasPlanGrafico(boolean generarFilasPlanGrafico) {
        this.generarFilasPlanGrafico = generarFilasPlanGrafico;
    }

    public String getClaveFilaSesiones() {
        return tipoPreparacion.name() + "_" + nombre + "_SESIONES";
    }

    public String getClaveFilaVolumen() {
        return tipoPreparacion.name() + "_" + nombre + "_VOLUMEN";
    }

    public String getTituloFilaSesiones() {
        return tipoPreparacion.name() + " " + nombre + " Sesiones";
    }

    public String getTituloFilaVolumen() {
        return tipoPreparacion.name() + " " + nombre + " Volumen";
    }

    public double calcularAmplitudRango() {
        return (volumenMicroMaximo - volumenMicroMinimo) / 4.0;
    }

    public String obtenerRangoVolumen(int rango) {
        double amplitud = calcularAmplitudRango();

        double inicio = volumenMicroMinimo + ((rango - 1) * amplitud);
        double fin = inicio + amplitud;

        return Math.round(inicio) + " - " + Math.round(fin) + " " + unidadMedida;
    }
}