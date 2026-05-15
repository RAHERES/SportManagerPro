package com.example.sportmanagerpro.planificacion.service;

import com.example.sportmanagerpro.planificacion.enums.TipoBloquePlanificacion;
import com.example.sportmanagerpro.planificacion.enums.TipoPeriodizacion;

import java.util.List;

/**
 * Define qué bloques puede usar cada tipo de periodización.
 */
public class BloquesPorPeriodizacionService {

    public List<TipoBloquePlanificacion> obtenerBloquesPermitidos(TipoPeriodizacion tipo) {
        return switch (tipo) {
            case TRADICIONAL -> List.of(
                    TipoBloquePlanificacion.PREPARACION_GENERAL,
                    TipoBloquePlanificacion.PREPARACION_ESPECIAL,
                    TipoBloquePlanificacion.PRECOMPETITIVO,
                    TipoBloquePlanificacion.COMPETITIVO,
                    TipoBloquePlanificacion.TRANSITORIO
            );

            case ATR -> List.of(
                    TipoBloquePlanificacion.ACUMULACION,
                    TipoBloquePlanificacion.TRANSFORMACION,
                    TipoBloquePlanificacion.REALIZACION
            );

            case BLOQUES -> List.of(
                    TipoBloquePlanificacion.CARGA,
                    TipoBloquePlanificacion.IMPACTO,
                    TipoBloquePlanificacion.RECUPERACION
            );

            case PENDULAR, INTEGRADA, PERSONALIZADA -> List.of(
                    TipoBloquePlanificacion.PERSONALIZADO
            );
        };
    }
}