package com.example.sportmanagerpro.repositories;


import com.example.sportmanagerpro.database.DatabaseConnection;
import com.example.sportmanagerpro.models.Entrenamiento;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class EntrenamientoRepository {

    public void guardar(Entrenamiento entrenamiento) {
        String sql = """
            INSERT INTO entrenamientos (
                fecha, hora_inicio, hora_fin, id_categoria, objetivo,
                tipo_entrenamiento, intensidad, duracion_minutos,
                contenido_trabajado, material_utilizado, observaciones
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            llenarStatement(statement, entrenamiento);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el entrenamiento.", e);
        }
    }

    public void actualizar(Entrenamiento entrenamiento) {
        String sql = """
            UPDATE entrenamientos
            SET fecha = ?, hora_inicio = ?, hora_fin = ?, id_categoria = ?, objetivo = ?,
                tipo_entrenamiento = ?, intensidad = ?, duracion_minutos = ?,
                contenido_trabajado = ?, material_utilizado = ?, observaciones = ?
            WHERE id_entrenamiento = ?
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            llenarStatement(statement, entrenamiento);
            statement.setInt(12, entrenamiento.getIdEntrenamiento());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el entrenamiento.", e);
        }
    }

    public List<Entrenamiento> listarTodos() {
        List<Entrenamiento> entrenamientos = new ArrayList<>();

        String sql = """
            SELECT *
            FROM entrenamientos
            ORDER BY fecha DESC, hora_inicio DESC
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                entrenamientos.add(mapear(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar entrenamientos.", e);
        }

        return entrenamientos;
    }

    private void llenarStatement(PreparedStatement statement, Entrenamiento e) throws SQLException {
        statement.setString(1, e.getFecha() != null ? e.getFecha().toString() : null);
        statement.setString(2, e.getHoraInicio() != null ? e.getHoraInicio().toString() : null);
        statement.setString(3, e.getHoraFin() != null ? e.getHoraFin().toString() : null);
        statement.setInt(4, e.getIdCategoria());
        statement.setString(5, e.getObjetivo());
        statement.setString(6, e.getTipoEntrenamiento());
        statement.setString(7, e.getIntensidad());
        statement.setInt(8, e.getDuracionMinutos());
        statement.setString(9, e.getContenidoTrabajado());
        statement.setString(10, e.getMaterialUtilizado());
        statement.setString(11, e.getObservaciones());
    }

    private Entrenamiento mapear(ResultSet rs) throws SQLException {
        Entrenamiento e = new Entrenamiento();

        e.setIdEntrenamiento(rs.getInt("id_entrenamiento"));
        e.setFecha(LocalDate.parse(rs.getString("fecha")));

        String horaInicio = rs.getString("hora_inicio");
        String horaFin = rs.getString("hora_fin");

        e.setHoraInicio(horaInicio != null ? LocalTime.parse(horaInicio) : null);
        e.setHoraFin(horaFin != null ? LocalTime.parse(horaFin) : null);

        e.setIdCategoria(rs.getInt("id_categoria"));
        e.setObjetivo(rs.getString("objetivo"));
        e.setTipoEntrenamiento(rs.getString("tipo_entrenamiento"));
        e.setIntensidad(rs.getString("intensidad"));
        e.setDuracionMinutos(rs.getInt("duracion_minutos"));
        e.setContenidoTrabajado(rs.getString("contenido_trabajado"));
        e.setMaterialUtilizado(rs.getString("material_utilizado"));
        e.setObservaciones(rs.getString("observaciones"));

        return e;
    }
}