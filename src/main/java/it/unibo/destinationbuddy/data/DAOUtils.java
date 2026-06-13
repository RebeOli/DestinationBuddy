package it.unibo.destinationbuddy.data; // o data, in base a dove lo avete messo

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class DAOUtils {

    // ==========================================
    // AREA DA DECOMMENTARE IN BASE AL COMPUTER
    // ==========================================
    
    // SE SEI SU PC WINDOWS LOCALE: Togli i commenti a questa riga
    private static final String URL = "jdbc:mysql://localhost:3306/DestinationBuddy";
    
    // SE SEI SU MAC (VS CODE SU MAC, DATABASE SU VM): Togli i commenti a questa riga
    //private static final String URL = "jdbc:mysql://10.211.55.3:3306/DestinationBuddy";

    // ==========================================
    
    private static final String USER = "app_java";
    private static final String PASSWORD = "DestinationBuddy_ACR";

    // 1. IL VOSTRO METODO DI CONNESSIONE (aggiornato allo stile del prof)
    public static Connection connetti() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            // Se fallisce, usiamo la DAOException del prof!
            throw new DAOException(e);
        }
    }

    // 2. IL METODO MAGICO DEL PROFESSORE PER LE QUERY
    public static PreparedStatement prepare(Connection connection, String query, Object... values) throws SQLException {
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(query);
            for (int i = 0; i < values.length; i++) {
                // Questo ciclo imposta tutti i "?" in automatico!
                statement.setObject(i + 1, values[i]);
            }
            return statement;
        } catch (Exception e) {
            if (statement != null) {
                statement.close();
            }
            throw e;
        }
    }
}
