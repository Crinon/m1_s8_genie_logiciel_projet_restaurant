package m1_s8_genie_logiciel_projet_restaurant;

import restaurant.Directeur;
import restaurant.Restaurant;
import restaurant.Sql;

import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tests du projet restaurant")
public class TestUnitaire {

//  @Test(expected=IllegalArgumentException.class)
//  public void testIsMotDePasseValideNull() {
//      SecuriteHelper.isMotDePasseValide(null);
//  }
  
	@BeforeAll
	public static void initialisation() throws SQLException {
		System.out.println("Avant");
	}
	
  @Test
  public void insertionIngredient() throws SQLException, ClassNotFoundException, IOException {
		Sql sql = new Sql();	
		sql.executerTests("DROP ALL OBJECTS");
		sql.executerTests("CREATE SCHEMA IF NOT EXISTS restaurant");
		sql.executerTests("create user if not exists restaurant_user password '' admin");
		
		// Ingrédients
		sql.executerTests("CREATE SEQUENCE restaurant.ingredient_id_seq\r\n"
				+ "    INCREMENT 1\r\n"
				+ "    START 1\r\n"
				+ "    MINVALUE 1\r\n"
				+ "    MAXVALUE 2147483647\r\n"
				+ "    CACHE 1;\r\n");
		sql.executerTests("-- Table: restaurant.ingredient\r\n"
				+ "\r\n"
				+ "-- DROP TABLE restaurant.ingredient;\r\n"
				+ "\r\n"
				+ "CREATE TABLE restaurant.ingredient\r\n"
				+ "(\r\n"
				+ "    id integer NOT NULL DEFAULT nextval('restaurant.ingredient_id_seq'),\r\n"
				+ "    nom character varying NOT NULL,\r\n"
				+ "    quantite integer NOT NULL DEFAULT 0,\r\n"
				+ "    CONSTRAINT ingredient_pkey PRIMARY KEY (id)\r\n"
				+ ")\r\n");
		// Etages
		sql.executerTests("CREATE SEQUENCE restaurant.etage_id_seq\r\n"
				+ "    INCREMENT 1\r\n"
				+ "    START 1\r\n"
				+ "    MINVALUE 1\r\n"
				+ "    MAXVALUE 2147483647\r\n"
				+ "    CACHE 1;");
		sql.executerTests("CREATE TABLE restaurant.etage\r\n"
				+ "(\r\n"
				+ "    id integer NOT NULL DEFAULT nextval('restaurant.etage_id_seq'),\r\n"
				+ "    niveau integer NOT NULL,\r\n"
				+ "    CONSTRAINT etage_pkey PRIMARY KEY (id)\r\n"
				+ ");");
//		sql.executerTests("");
    	Restaurant.initialisation();
		Directeur p = new Directeur("directeur");
		p.ajouterIngredient("carotte",Restaurant.getIngredients());
		ResultSet res = sql.executerSelect("select * from restaurant.ingredient where nom = 'carotte'");
	    assertTrue(res.next());
	  }
	  
  }
  
