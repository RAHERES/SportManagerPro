package com.example.sportmanagerpro.services;

import com.example.sportmanagerpro.models.DashboardResumen;
import com.example.sportmanagerpro.models.HistorialDeportistaResumen;
import com.example.sportmanagerpro.models.ReporteCategoriaResumen;
import com.example.sportmanagerpro.repositories.ReporteRepository;

public class ReporteService {

    private final ReporteRepository reporteRepository = new ReporteRepository();

    public HistorialDeportistaResumen obtenerResumenDeportista(int idDeportista) {
        if (idDeportista <= 0) {
            throw new IllegalArgumentException("Debe seleccionar una deportista.");
        }

        return reporteRepository.obtenerResumenDeportista(idDeportista);
    }

    public DashboardResumen obtenerDashboardResumen() {
        return reporteRepository.obtenerDashboardResumen();
    }

    public ReporteCategoriaResumen obtenerResumenCategoria(int idCategoria) {
        if (idCategoria <= 0) {
            throw new IllegalArgumentException("Debe seleccionar una categoría.");
        }

        return reporteRepository.obtenerResumenCategoria(idCategoria);
    }
}