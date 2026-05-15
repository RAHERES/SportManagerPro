package com.example.sportmanagerpro.repositories;

import com.example.sportmanagerpro.database.DatabaseConnection;
import com.example.sportmanagerpro.models.ObservacionDeportiva;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ObservacionDeportivaRepository {

    public void guardar(ObservacionDeportiva observacion) {
        String sql = """
            INSERT INTO observaciones_deportivas (
                id_deportista, fecha, tipo, observacion
            )
            VALUES (?, ?, ?, ?)
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            llenarStatement(statement, observacion);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar la observación deportiva.", e);
        }
    }

    public void actualizar(ObservacionDeportiva observacion) {
        String sql = """
            UPDATE observaciones_deportivas
            SET id_deportista = ?, fecha = ?, tipo = ?, observacion = ?
            WHERE id_observacion = ?
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            llenarStatement(statement, observacion);
            statement.setInt(5, observacion.getIdObservacion());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar la observación deportiva.", e);
        }
    }

    public List<ObservacionDeportiva> listarTodas() {
        List<ObservacionDeportiva> observaciones = new ArrayList<>();

        String sql = """
            SELECT *
            FROM observaciones_deportivas
            ORDER BY fecha DESC
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                observaciones.add(mapear(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar observaciones deportivas.", e);
        }

        return observaciones;
    }

    public List<ObservacionDeportiva> listarPorDeportista(int idDeportista) {
        List<ObservacionDeportiva> observaciones = new ArrayList<>();

        String sql = """
            SELECT *
            FROM observaciones_deportivas
            WHERE id_deportista = ?
            ORDER BY fecha DESC
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idDeportista);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    observaciones.add(mapear(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar observaciones por deportista.", e);
        }

        return observaciones;
    }

    private void llenarStatement(PreparedStatement statement, ObservacionDeportiva o) throws SQLException {
        statement.setInt(1, o.getIdDeportista());
        statement.setString(2, o.getFecha() != null ? o.getFecha().toString() : null);
        statement.setString(3, o.getTipo());
        statement.setString(4, o.getObservacion());
    }

    private ObservacionDeportiva mapear(ResultSet rs) throws SQLException {
        ObservacionDeportiva o = new ObservacionDeportiva();

        o.setIdObservacion(rs.getInt("id_observacion"));
        o.setIdDeportista(rs.getInt("id_deportista"));
        o.setFecha(LocalDate.parse(rs.getString("fecha")));
        o.setTipo(rs.getString("tipo"));
        o.setObservacion(rs.getString("observacion"));

        return o;
    }
}