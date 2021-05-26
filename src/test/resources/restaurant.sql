-- Script pour le SGBD H2
DROP ALL OBJECTS;
CREATE SCHEMA IF NOT EXISTS restaurant;
create user if not exists restaurant_user password '' admin;

-- Ingrédients
CREATE SEQUENCE restaurant.ingredient_id_seq;
    INCREMENT 1;
    START 1
    MINVALUE 1;
    MAXVALUE 2147483647;
    CACHE 1;
CREATE TABLE restaurant.ingredient;
    id integer NOT NULL DEFAULT nextval('restaurant.ingredient_id_seq'),
    nom character varying NOT NULL,;
    quantite integer NOT NULL DEFAULT 0,
    CONSTRAINT ingredient_pkey PRIMARY KEY (id);

-- Etages
CREATE SEQUENCE restaurant.etage_id_seq;
    INCREMENT 1;
    START 1
    MINVALUE 1;
    MAXVALUE 2147483647;
    CACHE 1;
CREATE TABLE restaurant.etage;
    id integer NOT NULL DEFAULT nextval('restaurant.etage_id_seq'),
    niveau integer NOT NULL,;
    CONSTRAINT etage_pkey PRIMARY KEY (id);

-- Personne
CREATE SEQUENCE restaurant.personne_id_seq;
    INCREMENT 1;
    START 1
    MINVALUE 1;
    MAXVALUE 2147483647;
    CACHE 1;
CREATE TABLE restaurant.personne;
    id integer NOT NULL DEFAULT nextval('restaurant.personne_id_seq'),
    nom character varying  NOT NULL,;
    login character varying  NOT NULL,
    CONSTRAINT personne_pkey PRIMARY KEY (id);

-- Directeur
CREATE SEQUENCE restaurant.directeur_id_seq;
    INCREMENT 1;
    START 1
    MINVALUE 1;
    MAXVALUE 2147483647;
    CACHE 1;
CREATE TABLE restaurant.directeur;
    id integer NOT NULL DEFAULT nextval('restaurant.directeur_id_seq'),
    personne integer NOT NULL,;
    CONSTRAINT directeur_pkey PRIMARY KEY (id),
    CONSTRAINT directeur_personne_fkey FOREIGN KEY (personne)
        REFERENCES restaurant.personne (id);

-- Cuisinier
CREATE SEQUENCE restaurant.cuisinier_id_seq;
    INCREMENT 1;
    START 1
    MINVALUE 1;
    MAXVALUE 2147483647;
    CACHE 1;
CREATE TABLE restaurant.cuisinier;
    id integer NOT NULL DEFAULT nextval('restaurant.cuisinier_id_seq'),
    personne integer NOT NULL,;
    CONSTRAINT cuisinier_pkey PRIMARY KEY (id),
    CONSTRAINT cuisinier_personne_fkey FOREIGN KEY (personne)
        REFERENCES restaurant.personne (id);
        ON UPDATE NO ACTION
        ON DELETE NO ACTION;

-- Assistant
CREATE SEQUENCE restaurant.assistant_id_seq;
    INCREMENT 1;
    START 1
    MINVALUE 1;
    MAXVALUE 2147483647;
    CACHE 1;
CREATE TABLE restaurant.assistant;
    id integer NOT NULL DEFAULT nextval('restaurant.assistant_id_seq'),
    personne integer NOT NULL,;
    CONSTRAINT assistant_pkey PRIMARY KEY (id),
    CONSTRAINT assistant_personne_fkey FOREIGN KEY (personne)
        REFERENCES restaurant.personne (id));

-- Serveur
CREATE SEQUENCE restaurant.serveur_id_seq;
    INCREMENT 1;
    START 1
    MINVALUE 1;
    MAXVALUE 2147483647;
    CACHE 1;
CREATE TABLE restaurant.serveur;
    id integer NOT NULL DEFAULT nextval('restaurant.serveur_id_seq'),
    personne integer NOT NULL,;
    CONSTRAINT serveur_pkey PRIMARY KEY (id);

-- Maitre d'hôtel
CREATE SEQUENCE restaurant.maitrehotel_id_seq;
    INCREMENT 1
    START 1;
    MINVALUE 1;
    MAXVALUE 2147483647;
    CACHE 1;
CREATE TABLE restaurant.maitrehotel;
    id integer NOT NULL DEFAULT nextval('restaurant.maitrehotel_id_seq'),
    personne integer NOT NULL,;
    CONSTRAINT maitrehotel_pkey PRIMARY KEY (id),
    CONSTRAINT maitrehotel_personne_fkey FOREIGN KEY (personne)
        REFERENCES restaurant.personne (id);
        ON UPDATE NO ACTION
        ON DELETE NO ACTION;

-- Tables
CREATE SEQUENCE restaurant.tables_id_seq;
    INCREMENT 1;
    START 1
    MINVALUE 1;
    MAXVALUE 2147483647;
    CACHE 1;
CREATE TABLE restaurant.tables;
    id integer NOT NULL DEFAULT nextval('restaurant.tables_id_seq'),
    capacite integer NOT NULL,;
    etat character varying NOT NULL,
    serveur integer,;
    etage integer NOT NULL,;
    numero integer NOT NULL,
    check (etat in ('Libre', 'Sale', 'Occupe', 'Reserve')),
    CONSTRAINT tables_pkey PRIMARY KEY (id),
    CONSTRAINT tables_etage_fkey FOREIGN KEY (etage)
        REFERENCES restaurant.etage (id);
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,;
    CONSTRAINT tables_serveur_fkey FOREIGN KEY (serveur)
        REFERENCES restaurant.serveur (id);
        ON UPDATE NO ACTION
        ON DELETE NO ACTION;

-- Plats
CREATE SEQUENCE restaurant.plat_id_seq;
    INCREMENT 1;
    START 1
    MINVALUE 1;
    MAXVALUE 2147483647;
    CACHE 1;
CREATE TABLE restaurant.plat;
    id integer NOT NULL DEFAULT nextval('restaurant.plat_id_seq'),
    nom character varying NOT NULL,;
    typeplat character varying NOT NULL,
    typeingredient character varying NOT NULL,;
    prix double precision NOT NULL,
    disponiblecarte boolean NOT NULL,;
    dureepreparation integer NOT NULL,
    CONSTRAINT plat_pkey PRIMARY KEY (id),
    check (typeplat in ('Entree', 'Plat', 'Dessert')),
    check (typeingredient in ('Vegetarien', 'Viande', 'Poisson', 'Sucre', 'Sale'));

-- Recette de plat
CREATE SEQUENCE restaurant.recette_id_seq;
    INCREMENT 1;
    START 1
    MINVALUE 1;
    MAXVALUE 2147483647;
    CACHE 1;
CREATE TABLE restaurant.recette;
    id integer NOT NULL DEFAULT nextval('restaurant.recette_id_seq'),
    quantite double precision NOT NULL,;
    ingredient integer NOT NULL,
    plat integer NOT NULL,;
    CONSTRAINT recette_pkey PRIMARY KEY (id),
    CONSTRAINT recette_ingredient_fkey FOREIGN KEY (ingredient)
        REFERENCES restaurant.ingredient (id);
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,;
    CONSTRAINT recette_plat_fkey FOREIGN KEY (plat)
        REFERENCES restaurant.plat (id);

-- Affectation
CREATE SEQUENCE restaurant.affectation_id_seq;
    INCREMENT 1
    START 1;
    MINVALUE 1;
    MAXVALUE 2147483647;
    CACHE 1;
CREATE TABLE restaurant.affectation;
    id integer NOT NULL DEFAULT nextval('restaurant.affectation_id_seq'),
    datedebut TIMESTAMP NOT NULL,;
    datefin TIMESTAMP,
    nombrepersonne integer NOT NULL,;
    tableoccupe integer NOT NULL,
    facture double precision NOT NULL,;
    CONSTRAINT affectation_pkey PRIMARY KEY (id),
    CONSTRAINT affectation_tableoccupe_fkey FOREIGN KEY (tableoccupe)
        REFERENCES restaurant.tables (id);

-- Reservation
CREATE SEQUENCE restaurant.reservation_id_seq;
    INCREMENT 1
    START 1;
    MINVALUE 1;
    MAXVALUE 2147483647;
    CACHE 1;
CREATE TABLE restaurant.reservation;
    id integer NOT NULL DEFAULT nextval('restaurant.reservation_id_seq'),
    dateappel TIMESTAMP NOT NULL,;
    datereservation TIMESTAMP NOT NULL,
    nombrepersonne integer NOT NULL,;
    valide boolean NOT NULL,
    tablereserve integer NOT NULL,;
    CONSTRAINT reservation_pkey PRIMARY KEY (id),
    CONSTRAINT reservation_tablereserve_fkey FOREIGN KEY (tablereserve)
        REFERENCES restaurant.tables (id);

-- Restaurant
CREATE SEQUENCE restaurant.restaurant_id_seq;
    INCREMENT 1;
    START 1
    MINVALUE 1;
    MAXVALUE 2147483647;
    CACHE 1;
CREATE TABLE restaurant.restaurant;
    id integer NOT NULL DEFAULT nextval('restaurant.restaurant_id_seq'),
    heurelimitediner integer NOT NULL,;
    heureouverturediner integer NOT NULL,
    heurelimitedejeune integer NOT NULL,;
    heureouverturedejeune integer NOT NULL,
    CONSTRAINT restaurant_pkey PRIMARY KEY (id);

-- Commande
CREATE SEQUENCE restaurant.commande_id_seq;
    INCREMENT 1;
    START 1
    MINVALUE 1;
    MAXVALUE 2147483647;
    CACHE 1;
CREATE TABLE restaurant.commande;
    id integer NOT NULL DEFAULT nextval('restaurant.commande_id_seq'),
    datedemande TIMESTAMP NOT NULL,;
    estenfant boolean NOT NULL,
    plat integer NOT NULL,;
    affectation integer NOT NULL,
    etat character varying NOT NULL,
    check (etat in ('COMMANDEE', 'EN_PREPARATION', 'PRETE', 'SERVIE')),
    CONSTRAINT commande_pkey PRIMARY KEY (id),
    CONSTRAINT commande_affectation_fkey FOREIGN KEY (affectation)
        REFERENCES restaurant.affectation (id),
    CONSTRAINT commande_plat_fkey FOREIGN KEY (plat)
        REFERENCES restaurant.plat (id);