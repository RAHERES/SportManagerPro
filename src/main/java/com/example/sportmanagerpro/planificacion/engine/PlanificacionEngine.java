package com.example.sportmanagerpro.planificacion.engine;


import com.example.sportmanagerpro.planificacion.strategy.ModeloPeriodizacionStrategy;

/**
 * Motor principal de planificación.
 */
public class PlanificacionEngine {

    private final ModeloPeriodizacionStrategy strategy;

    public PlanificacionEngine(
            ModeloPeriodizacionStrategy strategy) {

        this.strategy = strategy;
    }

    /**
     * Ejecuta todo el proceso automático
     * de planificación.
     */
    public void ejecutarPlanificacion() {

        strategy.generarMacrociclo();

        strategy.generarMesociclos();

        strategy.generarMicrociclos();

        strategy.distribuirCargas();

        strategy.distribuirTiempos();
    }
}