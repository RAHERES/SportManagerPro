package com.example.sportmanagerpro.sportnutri1.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

public class ExpedientePersona {

    private String idExpediente;
    private String fechaAlta;
    private String estadoExpediente;
    private String profesionalResponsable;
    private String referidoPor;
    private String consentimiento;
    private String observacionesGenerales;

    private TipoPersona tipoPersona;
    private EstadoFisiologico estadoFisiologico;
    private NivelActividad nivelActividad;

    private String nombres;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String fechaNacimiento;
    private String edad;
    private String sexo;
    private String estadoCivil;
    private String curp;
    private String telefono;
    private String correo;
    private String direccion;
    private String escolaridad;
    private String gradoSemestre;
    private String ocupacion;
    private String contactoEmergencia;
    private String telefonoEmergencia;
    private String parentesco;

    private String practicaDeporte;
    private String deportePrincipal;
    private String posicion;
    private String categoria;
    private String anosPracticando;
    private String entrenamientosSemana;

    private String peso;
    private String talla;
    private String imc;
    private String cintura;
    private String cadera;

    private String motivoConsulta;
    private String antecedentes;
    private String patologias;
    private String medicamentos;
    private String alergias;

    private String alimentosHabituales;
    private String alimentosRechazados;
    private String consumoAgua;
    private String objetivoNutricional;

    private String objetivoEntrenamiento;
    private String diasEntrenamiento;
    private String restricciones;
    private String lesionesPrevias;

    private final Set<String> modulosActivos = new LinkedHashSet<>();
    private LocalDate fechaCreacion = LocalDate.now();
    private LocalDateTime ultimaActualizacion = LocalDateTime.now();

    public String getNombreCompleto() {
        return unir(nombres, apellidoPaterno, apellidoMaterno);
    }

    private String unir(String... valores) {
        StringBuilder sb = new StringBuilder();
        for (String v : valores) {
            if (v != null && !v.isBlank()) {
                sb.append(v).append(" ");
            }
        }
        return sb.toString().trim();
    }

    public String getIdExpediente() { return idExpediente; }
    public void setIdExpediente(String idExpediente) { this.idExpediente = idExpediente; }

    public String getFechaAlta() { return fechaAlta; }
    public void setFechaAlta(String fechaAlta) { this.fechaAlta = fechaAlta; }

    public String getEstadoExpediente() { return estadoExpediente; }
    public void setEstadoExpediente(String estadoExpediente) { this.estadoExpediente = estadoExpediente; }

    public String getProfesionalResponsable() { return profesionalResponsable; }
    public void setProfesionalResponsable(String profesionalResponsable) { this.profesionalResponsable = profesionalResponsable; }

    public String getReferidoPor() { return referidoPor; }
    public void setReferidoPor(String referidoPor) { this.referidoPor = referidoPor; }

    public String getConsentimiento() { return consentimiento; }
    public void setConsentimiento(String consentimiento) { this.consentimiento = consentimiento; }

    public String getObservacionesGenerales() { return observacionesGenerales; }
    public void setObservacionesGenerales(String observacionesGenerales) { this.observacionesGenerales = observacionesGenerales; }

    public TipoPersona getTipoPersona() { return tipoPersona; }
    public void setTipoPersona(TipoPersona tipoPersona) { this.tipoPersona = tipoPersona; }

    public EstadoFisiologico getEstadoFisiologico() { return estadoFisiologico; }
    public void setEstadoFisiologico(EstadoFisiologico estadoFisiologico) { this.estadoFisiologico = estadoFisiologico; }

    public NivelActividad getNivelActividad() { return nivelActividad; }
    public void setNivelActividad(NivelActividad nivelActividad) { this.nivelActividad = nivelActividad; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidoPaterno() { return apellidoPaterno; }
    public void setApellidoPaterno(String apellidoPaterno) { this.apellidoPaterno = apellidoPaterno; }

    public String getApellidoMaterno() { return apellidoMaterno; }
    public void setApellidoMaterno(String apellidoMaterno) { this.apellidoMaterno = apellidoMaterno; }

    public String getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getEdad() { return edad; }
    public void setEdad(String edad) { this.edad = edad; }

    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }

    public String getEstadoCivil() { return estadoCivil; }
    public void setEstadoCivil(String estadoCivil) { this.estadoCivil = estadoCivil; }

    public String getCurp() { return curp; }
    public void setCurp(String curp) { this.curp = curp; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getEscolaridad() { return escolaridad; }
    public void setEscolaridad(String escolaridad) { this.escolaridad = escolaridad; }

    public String getGradoSemestre() { return gradoSemestre; }
    public void setGradoSemestre(String gradoSemestre) { this.gradoSemestre = gradoSemestre; }

    public String getOcupacion() { return ocupacion; }
    public void setOcupacion(String ocupacion) { this.ocupacion = ocupacion; }

    public String getContactoEmergencia() { return contactoEmergencia; }
    public void setContactoEmergencia(String contactoEmergencia) { this.contactoEmergencia = contactoEmergencia; }

    public String getTelefonoEmergencia() { return telefonoEmergencia; }
    public void setTelefonoEmergencia(String telefonoEmergencia) { this.telefonoEmergencia = telefonoEmergencia; }

    public String getParentesco() { return parentesco; }
    public void setParentesco(String parentesco) { this.parentesco = parentesco; }

    public String getPracticaDeporte() { return practicaDeporte; }
    public void setPracticaDeporte(String practicaDeporte) { this.practicaDeporte = practicaDeporte; }

    public String getDeportePrincipal() { return deportePrincipal; }
    public void setDeportePrincipal(String deportePrincipal) { this.deportePrincipal = deportePrincipal; }

    public String getPosicion() { return posicion; }
    public void setPosicion(String posicion) { this.posicion = posicion; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getAnosPracticando() { return anosPracticando; }
    public void setAnosPracticando(String anosPracticando) { this.anosPracticando = anosPracticando; }

    public String getEntrenamientosSemana() { return entrenamientosSemana; }
    public void setEntrenamientosSemana(String entrenamientosSemana) { this.entrenamientosSemana = entrenamientosSemana; }

    public String getPeso() { return peso; }
    public void setPeso(String peso) { this.peso = peso; }

    public String getTalla() { return talla; }
    public void setTalla(String talla) { this.talla = talla; }

    public String getImc() { return imc; }
    public void setImc(String imc) { this.imc = imc; }

    public String getCintura() { return cintura; }
    public void setCintura(String cintura) { this.cintura = cintura; }

    public String getCadera() { return cadera; }
    public void setCadera(String cadera) { this.cadera = cadera; }

    public String getMotivoConsulta() { return motivoConsulta; }
    public void setMotivoConsulta(String motivoConsulta) { this.motivoConsulta = motivoConsulta; }

    public String getAntecedentes() { return antecedentes; }
    public void setAntecedentes(String antecedentes) { this.antecedentes = antecedentes; }

    public String getPatologias() { return patologias; }
    public void setPatologias(String patologias) { this.patologias = patologias; }

    public String getMedicamentos() { return medicamentos; }
    public void setMedicamentos(String medicamentos) { this.medicamentos = medicamentos; }

    public String getAlergias() { return alergias; }
    public void setAlergias(String alergias) { this.alergias = alergias; }

    public String getAlimentosHabituales() { return alimentosHabituales; }
    public void setAlimentosHabituales(String alimentosHabituales) { this.alimentosHabituales = alimentosHabituales; }

    public String getAlimentosRechazados() { return alimentosRechazados; }
    public void setAlimentosRechazados(String alimentosRechazados) { this.alimentosRechazados = alimentosRechazados; }

    public String getConsumoAgua() { return consumoAgua; }
    public void setConsumoAgua(String consumoAgua) { this.consumoAgua = consumoAgua; }

    public String getObjetivoNutricional() { return objetivoNutricional; }
    public void setObjetivoNutricional(String objetivoNutricional) { this.objetivoNutricional = objetivoNutricional; }

    public String getObjetivoEntrenamiento() { return objetivoEntrenamiento; }
    public void setObjetivoEntrenamiento(String objetivoEntrenamiento) { this.objetivoEntrenamiento = objetivoEntrenamiento; }

    public String getDiasEntrenamiento() { return diasEntrenamiento; }
    public void setDiasEntrenamiento(String diasEntrenamiento) { this.diasEntrenamiento = diasEntrenamiento; }

    public String getRestricciones() { return restricciones; }
    public void setRestricciones(String restricciones) { this.restricciones = restricciones; }

    public String getLesionesPrevias() { return lesionesPrevias; }
    public void setLesionesPrevias(String lesionesPrevias) { this.lesionesPrevias = lesionesPrevias; }

    public Set<String> getModulosActivos() { return modulosActivos; }

    public LocalDate getFechaCreacion() { return fechaCreacion; }

    public LocalDateTime getUltimaActualizacion() { return ultimaActualizacion; }
    public void setUltimaActualizacion(LocalDateTime ultimaActualizacion) { this.ultimaActualizacion = ultimaActualizacion; }
}