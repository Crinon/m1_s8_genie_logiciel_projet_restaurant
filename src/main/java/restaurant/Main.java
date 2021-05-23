package restaurant;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Main {

	private static Scanner scanner = new Scanner(System.in);

	// Permet de se connecter via l'identifiant, s'il existe
	public static Personne connexion() {

		// Requête qui vérifie
		try {

			System.out.println("Veuillez saisir votre identifiant");
			String identifiant = scanner.nextLine();

			// On vérifie si l'identifiant entré existe
			for (int i = 0; i < Restaurant.getPersonnel().size(); i++) {
				if (Restaurant.getPersonnel().get(i).getIdentifiant().equals(identifiant)) {
					return Restaurant.getPersonnel().get(i);
				}
			}
		} catch (Exception e) {
			System.out.println("Cet utilisatuer n'existe pas, veuillez réessayer");
		}

		return null;
	}

	// Vérifie la valeur entree au clavier
	public static boolean valeurIntOk(int valeur, int min, int max) {
		if (valeur < min || valeur > max) {
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
	public static boolean estNulleOuVideString(String valeur) {

		if (valeur == null || valeur == "") {
			return true;
		}
		return false; // N'est pas vide
	}

	// Vérifie que les caractères de "valeur" sont uniquement des lettres
	// On n'accepte pas les caractères spéciaux (accents, etc)
	public static boolean uniquementLettres(String valeur) {

		if (Pattern.matches("[a-zA-Z]+", valeur) == true) {
			return false;
		}
		return true;
	}

	// Menu principal du directeur
	public static void menuPrincipalDirecteur(int choix) {

		while (!valeurIntOk(choix, 0, 7)) {
			System.out.println("Veuillez saisir votre identifiant");
			String identifiant = scanner.nextLine();

		}
		switch (choix) {

		// Déconnexion
		case 0:

			break;
		// Commander un ingredient
		case 1:

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

		boolean quitterApplication = false;
		String identifiant = "";
		Personne persConnectee = null;

		// Initialisation du programme
		Restaurant.initialisation(); // On a donc au minimum un utilisateur : le directeur
		persConnectee = connexion();
		// Application active
		while (!quitterApplication) {

			while (persConnectee == null) {
				// L'utilisateur se connecte : reconnaissance du rôle
				persConnectee = connexion();
			}
			System.out.println("\nConnecté en tant que : " + persConnectee.getIdentifiant() + ";"
					+ persConnectee.getClass().getName());

			switch (persConnectee.getClass().getName()) {
			case "restaurant.Directeur":

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
