CREATE TABLE IF NOT EXISTS Restaurant (
	id SERIAL NOT NULL,
	heureOuvertureDejeune TIME NOT NULL,
	heureLimiteDejeune TIME NOT NULL,
	heureOuvertureDiner TIME NOT NULL,
	heureLimiteDiner TIME NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS Personne (
	id SERIAL NOT NULL,
	nom VARCHAR NOT NULL,
	login VARCHAR not null,
	PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS MaitreHotel (
	id SERIAL NOT NULL,
	personne INTEGER NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (personne) REFERENCES Personne (id)
);

CREATE TABLE IF NOT EXISTS Serveur (
	id SERIAL NOT NULL,
	personne INTEGER NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (personne) REFERENCES Personne (id)
);

CREATE TABLE IF NOT EXISTS Assistant (
	id SERIAL NOT NULL,
	personne INTEGER NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (personne) REFERENCES Personne (id)
);

CREATE TABLE IF NOT EXISTS Cuisinier (
	id SERIAL NOT NULL,
	personne INTEGER NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (personne) REFERENCES Personne (id)
);

CREATE TABLE IF NOT EXISTS Directeur (
	id SERIAL NOT NULL,
	personne INTEGER NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (personne) REFERENCES Personne (id)
);
	
CREATE TABLE IF NOT EXISTS Etage (
	id SERIAL NOT NULL,
	PRIMARY KEY (id)
);

CREATE TYPE ETATTABLE AS ENUM ('Libre', 'Sale', 'Occupe', 'Reserve');
 
CREATE TABLE IF NOT EXISTS Tables (
	id SERIAL NOT NULL,
	capacite INTEGER NOT NULL,
	etat ETATTABLE NOT NULL,
	etage INTEGER NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (etage) REFERENCES Etage (id)
);

CREATE TABLE IF NOT EXISTS Reservation (
	id SERIAL NOT NULL,
	dateAppel DATE NOT NULL,
	dateReservation DATE NOT NULL,
	nombrePersonne INTEGER NOT NULL,
	valide BOOLEAN NOT NULL,
	tableReserve INTEGER NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (tableReserve) REFERENCES Tables (id)
);

CREATE TABLE IF NOT EXISTS Affectation (
	id SERIAL NOT NULL,
	dateDebut DATE NOT NULL,
	dateFin DATE,
	nombrePersonne INTEGER NOT NULL,
	tableOccupe INTEGER NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (tableOccupe) REFERENCES Tables (id)
);

CREATE TABLE IF NOT EXISTS Ingredient (
	id SERIAL NOT NULL,
	nom VARCHAR NOT NULL,
	quantite INTEGER NOT NULL DEFAULT 0,
	PRIMARY KEY (id)
);

CREATE TYPE TYPEPLAT AS ENUM ('Entree', 'Plat', 'Dessert');
CREATE TYPE TYPEINGREDIENT AS ENUM ('Vegetarien', 'Viande, Poisson', 'Sucre','Sale');
CREATE TYPE ETATCOMMANDE AS ENUM ('COMMANDEE', 'EN_PREPARATION', 'PRETE', 'SERVIE');

CREATE TABLE IF NOT EXISTS Plat (
	id SERIAL NOT NULL,
	nom VARCHAR NOT NULL,
	typePlat TYPEPLAT NOT NULL,
	typeIngredient TYPEINGREDIENT NOT NULL,
	prix FLOAT NOT NULL,
	disponibleCarte BOOLEAN NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS Recette (
	id SERIAL NOT NULL,
	quantite FLOAT NOT NULL,
	ingredient INTEGER NOT NULL,
	plat INTEGER NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (ingredient) REFERENCES Ingredient (id),
	FOREIGN KEY (plat) REFERENCES Plat (id)
);

CREATE TABLE IF NOT EXISTS Commande (
	id SERIAL NOT NULL,
	dateDemande DATE NOT NULL,
	estEnfant BOOLEAN NOT NULL,
	plat INTEGER NOT NULL,
	affectation INTEGER NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (plat) REFERENCES Plat (id),
	FOREIGN KEY (affectation) REFERENCES Affectation (id)
);
