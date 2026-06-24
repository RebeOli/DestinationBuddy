package it.unibo.destinationbuddy.data;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
/**
 * Questa classe rappresenta l'utente, la guida, e l'amministratore. 
 */
public final class Persona {
    public String cf;
    public String nome;
    public String cognome;
    public boolean tipoUtente;
    public boolean tipoAmministratore;
    public int escursioniEffettuate;
    public LocalDate dataIscrizione;
    public LocalDate dataAssunzione;
    public String email;
    public String password;
    public boolean statoAccount; 

    public Persona(String cf, String nome, String cognome, boolean tipoUtente, boolean tipoAmministratore,
            int escursioniEffettuate, LocalDate dataIscrizione, LocalDate dataAssunzione,
            String email, String password, boolean statoAccount) {
        this.cf = cf;
        this.nome = nome == null ? "" : nome;
        this.cognome = cognome == null ? "" : cognome;
        this.tipoUtente = tipoUtente;
        this.tipoAmministratore = tipoAmministratore;
        this.escursioniEffettuate = escursioniEffettuate;
        this.dataIscrizione = dataIscrizione;
        this.dataAssunzione = dataAssunzione;
        this.email = email == null ? "" : email;
        this.password = password == null ? "" : password;
        this.statoAccount = statoAccount;
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
        result = prime * result + escursioniEffettuate;
        result = prime * result + ((dataIscrizione == null) ? 0 : dataIscrizione.hashCode());
        result = prime * result + ((dataAssunzione == null) ? 0 : dataAssunzione.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + (statoAccount ? 1231 : 1237);
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
        if (statoAccount != other.statoAccount)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Persona [cf=" + cf + ", nome=" + nome + ", cognome=" + cognome + ", tipoUtente=" + tipoUtente
                + ", tipoAmministratore=" + tipoAmministratore + ", escursioniEffettuate="
                + escursioniEffettuate + ", dataIscrizione=" + dataIscrizione + ", dataAssunzione=" + dataAssunzione
                + ", email=" + email + ", password=" + password + ", stato_account=" + statoAccount + "]";
    }

    public boolean isAmministratore() {
        return this.tipoAmministratore;
    }

    public static final class DAO {
        public static Optional<Persona> autentica (Connection connection, String email, String password) {
            try (var statement = DAOUtils.prepare(connection, Queries.AUTENTICA_PERSONA, email, password);
                 var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    var cf = resultSet.getString("CF");
                    var nome = resultSet.getString("nome");
                    var cognome = resultSet.getString("cognome");
                    var tipoUtente = resultSet.getBoolean("tipo_utente");
                    var tipoAmministratore = resultSet.getBoolean("tipo_amministratore");
                    var escursioniEffettuate = resultSet.getInt("escursioni_effettuate");
                    var sqlDataIscrizione = resultSet.getDate("data_iscrizione");
                    LocalDate dataIscrizione = (sqlDataIscrizione != null) ? sqlDataIscrizione.toLocalDate() : null;
                    var sqlDataAssunzione = resultSet.getDate("data_assunzione");
                    LocalDate dataAssunzione = (sqlDataAssunzione != null) ? sqlDataAssunzione.toLocalDate() : null;

                    var statoAccount = resultSet.getBoolean("stato_account");
                    var utente = new Persona(cf, nome, cognome, tipoUtente, tipoAmministratore, escursioniEffettuate, dataIscrizione, dataAssunzione, email,password, statoAccount);
                    return Optional.of(utente);
                } else {
                    return Optional.empty();
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }

        public static void incrementaEscursioniEffettuate(Connection connection, Escursione esc) {
            try (var statement = DAOUtils.prepare(connection, Queries.AGGIORNA_ESCURSIONI_EFFETTUATE, esc.idEscursione)) {
                statement.executeUpdate();
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }

        public static void registraUtente(Connection connection, Persona u) {
            try (var statement = DAOUtils.prepare(connection, Queries.REGISTRA_PERSONA, u.cf, u.nome, u.cognome, u.email, u.password)) {
                statement.executeUpdate();
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }

        public static List<Persona> getUtentiDaPremiare(Connection connection) {
            final List<Persona> utenti = new ArrayList<>();
            try (var statement = DAOUtils.prepare(connection, Queries.UTENTI_TUTTI_PAESI);
                 var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String cf = "";
                    try { cf = resultSet.getString("CF"); } catch(Exception ignored) {}
                    if (cf != null && !cf.isEmpty()) {
                        try (var stDettagli = DAOUtils.prepare(connection, "SELECT * FROM PERSONE WHERE CF = ?", cf);
                             var rsDettagli = stDettagli.executeQuery()) {
                            if (rsDettagli.next()) {
                                Persona utente = new Persona(
                                    cf, rsDettagli.getString("nome"), rsDettagli.getString("cognome"),
                                    rsDettagli.getBoolean("tipo_utente"), rsDettagli.getBoolean("tipo_amministratore"), rsDettagli.getInt("escursioni_effettuate"),
                                    rsDettagli.getDate("data_iscrizione") != null ? rsDettagli.getDate("data_iscrizione").toLocalDate() : null,
                                    rsDettagli.getDate("data_assunzione") != null ? rsDettagli.getDate("data_assunzione").toLocalDate() : null,
                                    rsDettagli.getString("email"), rsDettagli.getString("password"), false
                                );
                                utenti.add(utente);
                            }
                        } catch(Exception ignored) {}
                    }
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }
            return utenti;
        }

        public static List<Persona> getTutteLeGuide(Connection connection) {
            final List<Persona> guide = new ArrayList<>();
            String query = "SELECT p.*, g.stato_account AS stato_guida FROM PERSONE p JOIN GUIDE g ON p.CF = g.CF";
            try (var statement = DAOUtils.prepare(connection, query);
                 var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Persona guida = new Persona(
                        resultSet.getString("CF"), resultSet.getString("nome"), resultSet.getString("cognome"),
                        resultSet.getBoolean("tipo_utente"), resultSet.getBoolean("tipo_amministratore"), resultSet.getInt("escursioni_effettuate"),
                        resultSet.getDate("data_iscrizione") != null ? resultSet.getDate("data_iscrizione").toLocalDate() : null,
                        resultSet.getDate("data_assunzione") != null ? resultSet.getDate("data_assunzione").toLocalDate() : null,
                        resultSet.getString("email"), resultSet.getString("password"), 
                        resultSet.getBoolean("stato_guida") 
                    );
                    guide.add(guida);
                }
            } catch (Exception e) {
                System.out.println("Errore caricamento guide: " + e.getMessage());
            }
            return guide;
        }

        public static List<String> getGuideSospendibili(Connection connection) {
            List<String> cfSospendibili = new ArrayList<>();
            try (var statement = DAOUtils.prepare(connection, Queries.GUIDE_DA_SOSPENDERE);
                var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    cfSospendibili.add(resultSet.getString("CF"));
                }
            } catch (Exception e) {
                System.err.println("Errore caricamento guide da sospendere: " + e.getMessage());
            }
            return cfSospendibili;
        }

        public static void disattivaGuida(Connection connection, Persona guida) {
            try (var statement = DAOUtils.prepare(connection, Queries.SOSPENDI_GUIDA, guida.cf)) {
                statement.executeUpdate();
            } catch (Exception e) {
                System.err.println("ERRORE DATABASE (Sospendi): " + e.getMessage());
                throw new DAOException(e);
            }
        }

        public static void attivaGuida(Connection connection, Persona guida) {
            try (var statement = DAOUtils.prepare(connection, Queries.RIATTIVA_GUIDA, guida.cf)) {
                statement.executeUpdate();
            } catch (Exception e) {
                System.err.println("ERRORE DATABASE (Attiva): " + e.getMessage());
                throw new DAOException(e);
            }
        }
    }
}