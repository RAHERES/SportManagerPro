package com.example.sportmanagerpro.planificacion.strategy;

/**
 * Implementación del modelo tradicional.
 */
public class ModeloTradicionalStrategy
        implements ModeloPeriodizacionStrategy {

    @Override
    public void generarMacrociclo() {

        System.out.println(
                "Generando macrociclo tradicional...");
    }

    @Override
    public void generarMesociclos() {

        System.out.println(
                "Generando mesociclos tradicionales...");
    }

    @Override
    public void generarMicrociclos() {

        System.out.println(
                "Generando microciclos tradicionales...");
    }

    @Override
    public void distribuirCargas() {

        System.out.println(
                "Distribuyendo cargas tradicionales...");
    }

    @Override
    public void distribuirTiempos() {

        System.out.println(
                "Distribuyendo tiempos tradicionales...");
    }
}