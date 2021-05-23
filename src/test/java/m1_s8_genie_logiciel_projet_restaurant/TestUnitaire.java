package m1_s8_genie_logiciel_projet_restaurant;

import restaurant.Affectation;
import restaurant.Assistant;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import restaurant.Categorie;
import restaurant.Directeur;
import restaurant.Etage;
import restaurant.Ingredient;
import restaurant.Personne;
import restaurant.Plat;
import restaurant.Reservation;
import restaurant.Restaurant;
import restaurant.Serveur;
import restaurant.Sql;
import restaurant.Table;
import restaurant.Type;

import java.sql.ResultSetMetaData;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import org.junit.jupiter.api.Order;

@DisplayName("Tests du projet restaurant")
public class TestUnitaire {
	public final String propertiesFilename = "database.properties";
	private static Sql sql;
	Directeur directeur = new Directeur(0, "directeur", "directeur0");

//  @Test(expected=IllegalArgumentException.class)
//  public void testIsMotDePasseValideNull() {
//      SecuriteHelper.isMotDePasseValide(null);
//  }

	@BeforeAll
	public static void initialisation() throws SQLException, ClassNotFoundException, IOException {
		sql = new Sql();
		System.out.println("\nBEFORE : création BDD");
		sql.executerTests("DROP ALL OBJECTS");
		sql.executerTests("CREATE SCHEMA IF NOT EXISTS restaurant");
		sql.executerTests("create user if not exists restaurant_user password '' admin");

		// Ingrédients
		sql.executerTests("CREATE SEQUENCE restaurant.ingredient_id_seq\r\n" + "    INCREMENT 1\r\n" + "    START 1\r\n"
				+ "    MINVALUE 1\r\n" + "    MAXVALUE 2147483647\r\n" + "    CACHE 1;");
		sql.executerTests("CREATE TABLE restaurant.ingredient\r\n" + "(\r\n"
				+ "    id integer NOT NULL DEFAULT nextval('restaurant.ingredient_id_seq'),\r\n"
				+ "    nom character varying NOT NULL,\r\n" + "    quantite integer NOT NULL DEFAULT 0,\r\n"
				+ "    CONSTRAINT ingredient_pkey PRIMARY KEY (id)\r\n" + ");");
		// Etages
		sql.executerTests("CREATE SEQUENCE restaurant.etage_id_seq\r\n" + "    INCREMENT 1\r\n" + "    START 1\r\n"
				+ "    MINVALUE 1\r\n" + "    MAXVALUE 2147483647\r\n" + "    CACHE 1;");
		sql.executerTests("CREATE TABLE restaurant.etage\r\n" + "(\r\n"
				+ "    id integer NOT NULL DEFAULT nextval('restaurant.etage_id_seq'),\r\n"
				+ "    niveau integer NOT NULL,\r\n" + "    CONSTRAINT etage_pkey PRIMARY KEY (id)\r\n" + ");");
		// Personne
		sql.executerTests("CREATE SEQUENCE restaurant.personne_id_seq\r\n" + "    INCREMENT 1\r\n" + "    START 1\r\n"
				+ "    MINVALUE 1\r\n" + "    MAXVALUE 2147483647\r\n" + "    CACHE 1;");
		sql.executerTests("CREATE TABLE restaurant.personne\r\n" + "(\r\n"
				+ "    id integer NOT NULL DEFAULT nextval('restaurant.personne_id_seq'),\r\n"
				+ "    nom character varying  NOT NULL,\r\n" + "    login character varying  NOT NULL,\r\n"
				+ "    CONSTRAINT personne_pkey PRIMARY KEY (id)\r\n" + ")");
		// Directeur
		sql.executerTests("CREATE SEQUENCE restaurant.directeur_id_seq\r\n" + "    INCREMENT 1\r\n" + "    START 1\r\n"
				+ "    MINVALUE 1\r\n" + "    MAXVALUE 2147483647\r\n" + "    CACHE 1;");
		sql.executerTests("CREATE TABLE restaurant.directeur\r\n" + "(\r\n"
				+ "    id integer NOT NULL DEFAULT nextval('restaurant.directeur_id_seq'),\r\n"
				+ "    personne integer NOT NULL,\r\n" + "    CONSTRAINT directeur_pkey PRIMARY KEY (id),\r\n"
				+ "    CONSTRAINT directeur_personne_fkey FOREIGN KEY (personne)\r\n"
				+ "        REFERENCES restaurant.personne (id) \r\n" + ")");
		// Cuisinier
		sql.executerTests("CREATE SEQUENCE restaurant.cuisinier_id_seq\r\n" + "    INCREMENT 1\r\n" + "    START 1\r\n"
				+ "    MINVALUE 1\r\n" + "    MAXVALUE 2147483647\r\n" + "    CACHE 1;");
		sql.executerTests("CREATE TABLE restaurant.cuisinier\r\n" + "(\r\n"
				+ "    id integer NOT NULL DEFAULT nextval('restaurant.cuisinier_id_seq'),\r\n"
				+ "    personne integer NOT NULL,\r\n" + "    CONSTRAINT cuisinier_pkey PRIMARY KEY (id),\r\n"
				+ "    CONSTRAINT cuisinier_personne_fkey FOREIGN KEY (personne)\r\n"
				+ "        REFERENCES restaurant.personne (id) \r\n" + "        ON UPDATE NO ACTION\r\n"
				+ "        ON DELETE NO ACTION\r\n" + ")");
		// Assistant
		sql.executerTests("CREATE SEQUENCE restaurant.assistant_id_seq\r\n" + "    INCREMENT 1\r\n" + "    START 1\r\n"
				+ "    MINVALUE 1\r\n" + "    MAXVALUE 2147483647\r\n" + "    CACHE 1;");
		sql.executerTests("CREATE TABLE restaurant.assistant\r\n" + "(\r\n"
				+ "    id integer NOT NULL DEFAULT nextval('restaurant.assistant_id_seq'),\r\n"
				+ "    personne integer NOT NULL,\r\n" + "    CONSTRAINT assistant_pkey PRIMARY KEY (id),\r\n"
				+ "    CONSTRAINT assistant_personne_fkey FOREIGN KEY (personne)\r\n"
				+ "        REFERENCES restaurant.personne (id) \r\n" + "        ON UPDATE NO ACTION\r\n"
				+ "        ON DELETE NO ACTION\r\n" + ")");
		// Serveur
		sql.executerTests("CREATE SEQUENCE restaurant.serveur_id_seq\r\n" + "    INCREMENT 1\r\n" + "    START 1\r\n"
				+ "    MINVALUE 1\r\n" + "    MAXVALUE 2147483647\r\n" + "    CACHE 1;");
		sql.executerTests("CREATE TABLE restaurant.serveur\r\n" + "(\r\n"
				+ "    id integer NOT NULL DEFAULT nextval('restaurant.serveur_id_seq'),\r\n"
				+ "    personne integer NOT NULL,\r\n" + "    CONSTRAINT serveur_pkey PRIMARY KEY (id)\r\n" + ")");
		// Maitre d'hôtel
		sql.executerTests("CREATE SEQUENCE restaurant.maitrehotel_id_seq\r\n" + "    INCREMENT 1\r\n"
				+ "    START 1\r\n" + "    MINVALUE 1\r\n" + "    MAXVALUE 2147483647\r\n" + "    CACHE 1;");
		sql.executerTests("CREATE TABLE restaurant.maitrehotel\r\n" + "(\r\n"
				+ "    id integer NOT NULL DEFAULT nextval('restaurant.maitrehotel_id_seq'),\r\n"
				+ "    personne integer NOT NULL,\r\n" + "    CONSTRAINT maitrehotel_pkey PRIMARY KEY (id),\r\n"
				+ "    CONSTRAINT maitrehotel_personne_fkey FOREIGN KEY (personne)\r\n"
				+ "        REFERENCES restaurant.personne (id) \r\n" + "        ON UPDATE NO ACTION\r\n"
				+ "        ON DELETE NO ACTION\r\n" + ")");
		// Tables
		sql.executerTests("CREATE SEQUENCE restaurant.tables_id_seq\r\n" + "    INCREMENT 1\r\n" + "    START 1\r\n"
				+ "    MINVALUE 1\r\n" + "    MAXVALUE 2147483647\r\n" + "    CACHE 1;");
		sql.executerTests("CREATE TABLE restaurant.tables\r\n" + "(\r\n"
				+ "    id integer NOT NULL DEFAULT nextval('restaurant.tables_id_seq'),\r\n"
				+ "    capacite integer NOT NULL,\r\n" + "    etat character varying NOT NULL,\r\n"
				+ "    etage integer NOT NULL,\r\n" + "    numero integer NOT NULL,\r\n"
				+ "    check (etat in ('Libre', 'Sale', 'Occupe', 'Reserve')),\r\n"
				+ "    CONSTRAINT tables_pkey PRIMARY KEY (id),\r\n"
				+ "    CONSTRAINT tables_etage_fkey FOREIGN KEY (etage)\r\n"
				+ "        REFERENCES restaurant.etage (id)\r\n" + "        ON UPDATE NO ACTION\r\n"
				+ "        ON DELETE NO ACTION\r\n" + ")");
		// Plat
		sql.executerTests("CREATE SEQUENCE restaurant.plat_id_seq\r\n" + "    INCREMENT 1\r\n" + "    START 1\r\n"
				+ "    MINVALUE 1\r\n" + "    MAXVALUE 2147483647\r\n" + "    CACHE 1;");
		sql.executerTests("CREATE TABLE restaurant.plat\r\n" + "(\r\n"
				+ "    id integer NOT NULL DEFAULT nextval('restaurant.plat_id_seq'),\r\n"
				+ "    nom character varying NOT NULL,\r\n" + "    typeplat character varying NOT NULL,\r\n"
				+ "    typeingredient character varying NOT NULL,\r\n" + "    prix double precision NOT NULL,\r\n"
				+ "    disponiblecarte boolean NOT NULL,\r\n" + "    dureepreparation integer NOT NULL,\r\n"
				+ "    CONSTRAINT plat_pkey PRIMARY KEY (id),\r\n"
				+ "    check (typeplat in ('Entree', 'Plat', 'Dessert')),\r\n"
				+ "    check (typeingredient in ('Vegetarien', 'Viande', 'Poisson', 'Sucre', 'Sale'))\r\n" + ")");
		// Recette de plat
		sql.executerTests("CREATE SEQUENCE restaurant.recette_id_seq\r\n" + "    INCREMENT 1\r\n" + "    START 1\r\n"
				+ "    MINVALUE 1\r\n" + "    MAXVALUE 2147483647\r\n" + "    CACHE 1;");
		sql.executerTests("CREATE TABLE restaurant.recette\r\n" + "(\r\n"
				+ "    id integer NOT NULL DEFAULT nextval('restaurant.recette_id_seq'),\r\n"
				+ "    quantite double precision NOT NULL,\r\n" + "    ingredient integer NOT NULL,\r\n"
				+ "    plat integer NOT NULL,\r\n" + "    CONSTRAINT recette_pkey PRIMARY KEY (id),\r\n"
				+ "    CONSTRAINT recette_ingredient_fkey FOREIGN KEY (ingredient)\r\n"
				+ "        REFERENCES restaurant.ingredient (id)\r\n" + "        ON UPDATE NO ACTION\r\n"
				+ "        ON DELETE NO ACTION,\r\n" + "    CONSTRAINT recette_plat_fkey FOREIGN KEY (plat)\r\n"
				+ "        REFERENCES restaurant.plat (id)\r\n" + ")");
		// Affectation
		sql.executerTests("CREATE SEQUENCE restaurant.affectation_id_seq\r\n" + "    INCREMENT 1\r\n"
				+ "    START 1\r\n" + "    MINVALUE 1\r\n" + "    MAXVALUE 2147483647\r\n" + "    CACHE 1;");
		sql.executerTests("CREATE TABLE restaurant.affectation\r\n" + "(\r\n"
				+ "    id integer NOT NULL DEFAULT nextval('restaurant.affectation_id_seq'),\r\n"
				+ "    datedebut TIMESTAMP NOT NULL,\r\n" + "    datefin TIMESTAMP,\r\n"
				+ "    nombrepersonne integer NOT NULL,\r\n" + "    tableoccupe integer NOT NULL,\r\n"
				+ "    CONSTRAINT affectation_pkey PRIMARY KEY (id),\r\n"
				+ "    CONSTRAINT affectation_tableoccupe_fkey FOREIGN KEY (tableoccupe)\r\n"
				+ "        REFERENCES restaurant.tables (id)\r\n" + ")");
		// Reservation
		sql.executerTests("CREATE SEQUENCE restaurant.reservation_id_seq\r\n" + "    INCREMENT 1\r\n"
				+ "    START 1\r\n" + "    MINVALUE 1\r\n" + "    MAXVALUE 2147483647\r\n" + "    CACHE 1;");
		sql.executerTests("CREATE TABLE restaurant.reservation\r\n" + "(\r\n"
				+ "    id integer NOT NULL DEFAULT nextval('restaurant.reservation_id_seq'),\r\n"
				+ "    dateappel TIMESTAMP NOT NULL,\r\n" + "    datereservation TIMESTAMP NOT NULL,\r\n"
				+ "    nombrepersonne integer NOT NULL,\r\n" + "    valide boolean NOT NULL,\r\n"
				+ "    tablereserve integer NOT NULL,\r\n" + "    CONSTRAINT reservation_pkey PRIMARY KEY (id),\r\n"
				+ "    CONSTRAINT reservation_tablereserve_fkey FOREIGN KEY (tablereserve)\r\n"
				+ "        REFERENCES restaurant.tables (id)\r\n" + ")");
		// Restaurant
		sql.executerTests("CREATE SEQUENCE restaurant.restaurant_id_seq\r\n" + "    INCREMENT 1\r\n" + "    START 1\r\n"
				+ "    MINVALUE 1\r\n" + "    MAXVALUE 2147483647\r\n" + "    CACHE 1;");
		sql.executerTests("CREATE TABLE restaurant.restaurant\r\n" + "(\r\n"
				+ "    id integer NOT NULL DEFAULT nextval('restaurant.restaurant_id_seq'),\r\n"
				+ "    heurelimitediner integer NOT NULL,\r\n" + "    heureouverturediner integer NOT NULL,\r\n"
				+ "    heurelimitedejeune integer NOT NULL,\r\n" + "    heureouverturedejeune integer NOT NULL,\r\n"
				+ "    CONSTRAINT restaurant_pkey PRIMARY KEY (id)\r\n" + ")");
		Restaurant.initialisation();
	}

	@Test
	@DisplayName("Ajout automatique d'un directeur dans la base de données s'il n'en existe aucun")
	public void insertionDirecteurPremierDemarrage() {
		System.out.println("\nTest en cours : création du directeur lorsqu'il n'y en a aucun dans la base de données");
		ResultSet resultSet = sql.executerSelect("SELECT login from restaurant.personne where login = 'directeur0'");
		try {
			resultSet.next();
			// Le test vient de l'initialisation de la classe statique Restaurant
			assertEquals("directeur0", resultSet.getString("login"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Création d'un ingrédient dans la base de donnée")
	public void insertionIngredientDB() throws SQLException, ClassNotFoundException, IOException {
		directeur.ajouterIngredient("carotte", Restaurant.getIngredients());
		ResultSet res = sql.executerSelect("select * from restaurant.ingredient where nom = 'carotte'");
		assertTrue(res.next());
	}

	@Test
	@DisplayName("Création d'un ingrédient dans la mémoire")
	public void insertionIngredientJava() throws SQLException, ClassNotFoundException, IOException {
		directeur.ajouterIngredient("patate", Restaurant.getIngredients());
		assertEquals("patate", Restaurant.getIngredients().get(Restaurant.getIngredients().size() - 1).getNom());
	}

	@Test
	@DisplayName("Tentative de créer 2 fois le même ingrédient dans la base de données")
	public void refuseDoublonIngredientDB() throws ClassNotFoundException, SQLException, IOException {
		System.out.println("\nTest en cours : doublon d'ingrédient");
		directeur.ajouterIngredient("tomate", Restaurant.getIngredients());
		directeur.ajouterIngredient("tomate", Restaurant.getIngredients());
		ResultSet resultSet = sql
				.executerSelect("SELECT count(*) as count from restaurant.ingredient where nom = 'tomate'");
		resultSet.next();
		// Il ne doit y avoir qu'un seul ingrédient avec le nom tomate
		assertEquals("ERREUR : un ingrédient avec le même nom est présent 2 fois en base.", 1,
				Integer.parseInt(resultSet.getString("count")));
	}

	@Test
	@DisplayName("Commander un ingrédient : modification de la quantité dans la base de données")
	public void commanderIngredientDB() throws ClassNotFoundException, SQLException, IOException {
		System.out.println("\nTest en cours : commande d'ingrédient");
		directeur.commanderIngredient(Restaurant.getIngredients().get(0), 10);
		ResultSet resultSet = sql.executerSelect("SELECT nom,quantite from restaurant.ingredient where id = "
				+ Restaurant.getIngredients().get(0).getId());
		resultSet.next();
		assertEquals(10, Integer.parseInt(resultSet.getString("quantite")));
	}

	@Test
	@DisplayName("Insertion d'un personnel assistant dans la base de données")
	public void insertionPersonnelDB() {
		System.out.println("\nTest en cours : Insertion d'un assistant dans la base de données");
		directeur.ajouterPersonnel("Julien", "assistant", Restaurant.getPersonnel());
		ResultSet resultSet = sql.executerSelect("SELECT login from restaurant.personne where login = '"
				+ Restaurant.getPersonnel().get(Restaurant.getPersonnel().size() - 1).getIdentifiant() + "'");
		try {
			assertTrue(resultSet.next());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Modification d'un personnel (assistant vers serveur) dans la base de données")
	public void modificationPersonnelDB() {
		try {
			System.out.println(
					"\nTest en cours : Modification d'un personnel (assistant vers serveur) dans la base de données");
			Personne assistant = directeur.ajouterPersonnel("Hervé", "assistant", Restaurant.getPersonnel());
			Personne herve = directeur.modifierPersonnel(assistant, "serveur");
			ResultSet resultSet = sql
					.executerSelect("SELECT id from restaurant.serveur where personne = " + herve.getId());
			assertTrue(resultSet.next());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Suppression d'un personnel dans la base de données")
	public void suppressionPersonnelDB() {
		try {
			directeur.ajouterPersonnel("NicolasSupprimer", "cuisinier", Restaurant.getPersonnel());

			System.out.println("\nTest en cours : Suppression d'un personnel dans la base de données");
			Personne personne = Restaurant.getPersonnel().get(Restaurant.getPersonnel().size() - 1);
			directeur.supprimerPersonnel(personne, Restaurant.getPersonnel());
			ResultSet resultSet = sql.executerSelect(
					"SELECT login from restaurant.personne where login = '" + personne.getIdentifiant() + "'");
			// Initialisation du curseur
			resultSet.next();
			// Il ne doit exister aucune ligne dans le resultSet
			assertFalse(resultSet.next());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Insertion d'un personnel dans la mémoire")
	public void insertionPersonnelJava() {
		System.out.println("\nTest en cours : Insertion d'un personnel dans la mémoire");
		Personne newPersonne = directeur.ajouterPersonnel("Julien", "assistant", Restaurant.getPersonnel());
		assertEquals(newPersonne, Restaurant.getPersonnel().get(Restaurant.getPersonnel().size() - 1));
	}

	@Test
	@DisplayName("Modification d'un personnel (assistant vers serveur) dans la dans la mémoire")
	public void modificationPersonnelJava() {
		System.out.println(
				"\nTest en cours : Modification d'un personnel (assistant vers serveur) dans la base de données");
		Personne assistant = Restaurant.getPersonnel().get(Restaurant.getPersonnel().size() - 1);
		Personne serveur = directeur.modifierPersonnel(assistant, "serveur");
		System.out.println(serveur.getClass());
		assertTrue(serveur instanceof Serveur);
	}

	@Test
	@DisplayName("Suppression d'un personnel dans la mémoire")
	public void suppressionPersonnelJava() {
		System.out.println("\nTest en cours : Suppression d'un personnel dans la mémoire");
		Personne personne = directeur.ajouterPersonnel("Julien", "assistant", Restaurant.getPersonnel());
		directeur.supprimerPersonnel(personne, Restaurant.getPersonnel());
		assertEquals(-1, Restaurant.getPersonnel().indexOf(personne));
	}

	@Test
	@DisplayName("Ajout d'un étage dans la base de données")
	public void ajouterEtageDB() {
		try {
			System.out.println("\nTest en cours : Ajout d'un étage dans la base de données");
			// On regarde les étages déjà existants
			ResultSet resultSet = sql.executerSelect("SELECT MAX(niveau) as max FROM restaurant.etage");
			// Le rez-de-chaussé est 0
			int niveauMaxAvantTest = -1;
			if (resultSet.next()) {
				if (resultSet.getString("max") != null) {
					niveauMaxAvantTest = Integer.parseInt(resultSet.getString("max"));
				}
			}
			directeur.ajouterEtage();
			int niveauMaxApresTest;
			resultSet = sql.executerSelect("SELECT MAX(niveau) as max FROM restaurant.etage");
			resultSet.next();
			niveauMaxApresTest = Integer.parseInt(resultSet.getString("max"));
			// On vérifie que le nouvel étage et plus haut que l'ancien
			assertTrue(niveauMaxAvantTest < niveauMaxApresTest);
		} catch (NumberFormatException | SQLException | ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Ajout d'un étage dans la mémoire")
	public void ajouterEtageJava() {
		System.out.println("\nTest en cours : Ajout d'un étage dans la base de données");
		try {
			// On regarde les étages déjà existants
			int nbEtageAvant = Restaurant.getEtages().size();
			// On vérifie que le nouvel étage et plus haut que l'ancien
			directeur.ajouterEtage();
			int nbEtageApres = Restaurant.getEtages().size();
			assertTrue("Aucun étage ajouté dans l'arraylist d'étages", nbEtageAvant < nbEtageApres);
		} catch (ClassNotFoundException | SQLException | IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Suppression d'un étage dans la base de données")
	public void supprimerEtageDB() {
		try {
			System.out.println("\nTest en cours : Suppression d'un étage dans la base de données");
			// On ajoute plusieurs étages pour éviter le cas du RDC, moins simple à
			// comprendre
			directeur.ajouterEtage();
			directeur.ajouterEtage();
			ResultSet resultSet = sql.executerSelect("SELECT MAX(niveau) as max FROM restaurant.etage");
			resultSet.next();
			int niveauMaxAvantSuppression = Integer.parseInt(resultSet.getString("max"));
			directeur.supprimerDernierEtage();
			resultSet = sql.executerSelect("SELECT MAX(niveau) as max FROM restaurant.etage");
			int niveauMaxApresSuppression = -1;
			if (resultSet.next()) {
				if (resultSet.getString("max") != null) {
					niveauMaxApresSuppression = Integer.parseInt(resultSet.getString("max"));
				}
			}
			// On vérifie qu'il y a moins d'étage qu'avant la suppression
			assertTrue(niveauMaxApresSuppression < niveauMaxAvantSuppression);
		} catch (NumberFormatException | SQLException | ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Suppression d'un étage dans la mémoire")

	public void supprimerEtageJava() {
		System.out.println("\nTest en cours : Suppression d'un étage dans la mémoire");
		try {
			directeur.ajouterEtage();
			directeur.ajouterEtage();
			// On regarde les étages déjà existants
			int nbEtageAvantSuppression = Restaurant.getEtages().size();
			// On vérifie que le nouvel étage et plus haut que l'ancien
			directeur.supprimerDernierEtage();
			int nbEtageApresSuppression = Restaurant.getEtages().size();
			assertTrue("Aucun étage ajouté dans l'arraylist d'étages",
					nbEtageApresSuppression < nbEtageAvantSuppression);
		} catch (ClassNotFoundException | SQLException | IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Ajout d'une table à un étage dans la base de données")
	public void ajouterTableDB() {
		System.out.println("\nTest en cours : Ajout d'une table à un étage dans la base de données");
		try {
			int numero = 1;
			directeur.ajouterEtage();
			Etage etage = Restaurant.getEtages().get(Restaurant.getEtages().size() - 1);
			directeur.ajouterTable(numero, 10, etage);
			;
			ResultSet res = sql.executerSelect(
					"SELECT * FROM restaurant.tables WHERE numero=" + numero + " AND etage=" + etage.getId());
			// On vérifie que la ligne a été trouvé
			assertTrue(res.next());
		} catch (ClassNotFoundException | SQLException | IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Ajout d'une table à un étage dans la mémoire")
	public void ajouterTableJava() {
		System.out.println("\nTest en cours : Ajout d'une table à un étage dans la mémoire");
		try {
			directeur.ajouterEtage();
			int numero = 2;
			Etage etage = Restaurant.getEtages().get(Restaurant.getEtages().size() - 1);
			int nbTableAvant = etage.getTables().size();
			directeur.ajouterTable(numero, 10, etage);
			int nbTableApres = etage.getTables().size();
			assertTrue(nbTableAvant < nbTableApres);
		} catch (ClassNotFoundException | SQLException | IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Modificiation du numéro d'une table dans la base de donées")
	public void modifierNumeroTableDB() {
		System.out.println("\nTest en cours : Modificiation du numéro d'une table dans la base de donées");
		try {
			int numeroAvant = 3;
			int numeroApres = 4;
			int numeroTrouve = -1;
			directeur.ajouterEtage();
			Etage etage = Restaurant.getEtages().get(Restaurant.getEtages().size() - 1);
			directeur.ajouterTable(numeroAvant, 10, etage);
			Table tableActuelle = etage.getTables().get(0);
			directeur.modifierNumeroTable(tableActuelle, numeroApres);
			ResultSet resultSet = sql
					.executerSelect("SELECT numero FROM restaurant.tables WHERE id=" + tableActuelle.getId());
			if (resultSet.next()) {
				numeroTrouve = Integer.parseInt(resultSet.getString("numero"));
			}
			// On vérifie que la ligne a été trouvé
			assertEquals("Le numéro de table n'a pas été mis à jour", numeroApres, numeroTrouve);
		} catch (ClassNotFoundException | SQLException | IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Modificiation du numéro d'une table dans la mémoire")
	public void modifierNumeroTableJava() {
		System.out.println("\nTest en cours : Modificiation du numéro d'une table dans la mémoire");
		try {
			int numeroAvant = 3;
			int numeroApres = 4;
			directeur.ajouterEtage();
			Etage etage = Restaurant.getEtages().get(Restaurant.getEtages().size() - 1);
			directeur.ajouterTable(numeroAvant, 10, etage);
			Table tableActuelle = etage.getTables().get(0);
			directeur.modifierNumeroTable(tableActuelle, numeroApres);
			assertEquals("Le numéro de table n'a pas été mis à jour", numeroApres,
					etage.getTables().get(0).getNumero());
		} catch (ClassNotFoundException | SQLException | IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Suppression d'une table dans la base de donées")
	public void supprimerTableDB() {
		System.out.println("\nTest en cours : Suppression d'une table dans la base de donées");
		try {
			// Numéro de la table que l'on créé puis supprime pour le test
			int numeroTable = 5;
			directeur.ajouterEtage();
			// Etage qui va recevoir une table pour être supprimée
			Etage etage = Restaurant.getEtages().get(Restaurant.getEtages().size() - 1);
			// On ajoute la table
			directeur.ajouterTable(numeroTable, 10, etage);
			// La table créée et a supprimée
			Table tableActuelle = etage.getTables().get(0);
			// On supprime la table
			directeur.supprimerTable(tableActuelle, etage.getTables());
			ResultSet resultSet = sql
					.executerSelect("SELECT numero FROM restaurant.tables WHERE id=" + tableActuelle.getId());
			// On vérifie qu'aucune ligne n'est trouvée car l'id recherché a été supprimé
			assertFalse(resultSet.next());
		} catch (ClassNotFoundException | SQLException | IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Suppression d'une table dans la mémoire")
	public void supprimerTableJava() {
		System.out.println("\nTest en cours : Suppression d'une table dans la mémoire");
		try {
			// Numéro de la table que l'on créé puis supprime pour le test
			int numeroTable = 6;
			directeur.ajouterEtage();
			// Etage qui va recevoir une table pour être supprimée
			Etage etage = Restaurant.getEtages().get(Restaurant.getEtages().size() - 1);
			// On ajoute la table
			directeur.ajouterTable(numeroTable, 10, etage);
			int totalTablesAvantSuppression = etage.getTables().size();
			// La table créée et a supprimée
			Table tableActuelle = etage.getTables().get(0);
			// On supprime la table
			directeur.supprimerTable(tableActuelle, etage.getTables());
			int totalTablesApresSuppression = etage.getTables().size();
			// On vérifie qu'aucune ligne n'est trouvée car l'id recherché a été supprimé
			assertTrue(totalTablesApresSuppression < totalTablesAvantSuppression);
		} catch (ClassNotFoundException | SQLException | IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Création d'un plat dans la base de données")
	public void creationPlatDB() {
		try {
			System.out.println("\nTest en cours : Création d'un plat dans la base de données");
			String nomPlat = "Toast au saumon";
			Double prixPlat = 9.5;
			int tempsPrepa = 5;
			boolean surCarte = true;
			Type type = Type.ENTREE;
			Categorie categorie = Categorie.POISSON;
			String nomIngredientSaumon = "saumon";
			String nomIngredientToast = "tartine";
			int quantiteSaumon = 2;
			int quantiteTartine = 2;
			directeur.ajouterIngredient(nomIngredientSaumon, Restaurant.getIngredients());
			Ingredient saumon = Restaurant.getIngredients().get(Restaurant.getIngredients().size() - 1);
			directeur.ajouterIngredient(nomIngredientToast, Restaurant.getIngredients());
			Ingredient tartine = Restaurant.getIngredients().get(Restaurant.getIngredients().size() - 1);
			HashMap<Ingredient, Integer> recette = new HashMap<>();
			recette.put(saumon, quantiteSaumon);
			recette.put(tartine, quantiteTartine);
			Plat plat = directeur.creerPlat(nomPlat, prixPlat, tempsPrepa, surCarte, type, categorie, recette);
			ResultSet resultSet = sql.executerSelect("SELECT id FROM restaurant.plat WHERE id=" + plat.getId());
			// On vérifie qu'une ligne a bien été créé avec l'id du plat généré
			assertTrue(resultSet.next());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Création d'un plat dans la mémoire")
	public void creationPlatJava() {
		System.out.println("\nTest en cours : Création d'un plat dans la mémoire");
		int nbPlatAvant = Restaurant.getPlats().size();
		String nomPlat = "Toast au saumon";
		Double prixPlat = 9.5;
		int tempsPrepa = 5;
		boolean surCarte = true;
		Type type = Type.ENTREE;
		Categorie categorie = Categorie.POISSON;
		String nomIngredientSaumon = "saumon";
		String nomIngredientToast = "tartine";
		int quantiteSaumon = 2;
		int quantiteTartine = 2;
		directeur.ajouterIngredient(nomIngredientSaumon, Restaurant.getIngredients());
		Ingredient saumon = Restaurant.getIngredients().get(Restaurant.getIngredients().size() - 1);
		directeur.ajouterIngredient(nomIngredientToast, Restaurant.getIngredients());
		Ingredient tartine = Restaurant.getIngredients().get(Restaurant.getIngredients().size() - 1);
		HashMap<Ingredient, Integer> recette = new HashMap<>();
		recette.put(saumon, quantiteSaumon);
		recette.put(tartine, quantiteTartine);
		directeur.creerPlat(nomPlat, prixPlat, tempsPrepa, surCarte, type, categorie, recette);
		int nbPlatApres = Restaurant.getPlats().size();
		assertTrue(nbPlatAvant < nbPlatApres);
	}

	@Test
	@DisplayName("Modification du prix d'un plat dans la base de données")
	public void modificationPrixPlatDB() {
		try {
			System.out.println("\nTest en cours : Création d'un plat dans la base de données");
			String nomPlat = "Toast au saumon";
			Double prixPlat = 9.5;
			int tempsPrepa = 5;
			boolean surCarte = true;
			Type type = Type.ENTREE;
			Categorie categorie = Categorie.POISSON;
			String nomIngredientSaumon = "saumon";
			String nomIngredientToast = "tartine";
			int quantiteSaumon = 2;
			int quantiteTartine = 2;
			directeur.ajouterIngredient(nomIngredientSaumon, Restaurant.getIngredients());
			Ingredient saumon = Restaurant.getIngredients().get(Restaurant.getIngredients().size() - 1);
			directeur.ajouterIngredient(nomIngredientToast, Restaurant.getIngredients());
			Ingredient tartine = Restaurant.getIngredients().get(Restaurant.getIngredients().size() - 1);
			HashMap<Ingredient, Integer> recette = new HashMap<>();
			recette.put(saumon, quantiteSaumon);
			recette.put(tartine, quantiteTartine);
			Plat plat = directeur.creerPlat(nomPlat, prixPlat, tempsPrepa, surCarte, type, categorie, recette);
			directeur.modifierPrixPlat(plat, 10);
			ResultSet resultSet = sql.executerSelect("SELECT prix FROM restaurant.plat WHERE id=" + plat.getId());
			resultSet.next();
			// On vérifie qu'une ligne a bien été créé avec l'id du plat généré
			assertEquals(10, resultSet.getDouble("prix"), 0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Modification du prix d'un plat dans la mémoire")
	public void modificationPrixPlatJava() {
		System.out.println("\nTest en cours : Création d'un plat dans la mémoire");
		String nomPlat = "Toast au saumon";
		Double prixPlat = 9.5;
		int tempsPrepa = 5;
		boolean surCarte = true;
		Type type = Type.ENTREE;
		Categorie categorie = Categorie.POISSON;
		String nomIngredientSaumon = "saumon";
		String nomIngredientToast = "tartine";
		int quantiteSaumon = 2;
		int quantiteTartine = 2;
		directeur.ajouterIngredient(nomIngredientSaumon, Restaurant.getIngredients());
		Ingredient saumon = Restaurant.getIngredients().get(Restaurant.getIngredients().size() - 1);
		directeur.ajouterIngredient(nomIngredientToast, Restaurant.getIngredients());
		Ingredient tartine = Restaurant.getIngredients().get(Restaurant.getIngredients().size() - 1);
		HashMap<Ingredient, Integer> recette = new HashMap<>();
		recette.put(saumon, quantiteSaumon);
		recette.put(tartine, quantiteTartine);
		Plat plat = directeur.creerPlat(nomPlat, prixPlat, tempsPrepa, surCarte, type, categorie, recette);
		directeur.modifierPrixPlat(plat, 10);
		assertEquals(10, plat.getPrix(), 0);
	}

	@Test
	@DisplayName("Modification de la disponibilité d'un plat dans la base de données")
	public void modificationCartePlatDB() {
		try {
			System.out.println("\nTest en cours : Création d'un plat dans la base de données");
			String nomPlat = "Toast au saumon";
			Double prixPlat = 9.5;
			int tempsPrepa = 5;
			boolean surCarte = true;
			Type type = Type.ENTREE;
			Categorie categorie = Categorie.POISSON;
			String nomIngredientSaumon = "saumon";
			String nomIngredientToast = "tartine";
			int quantiteSaumon = 2;
			int quantiteTartine = 2;
			directeur.ajouterIngredient(nomIngredientSaumon, Restaurant.getIngredients());
			Ingredient saumon = Restaurant.getIngredients().get(Restaurant.getIngredients().size() - 1);
			directeur.ajouterIngredient(nomIngredientToast, Restaurant.getIngredients());
			Ingredient tartine = Restaurant.getIngredients().get(Restaurant.getIngredients().size() - 1);
			HashMap<Ingredient, Integer> recette = new HashMap<>();
			recette.put(saumon, quantiteSaumon);
			recette.put(tartine, quantiteTartine);
			Plat plat = directeur.creerPlat(nomPlat, prixPlat, tempsPrepa, surCarte, type, categorie, recette);
			directeur.modifierCartePlat(plat, false);
			ResultSet resultSet = sql
					.executerSelect("SELECT disponiblecarte FROM restaurant.plat WHERE id=" + plat.getId());
			resultSet.next();
			// On vérifie qu'une ligne a bien été créé avec l'id du plat généré
			assertFalse(resultSet.getBoolean("disponiblecarte"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Modification de la disponibilité d'un plat dans la mémoire")
	public void modificationCartePlatJava() {
		System.out.println("\nTest en cours : Création d'un plat dans la mémoire");
		String nomPlat = "Toast au saumon";
		Double prixPlat = 9.5;
		int tempsPrepa = 5;
		boolean surCarte = true;
		Type type = Type.ENTREE;
		Categorie categorie = Categorie.POISSON;
		String nomIngredientSaumon = "saumon";
		String nomIngredientToast = "tartine";
		int quantiteSaumon = 2;
		int quantiteTartine = 2;
		directeur.ajouterIngredient(nomIngredientSaumon, Restaurant.getIngredients());
		Ingredient saumon = Restaurant.getIngredients().get(Restaurant.getIngredients().size() - 1);
		directeur.ajouterIngredient(nomIngredientToast, Restaurant.getIngredients());
		Ingredient tartine = Restaurant.getIngredients().get(Restaurant.getIngredients().size() - 1);
		HashMap<Ingredient, Integer> recette = new HashMap<>();
		recette.put(saumon, quantiteSaumon);
		recette.put(tartine, quantiteTartine);
		Plat plat = directeur.creerPlat(nomPlat, prixPlat, tempsPrepa, surCarte, type, categorie, recette);
		directeur.modifierCartePlat(plat, false);
		assertFalse(plat.isDisponibleCarte());
	}

	@Test
	@DisplayName("Modification de la durée d'un plat dans la base de données")
	public void modificationDureePlatDB() {
		try {
			System.out.println("\nTest en cours : Création d'un plat dans la base de données");
			String nomPlat = "Toast au saumon";
			Double prixPlat = 9.5;
			int tempsPrepa = 5;
			boolean surCarte = true;
			Type type = Type.ENTREE;
			Categorie categorie = Categorie.POISSON;
			String nomIngredientSaumon = "saumon";
			String nomIngredientToast = "tartine";
			int quantiteSaumon = 2;
			int quantiteTartine = 2;
			directeur.ajouterIngredient(nomIngredientSaumon, Restaurant.getIngredients());
			Ingredient saumon = Restaurant.getIngredients().get(Restaurant.getIngredients().size() - 1);
			directeur.ajouterIngredient(nomIngredientToast, Restaurant.getIngredients());
			Ingredient tartine = Restaurant.getIngredients().get(Restaurant.getIngredients().size() - 1);
			HashMap<Ingredient, Integer> recette = new HashMap<>();
			recette.put(saumon, quantiteSaumon);
			recette.put(tartine, quantiteTartine);
			Plat plat = directeur.creerPlat(nomPlat, prixPlat, tempsPrepa, surCarte, type, categorie, recette);
			directeur.modifierDureePlat(plat, 10);
			ResultSet resultSet = sql
					.executerSelect("SELECT dureepreparation FROM restaurant.plat WHERE id=" + plat.getId());
			resultSet.next();
			// On vérifie qu'une ligne a bien été créé avec l'id du plat généré
			assertEquals(10, resultSet.getInt("dureepreparation"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Modification de la durée d'un plat dans la mémoire")
	public void modificationDureePlatJava() {
		System.out.println("\nTest en cours : Création d'un plat dans la mémoire");
		String nomPlat = "Toast au saumon";
		Double prixPlat = 9.5;
		int tempsPrepa = 5;
		boolean surCarte = true;
		Type type = Type.ENTREE;
		Categorie categorie = Categorie.POISSON;
		String nomIngredientSaumon = "saumon";
		String nomIngredientToast = "tartine";
		int quantiteSaumon = 2;
		int quantiteTartine = 2;
		directeur.ajouterIngredient(nomIngredientSaumon, Restaurant.getIngredients());
		Ingredient saumon = Restaurant.getIngredients().get(Restaurant.getIngredients().size() - 1);
		directeur.ajouterIngredient(nomIngredientToast, Restaurant.getIngredients());
		Ingredient tartine = Restaurant.getIngredients().get(Restaurant.getIngredients().size() - 1);
		HashMap<Ingredient, Integer> recette = new HashMap<>();
		recette.put(saumon, quantiteSaumon);
		recette.put(tartine, quantiteTartine);
		Plat plat = directeur.creerPlat(nomPlat, prixPlat, tempsPrepa, surCarte, type, categorie, recette);
		directeur.modifierDureePlat(plat, 10);
		assertEquals(10, plat.getDureePreparation());
	}

	@Test
	@DisplayName("Création d'une affectation dans la base de données")
	public void creationAffectationDB() {
		System.out.println("\nTest en cours : Création d'une affectation dans la base de données");
		try {
			// Prérequis du test : mise en place d'un étage et d'une table
			// Numéro de la table que l'on créé
			int numeroTable = 7;
			directeur.ajouterEtage();
			// Etage qui va recevoir une table
			Etage etage = Restaurant.getEtages().get(Restaurant.getEtages().size() - 1);
			// On ajoute la table
			directeur.ajouterTable(numeroTable, 10, etage);
			// La table créée
			Table tableActuelle = etage.getTables().get(0);

			// Création de l'affectation (date immédiate)
			Date dateNow = new Timestamp(new Date().getTime());
			Affectation affectation = directeur.creationAffectation(dateNow, 2, tableActuelle);
			ResultSet resultSet = sql
					.executerSelect("SELECT id FROM restaurant.affectation WHERE id=" + affectation.getId());
			// On vérifie qu'une ligne a bien été créé
			assertTrue(resultSet.next());
		} catch (ClassNotFoundException | SQLException | IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Création d'une affectation dans la mémoire")
	public void creationAffectationJava() {
		System.out.println("\nTest en cours : Création d'une affectation dans la mémoire");
		try {
			// Prérequis du test : mise en place d'un étage et d'une table
			// Numéro de la table que l'on créé
			int numeroTable = 8;
			directeur.ajouterEtage();
			// Etage qui va recevoir une table
			Etage etage = Restaurant.getEtages().get(Restaurant.getEtages().size() - 1);
			// On ajoute la table
			directeur.ajouterTable(numeroTable, 10, etage);
			// La table créée
			Table tableActuelle = etage.getTables().get(0);

			int tailleAvant = Restaurant.getAffectationsJour().size();
			// Création de l'affectation (date immédiate)
			directeur.creationAffectation(new Timestamp(new Date().getTime()), 2, tableActuelle);
			int tailleApres = Restaurant.getAffectationsJour().size();
			// On vérifie qu'une ligne a bien été créé
			assertTrue(tailleAvant < tailleApres);
		} catch (ClassNotFoundException | SQLException | IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Calcul d'une facture dans la base de données")
	public void updateFactureDB() {
		System.out.println("\nTest en cours : Calcul d'une facture dans la base de données");
		assertTrue(false);
	}

	@Test
	@DisplayName("Calcul d'une facture dans la mémoire")
	public void updateFactureJava() {
		System.out.println("\nTest en cours : Calcul d'une facture dans la mémoire");
		assertTrue(false);
	}

	@Test
	@DisplayName("Ajout date de fin d'une affectation dans la base de donnée")
	public void dateFinAffectationDB() {
		System.out.println("\nTest en cours : Ajout date de fin d'une affectation dans la base de donnée");
		// Prérequis du test : mise en place d'un étage et d'une table et d'une
		// affectation
		// Numéro de la table que l'on créé
		try {
			int numeroTable = 9;
			directeur.ajouterEtage();
			// Etage qui va recevoir une table
			Etage etage = Restaurant.getEtages().get(Restaurant.getEtages().size() - 1);
			// On ajoute la table
			directeur.ajouterTable(numeroTable, 10, etage);
			// La table créée
			Table tableActuelle = etage.getTables().get(0);
			// Création de l'affectation (date immédiate)
			Date debut = new Timestamp(new Date().getTime());
			Affectation affectation = directeur.creationAffectation(debut, 2, tableActuelle);

			// Attente de 0.5 secondes
			Thread.sleep(500);
			Date dateFin = new Timestamp(new Date().getTime());
			directeur.dateFinAffectation(affectation, dateFin);
			ResultSet resultSet = sql
					.executerSelect("SELECT datefin FROM restaurant.affectation WHERE id=" + affectation.getId());
			// check datefin not null
			resultSet.next();
			assertNotNull(resultSet.getDate("datefin"));
		} catch (ClassNotFoundException | SQLException | IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Ajout date de fin d'une affectation dans la mémoire")
	public void dateFinAffectationJava() {
		System.out.println("\nTest en cours : Calcul d'une facture la mémoire");
		// Prérequis du test : mise en place d'un étage et d'une table et d'une
		// affectation
		// Numéro de la table que l'on créé
		try {
			int numeroTable = 9;
			directeur.ajouterEtage();
			// Etage qui va recevoir une table
			Etage etage = Restaurant.getEtages().get(Restaurant.getEtages().size() - 1);
			// On ajoute la table
			directeur.ajouterTable(numeroTable, 10, etage);
			// La table créée
			Table tableActuelle = etage.getTables().get(0);
			// Création de l'affectation (date immédiate)
			Date debut = new Timestamp(new Date().getTime());
			Affectation affectation = directeur.creationAffectation(debut, 2, tableActuelle);

			Date dateFin = new Timestamp(new Date().getTime());
			directeur.dateFinAffectation(affectation, dateFin);
			assertNotNull(affectation.getDateFin());
		} catch (ClassNotFoundException | SQLException | IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Création d'une affectation dans la base de données")
	public void creationReservationDB() {
		System.out.println("\nTest en cours : Création d'une affectation dans la base de données");
		try {
			// Prérequis du test : mise en place d'un étage et d'une table
			int numeroTable = 10;
			directeur.ajouterEtage();
			// Etage qui va recevoir une table
			Etage etage = Restaurant.getEtages().get(Restaurant.getEtages().size() - 1);
			// On ajoute la table
			directeur.ajouterTable(numeroTable, 10, etage);
			// La table créée
			Table tableActuelle = etage.getTables().get(0);

			// La date de l'appel est immédiate
			Date dateAppel = new Timestamp(new Date().getTime());
			// Date demandée par le client
			String dateReservation = "27/12/1995 22:55:00";
			Date date1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dateReservation);
			Date dateReservationSQL = new Timestamp(date1.getTime());
			// Création de la réservation
			Reservation reservation = directeur.creationReservation(dateAppel, dateReservationSQL, 5, tableActuelle);
			ResultSet resultSet = sql
					.executerSelect("SELECT id FROM restaurant.reservation WHERE id=" + reservation.getId());
			assertTrue(resultSet.next());
		} catch (ClassNotFoundException | SQLException | IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Création d'une affectation dans la mémoire")
	public void creationReservationJava() {
		System.out.println("\nTest en cours : Création d'une affectation dans la mémoire");
		try {
			// Prérequis du test : mise en place d'un étage et d'une table
			int numeroTable = 11;
			directeur.ajouterEtage();
			// Etage qui va recevoir une table
			Etage etage = Restaurant.getEtages().get(Restaurant.getEtages().size() - 1);
			// On ajoute la table
			directeur.ajouterTable(numeroTable, 10, etage);
			// La table créée
			Table tableActuelle = etage.getTables().get(0);
			int nombreReservationsAvant = Restaurant.getReservationsJour().size();

			// La date de l'appel est immédiate
			Date dateAppel = new Timestamp(new Date().getTime());
			// Date demandée par le client
			String dateReservation = "27/12/1992 22:55:00";
			Date date1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dateReservation);
			Date dateReservationSQL = new Timestamp(date1.getTime());
			// Création de la réservation
			directeur.creationReservation(dateAppel, dateReservationSQL, 5, tableActuelle);
			int nombreReservationsApres = Restaurant.getReservationsJour().size();
			assertTrue(nombreReservationsAvant < nombreReservationsApres);
		} catch (ClassNotFoundException | SQLException | IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Suppression d'une réservation dans la base de donées")
	public void supprimerReservationDB() {
		System.out.println("\nTest en cours : Suppression d'une réservation dans la base de donées");
		try {
			int numeroTable = 12;
			directeur.ajouterEtage();
			// Etage qui va recevoir une table
			Etage etage = Restaurant.getEtages().get(Restaurant.getEtages().size() - 1);
			// On ajoute la table
			directeur.ajouterTable(numeroTable, 10, etage);
			// La table créée
			Table tableActuelle = etage.getTables().get(0);
			// La date de l'appel est immédiate
			Date dateAppel = new Timestamp(new Date().getTime());
			// Date demandée par le client
			String dateReservation = "27/12/1992 22:55:00";
			Date date1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dateReservation);
			Date dateReservationSQL = new Timestamp(date1.getTime());
			// Création de la réservation
			Reservation asuppr = directeur.creationReservation(dateAppel, dateReservationSQL, 5, tableActuelle);

			// On supprime la table
			directeur.supprimerReservation(asuppr);
			ResultSet resultSet = sql
					.executerSelect("SELECT id FROM restaurant.reservation WHERE id=" + asuppr.getId());
			// On vérifie qu'aucune ligne n'est trouvée car l'id recherché a été supprimé
			assertFalse(resultSet.next());
		} catch (ClassNotFoundException | SQLException | IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Suppression d'une réservation dans la mémoire")
	public void supprimerReservationJava() {
		System.out.println("\nTest en cours : Suppression d'une réservation dans la mémoire");
		try {
			int numeroTable = 12;
			directeur.ajouterEtage();
			// Etage qui va recevoir une table
			Etage etage = Restaurant.getEtages().get(Restaurant.getEtages().size() - 1);
			// On ajoute la table
			directeur.ajouterTable(numeroTable, 10, etage);
			// La table créée
			Table tableActuelle = etage.getTables().get(0);
			// La date de l'appel est immédiate
			Date dateAppel = new Timestamp(new Date().getTime());
			// Date demandée par le client
			String dateReservation = "27/12/1992 22:55:00";
			Date date1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dateReservation);
			Date dateReservationSQL = new Timestamp(date1.getTime());
			// Création de la réservation
			Reservation asuppr = directeur.creationReservation(dateAppel, dateReservationSQL, 5, tableActuelle);
			int nbReservationAvant = Restaurant.getReservationsJour().size();
			// On supprime la table
			directeur.supprimerReservation(asuppr);
			int nbReservationApres = Restaurant.getReservationsJour().size();

			ResultSet resultSet = sql
					.executerSelect("SELECT id FROM restaurant.reservation WHERE id=" + asuppr.getId());
			// On vérifie qu'aucune ligne n'est trouvée car l'id recherché a été supprimé
			assertTrue(nbReservationApres < nbReservationAvant);
		} catch (ClassNotFoundException | SQLException | IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Vérification de l'initialisation des horaires dans la base de données")
	public void verifierInitialisationHorairesDB() {
		System.out.println("\nTest en cours : Vérification de l'initialisation des horaires dans la base de données");
		try {
			directeur.ajouterEtage();
			ResultSet resultSet = sql.executerSelect("SELECT * FROM restaurant.restaurant");
			resultSet.next();
			// Récupération des valeurs par défaut
			Properties prop = new Properties();
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(this.propertiesFilename);
			prop.load(inputStream);
			int heureDejeunerOuverture = Integer.parseInt(prop.getProperty("default.heureDejeunerOuverture"));
//			int heureDejeunerLimite = Integer.parseInt(prop.getProperty("default.heureDejeunerLimite"));
//			int heureDinerOuverture = Integer.parseInt(prop.getProperty("default.heureDinerOuverture"));
//			int heureDinerLimite = Integer.parseInt(prop.getProperty("default.heureDinerLimite"));
			assertEquals(heureDejeunerOuverture, resultSet.getInt("heureouverturedejeune"));
		} catch (ClassNotFoundException | SQLException | IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Vérification de l'initialisation des horaires dans la mémoire")
	public void verifierInitialisationHorairesJava() {
		System.out.println("\nTest en cours : Vérification de l'initialisation des horaires dans la mémoire");
		try {
			directeur.ajouterEtage();
			// Récupération des valeurs par défaut
			Properties prop = new Properties();
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(this.propertiesFilename);
			prop.load(inputStream);
			int heureDejeunerOuverture = Integer.parseInt(prop.getProperty("default.heureDejeunerOuverture"));
			assertEquals(LocalTime.ofSecondOfDay(heureDejeunerOuverture), Restaurant.getHeureDejeunerOuverture());
		} catch (ClassNotFoundException | SQLException | IOException e) {
			e.printStackTrace();
		}
	}

}
