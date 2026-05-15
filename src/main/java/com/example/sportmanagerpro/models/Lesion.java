package com.example.sportmanagerpro.models;

import java.time.LocalDate;

public class Lesion {

    private int idLesion;
    private int idDeportista;
    private LocalDate fecha;
    private String zonaAfectada;
    private String tipoMolestia;
    private String contexto;
    private String accionTomada;
    private LocalDate fechaRegresoEstimada;
    private String estado;
    private String observaciones;

    public int getIdLesion() { return idLesion; }
    public void setIdLesion(int idLesion) { this.idLesion = idLesion; }

    public int getIdDeportista() { return idDeportista; }
    public void setIdDeportista(int idDeportista) { this.idDeportista = idDeportista; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getZonaAfectada() { return zonaAfectada; }
    public void setZonaAfectada(String zonaAfectada) { this.zonaAfectada = zonaAfectada; }

    public String getTipoMolestia() { return tipoMolestia; }
    public void setTipoMolestia(String tipoMolestia) { this.tipoMolestia = tipoMolestia; }

    public String getContexto() { return contexto; }
    public void setContexto(String contexto) { this.contexto = contexto; }

    public String getAccionTomada() { return accionTomada; }
    public void setAccionTomada(String accionTomada) { this.accionTomada = accionTomada; }

    public LocalDate getFechaRegresoEstimada() { return fechaRegresoEstimada; }
    public void setFechaRegresoEstimada(LocalDate fechaRegresoEstimada) { this.fechaRegresoEstimada = fechaRegresoEstimada; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}