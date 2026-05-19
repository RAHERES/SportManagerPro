package com.example.sportmanagerpro.planificacion.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa la estructura de ciclaje de un mesociclo.
 * Ejemplo: 4, 6, 7, 3 para una estructura ascendente 3:1.
 */
public class CiclajeMesociclo {

    private String nombre;
    private List<Integer> valoresMicrociclo = new ArrayList<>();

    public CiclajeMesociclo(String nombre, List<Integer> valoresMicrociclo) {
        this.nombre = nombre;
        this.valoresMicrociclo = valoresMicrociclo;
    }

    public String getNombre() {
        return nombre;
    }

    public List<Integer> getValoresMicrociclo() {
        return valoresMicrociclo;
    }

    public int getSumaValores() {
        return valoresMicrociclo.stream()
                .mapToInt(Integer::intValue)
                .sum();
    }
}