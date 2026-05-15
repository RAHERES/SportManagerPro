package com.example.sportmanagerpro.repositories;

import com.example.sportmanagerpro.database.DatabaseConnection;
import com.example.sportmanagerpro.models.Lesion;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LesionRepository {

    public void guardar(Lesion lesion) {
        String sql = """
            INSERT INTO lesiones (
                id_deportista, fecha, zona_afectada, tipo_molestia,
                contexto, accion_tomada, fecha_regreso_estimada,
                estado, observaciones
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            llenarStatement(statement, lesion);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar la lesión.", e);
        }
    }

    public void actualizar(Lesion lesion) {
        String sql = """
            UPDATE lesiones
            SET id_deportista = ?, fecha = ?, zona_afectada = ?, tipo_molestia = ?,
                contexto = ?, accion_tomada = ?, fecha_regreso_estimada = ?,
                estado = ?, observaciones = ?
            WHERE id_lesion = ?
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            llenarStatement(statement, lesion);
            statement.setInt(10, lesion.getIdLesion());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar la lesión.", e);
        }
    }

    public List<Lesion> listarTodas() {
        List<Lesion> lesiones = new ArrayList<>();

        String sql = """
            SELECT *
            FROM lesiones
            ORDER BY fecha DESC
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                lesiones.add(mapear(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar lesiones.", e);
        }

        return lesiones;
    }

    public List<Lesion> listarPorDeportista(int idDeportista) {
        List<Lesion> lesiones = new ArrayList<>();

        String sql = """
            SELECT *
            FROM lesiones
            WHERE id_deportista = ?
            ORDER BY fecha DESC
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idDeportista);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    lesiones.add(mapear(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar lesiones por deportista.", e);
        }

        return lesiones;
    }

    private void llenarStatement(PreparedStatement statement, Lesion l) throws SQLException {
        statement.setInt(1, l.getIdDeportista());
        statement.setString(2, l.getFecha() != null ? l.getFecha().toString() : null);
        statement.setString(3, l.getZonaAfectada());
        statement.setString(4, l.getTipoMolestia());
        statement.setString(5, l.getContexto());
        statement.setString(6, l.getAccionTomada());
        statement.setString(7, l.getFechaRegresoEstimada() != null ? l.getFechaRegresoEstimada().toString() : null);
        statement.setString(8, l.getEstado());
        statement.setString(9, l.getObservaciones());
    }

    private Lesion mapear(ResultSet rs) throws SQLException {
        Lesion l = new Lesion();

        l.setIdLesion(rs.getInt("id_lesion"));
        l.setIdDeportista(rs.getInt("id_deportista"));

        String fecha = rs.getString("fecha");
        String regreso = rs.getString("fecha_regreso_estimada");

        l.setFecha(fecha != null ? LocalDate.parse(fecha) : null);
        l.setZonaAfectada(rs.getString("zona_afectada"));
        l.setTipoMolestia(rs.getString("tipo_molestia"));
        l.setContexto(rs.getString("contexto"));
        l.setAccionTomada(rs.getString("accion_tomada"));
        l.setFechaRegresoEstimada(regreso != null ? LocalDate.parse(regreso) : null);
        l.setEstado(rs.getString("estado"));
        l.setObservaciones(rs.getString("observaciones"));

        return l;
    }
}