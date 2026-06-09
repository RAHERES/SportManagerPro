    package com.example.sportmanagerpro.planificacion.enums;

/**
 * Clasifica una cualidad o contenido dentro de los aspectos de la preparación.
 */
public enum TipoPreparacion {

    PFG("P. Física general"),
    PFE("P. Física especial"),
    PTT("P. Técnico-táctica"),
    PT("P. Teórica"),
    PPS("P. Psicológica");

    private final String nombreVisible;

    TipoPreparacion(String nombreVisible) {
        this.nombreVisible = nombreVisible;
    }

    public String getNombreVisible() {
        return nombreVisible;
    }

    @Override
    public String toString() {
        return nombreVisible;
    }
}