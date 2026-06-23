package it.unibo.destinationbuddy;

import static org.assertj.core.api.Assertions.*;

import it.unibo.destinationbuddy.data.DAOUtils;
import it.unibo.destinationbuddy.data.Persona;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Optional;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public final class UtentiTest {

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
                "VALUES ('TESTBIA99X99Y99Z', 'Bianca', 'Test', 'ACC-TEST-B', 'bianca@test.com', 'password123', 1, 0, 0, CURRENT_DATE);"
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
    public void testLoginCorretto() {
        Optional<Persona> utente = Persona.DAO.autentica(connection, "bianca@test.com", "password123");
        assertThat(utente).isPresent();
        assertThat(utente.get().nome).isEqualTo("Bianca");
        assertThat(utente.get().cf).isEqualTo("TESTBIA99X99Y99Z");
    }

    @Test
    public void testLoginErrato() {
        Optional<Persona> utente = Persona.DAO.autentica(connection, "bianca@test.com", "passwordSbagliata");
        assertThat(utente).isEmpty();
    }
}