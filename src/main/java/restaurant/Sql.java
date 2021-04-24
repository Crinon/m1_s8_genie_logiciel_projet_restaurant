package restaurant;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sql {
	private Connection c = null;
	private Statement stmt = null;
	private String pg_user = "postgres";
	private String pg_pw = "postgres";

	public Sql() throws ClassNotFoundException, SQLException {
		Class.forName("org.postgresql.Driver");
		c = DriverManager.getConnection("jdbc:postgresql://nicolascrinon.ddns.net:5432/restaurant",pg_user, pg_pw);
		c.setAutoCommit(false);
	}

	public boolean executerSQL(String requete) {
			try {
				this.stmt = c.createStatement();
				System.out.println("En cours de traitement : " + requete);
				stmt.executeUpdate(requete);
			    c.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return true;
	}
	
	public String definirLogin(String nom) {
		String sql = "SELECT nom,login FROM personne WHERE nom = '"+ nom + "'";
		try {
			this.stmt = c.createStatement();
			System.out.println("En cours de select : " + sql);
			ResultSet rs = stmt.executeQuery(sql);
			int nbLignes = rs.getFetchSize();
			System.out.println("Nombre de " + nom + " trouvé(s) : "+ nbLignes);
			// Si on a trouvé plusieurs personnes du même nom
			if(nbLignes > 0) {
				// Récurération du nombre derrière le nom pour le login
		        Pattern pat = Pattern.compile("\\d+");
		        // On se déplace à la dernière ligne
		        rs.last();
		        // On regarde le login du dernier enregistré
		        String dernierLogin = rs.getString("login");
		        Matcher m = pat.matcher(dernierLogin);
		        int id = 0;
		        while(m.find()) {
		            id = Integer.parseInt(m.group());
		        }
				System.out.println("Id trouvé " + id);
				return nom + id++;
			} else {
		        // S'il n'y a encore aucun login, le premier est généré avec 0
				return nom + "0";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}

	// Insertion
//	public static final String requete_insertion_maitredhotel = "INSERT INTO ";
//	public static final String requete_insertion_serveur = "INSERT INTO restaurant.serveur (personne) VALUES ('%s')";
	public static final String requete_insertion_personne = "INSERT INTO restaurant.%s (personne) VALUES ('%s')";
	public static final String requete_insertion_personneRole = "INSERT INTO restaurant.%s (personne) VALUES ('%s')";
}
