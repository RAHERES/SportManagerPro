package com.example.sportmanagerpro.services;

import com.example.sportmanagerpro.models.Entrenamiento;
import com.example.sportmanagerpro.repositories.EntrenamientoRepository;

import java.time.LocalDate;
import java.util.List;

public class EntrenamientoService {

    private final EntrenamientoRepository entrenamientoRepository = new EntrenamientoRepository();

    public void guardar(Entrenamiento entrenamiento) {
        validar(entrenamiento);
        entrenamientoRepository.guardar(entrenamiento);
    }

    public void actualizar(Entrenamiento entrenamiento) {
        validar(entrenamiento);
        entrenamientoRepository.actualizar(entrenamiento);
    }

    public List<Entrenamiento> listarTodos() {
        return entrenamientoRepository.listarTodos();
    }

    private void validar(Entrenamiento entrenamiento) {
        if (entrenamiento.getFecha() == null) {
            throw new IllegalArgumentException("Debe seleccionar la fecha del entrenamiento.");
        }

        if (entrenamiento.getFecha().isAfter(LocalDate.now().plusYears(1))) {
            throw new IllegalArgumentException("La fecha del entrenamiento parece demasiado futura.");
        }

        if (entrenamiento.getIdCategoria() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar una categoría.");
        }

        if (entrenamiento.getTipoEntrenamiento() == null || entrenamiento.getTipoEntrenamiento().trim().isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar el tipo de entrenamiento.");
        }

        if (entrenamiento.getDuracionMinutos() <= 0) {
            throw new IllegalArgumentException("La duración debe ser mayor a cero.");
        }

        if (entrenamiento.getHoraInicio() != null
                && entrenamiento.getHoraFin() != null
                && entrenamiento.getHoraFin().isBefore(entrenamiento.getHoraInicio())) {
            throw new IllegalArgumentException("La hora de término no puede ser anterior a la hora de inicio.");
        }
    }
}