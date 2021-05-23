package restaurant;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class Sql {

	private Connection c = null;
	private Statement stmt = null;
	public final String propertiesFilename = "database.properties";
	private Properties prop = new Properties();

	public static final String requete_insertion_personne = "INSERT INTO restaurant.personne (nom,login) VALUES ('%s','%s')";
	public static final String requete_insertion_personneRole = "INSERT INTO restaurant.%s (personne) VALUES ('%s')";
	// V�rifie si le login existe lors de la connexion : renvoie 1 si vrai, 0 sinon
	// (en sachant qu'il n'y a pas de doublons)
	public static final String requete_login_existe = "SELECT COUNT(p.id) as existe FROM restaurant.personne p WHERE p.login = '%s'";
	// Revenu hebdomadaire
	/*
	 * SELECT SUM(plt.prix) FROM restaurant.commande cmd LEFT JOIN
	 * restaurant.affectation aff ON aff.id = cmd.affectation LEFT JOIN
	 * restaurant.plat plt ON plt.id = cmd.plat WHERE YEAR(aff.datefin) =
	 * YEAR(NOW()) AND MONTH(aff.datefin) = MONTH(NOW()) AND WEEK(aff.datefin)) =
	 * WEEK(NOW())
	 */

	// Revenu quotidien
	/*
	 * SELECT SUM(plt.prix) FROM restaurant.commande cmd LEFT JOIN
	 * restaurant.affectation aff ON aff.id = cmd.affectation LEFT JOIN
	 * restaurant.plat plt ON plt.id = cmd.plat WHERE YEAR(aff.datefin) =
	 * YEAR(NOW()) AND MONTH(aff.datefin) = MONTH(NOW()) AND DAY(aff.datefin) =
	 * DAY(NOW())
	 */

	// Revenu mensuel
	/*
	 * SELECT SUM(plt.prix) FROM restaurant.commande cmd LEFT JOIN
	 * restaurant.affectation aff ON aff.id = cmd.affectation LEFT JOIN
	 * restaurant.plat plt ON plt.id = cmd.plat WHERE YEAR(aff.datefin) =
	 * YEAR(NOW()) AND MONTH(aff.datefin) = MONTH(NOW())
	 */

	// Revenu hebdomadaire
	// Revenu quotidien
	// Revenu mensuel
	// Temps de preparation moyen
	public static final String requete_temps_prepare_moyen = "SELECT SUM(p.dureePreparation)/COUNT(c.id) AS tempsPrepaMoyen\r\n"
			+ "FROM restaurant.commande c\r\n" + "LEFT JOIN restaurant.plat p ON c.plat = p.id";

	// Temps de preparation moyen
	/*
	 * SELECT SUM(p.dureePreparation)/COUNT(c.id) AS tempsPrepaMoyen FROM
	 * restaurant.commande c LEFT JOIN restaurant.plat p ON c.plat = p.id
	 */

	// Temps moyen par client

	// Profit dejeuner
	/*
	 * SELECT SUM(plt.prix) FROM restaurant.commande cmd LEFT JOIN
	 * restaurant.affectation aff ON aff.id = cmd.affectation LEFT JOIN
	 * restaurant.plat plt ON plt.id = cmd.plat WHERE YEAR(aff.datefin) =
	 * YEAR(NOW()) AND MONTH(aff.datefin) = MONTH(NOW()) AND DAY(aff.datefin) =
	 * DAY(NOW()) AND HOUR(aff.datefin) >
	 * restaurant.restaurant.heureouverturedejeune AND HOUR(aff.datefin) <=
	 * restaurant.restaurant.heurelimitedejeune
	 */

	// Profit diner
	/*
	 * SELECT SUM(plt.prix) FROM restaurant.commande cmd LEFT JOIN
	 * restaurant.affectation aff ON aff.id = cmd.affectation LEFT JOIN
	 * restaurant.plat plt ON plt.id = cmd.plat WHERE YEAR(aff.datefin) =
	 * YEAR(NOW()) AND MONTH(aff.datefin) = MONTH(NOW()) AND DAY(aff.datefin) =
	 * DAY(NOW()) AND HOUR(aff.datefin) > restaurant.restaurant.heureouverturediner
	 * AND HOUR(aff.datefin) <= restaurant.restaurant.heurelimitediner
	 */

	// Revenu par plat (plat + nbVentes + revenu)

	// Popularit� plats (plat + nbVentes)
	/*
	 * SELECT p.nom, COUNT(c.id) AS nbVendus FROM restaurant.commande c LEFT JOIN
	 * restaurant.plat p ON c.plat = p.id GROUP BY plat ORDER BY nbVendus
	 */

	// Temps moyen par client
	// Profit dejeuner
	// Profit diner

	public Sql() throws ClassNotFoundException, SQLException, IOException {
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(this.propertiesFilename);
		prop.load(inputStream);
		Class.forName("org.postgresql.Driver");
		c = DriverManager.getConnection(prop.getProperty("datasource.url"), prop.getProperty("datasource.username"),
				prop.getProperty("datasource.password"));
		c.setAutoCommit(false);
	}

	// La classe de test doit pouvoir exécuter des requêtes
	public Statement executerTests(String requete) {
		try {
			this.stmt = c.createStatement();
			System.out.println("execute : " + requete);
			stmt.execute(requete);
			c.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return stmt;
	}

	public Statement executerInsert(String requete) {
		try {
			this.stmt = c.createStatement();
			System.out.println("Insert : " + requete);
			stmt.executeUpdate(requete);
			c.commit();
			return stmt;

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean executerDelete(String requete) {
		try {
			this.stmt = c.createStatement();
			System.out.println("Delete : " + requete);
			stmt.executeUpdate(requete);
			c.commit();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public ResultSet executerSelect(String requete) {
		ResultSet res = null;
		try {
			this.stmt = c.createStatement();
			System.out.println("Select : " + requete);
			res = stmt.executeQuery(requete);
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	private void executerUpdate(String requete) {
		try {
			this.stmt = c.createStatement();
			System.out.println("Update : " + requete);
			stmt.executeUpdate(requete);
			c.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Méthode spéciale car lors de l'ajout d'une valeur, il faut l'ajouter dans la
	// table du rôle associé
	public Personne ajouterPersonne(String nom, String role) {
		Personne newPersonne = null;

		try {
			String login = definirLogin(nom, 0);
			executerInsert(String.format(requete_insertion_personne, nom, login));
			ResultSet resultSet = executerSelect("Select MAX(id) as max FROM restaurant.personne");
			resultSet.next();
			executerInsert(
					"INSERT INTO restaurant." + role + " (personne) VALUES (" + resultSet.getString("max") + ")");
			int id = demanderDernierId(role);
			switch (role) {
			case "assistant":
				newPersonne = new Assistant(id, nom, login);
				break;

			case "serveur":
				newPersonne = new Serveur(id, nom, login);
				break;

			case "maitrehotel":
				newPersonne = new Maitrehotel(id, nom, login);
				break;

			case "directeur":
				newPersonne = new Directeur(id, nom, login);
				break;

			case "cuisinier":
				newPersonne = new Cuisinier(id, nom, login);
				break;

			default:
				newPersonne = null;
				break;

			}
			newPersonne.setIdentifiant(login);
			newPersonne.setId(id);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return newPersonne;
	}

	/**
	 * Définit un login à partir d'un nom
	 * 
	 * @param nom
	 * @param nombre
	 * @return login
	 * @throws SQLException
	 */
	public String definirLogin(String nom, int nombre) {
		try {
			this.stmt = c.createStatement();
			ResultSet resultSet = executerSelect(
					"SELECT COUNT(*) as count FROM restaurant.personne WHERE login = '" + nom + nombre + "'");
			resultSet.next();
			int nbLignes = Integer.parseInt(resultSet.getString("count"));
			// Si le login est deja utilise
			if (nbLignes > 0) {
				return definirLogin(nom, nombre + 1);
			} else {
				// S'il n'y a encore aucun login, le premier est généré avec 0
				return nom + nombre;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * @param personne
	 * @param role
	 * @throws SQLException
	 */
	public void modifierPersonne(Personne personne, String role) {
		executerDelete(
				"DELETE FROM " + personne.getClass().getName().toLowerCase() + " WHERE personne = " + personne.getId());
		executerInsert("INSERT INTO restaurant." + role + " (personne) VALUES (" + personne.getId() + ")");
	}

	/**
	 * @param personne
	 * @throws SQLException
	 */
	public void supprimerPersonne(Personne personne) throws SQLException {
		executerDelete(
				"DELETE FROM " + personne.getClass().getName().toLowerCase() + " WHERE personne = " + personne.getId());
		executerDelete("DELETE FROM restaurant.personne WHERE id = " + personne.getId());
	}

	public void insererEtage() {
		ResultSet resultSet = executerSelect("SELECT MAX(niveau) as max FROM restaurant.etage");
		try {
			resultSet.next();
			int prochainNiveau = 0;
			if (resultSet.getString("max") != null) {
				prochainNiveau = Integer.parseInt(resultSet.getString("max")) + 1;
			}
			executerInsert("INSERT INTO restaurant.etage (niveau) VALUES (" + prochainNiveau + ")");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void supprimerEtage() {
		ResultSet resultSet = executerSelect(
				"SELECT id FROM restaurant.etage WHERE niveau = (SELECT MAX(niveau) FROM restaurant.etage)");

		try {
			resultSet.next();
			int idDernierNiveau = 0;
			if (resultSet.getString("id") != null) {
				idDernierNiveau = Integer.parseInt(resultSet.getString("id"));
				executerDelete("DELETE FROM restaurant.tables WHERE etage = " + idDernierNiveau);
				executerDelete("DELETE FROM restaurant.etage WHERE id = " + idDernierNiveau);
			} else {
				System.out.println(
						"Vous avez tenté de supprimer le dernier étage alors qu'il n'y en a aucun dans la base de données");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void insererTable(int numero, int capacite, Etage etage) {
		// On vérifie si la capacité donnée est supérieur à 0
		if (capacite < 1) {
			System.out.println("Vous avez tenté de créer une table avec une capacité inférieure à 1");
			return;
		}
		// On vérifie si le numéro de table est disponible
		ResultSet resultSet = executerSelect(
				"SELECT count(*) as count FROM restaurant.tables WHERE numero = " + numero);
		try {
			resultSet.next();
			if (Integer.parseInt(resultSet.getString("count")) != 0) {
				System.out.println("Vous avez tenté de créer une table avec un numéro déjà utilisé");
			} else {
				executerInsert("INSERT INTO restaurant.tables (numero,capacite,etat,etage) VALUES (" + numero + ","
						+ capacite + ", 'Libre' ," + etage.getId() + ")");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	// Méthode pour mettre à jour le numéro de la table
	public boolean updateTable(int numero, int newNumero, Table table) {
		// On vérifie que le nouveau numéro est différent de l'actuel
		if (numero == newNumero) {
			System.out.println(
					"Vous avez tenté de mettre à jour le numéro d'une table mais l'ancien numéro est le même que celui spécifié");
			return false;
		}
		// On vérifie si le nouveau numéro est disponible
		ResultSet resultSet = executerSelect(
				"SELECT count(*) as count FROM restaurant.tables WHERE numero = " + numero);
		try {
			resultSet.next();
			if (Integer.parseInt(resultSet.getString("count")) != 0) {
				System.out.println("Vous avez tenté de créer une table avec un numéro déjà utilisé");
				return false;
			}
			executerUpdate("UPDATE restaurant.tables SET numero=" + newNumero + " WHERE id = " + table.getId());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean deleteTable(Table table) {
		return executerDelete("DELETE FROM restaurant.tables WHERE id = " + table.getId());
	}

	public boolean insererIngredient(String nom) throws SQLException {
		// On vérifie que 2 ingrédient ne peuvent pas avoir le même nom
		ResultSet resultSet = executerSelect(
				"SELECT count(*) as count FROM restaurant.ingredient WHERE nom = '" + nom + "'");
		if (resultSet == null) {
			System.out.println("Vous avez tenté de créer un ingrédient avec un nom déjà existant");
			return false;
		}
		resultSet.next();

		if (Integer.parseInt(resultSet.getString("count")) != 0) {
			System.out.println("Vous avez tenté de créer un ingrédient avec un nom déjà existant");
			return false;
		}

		// On insère l'ingrédient avec une quantité nulle
		executerInsert("INSERT INTO restaurant.ingredient (nom,quantite) VALUES ('" + nom + "',0)");
		return true;

	}

	public int demanderDernierId(String table) throws SQLException {
		ResultSet resultSet = executerSelect("SELECT MAX(id) as max FROM restaurant." + table);
		// Démarrage du curseur
		resultSet.next();
		return Integer.parseInt(resultSet.getString("max"));
	}

	public boolean commanderIngredient(Ingredient ingredient, int ajout) throws NumberFormatException, SQLException {
		// On récupère le stock actuel pour incrémenter
		ResultSet resultSet = executerSelect(
				"SELECT quantite FROM restaurant.ingredient WHERE id =" + ingredient.getId());
		int quantiteActuelle = 0;
		int nouvelleQuantite = 0;
		resultSet.next();
		if (resultSet.getString("quantite") != null) {
			System.out.println("Quantité actuelle : " + quantiteActuelle);
			quantiteActuelle = Integer.parseInt(resultSet.getString("quantite"));
			nouvelleQuantite = quantiteActuelle + ajout;
			System.out.println("Quantité nouvelle : " + nouvelleQuantite);
			executerUpdate("UPDATE restaurant.ingredient SET quantite=" + nouvelleQuantite + " WHERE id = "
					+ ingredient.getId());
			return true;
		} else {
			return false;
		}
	}

	public void initialiserIngredients() {
		ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
		ResultSet resultset = executerSelect("SELECT * FROM restaurant.ingredient");
		try {
			while (resultset.next()) {
				ingredients.add(new Ingredient(resultset.getInt("id"), resultset.getString("nom"),
						resultset.getInt("quantite")));
			}
			Restaurant.setIngredients(ingredients);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void initialiserTables(Etage etage) throws NumberFormatException, SQLException {
		// On récupère toutes les tables affecté à l'étage demandé
		ResultSet resultSet = executerSelect("SELECT * FROM restaurant.tables WHERE id = " + etage.getId());
		// Pour chaque table trouvée, on créé un objet table que l'on ajoute à l'étage
		// en cours
		while (resultSet.next()) {
			etage.addTable(new Table(Integer.parseInt(resultSet.getString("id")),
					Integer.parseInt(resultSet.getString("numero")), Integer.parseInt(resultSet.getString("capacite")),
					EtatTable.valueOf(resultSet.getString("etat"))));
		}
	}

	public void initialiserEtages() throws NumberFormatException, SQLException {
		// Initialisation de la liste d'étages à retrouner
		ArrayList<Etage> etages = new ArrayList<>();
		// Sélection de tous les étages présents dans la DB
		ResultSet resultSet = executerSelect("SELECT * FROM restaurant.etage");
		// Pour chaque étage existant, on créé un objet étage et on l'ajoute à la liste
		// retournée
		while (resultSet.next()) {
			etages.add(new Etage(Integer.parseInt(resultSet.getString("id")),
					Integer.parseInt(resultSet.getString("niveau"))));
		}
		Restaurant.setEtages(etages);
	}

	public int demanderDernierEtage() {
		ResultSet resultSet = executerSelect("SELECT MAX(niveau) as max FROM restaurant.etage");
		// Démarrage du curseur
		try {
			resultSet.next();
			return resultSet.getInt("max");
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public void premierDemarrage() {
		try {
			// Regarde si c'est le premier démarrage de l'application
			ResultSet resultSet = executerSelect("SELECT * FROM restaurant.directeur");
			if (resultSet.next() == false) {
				// il faut créer un directeur automatiquement
				Personne directeur = new Directeur(1, "directeur", "directeur0");
				ajouterPersonne("directeur", "directeur");
			} else {
				// Rien à faire, il y a déjà un directeur, donc l'application peut fonctionner
				// correctement
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void initialiserPersonnel() {
		ArrayList<Personne> personnel = new ArrayList<Personne>();
		try {
			// On prend toutes les personnes
			ResultSet resultset = executerSelect("SELECT * FROM restaurant.personne");
			while (resultset.next()) {
				// Pour chaque personne on essaie de le trouver dans un rôle
				ResultSet resultsetDirecteur = executerSelect("SELECT * FROM restaurant.directeur WHERE personne = "
						+ Integer.parseInt(resultset.getString("id")));
				while (resultsetDirecteur.next()) {
					personnel.add(new Directeur(Integer.parseInt(resultset.getString("id")), resultset.getString("nom"),
							resultset.getString("login")));
				}
				ResultSet resultsetAssistant = executerSelect("SELECT * FROM restaurant.assistant WHERE personne = "
						+ Integer.parseInt(resultset.getString("id")));
				while (resultsetAssistant.next()) {
					personnel.add(new Assistant(Integer.parseInt(resultset.getString("id")), resultset.getString("nom"),
							resultset.getString("login")));
				}
				ResultSet resultsetMaitrehotel = executerSelect("SELECT * FROM restaurant.maitrehotel WHERE personne = "
						+ Integer.parseInt(resultset.getString("id")));
				while (resultsetMaitrehotel.next()) {
					personnel.add(new Maitrehotel(Integer.parseInt(resultset.getString("id")),
							resultset.getString("nom"), resultset.getString("login")));
				}
				ResultSet resultsetServeur = executerSelect("SELECT * FROM restaurant.serveur WHERE personne = "
						+ Integer.parseInt(resultset.getString("id")));
				while (resultsetServeur.next()) {
					personnel.add(new Serveur(Integer.parseInt(resultset.getString("id")), resultset.getString("nom"),
							resultset.getString("login")));
				}
			}
			Restaurant.setPersonnel(personnel);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Plat insererPlat(String nomPlat, Double prixPlat, int dureePreparation, boolean disponibleCarte, Type type,
			Categorie categorie, HashMap<Ingredient, Integer> recetteAcreer) {
		// Pour un plat il faut
//		Nom du plat
//		Prix du plat
//		Durée de préparation du plat
//		Sa disponibilité sur la carte
//		Son type
//		Sa catégorie
		String typeConverted = type.toString().toLowerCase().substring(0, 1).toUpperCase()
				+ type.toString().toLowerCase().substring(1);
		String categorieConverted = categorie.toString().toLowerCase().toString().toLowerCase().substring(0, 1)
				.toUpperCase() + categorie.toString().toLowerCase().toString().toLowerCase().substring(1);
		executerInsert(
				"INSERT INTO restaurant.plat (nom,typePlat,typeIngredient,prix,dureePreparation,disponibleCarte) VALUES ('"
						+ nomPlat + "','" + typeConverted + "','" + categorieConverted + "', " + prixPlat + ","
						+ +dureePreparation + "," + disponibleCarte + ")");
		try {
			int idPlat = demanderDernierId("plat");
//			Ensuite il faut sa recette
//			id ingrédient
//			id du plat fraichelent créé
//			quantité ingrédient
			Iterator it = recetteAcreer.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				Ingredient ingredient = (Ingredient) pair.getKey();
				int quantite = (int) pair.getValue();
				executerInsert("INSERT INTO restaurant.recette (quantite,ingredient,plat) VALUES (" + quantite + ","
						+ ingredient.getId() + "," + idPlat + ")");
			}
			return new Plat(idPlat, nomPlat, prixPlat, dureePreparation, disponibleCarte, type, categorie,
					recetteAcreer);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// WORK IN
	// PROGRESSPROGRESSPROGRESSPROGRESSPROGRESSPROGRESSPROGRESSPROGRESSPROGRESSPROGRESSPROGRESSPROGRESSPROGRESS
	public void initialiserPlats() {
		// Initialisation de la liste d'étages à retrouner
		ArrayList<Plat> plats = new ArrayList<>();
		// Sélection de tous les étages présents dans la DB
		ResultSet resultSet = executerSelect("SELECT * FROM restaurant.plat");
		// Pour chaque plat existant, on créé un objet Plat et on l'ajoute à la liste
		// retournée
		try {
			while (resultSet.next()) {
				HashMap<Ingredient, Integer> recetter = new HashMap<>();
				ResultSet resultSetIngredients = executerSelect("SELECT * FROM restaurant.ingredient WHERE ");

				plats.add(new Plat(Integer.parseInt(resultSet.getString("id")), resultSet.getString("nom"),
						Double.parseDouble(resultSet.getString("prix")),
						Integer.parseInt(resultSet.getString("dureePreparation")),
						resultSet.getBoolean("disponibleCarte"), Type.valueOf(resultSet.getString("typePlat")),
						Categorie.valueOf(resultSet.getString("typeIngredient")), recetter));
			}
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		}
		Restaurant.setPlats(plats);
	}

	/**
	 * @param plat
	 */
	public void supprimerPlat(Plat plat) {
		plat.getRecette().forEach((ing, qtt) -> supprimerRecette(plat, ing));
		executerDelete("DELETE FROM restaurant.plat WHERE id = " + plat.getId());
	}

	/**
	 * @param plat
	 * @param ing
	 * @return
	 */
	private void supprimerRecette(Plat plat, Ingredient ing) {
		executerDelete(
				"DELETE FROM restaurant.recette WHERE plat = " + plat.getId() + " AND ingredient = " + ing.getId());
	}

	/**
	 * @param plat
	 */
	public void modifierPlat(Plat plat) {
		// TODO
		// executerUpdate("UPDATE restaurant.tables SET numero=" + newNumero + " WHERE
		// id = " + table.getId());
	}

	/**
	 * @param plat
	 * @param ing
	 * @return
	 */
	private void modifierRecette(Plat plat, Ingredient ing) {
		// TODO
		// executerDelete("DELETE FROM restaurant.recette WHERE plat = " + plat.getId()
		// + " AND ingredient = " + ing.getId());
	}

	public Affectation creationAffectation(Date dateDebut, int nbPersonne, Table table) {
		try {
			executerInsert("INSERT INTO restaurant.affectation (datedebut,datefin,nombrepersonne,tableoccupe) VALUES ('"
					+ dateDebut + "',null," + nbPersonne + "," + table.getId() + ")");
			int idAffectation = demanderDernierId("affectation");
			Affectation affectation = new Affectation(idAffectation, dateDebut, nbPersonne, null, 0.00, table);
			return affectation;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// Le montant de la facture est égale à la somme de toutes les commandes de
	// l'affectation
	public Affectation updateFactureAffectation(Affectation affectation) {
//		try {
//			executerUpdate("INSERT INTO restaurant.affectation (datedebut,datefin,nombrepersonne,tableoccupe) VALUES ('"
//					+ dateDebut + "',null," + nbPersonne + "," + table.getId() + ")");
//			executerUpdate("UPDATE restaurant.affectation SET facture=" + nouvelleQuantite + " WHERE id = "
//
//			int idAffectation = demanderDernierId("affectation");
//			Affectation affectation = new Affectation(idAffectation, dateDebut, nbPersonne, null, 0.00, table);
//			return affectation;
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		return null;
	}

	public void dateFinAffectation(Affectation affectation, Date dateFin) {
		executerUpdate("UPDATE restaurant.affectation SET datefin='" + dateFin + "' WHERE id = " + affectation.getId());
	}

	/**
	 * @param plat
	 * @param duree
	 */
	public void modifierPrixPlat(Plat plat, double prix) {
		executerUpdate("UPDATE restaurant.plat SET prix = " + prix);
	}

	/**
	 * @param plat
	 */
	public void modifierCartePlat(Plat plat, boolean estCarte) {
		executerUpdate("UPDATE restaurant.plat SET disponiblecarte = " + estCarte);
	}

	/**
	 * @param plat
	 * @param duree
	 */
	public void modifierDureePlat(Plat plat, int duree) {
		executerUpdate("UPDATE restaurant.plat SET dureepreparation = " + duree);
	}

	public Reservation creationReservation(Date dateAppel, Date dateReserve, int nbPersonne, Table tableAreserver) {

		try {
			executerInsert(
					"INSERT INTO restaurant.reservation (dateappel, datereservation, nombrepersonne, valide, tablereserve) VALUES ('"
							+ dateAppel + "','" + dateReserve + "'," + nbPersonne + ",true," + tableAreserver.getId()
							+ ")");
			int id = demanderDernierId("reservation");
			Reservation reservation = new Reservation(id, true, dateAppel, dateReserve, nbPersonne, tableAreserver);
			return reservation;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void supprimerReservation(Reservation asuppr) {
		executerDelete("DELETE FROM restaurant.reservation WHERE id =" + asuppr.getId());
	}

	public void insertionHorairesDefaut() {
		try {
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(this.propertiesFilename);
			prop.load(inputStream);
			executerInsert(
					"INSERT INTO restaurant.restaurant (heurelimitediner,heureouverturediner,heurelimitedejeune,heureouverturedejeune) VALUES ("
							+ prop.getProperty("default.heureDinerLimite") + ","
							+ prop.getProperty("default.heureDinerOuverture") + ","
							+ prop.getProperty("default.heureDejeunerLimite") + ","
							+ prop.getProperty("default.heureDejeunerOuverture") + ")");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void initialiserHoraires() {
		try {
			ResultSet resultset = executerSelect("SELECT * FROM restaurant.restaurant");
			if (!resultset.next()) {
				// Initialisation des horaires de la base de données
				insertionHorairesDefaut();
				// Rechargement
				resultset = executerSelect("SELECT * FROM restaurant.restaurant");
				resultset.next();
			}
			Restaurant.setHeureDejeunerOuverture(LocalTime.ofSecondOfDay(resultset.getInt("heureouverturedejeune")));
			Restaurant.setHeureDejeunerLimite((LocalTime.ofSecondOfDay(resultset.getInt("heurelimitedejeune"))));
			Restaurant.setHeureDinerOuverture((LocalTime.ofSecondOfDay(resultset.getInt("heureouverturediner"))));
			Restaurant.setHeureDinerLimite((LocalTime.ofSecondOfDay(resultset.getInt("heurelimitediner"))));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

//	// On veut avoir la plus petite table disponible pouvant contenir tous les
//	// participants d'un repas
//	public Table getMiniTable(int nombreParticipants, Date dateReservation) {
//		// On regarde dans la base de données le jour donné toutes les tables de
//		// réservation
//		executerSelect(
//				"SELECT tables.id FROM restaurant.tables as tables, restaurant.reservation as reservation, restaurant.affectation as affectation "
//						+ "WHERE tables.capacite >= " + nombreParticipants + "ORDER BY tables.capacite ASC");
//
//		// On récupère les non réservée
//
//		// On essaie d'en récupérer une assez grande
//
//		return null;
//	}

}
