package com.example.sportmanagerpro.models;

/**
 * Representa una categoría deportiva dentro del sistema.
 */
public class Categoria {

    private int idCategoria;
    private String nombre;
    private String rangoEdad;
    private String descripcion;
    private boolean activa;

    public Categoria() {
    }

    public Categoria(int idCategoria, String nombre, String rangoEdad, String descripcion, boolean activa) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.rangoEdad = rangoEdad;
        this.descripcion = descripcion;
        this.activa = activa;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRangoEdad() {
        return rangoEdad;
    }

    public void setRangoEdad(String rangoEdad) {
        this.rangoEdad = rangoEdad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    @Override
    public String toString() {
        return nombre;
    }
}