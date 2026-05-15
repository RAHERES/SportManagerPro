package com.example.sportmanagerpro.models;

import java.time.LocalDate;

public class EvaluacionFisica {

    private int idEvaluacion;
    private int idDeportista;
    private LocalDate fecha;
    private String prueba;
    private double resultado;
    private String unidad;
    private String observaciones;
    private String evaluador;

    public int getIdEvaluacion() { return idEvaluacion; }
    public void setIdEvaluacion(int idEvaluacion) { this.idEvaluacion = idEvaluacion; }

    public int getIdDeportista() { return idDeportista; }
    public void setIdDeportista(int idDeportista) { this.idDeportista = idDeportista; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getPrueba() { return prueba; }
    public void setPrueba(String prueba) { this.prueba = prueba; }

    public double getResultado() { return resultado; }
    public void setResultado(double resultado) { this.resultado = resultado; }

    public String getUnidad() { return unidad; }
    public void setUnidad(String unidad) { this.unidad = unidad; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public String getEvaluador() { return evaluador; }
    public void setEvaluador(String evaluador) { this.evaluador = evaluador; }
}