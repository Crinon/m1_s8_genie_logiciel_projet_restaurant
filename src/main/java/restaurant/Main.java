package restaurant;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
    	// Initialisation du programme
    	Restaurant.initialisation();
    	// Problème à résoudre :
//    	On ajoute une table avec un numéro déjà pris donc ça n'insert pas, mais par contre ça l'ajoute quand même à l'arraylist de tables
    	
    	Sql sql = new Sql();


    	System.out.println(Restaurant.getIngredients());
    	System.out.println(Restaurant.getEtages());

//    	Restaurant restaurant = new Restaurant();

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
//	p.ajouterTable(22, 1, Restaurant.getEtages().get(0));

	p.ajouterIngredient("carotte",Restaurant.getIngredients());
	p.ajouterIngredient("radis",Restaurant.getIngredients());

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
	System.out.println(Restaurant.getEtages());

    }

}
