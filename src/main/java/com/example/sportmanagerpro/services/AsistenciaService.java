package com.example.sportmanagerpro.services;

import com.example.sportmanagerpro.models.Asistencia;
import com.example.sportmanagerpro.repositories.AsistenciaRepository;

public class AsistenciaService {

    private final AsistenciaRepository asistenciaRepository = new AsistenciaRepository();

    public void guardar(Asistencia asistencia) {

        validar(asistencia);

        asistenciaRepository.guardar(asistencia);
    }

    private void validar(Asistencia asistencia) {

        if (asistencia.getIdEntrenamiento() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar un entrenamiento.");
        }

        if (asistencia.getIdDeportista() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar una deportista.");
        }

        if (asistencia.getEstado() == null || asistencia.getEstado().trim().isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar el estado de asistencia.");
        }
    }
}