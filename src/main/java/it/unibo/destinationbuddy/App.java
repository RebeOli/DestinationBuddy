package it.unibo.destinationbuddy;

import it.unibo.destinationbuddy.data.DAOUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;

public class App extends Application {

    private Connection connection;

@Override
    public void start(Stage primaryStage) {
        try {
            System.out.println("🚀 Avvio di DestinationBuddy...");
            connection = DAOUtils.connetti();
            System.out.println("✅ Connessione stabilita.");

            // CREIAMO UNA FINESTRA VUOTA AL VOLO PER TEST
            javafx.scene.layout.StackPane root = new javafx.scene.layout.StackPane();
            root.getChildren().add(new javafx.scene.control.Label("Ciao! Il motore funziona!"));
            
            Scene scene = new Scene(root, 400, 300);
            primaryStage.setTitle("Destination Buddy");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // @Override
    // public void start(Stage primaryStage) {
    //     try {
    //         System.out.println("🚀 Avvio di DestinationBuddy...");

    //         // 1. Apriamo la connessione (Come fa il prof!)
    //         connection = DAOUtils.connetti();
    //         System.out.println("✅ Connessione stabilita.");

    //         // 2. Creeremo un "Model" centrale (Come fa il prof!)
    //         //Model model = new Model(connection);

    //         // 3. Diciamo a JavaFX di caricare il disegno fatto con Scene Builder
    //         FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/SchermataLogin.fxml"));
    //         Parent root = loader.load();

    //         // 4. Prendiamo il Controller della schermata e gli passiamo il Model!
    //         // LoginController controller = loader.getController();
    //         // controller.initModel(model);

    //         // 5. Mostriamo la finestra a schermo
    //         Scene scene = new Scene(root);
    //         primaryStage.setTitle("Destination Buddy");
    //         primaryStage.setScene(scene);
    //         primaryStage.setResizable(false); // Blocca la grandezza della finestra (opzionale)
    //         primaryStage.show();

    //     } catch (Exception e) {
    //         System.err.println("❌ Errore critico all'avvio dell'applicazione.");
    //         e.printStackTrace();
    //     }
    // }

    // Questo è l'esatto equivalente della funzione onClose() del prof.
    // Scatta in automatico quando cliccate la X per chiudere l'app.
    @Override
    public void stop() {
        if (connection != null) {
            try {
                System.out.println("🛑 Chiusura app. Disconnessione dal database...");
                connection.close();
            } catch (Exception ignored) {
                // Se dà errore in chiusura lo ignoriamo, proprio come fa il prof
            }
        }
    }

    public static void main(String[] args) {
        // // =========================================================
        // // TEST RAPIDO CONNESSIONE (Bypassa la grafica)
        // // =========================================================
        // System.out.println("⏳ Test del nuovo DAOUtils in corso...");
        
        // try (Connection testConn = DAOUtils.connetti()) {
        //     System.out.println("🎉 BINGO! Il refactoring è un successo! Motore acceso.");
        // } catch (Exception e) {
        //     System.err.println("❌ Ops, qualcosa è andato storto nel DAOUtils:");
        //     e.printStackTrace();
        // }
        launch(args);
    }
}