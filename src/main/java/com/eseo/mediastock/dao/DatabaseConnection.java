package com.eseo.mediastock.dao;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe utilitaire gérant la connexion unique à la base de données (Pattern Singleton).
 * <p>
 * Initialise le driver JDBC, lit les variables d'environnement (ou de configuration),
 * et fournit un objet {@link java.sql.Connection} persistant aux différents DAO pour
 * exécuter leurs requêtes SQL.
 * </p>
 */
public class DatabaseConnection {
    /**
     * The Dotenv.
     */
    static Dotenv dotenv = Dotenv.configure().directory("./").load();

    private static final String URL = dotenv.get("DB_URL");
    private static final String USER = dotenv.get("DB_USER");
    private static final String PASS = dotenv.get("DB_PASSWORD");

    /**
     * Gets connection.
     *
     * @return the connection
     * @throws SQLException the sql exception
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
