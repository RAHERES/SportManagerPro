package com.example.sportmanagerpro.services;

import com.example.sportmanagerpro.models.Lesion;
import com.example.sportmanagerpro.repositories.LesionRepository;

import java.util.List;

public class LesionService {

    private final LesionRepository lesionRepository = new LesionRepository();

    public void guardar(Lesion lesion) {
        validar(lesion);
        lesionRepository.guardar(lesion);
    }

    public void actualizar(Lesion lesion) {
        validar(lesion);
        lesionRepository.actualizar(lesion);
    }

    public List<Lesion> listarTodas() {
        return lesionRepository.listarTodas();
    }

    public List<Lesion> listarPorDeportista(int idDeportista) {
        return lesionRepository.listarPorDeportista(idDeportista);
    }

    private void validar(Lesion lesion) {
        if (lesion.getIdDeportista() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar una deportista.");
        }

        if (lesion.getFecha() == null) {
            throw new IllegalArgumentException("Debe seleccionar la fecha de la molestia o lesión.");
        }

        if (lesion.getZonaAfectada() == null || lesion.getZonaAfectada().trim().isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar la zona afectada.");
        }

        if (lesion.getEstado() == null || lesion.getEstado().trim().isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar el estado de seguimiento.");
        }

        if (lesion.getFechaRegresoEstimada() != null
                && lesion.getFechaRegresoEstimada().isBefore(lesion.getFecha())) {
            throw new IllegalArgumentException("La fecha estimada de regreso no puede ser anterior a la fecha de lesión.");
        }
    }
}