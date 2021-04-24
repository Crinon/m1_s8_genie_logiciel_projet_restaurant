package restaurant;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Directeur p = new Directeur("directeur"); 
		p.ajouterPersonnel(new Assistant("Florian"), "assistant");
//		p.ajouterPersonnel(new Serveur("Hervé"), "serveur");
//		
		

		
		// Au démarrage de l'application, on charge les données
		// Chargement des horaires
		// Instancier le personnel
		
		
	}

}
