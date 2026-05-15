package com.example.sportmanagerpro.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Crea las tablas principales de la aplicación si todavía no existen.
 */
public class DatabaseInitializer {

    private DatabaseInitializer() {
    }

    public static void initializeDatabase() {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute("PRAGMA foreign_keys = ON");

            statement.execute("""
                CREATE TABLE IF NOT EXISTS categorias (
                    id_categoria INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre TEXT NOT NULL UNIQUE,
                    rango_edad TEXT,
                    descripcion TEXT,
                    activa INTEGER NOT NULL DEFAULT 1
                );
            """);

            statement.execute("""
                CREATE TABLE IF NOT EXISTS deportistas (
                    id_deportista INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre_completo TEXT NOT NULL,
                    fecha_nacimiento TEXT,
                    sexo TEXT,
                    grado_grupo TEXT,
                    posicion_principal TEXT,
                    posicion_secundaria TEXT,
                    pierna_dominante TEXT,
                    telefono_tutor TEXT,
                    id_categoria INTEGER,
                    activo INTEGER NOT NULL DEFAULT 1,
                    observaciones TEXT,
                    fecha_registro TEXT NOT NULL DEFAULT CURRENT_DATE,
                    FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria)
                );
            """);

            statement.execute("""
                CREATE TABLE IF NOT EXISTS entrenamientos (
                    id_entrenamiento INTEGER PRIMARY KEY AUTOINCREMENT,
                    fecha TEXT NOT NULL,
                    hora_inicio TEXT,
                    hora_fin TEXT,
                    id_categoria INTEGER,
                    objetivo TEXT,
                    tipo_entrenamiento TEXT,
                    intensidad TEXT,
                    duracion_minutos INTEGER,
                    contenido_trabajado TEXT,
                    material_utilizado TEXT,
                    observaciones TEXT,
                    FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria)
                );
            """);

            statement.execute("""
                CREATE TABLE IF NOT EXISTS asistencias (
                    id_asistencia INTEGER PRIMARY KEY AUTOINCREMENT,
                    id_entrenamiento INTEGER NOT NULL,
                    id_deportista INTEGER NOT NULL,
                    estado TEXT NOT NULL,
                    motivo TEXT,
                    observaciones TEXT,
                    UNIQUE(id_entrenamiento, id_deportista),
                    FOREIGN KEY (id_entrenamiento) REFERENCES entrenamientos(id_entrenamiento),
                    FOREIGN KEY (id_deportista) REFERENCES deportistas(id_deportista)
                );
            """);
            statement.execute("""
    CREATE TABLE IF NOT EXISTS evaluaciones_fisicas (
        id_evaluacion INTEGER PRIMARY KEY AUTOINCREMENT,
        id_deportista INTEGER NOT NULL,
        fecha TEXT NOT NULL,
        prueba TEXT NOT NULL,
        resultado REAL NOT NULL,
        unidad TEXT,
        observaciones TEXT,
        evaluador TEXT,
        FOREIGN KEY (id_deportista) REFERENCES deportistas(id_deportista)
    );

""");

            statement.execute("""
    CREATE TABLE IF NOT EXISTS competencias (
        id_competencia INTEGER PRIMARY KEY AUTOINCREMENT,
        nombre TEXT NOT NULL,
        tipo TEXT,
        sede TEXT,
        fecha_inicio TEXT,
        fecha_fin TEXT,
        descripcion TEXT,
        observaciones TEXT
    );
""");

            statement.execute("""
    CREATE TABLE IF NOT EXISTS partidos (
        id_partido INTEGER PRIMARY KEY AUTOINCREMENT,
        id_competencia INTEGER,
        id_categoria INTEGER NOT NULL,
        fecha TEXT NOT NULL,
        rival TEXT NOT NULL,
        sede TEXT,
        fase TEXT,
        marcador_favor INTEGER DEFAULT 0,
        marcador_contra INTEGER DEFAULT 0,
        resultado TEXT,
        observaciones TEXT,
        FOREIGN KEY (id_competencia) REFERENCES competencias(id_competencia),
        FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria)
    );
""");

            statement.execute("""
    CREATE TABLE IF NOT EXISTS estadisticas_partido (
        id_estadistica INTEGER PRIMARY KEY AUTOINCREMENT,
        id_partido INTEGER NOT NULL,
        id_deportista INTEGER NOT NULL,
        titular INTEGER DEFAULT 0,
        minutos_jugados INTEGER DEFAULT 0,
        goles INTEGER DEFAULT 0,
        asistencias INTEGER DEFAULT 0,
        tarjetas_amarillas INTEGER DEFAULT 0,
        tarjetas_rojas INTEGER DEFAULT 0,
        observaciones TEXT,
        UNIQUE(id_partido, id_deportista),
        FOREIGN KEY (id_partido) REFERENCES partidos(id_partido),
        FOREIGN KEY (id_deportista) REFERENCES deportistas(id_deportista)
    );
""");

            statement.execute("""
    CREATE TABLE IF NOT EXISTS lesiones (
        id_lesion INTEGER PRIMARY KEY AUTOINCREMENT,
        id_deportista INTEGER NOT NULL,
        fecha TEXT NOT NULL,
        zona_afectada TEXT,
        tipo_molestia TEXT,
        contexto TEXT,
        accion_tomada TEXT,
        fecha_regreso_estimada TEXT,
        estado TEXT,
        observaciones TEXT,
        FOREIGN KEY (id_deportista) REFERENCES deportistas(id_deportista)
    );
""");
            statement.execute("""
    CREATE TABLE IF NOT EXISTS observaciones_deportivas (
        id_observacion INTEGER PRIMARY KEY AUTOINCREMENT,
        id_deportista INTEGER NOT NULL,
        fecha TEXT NOT NULL,
        tipo TEXT NOT NULL,
        observacion TEXT NOT NULL,
        FOREIGN KEY (id_deportista) REFERENCES deportistas(id_deportista)
    );
""");


        } catch (SQLException e) {
            throw new RuntimeException("Error al inicializar la base de datos", e);
        }
    }

}