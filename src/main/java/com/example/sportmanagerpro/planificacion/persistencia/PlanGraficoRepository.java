package com.example.sportmanagerpro.planificacion.persistencia;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.nio.file.*;
import java.util.Comparator;
import java.util.List;

/**
 * Repositorio encargado de guardar, cargar y listar planes gráficos en JSON.
 */
public class PlanGraficoRepository {

    private final Path carpetaPlanes;
    private final ObjectMapper mapper;

    public PlanGraficoRepository() {
        this.carpetaPlanes = Paths.get(
                System.getProperty("user.home"),
                "SportManagerPro",
                "planes_graficos_json"
        );

        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public Path getCarpetaPlanes() {
        return carpetaPlanes;
    }

    public void guardar(PlanGrafico plan) throws IOException {
        Files.createDirectories(carpetaPlanes);

        String nombreArchivo = normalizarNombreArchivo(plan.nombrePlan);
        Path archivo = carpetaPlanes.resolve(nombreArchivo + ".json");

        mapper.writeValue(archivo.toFile(), plan);
    }

    public PlanGrafico cargar(Path archivo) throws IOException {
        return mapper.readValue(archivo.toFile(), PlanGrafico.class);
    }

    public List<Path> listarPlanes() throws IOException {
        Files.createDirectories(carpetaPlanes);

        try (var stream = Files.list(carpetaPlanes)) {
            return stream
                    .filter(path -> path.toString().endsWith(".json"))
                    .sorted(Comparator.comparing(Path::getFileName))
                    .toList();
        }
    }

    public void eliminar(Path archivo) throws IOException {
        Files.deleteIfExists(archivo);
    }

    private String normalizarNombreArchivo(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return "plan_grafico";
        }

        return nombre.trim()
                .replaceAll("[^a-zA-Z0-9áéíóúÁÉÍÓÚñÑ _-]", "")
                .replace(" ", "_");
    }
}