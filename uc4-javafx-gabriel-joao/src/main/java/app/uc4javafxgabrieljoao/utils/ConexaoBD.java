package app.uc4javafxgabrieljoao.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBD {
    private static final String URL = "jdbc : postgresql://localhost5432/mediotec_sistema_locadora";
    private static final String USUARIO = "postgres";
    private static String SENHA = "postgres";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL,USUARIO,SENHA);
    }
}