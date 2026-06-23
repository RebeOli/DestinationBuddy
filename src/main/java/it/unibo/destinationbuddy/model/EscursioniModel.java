package it.unibo.destinationbuddy.model;

import java.util.List;
import java.util.Optional;

import it.unibo.destinationbuddy.data.Escursione;
import it.unibo.destinationbuddy.data.EscursionePreview;
import it.unibo.destinationbuddy.data.LuogoEsplorabile;
import it.unibo.destinationbuddy.data.TipologiaCertificazione;
import it.unibo.destinationbuddy.data.TipologiaEscursione;

public interface EscursioniModel {

    List<EscursionePreview> getTop5();

    List<EscursionePreview> getEscursioniPerMese(int mese);

    List<EscursionePreview> getAll();

    Optional<Escursione> getDettaglio(EscursionePreview escursionePreview);

    List<EscursionePreview> getEscursionePerTipologia(TipologiaEscursione tipologia);

    List<TipologiaEscursione> getTipologie();

    void creaEscursione(Escursione e, String descrizione, int numeroPartecipanti, String guidaCF,
            List<String> tipologie, List<String> certificazioniSelezionate, TipologiaCertificazione nuovaCertificazione);
    
    void aggiungiLuogoEsplorabile(LuogoEsplorabile l);

    List<String> getPaesi();

    List<String> getZonePerPaese(String paese);

    List<String> getCategorieLuoghi();

    List<EscursionePreview> getEscursioniGuida(String guidaCF);

    List<String> getLuoghiPerZona(String paese, String zona);

}
