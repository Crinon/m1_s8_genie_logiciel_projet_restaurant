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
	public static final String requete_insertion_personne = "INSERT INTO restaurant.personne (nom,login) VALUES ('%s','%s')";
	public static final String requete_insertion_personneRole = "INSERT INTO restaurant.%s (personne) VALUES ('%s')";

	public Sql() throws ClassNotFoundException, SQLException {
		Class.forName("org.postgresql.Driver");
		c = DriverManager.getConnection("jdbc:postgresql://nicolascrinon.ddns.net:5432/restaurant", pg_user, pg_pw);
		c.setAutoCommit(false);
	}

	public boolean executerInsert(String requete) {
		try {
			this.stmt = c.createStatement();
			System.out.println("Insert : " + requete);
			stmt.executeUpdate(requete);
			c.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean executerDelete(String requete) {
		try {
			this.stmt = c.createStatement();
			System.out.println("Delete : " + requete);
			stmt.executeUpdate(requete);
			c.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	public ResultSet executerSelect(String requete) {
		ResultSet res = null;
		try {
			this.stmt = c.createStatement();
			System.out.println("Select : " + requete);
			res = stmt.executeQuery(requete);
		} catch (SQLException e) {
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
		ResultSet resultSet = executerSelect(
				"SELECT MAX(niveau) FROM restaurant.etage");
		resultSet.next();
		int prochainNiveau = 0;
		if(resultSet.getString("max") != null) {
			prochainNiveau = Integer.parseInt(resultSet.getString("max")) + 1;
		}
		executerInsert("INSERT INTO restaurant.etage (niveau) VALUES (" + prochainNiveau + ")");
	}
	
	public ArrayList<Etage> getTousEtages() throws NumberFormatException, SQLException {
		// Initialisation de la liste d'étages à retrouner
    	ArrayList<Etage> etages = new ArrayList<>();
    	// Sélection de tous les étages présents dans la DB
		ResultSet resultSet = executerSelect(
				"SELECT * FROM restaurant.etage");
		// Pour chaque étage existant, on créé un objet étage et on l'ajoute à la liste retournée
		while(resultSet.next()) {
			etages.add(new Etage(Integer.parseInt(resultSet.getString("id")),Integer.parseInt(resultSet.getString("niveau"))));
		}
		return etages;
	}

	public void supprimerEtage() throws SQLException {
		ResultSet resultSet = executerSelect(
				"SELECT id FROM restaurant.etage WHERE niveau = (SELECT MAX(niveau) FROM restaurant.etage)");
		resultSet.next();
		int idDernierNiveau = 0;
		if(resultSet.getString("id") != null) {
			idDernierNiveau = Integer.parseInt(resultSet.getString("id"));
			executerDelete("DELETE FROM restaurant.table WHERE etage = " + idDernierNiveau);
			executerDelete("DELETE FROM restaurant.etage WHERE id = " + idDernierNiveau);
		} else {
			System.out.println("Vous avez tenté de supprimer le dernier étage alors qu'il n'y en a aucun dans la base de données");
		}
	}
	
	
	
	
	public void insererTable() {
		// TODO Auto-generated method stub
		
	}
	
}


