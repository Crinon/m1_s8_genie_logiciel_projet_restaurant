-- Table: restaurant.ingredient

-- DROP TABLE restaurant.ingredient;

CREATE TABLE restaurant.ingredient
(
    id integer NOT NULL DEFAULT nextval('restaurant.ingredient_id_seq'::regclass),
    nom character varying COLLATE pg_catalog."default" NOT NULL,
    quantite integer NOT NULL DEFAULT 0,
    CONSTRAINT ingredient_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE restaurant.ingredient
    OWNER to restaurant_user;