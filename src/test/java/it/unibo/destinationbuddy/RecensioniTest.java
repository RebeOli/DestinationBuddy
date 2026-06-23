package it.unibo.destinationbuddy;

import static org.assertj.core.api.Assertions.*;

import it.unibo.destinationbuddy.data.DAOUtils;
import it.unibo.destinationbuddy.data.Recensione;
import it.unibo.destinationbuddy.model.DBAdminModel;
import it.unibo.destinationbuddy.model.DBPrenotazioniModel;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public final class RecensioniTest {

    private static Connection connection;
    private static Savepoint savepoint;
    private static DBPrenotazioniModel prenotazioniModel;
    private static DBAdminModel adminModel;

    @BeforeClass
    public static void setup() throws SQLException {
        connection = DAOUtils.connetti();
        connection.setAutoCommit(false);
        savepoint = connection.setSavepoint();
        prenotazioniModel = new DBPrenotazioniModel(connection);
        adminModel = new DBAdminModel(connection);

        try (var statement = connection.createStatement()) {
            statement.executeUpdate(
                "INSERT INTO PERSONE (CF, nome, cognome, ID_account, email, password, tipo_utente, tipo_amministratore, escursioni_effettuate, data_iscrizione) " +
                "VALUES ('GUIDAREC00000000', 'Marco', 'Guida', 'ACC-REC-G', 'marco@test.com', 'pwd', 0, 0, 0, CURRENT_DATE);"
            );
            statement.executeUpdate(
                "INSERT INTO GUIDE (CF, stato_account) VALUES ('GUIDAREC00000000', 1);"
            );

            statement.executeUpdate(
                "INSERT INTO PERSONE (CF, nome, cognome, ID_account, email, password, tipo_utente, tipo_amministratore, escursioni_effettuate, data_iscrizione) " +
                "VALUES ('UTENTEREC0000000', 'Luca', 'Rossi', 'ACC-REC-U', 'luca@test.com', 'pwd', 1, 0, 0, CURRENT_DATE);"
            );

            statement.executeUpdate(
                "INSERT INTO ESCURSIONI (ID_escursione, titolo, descrizione, difficolta, numero_partecipanti, costo, data_apertura_iscrizione, data_chiusura_iscrizione, Guida_CF) " +
                "VALUES ('ESC-TEST-REC', 'Trekking Recensioni', 'Test', 'Intermedia', 10, 15.00, '2024-01-01', '2030-01-01', 'GUIDAREC00000000');"
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
    public void testFlussoRecensioni() {
        List<Recensione> recensioniIniziali = prenotazioniModel.getRecensioniPerEscursione("ESC-TEST-REC");
        assertThat(recensioniIniziali).isEmpty();
        Recensione nuovaRecensione = new Recensione(
            "Bellissimo!",
            "UTENTEREC0000000",
            5,
            "",
            "Percorso fantastico",
            "Approvata",
            "ESC-TEST-REC"
        );

        boolean inserito = prenotazioniModel.inserisciRecensione(nuovaRecensione);
        assertThat(inserito).isTrue();
        List<Recensione> recensioniDopoInserimento = prenotazioniModel.getRecensioniPerEscursione("ESC-TEST-REC");
        assertThat(recensioniDopoInserimento).hasSize(1);
        assertThat(recensioniDopoInserimento.get(0).titolo).isEqualTo("Bellissimo!");
        adminModel.eliminaRecensione("UTENTEREC0000000", "ESC-TEST-REC");
        List<Recensione> recensioniDopoEliminazione = prenotazioniModel.getRecensioniPerEscursione("ESC-TEST-REC");
        assertThat(recensioniDopoEliminazione).isEmpty();
    }
}