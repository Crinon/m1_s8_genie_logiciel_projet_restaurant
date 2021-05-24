package fr.ul.miage.restaurant;

import java.util.Scanner;

public class Main {

	public static Scanner scanner = new Scanner(System.in);
	public static boolean quitterApplication = false;
	public static Personne persConnectee = null;

	public static void quitterApplication() {
		quitterApplication = true; // Permet de quitter l'application
		System.out.println("\nAu revoir et à bientôt !");
	}

	// Permet de se connecter via l'identifiant, s'il existe
	public static Personne connexion() {

		// Requête qui vérifie
		try {

			System.out.println(
					"Veuillez saisir 0 pour quitter l'application,\n ou votre identifiant pour vous connecter.");
			String identifiant = scanner.nextLine();

			if (!estNullOuVide(identifiant) && identifiant.equals("0")) {
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
			liste += i + ":" + Restaurant.getIngredients().get(i).getNom() + "; quantite: "
					+ Restaurant.getIngredients().get(i).getQuantite();
		}
		return liste;
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
			System.out.println("\nConnecté en tant que : " + persConnectee.getIdentifiant());

			// Menu en fonction du rôle
			switch (persConnectee.getClass().getName()) {
			case "restaurant.Directeur":
				Directeur.menuPrincipalDirecteur();
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
