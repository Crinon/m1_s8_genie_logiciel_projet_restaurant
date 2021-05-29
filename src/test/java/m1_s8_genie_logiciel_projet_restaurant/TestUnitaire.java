package m1_s8_genie_logiciel_projet_restaurant;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import restaurant.Affectation;
import restaurant.Categorie;
import restaurant.Commande;
import restaurant.Directeur;
import restaurant.Etage;
import restaurant.Etat;
import restaurant.EtatTable;
import restaurant.Ingredient;
import restaurant.Personne;
import restaurant.Plat;
import restaurant.Reservation;
import restaurant.Restaurant;
import restaurant.Serveur;
import restaurant.Sql;
import restaurant.Table;
import restaurant.Type;

@DisplayName("Tests du projet restaurant")
public class TestUnitaire {

	public static int numeroGlobal;
	public final String propertiesFilename = "properties";
	private static Sql sql;
	Directeur directeur = new Directeur(0, "directeur", "directeur0");

	public int incr() {
		this.numeroGlobal = this.numeroGlobal + 1;
		return this.numeroGlobal;
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@BeforeAll
	public static void initialisation() {
		numeroGlobal = 30;
		sql = new Sql();
		System.out.println("\nBEFORE ALL : création BDD");
		System.err.println("Exécution du script de création de la base de données H2.");
		// On récupère le fichier SQL au format string
		InputStream inputStream = TestUnitaire.class.getClassLoader().getResourceAsStream("restaurant.sql");
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		try {
			for (int length; (length = inputStream.read(buffer)) != -1;) {
				result.write(buffer, 0, length);
			}
			String sqlbuildscript = result.toString("UTF-8");
			sql.executerUpdate(sqlbuildscript);

			// Insertion d'un jeu de données
			System.err.println("Insertion du jeu de données");
			// On récupère le fichier SQL au format string
			inputStream = TestUnitaire.class.getClassLoader().getResourceAsStream("jeudonnees.sql");
			result = new ByteArrayOutputStream();
			byte[] buffer2 = new byte[1024];
			for (int length; (length = inputStream.read(buffer2)) != -1;) {
				result.write(buffer2, 0, length);
			}
			String jeudedonnees = result.toString("UTF-8");
			sql.executerUpdate(jeudedonnees);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Restaurant.initialisation();
	}

	@BeforeEach
	public void resetDB() {
		sql.hardResetH2(sql.hardResetH2);
		Restaurant.resetRestaurant();
		Restaurant.initialisation();
	}

	@Test
	@DisplayName("Création d'un ingrédient dans la base de donnée")
	public void insertionIngredientDB() throws SQLException, ClassNotFoundException, IOException {
		directeur.ajouterIngredient("carotte");
		ResultSet res = sql.executerSelect("select * from restaurant.ingredient where nom = 'carotte'");
		assertTrue(res.next());
	}

	@Test
	@DisplayName("Création d'un ingrédient dans la mémoire")
	public void insertionIngredientJava() throws SQLException, ClassNotFoundException, IOException {
		directeur.ajouterIngredient("chips");
		assertEquals("chips", Restaurant.getIngredients().get(Restaurant.getIngredients().size() - 1).getNom());
	}

	@Test
	@DisplayName("Tentative de créer 2 fois le même ingrédient dans la base de données")
	public void refuseDoublonIngredientDB() throws ClassNotFoundException, SQLException, IOException {
		System.out.println("\nTest en cours : doublon d'ingrédient");
		directeur.ajouterIngredient("tomate");
		directeur.ajouterIngredient("tomate");
		ResultSet resultSet = sql
				.executerSelect("SELECT count(*) as count from restaurant.ingredient where nom = 'tomate'");
		resultSet.next();
		assertEquals(1, Integer.parseInt(resultSet.getString("count")));
	}

	@Test
	@DisplayName("Commander un ingrédient : modification de la quantité dans la base de données")
	public void commanderIngredientDB() {
		System.out.println("\nTest en cours : commande d'ingrédient");
		try {
			Ingredient ingredient = directeur.ajouterIngredient("Oxygène");
			directeur.commanderIngredient(ingredient, 10);
			directeur.commanderIngredient(ingredient, 10);
			directeur.commanderIngredient(ingredient, 10);
			ResultSet resultSet = sql
					.executerSelect("SELECT quantite FROM restaurant.ingredient where id = " + ingredient.getId());
			resultSet.next();
			int stock = Integer.parseInt(resultSet.getString("quantite"));
			assertEquals(30, stock);
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
			Personne assistant = directeur.ajouterPersonnel("Hervé", "assistant");
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
			directeur.ajouterPersonnel("NicolasSupprimer", "cuisinier");

			System.out.println("\nTest en cours : Suppression d'un personnel dans la base de données");
			Personne personne = Restaurant.getPersonnel().get(Restaurant.getPersonnel().size() - 1);
			directeur.supprimerPersonnel(personne, Restaurant.getPersonnel());
			ResultSet resultSet = sql.executerSelect(
					"SELECT login from restaurant.personne where login = '" + personne.getIdentifiant() + "'");
			resultSet.next(); // Il ne doit exister aucune ligne dans le resultSet
			assertFalse(resultSet.next());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Insertion d'un personnel dans la mémoire")
	public void insertionPersonnelJava() {
		System.out.println("\nTest en cours : Insertion d'un personnel dans la mémoire");
		Personne newPersonne = directeur.ajouterPersonnel("Julien", "assistant");
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
		Personne personne = directeur.ajouterPersonnel("Julien", "assistant");
		directeur.supprimerPersonnel(personne, Restaurant.getPersonnel());
		assertFalse(Restaurant.getPersonnel().contains(personne));
	}

	@Test
	@DisplayName("Ajout d'un étage dans la mémoire")
	public void ajouterEtageJava() {
		System.out.println("\nTest en cours : Ajout d'un étage dans la mémoire");
		// On regarde les étages déjà existants int nbEtageAvant =
		int nbEtageAvant = Restaurant.getEtages().size();
		directeur.ajouterEtage();
		int nbEtageApres = Restaurant.getEtages().size();
		assertTrue(nbEtageAvant < nbEtageApres);
	}

	@Test
	@DisplayName("Commander un ingrédient : modification de la quantité d'un ingrédient dans la mémoire")
	public void commanderIngredientJava() throws ClassNotFoundException, SQLException, IOException {
		System.out.println("\nTest en cours : modification de la quantité d'un ingrédient dans la mémoire");
		Ingredient ingredientTest = directeur.ajouterIngredient("ours");
		directeur.commanderIngredient(ingredientTest, 17);
		directeur.commanderIngredient(ingredientTest, 23);

		assertEquals(40, ingredientTest.getQuantite());
//		assertEquals(10, Integer.parseInt(resultSet.getString("quantite")));
	}

	@Test
	@DisplayName("Insertion d'un personnel assistant dans la base de données")
	public void insertionPersonnelDB() {
		try {
			System.out.println("\nTest en cours : Insertion d'un assistant dans la base de données");
			directeur.ajouterPersonnel("Julien", "assistant");
			ResultSet resultSet = sql.executerSelect("SELECT login from restaurant.personne where login = '"
					+ Restaurant.getPersonnel().get(Restaurant.getPersonnel().size() - 1).getIdentifiant() + "'");
			assertTrue(resultSet.next());
		} catch (SQLException e) {
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
			assertTrue(niveauMaxApresSuppression < niveauMaxAvantSuppression);
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Suppression d'un étage dans la mémoire")
	public void supprimerEtageJava() {
		System.out.println("\nTest en cours : Suppression d'un étage dans la mémoire");
		directeur.ajouterEtage();
		directeur.ajouterEtage(); // On
		int nbEtageAvantSuppression = Restaurant.getEtages().size();
		directeur.supprimerDernierEtage();
		int nbEtageApresSuppression = Restaurant.getEtages().size();
		assertTrue(nbEtageApresSuppression < nbEtageAvantSuppression);
	}

	@Test
	@DisplayName("Ajout d'une table à un étage dans la base de données")
	public void ajouterTableDB() {
		System.out.println("\nTest en cours : Ajout d'une table à un étage dans la base de données");
		try {
			int numero = incr();
			directeur.ajouterEtage();
			Etage etage = Restaurant.getEtages().get(Restaurant.getEtages().size() - 1);
			directeur.ajouterTable(numero, 10, etage);
			;
			ResultSet res = sql.executerSelect(
					"SELECT * FROM restaurant.tables WHERE numero=" + numero + " AND etage=" + etage.getId());
			assertTrue(res.next());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Ajout d'une table à un étage dans la mémoire")
	public void ajouterTableJava() {
		System.out.println("\nTest en cours : Ajout d'une table à un étage dans la mémoire");
		directeur.ajouterEtage();
		int numero = incr();
		Etage etage = Restaurant.getEtages().get(Restaurant.getEtages().size() - 1);
		int nbTableAvant = etage.getTables().size();
		directeur.ajouterTable(numero, 10, etage);
		int nbTableApres = etage.getTables().size();
		assertTrue(nbTableAvant < nbTableApres);
	}

	@Test
	@DisplayName("Modificiation du numéro d'une table dans la base de donées")
	public void modifierNumeroTableDB() {
		System.out.println("\nTest en cours : Modificiation du numéro d'une table dans la base de donées");
		try {
			int numeroAvant = incr();
			System.out.println("Numéro de la table à créer puis modifier : " + numeroAvant);
			int numeroApres = incr();
			System.out.println("Nouveau numéro souhaité : " + numeroApres);
			int numeroTrouve = -1;
			directeur.ajouterEtage();
			Etage etage = Restaurant.getEtages().get(Restaurant.getEtages().size() - 1);
			Table tableActuelle = directeur.ajouterTable(numeroAvant, 10, etage);
			System.out.println(tableActuelle);
			directeur.modifierNumeroTable(tableActuelle, numeroApres);
			ResultSet resultSet = sql
					.executerSelect("SELECT numero FROM restaurant.tables WHERE id=" + tableActuelle.getId());
			if (resultSet.next()) {
				numeroTrouve = Integer.parseInt(resultSet.getString("numero"));
			}
			assertEquals(numeroApres, numeroTrouve);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Modificiation du numéro d'une table dans la mémoire")
	public void modifierNumeroTableJava() {
		System.out.println("\nTest en cours : Modificiation du numéro d'une table dans la mémoire");
		int numeroAvant = incr();
		System.out.println("Numéro de la table à créer puis modifier : " + numeroAvant);
		int numeroApres = incr();
		System.out.println("Nouveau numéro souhaité : " + numeroApres);
		directeur.ajouterEtage();
		Etage etage = Restaurant.getEtages().get(Restaurant.getEtages().size() - 1);
		Table tableActuelle = directeur.ajouterTable(numeroAvant, 10, etage);
		directeur.modifierNumeroTable(tableActuelle, numeroApres);
		assertEquals(numeroApres, tableActuelle.getNumero());
	}

	@Test
	@DisplayName("Modificiation de l'état d'une table dans la base de données")
	public void modifierEtatTableDB() {
		System.out.println("\nTest en cours : Modificiation de l'état d'une table dans la base de données");
		try {
			int numero = incr();
			directeur.ajouterEtage();
			Etage etage = Restaurant.getEtages().get(Restaurant.getEtages().size() - 1);
			directeur.ajouterTable(numero, 10, etage);
			Table tableActuelle = etage.getTables().get(etage.getTables().size() - 1);
			directeur.modifierEtatTable(tableActuelle, EtatTable.Reserve);
			ResultSet resultSet = sql
					.executerSelect("SELECT etat FROM restaurant.tables WHERE id=" + tableActuelle.getId());
			resultSet.next();
			assertEquals("Reserve", resultSet.getString("etat"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Modificiation de l'état d'une table dans la mémoire")
	public void modifierEtatTableJava() {
		System.out.println("\nTest en cours : Modificiation de l'état d'une table dans la mémoire");
		int numero = incr();
		directeur.ajouterEtage();
		Etage etage = Restaurant.getEtages().get(Restaurant.getEtages().size() - 1);
		directeur.ajouterTable(numero, 10, etage);
		Table tableActuelle = etage.getTables().get(etage.getTables().size() - 1);
		directeur.modifierEtatTable(tableActuelle, EtatTable.Reserve);
		assertEquals("Reserve", tableActuelle.getEtat().name());
	}

	@Test
	@DisplayName("Affectation d'une table à un serveur lors d'une affectation dans la base de données")
	public void modifierServeurTableDB() {
		System.out.println("\nTest en cours : Modificiation du numéro d'une table dans la base de données");
		try {
			int numero = incr();
			directeur.ajouterEtage();
			Serveur serveur = (Serveur) directeur.ajouterPersonnel("Jean", "serveur");
			Etage etage = Restaurant.getEtages().get(Restaurant.getEtages().size() - 1);
			directeur.ajouterTable(numero, 10, etage);
			Table tableActuelle = etage.getTables().get(etage.getTables().size() - 1);
			directeur.affecterTableServeur(serveur, tableActuelle);
			ResultSet resultSet = sql
					.executerSelect("SELECT serveur FROM restaurant.tables WHERE id=" + tableActuelle.getId());
			resultSet.next();
			assertEquals(serveur.getId(), resultSet.getInt("serveur"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Affectation table serveurs lors d'une affectation dans la mémoire")
	public void modifierServeurTableJava() {
		System.out.println("\nTest en cours : Modificiation du numéro d'une table dans la mémoire");
		int numero = incr();
		directeur.ajouterEtage();
		Serveur serveur = (Serveur) directeur.ajouterPersonnel("Jean", "serveur");
		Etage etage = Restaurant.getEtages().get(Restaurant.getEtages().size() - 1);
		directeur.ajouterTable(numero, 10, etage);
		Table tableActuelle = etage.getTables().get(etage.getTables().size() - 1);
		directeur.affecterTableServeur(serveur, tableActuelle);
		assertTrue(serveur.getTablesAffectees().contains(tableActuelle));
	}

	@Test
	@DisplayName("Retrait table serveurs lors d'une affectation dans la mémoire 2")
	public void modifierServeurTableJava2() {
		System.out.println("\nTest en cours : Modificiation du numéro d'une table dans la mémoire");
		int numero = incr();
		directeur.ajouterEtage();
		Serveur serveur = (Serveur) directeur.ajouterPersonnel("Jean", "serveur");
		Etage etage = Restaurant.getEtages().get(Restaurant.getEtages().size() - 1);
		directeur.ajouterTable(numero, 10, etage);
		Table tableActuelle = etage.getTables().get(etage.getTables().size() - 1);
		directeur.affecterTableServeur(serveur, tableActuelle);
		Restaurant.getPersonnel().stream().filter(p -> p.getClass().getName().equals("Serveur"))
				.filter(s -> s.getId() != serveur.getId())
				.forEach(s -> assertFalse(((Serveur) s).getTablesAffectees().contains(tableActuelle)));
	}

	@Test
	@DisplayName("Suppression d'une table dans la base de donées")
	public void supprimerTableDB() {
		System.out.println("\nTest en cours : Suppression d'une table dans la base de donées");
		try {
			// Numéro de la table que l'on créé puis supprime pour le test
			int numero = incr();

			directeur.ajouterEtage();
			// Etage qui va recevoir une table pour être supprimée
			Etage etage = Restaurant.getEtages().get(Restaurant.getEtages().size() - 1);
			// On ajoute la table
			Table tableActuelle = directeur.ajouterTable(numero, 10, etage);
			// On supprime la table
			directeur.supprimerTable(tableActuelle, etage.getTables());
			ResultSet resultSet = sql
					.executerSelect("SELECT numero FROM restaurant.tables WHERE id=" + tableActuelle.getId());
			// On vérifie qu'aucune ligne n'est trouvée car l'id recherché a été supprimé
			assertFalse(resultSet.next());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Suppression d'une table dans la mémoire")
	public void supprimerTableJava() {
		System.out.println("\nTest en cours : Suppression d'une table dans la mémoire");
		// Numéro de la table que l'on créé puis supprime pour le test
		int numero = incr();
		directeur.ajouterEtage();
		// Etage qui va recevoir une table pour être supprimée
		Etage etage = Restaurant.getEtages().get(Restaurant.getEtages().size() - 1);
		// On ajoute la table
		directeur.ajouterTable(numero, 10, etage);
		int totalTablesAvantSuppression = etage.getTables().size();
		// La table créée et a supprimée
		Table tableActuelle = etage.getTables().get(etage.getTables().size() - 1);
		// On supprime la table
		directeur.supprimerTable(tableActuelle, etage.getTables());
		int totalTablesApresSuppression = etage.getTables().size();
		// On vérifie qu'aucune ligne n'est trouvée car l'id recherché a été supprimé
		assertTrue(totalTablesApresSuppression < totalTablesAvantSuppression);
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
			Type type = Type.Entree;
			Categorie categorie = Categorie.Poisson;
			String nomIngredientSaumon = "saumon";
			String nomIngredientToast = "tartine";
			int quantiteSaumon = 2;
			int quantiteTartine = 2;
			directeur.ajouterIngredient(nomIngredientSaumon);
			Ingredient saumon = Restaurant.getIngredients().get(Restaurant.getIngredients().size() - 1);
			directeur.ajouterIngredient(nomIngredientToast);
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
		String nomPlat = "Gros";
		Double prixPlat = 9.5;
		int tempsPrepa = 5;
		boolean surCarte = true;
		Type type = Type.Entree;
		Categorie categorie = Categorie.Poisson;
		String nomIngredientSaumon = "gras";
		String nomIngredientToast = "sauce";
		int quantiteSaumon = 2;
		int quantiteTartine = 2;
		directeur.ajouterIngredient(nomIngredientSaumon);
		Ingredient saumon = Restaurant.getIngredients().get(Restaurant.getIngredients().size() - 1);
		directeur.ajouterIngredient(nomIngredientToast);
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
			System.out.println("\nTest en cours : Modification du prix d'un plat dans la base de données");
			String nomPlat = "Soupe";
			Double prixPlat = 9.5;
			int tempsPrepa = 5;
			boolean surCarte = true;
			Type type = Type.Entree;
			Categorie categorie = Categorie.Poisson;
			String nomIngredientSaumon = "chou";
			String nomIngredientToast = "patate";
			int quantiteSaumon = 2;
			int quantiteTartine = 2;
			directeur.ajouterIngredient(nomIngredientSaumon);
			Ingredient saumon = Restaurant.getIngredients().get(Restaurant.getIngredients().size() - 1);
			directeur.ajouterIngredient(nomIngredientToast);
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
		System.out.println("\nTest en cours : Modification du prix d'un plat dans la mémoire");
		String nomPlat = "Soupe canibale";
		Double prixPlat = 9.5;
		int tempsPrepa = 5;
		boolean surCarte = true;
		Type type = Type.Entree;
		Categorie categorie = Categorie.Poisson;
		String nomIngredientSaumon = "poulet";
		String nomIngredientToast = "kangourou";
		int quantiteSaumon = 2;
		int quantiteTartine = 2;
		directeur.ajouterIngredient(nomIngredientSaumon);
		Ingredient saumon = Restaurant.getIngredients().get(Restaurant.getIngredients().size() - 1);
		directeur.ajouterIngredient(nomIngredientToast);
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
			System.out.println("\nTest en cours : Modification de la disponibilité d'un plat dans la base de données");
			String nomPlat = "Enfant";
			Double prixPlat = 9.5;
			int tempsPrepa = 5;
			boolean surCarte = true;
			Type type = Type.Entree;
			Categorie categorie = Categorie.Poisson;
			String nomIngredientSaumon = "knacki";
			String nomIngredientToast = "ketchup";
			int quantiteSaumon = 2;
			int quantiteTartine = 2;
			directeur.ajouterIngredient(nomIngredientSaumon);
			Ingredient saumon = Restaurant.getIngredients().get(Restaurant.getIngredients().size() - 1);
			directeur.ajouterIngredient(nomIngredientToast);
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
		System.out.println("\nTest en cours : Modification de la disponibilité d'un plat dans la mémoire");
		String nomPlat = "Salade";
		Double prixPlat = 9.5;
		int tempsPrepa = 5;
		boolean surCarte = true;
		Type type = Type.Entree;
		Categorie categorie = Categorie.Poisson;
		String nomIngredientSaumon = "laitue";
		String nomIngredientToast = "chèvre";
		int quantiteSaumon = 2;
		int quantiteTartine = 2;
		directeur.ajouterIngredient(nomIngredientSaumon);
		Ingredient saumon = Restaurant.getIngredients().get(Restaurant.getIngredients().size() - 1);
		directeur.ajouterIngredient(nomIngredientToast);
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
			System.out.println("\nTest en cours : Modification de la durée d'un plat dans la base de données");
			String nomPlat = "Paupiettes";
			Double prixPlat = 9.5;
			int tempsPrepa = 5;
			boolean surCarte = true;
			Type type = Type.Entree;
			Categorie categorie = Categorie.Poisson;
			String nomIngredientPorc = "porc";
			String nomIngredientEscalope = "escalope";
			int quantitePorc = 1;
			int quantiteEscalope = 1;
			directeur.ajouterIngredient(nomIngredientPorc);
			Ingredient saumon = Restaurant.getIngredients().get(Restaurant.getIngredients().size() - 1);
			directeur.ajouterIngredient(nomIngredientEscalope);
			Ingredient tartine = Restaurant.getIngredients().get(Restaurant.getIngredients().size() - 1);
			HashMap<Ingredient, Integer> recette = new HashMap<>();
			recette.put(saumon, quantitePorc);
			recette.put(tartine, quantiteEscalope);
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
		System.out.println("\nTest en cours : Modification de la durée d'un plat dans la mémoire");
		String nomPlat = "Lasagne";
		Double prixPlat = 9.5;
		int tempsPrepa = 5;
		boolean surCarte = true;
		Type type = Type.Entree;
		Categorie categorie = Categorie.Poisson;
		String nomIngredientPatelasagne = "patelasagne";
		String nomIngredientBoeuf = "boeuf";
		int quantitePatelasagne = 5;
		int quantiteBoeuf = 5;
		directeur.ajouterIngredient(nomIngredientPatelasagne);
		Ingredient patelasagne = Restaurant.getIngredients().get(Restaurant.getIngredients().size() - 1);
		directeur.ajouterIngredient(nomIngredientBoeuf);
		Ingredient boeuf = Restaurant.getIngredients().get(Restaurant.getIngredients().size() - 1);
		HashMap<Ingredient, Integer> recette = new HashMap<>();
		recette.put(patelasagne, quantitePatelasagne);
		recette.put(boeuf, quantiteBoeuf);
		Plat plat = directeur.creerPlat(nomPlat, prixPlat, tempsPrepa, surCarte, type, categorie, recette);
		directeur.modifierDureePlat(plat, 10);
		assertEquals(10, plat.getDureePreparation());
	}

	@Test
	@DisplayName("Modification de la durée d'un plat dans la base de données")
	public void suppressionPlatDB() {
		try {
			System.out.println("\nTest en cours : Modification de la durée d'un plat dans la base de données");
			String nomPlat = "Cordon bleu";
			Double prixPlat = 9.5;
			int tempsPrepa = 5;
			boolean surCarte = true;
			Type type = Type.Entree;
			Categorie categorie = Categorie.Poisson;
			String nomIngredientVeau = "veau";
			String nomIngredientFromage = "comté";
			int quantiteVeau = 2;
			int quantiteFromage = 2;
			directeur.ajouterIngredient(nomIngredientVeau);
			Ingredient saumon = Restaurant.getIngredients().get(Restaurant.getIngredients().size() - 1);
			directeur.ajouterIngredient(nomIngredientFromage);
			Ingredient tartine = Restaurant.getIngredients().get(Restaurant.getIngredients().size() - 1);
			HashMap<Ingredient, Integer> recette = new HashMap<>();
			recette.put(saumon, quantiteVeau);
			recette.put(tartine, quantiteFromage);
			Plat plat = directeur.creerPlat(nomPlat, prixPlat, tempsPrepa, surCarte, type, categorie, recette);
			directeur.supprimerPlat(plat);
			ResultSet resultSet = sql.executerSelect("SELECT id FROM restaurant.plat WHERE id=" + plat.getId());
			assertFalse(resultSet.next());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Modification de la durée d'un plat dans la mémoire")
	public void suppressionPlatJava() {
		System.out.println("\nTest en cours : Modification de la durée d'un plat dans la mémoire");
		String nomPlat = "Régime";
		Double prixPlat = 9.5;
		int tempsPrepa = 5;
		boolean surCarte = true;
		Type type = Type.Entree;
		Categorie categorie = Categorie.Poisson;
		String nomIngredientSaumon = "eau";
		String nomIngredientToast = "pain";
		int quantiteSaumon = 2;
		int quantiteTartine = 2;
		directeur.ajouterIngredient(nomIngredientSaumon);
		Ingredient saumon = Restaurant.getIngredients().get(Restaurant.getIngredients().size() - 1);
		directeur.ajouterIngredient(nomIngredientToast);
		Ingredient tartine = Restaurant.getIngredients().get(Restaurant.getIngredients().size() - 1);
		HashMap<Ingredient, Integer> recette = new HashMap<>();
		recette.put(saumon, quantiteSaumon);
		recette.put(tartine, quantiteTartine);
		Plat plat = directeur.creerPlat(nomPlat, prixPlat, tempsPrepa, surCarte, type, categorie, recette);
		directeur.supprimerPlat(plat);
		assertFalse(Restaurant.getPlats().contains(plat));
	}

	@Test
	@DisplayName("Récuperation plus petite table")
	public void getTableMini() {
		System.out.println("\nTest en cours : Récuperation plus petite table");
		try {
			int numero = incr();
			int numero2 = incr();
			int numero3 = incr();
			int numero4 = incr();
			int numero5 = incr();
			int numero6 = incr();
			int numero7 = incr();
			int numero8 = incr();
			int numero9 = incr();
			int numero10 = incr();
			directeur.ajouterEtage();
			Etage etage = Restaurant.getEtages().get(Restaurant.getEtages().size() - 1);
			directeur.ajouterEtage();
			Etage etage2 = Restaurant.getEtages().get(Restaurant.getEtages().size() - 1);
			directeur.ajouterTable(numero, 2, etage);
			directeur.ajouterTable(numero2, 3, etage2);
			directeur.ajouterTable(numero3, 4, etage);
			directeur.ajouterTable(numero4, 4, etage2);
			directeur.ajouterTable(numero5, 2, etage);
			directeur.ajouterTable(numero6, 3, etage);
			directeur.ajouterTable(numero7, 2, etage);
			directeur.ajouterTable(numero8, 1, etage);
			directeur.ajouterTable(numero9, 10, etage);
			directeur.ajouterTable(numero10, 8, etage);
			String dateReservation = "20/06/2021 12:00:00";
			Date date1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dateReservation);
			Date dateReservationSQL = new Timestamp(date1.getTime());
			String dateAppel = "20/06/2020 12:00:00";
			Date date2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dateAppel);
			Date dateAppelSQL = new Timestamp(date2.getTime());
			Reservation res = directeur.creationReservation(dateAppelSQL, dateReservationSQL, 4);
			Reservation res2 = directeur.creationReservation(dateAppelSQL, dateReservationSQL, 2);
			Reservation res3 = directeur.creationReservation(dateAppelSQL, dateReservationSQL, 3);
			Reservation res4 = directeur.creationReservation(dateAppelSQL, dateReservationSQL, 2);
			Reservation res5 = directeur.creationReservation(dateAppelSQL, dateReservationSQL, 2);
			Reservation res6 = directeur.creationReservation(dateAppelSQL, dateReservationSQL, 4);
			Reservation res7 = directeur.creationReservation(dateAppelSQL, dateReservationSQL, 2);
			System.out.println(res.getTable());
			System.out.println(res2.getTable());
			System.out.println(res3.getTable());
			System.out.println(res4.getTable());
			System.out.println(res5.getTable());
			System.out.println(res6.getTable());
			System.out.println(res7.getTable());
			assertEquals(numero2, res7.getTable().getNumero());
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Création d'une affectation dans la base de données")
	public void creationAffectationDB() {
		System.out.println("\nTest en cours : Création d'une affectation dans la base de données");
		try {
			// Prérequis du test : mise en place d'un étage et d'une table
			// Numéro de la table que l'on créé
			int numero = incr();
			directeur.ajouterEtage();
			// Etage qui va recevoir une table
			Etage etage = Restaurant.getEtages().get(Restaurant.getEtages().size() - 1);
			// On ajoute la table
			Table tableActuelle = directeur.ajouterTable(numero, 10, etage);

			// Création de l'affectation (date immédiate)
			Affectation affectation = directeur.creationAffectation(new Timestamp(new Date().getTime()), 2);
			ResultSet resultSet = sql
					.executerSelect("SELECT id FROM restaurant.affectation WHERE id=" + affectation.getId());
			// On vérifie qu'une ligne a bien été créé
			assertTrue(resultSet.next());
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Test
	@DisplayName("Création d'une affectation dans la mémoire")
	public void creationAffectationJava() {
		System.out.println("\nTest en cours : Création d'une affectation dans la mémoire");
		// Prérequis du test : mise en place d'un étage et d'une table
		// Numéro de la table que l'on créé
		int numero = incr();
		directeur.ajouterEtage();
		// Etage qui va recevoir une table
		Etage etage = Restaurant.getEtages().get(Restaurant.getEtages().size() - 1);
		// On ajoute la table
		Table tableActuelle = directeur.ajouterTable(numero, 10, etage);

		int tailleAvant = Restaurant.getAffectationsJour().size();
		// Création de l'affectation (date immédiate)
		directeur.creationAffectation(new Timestamp(new Date().getTime()), 2);
		int tailleApres = Restaurant.getAffectationsJour().size();
		// On vérifie qu'une ligne a bien été créé
		assertTrue(tailleAvant < tailleApres);
	}

	@Test
	@DisplayName("Ajout date de fin d'une affectation dans la base de donnée")
	public void dateFinAffectationDB() {
		System.out.println("\nTest en cours : Ajout date de fin d'une affectation dans la base de donnée");
		// Prérequis du test : mise en place d'un étage et d'une table et d'une
		// affectation
		// Numéro de la table que l'on créé
		try {
			int numero = incr();
			directeur.ajouterEtage();
			// Etage qui va recevoir une table
			Etage etage = Restaurant.getEtages().get(Restaurant.getEtages().size() - 1);
			// On ajoute la table
			directeur.ajouterTable(numero, 10, etage);
			// La table créée
			Table tableActuelle = etage.getTables().get(etage.getTables().size() - 1);
			// Création de l'affectation (date immédiate)
			Date debut = new Timestamp(new Date().getTime());
			Affectation affectation = directeur.creationAffectation(debut, 2);

			// Attente de 0.5 secondes
			Thread.sleep(500);
			Date dateFin = new Timestamp(new Date().getTime());
			directeur.dateFinAffectation(affectation, dateFin);
			ResultSet resultSet = sql
					.executerSelect("SELECT datefin FROM restaurant.affectation WHERE id=" + affectation.getId());
			// check datefin not null
			resultSet.next();
			assertNotNull(resultSet.getDate("datefin"));
		} catch (SQLException | InterruptedException e) {
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
			int numero = incr();
			directeur.ajouterEtage();
			// Etage qui va recevoir une table
			Etage etage = Restaurant.getEtages().get(Restaurant.getEtages().size() - 1);
			// On ajoute la table
			directeur.ajouterTable(numero, 10, etage);
			// La table créée
			Table tableActuelle = etage.getTables().get(etage.getTables().size() - 1);
			// Création de l'affectation (date immédiate)
			Date debut = new Timestamp(new Date().getTime());
			Affectation affectation = directeur.creationAffectation(debut, 2);

			// Attente de 0.5 secondes
			Thread.sleep(500);
			Date dateFin = new Timestamp(new Date().getTime());
			directeur.dateFinAffectation(affectation, dateFin);
			assertNotNull(affectation.getDateFin());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Création d'une affectation dans la base de données")
	public void creationReservationDB() {
		System.out.println("\nTest en cours : Création d'une affectation dans la base de données");
		try {
			// Prérequis du test : mise en place d'un étage et d'une table
			int numero = incr();
			directeur.ajouterEtage();
			// Etage qui va recevoir une table
			Etage etage = Restaurant.getEtages().get(Restaurant.getEtages().size() - 1);
			// On ajoute la table
			Table tableActuelle = directeur.ajouterTable(numero, 10, etage);

			// La date de l'appel est immédiate
			Date dateAppel = new Timestamp(new Date().getTime());
			// Date demandée par le client
			String dateReservation = "27/12/1995 22:55:00";
			Date date1;
			date1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dateReservation);
			Date dateReservationSQL = new Timestamp(date1.getTime());
			// Création de la réservation
			Reservation reservation = directeur.creationReservation(dateAppel, dateReservationSQL, 5);
			ResultSet resultSet = sql
					.executerSelect("SELECT id FROM restaurant.reservation WHERE id=" + reservation.getId());
			assertTrue(resultSet.next());
		} catch (SQLException | ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Création d'une affectation dans la mémoire")
	public void creationReservationJava() {
		System.out.println("\nTest en cours : Création d'une affectation dans la mémoire");
		try {
			// Prérequis du test : mise en place d'un étage et d'une table
			int numero = incr();
			directeur.ajouterEtage();
			// Etage qui va recevoir une table
			Etage etage = Restaurant.getEtages().get(Restaurant.getEtages().size() - 1);
			// On ajoute la table
			Table tableActuelle = directeur.ajouterTable(numero, 10, etage);

			int nombreReservationsAvant = Restaurant.getReservationsJour().size();

			// La date de l'appel est immédiate
			Date dateAppel = new Timestamp(new Date().getTime());
			// Date demandée par le client
			String dateReservation = "27/12/1992 22:55:00";
			Date date1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dateReservation);
			Date dateReservationSQL = new Timestamp(date1.getTime());
			// Création de la réservation
			directeur.creationReservation(dateAppel, dateReservationSQL, 5);
			int nombreReservationsApres = Restaurant.getReservationsJour().size();
			assertTrue(nombreReservationsAvant < nombreReservationsApres);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Suppression d'une réservation dans la base de donées")
	public void supprimerReservationDB() {
		System.out.println("\nTest en cours : Suppression d'une réservation dans la base de donées");
		try {
			int numero = incr();
			directeur.ajouterEtage();
			// Etage qui va recevoir une table
			Etage etage = Restaurant.getEtages().get(Restaurant.getEtages().size() - 1);
			// On ajoute la table
			directeur.ajouterTable(numero, 10, etage);
			// La table créée
			Table tableActuelle = etage.getTables().get(etage.getTables().size() - 1);
			// La date de l'appel est immédiate
			Date dateAppel = new Timestamp(new Date().getTime());
			// Date demandée par le client
			String dateReservation = "27/12/1992 22:55:00";
			Date date1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dateReservation);
			Date dateReservationSQL = new Timestamp(date1.getTime());
			// Création de la réservation
			Reservation asuppr = directeur.creationReservation(dateAppel, dateReservationSQL, 5);

			// On supprime la table
			directeur.supprimerReservation(asuppr);
			ResultSet resultSet = sql
					.executerSelect("SELECT id FROM restaurant.reservation WHERE id=" + asuppr.getId());
			// On vérifie qu'aucune ligne n'est trouvée car l'id recherché a été supprimé
			assertFalse(resultSet.next());
		} catch (SQLException | ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Suppression d'une réservation dans la mémoire")
	public void supprimerReservationJava() {
		System.out.println("\nTest en cours : Suppression d'une réservation dans la mémoire");
		try {
			int numero = incr();
			directeur.ajouterEtage();
			// Etage qui va recevoir une table
			Etage etage = Restaurant.getEtages().get(Restaurant.getEtages().size() - 1);
			// On ajoute la table
			Table tableActuelle = directeur.ajouterTable(numero, 10, etage);
			// La date de l'appel est immédiate
			Date dateAppel = new Timestamp(new Date().getTime());
			// Date demandée par le client
			String dateReservation = "27/12/1992 22:55:00";
			Date date1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dateReservation);
			Date dateReservationSQL = new Timestamp(date1.getTime());
			// Création de la réservation
			Reservation asuppr = directeur.creationReservation(dateAppel, dateReservationSQL, 5);
			int nbReservationAvant = Restaurant.getReservationsJour().size();
			// On supprime la table
			directeur.supprimerReservation(asuppr);
			int nbReservationApres = Restaurant.getReservationsJour().size();
			assertTrue(nbReservationApres < nbReservationAvant);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Annulation d'une réservation dans la mémoire")
	public void annulerReservationJava() {
		System.out.println("\nTest en cours : Annulation d'une réservation dans la mémoire");
		try {
			int numero = incr();
			directeur.ajouterEtage();
			// Etage qui va recevoir une table
			Etage etage = Restaurant.getEtages().get(Restaurant.getEtages().size() - 1);
			// On ajoute la table
			Table tableActuelle = directeur.ajouterTable(numero, 10, etage);
			// La date de l'appel est immédiate
			Date dateAppel = new Timestamp(new Date().getTime());
			// Date demandée par le client
			String dateReservation = "27/12/1992 22:55:00";
			Date date1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dateReservation);
			Date dateReservationSQL = new Timestamp(date1.getTime());
			// Création de la réservation
			Reservation asuppr = directeur.creationReservation(dateAppel, dateReservationSQL, 5);

			// Annulation de la réservation
			directeur.annulerReservation(asuppr);
			assertFalse(asuppr.isEffetive());
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Annulation d'une réservation dans la base de données")
	public void annulerReservationDB() {
		System.out.println("\nTest en cours : Annulation d'une réservation dans la base de données");
		try {
			int numero = incr();
			directeur.ajouterEtage();
			// Etage qui va recevoir une table
			Etage etage = Restaurant.getEtages().get(Restaurant.getEtages().size() - 1);
			// On ajoute la table
			directeur.ajouterTable(numero, 10, etage);
			// La date de l'appel est immédiate
			Date dateAppel = new Timestamp(new Date().getTime());
			// Date demandée par le client
			String dateReservation = "27/12/1992 22:55:00";
			Date date1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dateReservation);
			Date dateReservationSQL = new Timestamp(date1.getTime());
			// Création de la réservation
			Reservation asuppr = directeur.creationReservation(dateAppel, dateReservationSQL, 5);

			// Annulation de la réservation
			directeur.annulerReservation(asuppr);

			ResultSet resultset = sql
					.executerSelect("SELECT valide FROM restaurant.reservation WHERE id=" + asuppr.getId());
			resultset.next();
			assertFalse(resultset.getBoolean("valide"));
		} catch (ParseException | SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Vérification de l'initialisation des horaires dans la base de données")
	public void verifierInitialisationHorairesDB() {
		System.out.println("\nTest en cours : Vérification de l'initialisation des horaires dans la base de données");
		try {
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
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Vérification de l'initialisation des horaires dans la mémoire")
	public void verifierInitialisationHorairesJava() {
		System.out.println("\nTest en cours : Vérification de l'initialisation des horaires dans la mémoire");
		try {
			// Récupération des valeurs par défaut
			Properties prop = new Properties();
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(this.propertiesFilename);
			prop.load(inputStream);
			int heureDejeunerOuverture = Integer.parseInt(prop.getProperty("default.heureDejeunerOuverture"));
			assertEquals(LocalTime.ofSecondOfDay(heureDejeunerOuverture), Restaurant.getHeureDejeunerOuverture());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Modification d'un horaire du restaurant dans la mémoire")
	public void modificationHoraireJava() {
		System.out.println("\nTest en cours : Modification d'un horaire du restaurant dans la mémoire");
		int heure = 13;
		int minute = 11;
		int nbSecondes = LocalTime.of(heure, minute).getSecond();
		directeur.modifierHoraire("ouverture midi", nbSecondes);
		System.err.println(LocalTime.of(heure, minute).getSecond());
		System.err.println(Restaurant.getHeureDejeunerOuverture());
		assertEquals(nbSecondes, Restaurant.getHeureDejeunerOuverture().getSecond());
	}

	@Test
	@DisplayName("Modification d'un horaire du restaurant dans la base de données")
	public void modificationHoraireDB() {
		System.out.println("\nTest en cours : Modification d'un horaire du restaurant dans la base de données");
		try {
			int heure = 21;
			int minute = 11;
			int nbSecondes = LocalTime.of(heure, minute).getSecond();
			directeur.modifierHoraire("ouverture soir", nbSecondes);
			System.err.println(LocalTime.of(heure, minute).getSecond());
			System.err.println(Restaurant.getHeureDejeunerOuverture());
			ResultSet resultset = sql.executerSelect("SELECT heureouverturediner FROM restaurant.restaurant");
			resultset.next();
			assertEquals(nbSecondes, resultset.getInt("heureouverturediner"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Création d'une commande dans la base de données")
	public void creationCommandeDB() {
		System.out.println("\nTest en cours : Création d'une commande dans la base de données");
		try {
			// Prérequis : étage, table, plat et affectation
			int numero = incr();
			directeur.ajouterEtage();
			// Etage qui va recevoir une table
			Etage etage = Restaurant.getEtages().get(Restaurant.getEtages().size() - 1);
			// On ajoute la table
			Table tableActuelle = directeur.ajouterTable(numero, 10, etage);
			// Création de l'affectation (date immédiate)
			Affectation affectation = directeur.creationAffectation(new Timestamp(new Date().getTime()), 2);
			Date dateCommande = new Timestamp(new Date().getTime());
			// Création du plat
			String nomPlat = "Sanglier et frites";
			Double prixPlat = 9.5;
			int tempsPrepa = 5;
			boolean surCarte = true;
			Type type = Type.Entree;
			Categorie categorie = Categorie.Poisson;
			String nomIngredientSaumon = "sanglier";
			String nomIngredientToast = "firte";
			int quantiteSaumon = 1;
			int quantiteTartine = 10;
			directeur.ajouterIngredient(nomIngredientSaumon);
			Ingredient saumon = Restaurant.getIngredients().get(Restaurant.getIngredients().size() - 1);
			directeur.ajouterIngredient(nomIngredientToast);
			Ingredient tartine = Restaurant.getIngredients().get(Restaurant.getIngredients().size() - 1);
			HashMap<Ingredient, Integer> recette = new HashMap<>();
			recette.put(saumon, quantiteSaumon);
			recette.put(tartine, quantiteTartine);
			Plat plat = directeur.creerPlat(nomPlat, prixPlat, tempsPrepa, surCarte, type, categorie, recette);
			boolean estEnfant = true;
			Commande commande = directeur.creationCommande(dateCommande, plat, estEnfant, affectation);

			ResultSet resultSet = sql.executerSelect("SELECT * FROM restaurant.commande WHERE id=" + commande.getId());
			assertTrue(resultSet.next());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Création d'une commande dans la mémoire")
	public void creationCommandeJava() {
		System.out.println("\nTest en cours : Création d'une commande dans la mémoire");
		// Prérequis : étage, table, plat et affectation
		int numero = incr();
		// Etage qui va recevoir une table
		Etage etage = directeur.ajouterEtage();
		// On ajoute la table
		Table tableActuelle = directeur.ajouterTable(numero, 10, etage);
		// Création de l'affectation (date immédiate)
		Affectation affectation = directeur.creationAffectation(new Timestamp(new Date().getTime()), 2);
		Date dateCommande = new Timestamp(new Date().getTime());
		// Création du plat
		String nomPlat = "Crododile mayo";
		Double prixPlat = 9.5;
		int tempsPrepa = 5;
		boolean surCarte = true;
		Type type = Type.Entree;
		Categorie categorie = Categorie.Poisson;
		Ingredient ingredient1 = directeur.ajouterIngredient("crocodile");
		Ingredient ingredient2 = directeur.ajouterIngredient("mayonnaise");
		HashMap<Ingredient, Integer> recette = new HashMap<>();
		recette.put(ingredient1, 5);
		recette.put(ingredient2, 7);
		Plat plat = directeur.creerPlat(nomPlat, prixPlat, tempsPrepa, surCarte, type, categorie, recette);
		boolean estEnfant = true;
		directeur.commanderIngredient(ingredient1, 50);
		directeur.commanderIngredient(ingredient2, 50);
		int nbCommandeAvant = 0;
		directeur.creationCommande(dateCommande, plat, estEnfant, affectation);
		int nbCommandeApres = Restaurant.getCommandes().size();
		assertTrue(nbCommandeAvant < nbCommandeApres);
	}

	@Test
	@DisplayName("Modification de l'etat d'une commande dans la base de données")
	public void modifierEtatCommandeDB() {
		System.out.println("\nTest en cours : Modification de l'etat d'une commande dans la base de données");
		try {
			// Prérequis : étage, table, plat et affectation
			int numero = incr();
			directeur.ajouterEtage();
			// Etage qui va recevoir une table
			Etage etage = Restaurant.getEtages().get(Restaurant.getEtages().size() - 1);
			// On ajoute la table
			Table tableActuelle = directeur.ajouterTable(numero, 10, etage);
			// Création de l'affectation (date immédiate)
			Affectation affectation = directeur.creationAffectation(new Timestamp(new Date().getTime()), 2);
			Date dateCommande = new Timestamp(new Date().getTime());
			// Création du plat
			String nomPlat = "Pot au feu";
			Double prixPlat = 9.5;
			int tempsPrepa = 5;
			boolean surCarte = true;
			Type type = Type.Entree;
			Categorie categorie = Categorie.Poisson;
			String nomIngredientSaumon = "joue";
			String nomIngredientToast = "navet";
			int quantiteSaumon = 1;
			int quantiteTartine = 10;
			directeur.ajouterIngredient(nomIngredientSaumon);
			Ingredient saumon = Restaurant.getIngredients().get(Restaurant.getIngredients().size() - 1);
			directeur.ajouterIngredient(nomIngredientToast);
			Ingredient tartine = Restaurant.getIngredients().get(Restaurant.getIngredients().size() - 1);
			HashMap<Ingredient, Integer> recette = new HashMap<>();
			recette.put(saumon, quantiteSaumon);
			recette.put(tartine, quantiteTartine);
			Plat plat = directeur.creerPlat(nomPlat, prixPlat, tempsPrepa, surCarte, type, categorie, recette);
			boolean estEnfant = true;
			Commande commande = directeur.creationCommande(dateCommande, plat, estEnfant, affectation);
			directeur.modifierEtatCommande(commande, Etat.SERVIE);
			ResultSet resultSet = sql
					.executerSelect("SELECT etat FROM restaurant.commande WHERE id=" + commande.getId());
			resultSet.next();
			assertEquals("SERVIE", resultSet.getString("etat"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Modification de l'etat d'une commande dans la mémoire")
	public void modifierEtatCommandeJava() {
		System.out.println("\nTest en cours : Modification de l'etat d'une commande dans la mémoire");
		// Prérequis : étage, table, plat et affectation
		int numero = incr();
		// Etage qui va recevoir une table
		Etage etage = directeur.ajouterEtage();
		// On ajoute la table
		Table tableActuelle = directeur.ajouterTable(numero, 10, etage);
		// Création de l'affectation (date immédiate)
		Affectation affectation = directeur.creationAffectation(new Timestamp(new Date().getTime()), 2);
		Date dateCommande = new Timestamp(new Date().getTime());
		// Création du plat
		String nomPlat = "Sardines";
		Double prixPlat = 9.5;
		int tempsPrepa = 5;
		boolean surCarte = true;
		Type type = Type.Entree;
		Categorie categorie = Categorie.Poisson;
		Ingredient ingredient1 = directeur.ajouterIngredient("sardine");
		Ingredient ingredient2 = directeur.ajouterIngredient("huile");
		HashMap<Ingredient, Integer> recette = new HashMap<>();
		recette.put(ingredient1, 5);
		recette.put(ingredient2, 7);
		Plat plat = directeur.creerPlat(nomPlat, prixPlat, tempsPrepa, surCarte, type, categorie, recette);
		boolean estEnfant = true;
		directeur.commanderIngredient(ingredient1, 50);
		directeur.commanderIngredient(ingredient2, 50);
		int nbCommandeAvant = 0;
		Commande commande = directeur.creationCommande(dateCommande, plat, estEnfant, affectation);
		directeur.modifierEtatCommande(commande, Etat.SERVIE);
		assertEquals(Etat.SERVIE, commande.getEtat());
	}

	@Test
	@DisplayName("Création d'une facture dans la base de données")
	public void creerFactureDB() {
		System.out.println("\nTest en cours : Création d'une facture dans la base de données");
		try {
			// Prérequis : étage, table, plat et affectation
			int numero = incr();
			directeur.ajouterEtage();
			// Etage qui va recevoir une table
			Etage etage = Restaurant.getEtages().get(Restaurant.getEtages().size() - 1);
			// On ajoute la table
			Table tableActuelle = directeur.ajouterTable(numero, 10, etage);
			// Création de l'affectation (date immédiate)
			Affectation affectation = directeur.creationAffectation(new Timestamp(new Date().getTime()), 2);
			Date dateCommande = new Timestamp(new Date().getTime());
			// Création du plat
			String nomPlat = "Gigot";
			Double prixPlat = 9.5;
			int tempsPrepa = 5;
			boolean surCarte = true;
			Type type = Type.Entree;
			Categorie categorie = Categorie.Poisson;
			String nomIngredientSaumon = "agneau";
			String nomIngredientToast = "haricots";
			int quantiteSaumon = 1;
			int quantiteTartine = 10;
			directeur.ajouterIngredient(nomIngredientSaumon);
			Ingredient saumon = Restaurant.getIngredients().get(Restaurant.getIngredients().size() - 1);
			directeur.ajouterIngredient(nomIngredientToast);
			Ingredient tartine = Restaurant.getIngredients().get(Restaurant.getIngredients().size() - 1);
			HashMap<Ingredient, Integer> recette = new HashMap<>();
			recette.put(saumon, quantiteSaumon);
			recette.put(tartine, quantiteTartine);
			Plat plat = directeur.creerPlat(nomPlat, prixPlat, tempsPrepa, surCarte, type, categorie, recette);
			boolean estEnfant = true;
			directeur.creationCommande(dateCommande, plat, estEnfant, affectation);
			directeur.creationCommande(dateCommande, plat, estEnfant, affectation);
			directeur.creationCommande(dateCommande, plat, estEnfant, affectation);
			directeur.creerFacture(affectation);
			ResultSet resultSet = sql
					.executerSelect("SELECT facture FROM restaurant.affectation WHERE id=" + affectation.getId());
			resultSet.next();
			assertEquals(prixPlat * 3, resultSet.getDouble("facture"), 0.001);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Création d'une facture dans la mémoire")
	public void creerFactureJava() {
		System.out.println("\nTest en cours : Création d'une facture dans la mémoire");
		// Prérequis : étage, table, plat et affectation
		int numero = incr();
		// Etage qui va recevoir une table
		Etage etage = directeur.ajouterEtage();
		// On ajoute la table
		Table tableActuelle = directeur.ajouterTable(numero, 10, etage);
		// Création de l'affectation (date immédiate)
		Affectation affectation = directeur.creationAffectation(new Timestamp(new Date().getTime()), 2);
		Date dateCommande = new Timestamp(new Date().getTime());
		// Création du plat
		String nomPlat = "Cassoulet";
		Double prixPlat = 9.5;
		int tempsPrepa = 5;
		boolean surCarte = true;
		Type type = Type.Entree;
		Categorie categorie = Categorie.Poisson;
		Ingredient ingredient1 = directeur.ajouterIngredient("saussice");
		Ingredient ingredient2 = directeur.ajouterIngredient("flageolets");
		HashMap<Ingredient, Integer> recette = new HashMap<>();
		recette.put(ingredient1, 5);
		recette.put(ingredient2, 7);
		Plat plat = directeur.creerPlat(nomPlat, prixPlat, tempsPrepa, surCarte, type, categorie, recette);
		boolean estEnfant = true;
		directeur.creationCommande(dateCommande, plat, estEnfant, affectation);
		directeur.creationCommande(dateCommande, plat, estEnfant, affectation);
		directeur.creationCommande(dateCommande, plat, estEnfant, affectation);
		directeur.creerFacture(affectation);
		assertEquals(prixPlat * 3, affectation.getFacture(), 0.001);
	}

	@Test
	@DisplayName("Baisse de stock au passage d'une commande dans la base de données")
	public void baisseStockCommandeDB() {
		System.out.println("\nTest en cours : Baisse de stock au passage d'une commande dans la base de données");
		try {
			// Prérequis : étage, table, plat et affectation
			int numero = incr();
			directeur.ajouterEtage();
			// Etage qui va recevoir une table
			Etage etage = Restaurant.getEtages().get(Restaurant.getEtages().size() - 1);
			// On ajoute la table
			Table tableActuelle = directeur.ajouterTable(numero, 10, etage);
			// Création de l'affectation (date immédiate)
			Affectation affectation = directeur.creationAffectation(new Timestamp(new Date().getTime()), 2);
			Date dateCommande = new Timestamp(new Date().getTime());
			// Création du plat
			String nomPlat = "Rat & radis";
			Double prixPlat = 9.5;
			int tempsPrepa = 5;
			boolean surCarte = true;
			Type type = Type.Dessert;
			Categorie categorie = Categorie.Sucre;
			Ingredient ingredient1 = directeur.ajouterIngredient("rat");
			Ingredient ingredient2 = directeur.ajouterIngredient("radis");
			HashMap<Ingredient, Integer> recette = new HashMap<>();
			recette.put(ingredient1, 5);
			recette.put(ingredient2, 3);
			Plat plat = directeur.creerPlat(nomPlat, prixPlat, tempsPrepa, surCarte, type, categorie, recette);
			boolean estEnfant = true;
			directeur.commanderIngredient(ingredient1, 50);
			directeur.commanderIngredient(ingredient2, 50);
			ResultSet resultSetAvant = sql
					.executerSelect("SELECT quantite FROM restaurant.ingredient WHERE id=" + ingredient1.getId());
			resultSetAvant.next();
			int quantiteAvantCommande = resultSetAvant.getInt("quantite");
			directeur.creationCommande(dateCommande, plat, estEnfant, affectation);
			ResultSet resultSetApres = sql
					.executerSelect("SELECT quantite FROM restaurant.ingredient WHERE id=" + ingredient1.getId());
			resultSetApres.next();
			int quantiteApresCommande = resultSetApres.getInt("quantite");
			assertTrue(quantiteAvantCommande > quantiteApresCommande);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Baisse de stock au passage d'une commande dans la mémoire")
	public void baisseStockCommandeJava() {
		System.out.println("\nTest en cours : Baisse de stock au passage d'une commande dans la mémoire");
		// Prérequis : étage, table, plat et affectation
		int numero = incr();
		directeur.ajouterEtage();
		// Etage qui va recevoir une table
		Etage etage = Restaurant.getEtages().get(Restaurant.getEtages().size() - 1);
		// On ajoute la table
		Table tableActuelle = directeur.ajouterTable(numero, 10, etage);
		// Création de l'affectation (date immédiate)
		Affectation affectation = directeur.creationAffectation(new Timestamp(new Date().getTime()), 2);
		Date dateCommande = new Timestamp(new Date().getTime());
		// Création du plat
		String nomPlat = "Anaconda & sel";
		Double prixPlat = 9.5;
		int tempsPrepa = 5;
		boolean surCarte = true;
		Type type = Type.Dessert;
		Categorie categorie = Categorie.Sucre;
		Ingredient ingredient1 = directeur.ajouterIngredient("anaconda");
		Ingredient ingredient2 = directeur.ajouterIngredient("sel");
		HashMap<Ingredient, Integer> recette = new HashMap<>();
		recette.put(ingredient1, 5);
		recette.put(ingredient2, 3);
		Plat plat = directeur.creerPlat(nomPlat, prixPlat, tempsPrepa, surCarte, type, categorie, recette);
		boolean estEnfant = true;
		directeur.commanderIngredient(ingredient1, 50);
		directeur.commanderIngredient(ingredient2, 50);
		int quantiteAvantCommande = ingredient1.getQuantite();
		directeur.creationCommande(dateCommande, plat, estEnfant, affectation);
		int quantiteApresCommande = ingredient1.getQuantite();
		assertTrue(quantiteAvantCommande > quantiteApresCommande);
	}

	@Test
	@DisplayName("Suppression d'un serveur (ayant plusieurs tables) dans la base de données")
	public void suppressionPersonnelServeurDB() throws SQLException {
		System.out
				.println("\nTest en cours : Suppression d'un serveur (ayant plusieurs tables) dans la base de données");
		Etage etage1 = directeur.ajouterEtage();
		int numero1 = incr();
		Table table1 = directeur.ajouterTable(numero1, 10, etage1);
		int numero2 = incr();
		Table table2 = directeur.ajouterTable(numero2, 10, etage1);
		int numero3 = incr();
		Table table3 = directeur.ajouterTable(numero3, 10, etage1);
		Etage etage2 = directeur.ajouterEtage();
		int numero4 = incr();
		Table table4 = directeur.ajouterTable(numero4, 10, etage2);
		Serveur serveur = (Serveur) directeur.ajouterPersonnel("Jordan", "serveur");
		directeur.affecterTableServeur(serveur, table1);
		directeur.affecterTableServeur(serveur, table2);
		directeur.affecterTableServeur(serveur, table3);
		directeur.affecterTableServeur(serveur, table4);
		directeur.supprimerPersonnel(serveur, Restaurant.getPersonnel());
		ResultSet rs = sql.executerSelect("SELECT serveur FROM restaurant.tables WHERE id = " + table4.getId());
		rs.next();
		assertEquals(0, rs.getInt("serveur"));
	}

	@Test
	@DisplayName("Suppression d'un serveur (ayant plusieurs tables) dans la mémoire")
	public void suppressionPersonnelServeurJava() {
		System.out.println("\nTest en cours : Suppression d'un serveur (ayant plusieurs tables) dans la mémoire");
		Etage etage1 = directeur.ajouterEtage();
		int numero1 = incr();
		Table table1 = directeur.ajouterTable(numero1, 10, etage1);
		int numero2 = incr();
		Table table2 = directeur.ajouterTable(numero2, 10, etage1);
		int numero3 = incr();
		Table table3 = directeur.ajouterTable(numero3, 10, etage1);
		Etage etage2 = directeur.ajouterEtage();
		int numero4 = incr();
		Table table4 = directeur.ajouterTable(numero4, 10, etage2);
		Serveur serveur = (Serveur) directeur.ajouterPersonnel("Julien", "serveur");
		directeur.affecterTableServeur(serveur, table1);
		directeur.affecterTableServeur(serveur, table2);
		directeur.affecterTableServeur(serveur, table3);
		directeur.affecterTableServeur(serveur, table4);
		directeur.supprimerPersonnel(serveur, Restaurant.getPersonnel());
		assertNull(table4.getServeur());
	}

}