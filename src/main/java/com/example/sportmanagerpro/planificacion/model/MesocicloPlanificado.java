package com.example.sportmanagerpro.planificacion.model;

import com.example.sportmanagerpro.planificacion.enums.TipoMesociclo;
import com.example.sportmanagerpro.planificacion.enums.TipoMicrociclo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un mesociclo dentro del plan gráfico.
 */
public class MesocicloPlanificado {

    private TipoMesociclo tipoMesociclo;
    private String nombre;
    private int semanaInicio;
    private int duracionSemanas;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String colorHex;

    private String objetivo;
    private String capacidadesPrioritarias;

    private double porcentajePreparacionFisica = 70;
    private double porcentajePreparacionTecnicoTactica = 30;

    private double porcentajeAerobico = 45;
    private double porcentajeFuerza = 55;
    private double porcentajeComplejos = 100;

    private List<MicrocicloMesocicloConfig> configuracionMicrociclos = new ArrayList<>();

    public MesocicloPlanificado(TipoMesociclo tipoMesociclo,
                                String nombre,
                                int semanaInicio,
                                int duracionSemanas,
                                LocalDate fechaInicio,
                                LocalDate fechaFin,
                                String colorHex) {
        this.tipoMesociclo = tipoMesociclo;
        this.nombre = nombre;
        this.semanaInicio = semanaInicio;
        this.duracionSemanas = duracionSemanas;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.colorHex = colorHex;
        this.objetivo = "";
        this.capacidadesPrioritarias = "";
        ajustarConfiguracionMicrociclos();
    }

    public void ajustarConfiguracionMicrociclos() {
        while (configuracionMicrociclos.size() < duracionSemanas) {
            int index = configuracionMicrociclos.size();

            TipoMicrociclo tipo = switch (index) {
                case 0 -> TipoMicrociclo.AJUSTE;
                default -> index == duracionSemanas - 1
                        ? TipoMicrociclo.RECUPERACION
                        : TipoMicrociclo.CARGA;
            };

            double porcentaje = switch (tipo) {
                case AJUSTE -> 60;
                case CARGA -> 70;
                case IMPACTO -> 75;
                case RECUPERACION -> 50;
                case PRECOMPETITIVO -> 60;
                case COMPETENCIA -> 40;
            };

            configuracionMicrociclos.add(
                    new MicrocicloMesocicloConfig(tipo, porcentaje, 5, 120)
            );
        }

        while (configuracionMicrociclos.size() > duracionSemanas) {
            configuracionMicrociclos.remove(configuracionMicrociclos.size() - 1);
        }
    }

    public int getSemanaFin() {
        return semanaInicio + duracionSemanas - 1;
    }

    public TipoMesociclo getTipoMesociclo() {
        return tipoMesociclo;
    }

    public void setTipoMesociclo(TipoMesociclo tipoMesociclo) {
        this.tipoMesociclo = tipoMesociclo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getSemanaInicio() {
        return semanaInicio;
    }

    public void setSemanaInicio(int semanaInicio) {
        this.semanaInicio = semanaInicio;
    }

    public int getDuracionSemanas() {
        return duracionSemanas;
    }

    public void setDuracionSemanas(int duracionSemanas) {
        this.duracionSemanas = duracionSemanas;
        ajustarConfiguracionMicrociclos();
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getColorHex() {
        return colorHex;
    }

    public void setColorHex(String colorHex) {
        this.colorHex = colorHex;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getCapacidadesPrioritarias() {
        return capacidadesPrioritarias;
    }

    public void setCapacidadesPrioritarias(String capacidadesPrioritarias) {
        this.capacidadesPrioritarias = capacidadesPrioritarias;
    }

    public double getPorcentajePreparacionFisica() {
        return porcentajePreparacionFisica;
    }

    public void setPorcentajePreparacionFisica(double porcentajePreparacionFisica) {
        this.porcentajePreparacionFisica = porcentajePreparacionFisica;
    }

    public double getPorcentajePreparacionTecnicoTactica() {
        return porcentajePreparacionTecnicoTactica;
    }

    public void setPorcentajePreparacionTecnicoTactica(double porcentajePreparacionTecnicoTactica) {
        this.porcentajePreparacionTecnicoTactica = porcentajePreparacionTecnicoTactica;
    }

    public double getPorcentajeAerobico() {
        return porcentajeAerobico;
    }

    public void setPorcentajeAerobico(double porcentajeAerobico) {
        this.porcentajeAerobico = porcentajeAerobico;
    }

    public double getPorcentajeFuerza() {
        return porcentajeFuerza;
    }

    public void setPorcentajeFuerza(double porcentajeFuerza) {
        this.porcentajeFuerza = porcentajeFuerza;
    }

    public double getPorcentajeComplejos() {
        return porcentajeComplejos;
    }

    public void setPorcentajeComplejos(double porcentajeComplejos) {
        this.porcentajeComplejos = porcentajeComplejos;
    }

    public List<MicrocicloMesocicloConfig> getConfiguracionMicrociclos() {
        return configuracionMicrociclos;
    }

    public void setConfiguracionMicrociclos(List<MicrocicloMesocicloConfig> configuracionMicrociclos) {
        this.configuracionMicrociclos = configuracionMicrociclos;
        ajustarConfiguracionMicrociclos();
    }
}