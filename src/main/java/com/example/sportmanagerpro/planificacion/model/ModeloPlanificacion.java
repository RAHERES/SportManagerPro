package com.example.sportmanagerpro.planificacion.model;


import com.example.sportmanagerpro.planificacion.enums.TipoModeloPlanificacion;

/**
 * Representa un modelo de planificación deportiva.
 */
public class ModeloPlanificacion {

    private Integer id;

    private String nombre;

    private TipoModeloPlanificacion tipo;

    private String descripcion;

    private boolean personalizado;

    public ModeloPlanificacion() {
    }

    public ModeloPlanificacion(Integer id,
                               String nombre,
                               TipoModeloPlanificacion tipo,
                               String descripcion,
                               boolean personalizado) {

        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.personalizado = personalizado;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoModeloPlanificacion getTipo() {
        return tipo;
    }

    public void setTipo(TipoModeloPlanificacion tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isPersonalizado() {
        return personalizado;
    }

    public void setPersonalizado(boolean personalizado) {
        this.personalizado = personalizado;
    }
}