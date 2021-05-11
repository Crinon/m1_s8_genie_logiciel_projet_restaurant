package m1_s8_genie_logiciel_projet_restaurant;

import restaurant.Assistant;
import restaurant.Directeur;
import restaurant.Personne;
import restaurant.Restaurant;
import restaurant.Serveur;
import restaurant.Sql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

@DisplayName("Tests du projet restaurant")
public class TestUnitaire {
	private static Sql sql;
	Directeur directeur = new Directeur("directeur");

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
		sql.executerTests("CREATE SEQUENCE restaurant.ingredient_id_seq\r\n"
				+ "    INCREMENT 1\r\n"
				+ "    START 1\r\n"
				+ "    MINVALUE 1\r\n"
				+ "    MAXVALUE 2147483647\r\n"
				+ "    CACHE 1;");
		sql.executerTests("CREATE TABLE restaurant.ingredient\r\n"
				+ "(\r\n"
				+ "    id integer NOT NULL DEFAULT nextval('restaurant.ingredient_id_seq'),\r\n"
				+ "    nom character varying NOT NULL,\r\n"
				+ "    quantite integer NOT NULL DEFAULT 0,\r\n"
				+ "    CONSTRAINT ingredient_pkey PRIMARY KEY (id)\r\n"
				+ ");");
		// Etages
		sql.executerTests("CREATE SEQUENCE restaurant.etage_id_seq\r\n" + "    INCREMENT 1\r\n" + "    START 1\r\n"
				+ "    MINVALUE 1\r\n" + "    MAXVALUE 2147483647\r\n" + "    CACHE 1;");
		sql.executerTests("CREATE TABLE restaurant.etage\r\n" + "(\r\n"
				+ "    id integer NOT NULL DEFAULT nextval('restaurant.etage_id_seq'),\r\n"
				+ "    niveau integer NOT NULL,\r\n" + "    CONSTRAINT etage_pkey PRIMARY KEY (id)\r\n" + ");");
		// Personne
		sql.executerTests("CREATE SEQUENCE restaurant.personne_id_seq\r\n"
				+ "    INCREMENT 1\r\n"
				+ "    START 1\r\n"
				+ "    MINVALUE 1\r\n"
				+ "    MAXVALUE 2147483647\r\n"
				+ "    CACHE 1;");
		sql.executerTests("CREATE TABLE restaurant.personne\r\n"
				+ "(\r\n"
				+ "    id integer NOT NULL DEFAULT nextval('restaurant.personne_id_seq'),\r\n"
				+ "    nom character varying  NOT NULL,\r\n"
				+ "    login character varying  NOT NULL,\r\n"
				+ "    CONSTRAINT personne_pkey PRIMARY KEY (id)\r\n"
				+ ")");
		// Directeur
		sql.executerTests("CREATE SEQUENCE restaurant.directeur_id_seq\r\n"
				+ "    INCREMENT 1\r\n"
				+ "    START 1\r\n"
				+ "    MINVALUE 1\r\n"
				+ "    MAXVALUE 2147483647\r\n"
				+ "    CACHE 1;");
		sql.executerTests("CREATE TABLE restaurant.directeur\r\n"
				+ "(\r\n"
				+ "    id integer NOT NULL DEFAULT nextval('restaurant.directeur_id_seq'),\r\n"
				+ "    personne integer NOT NULL,\r\n"
				+ "    CONSTRAINT directeur_pkey PRIMARY KEY (id),\r\n"
				+ "    CONSTRAINT directeur_personne_fkey FOREIGN KEY (personne)\r\n"
				+ "        REFERENCES restaurant.personne (id) \r\n"
				+ ")");
		// Cuisinier
		sql.executerTests("CREATE SEQUENCE restaurant.cuisinier_id_seq\r\n"
				+ "    INCREMENT 1\r\n"
				+ "    START 1\r\n"
				+ "    MINVALUE 1\r\n"
				+ "    MAXVALUE 2147483647\r\n"
				+ "    CACHE 1;");
		sql.executerTests("CREATE TABLE restaurant.cuisinier\r\n"
				+ "(\r\n"
				+ "    id integer NOT NULL DEFAULT nextval('restaurant.cuisinier_id_seq'),\r\n"
				+ "    personne integer NOT NULL,\r\n"
				+ "    CONSTRAINT cuisinier_pkey PRIMARY KEY (id),\r\n"
				+ "    CONSTRAINT cuisinier_personne_fkey FOREIGN KEY (personne)\r\n"
				+ "        REFERENCES restaurant.personne (id) \r\n"
				+ "        ON UPDATE NO ACTION\r\n"
				+ "        ON DELETE NO ACTION\r\n"
				+ ")");
		// Assistant
		sql.executerTests("CREATE SEQUENCE restaurant.assistant_id_seq\r\n"
				+ "    INCREMENT 1\r\n"
				+ "    START 1\r\n"
				+ "    MINVALUE 1\r\n"
				+ "    MAXVALUE 2147483647\r\n"
				+ "    CACHE 1;");
		sql.executerTests("CREATE TABLE restaurant.assistant\r\n"
				+ "(\r\n"
				+ "    id integer NOT NULL DEFAULT nextval('restaurant.assistant_id_seq'),\r\n"
				+ "    personne integer NOT NULL,\r\n"
				+ "    CONSTRAINT assistant_pkey PRIMARY KEY (id),\r\n"
				+ "    CONSTRAINT assistant_personne_fkey FOREIGN KEY (personne)\r\n"
				+ "        REFERENCES restaurant.personne (id) \r\n"
				+ "        ON UPDATE NO ACTION\r\n"
				+ "        ON DELETE NO ACTION\r\n"
				+ ")");
		// Serveur
		sql.executerTests("CREATE SEQUENCE restaurant.serveur_id_seq\r\n"
				+ "    INCREMENT 1\r\n"
				+ "    START 1\r\n"
				+ "    MINVALUE 1\r\n"
				+ "    MAXVALUE 2147483647\r\n"
				+ "    CACHE 1;");
		sql.executerTests("CREATE TABLE restaurant.serveur\r\n"
				+ "(\r\n"
				+ "    id integer NOT NULL DEFAULT nextval('restaurant.serveur_id_seq'),\r\n"
				+ "    personne integer NOT NULL,\r\n"
				+ "    CONSTRAINT serveur_pkey PRIMARY KEY (id)\r\n"
				+ ")");
		// Maitre d'hôtel
		sql.executerTests("CREATE SEQUENCE restaurant.maitrehotel_id_seq\r\n"
				+ "    INCREMENT 1\r\n"
				+ "    START 1\r\n"
				+ "    MINVALUE 1\r\n"
				+ "    MAXVALUE 2147483647\r\n"
				+ "    CACHE 1;");
		sql.executerTests("CREATE TABLE restaurant.maitrehotel\r\n"
				+ "(\r\n"
				+ "    id integer NOT NULL DEFAULT nextval('restaurant.maitrehotel_id_seq'),\r\n"
				+ "    personne integer NOT NULL,\r\n"
				+ "    CONSTRAINT maitrehotel_pkey PRIMARY KEY (id),\r\n"
				+ "    CONSTRAINT maitrehotel_personne_fkey FOREIGN KEY (personne)\r\n"
				+ "        REFERENCES restaurant.personne (id) \r\n"
				+ "        ON UPDATE NO ACTION\r\n"
				+ "        ON DELETE NO ACTION\r\n"
				+ ")");

		Restaurant.initialisation();
	}
	
	@Test
    @Order(1)
	@DisplayName("Ajout automatique d'un directeur dans la base de données s'il n'en existe aucun")
	public void insertionDirecteurPremierDemarrage() {
		System.out.println("\nTest en cours : création du directeur lorsqu'il n'y en a aucun dans la base de données");
		ResultSet resultSet = sql.executerSelect("SELECT login from restaurant.personne where login = 'directeur0'");
		try {
			resultSet.next();
			// Le test vient de l'initialisation de la classe statique Restaurant
			assertEquals("directeur0",resultSet.getString("login"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
    @Order(2)
	@DisplayName("Création d'un ingrédient dans la base de donnée")
	public void insertionIngredientDB() throws SQLException, ClassNotFoundException, IOException {
		directeur.ajouterIngredient("carotte", Restaurant.getIngredients());
		ResultSet res = sql.executerSelect("select * from restaurant.ingredient where nom = 'carotte'");
		assertTrue(res.next());
	}
	@Test
	@Order(3)
	@DisplayName("Création d'un ingrédient dans la mémoire")
	public void insertionIngredientJava() throws SQLException, ClassNotFoundException, IOException {
		directeur.ajouterIngredient("patate", Restaurant.getIngredients());
		assertEquals("patate",Restaurant.getIngredients().get(Restaurant.getIngredients().size()-1).getNom());
	}

	@Test
	@Order(4)
	@DisplayName("Tentative de créer 2 fois le même ingrédient dans la base de données")
	public void refuseDoublonIngredientDB() throws ClassNotFoundException, SQLException, IOException {
		System.out.println("\nTest en cours : doublon d'ingrédient");
		directeur.ajouterIngredient("tomate", Restaurant.getIngredients());
		directeur.ajouterIngredient("tomate", Restaurant.getIngredients());
		ResultSet resultSet = sql.executerSelect("SELECT count(*) as count from restaurant.ingredient where nom = 'tomate'");
		resultSet.next();
		// Il ne doit y avoir qu'un seul ingrédient avec le nom tomate
		assertEquals("ERREUR : un ingrédient avec le même nom est présent 2 fois en base.",
				1,
				Integer.parseInt(resultSet.getString("count")));
	}

	@Test
	@Order(5)
	@DisplayName("Commander un ingrédient : modification de la quantité dans la base de données")
	public void commanderIngredientDB() throws ClassNotFoundException, SQLException, IOException {
		System.out.println("\nTest en cours : commande d'ingrédient");
		directeur.commanderIngredient(Restaurant.getIngredients().get(0), 10);
		ResultSet resultSet = sql.executerSelect("SELECT nom,quantite from restaurant.ingredient where id = " + Restaurant.getIngredients().get(0).getId());
		resultSet.next();
		assertEquals(10,Integer.parseInt(resultSet.getString("quantite")));
	}
	

	@Test
	@Order(6)
	@DisplayName("Insertion d'un personnel assistant dans la base de données")
	public void insertionPersonnelDB() {
		System.out.println("\nTest en cours : Insertion d'un assistant dans la base de données");
		directeur.ajouterPersonnel("Julien", "assistant", Restaurant.getPersonnel());
		ResultSet resultSet = sql.executerSelect("SELECT login from restaurant.personne where login = '" + Restaurant.getPersonnel().get(Restaurant.getPersonnel().size()-1).getIdentifiant()  +"'");
		try {
			assertTrue(resultSet.next());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@Order(7)
	@DisplayName("Modification d'un personnel (assistant vers serveur) dans la base de données")
	public void modificationPersonnelDB() {
		try {
			System.out.println("\nTest en cours : Modification d'un personnel (assistant vers serveur) dans la base de données");
			Personne assistant =directeur.ajouterPersonnel("Hervé", "assistant", Restaurant.getPersonnel());
			Personne herve = directeur.modifierPersonnel(assistant, "serveur");
			ResultSet resultSet = sql.executerSelect("SELECT id from restaurant.serveur where personne = " + herve.getId());
			System.out.println("LAAAAA");
			assertTrue(resultSet.next());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	@Order(8)
	@DisplayName("Suppression d'un personnel dans la base de données")
	public void suppressionPersonnelDB() {
		try {
	        directeur.ajouterPersonnel("NicolasSupprimer", "directeur", Restaurant.getPersonnel());

			System.out.println("\nTest en cours : Suppression d'un personnel dans la base de données");
			Personne personne = Restaurant.getPersonnel().get(Restaurant.getPersonnel().size()-1);
	        directeur.supprimerPersonnel(personne, Restaurant.getPersonnel());
			ResultSet resultSet = sql.executerSelect("SELECT login from restaurant.personne where login = '" + personne.getIdentifiant()  +"'");
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
		assertEquals(newPersonne, Restaurant.getPersonnel().get(Restaurant.getPersonnel().size()-1));
	}
	
	@Test
	@DisplayName("Modification d'un personnel (assistant vers serveur) dans la dans la mémoire")
	public void modificationPersonnelJava() {
		System.out.println("\nTest en cours : Modification d'un personnel (assistant vers serveur) dans la base de données");
		Personne assistant = Restaurant.getPersonnel().get(Restaurant.getPersonnel().size()-1);
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
		assertEquals(-1,Restaurant.getPersonnel().indexOf(personne));
	}

}
