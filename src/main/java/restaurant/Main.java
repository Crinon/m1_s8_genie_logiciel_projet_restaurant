package restaurant;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

	public static Scanner scanner = new Scanner(System.in);
	public static Personne persConnectee = null;

	public static void quitterApplication() {
		System.out.println("\nAu revoir et à bientôt !");
		System.exit(1);
	}

	// Permet de se connecter via l'identifiant, s'il existe
	public static Personne connexion() {

		// Requête qui vérifie
		try {

			System.out.println(
					"Veuillez saisir 0 pour quitter l'application,\nou votre identifiant pour vous connecter.");
			String identifiant = scanner.nextLine();
			
			// Quitte l'application
			if (!estNullOuVide(identifiant) && identifiant.equals("0")) {
				quitterApplication();
			}

			// On vérifie si l'identifiant entré existe
			for (int i = 0; i < Restaurant.getPersonnel().size(); i++) {
				if (Restaurant.getPersonnel().get(i).getIdentifiant().equals(identifiant)) {
					return Restaurant.getPersonnel().get(i);
				}
			}
		} catch (Exception e) {
			//
		}
		System.out.println("Cet utilisateur n'existe pas, veuillez réessayer");
		return null;
	}

	// Vérifie la valeur entree au clavier
	public static boolean valeurIntOk(int valeur, int max) {
		if (valeur < 0 || valeur > max) {
			return false;
		}
		return true;
	}

	// Vérifie la valeur entree au clavier
	public static boolean longueurStringOk(String valeur, int longueurMin, int longueurMax) {
		if (valeur.length() < longueurMin || valeur.length() > longueurMax) {
			return false;
		}
		return true;
	}

	// Vérifie que la chaine de caractères n'est pas nulle ni vide
	public static boolean estNullOuVide(String valeur) {

		if (valeur == null || valeur == "") {
			return true;
		}
		return false; // N'est pas vide
	}

	// Vérifie que les caractères de "valeur" sont uniquement des lettres
	// On n'accepte pas les caractères spéciaux (accents, etc)
	public static boolean uniquementLettres(String valeur) {

		if (valeur.matches("[a-zA-Z]+") == true) {
			return true;
		}
		return false;
	}

	// Méthode qui permet de vérifier que "valeur" ne contient que des chiffres
	public static boolean uniquementChiffres(String valeur) {

		if (valeur.matches("[0-9]+") == true) {
			return true;
		}
		return false;
	}

	// Méthode qui permet de vérifier l'entrée pour un choix de l'utilisateur
	public static int choixUtilisateur(int valeurChoixMax) {

		String choix = "";

		while (estNullOuVide(choix) || !uniquementChiffres(choix)
				|| !valeurIntOk(Integer.parseInt(choix), valeurChoixMax)) {
			System.out.println(">Veuillez saisir votre choix (valeur allant de 0 à " + valeurChoixMax + ")");
			choix = scanner.nextLine();
		}
		return Integer.parseInt(choix);
	}

	// Affiche la liste des ingrédients disponibles
	public static String listingIngredients() {
		String liste = "";
		for (int i = 0; i < Restaurant.getIngredients().size(); i++) {
			liste += "\n " + i + ":" + Restaurant.getIngredients().get(i).getNom() + "; quantite: "
					+ Restaurant.getIngredients().get(i).getQuantite();
		}
		return liste;
	}
	
	 // Permet de commander un ingrédient pour l'ajouter au stock
 	public static void commanderIngredient() throws ClassNotFoundException, SQLException, IOException {

 			// Affichage menu
 			System.out.println("----------------------------------"
 					+ "\n-----Commander un ingredient------"
 					+ "\nListe des ingrédients : " + listingIngredients()
 					+ "\n----------------------------------\n"
 					+ "\nVeuillez taper un nom si vous voulez commander un ingrédient qui ne figure"
 					+ " pas dans la liste, ou le numéro d'un des ingrédients de la liste");
 			
 			String choix = scanner.nextLine();
 			int qtIngredient = 0;
 			do {
 				if (!estNullOuVide(choix) && uniquementLettres(choix) && choix.length() <= Restaurant.TAILLE_MAX_NOM_INGREDIENT) {
 					// Nouvel ingrédient
 					String nomIngredient = choix.toLowerCase();
 					System.out.println(">Quantité de " + choix + " à commander ?");
 					qtIngredient = choixUtilisateur(Restaurant.QUANTITE_MAX_COMMANDE); // Quantite max par commande : 500
 					((Directeur) persConnectee).ajouterIngredient(nomIngredient);
 					((Directeur) persConnectee).commanderIngredient(
 							Restaurant.getIngredients().get(Restaurant.getIngredients().size() - 1), qtIngredient); // Dernier
 					System.out.println("Commande passée (quantite : " + qtIngredient + ")");														// inséré

 				} else if (Restaurant.getIngredients().size() != 0 && !estNullOuVide(choix) && uniquementChiffres(choix)
 						&& !valeurIntOk(Integer.parseInt(choix), Restaurant.getIngredients().size())) {
 					// MAJ quantite d'un ingrédient existant
 					System.out.println(">Quantité à commander ?");
 					qtIngredient = choixUtilisateur(Restaurant.QUANTITE_MAX_COMMANDE); // Quantite max par commande : 500
 					((Directeur) persConnectee).commanderIngredient(
 							Restaurant.getIngredients().get(Restaurant.getIngredients().size() - 1), qtIngredient); // Dernier
 					System.out.println("Commande passée (quantite : " + qtIngredient + ")");																						// inséré

 				} else {
 					System.out.println("Ereur, veuillez réessayer");
 				}
 			} while (estNullOuVide(choix) || (!uniquementLettres(choix) && !uniquementChiffres(choix)));
 		
 	}
 	
 // Menu principal du directeur
  	public static void menuPrincipalDirecteur() throws ClassNotFoundException, SQLException, IOException {

  		// Affichage menu
  		System.out.println("----------------------------------"
  					+ "\n0: Déconnexion"
  				+ "\n1: Commander un ingredient"
  					+ "\n2: Ajouter personnel" + "\n3: Modifier personnel"
  					+ "\n4: Supprimer personnel"
  					+ "\n5: Suivi serveur" + "\n6: Statistiques"
  					+ "\n7: AJOUTER METHODES DES AUTRES ROLES"
  					+ "\n----------------------------------\n");

  		switch (Main.choixUtilisateur(7)) { // valeurChoixMin = 0

  		// Déconnexion
  		case 0:
  			Main.persConnectee = null;
  			System.out.println("Déconnexion....\nVous avez été déconnecté.\n");
  			break;

  		// Commander un ingredient
  		case 1:
  			commanderIngredient();
  			break;

  		// Ajouter personnel
  		case 2:

  			break;

  		// Modifier personnel
  		case 3:

  			break;

  		// Supprimer personnel
  		case 4:

  			break;

  		// Suivi serveur
  		case 5:

  			break;

  		// Statistiques
  		case 6:

  			break;

  		// Méthodes des autres rôles....
  		case 7:

  			break;

  		default:
  			break;
  		}

  	}
  	
    // Menu principal du maitre d hotel
 	public static void menuPrincipalMaitredhotel() throws ClassNotFoundException, SQLException, IOException {

 		// Affichage menu
 		System.out.println("----------------------------------"
 					+ "\n0: Déconnexion"
 					+ "\n1: Affecter un serveur à une table"
 					+ "\n2: AJOUTER METHODES DU ROLE SERVEUR ?????"
 					+ "\n----------------------------------\n");

 		switch (Main.choixUtilisateur(7)) { // valeurChoixMin = 0

 		// Déconnexion
 		case 0:
 			Main.persConnectee = null;
 			System.out.println("Déconnexion....\nVous avez été déconnecté.\n");
 			break;

 		// Affecter un serveur à une table
 		case 1:
 			//TODO
 			break;

 		// 
 		case 2:
 			//TODO
 			break;


 		default:
 			break;
 		}

 	}
 	
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
 	
 	 // Menu principal du serveur
 	public static void menuPrincipalServeur() throws ClassNotFoundException, SQLException, IOException {
 		
 		// Affichage menu
 		System.out.println("----------------------------------"
	 				//Affichage des tables de son étage + couleurs (état) associées
	 		 		//TODO
 					+ "\n0: Déconnexion"
 					+ "\n1: Affecter un serveur à une table"
 					+ "\n----------------------------------\n");

 		switch (Main.choixUtilisateur(7)) { // valeurChoixMin = 0

 		// Déconnexion
 		case 0:
 			Main.persConnectee = null;
 			System.out.println("Déconnexion....\nVous avez été déconnecté.\n");
 			break;

 		// Affecter un serveur à une table
 		case 1:
 			
 			break;

 		// TODO
 		case 2:

 			break;


 		default:
 			break;
 		}
 	}
 	
    // Menu principal de l'assistant
 	public static void menuPrincipalAssistant() throws ClassNotFoundException, SQLException, IOException {

 		// Affichage menu
 		System.out.println("----------------------------------"
 					+ "\n0: Déconnexion"
 					+ "\n1: Signaler table nettoyée"
 					+ "\n----------------------------------\n");

 		switch (Main.choixUtilisateur(7)) { // valeurChoixMin = 0

 		// Déconnexion
 		case 0:
 			Main.persConnectee = null;
 			System.out.println("Déconnexion....\nVous avez été déconnecté.\n");
 			break;

 		// Signaler table nettoyée
 		case 1:
 			//TODO
 			break;

 		default:
 			break;
 		}

 	}
 	
 	
	public static void main(String[] args) throws Exception {

		// Initialisation du programme
		Restaurant.initialisation(); // On a donc au minimum un utilisateur : le directeur
		// Application active
		do {
			while (persConnectee == null) {
				// L'utilisateur se connecte : reconnaissance du rôle
				persConnectee = connexion();
			}
			System.out.println("\nConnecté en tant que : " + persConnectee.getIdentifiant());

			// Menu en fonction du rôle
			switch (persConnectee.getClass().getName()) {
			case "restaurant.Directeur":
				persConnectee = (Directeur) persConnectee;
				menuPrincipalDirecteur();
				break;

			case "restaurant.Maitrehotel":
				persConnectee = (Maitrehotel) persConnectee;
				menuPrincipalMaitredhotel();
				break;

			case "restaurant.Cuisinier":
				persConnectee = (Cuisinier) persConnectee;
				menuPrincipalCuisinier();
				break;

			case "restaurant.Serveur":
				persConnectee = (Serveur) persConnectee;
				menuPrincipalServeur();
				break;

			case "restaurant.Assistant": // Assistant de service
				persConnectee = (Assistant) persConnectee;
				menuPrincipalAssistant();
				break;

			default:
				throw (new Exception("Problème de rôle"));
			}
			
		} while (true);
	}
}
