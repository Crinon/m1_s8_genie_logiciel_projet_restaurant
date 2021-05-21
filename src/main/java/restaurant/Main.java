package restaurant;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

	// Permet de se connecter via l'identifiant, s'il existe
	public static Personne connexion() {

		// Requête qui vérifie
		try (Scanner scanner = new Scanner(System.in)) {
			System.out.println("Veuillez saisir votre identifiant");
			String identifiant = scanner.nextLine();

			// On vérifie si l'identifiant entré existe
			for (int i = 0; i < Restaurant.getPersonnel().size(); i++) {
				if (Restaurant.getPersonnel().get(i).getIdentifiant().equals(identifiant)) {
					return Restaurant.getPersonnel().get(i);
				}
			}
			return null;

		}
	}

	public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {

		boolean quitterApplication = false;
		String identifiant = "";
		// Initialisation du programme
		Restaurant.initialisation(); // On a donc au minimum un utilisateur : le directeur

		// Application active
		while (!quitterApplication) {

			while (connexion() == null) {
				// L'utilisateur se connecte : reconnaissance du rôle
				connexion();

			}

		}

	}

}
