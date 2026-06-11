import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) {
        
        String url = "jdbc:mysql://localhost:3306/DestinationBuddy";
        String user = "app_java";
        String password = "DestinationBuddy_ACR"; // Rimetti la tua password vera
        
        System.out.println("⏳ Connessione in corso...\n");

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            
            System.out.println("🎉 BINGO! Connessi al database.");
            System.out.println("-----------------------------------");
            
            // 1. Scriviamo la nostra query SQL
            String query = "SELECT nome, cognome, email FROM PERSONE";
            
            // 2. Prepariamo la query ed eseguiamola
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {
                 
                System.out.println("📋 ELENCO UTENTI REGISTRATI:");
                
                // 3. Il ciclo while legge i risultati riga per riga finché ce ne sono
                while (rs.next()) {
                    String nome = rs.getString("nome");
                    String cognome = rs.getString("cognome");
                    String email = rs.getString("email");
                    
                    System.out.println("- " + nome + " " + cognome + " (" + email + ")");
                }
            }
            System.out.println("-----------------------------------");
            
        } catch (SQLException e) {
            System.out.println("❌ Errore di connessione o di esecuzione query:");
            e.printStackTrace();
        }
    }
}