package com.example.sportmanagerpro.planificacion.service;

import com.example.sportmanagerpro.planificacion.enums.EstadoSesion;
import com.example.sportmanagerpro.planificacion.enums.MotivoIncidencia;
import com.example.sportmanagerpro.planificacion.model.SesionPlanificada;
import com.example.sportmanagerpro.planificacion.model.SesionRealizada;

import java.time.Duration;
import java.time.LocalTime;

/**
 * Servicio encargado de guardar el resultado real de una sesión.
 */
public class RegistroSesionService {

    public SesionRealizada registrarSesion(SesionPlanificada planificada,
                                           EstadoSesion estado,
                                           LocalTime inicioReal,
                                           LocalTime finReal,
                                           int rpeReal,
                                           MotivoIncidencia motivo,
                                           String observaciones) {

        int duracionReal = calcularDuracionReal(estado, inicioReal, finReal);

        SesionRealizada realizada = new SesionRealizada(
                estado,
                inicioReal,
                finReal,
                duracionReal,
                rpeReal,
                motivo,
                observaciones
        );

        planificada.setSesionRealizada(realizada);

        return realizada;
    }

    private int calcularDuracionReal(EstadoSesion estado, LocalTime inicioReal, LocalTime finReal) {
        if (estado == EstadoSesion.SUSPENDIDA || estado == EstadoSesion.CANCELADA) {
            return 0;
        }

        if (inicioReal == null || finReal == null || !finReal.isAfter(inicioReal)) {
            return 0;
        }

        return (int) Duration.between(inicioReal, finReal).toMinutes();
    }
}