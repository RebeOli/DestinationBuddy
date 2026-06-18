package it.unibo.destinationbuddy.model.mocked;

import it.unibo.destinationbuddy.data.Persona;
import it.unibo.destinationbuddy.model.AdminModel;

import java.time.LocalDate;
import java.util.List;

public class MockedAdminModel implements AdminModel {

    @Override
    public List<Persona> getUtentiDaPremiare() {
        return List.of(
            new Persona(
                "RSSMRA80A01H501U",
                "Mario",
                "Rossi",
                true,
                false,
                "ACC-001",
                15,
                LocalDate.of(2022, 1, 10),
                null,
                "mario.rossi@email.com",
                "password123",
                null
            ),
            new Persona(
                "BNCGCM90B02A001Z",
                "Giacomo",
                "Bianchi",
                true,
                false,
                "ACC-002",
                20,
                LocalDate.of(2021, 5, 20),
                null,
                "giacomo.bianchi@email.com",
                "pass456",
                null
            )
        );
    }

    @Override
    public void disattivaGuida(Persona guida) {
        System.out.println("[MOCK] Amministratore: La guida " + guida.nome + " " + guida.cognome + " è stata DISATTIVATA.");
    }

    @Override
    public void attivaGuida(Persona guida) {
        System.out.println("[MOCK] Amministratore: La guida " + guida.nome + " " + guida.cognome + " è stata ATTIVATA.");
    }
}