package com.example.sportmanagerpro.planificacion.model;

import javafx.scene.paint.Color;

/**
 * Representa una celda editable dentro del plan gráfico.
 * Guarda el texto, color, fila, columna y tipo de dato que representa.
 */
public class CeldaPlanGrafico {

    private String fila;
    private int semana;
    private String valor;
    private String colorHex;
    private boolean editable;

    public CeldaPlanGrafico(String fila, int semana, String valor, String colorHex, boolean editable) {
        this.fila = fila;
        this.semana = semana;
        this.valor = valor;
        this.colorHex = colorHex;
        this.editable = editable;
    }

    public String getFila() {
        return fila;
    }

    public int getSemana() {
        return semana;
    }

    public String getValor() {
        return valor;
    }

    public String getColorHex() {
        return colorHex;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public void setColorHex(String colorHex) {
        this.colorHex = colorHex;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }
}