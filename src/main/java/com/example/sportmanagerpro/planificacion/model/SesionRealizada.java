package com.example.sportmanagerpro.planificacion.model;

import com.example.sportmanagerpro.planificacion.enums.EstadoSesion;
import com.example.sportmanagerpro.planificacion.enums.MotivoIncidencia;
import java.time.LocalTime;

/**
 * Registra lo que ocurrió realmente durante una sesión.
 */
public class SesionRealizada {

    private EstadoSesion estado;
    private LocalTime horaInicioReal;
    private LocalTime horaFinReal;
    private int duracionRealMin;
    private int rpeReal;
    private MotivoIncidencia motivo;
    private String observaciones;

    public SesionRealizada(EstadoSesion estado, LocalTime horaInicioReal, LocalTime horaFinReal,
                           int duracionRealMin, int rpeReal,
                           MotivoIncidencia motivo, String observaciones) {
        this.estado = estado;
        this.horaInicioReal = horaInicioReal;
        this.horaFinReal = horaFinReal;
        this.duracionRealMin = duracionRealMin;
        this.rpeReal = rpeReal;
        this.motivo = motivo;
        this.observaciones = observaciones;
    }

    public EstadoSesion getEstado() {
        return estado;
    }

    public int getDuracionRealMin() {
        return duracionRealMin;
    }

    public int getRpeReal() {
        return rpeReal;
    }

    public MotivoIncidencia getMotivo() {
        return motivo;
    }

    public String getObservaciones() {
        return observaciones;
    }
}