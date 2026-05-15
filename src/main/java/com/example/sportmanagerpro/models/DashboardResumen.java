package com.example.sportmanagerpro.models;

public class DashboardResumen {

    private int totalDeportistas;
    private int deportistasActivas;
    private int totalCategorias;
    private int totalEntrenamientos;
    private int totalPartidos;
    private int victorias;
    private int empates;
    private int derrotas;
    private int lesionesActivas;
    private double porcentajeAsistencia;

    public int getTotalDeportistas() { return totalDeportistas; }
    public void setTotalDeportistas(int totalDeportistas) { this.totalDeportistas = totalDeportistas; }

    public int getDeportistasActivas() { return deportistasActivas; }
    public void setDeportistasActivas(int deportistasActivas) { this.deportistasActivas = deportistasActivas; }

    public int getTotalCategorias() { return totalCategorias; }
    public void setTotalCategorias(int totalCategorias) { this.totalCategorias = totalCategorias; }

    public int getTotalEntrenamientos() { return totalEntrenamientos; }
    public void setTotalEntrenamientos(int totalEntrenamientos) { this.totalEntrenamientos = totalEntrenamientos; }

    public int getTotalPartidos() { return totalPartidos; }
    public void setTotalPartidos(int totalPartidos) { this.totalPartidos = totalPartidos; }

    public int getVictorias() { return victorias; }
    public void setVictorias(int victorias) { this.victorias = victorias; }

    public int getEmpates() { return empates; }
    public void setEmpates(int empates) { this.empates = empates; }

    public int getDerrotas() { return derrotas; }
    public void setDerrotas(int derrotas) { this.derrotas = derrotas; }

    public int getLesionesActivas() { return lesionesActivas; }
    public void setLesionesActivas(int lesionesActivas) { this.lesionesActivas = lesionesActivas; }

    public double getPorcentajeAsistencia() { return porcentajeAsistencia; }
    public void setPorcentajeAsistencia(double porcentajeAsistencia) { this.porcentajeAsistencia = porcentajeAsistencia; }
}