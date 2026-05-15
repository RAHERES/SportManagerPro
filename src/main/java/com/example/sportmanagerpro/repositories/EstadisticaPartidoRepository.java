package com.example.sportmanagerpro.repositories;

import com.example.sportmanagerpro.database.DatabaseConnection;
import com.example.sportmanagerpro.models.EstadisticaPartido;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EstadisticaPartidoRepository {

    public void guardar(EstadisticaPartido estadistica) {
        String sql = """
            INSERT OR REPLACE INTO estadisticas_partido (
                id_partido,
                id_deportista,
                titular,
                minutos_jugados,
                goles,
                asistencias,
                tarjetas_amarillas,
                tarjetas_rojas,
                observaciones
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, estadistica.getIdPartido());
            statement.setInt(2, estadistica.getIdDeportista());
            statement.setInt(3, estadistica.isTitular() ? 1 : 0);
            statement.setInt(4, estadistica.getMinutosJugados());
            statement.setInt(5, estadistica.getGoles());
            statement.setInt(6, estadistica.getAsistencias());
            statement.setInt(7, estadistica.getTarjetasAmarillas());
            statement.setInt(8, estadistica.getTarjetasRojas());
            statement.setString(9, estadistica.getObservaciones());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar la estadística del partido.", e);
        }
    }
}