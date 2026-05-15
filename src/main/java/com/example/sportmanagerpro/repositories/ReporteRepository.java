package com.example.sportmanagerpro.repositories;

import com.example.sportmanagerpro.database.DatabaseConnection;
import com.example.sportmanagerpro.models.DashboardResumen;
import com.example.sportmanagerpro.models.HistorialDeportistaResumen;
import com.example.sportmanagerpro.models.ReporteCategoriaResumen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReporteRepository {

    public HistorialDeportistaResumen obtenerResumenDeportista(int idDeportista) {
        HistorialDeportistaResumen resumen = new HistorialDeportistaResumen();

        resumen.setTotalEntrenamientos(contarTotalEntrenamientos(idDeportista));
        resumen.setAsistencias(contarAsistenciaPorEstado(idDeportista, "ASISTIO"));
        resumen.setFaltas(contarAsistenciaPorEstado(idDeportista, "FALTO"));
        resumen.setRetardos(contarAsistenciaPorEstado(idDeportista, "RETARDO"));

        cargarEstadisticasPartido(idDeportista, resumen);

        return resumen;
    }

    private int contarTotalEntrenamientos(int idDeportista) {
        String sql = """
            SELECT COUNT(*) AS total
            FROM asistencias
            WHERE id_deportista = ?
        """;

        return ejecutarConteo(sql, idDeportista);
    }

    private int contarAsistenciaPorEstado(int idDeportista, String estado) {
        String sql = """
            SELECT COUNT(*) AS total
            FROM asistencias
            WHERE id_deportista = ?
            AND estado = ?
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idDeportista);
            statement.setString(2, estado);

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? rs.getInt("total") : 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al contar asistencias.", e);
        }
    }

    private int ejecutarConteo(String sql, int idDeportista) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idDeportista);

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? rs.getInt("total") : 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al ejecutar conteo.", e);
        }
    }

    private void cargarEstadisticasPartido(int idDeportista, HistorialDeportistaResumen resumen) {
        String sql = """
            SELECT 
                COUNT(*) AS partidos,
                COALESCE(SUM(minutos_jugados), 0) AS minutos,
                COALESCE(SUM(goles), 0) AS goles,
                COALESCE(SUM(asistencias), 0) AS asistencias
            FROM estadisticas_partido
            WHERE id_deportista = ?
            AND minutos_jugados > 0
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idDeportista);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    resumen.setPartidosJugados(rs.getInt("partidos"));
                    resumen.setMinutosJugados(rs.getInt("minutos"));
                    resumen.setGoles(rs.getInt("goles"));
                    resumen.setAsistenciasPartido(rs.getInt("asistencias"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al cargar estadísticas de partido.", e);
        }
    }

    public DashboardResumen obtenerDashboardResumen() {
        DashboardResumen resumen = new DashboardResumen();

        resumen.setTotalDeportistas(contar("SELECT COUNT(*) FROM deportistas"));
        resumen.setDeportistasActivas(contar("SELECT COUNT(*) FROM deportistas WHERE activo = 1"));
        resumen.setTotalCategorias(contar("SELECT COUNT(*) FROM categorias"));
        resumen.setTotalEntrenamientos(contar("SELECT COUNT(*) FROM entrenamientos"));
        resumen.setTotalPartidos(contar("SELECT COUNT(*) FROM partidos"));
        resumen.setVictorias(contar("SELECT COUNT(*) FROM partidos WHERE resultado = 'Victoria'"));
        resumen.setEmpates(contar("SELECT COUNT(*) FROM partidos WHERE resultado = 'Empate'"));
        resumen.setDerrotas(contar("SELECT COUNT(*) FROM partidos WHERE resultado = 'Derrota'"));
        resumen.setLesionesActivas(contar("""
        SELECT COUNT(*) 
        FROM lesiones 
        WHERE estado IN ('Activa', 'En seguimiento', 'En reposo', 'Reincorporación progresiva')
    """));

        resumen.setPorcentajeAsistencia(calcularPorcentajeAsistencia());

        return resumen;
    }

    private int contar(String sql) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            return rs.next() ? rs.getInt(1) : 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener conteo del dashboard.", e);
        }
    }

    private double calcularPorcentajeAsistencia() {
        String sql = """
        SELECT
            COUNT(*) AS total,
            SUM(CASE WHEN estado = 'ASISTIO' THEN 1 ELSE 0 END) AS asistencias
        FROM asistencias
    """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            if (rs.next()) {
                int total = rs.getInt("total");
                int asistencias = rs.getInt("asistencias");

                if (total == 0) {
                    return 0;
                }

                return (asistencias * 100.0) / total;
            }

            return 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error al calcular porcentaje de asistencia.", e);
        }
    }

    public ReporteCategoriaResumen obtenerResumenCategoria(int idCategoria) {
        ReporteCategoriaResumen resumen = new ReporteCategoriaResumen();

        resumen.setTotalDeportistas(contarPorCategoria("""
        SELECT COUNT(*) 
        FROM deportistas 
        WHERE id_categoria = ?
    """, idCategoria));

        resumen.setDeportistasActivas(contarPorCategoria("""
        SELECT COUNT(*) 
        FROM deportistas 
        WHERE id_categoria = ? 
        AND activo = 1
    """, idCategoria));

        resumen.setEntrenamientos(contarPorCategoria("""
        SELECT COUNT(*) 
        FROM entrenamientos 
        WHERE id_categoria = ?
    """, idCategoria));

        resumen.setPartidos(contarPorCategoria("""
        SELECT COUNT(*) 
        FROM partidos 
        WHERE id_categoria = ?
    """, idCategoria));

        resumen.setVictorias(contarPorCategoria("""
        SELECT COUNT(*) 
        FROM partidos 
        WHERE id_categoria = ? 
        AND resultado = 'Victoria'
    """, idCategoria));

        resumen.setEmpates(contarPorCategoria("""
        SELECT COUNT(*) 
        FROM partidos 
        WHERE id_categoria = ? 
        AND resultado = 'Empate'
    """, idCategoria));

        resumen.setDerrotas(contarPorCategoria("""
        SELECT COUNT(*) 
        FROM partidos 
        WHERE id_categoria = ? 
        AND resultado = 'Derrota'
    """, idCategoria));

        resumen.setEvaluacionesFisicas(contarPorCategoria("""
        SELECT COUNT(*) 
        FROM evaluaciones_fisicas ef
        INNER JOIN deportistas d ON ef.id_deportista = d.id_deportista
        WHERE d.id_categoria = ?
    """, idCategoria));

        resumen.setLesionesActivas(contarPorCategoria("""
        SELECT COUNT(*) 
        FROM lesiones l
        INNER JOIN deportistas d ON l.id_deportista = d.id_deportista
        WHERE d.id_categoria = ?
        AND l.estado IN ('Activa', 'En seguimiento', 'En reposo', 'Reincorporación progresiva')
    """, idCategoria));

        resumen.setPorcentajeAsistencia(calcularPorcentajeAsistenciaCategoria(idCategoria));

        return resumen;
    }

    private int contarPorCategoria(String sql, int idCategoria) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idCategoria);

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener conteo por categoría.", e);
        }
    }

    private double calcularPorcentajeAsistenciaCategoria(int idCategoria) {
        String sql = """
        SELECT
            COUNT(*) AS total,
            SUM(CASE WHEN a.estado = 'ASISTIO' THEN 1 ELSE 0 END) AS asistencias
        FROM asistencias a
        INNER JOIN deportistas d ON a.id_deportista = d.id_deportista
        WHERE d.id_categoria = ?
    """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idCategoria);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    int total = rs.getInt("total");
                    int asistencias = rs.getInt("asistencias");

                    if (total == 0) {
                        return 0;
                    }

                    return (asistencias * 100.0) / total;
                }

                return 0;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al calcular asistencia por categoría.", e);
        }
    }
}