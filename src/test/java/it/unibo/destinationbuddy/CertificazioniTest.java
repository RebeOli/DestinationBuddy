package it.unibo.destinationbuddy;

import static org.assertj.core.api.Assertions.*;

import it.unibo.destinationbuddy.data.DAOUtils;
import it.unibo.destinationbuddy.data.Certificazione;
import it.unibo.destinationbuddy.model.DBCertificazioniModel;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public final class CertificazioniTest {
    private static Connection connection;
    private static Savepoint savepoint;
    private static DBCertificazioniModel model;

    @BeforeClass
    public static void setup() throws SQLException {
        connection = DAOUtils.connetti();
        connection.setAutoCommit(false);
        savepoint = connection.setSavepoint();
        model = new DBCertificazioniModel(connection);

        try (var statement = connection.createStatement()) {

            statement.executeUpdate(
                "INSERT INTO PERSONE (CF, nome, cognome, ID_account, email, password, tipo_utente, tipo_amministratore, escursioni_effettuate, data_iscrizione) " +
                "VALUES ('CFTESTCERT000000', 'Giovanni', 'Certificato', 'ACC-CERT-T', 'giovanni@test.com', 'pwd', 1, 0, 0, CURRENT_DATE);"
            );

            statement.executeUpdate(
                "INSERT INTO TIPOLOGIE_CERTIFICAZIONE (ID_certificazione, livello) " +
                "VALUES ('CERT-TEST-LOG', 'Primo Soccorso');"
            );

            statement.executeUpdate(
                "INSERT INTO CERTIFICAZIONI (ID_certificazione, n_certificazione, ente_rilasciante, data_rilascio, data_scadenza, stato_validazione, CF, Guida_CF) " +
                "VALUES ('CERT-TEST-LOG', 'N-999-TEST', 'Croce Rossa', '2025-01-01', '2030-01-01', 'In attesa di validazione', 'CFTESTCERT000000', NULL);"
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
    public void testValidazioneCertificazioneFlusso() {
        List<Certificazione> inAttesaIniziali = model.getCertificazioniInAttesa();
        assertThat(inAttesaIniziali)
            .extracting(c -> c.nCertificazione)
            .contains("N-999-TEST");

        model.validaCertificazione("CERT-TEST-LOG", "N-999-TEST");
        List<Certificazione> inAttesaDopo = model.getCertificazioniInAttesa();
        assertThat(inAttesaDopo)
            .extracting(c -> c.nCertificazione)
            .doesNotContain("N-999-TEST");

        try (var stmtCheck = DAOUtils.prepare(connection, 
                "SELECT stato_validazione FROM CERTIFICAZIONI WHERE ID_certificazione = ? AND n_certificazione = ?", 
                "CERT-TEST-LOG", "N-999-TEST");
             var rs = stmtCheck.executeQuery()) {
            assertThat(rs.next()).isTrue();
            String statoAttuale = rs.getString("stato_validazione");
            assertThat(statoAttuale).isEqualTo("Validata");

        } catch (SQLException e) {
            fail("Errore durante il controllo dello stato sul database", e);
        }
    }
}
