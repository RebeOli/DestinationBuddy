package it.unibo.destinationbuddy.data;

public final class Queries {

    private Queries() {}

    // ==================== OPERAZIONE 1: Registrazione utente ====================

    public static final String REGISTRA_PERSONA =
        """
        INSERT INTO PERSONE (CF, nome, cognome, tipo_utente,
            tipo_amministratore, ID_account, escursioni_effettuate,
            data_iscrizione, email, password)
        VALUES (?, ?, ?, 1, 0, ?, 0, CURRENT_DATE, ?, ?)
        """;

    public static final String SOTTOSCRIVI_ABBONAMENTO =
        """
        INSERT INTO ABBONAMENTI (data_abbonamento, costo_mensile,
            durata, data_pagamento, CF)
        VALUES (CURRENT_DATE, ?, ?, CURRENT_DATE, ?)
        """;

    public static final String INSERISCI_CERTIFICAZIONE =
        """
        INSERT INTO CERTIFICAZIONI (ID_certificazione, n_certificazione,
            ente_rilasciante, data_rilascio, data_scadenza,
            stato_validazione, CF, Guida_CF)
        VALUES (?, ?, ?, ?, ?, 'In attesa di validazione', ?, NULL)
        """;

    // ==================== OPERAZIONE 2: Creazione escursione ====================

    public static final String INSERISCI_ESCURSIONE =
        """
        INSERT INTO ESCURSIONI (ID_escursione, titolo, descrizione,
            difficolta, numero_partecipanti, costo,
            data_apertura_iscrizione, data_chiusura_iscrizione, Guida_CF)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

    public static final String INSERISCI_GIORNATA =
        """
        INSERT INTO GIORNATE (ID_escursione, data, programma)
        VALUES (?, ?, ?)
        """;

    public static final String INSERISCI_TAPPA =
        """
        INSERT INTO TAPPE (ID_tappa, durata, ID_escursione, data,
            nome_paese, nome_zona, nome_luogo)
        VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

    // ==================== OPERAZIONE 3: Prenotazione ====================

    public static final String CONTROLLA_ACCESSO_PRIORITARIO =
        """
        SELECT TA.vantaggi_accesso
        FROM ABBONAMENTI A
        JOIN TIPOLOGIE_ABBONAMENTO TA ON A.durata = TA.durata
        WHERE A.CF = ?
          AND CURRENT_DATE BETWEEN A.data_abbonamento
          AND DATE_ADD(A.data_abbonamento, INTERVAL TA.durata MONTH)
        """;

    public static final String POSTI_RIMANENTI =
        """
        SELECT (E.numero_partecipanti - COUNT(p.CF)) AS posti_rimanenti
        FROM ESCURSIONI E
        LEFT JOIN prenota p ON E.ID_escursione = p.ID_escursione
        WHERE E.ID_escursione = ?
        GROUP BY E.ID_escursione, E.numero_partecipanti
        """;

    public static final String VERIFICA_CERTIFICAZIONI =
        """
        SELECT
            (SELECT COUNT(*)
             FROM richiede r
             JOIN assume a ON r.ID_tipologia = a.ID_tipologia
             WHERE a.ID_escursione = ?) AS cert_richieste,
            (SELECT COUNT(*)
             FROM CERTIFICAZIONI c
             WHERE c.CF = ?
               AND c.stato_validazione = 'Validata'
               AND c.ID_certificazione IN (
                   SELECT r.ID_certificazione
                   FROM richiede r
                   JOIN assume a ON r.ID_tipologia = a.ID_tipologia
                   WHERE a.ID_escursione = ?
               )) AS cert_possedute_valide
        """;

    public static final String CONFERMA_PRENOTAZIONE =
        """
        INSERT INTO prenota (CF, ID_escursione, data_prenotazione, stato)
        VALUES (?, ?, CURRENT_DATE, 'Confermata')
        """;

    public static final String SCONTO_NOLEGGIO =
        """
        SELECT TA.sconto_noleggio
        FROM ABBONAMENTI A
        JOIN TIPOLOGIE_ABBONAMENTO TA ON A.costo_mensile = TA.costo_mensile
            AND A.durata = TA.durata
        WHERE A.CF = ?
          AND CURRENT_DATE >= A.data_abbonamento
        """;

    public static final String PEZZO_DISPONIBILE =
        """
        SELECT ID_pezzo
        FROM PEZZI
        WHERE ID_categoria = ?
          AND disponibilita = 1
        LIMIT 1
        """;

    public static final String ASSEGNA_PEZZO =
        """
        INSERT INTO assegna (ID_pezzo, ID_escursione, CF, data_noleggio, durata_noleggio)
        VALUES (?, ?, ?, CURRENT_DATE, ?)
        """;

    public static final String AGGIORNA_DISPONIBILITA_PEZZO =
        """
        UPDATE PEZZI
        SET disponibilita = 0
        WHERE ID_pezzo = ?
        """;

    // ==================== OPERAZIONE 4: Recensione e resoconto ====================

    public static final String INSERISCI_RECENSIONE =
        """
        INSERT INTO RECENSIONI (titolo, voto, immagini, descrizione,
            stato_recensione, CF, ID_escursione)
        VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

    public static final String INSERISCI_RESOCONTO =
        """
        INSERT INTO RIEPILOGA (ID_escursione, data_inizio, data_fine,
            temperatura_rilevata, precipitazioni, CF)
        VALUES (?, ?, ?, ?, ?, ?)
        """;

    // ==================== OPERAZIONE 5: Sospensione/riattivazione guida ====================

    public static final String GUIDE_DA_SOSPENDERE =
        """
        SELECT G.CF, COUNT(R.ID_escursione) AS num_recensioni_negative
        FROM GUIDE G
        JOIN ESCURSIONI E ON E.Guida_CF = G.CF
        JOIN RECENSIONI R ON R.ID_escursione = E.ID_escursione
        WHERE R.voto <= 2
          AND G.stato_account = 1
        GROUP BY G.CF
        HAVING COUNT(R.ID_escursione) > 5
        """;

    public static final String SOSPENDI_GUIDA =
        """
        UPDATE GUIDE
        SET stato_account = 0
        WHERE CF = ?
        """;

    public static final String RIATTIVA_GUIDA =
        """
        UPDATE GUIDE
        SET stato_account = 1
        WHERE CF = ?
        """;

    // ==================== OPERAZIONE 6: Migliori escursioni per recensioni ====================

    public static final String MIGLIORI_ESCURSIONI =
        """
        SELECT E.ID_escursione, E.titolo, E.difficolta, E.costo,
            AVG(R.voto) AS media_voti,
            COUNT(R.voto) AS numero_recensioni
        FROM ESCURSIONI E
        JOIN RECENSIONI R ON E.ID_escursione = R.ID_escursione
        GROUP BY E.ID_escursione, E.titolo, E.difficolta, E.costo
        HAVING COUNT(R.voto) >= ?
        ORDER BY media_voti DESC
        LIMIT 5
        """;

    // ==================== OPERAZIONE 7: Escursioni più prenotate per mese ====================

    public static final String ESCURSIONI_PER_MESE =
        """
        SELECT e.ID_escursione, e.titolo, e.difficolta, e.costo,
            COUNT(p.CF) AS numero_prenotazioni
        FROM ESCURSIONI e
        JOIN prenota p ON e.ID_escursione = p.ID_escursione
        JOIN GIORNATE g ON g.ID_escursione = e.ID_escursione
        WHERE MONTH(g.data) = ?
        GROUP BY e.ID_escursione, e.titolo, e.difficolta, e.costo
        ORDER BY numero_prenotazioni DESC
        LIMIT 3
        """;

    // ==================== OPERAZIONE 8: Utenti con escursioni in tutti i paesi ====================

    public static final String UTENTI_TUTTI_PAESI =
        """
        SELECT P.CF, P.nome, P.cognome,
            COUNT(DISTINCT PA.Nome) AS numero_paesi_visitati
        FROM PERSONE P
        JOIN prenota PR ON PR.CF = P.CF
        JOIN ESCURSIONI E ON E.ID_escursione = PR.ID_escursione
        JOIN GIORNATE G ON G.ID_escursione = E.ID_escursione
        JOIN TAPPE T ON T.ID_escursione = G.ID_escursione AND T.data = G.data
        JOIN LUOGHI_ESPLORABILI L ON L.nome_paese = T.nome_paese
            AND L.nome_zona = T.nome_zona AND L.nome = T.nome_luogo
        JOIN ZONE Z ON Z.nome_paese = L.nome_paese AND Z.nome = L.nome_zona
        JOIN PAESI PA ON PA.Nome = Z.nome_paese
        WHERE P.tipo_utente = 1
        GROUP BY P.CF, P.nome, P.cognome
        HAVING COUNT(DISTINCT PA.Nome) = (SELECT COUNT(*) FROM PAESI)
        """;

    // ==================== OPERAZIONE 9: Gestione certificazioni ====================

    public static final String CERTIFICAZIONI_IN_ATTESA =
        """
        SELECT C.ID_certificazione, C.n_certificazione, C.ente_rilasciante,
            C.data_rilascio, C.data_scadenza, C.stato_validazione, C.CF, C.Guida_CF,
            T.livello
        FROM CERTIFICAZIONI C
        JOIN PERSONE P ON P.CF = C.CF
        JOIN TIPOLOGIE_CERTIFICAZIONE T ON C.ID_certificazione = T.ID_certificazione
        WHERE C.stato_validazione = 'In attesa di validazione'
        """;

        public static final String VALIDA_CERTIFICAZIONE =
        """
        UPDATE CERTIFICAZIONI
        SET stato_validazione = 'Validata'
        WHERE ID_certificazione = ? AND n_certificazione = ?
        """;

    // ==================== OPERAZIONE 10: Escursioni effettuate da utente ====================

    public static final String ESCURSIONI_EFFETTUATE_UTENTE =
        """
        SELECT CF, nome, cognome, escursioni_effettuate
        FROM PERSONE
        WHERE CF = ?
        """;

    // ==================== OPERAZIONE 11: Costo totale equipaggiamento ====================

    public static final String COSTO_EQUIPAGGIAMENTO =
        """
        SELECT SUM(EQ.costo_totale_giornaliero) AS costo_totale_giornaliero_equip
        FROM ESCURSIONI E
        JOIN assume A ON E.ID_escursione = A.ID_escursione
        JOIN necessita N ON A.ID_tipologia = N.ID_tipologia
        JOIN EQUIPAGGIAMENTI EQ ON N.ID_categoria = EQ.ID_categoria
        WHERE E.ID_escursione = ?
        """;

    // ==================== OPERAZIONE 12: Inserimento tipologia escursione ====================

    public static final String INSERISCI_TIPOLOGIA_ESCURSIONE =
        """
        INSERT INTO TIPOLOGIE_ESCURSIONI (ID_tipologia)
        VALUES (?)
        """;

    public static final String ASSOCIA_CERTIFICAZIONE_TIPOLOGIA =
        """
        INSERT INTO richiede (ID_tipologia, ID_certificazione)
        VALUES (?, ?)
        """;

    public static final String PRENOTAZIONI_UTENTE =
        """
        SELECT p.CF, p.ID_escursione, p.data_prenotazione, 
            CASE 
                WHEN MAX(g.data) < CURRENT_DATE THEN 'Completata'
                WHEN MAX(g.data) >= CURRENT_DATE AND p.stato = 'Completata' THEN 'Confermata'
                ELSE p.stato 
            END AS stato,
            e.titolo
        FROM prenota p
        JOIN ESCURSIONI e ON p.ID_escursione = e.ID_escursione
        JOIN GIORNATE g ON e.ID_escursione = g.ID_escursione
        WHERE p.CF = ?
        GROUP BY p.CF, p.ID_escursione, p.data_prenotazione, p.stato, e.titolo
        ORDER BY p.data_prenotazione DESC
        """;

    public static final String LIST_ESCURSIONI =
        """
        SELECT ID_escursione, titolo, difficolta, costo
        FROM ESCURSIONI
        """;
    
    public static final String FIND_ESCURSIONE =
        """
        SELECT E.ID_escursione, E.titolo, E.difficolta, E.costo,
            (E.numero_partecipanti - COUNT(DISTINCT PR.CF)) AS posti_disponibili,
            E.data_apertura_iscrizione, E.data_chiusura_iscrizione,
            P.nome AS guida_nome, P.cognome AS guida_cognome, A.ID_tipologia
        FROM ESCURSIONI E
        JOIN PERSONE P ON E.Guida_CF = P.CF
        JOIN assume A ON E.ID_escursione = A.ID_escursione
        LEFT JOIN prenota PR ON E.ID_escursione = PR.ID_escursione
        WHERE E.ID_escursione = ?
        GROUP BY E.ID_escursione, E.titolo, E.difficolta, E.costo, E.numero_partecipanti,
                E.data_apertura_iscrizione, E.data_chiusura_iscrizione, P.nome, P.cognome, A.ID_tipologia
        """;

    public static final String CERTIFICAZIONI_RICHIESTE_ESCURSIONE =
        """
        SELECT DISTINCT TC.ID_certificazione, TC.livello
        FROM assume A
        JOIN richiede R ON A.ID_tipologia = R.ID_tipologia
        JOIN TIPOLOGIE_CERTIFICAZIONE TC ON R.ID_certificazione = TC.ID_certificazione
        WHERE A.ID_escursione = ?
        """;

    public static final String TAPPE_PER_GIORNATA =
        """
        SELECT ID_tappa, durata, ID_escursione, data, nome_paese, nome_zona, nome_luogo
        FROM TAPPE
        WHERE ID_escursione = ? AND data = ?
        """;

    public static final String GIORNATE_PER_ESCURSIONE =
        """
        SELECT ID_escursione, data, programma
        FROM GIORNATE
        WHERE ID_escursione = ?
        ORDER BY data
        """;
    
    public static final String EQUIPAGGIAMENTI_RICHIESTI_ESCURSIONE =
        """
        SELECT DISTINCT EQ.ID_categoria, EQ.costo_totale_giornaliero
        FROM assume A
        JOIN necessita N ON A.ID_tipologia = N.ID_tipologia
        JOIN EQUIPAGGIAMENTI EQ ON N.ID_categoria = EQ.ID_categoria
        WHERE A.ID_escursione = ?
        """;
    public static final String CERTIFICAZIONI_UTENTE =
        """
        SELECT c.ID_certificazione, c.n_certificazione, c.ente_rilasciante,
            c.data_rilascio, c.data_scadenza, c.stato_validazione, c.CF, c.Guida_CF,
            t.livello
        FROM CERTIFICAZIONI c
        JOIN TIPOLOGIE_CERTIFICAZIONE t ON c.ID_certificazione = t.ID_certificazione
        WHERE c.CF = ?
        """;

    public static final String TIPOLOGIE_CERTIFICAZIONE_ALL =
        """
        SELECT ID_certificazione, livello
        FROM TIPOLOGIE_CERTIFICAZIONE
        ORDER BY livello
        """;

    public static final String TIPOLOGIE_ESCURSIONE =
        """
        SELECT ID_tipologia
        FROM assume
        WHERE ID_escursione = ?
        """;

    //tabella associa -> escursione-tipologia
    public static final String ASSOCIA_ESCURSIONE_TIPOLOGIA =
        """
        INSERT INTO assume (ID_escursione, ID_tipologia)
        VALUES (?, ?)
        """;

    /*public static final String ESCURSIONI_PER_CATEGORIA =
        """
        SELECT DISTINCT E.ID_escursione, E.titolo, E.difficolta, E.costo
        FROM ESCURSIONI E
        JOIN GIORNATE G ON E.ID_escursione = G.ID_escursione
        JOIN TAPPE T ON G.ID_escursione = T.ID_escursione AND G.data = T.data
        JOIN LUOGHI_ESPLORABILI L ON T.nome_paese = L.nome_paese
            AND T.nome_zona = L.nome_zona
            AND T.nome_luogo = L.nome
        WHERE L.nome_categoria = ?
        """;*/
    // Per EscursionePreview.DAO
    public static final String ESCURSIONI_PER_TIPOLOGIA =
        """
        SELECT E.ID_escursione, E.titolo, E.difficolta, E.costo
        FROM ESCURSIONI E
        JOIN assume A ON E.ID_escursione = A.ID_escursione
        WHERE A.ID_tipologia = ?
        """;

    public static final String CATEGORIE_ALL =
        """
        SELECT nome_categoria
        FROM CATEGORIE
        ORDER BY nome_categoria
        """;

    //==================== QUERY PER PERSONA ====================
    public static final String AUTENTICA_PERSONA =
        """
        SELECT p.*, g.stato_account
        FROM PERSONE p
        LEFT JOIN GUIDE g ON p.CF = g.CF
        WHERE p.email = ? AND p.password = ?
        """;

    public static final String ULTIMO_ABBONAMENTO_UTENTE =
        """
        SELECT *
        FROM ABBONAMENTI
        WHERE CF = ?
        ORDER BY data_abbonamento DESC
        LIMIT 1
        """;

    public static final String AGGIORNA_ESCURSIONI_EFFETTUATE = 
        """
        UPDATE PERSONE
        SET escursioni_effettuate = escursioni_effettuate + 1
        WHERE CF IN (
            SELECT CF FROM prenota WHERE ID_escursione = ?
        )
        """;

    public static final String RECENSIONI_SCRITTE = 
        """
        SELECT *
        FROM RECENSIONI
        WHERE CF = ?
        """;

    public static final String LIST_TIPOLOGIE_ESCURSIONE =
        "SELECT ID_tipologia FROM tipologie_escursione";

    public static final String RECENSIONI_ESCURSIONE = 
        """
        SELECT R.*, P.nome, P.cognome
        FROM RECENSIONI R
        JOIN PERSONE P ON R.CF = P.CF
        WHERE R.ID_escursione = ?
        """;

    // ==================== OPERAZIONE: Moderazione Recensioni ====================
    public static final String GET_TUTTE_RECENSIONI = 
        """
        SELECT R.*, P.nome, P.cognome
        FROM RECENSIONI R
        JOIN PERSONE P ON R.CF = P.CF
        ORDER BY R.voto ASC
        """;

    public static final String ELIMINA_RECENSIONE = 
        "DELETE FROM RECENSIONI WHERE CF = ? AND ID_escursione = ?";

    public static final String TROVA_CF_CERTIFICAZIONE =
        """
        SELECT CF
        FROM CERTIFICAZIONI
        WHERE ID_certificazione = ? AND n_certificazione = ?
        """;

    public static final String TROVA_LIVELLO_TIPOLOGIA =
        """
        SELECT livello
        FROM TIPOLOGIE_CERTIFICAZIONE
        WHERE ID_certificazione = ?
        """;

    public static final String VERIFICA_GUIDA_ESISTENTE =
        """
        SELECT CF FROM GUIDE
        WHERE CF = ?
        AND stato_account = '1'
        """;

    public static final String INSERISCI_GUIDA =
        """
        INSERT INTO GUIDE (CF, stato_account) VALUES (?, 1)
        """;

    public static final String INSERISCI_TIPOLOGIA_CERT =
        """
        INSERT INTO TIPOLOGIE_CERTIFICAZIONE (ID_certificazione, livello) 
        VALUES (?, ?)
        """;

    public static final String INSERISCI_LUOGO_ESPLORABILE =
        """
        INSERT INTO LUOGHI_ESPLORABILI (nome, nome_zona, nome_paese, nome_categoria)
        VALUES (?, ?, ?, ?)
        """;

    public static final String LIST_PAESI = 
        """
        SELECT Nome FROM PAESI ORDER BY Nome
        """;

    public static final String LIST_ZONE_PER_PAESE = 
        """
        SELECT nome FROM ZONE WHERE nome_paese = ? ORDER BY nome
        """;
    public static final String ESCURSIONI_PER_GUIDA =
        """
        SELECT ID_escursione, titolo, difficolta, costo
        FROM ESCURSIONI
        WHERE Guida_CF = ?
        """;
    public static final String RESOCONTI_GUIDA =
        """
        SELECT ID_escursione, data_inizio, data_fine, temperatura_rilevata, precipitazioni
        FROM riepiloga
        WHERE CF = ?
        ORDER BY data_inizio DESC
        """;

    public static final String LIST_LUOGHI_PER_ZONA = 
        """
        SELECT nome 
        FROM LUOGHI_ESPLORABILI
        WHERE nome_paese = ? 
        AND nome_zona = ? 
        ORDER BY nome
        """;

    public static final String INSERISCI_PAESE =
        "INSERT INTO PAESI (nome) VALUES (?)";

    public static final String INSERISCI_ZONA =
        "INSERT INTO ZONE (nome_paese, nome, descrizione) VALUES (?, ?, ?)";

    public static final String INSERISCI_CATEGORIA =
        "INSERT INTO CATEGORIE (nome_categoria) VALUES (?)";
}


