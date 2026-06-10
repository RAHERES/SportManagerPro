package com.example.sportmanagerpro.sportnutri1.repository;

import com.example.sportmanagerpro.sportnutri1.model.Paciente;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio SQLite para guardar, buscar, actualizar y eliminar pacientes.
 */
public class PacienteRepository {
    private static final String URL = "jdbc:sqlite:sportnutri_pacientes_nom.db";

    public PacienteRepository() {
        crearTabla();
    }

    private Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    private void crearTabla() {
        String sql = """
                CREATE TABLE IF NOT EXISTS pacientes (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    expediente TEXT UNIQUE,
                    nombres TEXT NOT NULL,
                    apellido_paterno TEXT,
                    apellido_materno TEXT,
                    fecha_nacimiento TEXT,
                    edad INTEGER,
                    sexo TEXT,
                    curp TEXT,
                    telefono TEXT,
                    correo TEXT,
                    estado_civil TEXT,
                    ocupacion TEXT,
                    escolaridad TEXT,
                    domicilio TEXT,
                    nombre_establecimiento TEXT,
                    razon_social TEXT,
                    tipo_servicio TEXT,
                    responsable TEXT,
                    cedula_profesional TEXT,
                    fecha_apertura TEXT,
                    antecedentes_hf TEXT,
                    antecedentes_pp TEXT,
                    antecedentes_pnp TEXT,
                    padecimiento_actual TEXT,
                    interrogatorio_sistemas TEXT,
                    exploracion_fisica TEXT,
                    estudios_previos TEXT,
                    diagnosticos TEXT,
                    pronostico TEXT,
                    indicacion_terapeutica TEXT,
                    notas_adicionales TEXT
                )
                """;
        try (Connection c = conectar(); Statement st = c.createStatement()) {
            st.execute(sql);
        } catch (SQLException ex) {
            throw new IllegalStateException("No se pudo crear la tabla de pacientes", ex);
        }
    }

    public List<Paciente> buscar(String filtro) {
        List<Paciente> pacientes = new ArrayList<>();
        String q = filtro == null ? "" : filtro.trim().toLowerCase();
        String sql = """
                SELECT * FROM pacientes
                WHERE lower(coalesce(nombres,'') || ' ' || coalesce(apellido_paterno,'') || ' ' || coalesce(apellido_materno,'') || ' ' || coalesce(telefono,'') || ' ' || coalesce(expediente,'')) LIKE ?
                ORDER BY id DESC
                """;
        try (Connection c = conectar(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, "%" + q + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) pacientes.add(mapear(rs));
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("No se pudieron buscar pacientes", ex);
        }
        return pacientes;
    }

    public Paciente guardar(Paciente p) {
        if (p.getExpediente() == null || p.getExpediente().isBlank()) {
            p.setExpediente(generarExpediente());
        }
        String sql = """
                INSERT INTO pacientes (expediente,nombres,apellido_paterno,apellido_materno,fecha_nacimiento,edad,sexo,curp,telefono,correo,estado_civil,ocupacion,escolaridad,domicilio,nombre_establecimiento,razon_social,tipo_servicio,responsable,cedula_profesional,fecha_apertura,antecedentes_hf,antecedentes_pp,antecedentes_pnp,padecimiento_actual,interrogatorio_sistemas,exploracion_fisica,estudios_previos,diagnosticos,pronostico,indicacion_terapeutica,notas_adicionales)
                VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
                """;
        try (Connection c = conectar(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            llenar(ps, p, false);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) p.setId(keys.getLong(1));
            }
            return p;
        } catch (SQLException ex) {
            throw new IllegalStateException("No se pudo guardar el paciente", ex);
        }
    }

    public void actualizar(Paciente p) {
        String sql = """
                UPDATE pacientes SET expediente=?,nombres=?,apellido_paterno=?,apellido_materno=?,fecha_nacimiento=?,edad=?,sexo=?,curp=?,telefono=?,correo=?,estado_civil=?,ocupacion=?,escolaridad=?,domicilio=?,nombre_establecimiento=?,razon_social=?,tipo_servicio=?,responsable=?,cedula_profesional=?,fecha_apertura=?,antecedentes_hf=?,antecedentes_pp=?,antecedentes_pnp=?,padecimiento_actual=?,interrogatorio_sistemas=?,exploracion_fisica=?,estudios_previos=?,diagnosticos=?,pronostico=?,indicacion_terapeutica=?,notas_adicionales=?
                WHERE id=?
                """;
        try (Connection c = conectar(); PreparedStatement ps = c.prepareStatement(sql)) {
            llenar(ps, p, true);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new IllegalStateException("No se pudo actualizar el paciente", ex);
        }
    }

    public void eliminar(long id) {
        try (Connection c = conectar(); PreparedStatement ps = c.prepareStatement("DELETE FROM pacientes WHERE id=?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new IllegalStateException("No se pudo eliminar el paciente", ex);
        }
    }

    private String generarExpediente() {
        String sql = "SELECT COALESCE(MAX(id),0)+1 FROM pacientes";
        try (Connection c = conectar(); Statement st = c.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            int siguiente = rs.next() ? rs.getInt(1) : 1;
            return String.format("EXP-%06d", siguiente);
        } catch (SQLException ex) {
            return "EXP-" + System.currentTimeMillis();
        }
    }

    private void llenar(PreparedStatement ps, Paciente p, boolean update) throws SQLException {
        int i = 1;
        ps.setString(i++, p.getExpediente());
        ps.setString(i++, p.getNombres());
        ps.setString(i++, p.getApellidoPaterno());
        ps.setString(i++, p.getApellidoMaterno());
        ps.setString(i++, fecha(p.getFechaNacimiento()));
        if (p.getEdad() == null) ps.setNull(i++, Types.INTEGER); else ps.setInt(i++, p.getEdad());
        ps.setString(i++, p.getSexo());
        ps.setString(i++, p.getCurp());
        ps.setString(i++, p.getTelefono());
        ps.setString(i++, p.getCorreo());
        ps.setString(i++, p.getEstadoCivil());
        ps.setString(i++, p.getOcupacion());
        ps.setString(i++, p.getEscolaridad());
        ps.setString(i++, p.getDomicilio());
        ps.setString(i++, p.getNombreEstablecimiento());
        ps.setString(i++, p.getRazonSocial());
        ps.setString(i++, p.getTipoServicio());
        ps.setString(i++, p.getResponsable());
        ps.setString(i++, p.getCedulaProfesional());
        ps.setString(i++, fecha(p.getFechaApertura()));
        ps.setString(i++, p.getAntecedentesHeredoFamiliares());
        ps.setString(i++, p.getAntecedentesPersonalesPatologicos());
        ps.setString(i++, p.getAntecedentesPersonalesNoPatologicos());
        ps.setString(i++, p.getPadecimientoActual());
        ps.setString(i++, p.getInterrogatorioSistemas());
        ps.setString(i++, p.getExploracionFisica());
        ps.setString(i++, p.getEstudiosPrevios());
        ps.setString(i++, p.getDiagnosticos());
        ps.setString(i++, p.getPronostico());
        ps.setString(i++, p.getIndicacionTerapeutica());
        ps.setString(i++, p.getNotasAdicionales());
        if (update) ps.setLong(i, p.getId());
    }

    private Paciente mapear(ResultSet rs) throws SQLException {
        Paciente p = new Paciente();
        p.setId(rs.getLong("id"));
        p.setExpediente(rs.getString("expediente"));
        p.setNombres(rs.getString("nombres"));
        p.setApellidoPaterno(rs.getString("apellido_paterno"));
        p.setApellidoMaterno(rs.getString("apellido_materno"));
        p.setFechaNacimiento(parseFecha(rs.getString("fecha_nacimiento")));
        int edad = rs.getInt("edad"); p.setEdad(rs.wasNull() ? null : edad);
        p.setSexo(rs.getString("sexo")); p.setCurp(rs.getString("curp")); p.setTelefono(rs.getString("telefono"));
        p.setCorreo(rs.getString("correo")); p.setEstadoCivil(rs.getString("estado_civil")); p.setOcupacion(rs.getString("ocupacion"));
        p.setEscolaridad(rs.getString("escolaridad")); p.setDomicilio(rs.getString("domicilio"));
        p.setNombreEstablecimiento(rs.getString("nombre_establecimiento")); p.setRazonSocial(rs.getString("razon_social"));
        p.setTipoServicio(rs.getString("tipo_servicio")); p.setResponsable(rs.getString("responsable"));
        p.setCedulaProfesional(rs.getString("cedula_profesional")); p.setFechaApertura(parseFecha(rs.getString("fecha_apertura")));
        p.setAntecedentesHeredoFamiliares(rs.getString("antecedentes_hf"));
        p.setAntecedentesPersonalesPatologicos(rs.getString("antecedentes_pp"));
        p.setAntecedentesPersonalesNoPatologicos(rs.getString("antecedentes_pnp"));
        p.setPadecimientoActual(rs.getString("padecimiento_actual")); p.setInterrogatorioSistemas(rs.getString("interrogatorio_sistemas"));
        p.setExploracionFisica(rs.getString("exploracion_fisica")); p.setEstudiosPrevios(rs.getString("estudios_previos"));
        p.setDiagnosticos(rs.getString("diagnosticos")); p.setPronostico(rs.getString("pronostico"));
        p.setIndicacionTerapeutica(rs.getString("indicacion_terapeutica")); p.setNotasAdicionales(rs.getString("notas_adicionales"));
        return p;
    }

    private String fecha(LocalDate date) { return date == null ? null : date.toString(); }
    private LocalDate parseFecha(String value) { return value == null || value.isBlank() ? null : LocalDate.parse(value); }
}
