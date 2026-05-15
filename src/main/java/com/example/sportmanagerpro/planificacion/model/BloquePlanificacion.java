package com.example.sportmanagerpro.planificacion.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa un bloque metodológico dentro del macrociclo.
 * Ejemplo: Acumulación, Transformación, Realización o cualquier bloque personalizado.
 */
public class BloquePlanificacion {

    private String nombre;
    private double porcentajePreparacionFisica;
    private double porcentajePreparacionTecnicoTactica;

    private final List<MicrocicloPlanificado> microciclos = new ArrayList<>();

    public BloquePlanificacion(String nombre,
                               double porcentajePreparacionFisica,
                               double porcentajePreparacionTecnicoTactica) {
        this.nombre = nombre;
        this.porcentajePreparacionFisica = porcentajePreparacionFisica;
        this.porcentajePreparacionTecnicoTactica = porcentajePreparacionTecnicoTactica;
    }

    public void agregarMicrociclo(MicrocicloPlanificado microciclo) {
        microciclos.add(microciclo);
    }

    public String getNombre() {
        return nombre;
    }

    public double getPorcentajePreparacionFisica() {
        return porcentajePreparacionFisica;
    }

    public double getPorcentajePreparacionTecnicoTactica() {
        return porcentajePreparacionTecnicoTactica;
    }

    public List<MicrocicloPlanificado> getMicrociclos() {
        return microciclos;
    }
}