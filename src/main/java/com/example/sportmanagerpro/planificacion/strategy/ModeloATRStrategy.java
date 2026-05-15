package com.example.sportmanagerpro.planificacion.strategy;

/**
 * Implementación del modelo ATR.
 */
public class ModeloATRStrategy
        implements ModeloPeriodizacionStrategy {

    @Override
    public void generarMacrociclo() {

        System.out.println(
                "Generando macrociclo ATR...");
    }

    @Override
    public void generarMesociclos() {

        System.out.println(
                "Generando mesociclos ATR...");
    }

    @Override
    public void generarMicrociclos() {

        System.out.println(
                "Generando microciclos ATR...");
    }

    @Override
    public void distribuirCargas() {

        System.out.println(
                "Distribuyendo cargas ATR...");
    }

    @Override
    public void distribuirTiempos() {

        System.out.println(
                "Distribuyendo tiempos ATR...");
    }
}