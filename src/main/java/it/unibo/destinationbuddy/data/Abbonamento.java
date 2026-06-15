package it.unibo.destinationbuddy.data;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.Optional;

public class Abbonamento {
    public final LocalDate dataAbbonamento;
    public final LocalDate dataPagamento;
    public final double costoMensile;
    public final int durata;
    public final String cf;

    public Abbonamento(LocalDate dataAbbonamento, LocalDate dataPagamento, double costoMensile, int durata, String cf) {
        this.dataAbbonamento = dataAbbonamento;
        this.dataPagamento = dataPagamento;
        this.costoMensile = costoMensile;
        this.durata = durata;
        this.cf = cf == null ? "" : cf;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dataAbbonamento == null) ? 0 : dataAbbonamento.hashCode());
        result = prime * result + ((dataPagamento == null) ? 0 : dataPagamento.hashCode());
        long temp;
        temp = Double.doubleToLongBits(costoMensile);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + durata;
        result = prime * result + ((cf == null) ? 0 : cf.hashCode());
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
        Abbonamento other = (Abbonamento) obj;
        if (dataAbbonamento == null) {
            if (other.dataAbbonamento != null)
                return false;
        } else if (!dataAbbonamento.equals(other.dataAbbonamento))
            return false;
        if (dataPagamento == null) {
            if (other.dataPagamento != null)
                return false;
        } else if (!dataPagamento.equals(other.dataPagamento))
            return false;
        if (Double.doubleToLongBits(costoMensile) != Double.doubleToLongBits(other.costoMensile))
            return false;
        if (durata != other.durata)
            return false;
        if (cf == null) {
            if (other.cf != null)
                return false;
        } else if (!cf.equals(other.cf))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Abbonamento [dataAbbonamento=" + dataAbbonamento + ", dataPagamento=" + dataPagamento
                + ", costoMensile=" + costoMensile + ", durata=" + durata + ", cf=" + cf + "]";
    }

    public boolean isAttivo() {
        return this.dataAbbonamento.plusMonths(durata).isAfter(LocalDate.now());
    }

    public static final class DAO {
        public static void acquistaAbbonamento(Connection connection, double costoMensile, int durata, String cf) {
            try (
                var statement = DAOUtils.prepare(connection, Queries.SOTTOSCRIVI_ABBONAMENTO, costoMensile, durata, cf);
            ) {
                statement.executeUpdate();
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }

        public static Optional<Abbonamento> trovaUltimoAbbonamento(Connection connection, Persona utente) {
            try (
                var statement = DAOUtils.prepare(connection, Queries.ULTIMO_ABBONAMENTO_UTENTE, utente.cf);
                var resultSet = statement.executeQuery();
            ) {
                if (resultSet.next()) {
                    var sqlDataAbbonamento = resultSet.getDate("data_abbonamento");
                    LocalDate dataAbbonamento = sqlDataAbbonamento.toLocalDate();
                    var sqlDataPagamento = resultSet.getDate("data_pagamento");
                    LocalDate dataPagamento = sqlDataPagamento.toLocalDate();
                    var costoMensile = resultSet.getDouble("costo_mensile");
                    var durata = resultSet.getInt("durata");

                    var ultimoAbbonamento = new Abbonamento(dataAbbonamento, dataPagamento, costoMensile, durata, utente.cf);

                    return Optional.of(ultimoAbbonamento);
                } else {
                    return Optional.empty();
                }
            } catch (Exception e) {
                throw new DAOException(e);
            }
        }

    }
}
