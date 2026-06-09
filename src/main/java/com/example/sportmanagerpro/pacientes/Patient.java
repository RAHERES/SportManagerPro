package com.example.sportmanagerpro.pacientes;

import java.time.LocalDate;

/**
 * Modelo de datos para el expediente básico de un paciente o cliente.
 *
 * <p>Esta clase concentra la información capturada en la pantalla de registro.
 * Se utiliza tanto para enviar datos a SQLite como para recuperar expedientes y
 * volver a cargarlos en la interfaz.</p>
 */
public class Patient {

    /** Identificador interno autoincremental en SQLite. */
    private Long id;

    /** Nombres del paciente. */
    private String nombres;

    /** Apellido paterno del paciente. */
    private String apellidoPaterno;

    /** Apellido materno del paciente. */
    private String apellidoMaterno;

    /** Fecha de nacimiento. */
    private LocalDate fechaNacimiento;

    /** Edad capturada o calculada externamente. */
    private Integer edad;

    /** Género seleccionado. */
    private String genero;

    /** Teléfono de contacto. */
    private String telefono;

    /** Correo electrónico. */
    private String correo;

    /** Estado civil. */
    private String estadoCivil;

    /** Objetivo principal del paciente. */
    private String objetivoPrincipal;

    /** Nivel de actividad física. */
    private String nivelActividad;

    /** Ocupación del paciente. */
    private String ocupacion;

    /** Canal por el que conoció el servicio. */
    private String comoNosConocio;

    /** Notas generales de primera consulta. */
    private String notasIniciales;

    /** Peso actual en kg. */
    private Double pesoActualKg;

    /** Estatura en cm. */
    private Double estaturaCm;

    /** Porcentaje de grasa corporal. */
    private Double porcentajeGrasa;

    /** IMC calculado automáticamente. */
    private Double imc;

    /** Circunferencia de cintura en cm. */
    private Double cinturaCm;

    /** Circunferencia de cadera en cm. */
    private Double caderaCm;

    /** Ruta local de la foto del paciente. */
    private String fotoPath;

    /** Estado del expediente. Puede ser BORRADOR o ACTIVO. */
    private String estadoRegistro;

    /** Fecha y hora de creación textual para simplificar compatibilidad. */
    private String creadoEn;

    public String getNombreCompleto() {
        return String.join(" ", safe(nombres), safe(apellidoPaterno), safe(apellidoMaterno)).trim();
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getEstadoCivil() { return estadoCivil; }
    public void setEstadoCivil(String estadoCivil) { this.estadoCivil = estadoCivil; }
    public String getObjetivoPrincipal() { return objetivoPrincipal; }
    public void setObjetivoPrincipal(String objetivoPrincipal) { this.objetivoPrincipal = objetivoPrincipal; }
    public String getNivelActividad() { return nivelActividad; }
    public void setNivelActividad(String nivelActividad) { this.nivelActividad = nivelActividad; }
    public String getOcupacion() { return ocupacion; }
    public void setOcupacion(String ocupacion) { this.ocupacion = ocupacion; }
    public String getComoNosConocio() { return comoNosConocio; }
    public void setComoNosConocio(String comoNosConocio) { this.comoNosConocio = comoNosConocio; }
    public String getNotasIniciales() { return notasIniciales; }
    public void setNotasIniciales(String notasIniciales) { this.notasIniciales = notasIniciales; }
    public Double getPesoActualKg() { return pesoActualKg; }
    public void setPesoActualKg(Double pesoActualKg) { this.pesoActualKg = pesoActualKg; }
    public Double getEstaturaCm() { return estaturaCm; }
    public void setEstaturaCm(Double estaturaCm) { this.estaturaCm = estaturaCm; }
    public Double getPorcentajeGrasa() { return porcentajeGrasa; }
    public void setPorcentajeGrasa(Double porcentajeGrasa) { this.porcentajeGrasa = porcentajeGrasa; }
    public Double getImc() { return imc; }
    public void setImc(Double imc) { this.imc = imc; }
    public Double getCinturaCm() { return cinturaCm; }
    public void setCinturaCm(Double cinturaCm) { this.cinturaCm = cinturaCm; }
    public Double getCaderaCm() { return caderaCm; }
    public void setCaderaCm(Double caderaCm) { this.caderaCm = caderaCm; }
    public String getFotoPath() { return fotoPath; }
    public void setFotoPath(String fotoPath) { this.fotoPath = fotoPath; }
    public String getEstadoRegistro() { return estadoRegistro; }
    public void setEstadoRegistro(String estadoRegistro) { this.estadoRegistro = estadoRegistro; }
    public String getCreadoEn() { return creadoEn; }
    public void setCreadoEn(String creadoEn) { this.creadoEn = creadoEn; }
}
