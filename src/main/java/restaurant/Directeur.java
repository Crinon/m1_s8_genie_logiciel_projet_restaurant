package restaurant;

import java.sql.SQLException;

public class Directeur extends Personne {

	public Directeur(String string) {
		this.identifiant = string;
	}

	public void ajouterPersonnel(Personne personne, String role) throws ClassNotFoundException, SQLException {
		// Object pour intéragir avec la base de données
		Sql sql = new Sql();
		// Utilisation de la requête pour insérer un serveur
		System.out.println(personne.getIdentifiant());
		sql.executerSQL(String.format(Sql.requete_insertion_personne,role,personne.getIdentifiant()));
	}
	
}