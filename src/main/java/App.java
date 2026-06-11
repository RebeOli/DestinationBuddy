import java.sql.Connection;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) {
        System.out.println("🚀 Avvio di DestinationBuddy in corso...");

        // Guarda com'è pulito! Nessuna password, nessun IP, nessuna stringa chilometrica qui dentro.
        // Chiediamo semplicemente al "Tecnico" di darci la connessione.
        try (Connection conn = Database.connetti()) {
            
            System.out.println("✅ Connessione stabilita! Il motore è acceso.");
            
            // Da qui in poi, più avanti, scriveremo il codice per far apparire 
            // la primissima finestra grafica della vostra applicazione!

        } catch (SQLException e) {
            System.out.println("Errore critico all'avvio:");
            e.printStackTrace();
        }
    }
}