package restaurant;

import java.io.IOException;
import java.sql.SQLException;

public class Cuisinier extends Personne {

	/**
	 * @param id
	 * @param nom
	 * @param identifiant
	 */
	public Cuisinier(int id, String nom, String identifiant) {
		super(id, nom, identifiant);
	}
	
	 // Menu principal du cuisinier
 	public static void menuPrincipalCuisinier() throws ClassNotFoundException, SQLException, IOException {

 		// Affichage menu
 		System.out.println("----------------------------------"
 					+ "\n0: Déconnexion"
 					+ "\n1: Définir un plat"
 					+ "\n2: Consulter les commandes"
 					+ "\n3: Passer une commande à \"terminée\""
 					+ "\n----------------------------------\n");

 		switch (Main.choixUtilisateur(7)) { // valeurChoixMin = 0

 		// Déconnexion
 		case 0:
 			Main.persConnectee = null;
 			System.out.println("Déconnexion....\nVous avez été déconnecté.\n");
 			break;

 		// Définir un plat
 		case 1:
 			//TODO
 			break;

 		// Consulter les commandes
 		case 2:
 			//TODO
 			break;
 			
 		// Passer une commande à "terminée"
 		case 3:
 			//TODO
 			break;


 		default:
 			break;
 		}

 	}
	

	// Il choisit parmis les plats pas encore en carte
	public void ajouterCarte(Plat plat) {

	}

	// Il choisit parmis les plats pas encore en carte
	public void retirerCarte(Plat plat) {

	}

	public void preparerPlat(Commande commande) {
		// Select * from commandes where pret is false order by estEnfant IS TRUE AND
		// date desc
	}

	public void definirPlat(double prix, int dureePreparation, boolean disponibleCarte, Type type,
			Categorie categorie) {
		// Insertion SQL
	}

	public void cuisinerPlat(Plat plat) {
		// Mise à jour état commande
		// Etat.EN_PREPARATION
		// Etat.PRETE
		// Avertir le serveur
	}

}
