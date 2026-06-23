package it.unibo.destinationbuddy;

import static org.assertj.core.api.Assertions.*;

import it.unibo.destinationbuddy.data.DAOUtils;
import it.unibo.destinationbuddy.data.Prenotazione;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public final class PrenotazioniTest {

    private static Connection connection;
    private static Savepoint savepoint;

    @BeforeClass
    public static void setup() throws SQLException {
        connection = DAOUtils.connetti();
        connection.setAutoCommit(false);
        savepoint = connection.setSavepoint();

        try (var statement = connection.createStatement()) {
            statement.executeUpdate(
                "INSERT INTO PERSONE (CF, nome, cognome, ID_account, email, password, tipo_utente, tipo_amministratore, escursioni_effettuate, data_iscrizione) " +
                "VALUES ('UTENTEPRENOTA000', 'Paolo', 'Turista', 'ACC-PREN-U', 'paolo@test.com', 'pwd', 1, 0, 0, CURRENT_DATE);"
            );

            statement.executeUpdate(
                "INSERT INTO PERSONE (CF, nome, cognome, ID_account, email, password, tipo_utente, tipo_amministratore, escursioni_effettuate, data_iscrizione) " +
                "VALUES ('GUIDAPRENOTA0000', 'Anna', 'Guida', 'ACC-PREN-G', 'anna@test.com', 'pwd', 0, 0, 0, CURRENT_DATE);"
            );

            statement.executeUpdate(
                "INSERT INTO GUIDE (CF, stato_account) VALUES ('GUIDAPRENOTA0000', 1);"
            );

            statement.executeUpdate(
                "INSERT INTO ESCURSIONI (ID_escursione, titolo, descrizione, difficolta, numero_partecipanti, costo, data_apertura_iscrizione, data_chiusura_iscrizione, Guida_CF) " +
                "VALUES ('ESC-TEST-PRENOTA', 'Gita di prova', 'Test prenotazione', 'Facile', 10, 20.00, '2024-01-01', '2030-01-01', 'GUIDAPRENOTA0000');"
            );
        }
    }

    @AfterClass
    public static void cleanup() throws SQLException {
        if (connection != null) {
            if (savepoint != null) {
                connection.rollback(savepoint);
            }
            connection.close();
        }
    }

    @Test
    public void testPostiScalatiDopoPrenotazione() {

        int postiIniziali = Prenotazione.DAO.getPostiRimanenti(connection, "ESC-TEST-PRENOTA");
        assertThat(postiIniziali).isEqualTo(10);

        boolean inserita = Prenotazione.DAO.confermaPrenotazione(connection, "UTENTEPRENOTA000", "ESC-TEST-PRENOTA");
        assertThat(inserita).isTrue();

        int postiFinali = Prenotazione.DAO.getPostiRimanenti(connection, "ESC-TEST-PRENOTA");
        assertThat(postiFinali).isEqualTo(9);
    }
}