package com.example.sportmanagerpro.repositories;

import com.example.sportmanagerpro.database.DatabaseConnection;
import com.example.sportmanagerpro.models.Competencia;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CompetenciaRepository {

    public void guardar(Competencia competencia) {
        String sql = """
            INSERT INTO competencias (
                nombre, tipo, sede, fecha_inicio, fecha_fin, descripcion, observaciones
            )
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            llenarStatement(statement, competencia);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar la competencia.", e);
        }
    }

    public void actualizar(Competencia competencia) {
        String sql = """
            UPDATE competencias
            SET nombre = ?, tipo = ?, sede = ?, fecha_inicio = ?, fecha_fin = ?,
                descripcion = ?, observaciones = ?
            WHERE id_competencia = ?
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            llenarStatement(statement, competencia);
            statement.setInt(8, competencia.getIdCompetencia());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar la competencia.", e);
        }
    }

    public List<Competencia> listarTodas() {
        List<Competencia> competencias = new ArrayList<>();

        String sql = """
            SELECT *
            FROM competencias
            ORDER BY fecha_inicio DESC
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                competencias.add(mapear(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar competencias.", e);
        }

        return competencias;
    }

    private void llenarStatement(PreparedStatement statement, Competencia c) throws SQLException {
        statement.setString(1, c.getNombre());
        statement.setString(2, c.getTipo());
        statement.setString(3, c.getSede());
        statement.setString(4, c.getFechaInicio() != null ? c.getFechaInicio().toString() : null);
        statement.setString(5, c.getFechaFin() != null ? c.getFechaFin().toString() : null);
        statement.setString(6, c.getDescripcion());
        statement.setString(7, c.getObservaciones());
    }

    private Competencia mapear(ResultSet rs) throws SQLException {
        Competencia c = new Competencia();

        c.setIdCompetencia(rs.getInt("id_competencia"));
        c.setNombre(rs.getString("nombre"));
        c.setTipo(rs.getString("tipo"));
        c.setSede(rs.getString("sede"));

        String fechaInicio = rs.getString("fecha_inicio");
        String fechaFin = rs.getString("fecha_fin");

        c.setFechaInicio(fechaInicio != null ? LocalDate.parse(fechaInicio) : null);
        c.setFechaFin(fechaFin != null ? LocalDate.parse(fechaFin) : null);

        c.setDescripcion(rs.getString("descripcion"));
        c.setObservaciones(rs.getString("observaciones"));

        return c;
    }
}