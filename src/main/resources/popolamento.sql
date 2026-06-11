create database if not exists DestinationBuddy;
use DestinationBuddy;

-- ==========================================
-- 1. POPOLAMENTO PERSONE E GUIDE
-- ==========================================
INSERT INTO PERSONE (CF, nome, cognome, ID_account, email, password, tipo_utente, tipo_amministratore, data_assunzione, ruolo, data_iscrizione, escursioni_effettuate) 
VALUES ('RSSMRA80A01H501A', 'Mario', 'Rossi', 'ACC-001', 'admin@escursioni.it', 'pass123', 0, 1, '2020-01-15', 'Supervisore', '2020-01-15', 0);

INSERT INTO PERSONE (CF, nome, cognome, ID_account, email, password, tipo_utente, tipo_amministratore, escursioni_effettuate, data_iscrizione) 
VALUES ('BNCGIA90B41F205Z', 'Giulia', 'Bianchi', 'ACC-002', 'giulia@mail.com', 'pass123', 1, 0, 0, '2023-05-10');

INSERT INTO PERSONE (CF, nome, cognome, ID_account, email, password, tipo_utente, tipo_amministratore, escursioni_effettuate, data_iscrizione) 
VALUES ('NNANNA85M22F205Z', 'Anna', 'Neri', 'ACC-003', 'anna.guida@mail.com', 'pass123', 1, 0, 12, '2018-04-10');

-- Ricordiamoci che Anna è una guida! Dobbiamo inserirla nella tabella GUIDE
INSERT INTO GUIDE (CF, stato_account) VALUES ('NNANNA85M22F205Z', 1);


-- ==========================================
-- 2. POPOLAMENTO LUOGHI E CATEGORIE
-- ==========================================
INSERT INTO PAESI (Nome) VALUES ('Italia');
INSERT INTO ZONE (nome, nome_paese, descrizione) VALUES ('Abruzzo', 'Italia', 'Parco Nazionale del Gran Sasso');
INSERT INTO CATEGORIE (nome_categoria) VALUES ('Montagna');

INSERT INTO LUOGHI_ESPLORABILI (nome_paese, nome_zona, nome, quota, difficolta_accesso, periodo_consigliato, nome_categoria) 
VALUES ('Italia', 'Abruzzo', 'Campo Imperatore', 2128, 'Media', 'Estate', 'Montagna');


-- ==========================================
-- 3. POPOLAMENTO ESCURSIONI E CERTIFICAZIONI
-- ==========================================
-- Dobbiamo creare la tipologia di certificazione prima di assegnare il brevetto
INSERT INTO TIPOLOGIE_CERTIFICAZIONE (ID_certificazione, livello) VALUES ('CERT-CAI', 'Avanzato');

-- Inseriamo il brevetto ad Anna
INSERT INTO CERTIFICAZIONI (ID_certificazione, n_certificazione, ente_rilasciante, data_rilascio, stato_validazione, CF, Guida_CF)
VALUES ('CERT-CAI', 'CAI-2020-ABZ', 'Club Alpino Italiano', '2020-05-15', 'Validata', 'NNANNA85M22F205Z', 'NNANNA85M22F205Z');

-- Inseriamo l'Escursione guidata da Anna (aggiunto l'ID_escursione come VARCHAR)
INSERT INTO ESCURSIONI (ID_escursione, titolo, descrizione, difficolta, numero_partecipanti, costo, data_apertura_iscrizione, data_chiusura_iscrizione, Guida_CF)
VALUES ('ESC-001', 'Trekking sul Gran Sasso', 'Splendida escursione in quota.', 'Intermedia', 20, 35.50, '2024-07-05', '2024-07-20', 'NNANNA85M22F205Z');


-- ==========================================
-- 4. OPERAZIONI FINALI: PRENOTAZIONI E RECENSIONI
-- ==========================================
-- Giulia prenota l'escursione ESC-001
INSERT INTO prenota (CF, ID_escursione, data_prenotazione, stato)
VALUES ('BNCGIA90B41F205Z', 'ESC-001', '2024-06-15', 'Saldato');

-- Giulia lascia una recensione (aggiunto campo immagini obbligatorio)
INSERT INTO RECENSIONI (titolo, CF, voto, immagini, descrizione, stato_recensione, ID_escursione)
VALUES ('Giornata Fantastica', 'BNCGIA90B41F205Z', 5, 'foto_gransasso.jpg', 'Panorama mozzafiato.', 'Approvata', 'ESC-001');

-- Anna compila il riepilogo a fine giornata
INSERT INTO riepiloga (ID_escursione, data_inizio, data_fine, temperatura_rilevata, precipitazioni, CF)
VALUES ('ESC-001', '2024-07-05', '2024-07-05', 18.5, 0.0, 'NNANNA85M22F205Z');