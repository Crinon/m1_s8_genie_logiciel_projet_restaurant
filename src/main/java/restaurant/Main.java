package restaurant;

import java.util.Scanner;

public class Main {

	private static Scanner scanner = new Scanner(System.in);
	static boolean quitterApplication = false;
	static Personne persConnectee = null;

	
	public static void quitterApplication() {
		quitterApplication = true; //Permet de quitter l'application
		System.out.println("\nAu revoir et à bientôt !");
	}
	
	// Permet de se connecter via l'identifiant, s'il existe
	public static Personne connexion() {

		// Requête qui vérifie
		try {

			System.out.println("Veuillez saisir 0 pour quitter l'application,\n ou votre identifiant pour vous connecter.");
			String identifiant = scanner.nextLine();
			
			if (!estNulleOuVide(identifiant) && identifiant.equals("0")) {
				quitterApplication();
				return null;
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
	public static boolean estNulleOuVide(String valeur) {

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

		
		while (estNulleOuVide(choix) || !uniquementChiffres(choix) || !valeurIntOk(Integer.parseInt(choix), valeurChoixMax)) {
			System.out.println(">Veuillez saisir votre choix (valeur allant de 0 à " + valeurChoixMax + ")");
			choix = scanner.nextLine();
		}
		return Integer.parseInt(choix);
	}

	// Affiche la liste des ingrédients disponibles
	public static String listingIngredients() {
		String liste = "";
		for (int i = 0; i < Restaurant.getIngredients().size(); i++) {
			liste += i + ":" + Restaurant.getIngredients().get(i).getNom()
				   + "; quantite: " + Restaurant.getIngredients().get(i).getQuantite();
		}
		return liste;
	}
	
	// Permet de commander un ingrédient pour l'ajouter au stock
	public static void commanderIngredient() {
		// Affichage menu
		System.out.println("----------------------------------"
					   + "\n-----Commander un ingredient------"
				       + "\nListe des ingrédients : "
				       + listingIngredients()
				       + "\nVeuillez sélectionner le numéro de l'ingrédient à commander"
				       + "\n----------------------------------\n"
				       );
		
		int numIngredient = choixUtilisateur(Restaurant.getIngredients().size() );
		
		System.out.println("Veuillez sélectionner la quantite de " + Restaurant.getIngredients().get(numIngredient));
		int qtIngredient = choixUtilisateur(500); //Quantite max par commande : 500
		
		//Ajout de l'ingrédient
		//APPEL DE LA METHODE ajoutIngredient
		
		
		//Valider ou retour
	}

	// Menu principal du directeur
	public static void  menuPrincipalDirecteur() {

		// Affichage menu
		System.out.println("----------------------------------"
				        + "\n0: Déconnexion"
				        + "\n1: Commander un ingredient"
				        + "\n2: Ajouter personnel"
				        + "\n3: Modifier personnel"
				        + "\n4: Supprimer personnel"
				        + "\n5: Suivi serveur"
				        + "\n6: Statistiques"
				        + "\n7: AJOUTER METHODES DES AUTRES ROLES"
				        + "\n----------------------------------\n"
				        );

		switch (choixUtilisateur(7)) { //valeurChoixMin = 0

		// Déconnexion
		case 0:
			persConnectee = null;
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

	public static void main(String[] args) throws Exception {		

		// Initialisation du programme
		Restaurant.initialisation(); // On a donc au minimum un utilisateur : le directeur
		persConnectee = connexion();
		// Application active
		while (!quitterApplication) {
			
			while (persConnectee == null) {
				// L'utilisateur se connecte : reconnaissance du rôle
				persConnectee = connexion();
			}
			System.out.println("\nConnecté en tant que : " + persConnectee.getIdentifiant() );

			// Menu en fonction du rôle
			switch (persConnectee.getClass().getName()) {
			case "restaurant.Directeur":
				menuPrincipalDirecteur();
				break;

			case "restaurant.Maitrehotel":

				break;

			case "restaurant.Cuisinier":

				break;

			case "restaurant.Serveur":

				break;

			case "restaurant.Assistant": // Assistant de service

				break;

			default:
				throw (new Exception("Problème de rôle"));
			}
			// Creer un ingrédient

		}
		scanner.close(); // On ferme le scanner après avoir quitté l'application

	}

}
