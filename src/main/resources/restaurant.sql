DROP SCHEMA IF EXISTS restaurant CASCADE;

--
-- PostgreSQL database dump
--

-- Dumped from database version 11.9 (Raspbian 11.9-0+deb10u1)
-- Dumped by pg_dump version 11.9 (Raspbian 11.9-0+deb10u1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;
SET TIMEZONE='Europe/Paris';
--
-- Name: restaurant; Type: SCHEMA; Schema: -; Owner: restaurant_user
--

CREATE SCHEMA restaurant;


ALTER SCHEMA restaurant OWNER TO restaurant_user;

--
-- Name: etatcommande; Type: TYPE; Schema: restaurant; Owner: postgres
--

CREATE TYPE restaurant.etatcommande AS ENUM (
    'COMMANDEE',
    'EN_PREPARATION',
    'PRETE',
    'SERVIE'
);


ALTER TYPE restaurant.etatcommande OWNER TO postgres;

--
-- Name: etattable; Type: TYPE; Schema: restaurant; Owner: postgres
--

CREATE TYPE restaurant.etattable AS ENUM (
    'Libre',
    'Sale',
    'Occupe',
    'Reserve'
);


ALTER TYPE restaurant.etattable OWNER TO postgres;

--
-- Name: typeingredient; Type: TYPE; Schema: restaurant; Owner: postgres
--

CREATE TYPE restaurant.typeingredient AS ENUM (
    'Vegetarien',
    'Viande',
    'Poisson',
    'Sucre',
    'Sale'
);


ALTER TYPE restaurant.typeingredient OWNER TO postgres;

--
-- Name: typeplat; Type: TYPE; Schema: restaurant; Owner: postgres
--

CREATE TYPE restaurant.typeplat AS ENUM (
    'Entree',
    'Plat',
    'Dessert'
);


ALTER TYPE restaurant.typeplat OWNER TO postgres;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: affectation; Type: TABLE; Schema: restaurant; Owner: restaurant_user
--

CREATE TABLE restaurant.affectation (
    id integer NOT NULL,
    datedebut timestamp NOT NULL,
    datefin timestamp,
    nombrepersonne integer NOT NULL,
    tableoccupe integer NOT NULL,
    facture double precision NOT NULL
);


ALTER TABLE restaurant.affectation OWNER TO restaurant_user;

--
-- Name: affectation_id_seq; Type: SEQUENCE; Schema: restaurant; Owner: restaurant_user
--

CREATE SEQUENCE restaurant.affectation_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE restaurant.affectation_id_seq OWNER TO restaurant_user;

--
-- Name: affectation_id_seq; Type: SEQUENCE OWNED BY; Schema: restaurant; Owner: restaurant_user
--

ALTER SEQUENCE restaurant.affectation_id_seq OWNED BY restaurant.affectation.id;


--
-- Name: assistant; Type: TABLE; Schema: restaurant; Owner: restaurant_user
--

CREATE TABLE restaurant.assistant (
    id integer NOT NULL,
    personne integer NOT NULL
);


ALTER TABLE restaurant.assistant OWNER TO restaurant_user;

--
-- Name: assistant_id_seq; Type: SEQUENCE; Schema: restaurant; Owner: restaurant_user
--

CREATE SEQUENCE restaurant.assistant_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE restaurant.assistant_id_seq OWNER TO restaurant_user;

--
-- Name: assistant_id_seq; Type: SEQUENCE OWNED BY; Schema: restaurant; Owner: restaurant_user
--

ALTER SEQUENCE restaurant.assistant_id_seq OWNED BY restaurant.assistant.id;


--
-- Name: commande; Type: TABLE; Schema: restaurant; Owner: restaurant_user
--

CREATE TABLE restaurant.commande (
    id integer NOT NULL,
    datedemande timestamp NOT NULL,
    estenfant boolean NOT NULL,
    plat integer NOT NULL,
    affectation integer NOT NULL,
    etat restaurant.etatcommande NOT NULL
);


ALTER TABLE restaurant.commande OWNER TO restaurant_user;

--
-- Name: commande_id_seq; Type: SEQUENCE; Schema: restaurant; Owner: restaurant_user
--

CREATE SEQUENCE restaurant.commande_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE restaurant.commande_id_seq OWNER TO restaurant_user;

--
-- Name: commande_id_seq; Type: SEQUENCE OWNED BY; Schema: restaurant; Owner: restaurant_user
--

ALTER SEQUENCE restaurant.commande_id_seq OWNED BY restaurant.commande.id;


--
-- Name: cuisinier; Type: TABLE; Schema: restaurant; Owner: restaurant_user
--

CREATE TABLE restaurant.cuisinier (
    id integer NOT NULL,
    personne integer NOT NULL
);


ALTER TABLE restaurant.cuisinier OWNER TO restaurant_user;

--
-- Name: cuisinier_id_seq; Type: SEQUENCE; Schema: restaurant; Owner: restaurant_user
--

CREATE SEQUENCE restaurant.cuisinier_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE restaurant.cuisinier_id_seq OWNER TO restaurant_user;

--
-- Name: cuisinier_id_seq; Type: SEQUENCE OWNED BY; Schema: restaurant; Owner: restaurant_user
--

ALTER SEQUENCE restaurant.cuisinier_id_seq OWNED BY restaurant.cuisinier.id;


--
-- Name: directeur; Type: TABLE; Schema: restaurant; Owner: restaurant_user
--

CREATE TABLE restaurant.directeur (
    id integer NOT NULL,
    personne integer NOT NULL
);


ALTER TABLE restaurant.directeur OWNER TO restaurant_user;

--
-- Name: directeur_id_seq; Type: SEQUENCE; Schema: restaurant; Owner: restaurant_user
--

CREATE SEQUENCE restaurant.directeur_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE restaurant.directeur_id_seq OWNER TO restaurant_user;

--
-- Name: directeur_id_seq; Type: SEQUENCE OWNED BY; Schema: restaurant; Owner: restaurant_user
--

ALTER SEQUENCE restaurant.directeur_id_seq OWNED BY restaurant.directeur.id;


--
-- Name: etage; Type: TABLE; Schema: restaurant; Owner: restaurant_user
--

CREATE TABLE restaurant.etage (
    id integer NOT NULL,
    niveau integer NOT NULL
);


ALTER TABLE restaurant.etage OWNER TO restaurant_user;

--
-- Name: etage_id_seq; Type: SEQUENCE; Schema: restaurant; Owner: restaurant_user
--

CREATE SEQUENCE restaurant.etage_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE restaurant.etage_id_seq OWNER TO restaurant_user;

--
-- Name: etage_id_seq; Type: SEQUENCE OWNED BY; Schema: restaurant; Owner: restaurant_user
--

ALTER SEQUENCE restaurant.etage_id_seq OWNED BY restaurant.etage.id;


--
-- Name: ingredient; Type: TABLE; Schema: restaurant; Owner: restaurant_user
--

CREATE TABLE restaurant.ingredient (
    id integer NOT NULL,
    nom character varying NOT NULL,
    quantite integer DEFAULT 0 NOT NULL
);


ALTER TABLE restaurant.ingredient OWNER TO restaurant_user;

--
-- Name: ingredient_id_seq; Type: SEQUENCE; Schema: restaurant; Owner: restaurant_user
--

CREATE SEQUENCE restaurant.ingredient_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE restaurant.ingredient_id_seq OWNER TO restaurant_user;

--
-- Name: ingredient_id_seq; Type: SEQUENCE OWNED BY; Schema: restaurant; Owner: restaurant_user
--

ALTER SEQUENCE restaurant.ingredient_id_seq OWNED BY restaurant.ingredient.id;


--
-- Name: maitrehotel; Type: TABLE; Schema: restaurant; Owner: restaurant_user
--

CREATE TABLE restaurant.maitrehotel (
    id integer NOT NULL,
    personne integer NOT NULL
);


ALTER TABLE restaurant.maitrehotel OWNER TO restaurant_user;

--
-- Name: maitrehotel_id_seq; Type: SEQUENCE; Schema: restaurant; Owner: restaurant_user
--

CREATE SEQUENCE restaurant.maitrehotel_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE restaurant.maitrehotel_id_seq OWNER TO restaurant_user;

--
-- Name: maitrehotel_id_seq; Type: SEQUENCE OWNED BY; Schema: restaurant; Owner: restaurant_user
--

ALTER SEQUENCE restaurant.maitrehotel_id_seq OWNED BY restaurant.maitrehotel.id;


--
-- Name: personne; Type: TABLE; Schema: restaurant; Owner: restaurant_user
--

CREATE TABLE restaurant.personne (
    id integer NOT NULL,
    nom character varying NOT NULL,
    login character varying NOT NULL
);


ALTER TABLE restaurant.personne OWNER TO restaurant_user;

--
-- Name: personne_id_seq; Type: SEQUENCE; Schema: restaurant; Owner: restaurant_user
--

CREATE SEQUENCE restaurant.personne_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE restaurant.personne_id_seq OWNER TO restaurant_user;

--
-- Name: personne_id_seq; Type: SEQUENCE OWNED BY; Schema: restaurant; Owner: restaurant_user
--

ALTER SEQUENCE restaurant.personne_id_seq OWNED BY restaurant.personne.id;


--
-- Name: plat; Type: TABLE; Schema: restaurant; Owner: restaurant_user
--

CREATE TABLE restaurant.plat (
    id integer NOT NULL,
    nom character varying NOT NULL,
    typeplat restaurant.typeplat NOT NULL,
    prix double precision NOT NULL,
    disponiblecarte boolean NOT NULL,
    dureepreparation integer NOT NULL,
    typeingredient restaurant.typeingredient NOT NULL
);


ALTER TABLE restaurant.plat OWNER TO restaurant_user;

--
-- Name: plat_id_seq; Type: SEQUENCE; Schema: restaurant; Owner: restaurant_user
--

CREATE SEQUENCE restaurant.plat_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE restaurant.plat_id_seq OWNER TO restaurant_user;

--
-- Name: plat_id_seq; Type: SEQUENCE OWNED BY; Schema: restaurant; Owner: restaurant_user
--

ALTER SEQUENCE restaurant.plat_id_seq OWNED BY restaurant.plat.id;


--
-- Name: recette; Type: TABLE; Schema: restaurant; Owner: restaurant_user
--

CREATE TABLE restaurant.recette (
    id integer NOT NULL,
    quantite double precision NOT NULL,
    ingredient integer NOT NULL,
    plat integer NOT NULL
);


ALTER TABLE restaurant.recette OWNER TO restaurant_user;

--
-- Name: recette_id_seq; Type: SEQUENCE; Schema: restaurant; Owner: restaurant_user
--

CREATE SEQUENCE restaurant.recette_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE restaurant.recette_id_seq OWNER TO restaurant_user;

--
-- Name: recette_id_seq; Type: SEQUENCE OWNED BY; Schema: restaurant; Owner: restaurant_user
--

ALTER SEQUENCE restaurant.recette_id_seq OWNED BY restaurant.recette.id;


--
-- Name: reservation; Type: TABLE; Schema: restaurant; Owner: restaurant_user
--

CREATE TABLE restaurant.reservation (
    id integer NOT NULL,
    dateappel timestamp NOT NULL,
    datereservation timestamp NOT NULL,
    nombrepersonne integer NOT NULL,
    valide boolean NOT NULL,
    tablereserve integer NOT NULL
);


ALTER TABLE restaurant.reservation OWNER TO restaurant_user;

--
-- Name: reservation_id_seq; Type: SEQUENCE; Schema: restaurant; Owner: restaurant_user
--

CREATE SEQUENCE restaurant.reservation_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE restaurant.reservation_id_seq OWNER TO restaurant_user;

--
-- Name: reservation_id_seq; Type: SEQUENCE OWNED BY; Schema: restaurant; Owner: restaurant_user
--

ALTER SEQUENCE restaurant.reservation_id_seq OWNED BY restaurant.reservation.id;


--
-- Name: restaurant; Type: TABLE; Schema: restaurant; Owner: restaurant_user
--

CREATE TABLE restaurant.restaurant (
    id integer NOT NULL,
    heurelimitediner integer NOT NULL,
    heureouverturediner integer NOT NULL,
    heurelimitedejeune integer NOT NULL,
    heureouverturedejeune integer NOT NULL
);


ALTER TABLE restaurant.restaurant OWNER TO restaurant_user;

--
-- Name: restaurant_id_seq; Type: SEQUENCE; Schema: restaurant; Owner: restaurant_user
--

CREATE SEQUENCE restaurant.restaurant_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE restaurant.restaurant_id_seq OWNER TO restaurant_user;

--
-- Name: restaurant_id_seq; Type: SEQUENCE OWNED BY; Schema: restaurant; Owner: restaurant_user
--

ALTER SEQUENCE restaurant.restaurant_id_seq OWNED BY restaurant.restaurant.id;


--
-- Name: serveur; Type: TABLE; Schema: restaurant; Owner: restaurant_user
--

CREATE TABLE restaurant.serveur (
    id integer NOT NULL,
    personne integer NOT NULL
);


ALTER TABLE restaurant.serveur OWNER TO restaurant_user;

--
-- Name: serveur_id_seq; Type: SEQUENCE; Schema: restaurant; Owner: restaurant_user
--

CREATE SEQUENCE restaurant.serveur_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE restaurant.serveur_id_seq OWNER TO restaurant_user;

--
-- Name: serveur_id_seq; Type: SEQUENCE OWNED BY; Schema: restaurant; Owner: restaurant_user
--

ALTER SEQUENCE restaurant.serveur_id_seq OWNED BY restaurant.serveur.id;


--
-- Name: tables; Type: TABLE; Schema: restaurant; Owner: restaurant_user
--

CREATE TABLE restaurant.tables (
    id integer NOT NULL,
    capacite integer NOT NULL,
    etat restaurant.etattable NOT NULL,
    etage integer NOT NULL,
    numero integer NOT NULL,
    serveur integer
);


ALTER TABLE restaurant.tables OWNER TO restaurant_user;

--
-- Name: tables_id_seq; Type: SEQUENCE; Schema: restaurant; Owner: restaurant_user
--

CREATE SEQUENCE restaurant.tables_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE restaurant.tables_id_seq OWNER TO restaurant_user;

--
-- Name: tables_id_seq; Type: SEQUENCE OWNED BY; Schema: restaurant; Owner: restaurant_user
--

ALTER SEQUENCE restaurant.tables_id_seq OWNED BY restaurant.tables.id;


--
-- Name: affectation id; Type: DEFAULT; Schema: restaurant; Owner: restaurant_user
--

ALTER TABLE ONLY restaurant.affectation ALTER COLUMN id SET DEFAULT nextval('restaurant.affectation_id_seq'::regclass);


--
-- Name: assistant id; Type: DEFAULT; Schema: restaurant; Owner: restaurant_user
--

ALTER TABLE ONLY restaurant.assistant ALTER COLUMN id SET DEFAULT nextval('restaurant.assistant_id_seq'::regclass);


--
-- Name: commande id; Type: DEFAULT; Schema: restaurant; Owner: restaurant_user
--

ALTER TABLE ONLY restaurant.commande ALTER COLUMN id SET DEFAULT nextval('restaurant.commande_id_seq'::regclass);


--
-- Name: cuisinier id; Type: DEFAULT; Schema: restaurant; Owner: restaurant_user
--

ALTER TABLE ONLY restaurant.cuisinier ALTER COLUMN id SET DEFAULT nextval('restaurant.cuisinier_id_seq'::regclass);


--
-- Name: directeur id; Type: DEFAULT; Schema: restaurant; Owner: restaurant_user
--

ALTER TABLE ONLY restaurant.directeur ALTER COLUMN id SET DEFAULT nextval('restaurant.directeur_id_seq'::regclass);


--
-- Name: etage id; Type: DEFAULT; Schema: restaurant; Owner: restaurant_user
--

ALTER TABLE ONLY restaurant.etage ALTER COLUMN id SET DEFAULT nextval('restaurant.etage_id_seq'::regclass);


--
-- Name: ingredient id; Type: DEFAULT; Schema: restaurant; Owner: restaurant_user
--

ALTER TABLE ONLY restaurant.ingredient ALTER COLUMN id SET DEFAULT nextval('restaurant.ingredient_id_seq'::regclass);


--
-- Name: maitrehotel id; Type: DEFAULT; Schema: restaurant; Owner: restaurant_user
--

ALTER TABLE ONLY restaurant.maitrehotel ALTER COLUMN id SET DEFAULT nextval('restaurant.maitrehotel_id_seq'::regclass);


--
-- Name: personne id; Type: DEFAULT; Schema: restaurant; Owner: restaurant_user
--

ALTER TABLE ONLY restaurant.personne ALTER COLUMN id SET DEFAULT nextval('restaurant.personne_id_seq'::regclass);


--
-- Name: plat id; Type: DEFAULT; Schema: restaurant; Owner: restaurant_user
--

ALTER TABLE ONLY restaurant.plat ALTER COLUMN id SET DEFAULT nextval('restaurant.plat_id_seq'::regclass);


--
-- Name: recette id; Type: DEFAULT; Schema: restaurant; Owner: restaurant_user
--

ALTER TABLE ONLY restaurant.recette ALTER COLUMN id SET DEFAULT nextval('restaurant.recette_id_seq'::regclass);


--
-- Name: reservation id; Type: DEFAULT; Schema: restaurant; Owner: restaurant_user
--

ALTER TABLE ONLY restaurant.reservation ALTER COLUMN id SET DEFAULT nextval('restaurant.reservation_id_seq'::regclass);


--
-- Name: restaurant id; Type: DEFAULT; Schema: restaurant; Owner: restaurant_user
--

ALTER TABLE ONLY restaurant.restaurant ALTER COLUMN id SET DEFAULT nextval('restaurant.restaurant_id_seq'::regclass);


--
-- Name: serveur id; Type: DEFAULT; Schema: restaurant; Owner: restaurant_user
--

ALTER TABLE ONLY restaurant.serveur ALTER COLUMN id SET DEFAULT nextval('restaurant.serveur_id_seq'::regclass);


--
-- Name: tables id; Type: DEFAULT; Schema: restaurant; Owner: restaurant_user
--

ALTER TABLE ONLY restaurant.tables ALTER COLUMN id SET DEFAULT nextval('restaurant.tables_id_seq'::regclass);



--
-- Name: affectation_id_seq; Type: SEQUENCE SET; Schema: restaurant; Owner: restaurant_user
--

SELECT pg_catalog.setval('restaurant.affectation_id_seq', 1, false);


--
-- Name: assistant_id_seq; Type: SEQUENCE SET; Schema: restaurant; Owner: restaurant_user
--

SELECT pg_catalog.setval('restaurant.assistant_id_seq', 1, false);


--
-- Name: commande_id_seq; Type: SEQUENCE SET; Schema: restaurant; Owner: restaurant_user
--

SELECT pg_catalog.setval('restaurant.commande_id_seq', 1, false);


--
-- Name: cuisinier_id_seq; Type: SEQUENCE SET; Schema: restaurant; Owner: restaurant_user
--

SELECT pg_catalog.setval('restaurant.cuisinier_id_seq', 1, false);


--
-- Name: directeur_id_seq; Type: SEQUENCE SET; Schema: restaurant; Owner: restaurant_user
--

SELECT pg_catalog.setval('restaurant.directeur_id_seq', 1, true);


--
-- Name: etage_id_seq; Type: SEQUENCE SET; Schema: restaurant; Owner: restaurant_user
--

SELECT pg_catalog.setval('restaurant.etage_id_seq', 1, false);


--
-- Name: ingredient_id_seq; Type: SEQUENCE SET; Schema: restaurant; Owner: restaurant_user
--

SELECT pg_catalog.setval('restaurant.ingredient_id_seq', 2, true);


--
-- Name: maitrehotel_id_seq; Type: SEQUENCE SET; Schema: restaurant; Owner: restaurant_user
--

SELECT pg_catalog.setval('restaurant.maitrehotel_id_seq', 1, false);


--
-- Name: personne_id_seq; Type: SEQUENCE SET; Schema: restaurant; Owner: restaurant_user
--

SELECT pg_catalog.setval('restaurant.personne_id_seq', 3, true);


--
-- Name: plat_id_seq; Type: SEQUENCE SET; Schema: restaurant; Owner: restaurant_user
--

SELECT pg_catalog.setval('restaurant.plat_id_seq', 1, false);


--
-- Name: recette_id_seq; Type: SEQUENCE SET; Schema: restaurant; Owner: restaurant_user
--

SELECT pg_catalog.setval('restaurant.recette_id_seq', 1, false);


--
-- Name: reservation_id_seq; Type: SEQUENCE SET; Schema: restaurant; Owner: restaurant_user
--

SELECT pg_catalog.setval('restaurant.reservation_id_seq', 1, false);


--
-- Name: restaurant_id_seq; Type: SEQUENCE SET; Schema: restaurant; Owner: restaurant_user
--

SELECT pg_catalog.setval('restaurant.restaurant_id_seq', 1, true);


--
-- Name: serveur_id_seq; Type: SEQUENCE SET; Schema: restaurant; Owner: restaurant_user
--

SELECT pg_catalog.setval('restaurant.serveur_id_seq', 2, true);


--
-- Name: tables_id_seq; Type: SEQUENCE SET; Schema: restaurant; Owner: restaurant_user
--

SELECT pg_catalog.setval('restaurant.tables_id_seq', 1, false);


--
-- Name: affectation affectation_pkey; Type: CONSTRAINT; Schema: restaurant; Owner: restaurant_user
--

ALTER TABLE ONLY restaurant.affectation
    ADD CONSTRAINT affectation_pkey PRIMARY KEY (id);


--
-- Name: assistant assistant_pkey; Type: CONSTRAINT; Schema: restaurant; Owner: restaurant_user
--

ALTER TABLE ONLY restaurant.assistant
    ADD CONSTRAINT assistant_pkey PRIMARY KEY (id);


--
-- Name: commande commande_pkey; Type: CONSTRAINT; Schema: restaurant; Owner: restaurant_user
--

ALTER TABLE ONLY restaurant.commande
    ADD CONSTRAINT commande_pkey PRIMARY KEY (id);


--
-- Name: cuisinier cuisinier_pkey; Type: CONSTRAINT; Schema: restaurant; Owner: restaurant_user
--

ALTER TABLE ONLY restaurant.cuisinier
    ADD CONSTRAINT cuisinier_pkey PRIMARY KEY (id);


--
-- Name: directeur directeur_pkey; Type: CONSTRAINT; Schema: restaurant; Owner: restaurant_user
--

ALTER TABLE ONLY restaurant.directeur
    ADD CONSTRAINT directeur_pkey PRIMARY KEY (id);


--
-- Name: etage etage_pkey; Type: CONSTRAINT; Schema: restaurant; Owner: restaurant_user
--

ALTER TABLE ONLY restaurant.etage
    ADD CONSTRAINT etage_pkey PRIMARY KEY (id);


--
-- Name: ingredient ingredient_pkey; Type: CONSTRAINT; Schema: restaurant; Owner: restaurant_user
--

ALTER TABLE ONLY restaurant.ingredient
    ADD CONSTRAINT ingredient_pkey PRIMARY KEY (id);


--
-- Name: maitrehotel maitrehotel_pkey; Type: CONSTRAINT; Schema: restaurant; Owner: restaurant_user
--

ALTER TABLE ONLY restaurant.maitrehotel
    ADD CONSTRAINT maitrehotel_pkey PRIMARY KEY (id);


--
-- Name: personne personne_pkey; Type: CONSTRAINT; Schema: restaurant; Owner: restaurant_user
--

ALTER TABLE ONLY restaurant.personne
    ADD CONSTRAINT personne_pkey PRIMARY KEY (id);


--
-- Name: plat plat_pkey; Type: CONSTRAINT; Schema: restaurant; Owner: restaurant_user
--

ALTER TABLE ONLY restaurant.plat
    ADD CONSTRAINT plat_pkey PRIMARY KEY (id);


--
-- Name: recette recette_pkey; Type: CONSTRAINT; Schema: restaurant; Owner: restaurant_user
--

ALTER TABLE ONLY restaurant.recette
    ADD CONSTRAINT recette_pkey PRIMARY KEY (id);


--
-- Name: reservation reservation_pkey; Type: CONSTRAINT; Schema: restaurant; Owner: restaurant_user
--

ALTER TABLE ONLY restaurant.reservation
    ADD CONSTRAINT reservation_pkey PRIMARY KEY (id);


--
-- Name: restaurant restaurant_pkey; Type: CONSTRAINT; Schema: restaurant; Owner: restaurant_user
--

ALTER TABLE ONLY restaurant.restaurant
    ADD CONSTRAINT restaurant_pkey PRIMARY KEY (id);


--
-- Name: serveur serveur_pkey; Type: CONSTRAINT; Schema: restaurant; Owner: restaurant_user
--

ALTER TABLE ONLY restaurant.serveur
    ADD CONSTRAINT serveur_pkey PRIMARY KEY (id);


--
-- Name: tables tables_pkey; Type: CONSTRAINT; Schema: restaurant; Owner: restaurant_user
--

ALTER TABLE ONLY restaurant.tables
    ADD CONSTRAINT tables_pkey PRIMARY KEY (id);



--
-- Name: assistant assistant_personne_fkey; Type: FK CONSTRAINT; Schema: restaurant; Owner: restaurant_user
--

ALTER TABLE ONLY restaurant.assistant
    ADD CONSTRAINT assistant_personne_fkey FOREIGN KEY (personne) REFERENCES restaurant.personne(id);


--
-- Name: commande commande_affectation_fkey; Type: FK CONSTRAINT; Schema: restaurant; Owner: restaurant_user
--

ALTER TABLE ONLY restaurant.commande
    ADD CONSTRAINT commande_affectation_fkey FOREIGN KEY (affectation) REFERENCES restaurant.affectation(id);


--
-- Name: commande commande_plat_fkey; Type: FK CONSTRAINT; Schema: restaurant; Owner: restaurant_user
--

ALTER TABLE ONLY restaurant.commande
    ADD CONSTRAINT commande_plat_fkey FOREIGN KEY (plat) REFERENCES restaurant.plat(id);


--
-- Name: cuisinier cuisinier_personne_fkey; Type: FK CONSTRAINT; Schema: restaurant; Owner: restaurant_user
--

ALTER TABLE ONLY restaurant.cuisinier
    ADD CONSTRAINT cuisinier_personne_fkey FOREIGN KEY (personne) REFERENCES restaurant.personne(id);


--
-- Name: directeur directeur_personne_fkey; Type: FK CONSTRAINT; Schema: restaurant; Owner: restaurant_user
--

ALTER TABLE ONLY restaurant.directeur
    ADD CONSTRAINT directeur_personne_fkey FOREIGN KEY (personne) REFERENCES restaurant.personne(id);


--
-- Name: maitrehotel maitrehotel_personne_fkey; Type: FK CONSTRAINT; Schema: restaurant; Owner: restaurant_user
--

ALTER TABLE ONLY restaurant.maitrehotel
    ADD CONSTRAINT maitrehotel_personne_fkey FOREIGN KEY (personne) REFERENCES restaurant.personne(id);


--
-- Name: recette recette_ingredient_fkey; Type: FK CONSTRAINT; Schema: restaurant; Owner: restaurant_user
--

ALTER TABLE ONLY restaurant.recette
    ADD CONSTRAINT recette_ingredient_fkey FOREIGN KEY (ingredient) REFERENCES restaurant.ingredient(id);


--
-- Name: recette recette_plat_fkey; Type: FK CONSTRAINT; Schema: restaurant; Owner: restaurant_user
--

ALTER TABLE ONLY restaurant.recette
    ADD CONSTRAINT recette_plat_fkey FOREIGN KEY (plat) REFERENCES restaurant.plat(id);


--
-- Name: reservation reservation_tablereserve_fkey; Type: FK CONSTRAINT; Schema: restaurant; Owner: restaurant_user
--

ALTER TABLE ONLY restaurant.reservation
    ADD CONSTRAINT reservation_tablereserve_fkey FOREIGN KEY (tablereserve) REFERENCES restaurant.tables(id);


--
-- Name: tables tables_etage_fkey; Type: FK CONSTRAINT; Schema: restaurant; Owner: restaurant_user
--

ALTER TABLE ONLY restaurant.tables
    ADD CONSTRAINT tables_etage_fkey FOREIGN KEY (etage) REFERENCES restaurant.etage(id);


--
-- Name: tables tables_serveur_fkey; Type: FK CONSTRAINT; Schema: restaurant; Owner: restaurant_user
--

ALTER TABLE ONLY restaurant.tables
    ADD CONSTRAINT tables_serveur_fkey FOREIGN KEY (serveur) REFERENCES restaurant.serveur(id);


--
-- PostgreSQL database dump complete
--


TRUNCATE TABLE restaurant.affectation,
restaurant.assistant,
restaurant.commande,
restaurant.cuisinier,
restaurant.directeur,
restaurant.etage,
restaurant.ingredient,
restaurant.maitrehotel,
restaurant.personne,
restaurant.plat,
restaurant.recette,
restaurant.reservation,
restaurant.restaurant,
restaurant.serveur,
restaurant.tables
RESTART IDENTITY;