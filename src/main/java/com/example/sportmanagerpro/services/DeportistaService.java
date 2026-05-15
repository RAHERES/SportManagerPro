package com.example.sportmanagerpro.services;


import com.example.sportmanagerpro.models.Deportista;
import com.example.sportmanagerpro.repositories.DeportistaRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class DeportistaService {

    private final DeportistaRepository deportistaRepository = new DeportistaRepository();

    public void guardar(Deportista deportista) {
        validar(deportista);
        deportistaRepository.guardar(deportista);
    }

    public void actualizar(Deportista deportista) {
        validar(deportista);
        deportistaRepository.actualizar(deportista);
    }

    public List<Deportista> listarTodas() {
        return deportistaRepository.listarTodas();
    }

    public void desactivar(int idDeportista) {
        deportistaRepository.desactivar(idDeportista);
    }

    public int calcularEdad(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) {
            return 0;
        }
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }

    private void validar(Deportista deportista) {
        if (deportista.getNombreCompleto() == null || deportista.getNombreCompleto().trim().isEmpty()) {
            throw new IllegalArgumentException("Debe ingresar el nombre completo de la deportista.");
        }

        if (deportista.getIdCategoria() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar una categoría.");
        }

        if (deportista.getFechaNacimiento() != null && deportista.getFechaNacimiento().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser futura.");
        }
    }

    public List<Deportista> listarPorCategoria(int idCategoria) {
        return deportistaRepository.listarPorCategoria(idCategoria);
    }
}