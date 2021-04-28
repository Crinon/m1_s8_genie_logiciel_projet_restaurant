package restaurant;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Sql {

    private Connection c       = null;
    private Statement  stmt    = null;
    private String     pg_user = "postgres";
    private String     pg_pw   = "postgres";
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
    
    //Revenu par plat (plat + nbVentes + revenu)
    
    //Popularit� plats (plat + nbVentes)
    
    

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
	}
	catch (SQLException e) {
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
	}
	catch (SQLException e) {
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
	}
	catch (SQLException e) {
	    e.printStackTrace();
	}
	return res;
    }

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
	    }
	    else {
		// S'il n'y a encore aucun login, le premier est généré avec 0
		return nom + nombre;
	    }
	}
	catch (SQLException e) {
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
}
