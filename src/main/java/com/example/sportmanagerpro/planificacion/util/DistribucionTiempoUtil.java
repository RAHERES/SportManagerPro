package com.example.sportmanagerpro.planificacion.util;

/**
 * Utilidades matemáticas para la distribución
 * metodológica de tiempos.
 */
public class DistribucionTiempoUtil {

    /**
     * Calcula tiempo total del bloque.
     *
     * @param minutosMicrociclo minutos por micro
     * @param cantidadMicrociclos número de micros
     * @return tiempo total
     */
    public static int calcularTiempoTotal(
            int minutosMicrociclo,
            int cantidadMicrociclos) {

        return minutosMicrociclo
                * cantidadMicrociclos;
    }

    /**
     * Calcula tiempo físico.
     *
     * @param tiempoTotal tiempo total
     * @param porcentaje porcentaje físico
     * @return tiempo físico
     */
    public static double calcularTiempoFisico(
            int tiempoTotal,
            double porcentaje) {

        return tiempoTotal * porcentaje;
    }

    /**
     * Calcula coeficiente indicativo.
     *
     * @param tiempoFisico tiempo físico
     * @param sumaPorcentajes suma porcentajes
     * @return coeficiente
     */
    public static double calcularCI(
            double tiempoFisico,
            double sumaPorcentajes) {

        return tiempoFisico
                / sumaPorcentajes;
    }

    /**
     * Distribuye minutos por microciclo.
     *
     * @param porcentaje porcentaje del micro
     * @param ci coeficiente indicativo
     * @return minutos asignados
     */
    public static double distribuirTiempo(
            double porcentaje,
            double ci) {

        return porcentaje * ci;
    }
}