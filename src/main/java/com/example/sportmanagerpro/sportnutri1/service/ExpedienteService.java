package com.example.sportmanagerpro.sportnutri1.service;

import com.example.sportmanagerpro.sportnutri1.model.ExpedientePersona;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ExpedienteService {

    private final List<ExpedientePersona> expedientes = new ArrayList<>();

    public ExpedientePersona guardar(ExpedientePersona expediente) {
        if (expediente.getIdExpediente() == null || expediente.getIdExpediente().isBlank()) {
            expediente.setIdExpediente("EXP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }

        expediente.setUltimaActualizacion(LocalDateTime.now());
        expedientes.add(expediente);
        return expediente;
    }

    public List<ExpedientePersona> listar() {
        return new ArrayList<>(expedientes);
    }
}