package com.example.sportmanagerpro.services;

import com.example.sportmanagerpro.models.Competencia;
import com.example.sportmanagerpro.repositories.CompetenciaRepository;

import java.util.List;

public class CompetenciaService {

    private final CompetenciaRepository competenciaRepository = new CompetenciaRepository();

    public void guardar(Competencia competencia) {
        validar(competencia);
        competenciaRepository.guardar(competencia);
    }

    public void actualizar(Competencia competencia) {
        validar(competencia);
        competenciaRepository.actualizar(competencia);
    }

    public List<Competencia> listarTodas() {
        return competenciaRepository.listarTodas();
    }

    private void validar(Competencia competencia) {
        if (competencia.getNombre() == null || competencia.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("Debe ingresar el nombre de la competencia.");
        }

        if (competencia.getFechaInicio() != null
                && competencia.getFechaFin() != null
                && competencia.getFechaFin().isBefore(competencia.getFechaInicio())) {
            throw new IllegalArgumentException("La fecha final no puede ser anterior a la fecha de inicio.");
        }
    }
}