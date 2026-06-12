package it.unibo.destinationbuddy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    
    // ==========================================
    // AREA DA DECOMMENTARE IN BASE AL COMPUTER
    // ==========================================
    
    // SE SEI SU PC WINDOWS LOCALE: Togli i commenti a questa riga
    private static final String URL = "jdbc:mysql://localhost:3306/DestinationBuddy";
    
    // SE SEI SU MAC (VS CODE SU MAC, DATABASE SU VM): Togli i commenti a questa riga e metti l'IP della tua VM
    //private static final String URL = "jdbc:mysql://10.211.55.3:3306/DestinationBuddy";
    //private static final String URL = "jdbc:mysql://10.211.55.3:3306/destinationbuddy";

    // ==========================================
    
    private static final String USER = "app_java";
    private static final String PASSWORD = "DestinationBuddy_ACR";

    // Questo metodo verrà usato da tutto il resto del programma!
    public static Connection connetti() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
