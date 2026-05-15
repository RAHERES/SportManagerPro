package com.example.sportmanagerpro.planificacion.strategy;

/**
 * Contrato base para todos los modelos
 * de planificación deportiva.
 */
public interface ModeloPeriodizacionStrategy {

    void generarMacrociclo();

    void generarMesociclos();

    void generarMicrociclos();

    void distribuirCargas();

    void distribuirTiempos();

}