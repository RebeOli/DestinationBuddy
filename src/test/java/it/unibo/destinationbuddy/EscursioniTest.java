package it.unibo.destinationbuddy;

import static org.assertj.core.api.Assertions.*;

import it.unibo.destinationbuddy.data.DAOUtils;
import it.unibo.destinationbuddy.data.Escursione;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Optional;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public final class EscursioniTest {

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
                "VALUES ('GUIDAESC00000000', 'Claudio', 'Alpinista', 'ACC-ESC-G', 'claudio@test.com', 'pwd', 0, 0, 0, CURRENT_DATE);"
            );
            statement.executeUpdate(
                "INSERT INTO GUIDE (CF, stato_account) VALUES ('GUIDAESC00000000', 1);"
            );

            statement.executeUpdate(
                "INSERT INTO ESCURSIONI (ID_escursione, titolo, descrizione, difficolta, numero_partecipanti, costo, data_apertura_iscrizione, data_chiusura_iscrizione, Guida_CF) " +
                "VALUES ('ESC-DA-TESTARE', 'Monte Cimone', 'Bellissimo trekking', 'Difficile', 15, 30.00, '2024-01-01', '2030-01-01', 'GUIDAESC00000000');"
            );

            statement.executeUpdate(
                "INSERT INTO assume (ID_escursione, ID_tipologia) VALUES ('ESC-DA-TESTARE', 'Trekking');"
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
    public void testRecuperoDettagliEscursione() {
        Optional<Escursione> escursioneOttenuta = Escursione.DAO.find(connection, "ESC-DA-TESTARE");
        assertThat(escursioneOttenuta).isPresent(); 
        Escursione esc = escursioneOttenuta.get();
        assertThat(esc.titolo).isEqualTo("Monte Cimone");
        assertThat(esc.difficolta).isEqualTo("Difficile");
        assertThat(esc.costo).isEqualTo(30.00);
        assertThat(esc.guidaNome).isEqualTo("Claudio");
        assertThat(esc.guidaCognome).isEqualTo("Alpinista");
    }
}