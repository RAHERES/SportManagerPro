package com.example.sportmanagerpro.planificacion.service;

import com.example.sportmanagerpro.planificacion.enums.TipoEtapaPlanificacion;
import com.example.sportmanagerpro.planificacion.enums.TipoPeriodoPlanificacion;
import com.example.sportmanagerpro.planificacion.enums.TipoPeriodizacion;
import com.example.sportmanagerpro.planificacion.model.EtapaPlanificada;
import com.example.sportmanagerpro.planificacion.model.PeriodoPlanificado;
import com.example.sportmanagerpro.planificacion.model.SemanaPlanificacion;

import java.util.ArrayList;
import java.util.List;

/**
 * Genera etapas metodológicas según el tipo de periodización seleccionado.
 */
public class EtapasPorPeriodizacionService {

    public List<EtapaPlanificada> generarEtapas(TipoPeriodizacion tipoPeriodizacion,
                                                List<PeriodoPlanificado> periodos,
                                                List<SemanaPlanificacion> semanas) {

        List<EtapaPlanificada> etapas = new ArrayList<>();

        if (tipoPeriodizacion == null || periodos == null || periodos.isEmpty()) {
            return etapas;
        }

        switch (tipoPeriodizacion) {
            case TRADICIONAL -> etapas.addAll(generarEtapasTradicional(periodos, semanas));
            case ATR -> etapas.addAll(generarEtapasATR(periodos, semanas));
            case BLOQUES -> etapas.addAll(generarEtapasBloques(periodos, semanas));
            case PENDULAR -> etapas.addAll(generarEtapasPendular(periodos, semanas));
            case INTEGRADA -> etapas.addAll(generarEtapasIntegrada(periodos, semanas));
            case PERSONALIZADA -> etapas.addAll(generarEtapasPersonalizada(periodos, semanas));
        }

        return etapas;
    }

    private List<EtapaPlanificada> generarEtapasTradicional(List<PeriodoPlanificado> periodos,
                                                            List<SemanaPlanificacion> semanas) {

        List<EtapaPlanificada> etapas = new ArrayList<>();

        for (PeriodoPlanificado periodo : periodos) {
            switch (periodo.getTipoPeriodo()) {
                case PREPARATORIO -> etapas.addAll(generarDentroPeriodo(periodo, semanas,
                        new DefEtapa(TipoEtapaPlanificacion.PREPARACION_GENERAL, 60),
                        new DefEtapa(TipoEtapaPlanificacion.PREPARACION_ESPECIAL, 40)
                ));

                case COMPETITIVO -> etapas.addAll(generarDentroPeriodo(periodo, semanas,
                        new DefEtapa(TipoEtapaPlanificacion.PRECOMPETITIVA, 20),
                        new DefEtapa(TipoEtapaPlanificacion.COMPETITIVA, 70),
                        new DefEtapa(TipoEtapaPlanificacion.MANTENIMIENTO, 10)
                ));

                case TRANSITORIO -> etapas.addAll(generarDentroPeriodo(periodo, semanas,
                        new DefEtapa(TipoEtapaPlanificacion.RECUPERACION, 70),
                        new DefEtapa(TipoEtapaPlanificacion.REGENERACION, 30)
                ));
            }
        }

        return etapas;
    }

    private List<EtapaPlanificada> generarEtapasATR(List<PeriodoPlanificado> periodos,
                                                    List<SemanaPlanificacion> semanas) {

        List<EtapaPlanificada> etapas = new ArrayList<>();

        for (PeriodoPlanificado periodo : periodos) {
            if (periodo.getTipoPeriodo() == TipoPeriodoPlanificacion.TRANSITORIO) {
                etapas.addAll(generarDentroPeriodo(periodo, semanas,
                        new DefEtapa(TipoEtapaPlanificacion.RECUPERACION, 100)
                ));
            } else {
                etapas.addAll(generarDentroPeriodo(periodo, semanas,
                        new DefEtapa(TipoEtapaPlanificacion.ACUMULACION, 45),
                        new DefEtapa(TipoEtapaPlanificacion.TRANSFORMACION, 35),
                        new DefEtapa(TipoEtapaPlanificacion.REALIZACION, 20)
                ));
            }
        }

        return etapas;
    }

    private List<EtapaPlanificada> generarEtapasBloques(List<PeriodoPlanificado> periodos,
                                                        List<SemanaPlanificacion> semanas) {

        List<EtapaPlanificada> etapas = new ArrayList<>();

        for (PeriodoPlanificado periodo : periodos) {
            etapas.addAll(generarDentroPeriodo(periodo, semanas,
                    new DefEtapa(TipoEtapaPlanificacion.CONCENTRACION_CARGA, 50),
                    new DefEtapa(TipoEtapaPlanificacion.TRANSFERENCIA, 30),
                    new DefEtapa(TipoEtapaPlanificacion.PUESTA_A_PUNTO, 20)
            ));
        }

        return etapas;
    }

    private List<EtapaPlanificada> generarEtapasPendular(List<PeriodoPlanificado> periodos,
                                                         List<SemanaPlanificacion> semanas) {

        List<EtapaPlanificada> etapas = new ArrayList<>();

        for (PeriodoPlanificado periodo : periodos) {
            etapas.addAll(generarDentroPeriodo(periodo, semanas,
                    new DefEtapa(TipoEtapaPlanificacion.CARGA_GENERAL, 40),
                    new DefEtapa(TipoEtapaPlanificacion.CARGA_ESPECIAL, 35),
                    new DefEtapa(TipoEtapaPlanificacion.CARGA_COMPETITIVA, 25)
            ));
        }

        return etapas;
    }

    private List<EtapaPlanificada> generarEtapasIntegrada(List<PeriodoPlanificado> periodos,
                                                          List<SemanaPlanificacion> semanas) {

        List<EtapaPlanificada> etapas = new ArrayList<>();

        for (PeriodoPlanificado periodo : periodos) {
            etapas.addAll(generarDentroPeriodo(periodo, semanas,
                    new DefEtapa(TipoEtapaPlanificacion.ADQUISICION_MODELO_JUEGO, 30),
                    new DefEtapa(TipoEtapaPlanificacion.ESTABILIZACION_MODELO_JUEGO, 40),
                    new DefEtapa(TipoEtapaPlanificacion.OPTIMIZACION_MODELO_JUEGO, 20),
                    new DefEtapa(TipoEtapaPlanificacion.MORFOCICLO_PATRON, 10)
            ));
        }

        return etapas;
    }

    private List<EtapaPlanificada> generarEtapasPersonalizada(List<PeriodoPlanificado> periodos,
                                                              List<SemanaPlanificacion> semanas) {

        List<EtapaPlanificada> etapas = new ArrayList<>();

        for (PeriodoPlanificado periodo : periodos) {
            etapas.addAll(generarDentroPeriodo(periodo, semanas,
                    new DefEtapa(TipoEtapaPlanificacion.PERSONALIZADA, 100)
            ));
        }

        return etapas;
    }

    private List<EtapaPlanificada> generarDentroPeriodo(PeriodoPlanificado periodo,
                                                        List<SemanaPlanificacion> semanas,
                                                        DefEtapa... definiciones) {

        List<EtapaPlanificada> etapas = new ArrayList<>();

        int totalSemanasPeriodo = periodo.getDuracionSemanas();

        int[] duraciones = convertirPorcentajesASemanas(totalSemanasPeriodo, definiciones);

        int semanaInicioActual = periodo.getSemanaInicio();

        for (int i = 0; i < definiciones.length; i++) {
            int duracion = duraciones[i];

            if (duracion <= 0) {
                continue;
            }

            int semanaFinActual = semanaInicioActual + duracion - 1;

            if (semanaFinActual > periodo.getSemanaFin()) {
                semanaFinActual = periodo.getSemanaFin();
            }

            SemanaPlanificacion semanaInicio = semanas.get(semanaInicioActual - 1);
            SemanaPlanificacion semanaFin = semanas.get(semanaFinActual - 1);

            etapas.add(new EtapaPlanificada(
                    definiciones[i].tipoEtapa,
                    periodo.getTipoPeriodo(),
                    semanaInicioActual,
                    semanaFinActual,
                    semanaInicio.getFechaInicio(),
                    semanaFin.getFechaFin(),
                    definiciones[i].porcentaje
            ));

            semanaInicioActual = semanaFinActual + 1;

            if (semanaInicioActual > periodo.getSemanaFin()) {
                break;
            }
        }

        return etapas;
    }

    private int[] convertirPorcentajesASemanas(int totalSemanasPeriodo, DefEtapa[] definiciones) {
        int[] duraciones = new int[definiciones.length];
        int suma = 0;
        int indiceMayor = 0;
        double mayorPorcentaje = 0;

        for (int i = 0; i < definiciones.length; i++) {
            duraciones[i] = (int) Math.round(totalSemanasPeriodo * definiciones[i].porcentaje / 100.0);
            suma += duraciones[i];

            if (definiciones[i].porcentaje > mayorPorcentaje) {
                mayorPorcentaje = definiciones[i].porcentaje;
                indiceMayor = i;
            }
        }

        int diferencia = totalSemanasPeriodo - suma;
        duraciones[indiceMayor] += diferencia;

        return duraciones;
    }

    private static class DefEtapa {
        private final TipoEtapaPlanificacion tipoEtapa;
        private final double porcentaje;

        private DefEtapa(TipoEtapaPlanificacion tipoEtapa, double porcentaje) {
            this.tipoEtapa = tipoEtapa;
            this.porcentaje = porcentaje;
        }
    }
}