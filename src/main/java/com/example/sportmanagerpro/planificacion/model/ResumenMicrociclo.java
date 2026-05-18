package com.example.sportmanagerpro.planificacion.model;

/**
 * Resultado del análisis de un microciclo.
 */
public class ResumenMicrociclo {

    private int minutosPlanificados;
    private int minutosRealizados;
    private double cargaPlanificada;
    private double cargaReal;
    private double cumplimiento;

    public int getMinutosPlanificados() {
        return minutosPlanificados;
    }

    public int getMinutosRealizados() {
        return minutosRealizados;
    }

    public double getCargaPlanificada() {
        return cargaPlanificada;
    }

    public double getCargaReal() {
        return cargaReal;
    }

    public double getCumplimiento() {
        return cumplimiento;
    }

    public void setMinutosPlanificados(int minutosPlanificados) {
        this.minutosPlanificados = minutosPlanificados;
    }

    public void setMinutosRealizados(int minutosRealizados) {
        this.minutosRealizados = minutosRealizados;
    }

    public void setCargaPlanificada(double cargaPlanificada) {
        this.cargaPlanificada = cargaPlanificada;
    }

    public void setCargaReal(double cargaReal) {
        this.cargaReal = cargaReal;
    }

    public void setCumplimiento(double cumplimiento) {
        this.cumplimiento = cumplimiento;
    }
}