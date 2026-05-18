package com.example.sportmanagerpro.planificacion.service;

import com.example.sportmanagerpro.planificacion.enums.TipoMicrociclo;
import com.example.sportmanagerpro.planificacion.model.ConfiguracionDiaEntrenamiento;
import com.example.sportmanagerpro.planificacion.model.Microciclo;
import com.example.sportmanagerpro.planificacion.model.SesionPlanificada;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio encargado de generar microciclos y sesiones planificadas.
 */
public class CalendarioEntrenamientoService {

    public List<Microciclo> generarMicrociclos(LocalDate inicio, LocalDate fin) {
        List<Microciclo> microciclos = new ArrayList<>();
        LocalDate actual = inicio;
        int numero = 1;

        while (!actual.isAfter(fin)) {
            LocalDate cierre = actual.plusDays(6);

            if (cierre.isAfter(fin)) {
                cierre = fin;
            }

            TipoMicrociclo tipo = sugerirTipoMicrociclo(numero);

            microciclos.add(new Microciclo(
                    numero,
                    actual,
                    cierre,
                    tipo,
                    porcentajePorTipo(tipo)
            ));

            actual = cierre.plusDays(1);
            numero++;
        }

        return microciclos;
    }

    public void generarSesiones(Microciclo microciclo,
                                List<ConfiguracionDiaEntrenamiento> configuracion) {

        Map<DayOfWeek, ConfiguracionDiaEntrenamiento> diasActivos = configuracion.stream()
                .filter(ConfiguracionDiaEntrenamiento::isEntrena)
                .collect(Collectors.toMap(
                        ConfiguracionDiaEntrenamiento::getDiaSemana,
                        c -> c
                ));

        LocalDate fecha = microciclo.getFechaInicio();

        while (!fecha.isAfter(microciclo.getFechaFin())) {
            ConfiguracionDiaEntrenamiento dia = diasActivos.get(fecha.getDayOfWeek());

            if (dia != null) {
                microciclo.getSesiones().add(new SesionPlanificada(
                        fecha,
                        dia.getHoraInicio(),
                        dia.getDuracionMin(),
                        "Objetivo del microciclo " + microciclo.getNumero(),
                        rpeSugerido(microciclo.getTipo())
                ));
            }

            fecha = fecha.plusDays(1);
        }
    }

    private TipoMicrociclo sugerirTipoMicrociclo(int numero) {
        int posicion = numero % 4;

        if (posicion == 1) return TipoMicrociclo.AJUSTE;
        if (posicion == 2) return TipoMicrociclo.CARGA;
        if (posicion == 3) return TipoMicrociclo.IMPACTO;

        return TipoMicrociclo.RECUPERACION;
    }

    private double porcentajePorTipo(TipoMicrociclo tipo) {
        return switch (tipo) {
            case AJUSTE -> 60;
            case CARGA -> 80;
            case IMPACTO -> 95;
            case RECUPERACION -> 50;
            case PRECOMPETITIVO -> 40;
            case COMPETENCIA -> 30;
        };
    }

    private int rpeSugerido(TipoMicrociclo tipo) {
        return switch (tipo) {
            case AJUSTE -> 5;
            case CARGA -> 6;
            case IMPACTO -> 8;
            case RECUPERACION -> 3;
            case PRECOMPETITIVO -> 4;
            case COMPETENCIA -> 7;
        };
    }
}