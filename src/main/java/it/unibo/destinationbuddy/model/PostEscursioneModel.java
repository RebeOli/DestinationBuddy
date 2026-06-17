package it.unibo.destinationbuddy.model;

import it.unibo.destinationbuddy.data.Recensione;
import it.unibo.destinationbuddy.data.Resoconto;

public interface PostEscursioneModel {

    boolean inserisciRecensione(Recensione r);
    boolean inserisciResoconto(Resoconto r);
}
