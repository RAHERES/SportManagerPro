package com.example.sportmanagerpro.planificacion.engine;

import com.example.sportmanagerpro.planificacion.model.BloquePlanificacion;
import com.example.sportmanagerpro.planificacion.model.MicrocicloPlanificado;

/**
 * Calcula la distribución metodológica de tiempos dentro de un bloque.
 */
public class DistribucionTiemposEngine {

    public void calcular(BloquePlanificacion bloque) {

        double tiempoTotalBloque = bloque.getMicrociclos()
                .stream()
                .mapToDouble(MicrocicloPlanificado::getMinutosTotalesSemana)
                .sum();

        double tiempoFisicoBloque =
                tiempoTotalBloque * (bloque.getPorcentajePreparacionFisica() / 100.0);

        double sumaPorcentajesCarga = bloque.getMicrociclos()
                .stream()
                .mapToDouble(MicrocicloPlanificado::getPorcentajeCarga)
                .sum();

        double coeficienteIndicativo =
                tiempoFisicoBloque / sumaPorcentajesCarga;

        for (MicrocicloPlanificado micro : bloque.getMicrociclos()) {

            double tiempoFisicoMicro =
                    micro.getPorcentajeCarga() * coeficienteIndicativo;

            double tiempoTecnicoTacticoMicro =
                    micro.getMinutosTotalesSemana() - tiempoFisicoMicro;

            micro.setMinutosPreparacionFisica(tiempoFisicoMicro);
            micro.setMinutosPreparacionTecnicoTactica(tiempoTecnicoTacticoMicro);
        }
    }
}