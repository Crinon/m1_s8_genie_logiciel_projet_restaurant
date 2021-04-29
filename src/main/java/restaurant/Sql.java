package restaurant;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Sql {

	private Connection c = null;
	private Statement stmt = null;
	private String pg_user = "postgres";
	private String pg_pw = "postgres";
	// Insertion
//	public static final String requete_insertion_maitredhotel = "INSERT INTO ";
//	public static final String requete_insertion_serveur = "INSERT INTO restaurant.serveur (personne) VALUES ('%s')";

    public static final String requete_insertion_personne     = "INSERT INTO restaurant.personne (nom,login) VALUES ('%s','%s')";
    public static final String requete_insertion_personneRole = "INSERT INTO restaurant.%s (personne) VALUES ('%s')";
    
    //V�rifie si le login existe lors de la connexion : renvoie 1 si vrai, 0 sinon (en sachant qu'il n'y a pas de doublons)
    public static final String requete_login_existe = "SELECT COUNT(p.id) as existe FROM restaurant.personne p WHERE p.login = '%s'";
    
    
    //Revenu hebdomadaire
    //Revenu quotidien
    //Revenu mensuel

    //Temps de preparation moyen
    public static final String requete_temps_prepare_moyen = "SELECT SUM(p.dureePreparation)/COUNT(c.id) AS tempsPrepaMoyen\r\n" + 
    		"FROM restaurant.commande c\r\n" + 
    		"LEFT JOIN restaurant.plat p ON c.plat = p.id";
    
    //Temps moyen par client
    //Profit dejeuner
    //Profit diner
    
    

    public Sql() throws ClassNotFoundException, SQLException {
	Class.forName("org.postgresql.Driver");
	c = DriverManager.getConnection("jdbc:postgresql://nicolascrinon.ddns.net:5432/restaurant", pg_user, pg_pw);
	c.setAutoCommit(false);
    }

    public Statement executerInsert(String requete) {
	try {
	    this.stmt = c.createStatement();
	    System.out.println("Insert : " + requete);
	    stmt.executeUpdate(requete);
	    c.commit();
	}
	catch (SQLException e) {
	    e.printStackTrace();
	}
    return stmt;
    }    

    private boolean executerDelete(String requete) {
	try {
	    this.stmt = c.createStatement();
	    System.out.println("Delete : " + requete);
	    stmt.executeUpdate(requete);
	    c.commit();
	}
	catch (SQLException e) {
	    e.printStackTrace();
	}
	return true;
    }

    private ResultSet executerSelect(String requete) {
	ResultSet res = null;
	try {
	    this.stmt = c.createStatement();
	    System.out.println("Select : " + requete);
	    res = stmt.executeQuery(requete);
	}
	catch (SQLException e) {
	    e.printStackTrace();
	}
	return res;
    }
    
    
    private ResultSet executerUpdate(String requete) {
    	ResultSet res = null;
    	try {
    	    this.stmt = c.createStatement();
    	    System.out.println("Update : " + requete);
    	    res = stmt.executeQuery(requete);
    	}
    	catch (SQLException e) {
    	    e.printStackTrace();
    	}
    	return res;
        }
 
      
	// Méthode spéciale car lors de l'ajout d'une valeur, il faut l'ajouter dans la
	// table du rôle associé
	public void ajouterPersonne(Personne personne, String role) throws SQLException {
		personne.setIdentifiant(definirLogin(personne.getNom(), 0));
		executerInsert(String.format(requete_insertion_personne, personne.getNom(), personne.getIdentifiant()));
		ResultSet resultSet = executerSelect("Select MAX(id) FROM restaurant.personne");
		resultSet.next();
		executerInsert("INSERT INTO restaurant." + role + " (personne) VALUES (" + resultSet.getString("max") + ")");
		personne.setId(Integer.parseInt(resultSet.getString("max")));
	}

	/**
	 * Définit un login à partir d'un nom
	 * 
	 * @param nom
	 * @param nombre
	 * @return login
	 */
	public String definirLogin(String nom, int nombre) {
		String sql = "SELECT login FROM restaurant.personne WHERE nom = '" + nom + "'";
		try {
			this.stmt = c.createStatement();
			ResultSet resultSet = executerSelect(
					"SELECT COUNT(*) FROM restaurant.personne WHERE login = '" + nom + nombre + "'");
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
		}
		return "";
	}

	/**
	 * @param personne
	 * @param role
	 */
	public void modifierPersonne(Personne personne, String role) {
		executerDelete("DELETE FROM restaurant." + personne.getClass().getName().toLowerCase() + " WHERE personne = "
				+ personne.getId());
		executerInsert("INSERT INTO restaurant." + role + " (personne) VALUES (" + personne.getId() + ")");
	}

	/**
	 * @param personne
	 */
	public void supprimerPersonne(Personne personne) {
		executerDelete("DELETE FROM restaurant." + personne.getClass().getName().toLowerCase() + " WHERE personne = "
				+ personne.getId());
		executerDelete("DELETE FROM restaurant.personne WHERE id = " + personne.getId());
	}

	public void insererEtage() throws NumberFormatException, SQLException {
		ResultSet resultSet = executerSelect("SELECT MAX(niveau) FROM restaurant.etage");
		resultSet.next();
		int prochainNiveau = 0;
		if (resultSet.getString("max") != null) {
			prochainNiveau = Integer.parseInt(resultSet.getString("max")) + 1;
		}
		executerInsert("INSERT INTO restaurant.etage (niveau) VALUES (" + prochainNiveau + ")");
	}



	public void supprimerEtage() throws SQLException {
		ResultSet resultSet = executerSelect(
				"SELECT id FROM restaurant.etage WHERE niveau = (SELECT MAX(niveau) FROM restaurant.etage)");
		resultSet.next();
		int idDernierNiveau = 0;
		if (resultSet.getString("id") != null) {
			idDernierNiveau = Integer.parseInt(resultSet.getString("id"));
			executerDelete("DELETE FROM restaurant.table WHERE etage = " + idDernierNiveau);
			executerDelete("DELETE FROM restaurant.etage WHERE id = " + idDernierNiveau);
		} else {
			System.out.println(
					"Vous avez tenté de supprimer le dernier étage alors qu'il n'y en a aucun dans la base de données");
		}
	}

	public void insererTable(int numero, int capacite, Etage etage) throws SQLException {
		// On vérifie si la capacité donnée est supérieur à 0
		if (capacite < 1) {
			System.out.println("Vous avez tenté de créer une table avec une capacité inférieure à 1");
			return;
		}
		// On vérifie si le numéro de table est disponible
		ResultSet resultSet = executerSelect("SELECT count(*) FROM restaurant.tables WHERE numero = " + numero);
		resultSet.next();
		if (Integer.parseInt(resultSet.getString("count"))!= 0) {
			System.out.println("Vous avez tenté de créer une table avec un numéro déjà utilisé");
		} else {
			executerInsert("INSERT INTO restaurant.tables (numero,capacite,etat,etage) VALUES (" + numero + ","
					+ capacite + ", 'Libre' ," + etage.getId() + ")");
		}
	}

	
	// Méthode pour mettre à jour le numéro de la table
	public boolean updateTable(int numero, int newNumero, Table table) throws SQLException {
		// On vérifie que le nouveau numéro est différent de l'actuel
		if(numero==newNumero) {
			System.out.println("Vous avez tenté de mettre à jour le numéro d'une table mais l'ancien numéro est le même que celui spécifié");
			return false;
		}
		// On vérifie si le nouveau numéro est disponible
		ResultSet resultSet = executerSelect("SELECT count(*) FROM restaurant.tables WHERE numero = " + numero);
		resultSet.next();
		if (Integer.parseInt(resultSet.getString("count"))!= 0) {
			System.out.println("Vous avez tenté de créer une table avec un numéro déjà utilisé");
			return false;
		}
		executerUpdate("UPDATE restaurant.tables (numero) VALUES ("+ newNumero +") WHERE id = "+table.getId());
		return true;
	}

	public boolean deleteTable(Table table) {
		return executerDelete("DELETE FROM restaurant.table WHERE id = " + table.getId());
	}
	
	public Ingredient insererIngredient(String nom) throws SQLException {
		// On vérifie que 2 ingrédient ne peuvent pas avoir le même nom
		ResultSet resultSet = executerSelect("SELECT count(*) FROM restaurant.ingredient WHERE nom = '" + nom +"'");
		if(resultSet == null) {
			System.out.println("Vous avez tenté de créer un ingrédient avec un nom déjà existant");
			return null;			
		}
		resultSet.next();
		if (Integer.parseInt(resultSet.getString("count"))!= 0) {
			System.out.println("Vous avez tenté de créer un ingrédient avec un nom déjà existant");
			return null;
		}
		
		// On insère l'ingrédient avec une quantité nulle
		Statement statement = executerInsert("INSERT INTO restaurant.ingredient (nom,quantite) VALUES ('" + nom + "',0)");

        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
        		return new Ingredient((int) generatedKeys.getLong(1), nom, 0);
            }
        }
		return null;

	}
	
	public int demanderDernierId(String table) throws SQLException {
		ResultSet resultSet = executerSelect("SELECT MAX(id) FROM restaurant."+table);
		// Démarrage du curseur
		resultSet.next();
		return Integer.parseInt(resultSet.getString("MAX"));
	}
	

	public boolean commanderIngredient(Ingredient ingredient, int ajout) throws NumberFormatException, SQLException {
		// On récupère le stock actuel pour incrémenter
		ResultSet resultSet = executerSelect("SELECT quantite FROM restaurant.ingredient WHERE id =" + ingredient.getId());
		int quantiteActuelle = 0;
		int nouvelleQuantite = 0;
		if (resultSet.getString("quantite") != null) {
			quantiteActuelle = Integer.parseInt(resultSet.getString("quantite"));
			nouvelleQuantite = quantiteActuelle + ajout;
			executerUpdate("UPDATE restaurant.ingredient (quantite) VALUES () WHERE id = " + nouvelleQuantite);
			return true;
		} else {
			return false;
		}
	}
	
	public void initialiserIngredients() throws SQLException {
		ArrayList<Ingredient> ingredients= new ArrayList<Ingredient>();
		ResultSet resultset = executerSelect("SELECT * FROM restaurant.ingredient");
	     while (resultset.next()) {
	    	 ingredients.add(new Ingredient(resultset.getInt("id"), resultset.getString("nom"), resultset.getInt("quantite")));
	       }
    	 Restaurant.setIngredients(ingredients);
	}
	
	public void initialiserTables(Etage etage) throws NumberFormatException, SQLException {
		// On récupère toutes les tables affecté à l'étage demandé
		ResultSet resultSet = executerSelect("SELECT * FROM restaurant.tables WHERE id = "+etage.getId());
		// Pour chaque table trouvée, on créé un objet table que l'on ajoute à l'étage en cours
		while (resultSet.next()) {
			etage.addTable(new Table(Integer.parseInt(resultSet.getString("id")),
					Integer.parseInt(resultSet.getString("numero")),
					Integer.parseInt(resultSet.getString("capacite")),
					EtatTable.valueOf(resultSet.getString("etat"))
					));
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
	
	public int demanderDernierEtage() throws SQLException {
		ResultSet resultSet = executerSelect("SELECT MAX(niveau) FROM restaurant.etage");
		// Démarrage du curseur
		resultSet.next();
		return resultSet.getInt("MAX");
	}

}
