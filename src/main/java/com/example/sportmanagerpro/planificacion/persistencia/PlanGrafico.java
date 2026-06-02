package com.example.sportmanagerpro.planificacion.persistencia;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa un plan gráfico completo para guardar y cargar en formato JSON.
 */
public class PlanGrafico {

    public String id;
    public String nombrePlan;

    public String fechaInicio;
    public String fechaFin;

    public String deporte;
    public String categoria;
    public String objetivo;
    public String tipoPeriodizacion;

    public boolean modoPeriodosManual;

    public String fechaCreacion;
    public String fechaUltimaModificacion;

    public List<PeriodoDTO> periodos = new ArrayList<>();
    public List<MesocicloDTO> mesociclos = new ArrayList<>();
    public List<MicrocicloDTO> microciclos = new ArrayList<>();
    public List<SesionDTO> sesiones = new ArrayList<>();
    public List<CompetenciaDTO> competencias = new ArrayList<>();
    public List<CeldaDTO> celdas = new ArrayList<>();

    public static class PeriodoDTO {
        public String tipo;
        public int semanaInicio;
        public int semanaFin;
        public double porcentaje;
    }

    public static class MesocicloDTO {
        public String tipo;
        public String nombre;
        public int semanaInicio;
        public int duracion;
        public String color;
    }

    public static class MicrocicloDTO {
        public String tipo;
        public String nombre;
        public int semanaInicio;
        public int duracion;
        public String color;
    }

    public static class SesionDTO {
        public int semana;
        public String diaSemana;
        public String horaInicio;
        public int duracionMinutos;
        public boolean extra;
        public String observaciones;
    }

    public static class CompetenciaDTO {
        public String nombre;
        public String tipoCompetencia;
        public String fechaInicio;
        public String fechaFin;
        public String fase;
        public String sede;
        public String objetivo;
        public int prioridad;
        public boolean competenciaClave;
        public String observaciones;
    }

    public static class CeldaDTO {
        public String fila;
        public int semana;
        public String valor;
        public String colorHex;
        public boolean editable;
    }

    public List<FilaPersonalizadaDTO> filasPersonalizadas = new ArrayList<>();

    public static class FilaPersonalizadaDTO {
        public String id;
        public String nombre;
        public String colorTitulo;
        public boolean editable;
    }
}