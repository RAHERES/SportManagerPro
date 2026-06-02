package com.example.sportmanagerpro.planificacion.model;

import java.util.UUID;

/**
 * Representa una fila personalizada agregada manualmente al plan gráfico.
 */
public class FilaPlanGraficoPersonalizada {

    private String id;
    private String nombre;
    private String colorTitulo;
    private boolean editable;

    public FilaPlanGraficoPersonalizada() {
        this.id = UUID.randomUUID().toString();
        this.nombre = "Nueva fila";
        this.colorTitulo = "#ffffff";
        this.editable = true;
    }

    public FilaPlanGraficoPersonalizada(String nombre) {
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.colorTitulo = "#ffffff";
        this.editable = true;
    }

    public FilaPlanGraficoPersonalizada(String id, String nombre, String colorTitulo, boolean editable) {
        this.id = id;
        this.nombre = nombre;
        this.colorTitulo = colorTitulo;
        this.editable = editable;
    }

    public String getClaveFila() {
        return "CUSTOM_" + id;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getColorTitulo() {
        return colorTitulo;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setColorTitulo(String colorTitulo) {
        this.colorTitulo = colorTitulo;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }
}