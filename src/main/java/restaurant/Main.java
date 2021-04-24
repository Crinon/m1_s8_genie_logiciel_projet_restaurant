package restaurant;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
	Directeur p = new Directeur("directeur");
	Personne pers = new Serveur("Nicolas");

	/*
	 * p.ajouterPersonnel(new Assistant("Florian"), "directeur");
	 * p.ajouterPersonnel(new Assistant("Florian"), "assistant");
	 * p.ajouterPersonnel(new Assistant("Florian"), "serveur");
	 * p.ajouterPersonnel(new Assistant("Florian"), "maitrehotel");
	 * p.ajouterPersonnel(new Assistant("Florian"), "cuisinier");
	 */

	p.ajouterPersonnel(pers, "serveur");
	pers = p.modifierPersonnel(pers, "cuisinier");
	// p.supprimerPersonnel(pers);
	// Au démarrage de l'application, on charge les données
	// Chargement des horaires
	// Instancier le personnel

    }

}
