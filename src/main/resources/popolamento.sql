CREATE DATABASE IF NOT EXISTS DestinationBuddy;
USE DestinationBuddy;

-- PERSONE E GUIDE
-- Admin
INSERT INTO PERSONE (CF, nome, cognome, tipo_utente, tipo_amministratore, escursioni_effettuate, data_iscrizione, data_assunzione, email, password) 
VALUES ('RSSMRA80A01H501A', 'Mario', 'Rossi', 1, 1, 0,'2020-01-15', '2020-01-15', 'admin@escursioni.it', 'pass123');

-- Utenti
INSERT INTO PERSONE (CF, nome, cognome, tipo_utente, tipo_amministratore, escursioni_effettuate, data_iscrizione, data_assunzione, email, password) 
VALUES ('BNCGIA90B41F205Z', 'Giulia', 'Bianchi', 1, 0, 0, '2023-05-10', NULL, 'giulia@gmail.com', 'pass123');

INSERT INTO PERSONE (CF, nome, cognome, tipo_utente, tipo_amministratore, escursioni_effettuate, data_iscrizione, data_assunzione, email, password) 
VALUES ('MZZCHR05P64H199H', 'Chiara', 'Mazzoni', 1, 0, 0, '2025-09-10', NULL, 'chiara@gmail.com', 'pass123');

INSERT INTO PERSONE (CF, nome, cognome, tipo_utente, tipo_amministratore, escursioni_effettuate, data_iscrizione, data_assunzione, email, password) 
VALUES ('MNDARN05P64H199H', 'Arianna', 'Mondardini', 1, 0, 0, '2026-04-10', NULL, 'arianna@gmail.com', 'pass123');

INSERT INTO PERSONE (CF, nome, cognome, tipo_utente, tipo_amministratore, escursioni_effettuate, data_iscrizione, data_assunzione, email, password) 
VALUES ('OLVRBC05P64H199H', 'Rebecca', 'Olivieri', 1, 0, 0, '2024-04-12', NULL, 'rebecca@gmail.com', 'pass123');

INSERT INTO PERSONE (CF, nome, cognome, tipo_utente, tipo_amministratore, escursioni_effettuate, data_iscrizione, data_assunzione, email, password)
VALUES ('RSSMRA80A01H501T', 'Mario', 'Verdi', 1, 0, 0, CURRENT_DATE, NULL, 'mario.test@email.it', 'pass123');

-- Guide
INSERT INTO PERSONE (CF, nome, cognome, tipo_utente, tipo_amministratore, escursioni_effettuate, data_iscrizione, data_assunzione, email, password) 
VALUES ('NNANNA85M22F205Z', 'Anna', 'Neri', 1, 0, 0, '2018-04-10', NULL, 'anna.guida@gmail.com', 'pass123');

INSERT INTO PERSONE (CF, nome, cognome, tipo_utente, tipo_amministratore, escursioni_effettuate, data_iscrizione, data_assunzione, email, password) 
VALUES ('BNCLCU80A01H501Y', 'Luca', 'Bianchi', 1, 0, 0, CURRENT_DATE, NULL, 'luca@email.it', 'pass123');

INSERT INTO PERSONE (CF, nome, cognome, tipo_utente, tipo_amministratore, escursioni_effettuate, data_iscrizione, data_assunzione, email, password) 
VALUES ('VRDGLI90M41H501Z', 'Giulia', 'Verdi', 1, 0, 0, CURRENT_DATE, NULL, 'giuliaguida@email.it', 'pass123');

INSERT INTO PERSONE (CF, nome, cognome, tipo_utente, tipo_amministratore, escursioni_effettuate, data_iscrizione, data_assunzione, email, password) 
VALUES ('DPNJEA85M01Z110X', 'Jean', 'Dupont', 1, 0, 0, CURRENT_DATE, NULL, 'jean@email.fr', 'pass123');

-- Utenti per test sospensione
INSERT INTO PERSONE (CF, nome, cognome, tipo_utente, tipo_amministratore, escursioni_effettuate, data_iscrizione, data_assunzione, email, password) VALUES 
('HATER00000000001', 'Pino', 'Arrabbiato', 1, 0, 0, CURRENT_DATE, NULL, 'hater1@mail.com', 'pass'),
('HATER00000000002', 'Gino', 'Deluso', 1, 0, 0, CURRENT_DATE, NULL, 'hater2@mail.com', 'pass'),
('HATER00000000003', 'Rino', 'Triste', 1, 0, 0, CURRENT_DATE, NULL, 'hater3@mail.com', 'pass'),
('HATER00000000004', 'Nino', 'Furioso', 1, 0, 0, CURRENT_DATE, NULL, 'hater4@mail.com', 'pass'),
('HATER00000000005', 'Tino', 'Scontento', 1, 0, 0, CURRENT_DATE, NULL, 'hater5@mail.com', 'pass'),
('HATER00000000006', 'Dino', 'Amareggiato', 1, 0, 0, CURRENT_DATE, NULL, 'hater6@mail.com', 'pass');

INSERT INTO GUIDE (CF, stato_account) VALUES ('NNANNA85M22F205Z', 1);
INSERT INTO GUIDE (CF, stato_account) VALUES ('BNCLCU80A01H501Y', 1);
INSERT INTO GUIDE (CF, stato_account) VALUES ('VRDGLI90M41H501Z', 1);
INSERT INTO GUIDE (CF, stato_account) VALUES ('DPNJEA85M01Z110X', 1);

-- LUOGHI E CATEGORIE
INSERT INTO PAESI (Nome) VALUES ('Italia');
INSERT INTO PAESI (Nome) VALUES ('Francia');
INSERT INTO PAESI (Nome) VALUES ('Spagna');

INSERT INTO ZONE (nome, nome_paese, descrizione) VALUES ('Abruzzo', 'Italia', 'Parco Nazionale del Gran Sasso');
INSERT INTO ZONE (nome, nome_paese, descrizione) VALUES ('Trentino-Alto Adige', 'Italia', 'Dolomiti e valli alpine patrimonio UNESCO');
INSERT INTO ZONE (nome, nome_paese, descrizione) VALUES ('Toscana', 'Italia', 'Colline della Val d''Orcia e borghi storici');
INSERT INTO ZONE (nome, nome_paese, descrizione) VALUES ('Sardegna', 'Italia', 'Costa frastagliata, spiagge incontaminate e percorsi di trekking costieri');
INSERT INTO ZONE (nome, nome_paese, descrizione) VALUES ('Lombardia', 'Italia', 'Grandi laghi prealpini e montagne');
INSERT INTO ZONE (nome, nome_paese, descrizione) VALUES ('Alta Savoia', 'Francia', 'Regione alpina ai piedi del Monte Bianco');
INSERT INTO ZONE (nome, nome_paese, descrizione) VALUES ('Andalusia', 'Spagna', 'Paesaggi desertici, catene montuose e coste del sud');

INSERT INTO CATEGORIE (nome_categoria) VALUES ('Montagna');
INSERT INTO CATEGORIE (nome_categoria) VALUES ('Mare');
INSERT INTO CATEGORIE (nome_categoria) VALUES ('Collina');
INSERT INTO CATEGORIE (nome_categoria) VALUES ('Lago');

INSERT INTO LUOGHI_ESPLORABILI (nome_paese, nome_zona, nome, quota, difficolta_accesso, periodo_consigliato, nome_categoria) 
VALUES ('Italia', 'Abruzzo', 'Campo Imperatore', 2128, 'Media', 'Estate', 'Montagna');
INSERT INTO LUOGHI_ESPLORABILI (nome_paese, nome_zona, nome, quota, difficolta_accesso, periodo_consigliato, nome_categoria) 
VALUES ('Italia', 'Trentino-Alto Adige', 'Tre Cime di Lavaredo', 2348, 'Media', 'Estate', 'Montagna');
INSERT INTO LUOGHI_ESPLORABILI (nome_paese, nome_zona, nome, quota, difficolta_accesso, periodo_consigliato, nome_categoria) 
VALUES ('Italia', 'Toscana', 'Val d''Orcia Trail', 491, 'Facile', 'Primavera', 'Collina');
INSERT INTO LUOGHI_ESPLORABILI (nome_paese, nome_zona, nome, quota, difficolta_accesso, periodo_consigliato, nome_categoria) 
VALUES ('Italia', 'Sardegna', 'Cala Goloritzé', 5, 'Difficile', 'Estate', 'Mare');
INSERT INTO LUOGHI_ESPLORABILI (nome_paese, nome_zona, nome, quota, difficolta_accesso, periodo_consigliato, nome_categoria) 
VALUES ('Italia', 'Lombardia', 'Sentiero del Viandante (Como)', 250, 'Facile', 'Autunno', 'Lago');
INSERT INTO LUOGHI_ESPLORABILI (nome_paese, nome_zona, nome, quota, difficolta_accesso, periodo_consigliato, nome_categoria) 
VALUES ('Francia', 'Alta Savoia', 'Aiguille du Midi', 3842, 'Difficile', 'Inverno', 'Montagna');
INSERT INTO LUOGHI_ESPLORABILI (nome_paese, nome_zona, nome, quota, difficolta_accesso, periodo_consigliato, nome_categoria) 
VALUES ('Spagna', 'Andalusia', 'Cabo de Gata', 15, 'Media', 'Primavera', 'Mare');

-- TIPOLOGIE CERTIFICAZIONE

INSERT INTO TIPOLOGIE_CERTIFICAZIONE (ID_certificazione, livello) VALUES ('CERT-CAI', 'Avanzato');
INSERT INTO TIPOLOGIE_CERTIFICAZIONE (ID_certificazione, livello) VALUES ('PADI-OWD', 'Base');
INSERT INTO TIPOLOGIE_CERTIFICAZIONE (ID_certificazione, livello) VALUES ('AMM-FIM', 'Intermedio');
INSERT INTO TIPOLOGIE_CERTIFICAZIONE (ID_certificazione, livello) VALUES ('FFME-ALPI', 'Avanzato');
INSERT INTO TIPOLOGIE_CERTIFICAZIONE (ID_certificazione, livello) VALUES ('Brevetto Trekking', 'Base');
INSERT INTO TIPOLOGIE_CERTIFICAZIONE (ID_certificazione, livello) VALUES ('Brevetto Escursionismo Notturno', 'Intermedio');
INSERT INTO TIPOLOGIE_CERTIFICAZIONE (ID_certificazione, livello) VALUES ('Brevetto Hiking', 'Base');
INSERT INTO TIPOLOGIE_CERTIFICAZIONE (ID_certificazione, livello) VALUES ('Brevetto Speleologia', 'Avanzato');
INSERT INTO TIPOLOGIE_CERTIFICAZIONE (ID_certificazione, livello) VALUES ('Brevetto Mountain Bike', 'Intermedio');
INSERT INTO TIPOLOGIE_CERTIFICAZIONE (ID_certificazione, livello) VALUES ('Brevetto Ferrata', 'Avanzato');
INSERT INTO TIPOLOGIE_CERTIFICAZIONE (ID_certificazione, livello) VALUES ('UIAGM-ALPI', 'Guida');
INSERT INTO TIPOLOGIE_CERTIFICAZIONE (ID_certificazione, livello) VALUES ('AIGAE-AMB', 'Guida');
INSERT INTO TIPOLOGIE_CERTIFICAZIONE (ID_certificazione, livello) VALUES ('PADI-INSTR', 'Guida');

-- CERTIFICAZIONI UTENTI E GUIDE
INSERT INTO CERTIFICAZIONI (ID_certificazione, n_certificazione, ente_rilasciante, data_rilascio, data_scadenza, stato_validazione, CF, Guida_CF)
VALUES ('CERT-CAI', 'CAI-2020-ABZ', 'Club Alpino Italiano', '2020-05-15', '2030-05-15', 'Validata', 'NNANNA85M22F205Z', 'NNANNA85M22F205Z');
INSERT INTO CERTIFICAZIONI (ID_certificazione, n_certificazione, ente_rilasciante, data_rilascio, data_scadenza, stato_validazione, CF, Guida_CF)
VALUES ('PADI-OWD', 'PADI-2021-SAR', 'PADI International', '2021-06-10', '2031-06-10', 'Validata', 'BNCLCU80A01H501Y', 'BNCLCU80A01H501Y');
INSERT INTO CERTIFICAZIONI (ID_certificazione, n_certificazione, ente_rilasciante, data_rilascio, data_scadenza, stato_validazione, CF, Guida_CF)
VALUES ('AMM-FIM', 'FIM-2023-TOS', 'Federazione Italiana Montagna', '2023-04-20', '2028-04-20', 'In attesa di validazione', 'VRDGLI90M41H501Z', 'VRDGLI90M41H501Z');
INSERT INTO CERTIFICAZIONI (ID_certificazione, n_certificazione, ente_rilasciante, data_rilascio, data_scadenza, stato_validazione, CF, Guida_CF)
VALUES ('FFME-ALPI', 'FFME-2019-SAV', 'Fédération Française', '2019-09-05', '2029-09-05', 'Validata', 'DPNJEA85M01Z110X', 'DPNJEA85M01Z110X');

-- Certificazioni di livello Guida
INSERT INTO CERTIFICAZIONI (ID_certificazione, n_certificazione, ente_rilasciante, data_rilascio, data_scadenza, stato_validazione, CF, Guida_CF)
VALUES ('UIAGM-ALPI', 'UIAGM-2018-ITA', 'UIAGM International', '2018-04-01', '2028-04-01', 'Validata', 'NNANNA85M22F205Z', 'NNANNA85M22F205Z');
INSERT INTO CERTIFICAZIONI (ID_certificazione, n_certificazione, ente_rilasciante, data_rilascio, data_scadenza, stato_validazione, CF, Guida_CF)
VALUES ('PADI-INSTR', 'PADI-INS-2015', 'PADI Europe', '2015-06-10', '2030-06-10', 'Validata', 'BNCLCU80A01H501Y', 'BNCLCU80A01H501Y');
INSERT INTO CERTIFICAZIONI (ID_certificazione, n_certificazione, ente_rilasciante, data_rilascio, data_scadenza, stato_validazione, CF, Guida_CF)
VALUES ('AIGAE-AMB', 'AIGAE-2021-TOS', 'AIGAE Italia', '2021-03-20', '2026-03-20', 'Validata', 'VRDGLI90M41H501Z', 'VRDGLI90M41H501Z');
INSERT INTO CERTIFICAZIONI (ID_certificazione, n_certificazione, ente_rilasciante, data_rilascio, data_scadenza, stato_validazione, CF, Guida_CF)
VALUES ('UIAGM-ALPI', 'UIAGM-2016-FRA', 'SNGM France', '2016-09-05', '2029-09-05', 'Validata', 'DPNJEA85M01Z110X', 'DPNJEA85M01Z110X');


-- TIPOLOGIE ESCURSIONE
INSERT INTO TIPOLOGIE_ESCURSIONE (ID_tipologia) VALUES ('Trekking');
INSERT INTO TIPOLOGIE_ESCURSIONE (ID_tipologia) VALUES ('Snorkeling');
INSERT INTO TIPOLOGIE_ESCURSIONE (ID_tipologia) VALUES ('Hiking');
INSERT INTO TIPOLOGIE_ESCURSIONE (ID_tipologia) VALUES ('Alpinismo');
INSERT INTO TIPOLOGIE_ESCURSIONE (ID_tipologia) VALUES ('Trekking notturno');
INSERT INTO TIPOLOGIE_ESCURSIONE (ID_tipologia) VALUES ('Speleologia');
INSERT INTO TIPOLOGIE_ESCURSIONE (ID_tipologia) VALUES ('Mountain bike');
INSERT INTO TIPOLOGIE_ESCURSIONE (ID_tipologia) VALUES ('Ferrata');

-- CERTIFICAZIONI RICHIESTE PER TIPOLOGIA
INSERT INTO richiede (ID_tipologia, ID_certificazione) VALUES ('Alpinismo', 'FFME-ALPI');
INSERT INTO richiede (ID_tipologia, ID_certificazione) VALUES ('Alpinismo', 'CERT-CAI');
INSERT INTO richiede (ID_tipologia, ID_certificazione) VALUES ('Snorkeling', 'PADI-OWD');
INSERT INTO richiede (ID_tipologia, ID_certificazione) VALUES ('Trekking', 'Brevetto Trekking');
INSERT INTO richiede (ID_tipologia, ID_certificazione) VALUES ('Trekking notturno', 'Brevetto Escursionismo Notturno');
INSERT INTO richiede (ID_tipologia, ID_certificazione) VALUES ('Hiking', 'Brevetto Hiking');
INSERT INTO richiede (ID_tipologia, ID_certificazione) VALUES ('Speleologia', 'Brevetto Speleologia');
INSERT INTO richiede (ID_tipologia, ID_certificazione) VALUES ('Mountain bike', 'Brevetto Mountain Bike');
INSERT INTO richiede (ID_tipologia, ID_certificazione) VALUES ('Ferrata', 'Brevetto Ferrata');
INSERT INTO richiede (ID_tipologia, ID_certificazione) VALUES ('Ferrata', 'CERT-CAI');

-- EQUIPAGGIAMENTO
INSERT INTO EQUIPAGGIAMENTI (ID_categoria, costo_totale_giornaliero) VALUES ('Zaino tecnico', 5.00);
INSERT INTO EQUIPAGGIAMENTI (ID_categoria, costo_totale_giornaliero) VALUES ('Scarponi', 8.00);
INSERT INTO EQUIPAGGIAMENTI (ID_categoria, costo_totale_giornaliero) VALUES ('Imbracatura', 6.00);
INSERT INTO EQUIPAGGIAMENTI (ID_categoria, costo_totale_giornaliero) VALUES ('Muta da snorkeling', 10.00);
INSERT INTO EQUIPAGGIAMENTI (ID_categoria, costo_totale_giornaliero) VALUES ('Piccozza', 7.00);
INSERT INTO EQUIPAGGIAMENTI (ID_categoria, costo_totale_giornaliero) VALUES ('Bastoncini trekking', 3.00);
INSERT INTO EQUIPAGGIAMENTI (ID_categoria, costo_totale_giornaliero) VALUES ('Casco speleo + frontale', 12.00);
INSERT INTO EQUIPAGGIAMENTI (ID_categoria, costo_totale_giornaliero) VALUES ('Mountain bike', 25.00);
INSERT INTO EQUIPAGGIAMENTI (ID_categoria, costo_totale_giornaliero) VALUES ('Casco ferrata', 4.00);
INSERT INTO EQUIPAGGIAMENTI (ID_categoria, costo_totale_giornaliero) VALUES ('Set ferrata (kit + imbrago)', 9.00);

INSERT INTO necessita (ID_categoria, ID_tipologia) VALUES ('Zaino tecnico', 'Trekking');
INSERT INTO necessita (ID_categoria, ID_tipologia) VALUES ('Scarponi', 'Trekking');
INSERT INTO necessita (ID_categoria, ID_tipologia) VALUES ('Bastoncini trekking', 'Trekking');
INSERT INTO necessita (ID_categoria, ID_tipologia) VALUES ('Muta da snorkeling', 'Snorkeling');
INSERT INTO necessita (ID_categoria, ID_tipologia) VALUES ('Zaino tecnico', 'Hiking');
INSERT INTO necessita (ID_categoria, ID_tipologia) VALUES ('Bastoncini trekking', 'Hiking');
INSERT INTO necessita (ID_categoria, ID_tipologia) VALUES ('Imbracatura', 'Alpinismo');
INSERT INTO necessita (ID_categoria, ID_tipologia) VALUES ('Piccozza', 'Alpinismo');
INSERT INTO necessita (ID_categoria, ID_tipologia) VALUES ('Scarponi', 'Alpinismo');
INSERT INTO necessita (ID_categoria, ID_tipologia) VALUES ('Zaino tecnico', 'Trekking notturno');
INSERT INTO necessita (ID_categoria, ID_tipologia) VALUES ('Scarponi', 'Trekking notturno');
INSERT INTO necessita (ID_categoria, ID_tipologia) VALUES ('Casco speleo + frontale', 'Speleologia');
INSERT INTO necessita (ID_categoria, ID_tipologia) VALUES ('Mountain bike', 'Mountain bike');
INSERT INTO necessita (ID_categoria, ID_tipologia) VALUES ('Casco ferrata', 'Ferrata');
INSERT INTO necessita (ID_categoria, ID_tipologia) VALUES ('Set ferrata (kit + imbrago)', 'Ferrata');

INSERT INTO PEZZI (ID_pezzo, nome, costo_giornaliero, disponibilita, ID_categoria) VALUES ('PZ-001', 'Zaino 40L Osprey', 5.00, 1, 'Zaino tecnico');
INSERT INTO PEZZI (ID_pezzo, nome, costo_giornaliero, disponibilita, ID_categoria) VALUES ('PZ-002', 'Zaino 30L Deuter', 5.00, 1, 'Zaino tecnico');
INSERT INTO PEZZI (ID_pezzo, nome, costo_giornaliero, disponibilita, ID_categoria) VALUES ('PZ-003', 'Scarponi Salomon n.42', 8.00, 1, 'Scarponi');
INSERT INTO PEZZI (ID_pezzo, nome, costo_giornaliero, disponibilita, ID_categoria) VALUES ('PZ-004', 'Scarponi Salomon n.39', 8.00, 1, 'Scarponi');
INSERT INTO PEZZI (ID_pezzo, nome, costo_giornaliero, disponibilita, ID_categoria) VALUES ('PZ-005', 'Imbracatura Petzl', 6.00, 1, 'Imbracatura');
INSERT INTO PEZZI (ID_pezzo, nome, costo_giornaliero, disponibilita, ID_categoria) VALUES ('PZ-006', 'Muta 5mm taglia M', 10.00, 1, 'Muta da snorkeling');
INSERT INTO PEZZI (ID_pezzo, nome, costo_giornaliero, disponibilita, ID_categoria) VALUES ('PZ-007', 'Muta 5mm taglia L', 10.00, 1, 'Muta da snorkeling');
INSERT INTO PEZZI (ID_pezzo, nome, costo_giornaliero, disponibilita, ID_categoria) VALUES ('PZ-008', 'Piccozza Grivel', 7.00, 1, 'Piccozza');
INSERT INTO PEZZI (ID_pezzo, nome, costo_giornaliero, disponibilita, ID_categoria) VALUES ('PZ-009', 'Bastoncini Leki', 3.00, 1, 'Bastoncini trekking');
INSERT INTO PEZZI (ID_pezzo, nome, costo_giornaliero, disponibilita, ID_categoria) VALUES ('PZ-010', 'Casco + frontale Petzl', 12.00, 1, 'Casco speleo + frontale');
INSERT INTO PEZZI (ID_pezzo, nome, costo_giornaliero, disponibilita, ID_categoria) VALUES ('PZ-011', 'MTB full-suspension', 25.00, 1, 'Mountain bike');
INSERT INTO PEZZI (ID_pezzo, nome, costo_giornaliero, disponibilita, ID_categoria) VALUES ('PZ-012', 'Casco ferrata Kong', 4.00, 1, 'Casco ferrata');
INSERT INTO PEZZI (ID_pezzo, nome, costo_giornaliero, disponibilita, ID_categoria) VALUES ('PZ-013', 'Kit ferrata completo', 9.00, 1, 'Set ferrata (kit + imbrago)');

-- ESCURSIONI
INSERT INTO ESCURSIONI (ID_escursione, titolo, descrizione, difficolta, numero_partecipanti, costo, data_apertura_iscrizione, data_chiusura_iscrizione, Guida_CF)
VALUES ('ESC-001', 'Trekking sul Gran Sasso', 'Splendida escursione in quota.', 'Intermedia', 20, 35.50, '2024-06-05', '2024-06-20', 'NNANNA85M22F205Z');
INSERT INTO ESCURSIONI (ID_escursione, titolo, descrizione, difficolta, numero_partecipanti, costo, data_apertura_iscrizione, data_chiusura_iscrizione, Guida_CF)
VALUES ('ESC-002', 'Snorkeling a Cala Goloritzé', 'Esplorazione dei fondali sardi.', 'Facile', 15, 45.00, '2026-06-01', '2026-06-30', 'BNCLCU80A01H501Y');
INSERT INTO ESCURSIONI (ID_escursione, titolo, descrizione, difficolta, numero_partecipanti, costo, data_apertura_iscrizione, data_chiusura_iscrizione, Guida_CF)
VALUES ('ESC-003', 'Passeggiata in Val d''Orcia', 'Trekking leggero tra i cipressi toscani.', 'Facile', 25, 25.00, '2026-05-01', '2026-07-01', 'VRDGLI90M41H501Z');
INSERT INTO ESCURSIONI (ID_escursione, titolo, descrizione, difficolta, numero_partecipanti, costo, data_apertura_iscrizione, data_chiusura_iscrizione, Guida_CF)
VALUES ('ESC-004', 'Aiguille du Midi', 'Alpinismo sul Monte Bianco.', 'Difficile', 8, 150.00, '2026-11-01', '2026-12-15', 'DPNJEA85M01Z110X');
INSERT INTO ESCURSIONI (ID_escursione, titolo, descrizione, difficolta, numero_partecipanti, costo, data_apertura_iscrizione, data_chiusura_iscrizione, Guida_CF)
VALUES ('ESC-005', 'Notturna alle Tre Cime', 'Trekking sotto le stelle sulle Dolomiti.', 'Intermedia', 12, 55.00, '2026-08-01', '2026-08-20', 'NNANNA85M22F205Z');
INSERT INTO ESCURSIONI (ID_escursione, titolo, descrizione, difficolta, numero_partecipanti, costo, data_apertura_iscrizione, data_chiusura_iscrizione, Guida_CF)
VALUES ('ESC-006', 'Speleologia nelle Grotte di Frasassi', 'Esplorazione guidata delle grotte carsiche marchigiane.', 'Difficile', 10, 60.00, '2026-06-01', '2026-08-20', 'NNANNA85M22F205Z');
INSERT INTO ESCURSIONI (ID_escursione, titolo, descrizione, difficolta, numero_partecipanti, costo, data_apertura_iscrizione, data_chiusura_iscrizione, Guida_CF)
VALUES ('ESC-007', 'Anello del Monte Bianco in MTB', 'Percorso panoramico in mountain bike tra Italia e Francia.', 'Intermedia', 14, 70.00, '2026-06-10', '2026-07-10', 'BNCLCU80A01H501Y');
INSERT INTO ESCURSIONI (ID_escursione, titolo, descrizione, difficolta, numero_partecipanti, costo, data_apertura_iscrizione, data_chiusura_iscrizione, Guida_CF)
VALUES ('ESC-008', 'Ferrata delle Mésules', 'Via ferrata panoramica sulle Dolomiti, adatta a esperti.', 'Difficile', 8, 65.00, '2026-06-01', '2026-07-01', 'VRDGLI90M41H501Z');
INSERT INTO ESCURSIONI (ID_escursione, titolo, descrizione, difficolta, numero_partecipanti, costo, data_apertura_iscrizione, data_chiusura_iscrizione, Guida_CF)
VALUES ('ESC-009', 'Tramonto a Cabo de Gata', 'Passeggiata costiera in Andalusia.', 'Facile', 15, 20.00, '2026-01-01', '2026-04-01', 'VRDGLI90M41H501Z');
INSERT INTO ESCURSIONI (ID_escursione, titolo, descrizione, difficolta, numero_partecipanti, costo, data_apertura_iscrizione, data_chiusura_iscrizione, Guida_CF)
VALUES ('ESC-999', 'Tour Disastroso', 'Tutto è andato storto dall''inizio alla fine.', 'Facile', 10, 10.00, '2023-01-01', '2023-02-01', 'DPNJEA85M01Z110X');

-- ASSUME (escursione -> tipologia)

INSERT INTO assume (ID_escursione, ID_tipologia) VALUES ('ESC-001', 'Trekking');
INSERT INTO assume (ID_escursione, ID_tipologia) VALUES ('ESC-002', 'Snorkeling');
INSERT INTO assume (ID_escursione, ID_tipologia) VALUES ('ESC-003', 'Hiking');
INSERT INTO assume (ID_escursione, ID_tipologia) VALUES ('ESC-004', 'Alpinismo');
INSERT INTO assume (ID_escursione, ID_tipologia) VALUES ('ESC-005', 'Trekking notturno');
INSERT INTO assume (ID_escursione, ID_tipologia) VALUES ('ESC-006', 'Speleologia');
INSERT INTO assume (ID_escursione, ID_tipologia) VALUES ('ESC-007', 'Mountain bike');
INSERT INTO assume (ID_escursione, ID_tipologia) VALUES ('ESC-008', 'Ferrata');
INSERT INTO assume (ID_escursione, ID_tipologia) VALUES ('ESC-009', 'Hiking');
INSERT INTO assume (ID_escursione, ID_tipologia) VALUES ('ESC-999', 'Trekking');

-- 10. GIORNATE E TAPPE

INSERT INTO GIORNATE (ID_escursione, data, programma) VALUES ('ESC-001', '2024-07-15', 'Ritrovo a Campo Imperatore');
INSERT INTO GIORNATE (ID_escursione, data, programma) VALUES ('ESC-001', '2024-07-16', 'Salita al Corno Grande');
INSERT INTO GIORNATE (ID_escursione, data, programma) VALUES ('ESC-001', '2024-07-17', 'Discesa e rientro');
INSERT INTO GIORNATE (ID_escursione, data, programma) VALUES ('ESC-002', '2026-07-01', 'Immersione e relax in spiaggia');
INSERT INTO GIORNATE (ID_escursione, data, programma) VALUES ('ESC-002', '2026-07-02', 'Esplorazione grotte costiere');
INSERT INTO GIORNATE (ID_escursione, data, programma) VALUES ('ESC-003', '2026-08-10', 'Trekking e degustazione in collina');
INSERT INTO GIORNATE (ID_escursione, data, programma) VALUES ('ESC-003', '2026-08-11', 'Visita borgo di Pienza e degustazione');
INSERT INTO GIORNATE (ID_escursione, data, programma) VALUES ('ESC-004', '2026-12-20', 'Ascesa tecnica su neve e ghiaccio');
INSERT INTO GIORNATE (ID_escursione, data, programma) VALUES ('ESC-004', '2026-12-21', 'Vetta e bivacco in quota');
INSERT INTO GIORNATE (ID_escursione, data, programma) VALUES ('ESC-004', '2026-12-22', 'Discesa tecnica e rientro');
INSERT INTO GIORNATE (ID_escursione, data, programma) VALUES ('ESC-005', '2026-08-25', 'Passeggiata notturna e osservazione stelle');
INSERT INTO GIORNATE (ID_escursione, data, programma) VALUES ('ESC-005', '2026-08-26', 'Alba sulle Tre Cime e rientro');
INSERT INTO GIORNATE (ID_escursione, data, programma) VALUES ('ESC-006', '2026-09-05', 'Ingresso grotte e esplorazione base');
INSERT INTO GIORNATE (ID_escursione, data, programma) VALUES ('ESC-006', '2026-09-06', 'Tratti tecnici e risalita');
INSERT INTO GIORNATE (ID_escursione, data, programma) VALUES ('ESC-007', '2026-07-15', 'Tour del versante italiano');
INSERT INTO GIORNATE (ID_escursione, data, programma) VALUES ('ESC-007', '2026-07-16', 'Tour del versante francese');
INSERT INTO GIORNATE (ID_escursione, data, programma) VALUES ('ESC-008', '2026-07-25', 'Salita e percorso attrezzato');
INSERT INTO GIORNATE (ID_escursione, data, programma) VALUES ('ESC-009', '2026-06-21', 'Camminata e osservazione del tramonto');
INSERT INTO GIORNATE (ID_escursione, data, programma) VALUES ('ESC-999', '2023-03-01', 'Giornata di pioggia e fango');

INSERT INTO TAPPE (ID_tappa, durata, ID_escursione, data, nome_paese, nome_zona, nome_luogo) VALUES ('T01', 4.5, 'ESC-001', '2024-07-15', 'Italia', 'Abruzzo', 'Campo Imperatore');
INSERT INTO TAPPE (ID_tappa, durata, ID_escursione, data, nome_paese, nome_zona, nome_luogo) VALUES ('T01b', 6.0, 'ESC-001', '2024-07-16', 'Italia', 'Abruzzo', 'Campo Imperatore');
INSERT INTO TAPPE (ID_tappa, durata, ID_escursione, data, nome_paese, nome_zona, nome_luogo) VALUES ('T01c', 3.0, 'ESC-001', '2024-07-17', 'Italia', 'Abruzzo', 'Campo Imperatore');
INSERT INTO TAPPE (ID_tappa, durata, ID_escursione, data, nome_paese, nome_zona, nome_luogo) VALUES ('T02', 3.0, 'ESC-002', '2026-07-01', 'Italia', 'Sardegna', 'Cala Goloritzé');
INSERT INTO TAPPE (ID_tappa, durata, ID_escursione, data, nome_paese, nome_zona, nome_luogo) VALUES ('T02b', 4.0, 'ESC-002', '2026-07-02', 'Italia', 'Sardegna', 'Cala Goloritzé');
INSERT INTO TAPPE (ID_tappa, durata, ID_escursione, data, nome_paese, nome_zona, nome_luogo) VALUES ('T03', 5.0, 'ESC-003', '2026-08-10', 'Italia', 'Toscana', 'Val d''Orcia Trail');
INSERT INTO TAPPE (ID_tappa, durata, ID_escursione, data, nome_paese, nome_zona, nome_luogo) VALUES ('T03b', 3.5, 'ESC-003', '2026-08-11', 'Italia', 'Toscana', 'Val d''Orcia Trail');
INSERT INTO TAPPE (ID_tappa, durata, ID_escursione, data, nome_paese, nome_zona, nome_luogo) VALUES ('T04', 8.0, 'ESC-004', '2026-12-20', 'Francia', 'Alta Savoia', 'Aiguille du Midi');
INSERT INTO TAPPE (ID_tappa, durata, ID_escursione, data, nome_paese, nome_zona, nome_luogo) VALUES ('T04b', 10.0, 'ESC-004', '2026-12-21', 'Francia', 'Alta Savoia', 'Aiguille du Midi');
INSERT INTO TAPPE (ID_tappa, durata, ID_escursione, data, nome_paese, nome_zona, nome_luogo) VALUES ('T04c', 5.0, 'ESC-004', '2026-12-22', 'Francia', 'Alta Savoia', 'Aiguille du Midi');
INSERT INTO TAPPE (ID_tappa, durata, ID_escursione, data, nome_paese, nome_zona, nome_luogo) VALUES ('T05', 6.0, 'ESC-005', '2026-08-25', 'Italia', 'Trentino-Alto Adige', 'Tre Cime di Lavaredo');
INSERT INTO TAPPE (ID_tappa, durata, ID_escursione, data, nome_paese, nome_zona, nome_luogo) VALUES ('T05b', 4.0, 'ESC-005', '2026-08-26', 'Italia', 'Trentino-Alto Adige', 'Tre Cime di Lavaredo');
INSERT INTO TAPPE (ID_tappa, durata, ID_escursione, data, nome_paese, nome_zona, nome_luogo) VALUES ('T06a', 5.0, 'ESC-006', '2026-09-05', 'Italia', 'Toscana', 'Val d''Orcia Trail');
INSERT INTO TAPPE (ID_tappa, durata, ID_escursione, data, nome_paese, nome_zona, nome_luogo) VALUES ('T06b', 4.0, 'ESC-006', '2026-09-06', 'Italia', 'Toscana', 'Val d''Orcia Trail');
INSERT INTO TAPPE (ID_tappa, durata, ID_escursione, data, nome_paese, nome_zona, nome_luogo) VALUES ('T07a', 6.0, 'ESC-007', '2026-07-15', 'Francia', 'Alta Savoia', 'Aiguille du Midi');
INSERT INTO TAPPE (ID_tappa, durata, ID_escursione, data, nome_paese, nome_zona, nome_luogo) VALUES ('T07b', 6.0, 'ESC-007', '2026-07-16', 'Francia', 'Alta Savoia', 'Aiguille du Midi');
INSERT INTO TAPPE (ID_tappa, durata, ID_escursione, data, nome_paese, nome_zona, nome_luogo) VALUES ('T08a', 7.0, 'ESC-008', '2026-07-25', 'Italia', 'Trentino-Alto Adige', 'Tre Cime di Lavaredo');
INSERT INTO TAPPE (ID_tappa, durata, ID_escursione, data, nome_paese, nome_zona, nome_luogo) VALUES ('T09a', 3.0, 'ESC-009', '2026-06-21', 'Spagna', 'Andalusia', 'Cabo de Gata');
INSERT INTO TAPPE (ID_tappa, durata, ID_escursione, data, nome_paese, nome_zona, nome_luogo) VALUES ('T999', 2.0, 'ESC-999', '2023-03-01', 'Italia', 'Abruzzo', 'Campo Imperatore');

-- PRENOTAZIONI
INSERT INTO prenota (CF, ID_escursione, data_prenotazione, stato) VALUES ('BNCGIA90B41F205Z', 'ESC-001', '2024-06-15', 'Completata');
INSERT INTO prenota (CF, ID_escursione, data_prenotazione, stato) VALUES ('MZZCHR05P64H199H', 'ESC-002', CURRENT_DATE, 'Confermata');
INSERT INTO prenota (CF, ID_escursione, data_prenotazione, stato) VALUES ('MNDARN05P64H199H', 'ESC-001', '2024-06-15', 'Completata');
INSERT INTO prenota (CF, ID_escursione, data_prenotazione, stato) VALUES ('MNDARN05P64H199H', 'ESC-003', CURRENT_DATE, 'Confermata');
INSERT INTO prenota (CF, ID_escursione, data_prenotazione, stato) VALUES ('OLVRBC05P64H199H', 'ESC-003', CURRENT_DATE, 'Confermata');
INSERT INTO prenota (CF, ID_escursione, data_prenotazione, stato) VALUES ('OLVRBC05P64H199H', 'ESC-002', CURRENT_DATE, 'Confermata');
INSERT INTO prenota (CF, ID_escursione, data_prenotazione, stato) VALUES ('MZZCHR05P64H199H', 'ESC-003', CURRENT_DATE, 'Confermata');
INSERT INTO prenota (CF, ID_escursione, data_prenotazione, stato) VALUES ('RSSMRA80A01H501T', 'ESC-008', CURRENT_DATE, 'Confermata');
INSERT INTO prenota (CF, ID_escursione, data_prenotazione, stato) VALUES ('HATER00000000001', 'ESC-999', '2023-01-15' , 'Completata');
INSERT INTO prenota (CF, ID_escursione, data_prenotazione, stato) VALUES ('HATER00000000002', 'ESC-999', '2023-01-15', 'Completata');
INSERT INTO prenota (CF, ID_escursione, data_prenotazione, stato) VALUES ('HATER00000000003', 'ESC-999', '2023-01-15', 'Completata');
INSERT INTO prenota (CF, ID_escursione, data_prenotazione, stato) VALUES ('HATER00000000004', 'ESC-999', '2023-01-15', 'Completata');
INSERT INTO prenota (CF, ID_escursione, data_prenotazione, stato) VALUES ('HATER00000000005', 'ESC-999', '2023-01-15', 'Completata');
INSERT INTO prenota (CF, ID_escursione, data_prenotazione, stato) VALUES ('HATER00000000006', 'ESC-999', '2023-01-15', 'Completata');

--  RECENSIONI
INSERT INTO RECENSIONI (titolo, CF, voto, immagini, descrizione, stato_recensione, ID_escursione)
VALUES ('Giornata Fantastica', 'BNCGIA90B41F205Z', 5, 'foto_gransasso.jpg', 'Panorama mozzafiato.', 'Approvata', 'ESC-001');
INSERT INTO RECENSIONI (titolo, voto, immagini, descrizione, stato_recensione, CF, ID_escursione)
VALUES ('Vista mozzafiato', 5, NULL, 'Sentiero impegnativo ma davvero spettacolare in vetta.', 'Approvata', 'MNDARN05P64H199H', 'ESC-001');
INSERT INTO RECENSIONI (titolo, voto, immagini, descrizione, stato_recensione, CF, ID_escursione)
VALUES ('Mare stupendo', 5, NULL, 'Posto incredibile in Sardegna', 'Approvata', 'RSSMRA80A01H501T', 'ESC-002');
INSERT INTO RECENSIONI (titolo, voto, immagini, descrizione, stato_recensione, CF, ID_escursione)
VALUES ('Acqua limpidissima', 5, NULL, 'Un vero paradiso terrestre, guida simpatica e preparata.', 'Approvata', 'MZZCHR05P64H199H', 'ESC-002');
INSERT INTO RECENSIONI (titolo, voto, immagini, descrizione, stato_recensione, CF, ID_escursione)
VALUES ('Ottimo vino', 4, NULL, 'Camminata piacevole ma troppo caldo', 'Approvata', 'RSSMRA80A01H501T', 'ESC-003');
INSERT INTO RECENSIONI (titolo, voto, immagini, descrizione, stato_recensione, CF, ID_escursione)
VALUES ('Passeggiata rilassante', 4, NULL, 'Molto bello, anche se c''era parecchia gente sul sentiero principale.', 'Approvata', 'MNDARN05P64H199H', 'ESC-003');
INSERT INTO RECENSIONI (titolo, voto, immagini, descrizione, stato_recensione, CF, ID_escursione)
VALUES ('Freddo estremo', 5, NULL, 'Fatica ampiamente ripagata', 'Approvata', 'RSSMRA80A01H501T', 'ESC-004');
INSERT INTO RECENSIONI (titolo, voto, immagini, descrizione, stato_recensione, CF, ID_escursione)
VALUES ('Adrenalina pura', 5, NULL, 'Esperienza al limite, servono i ramponi giusti ma è incredibile.', 'Approvata', 'OLVRBC05P64H199H', 'ESC-004');
INSERT INTO RECENSIONI (titolo, voto, immagini, descrizione, stato_recensione, CF, ID_escursione)
VALUES ('Magico', 5, NULL, 'Esperienza da fare una volta nella vita', 'Approvata', 'RSSMRA80A01H501T', 'ESC-005');
INSERT INTO RECENSIONI (titolo, voto, immagini, descrizione, stato_recensione, CF, ID_escursione)
VALUES ('Atmosfera unica', 5, NULL, 'Vedere l''alba sulle Tre Cime ti cambia la vita. Guida bravissima.', 'Approvata', 'BNCGIA90B41F205Z', 'ESC-005');
INSERT INTO RECENSIONI (titolo, voto, immagini, descrizione, stato_recensione, CF, ID_escursione) VALUES 
('Pessimo', 1, NULL, 'Guida in ritardo e maleducata.', 'Approvata', 'HATER00000000001', 'ESC-999'),
('Da evitare', 2, NULL, 'Percorso brutto e poco sicuro.', 'Approvata', 'HATER00000000002', 'ESC-999'),
('Soldi buttati', 1, NULL, 'Non ci siamo per niente.', 'Approvata', 'HATER00000000003', 'ESC-999'),
('Mai più', 2, NULL, 'Disorganizzazione totale.', 'Approvata', 'HATER00000000004', 'ESC-999'),
('Terribile', 1, NULL, 'Un incubo nel fango.', 'Approvata', 'HATER00000000005', 'ESC-999'),
('Sconsigliatissimo', 2, NULL, 'Pessima esperienza, guida distratta.', 'Approvata', 'HATER00000000006', 'ESC-999');

-- RIEPILOGHI
INSERT INTO riepiloga (ID_escursione, data_inizio, data_fine, temperatura_rilevata, precipitazioni, CF)
VALUES ('ESC-001', '2024-07-05', '2024-07-05', 18.5, 0.0, 'NNANNA85M22F205Z');

-- ABBONAMENTI
INSERT INTO tipologie_abbonamento (costo_mensile, durata, sconto_noleggio, vantaggi_accesso) 
VALUES 
(9.90, 1, 0.10, 'Accesso prioritario e Sconto Noleggio 10%'), 
(7.90, 6, 0.20, 'Accesso prioritario e Sconto Noleggio 20%'), 
(5.90, 12, 0.30, 'Accesso prioritario e Sconto Noleggio 30%');

-- RICALCOLO ESCURSIONI_EFFETTUATE (attributo ridondante)
SET SQL_SAFE_UPDATES = 0;

UPDATE PERSONE p
SET p.escursioni_effettuate = (
    SELECT COUNT(*)
    FROM prenota pr
    WHERE pr.CF = p.CF
    AND pr.stato IN ('Completata', 'Saldato')
);

SET SQL_SAFE_UPDATES = 1;