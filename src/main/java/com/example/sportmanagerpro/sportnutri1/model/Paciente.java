package com.example.sportmanagerpro.sportnutri1.model;

import java.time.LocalDate;

/**
 * Modelo de datos para el expediente ambulatorio nutricional.
 */
public class Paciente {
    private long id;
    private String expediente;
    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private LocalDate fechaNacimiento;
    private Integer edad;
    private String sexo;
    private String curp;
    private String telefono;
    private String correo;
    private String estadoCivil;
    private String ocupacion;
    private String escolaridad;
    private String domicilio;
    private String nombreEstablecimiento;
    private String razonSocial;
    private String tipoServicio;
    private String responsable;
    private String cedulaProfesional;
    private LocalDate fechaApertura;
    private String antecedentesHeredoFamiliares;
    private String antecedentesPersonalesPatologicos;
    private String antecedentesPersonalesNoPatologicos;
    private String padecimientoActual;
    private String interrogatorioSistemas;
    private String exploracionFisica;
    private String estudiosPrevios;
    private String diagnosticos;
    private String pronostico;
    private String indicacionTerapeutica;
    private String notasAdicionales;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getExpediente() { return expediente; }
    public void setExpediente(String expediente) { this.expediente = expediente; }
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public String getApellidoPaterno() { return apellidoPaterno; }
    public void setApellidoPaterno(String apellidoPaterno) { this.apellidoPaterno = apellidoPaterno; }
    public String getApellidoMaterno() { return apellidoMaterno; }
    public void setApellidoMaterno(String apellidoMaterno) { this.apellidoMaterno = apellidoMaterno; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public Integer getEdad() { return edad; }
    public void setEdad(Integer edad) { this.edad = edad; }
    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }
    public String getCurp() { return curp; }
    public void setCurp(String curp) { this.curp = curp; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getEstadoCivil() { return estadoCivil; }
    public void setEstadoCivil(String estadoCivil) { this.estadoCivil = estadoCivil; }
    public String getOcupacion() { return ocupacion; }
    public void setOcupacion(String ocupacion) { this.ocupacion = ocupacion; }
    public String getEscolaridad() { return escolaridad; }
    public void setEscolaridad(String escolaridad) { this.escolaridad = escolaridad; }
    public String getDomicilio() { return domicilio; }
    public void setDomicilio(String domicilio) { this.domicilio = domicilio; }
    public String getNombreEstablecimiento() { return nombreEstablecimiento; }
    public void setNombreEstablecimiento(String nombreEstablecimiento) { this.nombreEstablecimiento = nombreEstablecimiento; }
    public String getRazonSocial() { return razonSocial; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }
    public String getTipoServicio() { return tipoServicio; }
    public void setTipoServicio(String tipoServicio) { this.tipoServicio = tipoServicio; }
    public String getResponsable() { return responsable; }
    public void setResponsable(String responsable) { this.responsable = responsable; }
    public String getCedulaProfesional() { return cedulaProfesional; }
    public void setCedulaProfesional(String cedulaProfesional) { this.cedulaProfesional = cedulaProfesional; }
    public LocalDate getFechaApertura() { return fechaApertura; }
    public void setFechaApertura(LocalDate fechaApertura) { this.fechaApertura = fechaApertura; }
    public String getAntecedentesHeredoFamiliares() { return antecedentesHeredoFamiliares; }
    public void setAntecedentesHeredoFamiliares(String antecedentesHeredoFamiliares) { this.antecedentesHeredoFamiliares = antecedentesHeredoFamiliares; }
    public String getAntecedentesPersonalesPatologicos() { return antecedentesPersonalesPatologicos; }
    public void setAntecedentesPersonalesPatologicos(String antecedentesPersonalesPatologicos) { this.antecedentesPersonalesPatologicos = antecedentesPersonalesPatologicos; }
    public String getAntecedentesPersonalesNoPatologicos() { return antecedentesPersonalesNoPatologicos; }
    public void setAntecedentesPersonalesNoPatologicos(String antecedentesPersonalesNoPatologicos) { this.antecedentesPersonalesNoPatologicos = antecedentesPersonalesNoPatologicos; }
    public String getPadecimientoActual() { return padecimientoActual; }
    public void setPadecimientoActual(String padecimientoActual) { this.padecimientoActual = padecimientoActual; }
    public String getInterrogatorioSistemas() { return interrogatorioSistemas; }
    public void setInterrogatorioSistemas(String interrogatorioSistemas) { this.interrogatorioSistemas = interrogatorioSistemas; }
    public String getExploracionFisica() { return exploracionFisica; }
    public void setExploracionFisica(String exploracionFisica) { this.exploracionFisica = exploracionFisica; }
    public String getEstudiosPrevios() { return estudiosPrevios; }
    public void setEstudiosPrevios(String estudiosPrevios) { this.estudiosPrevios = estudiosPrevios; }
    public String getDiagnosticos() { return diagnosticos; }
    public void setDiagnosticos(String diagnosticos) { this.diagnosticos = diagnosticos; }
    public String getPronostico() { return pronostico; }
    public void setPronostico(String pronostico) { this.pronostico = pronostico; }
    public String getIndicacionTerapeutica() { return indicacionTerapeutica; }
    public void setIndicacionTerapeutica(String indicacionTerapeutica) { this.indicacionTerapeutica = indicacionTerapeutica; }
    public String getNotasAdicionales() { return notasAdicionales; }
    public void setNotasAdicionales(String notasAdicionales) { this.notasAdicionales = notasAdicionales; }

    public String nombreCompleto() {
        return String.join(" ", n(nombres), n(apellidoPaterno), n(apellidoMaterno)).trim();
    }

    private String n(String value) { return value == null ? "" : value; }
}
