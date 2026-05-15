package com.example.sportmanagerpro.services;

import com.example.sportmanagerpro.models.ObservacionDeportiva;
import com.example.sportmanagerpro.repositories.ObservacionDeportivaRepository;

import java.util.List;

public class ObservacionDeportivaService {

    private final ObservacionDeportivaRepository repository = new ObservacionDeportivaRepository();

    public void guardar(ObservacionDeportiva observacion) {
        validar(observacion);
        repository.guardar(observacion);
    }

    public void actualizar(ObservacionDeportiva observacion) {
        validar(observacion);
        repository.actualizar(observacion);
    }

    public List<ObservacionDeportiva> listarTodas() {
        return repository.listarTodas();
    }

    public List<ObservacionDeportiva> listarPorDeportista(int idDeportista) {
        return repository.listarPorDeportista(idDeportista);
    }

    private void validar(ObservacionDeportiva observacion) {
        if (observacion.getIdDeportista() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar una deportista.");
        }

        if (observacion.getFecha() == null) {
            throw new IllegalArgumentException("Debe seleccionar la fecha de la observación.");
        }

        if (observacion.getTipo() == null || observacion.getTipo().trim().isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar el tipo de observación.");
        }

        if (observacion.getObservacion() == null || observacion.getObservacion().trim().isEmpty()) {
            throw new IllegalArgumentException("Debe escribir la observación.");
        }
    }
}