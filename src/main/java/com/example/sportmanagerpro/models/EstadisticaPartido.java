package com.example.sportmanagerpro.models;

public class EstadisticaPartido {

    private int idEstadistica;
    private int idPartido;
    private int idDeportista;
    private boolean titular;
    private int minutosJugados;
    private int goles;
    private int asistencias;
    private int tarjetasAmarillas;
    private int tarjetasRojas;
    private String observaciones;

    public int getIdEstadistica() { return idEstadistica; }
    public void setIdEstadistica(int idEstadistica) { this.idEstadistica = idEstadistica; }

    public int getIdPartido() { return idPartido; }
    public void setIdPartido(int idPartido) { this.idPartido = idPartido; }

    public int getIdDeportista() { return idDeportista; }
    public void setIdDeportista(int idDeportista) { this.idDeportista = idDeportista; }

    public boolean isTitular() { return titular; }
    public void setTitular(boolean titular) { this.titular = titular; }

    public int getMinutosJugados() { return minutosJugados; }
    public void setMinutosJugados(int minutosJugados) { this.minutosJugados = minutosJugados; }

    public int getGoles() { return goles; }
    public void setGoles(int goles) { this.goles = goles; }

    public int getAsistencias() { return asistencias; }
    public void setAsistencias(int asistencias) { this.asistencias = asistencias; }

    public int getTarjetasAmarillas() { return tarjetasAmarillas; }
    public void setTarjetasAmarillas(int tarjetasAmarillas) { this.tarjetasAmarillas = tarjetasAmarillas; }

    public int getTarjetasRojas() { return tarjetasRojas; }
    public void setTarjetasRojas(int tarjetasRojas) { this.tarjetasRojas = tarjetasRojas; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}