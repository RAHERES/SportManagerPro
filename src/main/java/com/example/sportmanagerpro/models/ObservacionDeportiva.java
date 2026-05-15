package com.example.sportmanagerpro.models;

import java.time.LocalDate;

public class ObservacionDeportiva {

    private int idObservacion;
    private int idDeportista;
    private LocalDate fecha;
    private String tipo;
    private String observacion;

    public int getIdObservacion() { return idObservacion; }
    public void setIdObservacion(int idObservacion) { this.idObservacion = idObservacion; }

    public int getIdDeportista() { return idDeportista; }
    public void setIdDeportista(int idDeportista) { this.idDeportista = idDeportista; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
}