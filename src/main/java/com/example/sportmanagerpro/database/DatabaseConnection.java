package com.example.sportmanagerpro.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestiona la conexión principal con la base de datos SQLite.
 */
public class DatabaseConnection {

    private static final String DATABASE_URL = "jdbc:sqlite:sportmanager.db";

    private DatabaseConnection() {
    }

    /**
     * Abre una conexión con la base de datos local.
     *
     * @return conexión activa con SQLite.
     * @throws SQLException si ocurre un error al conectar.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL);
    }
}