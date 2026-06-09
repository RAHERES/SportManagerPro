package com.example.sportmanagerpro.pacientes;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio SQLite para almacenar, actualizar, buscar y recuperar pacientes.
 *
 * <p>La base de datos se crea automáticamente en la carpeta del usuario:
 * {@code ~/SportNutriPro/pacientes.db}. No requiere instalar un servidor externo.</p>
 */
public class PatientDAO {

    /** Ruta local donde se almacena la base de datos SQLite. */
    private final Path databasePath;

    /** URL JDBC utilizada para conectarse a SQLite. */
    private final String jdbcUrl;

    /**
     * Construye el repositorio y crea la tabla si no existe.
     */
    public PatientDAO() {
        try {
            Path appDir = Path.of(System.getProperty("user.home"), "SportNutriPro");
            Files.createDirectories(appDir);
            this.databasePath = appDir.resolve("pacientes.db");
            this.jdbcUrl = "jdbc:sqlite:" + databasePath.toAbsolutePath();
            crearTablaSiNoExiste();
        } catch (Exception ex) {
            throw new IllegalStateException("No se pudo inicializar la base de datos de pacientes.", ex);
        }
    }

    /**
     * Devuelve la ruta física de la base de datos.
     *
     * @return ruta del archivo SQLite.
     */
    public Path getDatabasePath() {
        return databasePath;
    }

    /**
     * Abre una conexión nueva contra SQLite.
     *
     * @return conexión JDBC activa.
     * @throws SQLException si SQLite no permite la conexión.
     */
    private Connection conectar() throws SQLException {
        return DriverManager.getConnection(jdbcUrl);
    }

    /**
     * Crea la tabla principal de pacientes cuando la aplicación se ejecuta por primera vez.
     */
    private void crearTablaSiNoExiste() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS pacientes (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombres TEXT NOT NULL,
                    apellido_paterno TEXT,
                    apellido_materno TEXT,
                    fecha_nacimiento TEXT,
                    edad INTEGER,
                    genero TEXT,
                    telefono TEXT,
                    correo TEXT,
                    estado_civil TEXT,
                    objetivo_principal TEXT,
                    nivel_actividad TEXT,
                    ocupacion TEXT,
                    como_nos_conocio TEXT,
                    notas_iniciales TEXT,
                    peso_actual_kg REAL,
                    estatura_cm REAL,
                    porcentaje_grasa REAL,
                    imc REAL,
                    cintura_cm REAL,
                    cadera_cm REAL,
                    foto_path TEXT,
                    estado_registro TEXT NOT NULL,
                    creado_en TEXT NOT NULL
                );
                """;

        try (Connection conn = conectar(); Statement st = conn.createStatement()) {
            st.execute(sql);
        }
    }

    /**
     * Guarda un nuevo paciente o actualiza uno existente si ya contiene ID.
     *
     * @param patient paciente a persistir.
     * @return paciente con ID asignado.
     */
    public Patient guardar(Patient patient) {
        if (patient.getId() == null) {
            return insertar(patient);
        }
        actualizar(patient);
        return patient;
    }

    /**
     * Inserta un expediente nuevo.
     *
     * @param p paciente sin ID.
     * @return paciente con ID generado por SQLite.
     */
    private Patient insertar(Patient p) {
        String sql = """
                INSERT INTO pacientes (
                    nombres, apellido_paterno, apellido_materno, fecha_nacimiento, edad, genero,
                    telefono, correo, estado_civil, objetivo_principal, nivel_actividad, ocupacion,
                    como_nos_conocio, notas_iniciales, peso_actual_kg, estatura_cm, porcentaje_grasa,
                    imc, cintura_cm, cadera_cm, foto_path, estado_registro, creado_en
                ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
                """;

        if (p.getCreadoEn() == null || p.getCreadoEn().isBlank()) {
            p.setCreadoEn(LocalDateTime.now().toString());
        }

        try (Connection conn = conectar(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            llenarParametros(ps, p);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    p.setId(keys.getLong(1));
                }
            }
            return p;
        } catch (SQLException ex) {
            throw new IllegalStateException("No se pudo insertar el paciente.", ex);
        }
    }

    /**
     * Actualiza un expediente existente.
     *
     * @param p paciente con ID existente.
     */
    private void actualizar(Patient p) {
        String sql = """
                UPDATE pacientes SET
                    nombres=?, apellido_paterno=?, apellido_materno=?, fecha_nacimiento=?, edad=?, genero=?,
                    telefono=?, correo=?, estado_civil=?, objetivo_principal=?, nivel_actividad=?, ocupacion=?,
                    como_nos_conocio=?, notas_iniciales=?, peso_actual_kg=?, estatura_cm=?, porcentaje_grasa=?,
                    imc=?, cintura_cm=?, cadera_cm=?, foto_path=?, estado_registro=?, creado_en=?
                WHERE id=?
                """;

        try (Connection conn = conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            llenarParametros(ps, p);
            ps.setLong(24, p.getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new IllegalStateException("No se pudo actualizar el paciente.", ex);
        }
    }

    /**
     * Busca pacientes por nombre, teléfono, correo o ID.
     *
     * @param query texto de búsqueda.
     * @return lista de pacientes coincidentes.
     */
    public List<Patient> buscar(String query) {
        String q = query == null ? "" : query.trim();
        String sql = """
                SELECT * FROM pacientes
                WHERE CAST(id AS TEXT) LIKE ?
                   OR nombres LIKE ?
                   OR apellido_paterno LIKE ?
                   OR apellido_materno LIKE ?
                   OR telefono LIKE ?
                   OR correo LIKE ?
                ORDER BY id DESC
                LIMIT 100
                """;
        String pattern = "%" + q + "%";
        try (Connection conn = conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 1; i <= 6; i++) ps.setString(i, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                List<Patient> result = new ArrayList<>();
                while (rs.next()) result.add(map(rs));
                return result;
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("No se pudo buscar pacientes.", ex);
        }
    }

    /**
     * Lista los últimos expedientes guardados.
     *
     * @return pacientes ordenados por ID descendente.
     */
    public List<Patient> listarRecientes() {
        return buscar("");
    }

    /**
     * Busca un paciente por ID.
     *
     * @param id identificador de SQLite.
     * @return paciente o null si no existe.
     */
    public Patient obtenerPorId(long id) {
        String sql = "SELECT * FROM pacientes WHERE id = ?";
        try (Connection conn = conectar(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("No se pudo recuperar el paciente.", ex);
        }
    }

    /**
     * Elimina un expediente por ID.
     *
     * @param id identificador a eliminar.
     */
    public void eliminar(long id) {
        try (Connection conn = conectar(); PreparedStatement ps = conn.prepareStatement("DELETE FROM pacientes WHERE id = ?")) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new IllegalStateException("No se pudo eliminar el paciente.", ex);
        }
    }

    /**
     * Llena los parámetros comunes para INSERT y UPDATE.
     *
     * @param ps sentencia preparada.
     * @param p paciente fuente.
     * @throws SQLException si un parámetro no puede asignarse.
     */
    private void llenarParametros(PreparedStatement ps, Patient p) throws SQLException {
        ps.setString(1, p.getNombres());
        ps.setString(2, p.getApellidoPaterno());
        ps.setString(3, p.getApellidoMaterno());
        ps.setString(4, p.getFechaNacimiento() == null ? null : p.getFechaNacimiento().toString());
        setInteger(ps, 5, p.getEdad());
        ps.setString(6, p.getGenero());
        ps.setString(7, p.getTelefono());
        ps.setString(8, p.getCorreo());
        ps.setString(9, p.getEstadoCivil());
        ps.setString(10, p.getObjetivoPrincipal());
        ps.setString(11, p.getNivelActividad());
        ps.setString(12, p.getOcupacion());
        ps.setString(13, p.getComoNosConocio());
        ps.setString(14, p.getNotasIniciales());
        setDouble(ps, 15, p.getPesoActualKg());
        setDouble(ps, 16, p.getEstaturaCm());
        setDouble(ps, 17, p.getPorcentajeGrasa());
        setDouble(ps, 18, p.getImc());
        setDouble(ps, 19, p.getCinturaCm());
        setDouble(ps, 20, p.getCaderaCm());
        ps.setString(21, p.getFotoPath());
        ps.setString(22, p.getEstadoRegistro() == null ? "ACTIVO" : p.getEstadoRegistro());
        ps.setString(23, p.getCreadoEn() == null ? LocalDateTime.now().toString() : p.getCreadoEn());
    }

    private void setDouble(PreparedStatement ps, int index, Double value) throws SQLException {
        if (value == null) ps.setNull(index, Types.REAL); else ps.setDouble(index, value);
    }

    private void setInteger(PreparedStatement ps, int index, Integer value) throws SQLException {
        if (value == null) ps.setNull(index, Types.INTEGER); else ps.setInt(index, value);
    }

    /**
     * Convierte una fila SQL en objeto Patient.
     *
     * @param rs fila actual del ResultSet.
     * @return objeto Patient mapeado.
     * @throws SQLException si alguna columna no se puede leer.
     */
    private Patient map(ResultSet rs) throws SQLException {
        Patient p = new Patient();
        p.setId(rs.getLong("id"));
        p.setNombres(rs.getString("nombres"));
        p.setApellidoPaterno(rs.getString("apellido_paterno"));
        p.setApellidoMaterno(rs.getString("apellido_materno"));
        String fn = rs.getString("fecha_nacimiento");
        p.setFechaNacimiento(fn == null || fn.isBlank() ? null : LocalDate.parse(fn));
        int edad = rs.getInt("edad");
        p.setEdad(rs.wasNull() ? null : edad);
        p.setGenero(rs.getString("genero"));
        p.setTelefono(rs.getString("telefono"));
        p.setCorreo(rs.getString("correo"));
        p.setEstadoCivil(rs.getString("estado_civil"));
        p.setObjetivoPrincipal(rs.getString("objetivo_principal"));
        p.setNivelActividad(rs.getString("nivel_actividad"));
        p.setOcupacion(rs.getString("ocupacion"));
        p.setComoNosConocio(rs.getString("como_nos_conocio"));
        p.setNotasIniciales(rs.getString("notas_iniciales"));
        p.setPesoActualKg(getDouble(rs, "peso_actual_kg"));
        p.setEstaturaCm(getDouble(rs, "estatura_cm"));
        p.setPorcentajeGrasa(getDouble(rs, "porcentaje_grasa"));
        p.setImc(getDouble(rs, "imc"));
        p.setCinturaCm(getDouble(rs, "cintura_cm"));
        p.setCaderaCm(getDouble(rs, "cadera_cm"));
        p.setFotoPath(rs.getString("foto_path"));
        p.setEstadoRegistro(rs.getString("estado_registro"));
        p.setCreadoEn(rs.getString("creado_en"));
        return p;
    }

    private Double getDouble(ResultSet rs, String column) throws SQLException {
        double value = rs.getDouble(column);
        return rs.wasNull() ? null : value;
    }
}
