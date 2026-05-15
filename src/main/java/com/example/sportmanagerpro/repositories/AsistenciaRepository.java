package com.example.sportmanagerpro.repositories;

import com.example.sportmanagerpro.database.DatabaseConnection;
import com.example.sportmanagerpro.models.Asistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AsistenciaRepository {

    public void guardar(Asistencia asistencia) {

        String sql = """
            INSERT OR REPLACE INTO asistencias (
                id_entrenamiento,
                id_deportista,
                estado,
                motivo,
                observaciones
            )
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, asistencia.getIdEntrenamiento());
            statement.setInt(2, asistencia.getIdDeportista());
            statement.setString(3, asistencia.getEstado());
            statement.setString(4, asistencia.getMotivo());
            statement.setString(5, asistencia.getObservaciones());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar asistencia.", e);
        }
    }
}