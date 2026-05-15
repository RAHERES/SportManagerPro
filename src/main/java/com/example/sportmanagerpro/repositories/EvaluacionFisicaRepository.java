package com.example.sportmanagerpro.repositories;

import com.example.sportmanagerpro.database.DatabaseConnection;
import com.example.sportmanagerpro.models.EvaluacionFisica;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EvaluacionFisicaRepository {

    public void guardar(EvaluacionFisica evaluacion) {
        String sql = """
            INSERT INTO evaluaciones_fisicas (
                id_deportista, fecha, prueba, resultado,
                unidad, observaciones, evaluador
            )
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            llenarStatement(statement, evaluacion);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar la evaluación física.", e);
        }
    }

    public void actualizar(EvaluacionFisica evaluacion) {
        String sql = """
            UPDATE evaluaciones_fisicas
            SET id_deportista = ?, fecha = ?, prueba = ?, resultado = ?,
                unidad = ?, observaciones = ?, evaluador = ?
            WHERE id_evaluacion = ?
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            llenarStatement(statement, evaluacion);
            statement.setInt(8, evaluacion.getIdEvaluacion());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar la evaluación física.", e);
        }
    }

    public List<EvaluacionFisica> listarTodas() {
        List<EvaluacionFisica> evaluaciones = new ArrayList<>();

        String sql = """
            SELECT *
            FROM evaluaciones_fisicas
            ORDER BY fecha DESC
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                evaluaciones.add(mapear(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar evaluaciones físicas.", e);
        }

        return evaluaciones;
    }

    public List<EvaluacionFisica> listarPorDeportista(int idDeportista) {
        List<EvaluacionFisica> evaluaciones = new ArrayList<>();

        String sql = """
            SELECT *
            FROM evaluaciones_fisicas
            WHERE id_deportista = ?
            ORDER BY fecha DESC
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idDeportista);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    evaluaciones.add(mapear(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar evaluaciones por deportista.", e);
        }

        return evaluaciones;
    }

    private void llenarStatement(PreparedStatement statement, EvaluacionFisica e) throws SQLException {
        statement.setInt(1, e.getIdDeportista());
        statement.setString(2, e.getFecha() != null ? e.getFecha().toString() : null);
        statement.setString(3, e.getPrueba());
        statement.setDouble(4, e.getResultado());
        statement.setString(5, e.getUnidad());
        statement.setString(6, e.getObservaciones());
        statement.setString(7, e.getEvaluador());
    }

    private EvaluacionFisica mapear(ResultSet rs) throws SQLException {
        EvaluacionFisica e = new EvaluacionFisica();

        e.setIdEvaluacion(rs.getInt("id_evaluacion"));
        e.setIdDeportista(rs.getInt("id_deportista"));
        e.setFecha(LocalDate.parse(rs.getString("fecha")));
        e.setPrueba(rs.getString("prueba"));
        e.setResultado(rs.getDouble("resultado"));
        e.setUnidad(rs.getString("unidad"));
        e.setObservaciones(rs.getString("observaciones"));
        e.setEvaluador(rs.getString("evaluador"));

        return e;
    }
}