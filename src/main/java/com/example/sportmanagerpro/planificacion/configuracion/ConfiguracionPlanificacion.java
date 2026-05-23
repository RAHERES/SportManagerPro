package com.example.sportmanagerpro.planificacion.configuracion;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Guarda los parámetros previos necesarios antes de construir el plan gráfico.
 */
public class ConfiguracionPlanificacion {

    private String nombreTemporada;
    private String deporte;
    private String categoria;
    private String rama;
    private String nivelCompetitivo;
    private String objetivoGeneral;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    private String modeloPeriodizacion;

    private double porcentajePreparatorio;
    private double porcentajeCompetitivo;
    private double porcentajeTransitorio;

    private double porcentajePreparacionFisica;
    private double porcentajePreparacionTecnicoTactica;
    private double porcentajePreparacionTeorica;
    private double porcentajePreparacionPsicologica;

    private String tipoProgresionCarga;
    private boolean reducirCargaAntesCompetencia;
    private boolean permitirAjustesManuales;

    private final Map<DayOfWeek, DiaEntrenamientoConfig> diasEntrenamiento =
            new EnumMap<>(DayOfWeek.class);

    public ConfiguracionPlanificacion() {
        nombreTemporada = "Temporada 2026";
        deporte = "Fútbol";
        categoria = "Sub 17 Femenil";
        rama = "Femenil";
        nivelCompetitivo = "Escolar competitivo";
        objetivoGeneral = "Preparar al equipo para competir con mejor rendimiento físico, técnico y táctico.";

        fechaInicio = LocalDate.now();
        fechaFin = LocalDate.now().plusMonths(6);

        modeloPeriodizacion = "TRADICIONAL";

        porcentajePreparatorio = 50;
        porcentajeCompetitivo = 40;
        porcentajeTransitorio = 10;

        porcentajePreparacionFisica = 35;
        porcentajePreparacionTecnicoTactica = 45;
        porcentajePreparacionTeorica = 10;
        porcentajePreparacionPsicologica = 10;

        tipoProgresionCarga = "ONDULANTE";
        reducirCargaAntesCompetencia = true;
        permitirAjustesManuales = true;

        inicializarDias();
    }

    private final List<CompetenciaPlanificada> competencias = new ArrayList<>();

    public List<CompetenciaPlanificada> getCompetencias() {
        return competencias;
    }

    public void agregarCompetencia(CompetenciaPlanificada competencia) {
        competencias.add(competencia);
    }

    public void eliminarCompetencia(CompetenciaPlanificada competencia) {
        competencias.remove(competencia);
    }

    private void inicializarDias() {
        for (DayOfWeek dia : DayOfWeek.values()) {
            diasEntrenamiento.put(dia, new DiaEntrenamientoConfig(
                    dia,
                    false,
                    LocalTime.of(16, 30),
                    90,
                    1
            ));
        }

        diasEntrenamiento.get(DayOfWeek.TUESDAY).setEntrena(true);
        diasEntrenamiento.get(DayOfWeek.THURSDAY).setEntrena(true);
        diasEntrenamiento.get(DayOfWeek.SATURDAY).setEntrena(true);
        diasEntrenamiento.get(DayOfWeek.SATURDAY).setHoraInicio(LocalTime.of(14, 0));
        diasEntrenamiento.get(DayOfWeek.SATURDAY).setDuracionMinutos(120);
    }

    public int getDuracionTotalDias() {
        return (int) java.time.temporal.ChronoUnit.DAYS.between(fechaInicio, fechaFin) + 1;
    }

    public int getSesionesSemanales() {
        return diasEntrenamiento.values()
                .stream()
                .filter(DiaEntrenamientoConfig::isEntrena)
                .mapToInt(DiaEntrenamientoConfig::getUnidadesEntrenamiento)
                .sum();
    }

    public int getMinutosSemanales() {
        return diasEntrenamiento.values()
                .stream()
                .filter(DiaEntrenamientoConfig::isEntrena)
                .mapToInt(d -> d.getDuracionMinutos() * d.getUnidadesEntrenamiento())
                .sum();
    }

    public String getNombreTemporada() {
        return nombreTemporada;
    }

    public void setNombreTemporada(String nombreTemporada) {
        this.nombreTemporada = nombreTemporada;
    }

    public String getDeporte() {
        return deporte;
    }

    public void setDeporte(String deporte) {
        this.deporte = deporte;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getRama() {
        return rama;
    }

    public void setRama(String rama) {
        this.rama = rama;
    }

    public String getNivelCompetitivo() {
        return nivelCompetitivo;
    }

    public void setNivelCompetitivo(String nivelCompetitivo) {
        this.nivelCompetitivo = nivelCompetitivo;
    }

    public String getObjetivoGeneral() {
        return objetivoGeneral;
    }

    public void setObjetivoGeneral(String objetivoGeneral) {
        this.objetivoGeneral = objetivoGeneral;
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

    public String getModeloPeriodizacion() {
        return modeloPeriodizacion;
    }

    public void setModeloPeriodizacion(String modeloPeriodizacion) {
        this.modeloPeriodizacion = modeloPeriodizacion;
    }

    public double getPorcentajePreparatorio() {
        return porcentajePreparatorio;
    }

    public void setPorcentajePreparatorio(double porcentajePreparatorio) {
        this.porcentajePreparatorio = porcentajePreparatorio;
    }

    public double getPorcentajeCompetitivo() {
        return porcentajeCompetitivo;
    }

    public void setPorcentajeCompetitivo(double porcentajeCompetitivo) {
        this.porcentajeCompetitivo = porcentajeCompetitivo;
    }

    public double getPorcentajeTransitorio() {
        return porcentajeTransitorio;
    }

    public void setPorcentajeTransitorio(double porcentajeTransitorio) {
        this.porcentajeTransitorio = porcentajeTransitorio;
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

    public double getPorcentajePreparacionTeorica() {
        return porcentajePreparacionTeorica;
    }

    public void setPorcentajePreparacionTeorica(double porcentajePreparacionTeorica) {
        this.porcentajePreparacionTeorica = porcentajePreparacionTeorica;
    }

    public double getPorcentajePreparacionPsicologica() {
        return porcentajePreparacionPsicologica;
    }

    public void setPorcentajePreparacionPsicologica(double porcentajePreparacionPsicologica) {
        this.porcentajePreparacionPsicologica = porcentajePreparacionPsicologica;
    }

    public String getTipoProgresionCarga() {
        return tipoProgresionCarga;
    }

    public void setTipoProgresionCarga(String tipoProgresionCarga) {
        this.tipoProgresionCarga = tipoProgresionCarga;
    }

    public boolean isReducirCargaAntesCompetencia() {
        return reducirCargaAntesCompetencia;
    }

    public void setReducirCargaAntesCompetencia(boolean reducirCargaAntesCompetencia) {
        this.reducirCargaAntesCompetencia = reducirCargaAntesCompetencia;
    }

    public boolean isPermitirAjustesManuales() {
        return permitirAjustesManuales;
    }

    public void setPermitirAjustesManuales(boolean permitirAjustesManuales) {
        this.permitirAjustesManuales = permitirAjustesManuales;
    }

    public Map<DayOfWeek, DiaEntrenamientoConfig> getDiasEntrenamiento() {
        return diasEntrenamiento;
    }
}