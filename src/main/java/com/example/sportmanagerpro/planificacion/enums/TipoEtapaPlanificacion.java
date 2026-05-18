package com.example.sportmanagerpro.planificacion.enums;

/**
 * Define las etapas metodológicas posibles según el modelo de periodización.
 */
public enum TipoEtapaPlanificacion {

    // Tradicional
    PREPARACION_GENERAL,
    PREPARACION_ESPECIAL,
    PRECOMPETITIVA,
    COMPETITIVA,
    MANTENIMIENTO,
    RECUPERACION,
    REGENERACION,

    // ATR
    ACUMULACION,
    TRANSFORMACION,
    REALIZACION,

    // Bloques
    CONCENTRACION_CARGA,
    TRANSFERENCIA,
    PUESTA_A_PUNTO,

    // Pendular
    CARGA_GENERAL,
    CARGA_ESPECIAL,
    CARGA_COMPETITIVA,

    // Integrada / táctica
    ADQUISICION_MODELO_JUEGO,
    ESTABILIZACION_MODELO_JUEGO,
    OPTIMIZACION_MODELO_JUEGO,
    MORFOCICLO_PATRON,

    // Personalizada
    PERSONALIZADA
}