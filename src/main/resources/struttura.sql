create database if not exists DestinationBuddy;
use DestinationBuddy;

create table if not exists PERSONE (
    CF varchar(16) not null,
    nome varchar(50) not null,
    cognome varchar(50) not null,
    tipo_utente bit,
    tipo_amministratore bit,
    ID_account varchar(50) not null,
    escursioni_effettuate int not null default 0,
    data_iscrizione date not null,
    data_assunzione date,
    email varchar(100) not null,
    password varchar(255) not null,
    ruolo varchar(50),
    constraint persone_pk primary key (CF)
);

create table if not exists TIPOLOGIE_ABBONAMENTO (
    costo_mensile decimal(8,2) not null,
    durata int not null,
    sconto_noleggio decimal(5,2),
    vantaggi_accesso text,
    constraint tipologie_abbonamento_pk primary key (costo_mensile, durata)
);

create table if not exists TIPOLOGIE_CERTIFICAZIONE (
    ID_certificazione varchar(50) not null,
    livello varchar(50),
    constraint tipologie_certificazione_pk primary key (ID_certificazione)
);

create table if not exists TIPOLOGIE_ESCURSIONI (
    ID_tipologia varchar(50) not null,
    constraint tipologie_escursioni_pk primary key (ID_tipologia)
);

create table if not exists EQUIPAGGIAMENTI (
    ID_categoria varchar(50) not null,
    costo_totale_giornaliero decimal(8,2),
    constraint equipaggiamenti_pk primary key (ID_categoria)
);

create table if not exists PAESI (
    Nome varchar(100) not null,
    constraint paesi_pk primary key (Nome)
);

create table if not exists CATEGORIE (
    nome_categoria varchar(50) not null,
    constraint categorie_pk primary key (nome_categoria)
);

create table if not exists GUIDE (
    CF varchar(16) not null,
    stato_account bit,
    constraint guide_pk primary key (CF)
);

create table if not exists ABBONAMENTI (
    data_abbonamento date not null,
    data_pagamento date not null,
    CF varchar(16) not null,
    costo_mensile decimal(8,2) not null,
    durata int not null,
    constraint abbonamenti_pk primary key (CF, data_abbonamento)
);

create table if not exists ZONE (
    nome_paese varchar(100) not null,
    nome varchar(100) not null,
    descrizione text,
    constraint zone_pk primary key (nome_paese, nome)
);

create table if not exists PEZZI (
    ID_pezzo varchar(50) not null,
    nome varchar(100) not null,
    costo_giornaliero decimal(8,2),
    disponibilita bit,
    ID_categoria varchar(50) not null,
    constraint pezzi_pk primary key (ID_pezzo)
);

create table if not exists CERTIFICAZIONI (
    ID_certificazione varchar(50) not null,
    n_certificazione varchar(50) not null,
    ente_rilasciante varchar(100),
    data_rilascio date,
    data_scadenza date,
    stato_validazione varchar(50),
    CF varchar(16) not null,
    Guida_CF varchar(16),
    constraint certificazioni_pk primary key (ID_certificazione, n_certificazione)
);

create table if not exists ESCURSIONI (
    ID_escursione varchar(50) not null,
    titolo varchar(200) not null,
    descrizione text,
    difficolta varchar(50),
    numero_partecipanti int,
    costo decimal(8,2),
    data_apertura_iscrizione date,
    data_chiusura_iscrizione date,
    Guida_CF varchar(16) not null,
    constraint escursioni_pk primary key (ID_escursione)
);

create table if not exists LUOGHI_ESPLORABILI (
    nome_paese varchar(100) not null,
    nome_zona varchar(100) not null,
    nome varchar(100) not null,
    quota decimal(7,2),
    difficolta_accesso varchar(50),
    periodo_consigliato varchar(100),
    nome_categoria varchar(50),
    constraint luoghi_esplorabili_pk primary key (nome_paese, nome_zona, nome)
);

create table if not exists RECENSIONI (
    titolo varchar(200) not null,
    CF varchar(16) not null,
    voto int not null,
    immagini text,
    descrizione text,
    stato_recensione varchar(50),
    ID_escursione varchar(50) not null,
    constraint recensioni_pk primary key (titolo, CF),
    constraint recensioni_voto_chk check (voto between 1 and 5)
);

create table if not exists prenota (
    CF varchar(16) not null,
    ID_escursione varchar(50) not null,
    data_prenotazione date not null,
    stato varchar(50),
    constraint prenota_pk primary key (CF, ID_escursione)
);

create table if not exists richiede (
    ID_tipologia varchar(50) not null,
    ID_certificazione varchar(50) not null,
    constraint richiede_pk primary key (ID_tipologia, ID_certificazione)
);

create table if not exists riepiloga (
    ID_escursione varchar(50) not null,
    data_inizio date not null,
    data_fine date not null,
    temperatura_rilevata decimal(5,2),
    precipitazioni decimal(7,2),
    CF varchar(16) not null,
    constraint riepiloga_pk primary key (ID_escursione)
);

create table if not exists assume (
    ID_escursione varchar(50) not null,
    ID_tipologia varchar(50) not null,
    constraint assume_pk primary key (ID_escursione, ID_tipologia)
);

create table if not exists necessita (
    ID_categoria varchar(50) not null,
    ID_tipologia varchar(50) not null,
    constraint necessita_pk primary key (ID_categoria, ID_tipologia)
);

create table if not exists assegna (
    ID_pezzo varchar(50) not null,
    ID_escursione varchar(50) not null,
    CF varchar(16) not null,
    data_noleggio date,
    durata_noleggio int,
    constraint assegna_pk primary key (ID_pezzo, ID_escursione, CF)
);

create table if not exists GIORNATE (
    ID_escursione varchar(50) not null,
    data date not null,
    programma text,
    constraint giornate_pk primary key (ID_escursione, data)
);

create table if not exists CONDIZIONI_METEO (
    nome_paese varchar(100) not null,
    nome_zona varchar(100) not null,
    nome_luogo varchar(100) not null,
    stagione varchar(20) not null,
    temperaturaMedia decimal(5,2),
    precipitazioni decimal(7,2),
    constraint condizioni_meteo_pk primary key (nome_paese, nome_zona, nome_luogo, stagione),
    constraint meteo_stagione_chk check (stagione in ('Primavera','Estate','Autunno','Inverno'))
);

create table if not exists TAPPE (
    ID_tappa varchar(50) not null,
    durata int,
    ID_escursione varchar(50) not null,
    data date not null,
    nome_paese varchar(100) not null,
    nome_zona varchar(100) not null,
    nome_luogo varchar(100) not null,
    constraint tappe_pk primary key (ID_tappa)
);


-- Constraints -----------------------------------------------------------------

alter table GUIDE add constraint guide_references_persone
foreign key (CF) references PERSONE (CF);

alter table ABBONAMENTI add constraint abbonamenti_references_persone
foreign key (CF) references PERSONE (CF);

alter table ABBONAMENTI add constraint abbonamenti_references_tipologie
foreign key (costo_mensile, durata) references TIPOLOGIE_ABBONAMENTO (costo_mensile, durata);

alter table ZONE add constraint zone_references_paesi
foreign key (nome_paese) references PAESI (Nome);

alter table PEZZI add constraint pezzi_references_equipaggiamenti
foreign key (ID_categoria) references EQUIPAGGIAMENTI (ID_categoria);

alter table CERTIFICAZIONI add constraint certificazioni_references_tipologie
foreign key (ID_certificazione) references TIPOLOGIE_CERTIFICAZIONE (ID_certificazione);

alter table CERTIFICAZIONI add constraint certificazioni_references_persone
foreign key (CF) references PERSONE (CF);

alter table CERTIFICAZIONI add constraint certificazioni_references_guide
foreign key (Guida_CF) references GUIDE (CF);

alter table ESCURSIONI add constraint escursioni_references_guide
foreign key (Guida_CF) references GUIDE (CF);

alter table LUOGHI_ESPLORABILI add constraint luoghi_references_zone
foreign key (nome_paese, nome_zona) references ZONE (nome_paese, nome);

alter table LUOGHI_ESPLORABILI add constraint luoghi_references_categorie
foreign key (nome_categoria) references CATEGORIE (nome_categoria);

alter table RECENSIONI add constraint recensioni_references_persone
foreign key (CF) references PERSONE (CF);

alter table RECENSIONI add constraint recensioni_references_escursioni
foreign key (ID_escursione) references ESCURSIONI (ID_escursione);

alter table prenota add constraint prenota_references_persone
foreign key (CF) references PERSONE (CF);

alter table prenota add constraint prenota_references_escursioni
foreign key (ID_escursione) references ESCURSIONI (ID_escursione);

alter table richiede add constraint richiede_references_escursioni
foreign key (ID_tipologia) references TIPOLOGIE_ESCURSIONI (ID_tipologia);

alter table richiede add constraint richiede_references_certificazioni
foreign key (ID_certificazione) references TIPOLOGIE_CERTIFICAZIONE (ID_certificazione);

alter table riepiloga add constraint riepiloga_references_escursioni
foreign key (ID_escursione) references ESCURSIONI (ID_escursione);

alter table riepiloga add constraint riepiloga_references_persone
foreign key (CF) references PERSONE (CF);

alter table assume add constraint assume_references_escursioni
foreign key (ID_escursione) references ESCURSIONI (ID_escursione);

alter table assume add constraint assume_references_tipologie
foreign key (ID_tipologia) references TIPOLOGIE_ESCURSIONI (ID_tipologia);

alter table necessita add constraint necessita_references_equipaggiamenti
foreign key (ID_categoria) references EQUIPAGGIAMENTI (ID_categoria);

alter table necessita add constraint necessita_references_tipologie
foreign key (ID_tipologia) references TIPOLOGIE_ESCURSIONI (ID_tipologia);

alter table assegna add constraint assegna_references_pezzi
foreign key (ID_pezzo) references PEZZI (ID_pezzo);

alter table assegna add constraint assegna_references_escursioni
foreign key (ID_escursione) references ESCURSIONI (ID_escursione);

alter table assegna add constraint assegna_references_persone
foreign key (CF) references PERSONE (CF);

alter table GIORNATE add constraint giornate_references_escursioni
foreign key (ID_escursione) references ESCURSIONI (ID_escursione);

alter table CONDIZIONI_METEO add constraint meteo_references_luoghi
foreign key (nome_paese, nome_zona, nome_luogo) references LUOGHI_ESPLORABILI (nome_paese, nome_zona, nome);

alter table TAPPE add constraint tappe_references_giornate
foreign key (ID_escursione, data) references GIORNATE (ID_escursione, data);

alter table TAPPE add constraint tappe_references_luoghi
foreign key (nome_paese, nome_zona, nome_luogo) references LUOGHI_ESPLORABILI (nome_paese, nome_zona, nome);