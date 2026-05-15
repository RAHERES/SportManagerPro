package com.example.sportmanagerpro.services;

import com.example.sportmanagerpro.models.EstadisticaPartido;
import com.example.sportmanagerpro.repositories.EstadisticaPartidoRepository;

public class EstadisticaPartidoService {

    private final EstadisticaPartidoRepository repository = new EstadisticaPartidoRepository();

    public void guardar(EstadisticaPartido estadistica) {
        validar(estadistica);
        repository.guardar(estadistica);
    }

    private void validar(EstadisticaPartido estadistica) {
        if (estadistica.getIdPartido() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar un partido.");
        }

        if (estadistica.getIdDeportista() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar una deportista.");
        }

        if (estadistica.getMinutosJugados() < 0) {
            throw new IllegalArgumentException("Los minutos jugados no pueden ser negativos.");
        }

        if (estadistica.getGoles() < 0 || estadistica.getAsistencias() < 0) {
            throw new IllegalArgumentException("Goles y asistencias no pueden ser negativos.");
        }
    }
}