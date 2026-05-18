package com.example.sportmanagerpro.planificacion.service;

import com.example.sportmanagerpro.planificacion.enums.TipoPeriodoPlanificacion;
import com.example.sportmanagerpro.planificacion.enums.TipoPeriodizacion;
import com.example.sportmanagerpro.planificacion.model.PeriodoPlanificado;
import com.example.sportmanagerpro.planificacion.model.SemanaPlanificacion;

import java.util.ArrayList;
import java.util.List;

/**
 * Calcula la distribución sugerida de periodos dentro del plan gráfico.
 */
public class PeriodizacionService {

    /**
     * Genera los periodos principales según el número total de semanas,
     * el modelo de periodización y el deporte.
     */
    public List<PeriodoPlanificado> generarPeriodos(List<SemanaPlanificacion> semanas,
                                                    TipoPeriodizacion tipoPeriodizacion,
                                                    String deporte) {

        List<PeriodoPlanificado> periodos = new ArrayList<>();

        if (semanas == null || semanas.isEmpty()) {
            return periodos;
        }

        int totalSemanas = semanas.size();

        int semanasPreparatorio;
        int semanasCompetitivo;
        int semanasTransitorio;

        boolean esFutbol = deporte != null && deporte.equalsIgnoreCase("fútbol")
                || deporte != null && deporte.equalsIgnoreCase("futbol");

        switch (tipoPeriodizacion) {
            case TRADICIONAL -> {
                semanasPreparatorio = redondear(totalSemanas * 0.55);
                semanasCompetitivo = redondear(totalSemanas * 0.35);
                semanasTransitorio = totalSemanas - semanasPreparatorio - semanasCompetitivo;
            }

            case ATR -> {
                semanasPreparatorio = redondear(totalSemanas * 0.40);
                semanasCompetitivo = redondear(totalSemanas * 0.50);
                semanasTransitorio = totalSemanas - semanasPreparatorio - semanasCompetitivo;
            }

            case BLOQUES -> {
                semanasPreparatorio = redondear(totalSemanas * 0.45);
                semanasCompetitivo = redondear(totalSemanas * 0.45);
                semanasTransitorio = totalSemanas - semanasPreparatorio - semanasCompetitivo;
            }

            case PENDULAR, INTEGRADA, PERSONALIZADA -> {
                semanasPreparatorio = redondear(totalSemanas * 0.50);
                semanasCompetitivo = redondear(totalSemanas * 0.40);
                semanasTransitorio = totalSemanas - semanasPreparatorio - semanasCompetitivo;
            }

            default -> {
                semanasPreparatorio = redondear(totalSemanas * 0.55);
                semanasCompetitivo = redondear(totalSemanas * 0.35);
                semanasTransitorio = totalSemanas - semanasPreparatorio - semanasCompetitivo;
            }
        }

        if (esFutbol && totalSemanas >= 16) {
            semanasTransitorio = Math.max(2, redondear(totalSemanas * 0.08));
            semanasPreparatorio = Math.min(semanasPreparatorio, 8);
            semanasCompetitivo = totalSemanas - semanasPreparatorio - semanasTransitorio;
        }

        if (semanasTransitorio < 1) {
            semanasTransitorio = 1;
            semanasCompetitivo = totalSemanas - semanasPreparatorio - semanasTransitorio;
        }

        int semanaInicioPrep = 1;
        int semanaFinPrep = semanasPreparatorio;

        int semanaInicioComp = semanaFinPrep + 1;
        int semanaFinComp = semanaInicioComp + semanasCompetitivo - 1;

        int semanaInicioTrans = semanaFinComp + 1;
        int semanaFinTrans = totalSemanas;

        periodos.add(crearPeriodo(
                TipoPeriodoPlanificacion.PREPARATORIO,
                semanaInicioPrep,
                semanaFinPrep,
                semanas,
                55
        ));

        periodos.add(crearPeriodo(
                TipoPeriodoPlanificacion.COMPETITIVO,
                semanaInicioComp,
                semanaFinComp,
                semanas,
                35
        ));

        periodos.add(crearPeriodo(
                TipoPeriodoPlanificacion.TRANSITORIO,
                semanaInicioTrans,
                semanaFinTrans,
                semanas,
                10
        ));

        return periodos;
    }

    private PeriodoPlanificado crearPeriodo(TipoPeriodoPlanificacion tipo,
                                            int semanaInicio,
                                            int semanaFin,
                                            List<SemanaPlanificacion> semanas,
                                            double porcentaje) {

        SemanaPlanificacion primeraSemana = semanas.get(semanaInicio - 1);
        SemanaPlanificacion ultimaSemana = semanas.get(semanaFin - 1);

        return new PeriodoPlanificado(
                tipo,
                semanaInicio,
                semanaFin,
                primeraSemana.getFechaInicio(),
                ultimaSemana.getFechaFin(),
                porcentaje
        );
    }

    private int redondear(double valor) {
        return (int) Math.round(valor);
    }
}