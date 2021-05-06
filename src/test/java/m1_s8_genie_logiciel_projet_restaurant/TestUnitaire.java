package m1_s8_genie_logiciel_projet_restaurant;

import restaurant.Directeur;
import restaurant.Restaurant;
import restaurant.Sql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
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
	public void insertionIngredient() throws SQLException, ClassNotFoundException, IOException {
		directeur.ajouterIngredient("carotte", Restaurant.getIngredients());
		ResultSet res = sql.executerSelect("select * from restaurant.ingredient where nom = 'carotte'");
		assertTrue(res.next());
	}

	@Test
	public void refuseDoublonIngredient() throws ClassNotFoundException, SQLException, IOException {
		System.out.println("\nTest en cours : doublon d'ingrédient");
		directeur.ajouterIngredient("tomate", Restaurant.getIngredients());
		directeur.ajouterIngredient("tomate", Restaurant.getIngredients());
		ResultSet resultSet = sql.executerSelect("SELECT count(*) as count from restaurant.ingredient where nom = 'carotte'");
		resultSet.next();
		// Il ne doit y avoir qu'un seul ingrédient avec le nom tomate
		assertEquals("ERREUR : un ingrédient avec le même nom est présent 2 fois en base.",
				1,
				Integer.parseInt(resultSet.getString("count")));
	}

	@Test
	public void commanderIngredient() throws ClassNotFoundException, SQLException, IOException {
		System.out.println("\nTest en cours : commande d'ingrédient");
		directeur.commanderIngredient(Restaurant.getIngredients().get(0), 10);
		ResultSet resultSet = sql.executerSelect("SELECT nom,quantite from restaurant.ingredient where id = " + Restaurant.getIngredients().get(0).getId());
		resultSet.next();
		assertEquals(10,Integer.parseInt(resultSet.getString("quantite")));
	}
	
	@Test
	public void insertionDirecteurPremierDemarrage() {
		System.out.println("\nTest en cours : création du directeur lorsqu'il n'y en a aucun dans la base de données");
		ResultSet resultSet = sql.executerSelect("SELECT login from restaurant.personne where login = 'directeur0'");
		try {
			resultSet.next();
			assertEquals("directeur0",resultSet.getString("login"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
