package it.unibo.destinationbuddy.data;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public final class Persona {
    public String cf;
    public String nome;
    public String cognome;
    public boolean tipoUtente;
    public boolean tipoAmministratore;
    public String idAccount;
    public int escursioniEffettuate;
    public LocalDate dataIscrizione;
    public LocalDate dataAssunzione;
    public String email;
    public String password;
    public String statoAccount; // Sarà null per gli utenti normali, "attivo" o "disattivo" per le guide

    public List<Certificazione> certificazioni = new ArrayList<>();
    //public String ruolo;

    public Persona(String cf, String nome, String cognome, boolean tipoUtente, boolean tipoAmministratore,
            String idAccount, int escursioniEffettuate, LocalDate dataIscrizione, LocalDate dataAssunzione,
            String email, String password, String statoAccount) {
        this.cf = cf;
        this.nome = nome == null ? "" : nome;
        this.cognome = cognome == null ? "" : cognome;
        this.tipoUtente = tipoUtente;
        this.tipoAmministratore = tipoAmministratore;
        this.idAccount = idAccount == null ? "" : idAccount;
        this.escursioniEffettuate = escursioniEffettuate;
        this.dataIscrizione = dataIscrizione;
        this.dataAssunzione = dataAssunzione;
        this.email = email == null ? "" : email;
        this.password = password == null ? "" : password;
        this.statoAccount = statoAccount == null ? "" : statoAccount;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cf == null) ? 0 : cf.hashCode());
        result = prime * result + ((nome == null) ? 0 : nome.hashCode());
        result = prime * result + ((cognome == null) ? 0 : cognome.hashCode());
        result = prime * result + (tipoUtente ? 1231 : 1237);
        result = prime * result + (tipoAmministratore ? 1231 : 1237);
        result = prime * result + ((idAccount == null) ? 0 : idAccount.hashCode());
        result = prime * result + escursioniEffettuate;
        result = prime * result + ((dataIscrizione == null) ? 0 : dataIscrizione.hashCode());
        result = prime * result + ((dataAssunzione == null) ? 0 : dataAssunzione.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((statoAccount == null) ? 0 : statoAccount.hashCode());
        result = prime * result + ((certificazioni == null) ? 0 : certificazioni.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Persona other = (Persona) obj;
        if (cf == null) {
            if (other.cf != null)
                return false;
        } else if (!cf.equals(other.cf))
            return false;
        if (nome == null) {
            if (other.nome != null)
                return false;
        } else if (!nome.equals(other.nome))
            return false;
        if (cognome == null) {
            if (other.cognome != null)
                return false;
        } else if (!cognome.equals(other.cognome))
            return false;
        if (tipoUtente != other.tipoUtente)
            return false;
        if (tipoAmministratore != other.tipoAmministratore)
            return false;
        if (idAccount == null) {
            if (other.idAccount != null)
                return false;
        } else if (!idAccount.equals(other.idAccount))
            return false;
        if (escursioniEffettuate != other.escursioniEffettuate)
            return false;
        if (dataIscrizione == null) {
            if (other.dataIscrizione != null)
                return false;
        } else if (!dataIscrizione.equals(other.dataIscrizione))
            return false;
        if (dataAssunzione == null) {
            if (other.dataAssunzione != null)
                return false;
        } else if (!dataAssunzione.equals(other.dataAssunzione))
            return false;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        if (statoAccount == null) {
            if (other.statoAccount != null)
                return false;
        } else if (!statoAccount.equals(other.statoAccount))
            return false;
        if (certificazioni == null) {
            if (other.certificazioni != null)
                return false;
        } else if (!certificazioni.equals(other.certificazioni))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Persona [cf=" + cf + ", nome=" + nome + ", cognome=" + cognome + ", tipoUtente=" + tipoUtente
                + ", tipoAmministratore=" + tipoAmministratore + ", idAccount=" + idAccount + ", escursioniEffettuate="
                + escursioniEffettuate + ", dataIscrizione=" + dataIscrizione + ", dataAssunzione=" + dataAssunzione
                + ", email=" + email + ", password=" + password + ", stato_account=" + statoAccount
                + ", certificazioni=" + certificazioni + "]";
    }

    public boolean puoPrenotare(Escursione escursione) {
        var tipo_certificazioni = new HashSet<>();
        for (var c : certificazioni) {
            tipo_certificazioni.add(c.tipologia);
        }
        return tipo_certificazioni.containsAll(escursione.certificazioniRichieste);
    }

    public void aggiungiCertificazione(Certificazione c) {
        this.certificazioni.add(c);
    }

    public boolean isAmministratore() {
        return this.tipoAmministratore;
    }

    public static final class DAO {
        public static Optional<Persona> autentica (Connection connection, String email, String password) {
        try (
            var statement = DAOUtils.prepare(connection, Queries.AUTENTICA_PERSONA, email, password);
            var resultSet = statement.executeQuery();
        ) {
            if (resultSet.next()) {
                var cf = resultSet.getString("CF");
                var nome = resultSet.getString("nome");
                var cognome = resultSet.getString("cognome");
                var tipoUtente = resultSet.getBoolean("tipo_utente");
                var tipoAmministratore = resultSet.getBoolean("tipo_amministratore");
                var idAccount = resultSet.getString("ID_account");
                var escursioniEffettuate = resultSet.getInt("escursioni_effettuate");
                var sqlDataIscrizione = resultSet.getDate("data_iscrizione");
                LocalDate dataIscrizione = sqlDataIscrizione.toLocalDate();
                var sqlDataAssunzione = resultSet.getDate("data_assunzione");
                LocalDate dataAssunzione = (sqlDataAssunzione != null) ? sqlDataAssunzione.toLocalDate() : null;
                var statoAccount = resultSet.getString("stato_account");
                Persona utente = new Persona(cf, nome, cognome, tipoUtente, tipoAmministratore, idAccount, escursioniEffettuate, dataIscrizione, dataAssunzione, email, password, statoAccount);

                caricaCertificazioni(connection, utente);

                return Optional.of(utente);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new DAOException(e);
        }

    }

        public static void caricaCertificazioni(Connection connection, Persona utente) {
            var listaCert = Certificazione.DAO.listForUtente(connection, utente.cf);
            for (var cert : listaCert) {
                utente.aggiungiCertificazione(cert);
            }
        }

        public static void registraUtente(Connection connection, Persona u) {
            try (
                var statement = DAOUtils.prepare(connection, Queries.REGISTRA_PERSONA, u.cf, u.nome, u.cognome, u.idAccount, u.email, u.password);
            ) {
                statement.executeUpdate();
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }

        public static List<Persona> getUtentiDaPremiare(Connection connection) {
            final List<Persona> utenti = new ArrayList<>();
            try (
                var statement = DAOUtils.prepare(connection, Queries.UTENTI_TUTTI_PAESI);
                var resultSet = statement.executeQuery();
            ) {
                while (resultSet.next()) {
                    var cf = resultSet.getString("CF");
                    var nome = resultSet.getString("nome");
                    var cognome = resultSet.getString("cognome");
                    var tipoUtente = resultSet.getBoolean("tipo_utente");
                    var tipoAmministratore = resultSet.getBoolean("tipo_amministratore");
                    var idAccount = resultSet.getString("ID_account");
                    var escursioniEffettuate = resultSet.getInt("escursioni_effettuate");
                    var sqlDataIscrizione = resultSet.getDate("data_iscrizione");
                    LocalDate dataIscrizione = sqlDataIscrizione.toLocalDate();
                    var sqlDataAssunzione = resultSet.getDate("data_assunzione");
                    LocalDate dataAssunzione = (sqlDataAssunzione != null) ? sqlDataAssunzione.toLocalDate() : null;
                    var email = resultSet.getString("email");
                    var password = resultSet.getString("password");
                    var statoAccount = resultSet.getString("stato_account");
                    Persona utente = new Persona(cf, nome, cognome, tipoUtente, tipoAmministratore, idAccount, escursioniEffettuate, dataIscrizione, dataAssunzione, email, password, statoAccount);

                    utenti.add(utente);

                }
            } catch (Exception e) {
                throw new DAOException(e);
            }

            return utenti;

        }

        public static void disattivaGuida(Connection connection, Persona guida) {
            try (
                var statement = DAOUtils.prepare(connection, Queries.SOSPENDI_GUIDA, guida.cf);
            ) {
                statement.executeUpdate();
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }

        public static void attivaGuida(Connection connection, Persona guida) {
            try (
                var statement = DAOUtils.prepare(connection, Queries.RIATTIVA_GUIDA, guida.cf);
            ) {
                statement.executeUpdate();
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }
    }
}
