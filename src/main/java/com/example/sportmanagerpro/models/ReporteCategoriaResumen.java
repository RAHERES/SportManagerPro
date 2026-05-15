package com.example.sportmanagerpro.models;

public class ReporteCategoriaResumen {

    private int totalDeportistas;
    private int deportistasActivas;
    private int entrenamientos;
    private int partidos;
    private int victorias;
    private int empates;
    private int derrotas;
    private int lesionesActivas;
    private int evaluacionesFisicas;
    private double porcentajeAsistencia;

    public int getTotalDeportistas() { return totalDeportistas; }
    public void setTotalDeportistas(int totalDeportistas) { this.totalDeportistas = totalDeportistas; }

    public int getDeportistasActivas() { return deportistasActivas; }
    public void setDeportistasActivas(int deportistasActivas) { this.deportistasActivas = deportistasActivas; }

    public int getEntrenamientos() { return entrenamientos; }
    public void setEntrenamientos(int entrenamientos) { this.entrenamientos = entrenamientos; }

    public int getPartidos() { return partidos; }
    public void setPartidos(int partidos) { this.partidos = partidos; }

    public int getVictorias() { return victorias; }
    public void setVictorias(int victorias) { this.victorias = victorias; }

    public int getEmpates() { return empates; }
    public void setEmpates(int empates) { this.empates = empates; }

    public int getDerrotas() { return derrotas; }
    public void setDerrotas(int derrotas) { this.derrotas = derrotas; }

    public int getLesionesActivas() { return lesionesActivas; }
    public void setLesionesActivas(int lesionesActivas) { this.lesionesActivas = lesionesActivas; }

    public int getEvaluacionesFisicas() { return evaluacionesFisicas; }
    public void setEvaluacionesFisicas(int evaluacionesFisicas) { this.evaluacionesFisicas = evaluacionesFisicas; }

    public double getPorcentajeAsistencia() { return porcentajeAsistencia; }
    public void setPorcentajeAsistencia(double porcentajeAsistencia) { this.porcentajeAsistencia = porcentajeAsistencia; }
}