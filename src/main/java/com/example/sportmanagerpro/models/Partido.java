package com.example.sportmanagerpro.models;

import java.time.LocalDate;

public class Partido {

    private int idPartido;
    private int idCompetencia;
    private int idCategoria;
    private LocalDate fecha;
    private String rival;
    private String sede;
    private String fase;
    private int marcadorFavor;
    private int marcadorContra;
    private String resultado;
    private String observaciones;

    public int getIdPartido() { return idPartido; }
    public void setIdPartido(int idPartido) { this.idPartido = idPartido; }

    public int getIdCompetencia() { return idCompetencia; }
    public void setIdCompetencia(int idCompetencia) { this.idCompetencia = idCompetencia; }

    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getRival() { return rival; }
    public void setRival(String rival) { this.rival = rival; }

    public String getSede() { return sede; }
    public void setSede(String sede) { this.sede = sede; }

    public String getFase() { return fase; }
    public void setFase(String fase) { this.fase = fase; }

    public int getMarcadorFavor() { return marcadorFavor; }
    public void setMarcadorFavor(int marcadorFavor) { this.marcadorFavor = marcadorFavor; }

    public int getMarcadorContra() { return marcadorContra; }
    public void setMarcadorContra(int marcadorContra) { this.marcadorContra = marcadorContra; }

    public String getResultado() { return resultado; }
    public void setResultado(String resultado) { this.resultado = resultado; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}