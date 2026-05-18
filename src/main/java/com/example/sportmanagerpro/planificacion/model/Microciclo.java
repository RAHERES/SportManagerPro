package com.example.sportmanagerpro.planificacion.model;

import com.example.sportmanagerpro.planificacion.enums.TipoMicrociclo;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa una semana o bloque corto de entrenamiento.
 */
public class Microciclo {

    private int numero;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private TipoMicrociclo tipo;
    private double porcentajeCargaObjetivo;

    private final List<SesionPlanificada> sesiones = new ArrayList<>();

    public Microciclo(int numero, LocalDate fechaInicio, LocalDate fechaFin,
                      TipoMicrociclo tipo, double porcentajeCargaObjetivo) {
        this.numero = numero;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.tipo = tipo;
        this.porcentajeCargaObjetivo = porcentajeCargaObjetivo;
    }

    public int getNumero() {
        return numero;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public TipoMicrociclo getTipo() {
        return tipo;
    }

    public double getPorcentajeCargaObjetivo() {
        return porcentajeCargaObjetivo;
    }

    public List<SesionPlanificada> getSesiones() {
        return sesiones;
    }
}