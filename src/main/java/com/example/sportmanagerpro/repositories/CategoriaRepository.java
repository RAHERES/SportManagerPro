package com.example.sportmanagerpro.repositories;



import com.example.sportmanagerpro.database.DatabaseConnection;
import com.example.sportmanagerpro.models.Categoria;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestiona las operaciones de base de datos para categorías.
 */
public class CategoriaRepository {

    public void guardar(Categoria categoria) {
        String sql = """
            INSERT INTO categorias (nombre, rango_edad, descripcion, activa)
            VALUES (?, ?, ?, ?)
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, categoria.getNombre());
            statement.setString(2, categoria.getRangoEdad());
            statement.setString(3, categoria.getDescripcion());
            statement.setInt(4, categoria.isActiva() ? 1 : 0);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar la categoría", e);
        }
    }

    public void actualizar(Categoria categoria) {
        String sql = """
            UPDATE categorias
            SET nombre = ?, rango_edad = ?, descripcion = ?, activa = ?
            WHERE id_categoria = ?
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, categoria.getNombre());
            statement.setString(2, categoria.getRangoEdad());
            statement.setString(3, categoria.getDescripcion());
            statement.setInt(4, categoria.isActiva() ? 1 : 0);
            statement.setInt(5, categoria.getIdCategoria());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar la categoría", e);
        }
    }

    public List<Categoria> listarTodas() {
        List<Categoria> categorias = new ArrayList<>();

        String sql = """
            SELECT id_categoria, nombre, rango_edad, descripcion, activa
            FROM categorias
            ORDER BY nombre
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Categoria categoria = new Categoria();
                categoria.setIdCategoria(resultSet.getInt("id_categoria"));
                categoria.setNombre(resultSet.getString("nombre"));
                categoria.setRangoEdad(resultSet.getString("rango_edad"));
                categoria.setDescripcion(resultSet.getString("descripcion"));
                categoria.setActiva(resultSet.getInt("activa") == 1);
                categorias.add(categoria);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar las categorías", e);
        }

        return categorias;
    }

    public void desactivar(int idCategoria) {
        String sql = "UPDATE categorias SET activa = 0 WHERE id_categoria = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idCategoria);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al desactivar la categoría", e);
        }
    }
}