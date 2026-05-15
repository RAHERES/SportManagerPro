    package com.example.sportmanagerpro.models;

import java.time.LocalDate;

public class Deportista {

    private int idDeportista;
    private String nombreCompleto;
    private LocalDate fechaNacimiento;
    private String sexo;
    private String gradoGrupo;
    private String posicionPrincipal;
    private String posicionSecundaria;
    private String piernaDominante;
    private String telefonoTutor;
    private int idCategoria;
    private boolean activo;
    private String observaciones;
    private LocalDate fechaRegistro;

    public int getIdDeportista() { return idDeportista; }
    public void setIdDeportista(int idDeportista) { this.idDeportista = idDeportista; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }

    public String getGradoGrupo() { return gradoGrupo; }
    public void setGradoGrupo(String gradoGrupo) { this.gradoGrupo = gradoGrupo; }

    public String getPosicionPrincipal() { return posicionPrincipal; }
    public void setPosicionPrincipal(String posicionPrincipal) { this.posicionPrincipal = posicionPrincipal; }

    public String getPosicionSecundaria() { return posicionSecundaria; }
    public void setPosicionSecundaria(String posicionSecundaria) { this.posicionSecundaria = posicionSecundaria; }

    public String getPiernaDominante() { return piernaDominante; }
    public void setPiernaDominante(String piernaDominante) { this.piernaDominante = piernaDominante; }

    public String getTelefonoTutor() { return telefonoTutor; }
    public void setTelefonoTutor(String telefonoTutor) { this.telefonoTutor = telefonoTutor; }

    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDate fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    @Override
    public String toString() {
        return nombreCompleto;
    }
}