package com.example.sportmanagerpro.planificacion.service;

import com.example.sportmanagerpro.planificacion.model.Microciclo;
import com.example.sportmanagerpro.planificacion.model.ResumenMicrociclo;
import com.example.sportmanagerpro.planificacion.model.SesionPlanificada;
import com.example.sportmanagerpro.planificacion.model.SesionRealizada;

/**
 * Servicio encargado de comparar lo planificado contra lo realizado.
 */
public class AnalisisPlanificacionService {

    public ResumenMicrociclo analizarMicrociclo(Microciclo microciclo) {
        ResumenMicrociclo resumen = new ResumenMicrociclo();

        int minutosPlanificados = 0;
        int minutosRealizados = 0;
        double cargaPlanificada = 0;
        double cargaReal = 0;

        for (SesionPlanificada sesion : microciclo.getSesiones()) {
            minutosPlanificados += sesion.getDuracionPlanificadaMin();
            cargaPlanificada += sesion.getDuracionPlanificadaMin() * sesion.getRpePrevisto();

            SesionRealizada realizada = sesion.getSesionRealizada();

            if (realizada != null) {
                minutosRealizados += realizada.getDuracionRealMin();
                cargaReal += realizada.getDuracionRealMin() * realizada.getRpeReal();
            }
        }

        resumen.setMinutosPlanificados(minutosPlanificados);
        resumen.setMinutosRealizados(minutosRealizados);
        resumen.setCargaPlanificada(cargaPlanificada);
        resumen.setCargaReal(cargaReal);
        resumen.setCumplimiento(calcularCumplimiento(minutosPlanificados, minutosRealizados));

        return resumen;
    }

    private double calcularCumplimiento(int planificado, int realizado) {
        if (planificado <= 0) return 0;
        return (realizado * 100.0) / planificado;
    }
}