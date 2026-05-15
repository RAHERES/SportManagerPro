package com.example.sportmanagerpro.models;

public class HistorialDeportistaResumen {

    private int totalEntrenamientos;
    private int asistencias;
    private int faltas;
    private int retardos;
    private int partidosJugados;
    private int minutosJugados;
    private int goles;
    private int asistenciasPartido;

    public int getTotalEntrenamientos() { return totalEntrenamientos; }
    public void setTotalEntrenamientos(int totalEntrenamientos) { this.totalEntrenamientos = totalEntrenamientos; }

    public int getAsistencias() { return asistencias; }
    public void setAsistencias(int asistencias) { this.asistencias = asistencias; }

    public int getFaltas() { return faltas; }
    public void setFaltas(int faltas) { this.faltas = faltas; }

    public int getRetardos() { return retardos; }
    public void setRetardos(int retardos) { this.retardos = retardos; }

    public int getPartidosJugados() { return partidosJugados; }
    public void setPartidosJugados(int partidosJugados) { this.partidosJugados = partidosJugados; }

    public int getMinutosJugados() { return minutosJugados; }
    public void setMinutosJugados(int minutosJugados) { this.minutosJugados = minutosJugados; }

    public int getGoles() { return goles; }
    public void setGoles(int goles) { this.goles = goles; }

    public int getAsistenciasPartido() { return asistenciasPartido; }
    public void setAsistenciasPartido(int asistenciasPartido) { this.asistenciasPartido = asistenciasPartido; }
}