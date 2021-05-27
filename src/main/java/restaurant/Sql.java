package restaurant;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.stream.Collectors;

public class Sql {

    private Connection	       c		  = null;
    private Statement	       stmt		  = null;
    public final String	       propertiesFilename = "properties";
    private Properties	       prop		  = new Properties();
    public static final String hardResetPostgres  = "DROP SCHEMA restaurant CASCADE;";

    public static final String hardResetH2		      = "ALTER TABLE restaurant.affectation SET REFERENTIAL_INTEGRITY FALSE;"
	    + "TRUNCATE TABLE restaurant.affectation;\r\n"
	    + "ALTER TABLE restaurant.assistant SET REFERENTIAL_INTEGRITY FALSE;"
	    + "TRUNCATE TABLE restaurant.assistant;\r\n"
	    + "ALTER TABLE restaurant.commande SET REFERENTIAL_INTEGRITY FALSE;"
	    + "TRUNCATE TABLE restaurant.commande;\r\n"
	    + "ALTER TABLE restaurant.cuisinier SET REFERENTIAL_INTEGRITY FALSE;"
	    + "TRUNCATE TABLE restaurant.cuisinier;\r\n"
	    + "ALTER TABLE restaurant.directeur SET REFERENTIAL_INTEGRITY FALSE;"
	    + "TRUNCATE TABLE restaurant.directeur;\r\n"
	    + "ALTER TABLE restaurant.etage SET REFERENTIAL_INTEGRITY FALSE;" + "TRUNCATE TABLE restaurant.etage;\r\n"
	    + "ALTER TABLE restaurant.ingredient SET REFERENTIAL_INTEGRITY FALSE;"
	    + "TRUNCATE TABLE restaurant.ingredient;\r\n"
	    + "ALTER TABLE restaurant.maitrehotel SET REFERENTIAL_INTEGRITY FALSE;"
	    + "TRUNCATE TABLE restaurant.maitrehotel;\r\n"
	    + "ALTER TABLE restaurant.personne SET REFERENTIAL_INTEGRITY FALSE;"
	    + "TRUNCATE TABLE restaurant.personne;\r\n" + "ALTER TABLE restaurant.plat SET REFERENTIAL_INTEGRITY FALSE;"
	    + "TRUNCATE TABLE restaurant.plat;\r\n" + "ALTER TABLE restaurant.recette SET REFERENTIAL_INTEGRITY FALSE;"
	    + "TRUNCATE TABLE restaurant.recette;\r\n"
	    + "ALTER TABLE restaurant.reservation SET REFERENTIAL_INTEGRITY FALSE;"
	    + "TRUNCATE TABLE restaurant.reservation;\r\n"
	    + "ALTER TABLE restaurant.restaurant SET REFERENTIAL_INTEGRITY FALSE;"
	    + "TRUNCATE TABLE restaurant.restaurant;\r\n"
	    + "ALTER TABLE restaurant.serveur SET REFERENTIAL_INTEGRITY FALSE;"
	    + "TRUNCATE TABLE restaurant.serveur;\r\n"
	    + "ALTER TABLE restaurant.tables SET REFERENTIAL_INTEGRITY FALSE;" + "TRUNCATE TABLE restaurant.tables;\r\n"
	    + "\r\n" + "ALTER SEQUENCE restaurant.affectation_id_seq RESTART WITH 1;\r\n"
	    + "ALTER SEQUENCE restaurant.assistant_id_seq RESTART WITH 1;\r\n"
	    + "ALTER SEQUENCE restaurant.commande_id_seq RESTART WITH 1;\r\n"
	    + "ALTER SEQUENCE restaurant.cuisinier_id_seq RESTART WITH 1;\r\n"
	    + "ALTER SEQUENCE restaurant.directeur_id_seq RESTART WITH 1;\r\n"
	    + "ALTER SEQUENCE restaurant.etage_id_seq RESTART WITH 1;\r\n"
	    + "ALTER SEQUENCE restaurant.ingredient_id_seq RESTART WITH 1;\r\n"
	    + "ALTER SEQUENCE restaurant.maitrehotel_id_seq RESTART WITH 1;\r\n"
	    + "ALTER SEQUENCE restaurant.personne_id_seq RESTART WITH 1;\r\n"
	    + "ALTER SEQUENCE restaurant.plat_id_seq RESTART WITH 1;\r\n"
	    + "ALTER SEQUENCE restaurant.recette_id_seq RESTART WITH 1;\r\n"
	    + "ALTER SEQUENCE restaurant.reservation_id_seq RESTART WITH 1;\r\n"
	    + "ALTER SEQUENCE restaurant.restaurant_id_seq RESTART WITH 1;\r\n"
	    + "ALTER SEQUENCE restaurant.serveur_id_seq RESTART WITH 1;\r\n"
	    + "ALTER SEQUENCE restaurant.tables_id_seq RESTART WITH 1;";
    public static final String requete_insertion_personne     = "INSERT INTO restaurant.personne (nom,login) VALUES ('%s','%s')";
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

    public Sql() {
	InputStream inputStream = getClass().getClassLoader().getResourceAsStream(this.propertiesFilename);
	try {
	    prop.load(inputStream);
	    Class.forName("org.postgresql.Driver");
	    c = DriverManager.getConnection(prop.getProperty("datasource.url"), prop.getProperty("datasource.username"),
		    prop.getProperty("datasource.password"));
	    c.setAutoCommit(false);
	}
	catch (IOException | ClassNotFoundException | SQLException e) {
	    e.printStackTrace();
	}

    }

    // La classe de test doit pouvoir exécuter des requêtes
    public Statement executerTests(String requete) {
	try {
	    this.stmt = c.createStatement();
	    System.out.println("execute : " + requete);
	    stmt.execute(requete);
	    c.commit();
	}
	catch (SQLException e) {
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

	}
	catch (SQLException e) {
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
	}
	catch (SQLException e) {
	    e.printStackTrace();
	}
	return false;
    }

    public ResultSet executerSelect(String requete) {
	try {
	    ResultSet res = null;
	    this.stmt = c.createStatement();
//	    System.out.println("Select : " + requete);
	    res = stmt.executeQuery(requete);
	    c.commit();
	    return res;
	} catch (SQLException e) {
		    e.printStackTrace();
	}
	return null;
    }

    public boolean executerUpdate(String requete) {
	try {
	    this.stmt = c.createStatement();
//	    System.out.println("Update : " + requete);
	    stmt.executeUpdate(requete);
	    c.commit();
	    return true;
	}
	catch (SQLException e) {
		    e.printStackTrace();
		    return false;
	}
    }

    // Méthode spéciale car lors de l'ajout d'une valeur, il faut l'ajouter dans la
    // table du rôle associé
    public Personne ajouterPersonne(String nom, String role) {
	Personne newPersonne = null;

	try {
	    String login = definirLogin(nom, 0);
	    executerInsert(String.format("INSERT INTO restaurant.personne (nom,login) VALUES ('%s','%s')", nom, login));
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
	    newPersonne.setMasterid(demanderDernierId("personne"));

	}
	catch (SQLException e) {
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
	    }
	    else {
		// S'il n'y a encore aucun login, le premier est généré avec 0
		return nom + nombre;
	    }
	}
	catch (SQLException e) {
	    e.printStackTrace();
	    return null;
	}

    }

    /**
     * @param personne
     * @param role
     */
    public void modifierPersonne(Personne personne, String role) {
	executerDelete(
		"DELETE FROM " + personne.getClass().getName().toLowerCase() + " WHERE personne = " + personne.getId());
	executerInsert("INSERT INTO restaurant." + role + " (personne) VALUES (" + personne.getId() + ")");
    }

    /**
     * @param personne
     */
    public void supprimerPersonne(Personne personne) {
	executerDelete("DELETE FROM " + personne.getClass().getName().toLowerCase() + " WHERE personne = "
		+ personne.getMasterid());

	executerDelete("DELETE FROM restaurant.personne WHERE id = " + personne.getMasterid());
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
	}
	catch (SQLException e) {
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
	    }
	    else {
		System.out.println(
			"Vous avez tenté de supprimer le dernier étage alors qu'il n'y en a aucun dans la base de données");
	    }
	}
	catch (SQLException e) {
	    e.printStackTrace();
	}

    }

    public void insererTable(int numero, int capacite, Etage etage) {
	// On vérifie si la capacité donnée est supérieur à 0
	if (capacite < 1) {
	    System.err.println("Vous avez tenté de créer une table avec une capacité inférieure à 1");
	    return;
	}
	// On vérifie si le numéro de table est disponible
	ResultSet resultSet = executerSelect(
		"SELECT count(*) as count FROM restaurant.tables WHERE numero = " + numero);
	try {
	    resultSet.next();
	    if (Integer.parseInt(resultSet.getString("count")) != 0) {
		System.err.println("Vous avez tenté de créer une table avec un numéro déjà utilisé");
	    }
	    else {
		executerInsert("INSERT INTO restaurant.tables (numero,capacite,etat,etage) VALUES (" + numero + ","
			+ capacite + ", 'Libre' ," + etage.getId() + ")");
	    }
	}
	catch (SQLException e) {
	    e.printStackTrace();
	}

    }

    // Méthode pour mettre à jour le numéro de la table
    public boolean updateTable(Table table, int newNumero) {
	int ancienNumero = table.getNumero();
	// On vérifie que le nouveau numéro est différent de l'actuel
	if (ancienNumero == newNumero) {
	    System.err.println(
		    "Vous avez tenté de mettre à jour le numéro d'une table mais l'ancien numéro est le même que celui spécifié");
	    return false;
	}
	// On vérifie si le nouveau numéro est disponible
	ResultSet resultSet = executerSelect(
		"SELECT count(*) as count FROM restaurant.tables WHERE numero = " + ancienNumero);
	try {
	    resultSet.next();
	    System.out.println("nombre de table avec le numéro " + ancienNumero + " "
		    + Integer.parseInt(resultSet.getString("count")));
	    if (Integer.parseInt(resultSet.getString("count")) != 0) {
		executerUpdate("UPDATE restaurant.tables SET numero=" + newNumero + " WHERE id = " + table.getId());
		return true;
	    }
	    else {
		System.err.println(
			"Mise à jour de table : Vous avez tenté de modifier une table avec un numéro inexistant");
		return false;
	    }
	}
	catch (SQLException e) {
	    e.printStackTrace();
	}
	return true;
    }

    public boolean deleteTable(Table table) {
	return executerDelete("DELETE FROM restaurant.tables WHERE id = " + table.getId());
    }

    public boolean insererIngredient(String nom) {
	// On vérifie que 2 ingrédient ne peuvent pas avoir le même nom
	ResultSet resultSet = executerSelect(
		"SELECT count(*) as count FROM restaurant.ingredient WHERE nom = '" + nom + "'");
	if (resultSet == null) {
	    System.err.println("Vous avez tenté de créer un ingrédient avec un nom déjà existant");
	    return false;
	}
	try {
	    resultSet.next();
	    if (Integer.parseInt(resultSet.getString("count")) != 0) {
		System.err.println("Vous avez tenté de créer un ingrédient avec un nom déjà existant");
		return false;
	    }

	    // On insère l'ingrédient avec une quantité nulle
	    executerInsert("INSERT INTO restaurant.ingredient (nom,quantite) VALUES ('" + nom + "',0)");
	    return true;
	}
	catch (SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return false;

    }

    public int demanderDernierId(String table) {
	ResultSet resultSet = executerSelect("SELECT MAX(id) as max FROM restaurant." + table);
	// Démarrage du curseur
	try {
	    resultSet.next();
	    return Integer.parseInt(resultSet.getString("max"));
	}
	catch (SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return 0;
    }

    public boolean commanderIngredient(Ingredient ingredient, int ajout) {
	// On récupère le stock actuel pour incrémenter
	ResultSet resultSet = executerSelect(
		"SELECT quantite FROM restaurant.ingredient WHERE id =" + ingredient.getId());
	int quantiteActuelle = 0;
	int nouvelleQuantite = 0;
	try {
	    resultSet.next();
	    if (resultSet.getString("quantite") != null) {
		System.out.println(ajout);
		quantiteActuelle = Integer.parseInt(resultSet.getString("quantite"));
		System.out.println("Quantité actuelle : " + quantiteActuelle);
		nouvelleQuantite = quantiteActuelle + ajout;
		System.out.println("Quantité nouvelle : " + nouvelleQuantite);
		executerUpdate("UPDATE restaurant.ingredient SET quantite=" + nouvelleQuantite + " WHERE id = "
			+ ingredient.getId());
		for (int i = 0; i < Restaurant.getIngredients().size(); i++) {
		    if (Restaurant.getIngredients().get(i).getId() == ingredient.getId()) {
			Restaurant.getIngredients().get(i).setQuantite(nouvelleQuantite);
		    }
		}
		return true;
	    }
	    else {
		return false;
	    }
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
	return false;
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
	}
	catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    public void initialiserTables(Etage etage) {
	// On récupère toutes les tables affecté à l'étage demandé
	ResultSet resultSet = executerSelect("SELECT * FROM restaurant.tables WHERE etage = " + etage.getId());
	// Pour chaque table trouvée, on créé un objet table que l'on ajoute à l'étage
	// en cours
	try {
	    while (resultSet.next()) {
	    	System.err.println("table trouve : " + resultSet.getString("id"));
		etage.addTable(new Table(Integer.parseInt(resultSet.getString("id")),
			Integer.parseInt(resultSet.getString("numero")),
			Integer.parseInt(resultSet.getString("capacite")),
			EtatTable.valueOf(resultSet.getString("etat"))));
	    }
	}
	catch (NumberFormatException | SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public void initialiserEtages() {
	// Initialisation de la liste d'étages à retrouner
	ArrayList<Etage> etages = new ArrayList<>();
	// Sélection de tous les étages présents dans la DB
	ResultSet resultSet = executerSelect("SELECT * FROM restaurant.etage");
	// Pour chaque étage existant, on créé un objet étage et on l'ajoute à la liste
	// retournée
	try {
	    while (resultSet.next()) {
		etages.add(new Etage(Integer.parseInt(resultSet.getString("id")),
			Integer.parseInt(resultSet.getString("niveau"))));
	    }
	    Restaurant.setEtages(etages);
	}
	catch (NumberFormatException | SQLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public int demanderDernierEtage() {
	ResultSet resultSet = executerSelect("SELECT MAX(niveau) as max FROM restaurant.etage");
	// Démarrage du curseur
	try {
	    resultSet.next();
	    return resultSet.getInt("max");
	}
	catch (SQLException e) {
	    e.printStackTrace();
	    return 0;
	}
    }

    public void premierDemarrage() {
	try {
	    // Regarde si il y a au moins un directeur
	    ResultSet resultSet = executerSelect("SELECT * FROM restaurant.directeur");
	    if (resultSet.next() == false) {
		// il faut créer un directeur automatiquement
		ajouterPersonne("directeur", "directeur");
	    }
	}
	catch (SQLException e) {
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
	}
	catch (SQLException e) {
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
	executerInsert(
		"INSERT INTO restaurant.plat (nom,typeplat,typeingredient,prix,dureePreparation,disponibleCarte) VALUES ('"
			+ nomPlat + "','" + type.name() + "','" + categorie.name() + "'," + prixPlat + ","
			+ +dureePreparation + "," + disponibleCarte + ")");
	int idPlat = demanderDernierId("plat");
//			Ensuite il faut sa recette
//			id ingrédient
//			id du plat fraichelent créé
//			quantité ingrédient
	Iterator<Entry<Ingredient, Integer>> it = recetteAcreer.entrySet().iterator();
	while (it.hasNext()) {
	    Entry<Ingredient, Integer> pair = it.next();
	    Ingredient ingredient = (Ingredient) pair.getKey();
	    int quantite = (int) pair.getValue();
	    executerInsert("INSERT INTO restaurant.recette (quantite,ingredient,plat) VALUES (" + quantite + ","
		    + ingredient.getId() + "," + idPlat + ")");
	}
	return new Plat(idPlat, nomPlat, prixPlat, dureePreparation, disponibleCarte, type, categorie, recetteAcreer);
    }

    public void initialiserPlats() {
	// On créer l'arraylist de plat et on la retourne
	ArrayList<Plat> plats = new ArrayList<>();
	// Sélection de tous les plats présents dans la DB
	ResultSet resultSet = executerSelect("SELECT * FROM restaurant.plat");
	// Pour chaque plat existant, on créé un objet Plat et on l'ajoute à la liste
	// retournée
	try {
	    while (resultSet.next()) {
		HashMap<Ingredient, Integer> recette = new HashMap<Ingredient, Integer>();
		ResultSet resultSetLignesRecette = executerSelect(
			"SELECT * FROM restaurant.recette WHERE plat=" + resultSet.getInt("id"));
		while (resultSetLignesRecette.next()) {
		    Ingredient ingredient = Restaurant.getIngredients().stream().filter(ingredientCurrent -> {
			try {
			    return ingredientCurrent.getId() == resultSetLignesRecette.getInt("ingredient");
			}
			catch (SQLException e) {
			    e.printStackTrace();
			    return false;
			}
		    }).collect(Collectors.toList()).get(0);
		    int quantite = resultSetLignesRecette.getInt("quantite");
		    recette.put(ingredient, quantite);
		}
		plats.add(new Plat(Integer.parseInt(resultSet.getString("id")), resultSet.getString("nom"),
			Double.parseDouble(resultSet.getString("prix")),
			Integer.parseInt(resultSet.getString("dureePreparation")),
			resultSet.getBoolean("disponibleCarte"), Type.valueOf(resultSet.getString("typePlat")),
			Categorie.valueOf(resultSet.getString("typeIngredient")), recette));
	    }
	}
	catch (NumberFormatException | SQLException e) {
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

    public Affectation creationAffectation(Date dateDebut, int nbPersonne, Table table) {
	executerInsert(
		"INSERT INTO restaurant.affectation (datedebut,datefin,nombrepersonne,tableoccupe,facture) VALUES ('"
			+ dateDebut + "',null," + nbPersonne + "," + table.getId() + ",0)");
	int idAffectation = demanderDernierId("affectation");
	Affectation affectation = new Affectation(idAffectation, dateDebut, nbPersonne, new ArrayList<Commande>(), 0.00,
		table);
	return affectation;
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
	executerUpdate("UPDATE restaurant.plat SET prix = " + prix + " WHERE id = " + plat.getId());
    }

    /**
     * @param plat
     */
    public void modifierCartePlat(Plat plat, boolean estCarte) {
	executerUpdate("UPDATE restaurant.plat SET disponiblecarte = " + estCarte + " WHERE id = " + plat.getId());
    }

    /**
     * @param plat
     * @param duree
     */
    public void modifierDureePlat(Plat plat, int duree) {
	executerUpdate("UPDATE restaurant.plat SET dureepreparation = " + duree + " WHERE id = " + plat.getId());
    }

    /**
     * @param table
     * @param etat
     */
    public void modifierEtatTable(Table table, EtatTable etat) {
	executerUpdate("UPDATE restaurant.tables SET etat = '" + etat.name() + "' WHERE id = " + table.getId());
    }

    /**
     * @param serveur
     * @param table
     */
    public void affecterTableServeur(Serveur serveur, Table table) {
	if (serveur != null) {
	    executerUpdate(
		    "UPDATE restaurant.tables SET serveur = " + serveur.getId() + " WHERE id = " + table.getId());
	}
	else {
	    executerUpdate("UPDATE restaurant.tables SET serveur = null WHERE id = " + table.getId());
	}
    }

    public Reservation creationReservation(Date dateAppel, Date dateReserve, int nbPersonne, Table tableAreserver) {
	executerInsert(
		"INSERT INTO restaurant.reservation (dateappel, datereservation, nombrepersonne, valide, tablereserve) VALUES ('"
			+ dateAppel + "','" + dateReserve + "'," + nbPersonne + ",true," + tableAreserver.getId()
			+ ")");
	int id = demanderDernierId("reservation");
	Reservation reservation = new Reservation(id, true, dateAppel, dateReserve, nbPersonne, tableAreserver);
	return reservation;
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
	}
	catch (IOException e) {
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
	}
	catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    public Commande creationCommande(Date dateCommande, Plat plat, boolean estEnfant, Affectation affectation) {
	// Ajout de la commande en base
	executerInsert("INSERT INTO restaurant.commande (datedemande, estenfant, plat, affectation, etat) VALUES ('"
		+ dateCommande + "'," + estEnfant + "," + plat.getId() + "," + affectation.getId() + ",'"
		+ Etat.COMMANDEE.name() + "'" + ")");
	// Retrait des quantités nécessaires à la commande
	HashMap<Ingredient, Integer> recette = plat.getRecette();
	recette.forEach((ingredient, quantiteNecessaire) -> {
	    int quantiteRestante = ingredient.getQuantite() - quantiteNecessaire;
	    // Mise à jour quantité en base
	    executerUpdate("UPDATE restaurant.ingredient SET quantite=" + quantiteRestante + " WHERE id = "
		    + ingredient.getId());
	    // Mise à jour quantité en objet
	    ingredient.setQuantite(quantiteRestante);
	});
	int idCommande = demanderDernierId("commande");
	return new Commande(idCommande, dateCommande, estEnfant, plat, affectation, Etat.COMMANDEE);
    }

    public void initialiserReservation() {
	try {
	    // On récupère toutes les futures réservations
	    Date now = new Timestamp(new Date().getTime());
	    ResultSet resultset = executerSelect(
		    "SELECT * FROM restaurant.reservation WHERE datereservation>'" + now + "'");
	    while (resultset.next()) {
		ResultSet resultsetTableReserve = executerSelect(
			"SELECT * FROM restaurant.tables WHERE id=" + resultset.getInt("tablereserve"));
		resultsetTableReserve.next();
		int idTable = resultsetTableReserve.getInt("id");
		System.err.println("id de la table en cours : " + idTable);
		System.err.println(Restaurant.getEtages().toString());
		System.err.println(Restaurant.getToutesLesTables().toString());
		
		// On récupère la table avec le bon id
		Table tableReserve = Restaurant.getToutesLesTables().stream()
			.filter(tableCurrent -> tableCurrent.getId() == idTable).collect(Collectors.toList()).get(0);
		Restaurant.getReservationsJour()
			.add(new Reservation(resultset.getInt("id"), resultset.getBoolean("valide"),
				resultset.getDate("dateappel"), resultset.getDate("datereservation"),
				resultset.getInt("tablereserve"), tableReserve));
	    }
	}
	catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    // Les constantes ne sont utilisées qu'en java, pas dans la base de données
    public void initialiserConstantes() {
	try {
	    InputStream inputStream = getClass().getClassLoader().getResourceAsStream(this.propertiesFilename);
	    prop.load(inputStream);
	    Restaurant.setQUANTITE_MAX_STOCK(Integer.parseInt(prop.getProperty("constant.QUANTITE_MAX_STOCK")));
	}
	catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public void hardResetH2(String database) {
    	executerUpdate(database);
    }
    // Cette fonction drop le schéma restaurant et le reconstruit
    public void hardResetPg(String database) {
    	executerUpdate(database);
    	// Drop de schéma avant de re-générer de la base de données
    	  InputStream inputStream = getClass().getClassLoader().getResourceAsStream("restaurant.sql");
    	  ByteArrayOutputStream result = new ByteArrayOutputStream();
    	  byte[] buffer = new byte[1024];
    	  try {
			for (int length; (length = inputStream.read(buffer)) != -1; ) {
			      result.write(buffer, 0, length);
			  }
	    	String rebuild = result.toString("UTF-8");
	    	System.err.println("Schema postgresql restaurant dropé");
	    	executerUpdate(rebuild);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void insererJeuDonnees() {
    	  InputStream inputStream = getClass().getClassLoader().getResourceAsStream("jeudonnees.sql");
    	  ByteArrayOutputStream result = new ByteArrayOutputStream();
    	  byte[] buffer = new byte[1024];
    	  try {
			for (int length; (length = inputStream.read(buffer)) != -1; ) {
			      result.write(buffer, 0, length);
			  }
	    	String data = result.toString("UTF-8");
	    	System.err.println("Jeu de données inséré");
	    	executerUpdate(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public Double revenuHebdomadaire() {
	try {
	    ResultSet rs = executerSelect(
		    "SELECT SUM(plat.prix) AS revenu\r\n"
		    + "FROM restaurant.commande cmd\r\n"
		    + "LEFT JOIN restaurant.affectation aff ON aff.id = cmd.affectation \r\n"
		    + "LEFT JOIN restaurant.plat plat ON plat.id = cmd.plat \r\n"
		    + "WHERE date_part('year', aff.datefin) = date_part('year', CURRENT_DATE)\r\n"
		    + "AND  to_char(CURRENT_DATE, 'IYYY-IW') = to_char(aff.datefin, 'IYYY-IW')");
	    rs.next();
	    return rs.getDouble("revenu");
	}
	catch (SQLException e) {
	    e.printStackTrace();
	}
	return null;
    }

    public Double revenuMensuel() {
	try {
	    ResultSet rs = executerSelect(
		    "SELECT SUM(plat.prix) AS revenu\r\n"
		    + "FROM restaurant.commande cmd\r\n"
		    + "LEFT JOIN restaurant.affectation aff ON aff.id = cmd.affectation \r\n"
		    + "LEFT JOIN restaurant.plat plat ON plat.id = cmd.plat \r\n"
		    + "WHERE date_part('year', aff.datefin) = date_part('year', CURRENT_DATE)\r\n"
		    + "AND date_part('month', aff.datefin) = date_part('month', CURRENT_DATE)");
	    rs.next();
	    return rs.getDouble("revenu");
	}
	catch (SQLException e) {
	    e.printStackTrace();
	}
	return null;
    }

    public Double revenuQuotidien() {
	try {
	    ResultSet rs = executerSelect(
		    "SELECT SUM(plat.prix) AS revenu\r\n"
		    + "FROM restaurant.commande cmd\r\n"
		    + "LEFT JOIN restaurant.affectation aff ON aff.id = cmd.affectation \r\n"
		    + "LEFT JOIN restaurant.plat plat ON plat.id = cmd.plat \r\n"
		    + "WHERE date_part('year', aff.datefin) = date_part('year', CURRENT_DATE)\r\n"
		    + "AND date_part('month', aff.datefin) = date_part('month', CURRENT_DATE)\r\n"
		    + "AND date_part('day', aff.datefin) = date_part('day', CURRENT_DATE)");
	    rs.next();
	    return rs.getDouble("revenu");
	}
	catch (SQLException e) {
	    e.printStackTrace();
	}
	return null;
    }

    public HashMap<String, Double> popularitePlats() {
    	HashMap<String, Double> resultats = new HashMap<String,Double>();
	try {
	    ResultSet rs = executerSelect(
		    "SELECT p.nom, COUNT(c.id) AS nbVendus FROM restaurant.commande c LEFT JOIN restaurant.plat p ON c.plat = p.id GROUP BY p.nom ORDER BY nbVendus");
	    while(rs.next()) {
	    	resultats.put(rs.getString("nom"),rs.getDouble("nbVendus"));
	    }
	    return resultats;
	}
	catch (SQLException e) {
	    e.printStackTrace();
	}
	return null;
    }

    public Double tempsPreparationMoyen() {
	try {
	    ResultSet rs = executerSelect(
		    "SELECT SUM(plat.dureePreparation)/COUNT(comm.id) AS tempsPrepaMoyen \r\n"
		    + "FROM restaurant.commande comm LEFT JOIN restaurant.plat plat ON comm.plat = plat.id");
	    rs.next();
	    return rs.getDouble("tempsPrepaMoyen");
	}
	catch (SQLException e) {
	    e.printStackTrace();
	}
	return null;
    }

    public Double profitDejeunerJour() {
	try {
	    ResultSet rs = executerSelect(
		    "SELECT SUM(plat.prix) AS profitJourDejeuner\r\n"
		    + "FROM restaurant.commande cmd \r\n"
		    + "LEFT JOIN restaurant.affectation aff ON aff.id = cmd.affectation \r\n"
		    + "LEFT JOIN restaurant.plat plat ON plat.id = cmd.plat \r\n"
		    + "WHERE date_part('year', aff.datedebut) = date_part('year', CURRENT_DATE)\r\n"
		    + "AND date_part('month', aff.datedebut) = date_part('month', CURRENT_DATE)\r\n"
		    + "AND date_part('day', aff.datedebut) = date_part('day', CURRENT_DATE)\r\n"
		    + "AND extract(epoch FROM datedebut::time) >= (SELECT heureouverturedejeune FROM restaurant.restaurant WHERE id=1)\r\n"
		    + "AND extract(epoch FROM datedebut::time) < (SELECT heureouverturediner FROM restaurant.restaurant WHERE id=1)\r\n");
	    
	    rs.next();
	    return rs.getDouble("profitJourDejeuner");
	}
	catch (SQLException e) {
	    e.printStackTrace();
	}
	return null;
    }

    public Double profitDinerJour() {
	try {
	    ResultSet rs = executerSelect(
	    		"SELECT SUM(plat.prix) AS profitDinerDejeuner\r\n"
	    		+ "FROM restaurant.commande cmd \r\n"
	    		+ "LEFT JOIN restaurant.affectation aff ON aff.id = cmd.affectation \r\n"
	    		+ "LEFT JOIN restaurant.plat plat ON plat.id = cmd.plat \r\n"
	    		+ "WHERE date_part('year', aff.datedebut) = date_part('year', CURRENT_DATE)\r\n"
	    		+ "AND date_part('month', aff.datedebut) = date_part('month', CURRENT_DATE)\r\n"
	    		+ "AND date_part('day', aff.datedebut) = date_part('day', CURRENT_DATE)\r\n"
	    		+ "AND extract(epoch FROM datedebut::time) >= (SELECT heureouverturediner FROM restaurant.restaurant WHERE id=1)");
	    rs.next();
	    return rs.getDouble("profitDinerDejeuner");
	}
	catch (SQLException e) {
	    e.printStackTrace();
	}
	return null;
    }

    public void modifierEtatCommande(Commande commande, Etat etat) {
	executerUpdate("UPDATE restaurant.commande SET etat = '" + etat.name() + "' WHERE id = " + commande.getId());
    }

    public void creerFacture(Affectation affectation, double prixFacture) {
	executerUpdate("UPDATE restaurant.affectation SET facture = " + prixFacture + " WHERE id = " + affectation.getId());
	modifierEtatTable(affectation.getTable(), EtatTable.Sale);
	executerUpdate("UPDATE restaurant.tables SET etat = 'Sale' WHERE id = " + affectation.getTable().getId());
    }

	public void annulerReservation(Reservation reservation) {
		executerUpdate("UPDATE restaurant.reservation SET valide=false WHERE id = " + reservation.getId());
	}

	public void modifierHoraire(String nomHoraire, int horaire) {
		switch (nomHoraire) {
		case "ouverture midi":
			executerUpdate("UPDATE restaurant.restaurant SET heureouverturedejeune="+horaire);
			Restaurant.setHeureDejeunerOuverture(LocalTime.ofSecondOfDay(horaire));
			break;
		case "fermeture midi":
			executerUpdate("UPDATE restaurant.restaurant SET heurelimitedejeune="+horaire);
			Restaurant.setHeureDejeunerLimite(LocalTime.ofSecondOfDay(horaire));
			break;
		case "ouverture soir":
			executerUpdate("UPDATE restaurant.restaurant SET heureouverturediner="+horaire);
			Restaurant.setHeureDinerOuverture(LocalTime.ofSecondOfDay(horaire));
			break;
		case "fermeture soir":
			executerUpdate("UPDATE restaurant.restaurant SET heurelimitediner="+horaire);
			Restaurant.setHeureDinerLimite(LocalTime.ofSecondOfDay(horaire));
			break;
		default:
			break;
		}

	}

	public Double profitDejeunerAlltime() {
		try {
		    ResultSet rs = executerSelect(
	    "SELECT SUM(plat.prix) AS profitJourDejeunerAlltime\r\n"
	    + "FROM restaurant.commande cmd \r\n"
	    + "LEFT JOIN restaurant.affectation aff ON aff.id = cmd.affectation \r\n"
	    + "LEFT JOIN restaurant.plat plat ON plat.id = cmd.plat \r\n"
	    + "WHERE extract(epoch FROM datedebut::time) >= (SELECT heureouverturedejeune FROM restaurant.restaurant WHERE id=1)\r\n"
	    + "AND extract(epoch FROM datedebut::time) < (SELECT heureouverturediner FROM restaurant.restaurant WHERE id=1)\r\n");
		    rs.next();
		    return rs.getDouble("profitJourDejeunerAlltime");
		}
		catch (SQLException e) {
		    e.printStackTrace();
		}
		return null;
	}

	public Double profitDinerAlltime() {
		try {
		    ResultSet rs = executerSelect(
			    "SELECT SUM(plat.prix) AS profitDinerDejeunerAlltime\r\n"
			    + "FROM restaurant.commande cmd \r\n"
			    + "LEFT JOIN restaurant.affectation aff ON aff.id = cmd.affectation \r\n"
			    + "LEFT JOIN restaurant.plat plat ON plat.id = cmd.plat \r\n"
			    + "WHERE extract(epoch FROM datedebut::time) >= (SELECT heureouverturediner FROM restaurant.restaurant WHERE id=1)");
		    rs.next();
		    return rs.getDouble("profitDinerDejeunerAlltime");
		}
		catch (SQLException e) {
		    e.printStackTrace();
		}
		return null;
	}

	public Double tempsRotationMoyen() {
		try {
		    ResultSet rs = executerSelect(
			    "SELECT (SUM(extract(epoch FROM aff.datefin::time)-extract(epoch FROM aff.datedebut::time))/COUNT(aff.id))/60 AS tempsRotationMoyen \r\n"
			    + "FROM restaurant.affectation aff");
		    rs.next();
		    return rs.getDouble("tempsRotationMoyen");
		}
		catch (SQLException e) {
		    e.printStackTrace();
		}
		return null;
	}

	    public HashMap<String, Double> partPlatRecette() {
	    	HashMap<String, Double> resultats = new HashMap<String,Double>();
		try {
		    ResultSet rs = executerSelect(
			    "SELECT p.nom, (COUNT(c.id)*p.prix) AS recettePlat "
			    + "FROM restaurant.commande c "
			    + "LEFT JOIN restaurant.plat p ON c.plat = p.id "
			    + "GROUP BY p.nom,p.prix "
			    + "ORDER BY recettePlat DESC");
		    while(rs.next()) {
		    	resultats.put(rs.getString("nom"),rs.getDouble("recettePlat"));
		    }
		    return resultats;
		}
		catch (SQLException e) {
		    e.printStackTrace();
		}
		return null;
	    }

}
