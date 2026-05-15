package com.example.sportmanagerpro.repositories;

import com.example.sportmanagerpro.database.DatabaseConnection;
import com.example.sportmanagerpro.models.Partido;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PartidoRepository {

    public void guardar(Partido partido) {
        String sql = """
            INSERT INTO partidos (
                id_competencia, id_categoria, fecha, rival, sede, fase,
                marcador_favor, marcador_contra, resultado, observaciones
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            llenarStatement(statement, partido);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar el partido.", e);
        }
    }

    public void actualizar(Partido partido) {
        String sql = """
            UPDATE partidos
            SET id_competencia = ?, id_categoria = ?, fecha = ?, rival = ?,
                sede = ?, fase = ?, marcador_favor = ?, marcador_contra = ?,
                resultado = ?, observaciones = ?
            WHERE id_partido = ?
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            llenarStatement(statement, partido);
            statement.setInt(11, partido.getIdPartido());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el partido.", e);
        }
    }

    public List<Partido> listarTodos() {
        List<Partido> partidos = new ArrayList<>();

        String sql = """
            SELECT *
            FROM partidos
            ORDER BY fecha DESC
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                partidos.add(mapear(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar partidos.", e);
        }

        return partidos;
    }

    private void llenarStatement(PreparedStatement statement, Partido p) throws SQLException {
        if (p.getIdCompetencia() > 0) {
            statement.setInt(1, p.getIdCompetencia());
        } else {
            statement.setNull(1, Types.INTEGER);
        }

        statement.setInt(2, p.getIdCategoria());
        statement.setString(3, p.getFecha() != null ? p.getFecha().toString() : null);
        statement.setString(4, p.getRival());
        statement.setString(5, p.getSede());
        statement.setString(6, p.getFase());
        statement.setInt(7, p.getMarcadorFavor());
        statement.setInt(8, p.getMarcadorContra());
        statement.setString(9, p.getResultado());
        statement.setString(10, p.getObservaciones());
    }

    private Partido mapear(ResultSet rs) throws SQLException {
        Partido p = new Partido();

        p.setIdPartido(rs.getInt("id_partido"));
        p.setIdCompetencia(rs.getInt("id_competencia"));
        p.setIdCategoria(rs.getInt("id_categoria"));
        p.setFecha(LocalDate.parse(rs.getString("fecha")));
        p.setRival(rs.getString("rival"));
        p.setSede(rs.getString("sede"));
        p.setFase(rs.getString("fase"));
        p.setMarcadorFavor(rs.getInt("marcador_favor"));
        p.setMarcadorContra(rs.getInt("marcador_contra"));
        p.setResultado(rs.getString("resultado"));
        p.setObservaciones(rs.getString("observaciones"));

        return p;
    }
}