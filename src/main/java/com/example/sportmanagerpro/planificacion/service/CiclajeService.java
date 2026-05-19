package com.example.sportmanagerpro.planificacion.service;

import com.example.sportmanagerpro.planificacion.model.CiclajeMesociclo;

import java.util.ArrayList;
import java.util.List;

/**
 * Calcula la distribución del volumen del mesociclo hacia microciclos.
 */
public class CiclajeService {

    public List<Integer> distribuirVolumenMesociclo(int volumenMesociclo,
                                                    CiclajeMesociclo ciclaje) {

        List<Integer> resultado = new ArrayList<>();

        int suma = ciclaje.getSumaValores();
        int acumulado = 0;

        for (int i = 0; i < ciclaje.getValoresMicrociclo().size(); i++) {
            int valor = ciclaje.getValoresMicrociclo().get(i);

            int volumenSemana;

            if (i == ciclaje.getValoresMicrociclo().size() - 1) {
                volumenSemana = volumenMesociclo - acumulado;
            } else {
                volumenSemana = (int) Math.round((volumenMesociclo * valor) / (double) suma);
                acumulado += volumenSemana;
            }

            resultado.add(volumenSemana);
        }

        return resultado;
    }

    public List<Integer> distribuirVolumenSemanalEnSesiones(int volumenSemana,
                                                            List<Integer> pesosSesiones) {

        List<Integer> resultado = new ArrayList<>();

        int suma = pesosSesiones.stream()
                .mapToInt(Integer::intValue)
                .sum();

        int acumulado = 0;

        for (int i = 0; i < pesosSesiones.size(); i++) {
            int peso = pesosSesiones.get(i);

            int minutosSesion;

            if (i == pesosSesiones.size() - 1) {
                minutosSesion = volumenSemana - acumulado;
            } else {
                minutosSesion = (int) Math.round((volumenSemana * peso) / (double) suma);
                acumulado += minutosSesion;
            }

            resultado.add(minutosSesion);
        }

        return resultado;
    }
}