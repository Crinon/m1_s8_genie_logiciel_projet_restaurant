package restaurant;

import java.sql.SQLException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
    	Sql sql = new Sql();
    	// Initialisation du programme
//    	Restaurant restaurant = new Restaurant();
    	ArrayList<Etage> etages = sql.getTousEtages();

    		// Charger le personnel
    		
    		// Charger les informations du restaurant
    			// Etages
    				// Tables par étage
    			// Horaire
    		
    	
    	
    	
    	
    	
	Directeur p = new Directeur("directeur");
	Personne pers = new Serveur("Nicolas");
//	p.ajouterEtage();
//	p.ajouterEtage();
//	p.ajouterEtage();
//	
	ArrayList<Etage> etagess = sql.getTousEtages();
	p.ajouterTable(0, 1, etagess.get(0));

	/*
	 * p.ajouterPersonnel(new Assistant("Florian"), "directeur");
	 * p.ajouterPersonnel(new Assistant("Florian"), "assistant");
	 * p.ajouterPersonnel(new Assistant("Florian"), "serveur");
	 * p.ajouterPersonnel(new Assistant("Florian"), "maitrehotel");
	 * p.ajouterPersonnel(new Assistant("Florian"), "cuisinier");
	 */
//
//	p.ajouterPersonnel(pers, "serveur");
//	pers = p.modifierPersonnel(pers, "cuisinier");
	// p.supprimerPersonnel(pers);
	// Au démarrage de l'application, on charge les données
	// Chargement des horaires
	// Instancier le personnel

    }

}
