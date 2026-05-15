package com.example.sportmanagerpro.services;

import com.example.sportmanagerpro.models.Partido;
import com.example.sportmanagerpro.repositories.PartidoRepository;

import java.util.List;

public class PartidoService {

    private final PartidoRepository partidoRepository = new PartidoRepository();

    public void guardar(Partido partido) {
        validar(partido);
        partido.setResultado(calcularResultado(partido.getMarcadorFavor(), partido.getMarcadorContra()));
        partidoRepository.guardar(partido);
    }

    public void actualizar(Partido partido) {
        validar(partido);
        partido.setResultado(calcularResultado(partido.getMarcadorFavor(), partido.getMarcadorContra()));
        partidoRepository.actualizar(partido);
    }

    public List<Partido> listarTodos() {
        return partidoRepository.listarTodos();
    }

    private String calcularResultado(int favor, int contra) {
        if (favor > contra) {
            return "Victoria";
        }

        if (favor == contra) {
            return "Empate";
        }

        return "Derrota";
    }

    private void validar(Partido partido) {
        if (partido.getFecha() == null) {
            throw new IllegalArgumentException("Debe seleccionar la fecha del partido.");
        }

        if (partido.getIdCategoria() <= 0) {
            throw new IllegalArgumentException("Debe seleccionar una categoría.");
        }

        if (partido.getRival() == null || partido.getRival().trim().isEmpty()) {
            throw new IllegalArgumentException("Debe ingresar el rival.");
        }

        if (partido.getMarcadorFavor() < 0 || partido.getMarcadorContra() < 0) {
            throw new IllegalArgumentException("Los marcadores no pueden ser negativos.");
        }
    }
}