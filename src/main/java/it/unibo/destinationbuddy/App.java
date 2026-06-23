package it.unibo.destinationbuddy;

import it.unibo.destinationbuddy.controller.AppController;
import it.unibo.destinationbuddy.data.DAOUtils;
import it.unibo.destinationbuddy.model.*;
import javafx.application.Application;
import javafx.stage.Stage;
import java.sql.Connection;

/**
 * App — punto di ingresso JavaFX.
 * Crea la connessione, istanzia i Model, passa tutto all'AppController.
 * Nessuna logica di navigazione qui: tutto vive in AppController.
 */
public class App extends Application {

    private Connection connection;

    @Override
    public void start(Stage primaryStage) {
        try {
            System.out.println("🚀 Avvio di DestinationBuddy...");
            connection = DAOUtils.connetti();
            System.out.println("✅ Connessione stabilita.");

            // Crea i Model
            EscursioniModel     escursioniModel = new DBEscursioniModel(connection);
            UtentiModel         utentiModel     = new DBUtentiModel(connection);
            CertificazioniModel certsModel      = new DBCertificazioniModel(connection);
            AdminModel          adminModel      = new DBAdminModel(connection);
            PrenotazioniModel   prenotModel     = new DBPrenotazioniModel(connection);
            PostEscursioneModel postEscursioneModel = new DBPostEscursioneModel(connection);

            // Il controller fa tutto il resto
            AppController controller = new AppController(
                primaryStage,
                escursioniModel,
                utentiModel,
                certsModel,
                adminModel,
                prenotModel, 
                postEscursioneModel
            );
            controller.avvia();

        } catch (Exception e) {
            System.err.println("❌ Errore critico all'avvio:");
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        if (connection != null) {
            try {
                System.out.println("🛑 Chiusura. Disconnessione dal database...");
                connection.close();
            } catch (Exception ignored) {}
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
