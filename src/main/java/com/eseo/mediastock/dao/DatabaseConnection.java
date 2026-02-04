package com.eseo.mediastock.dao;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    private static final String URL = "https://91.175.22.47:33015/phpmyadmin";
    private static final String USER = "eseo";
    private static final String PASSWORD = "Eseo49.exe";

    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection (URL, USER, PASSWORD);
    }
}
