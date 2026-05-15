package com.example.sportmanagerpro.repositories;



import com.example.sportmanagerpro.database.DatabaseConnection;
import com.example.sportmanagerpro.models.Deportista;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DeportistaRepository {

    public void guardar(Deportista deportista) {
        String sql = """
            INSERT INTO deportistas (
                nombre_completo, fecha_nacimiento, sexo, grado_grupo,
                posicion_principal, posicion_secundaria, pierna_dominante,
                telefono_tutor, id_categoria, activo, observaciones
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            llenarStatement(statement, deportista);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar la deportista.", e);
        }
    }

    public void actualizar(Deportista deportista) {
        String sql = """
            UPDATE deportistas
            SET nombre_completo = ?, fecha_nacimiento = ?, sexo = ?, grado_grupo = ?,
                posicion_principal = ?, posicion_secundaria = ?, pierna_dominante = ?,
                telefono_tutor = ?, id_categoria = ?, activo = ?, observaciones = ?
            WHERE id_deportista = ?
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            llenarStatement(statement, deportista);
            statement.setInt(12, deportista.getIdDeportista());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar la deportista.", e);
        }
    }

    public List<Deportista> listarTodas() {
        List<Deportista> deportistas = new ArrayList<>();

        String sql = """
            SELECT *
            FROM deportistas
            ORDER BY nombre_completo
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                deportistas.add(mapear(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar deportistas.", e);
        }

        return deportistas;
    }

    public void desactivar(int idDeportista) {
        String sql = "UPDATE deportistas SET activo = 0 WHERE id_deportista = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idDeportista);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al desactivar la deportista.", e);
        }
    }

    private void llenarStatement(PreparedStatement statement, Deportista d) throws SQLException {
        statement.setString(1, d.getNombreCompleto());
        statement.setString(2, d.getFechaNacimiento() != null ? d.getFechaNacimiento().toString() : null);
        statement.setString(3, d.getSexo());
        statement.setString(4, d.getGradoGrupo());
        statement.setString(5, d.getPosicionPrincipal());
        statement.setString(6, d.getPosicionSecundaria());
        statement.setString(7, d.getPiernaDominante());
        statement.setString(8, d.getTelefonoTutor());
        statement.setInt(9, d.getIdCategoria());
        statement.setInt(10, d.isActivo() ? 1 : 0);
        statement.setString(11, d.getObservaciones());
    }

    private Deportista mapear(ResultSet rs) throws SQLException {
        Deportista d = new Deportista();

        d.setIdDeportista(rs.getInt("id_deportista"));
        d.setNombreCompleto(rs.getString("nombre_completo"));

        String fechaNacimiento = rs.getString("fecha_nacimiento");
        d.setFechaNacimiento(fechaNacimiento != null ? LocalDate.parse(fechaNacimiento) : null);

        d.setSexo(rs.getString("sexo"));
        d.setGradoGrupo(rs.getString("grado_grupo"));
        d.setPosicionPrincipal(rs.getString("posicion_principal"));
        d.setPosicionSecundaria(rs.getString("posicion_secundaria"));
        d.setPiernaDominante(rs.getString("pierna_dominante"));
        d.setTelefonoTutor(rs.getString("telefono_tutor"));
        d.setIdCategoria(rs.getInt("id_categoria"));
        d.setActivo(rs.getInt("activo") == 1);
        d.setObservaciones(rs.getString("observaciones"));

        String fechaRegistro = rs.getString("fecha_registro");
        d.setFechaRegistro(fechaRegistro != null ? LocalDate.parse(fechaRegistro) : null);

        return d;
    }

    public List<Deportista> listarPorCategoria(int idCategoria) {
        List<Deportista> deportistas = new ArrayList<>();

        String sql = """
        SELECT *
        FROM deportistas
        WHERE id_categoria = ?
        AND activo = 1
        ORDER BY nombre_completo
    """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idCategoria);

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {
                    deportistas.add(mapear(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar deportistas por categoría.", e);
        }

        return deportistas;
    }
}