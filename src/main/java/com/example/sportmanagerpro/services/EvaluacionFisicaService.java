package com.example.sportmanagerpro.services;

import com.example.sportmanagerpro.models.EvaluacionFisica;
import com.example.sportmanagerpro.repositories.EvaluacionFisicaRepository;

import java.util.List;

public class EvaluacionFisicaService {

    private final EvaluacionFisicaRepository evaluacionRepository = new EvaluacionFisicaRepository();

    public void guardar(EvaluacionFisica evaluacion) {
        validar(evaluacion);
        evaluacionRepository.guardar(evaluacion);
    }

    public void actualizar(EvaluacionFisica evaluacion) {
        validar(evaluacion);
        evaluacionRepository.actualizar(evaluacion);
    }

    public List<EvaluacionFisica> listarTodas() {
        return evaluacionRepository.listarTodas();
    }

    public List<EvaluacionFisica> listarPorDeportista(int idDeportista) {
        return evaluacionRepository.listarPorDeportista(idDeportista);
    }

    private void validar(EvaluacionFisica evaluacion) {
        if (evaluacion.getIdDeportista() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar una deportista.");
        }

        if (evaluacion.getFecha() == null) {
            throw new IllegalArgumentException("Debe seleccionar la fecha de evaluación.");
        }

        if (evaluacion.getPrueba() == null || evaluacion.getPrueba().trim().isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar la prueba física.");
        }

        if (evaluacion.getResultado() < 0) {
            throw new IllegalArgumentException("El resultado no puede ser negativo.");
        }

        if (evaluacion.getUnidad() == null || evaluacion.getUnidad().trim().isEmpty()) {
            throw new IllegalArgumentException("Debe indicar la unidad de medida.");
        }
    }
}