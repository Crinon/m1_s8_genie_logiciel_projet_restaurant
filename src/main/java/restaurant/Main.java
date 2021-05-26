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
	
	// Vérifie que les caractères de "valeur" crrespondent  un rôle
	// parmi : assistant ; serveur ; maitrehotel ; directeur ; cuisinier
	public static boolean estUnRole(String valeur) {

		if (valeur.equals("assistant")
			|| valeur.equals("serveur")
			|| valeur.equals("maitrehotel")
			|| valeur.equals("directeur")
			|| valeur.equals("cuisinier")
			) {
			return true;
		}
		return false;
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

		if (valeur.matches("[a-zA-Z]+")) {
			return true;
		}
		return false;
	}


	// Méthode qui permet de vérifier que "valeur" ne contient que des chiffres
	public static boolean uniquementChiffres(String valeur) {

		if (valeur.matches("[0-9]+")) {
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
	
	// Affiche les membres du personnel (sans le directeur)
	public static String listingPersonnel() {
		String liste = "";
		for (int i = 1; i < Restaurant.getPersonnel().size(); i++) { //On n'inclut pas le directeur
			liste += "\n " + i + ":" + Restaurant.getPersonnel().get(i).getNom() + "; role : "
					+ Restaurant.getPersonnel().get(i).getClass().getName().substring(11);
		}
		return liste;
	}
	
	
	
	 // Permet de commander un ingrédient pour l'ajouter au stock
 	public static void commanderIngredientDirecteur() throws ClassNotFoundException, SQLException, IOException {

 			// Affichage menu
 			System.out.println("----------------------------------"
 					+ "\n-----Commander un ingredient------"
 					+ "\nListe des ingrédients : " + listingIngredients()
 					+ "\n----------------------------------"
 					+ "\nVeuillez taper un nom si vous voulez commander un ingrédient qui ne figure"
 					+ " pas dans la liste, ou le numéro d'un des ingrédients de la liste");
 			
 			String choix;
 			int qtIngredient = 0;
 			choix = scanner.nextLine();
 			//Contrôle de l'entrée
 			while ( estNullOuVide(choix) //vide
 					|| (!uniquementLettres(choix) && !uniquementChiffres(choix))  // mélange de lettres/chiffres ou caractères spéciaux
 					|| (uniquementLettres(choix) && choix.length() > Restaurant.TAILLE_MAX_NOM_INGREDIENT) //chaine et ne respecte pas la longueur maximale
 					|| (uniquementChiffres(choix) && (Restaurant.getIngredients().size() == 0 //pas d'ingrédients dans la BDD
 					                             	|| !valeurIntOk(Integer.parseInt(choix), Restaurant.getIngredients().size()-1)) ) //valeur qui n'existe pas
 				  ){
 				System.out.println("Erreur, veuillez réessayer");
 				choix = scanner.nextLine();
 			} 
			
 			if (uniquementLettres(choix)) {
				// Nouvel ingrédient
				String nomIngredient = choix.toLowerCase();
				System.out.println(">Quantité de " + choix + " à commander ?");
				qtIngredient = choixUtilisateur(Restaurant.QUANTITE_MAX_COMMANDE); // Quantite max par commande : 500
				((Directeur) persConnectee).ajouterIngredient(nomIngredient);
				((Directeur) persConnectee).commanderIngredient(
						Restaurant.getIngredients().get(Restaurant.getIngredients().size() - 1), qtIngredient); // Dernier
				System.out.println("Commande passée (quantite : " + qtIngredient + ")");														// inséré

			} else if (uniquementChiffres(choix)) {
				// MAJ quantite d'un ingrédient existant
				System.out.println(">Quantité à commander ?");
				qtIngredient = choixUtilisateur(Restaurant.QUANTITE_MAX_COMMANDE); // Quantite max par commande : 500
				((Directeur) persConnectee).commanderIngredient(
						Restaurant.getIngredients().get(Integer.parseInt(choix)), qtIngredient); // Dernier
				System.out.println("Commande passée (quantite : " + qtIngredient + ")");																						// inséré
			}
 			
 		
 	}
 	
 // Menu principal du directeur
  	public static void menuPrincipalDirecteur() throws ClassNotFoundException, SQLException, IOException {

  		// Affichage menu
  		System.out.println("----------------------------------"
  					+ "\n0: Déconnexion"
  					+ "\n1: Commander un ingredient"
  					+ "\n2: Ajouter personnel"
  					+ "\n3: Modifier personnel"
  					+ "\n4: Supprimer personnel"
  					+ "\n5: Ajouter etage"
  					+ "\n6: Supprimer etage"
  					+ "\n7: Ajouter table"
  					+ "\n8: Modifier table"
  					+ "\n9: Supprimer table"
  					+ "\n10: Ajouter plat"
  					+ "\n11: Modifier plat"
  					+ "\n12: Modifier carte"
  					+ "\n13: Supprimer plat"
  					+ "\n14: Ajouter réservation"
  					+ "\n15: Supprimer réservation"
  					+ "\n16: Ajouter affectation"
  					+ "\n17: Ajouter commande"
  					+ "\n18: Ajouter facture"
  					+ "\n19: Ajouter affectation"
  					+ "\n20: Cuisiner un plat"
  					+ "\n21: Servir un plat"
  					+ "\n22: Nettoyer table"
  					+ "\n23: Statistiques"
  					+ "\n24: Vider la BDD"
  					+ "\n----------------------------------\n");

  		switch (Main.choixUtilisateur(24)) { // valeurChoixMin = 0

  		// Déconnexion
  		case 0:
  			Main.persConnectee = null;
  			System.out.println("Déconnexion....\nVous avez été déconnecté.\n");
  			break;

  		// Commander un ingredient
  		case 1:
  			commanderIngredientDirecteur();
  			break;

  		// Ajouter personnel
  		case 2:
  			ajouterPersonnelDirecteur();
  			break;

  		//  Modifier le rôle d'un membre du personnel
  		case 3:
  			modifierPersonnelDirecteur();
  			break;

  		// Supprimer personnel
  		case 4:
  			supprimerPersonnelDirecteur();
  			break;

  		case 5:

  			break;

  		case 6:

  			break;
  		case 7:

  			break;

  		case 8:

  			break;

  		case 9:

			break;

  		case 10:

			break;

  		case 11:

			break;

  		case 12:

			break;

  		case 13:

			break;

  		case 14:

			break;

  		case 15:

			break;

  		case 16:

			break;

  		case 17:

			break;

  		case 19:

			break;

  		case 20:

			break;

  		case 21:

			break;

  		case 22:

			break;

  		case 23:

			break;
  		case 24:

			break;

  		default:
  			break;
  		}

  	}
  	
 
  	// Modifier le rôle d'un membre du personnel
    private static void supprimerPersonnelDirecteur() {
    	// Affichage
		System.out.println("----------------------------------"
					   + "\n-----Supprimer du personnel------"
					   + "\nListe du personnel : " + listingPersonnel()
					   + "\n----------------------------------"
					   + "\nVeuillez saisir le numéro du membre à supprimer, ou 0 pour revenir au menu");
		//numéro
		String numPersonne = scanner.nextLine();
		//Contrôle de l'entrée
		while ( estNullOuVide(numPersonne) //vide
				|| !uniquementChiffres(numPersonne) //pas que des chiffres
				|| !(uniquementChiffres(numPersonne) && (valeurIntOk(Integer.parseInt(numPersonne), Restaurant.getPersonnel().size()-1)  //personne n'existe pas
														|| numPersonne.equals("0")) ) //valeur de retour
				){
			System.out.println("Erreur, veuillez réessayer");
			numPersonne = scanner.nextLine();
		}	
		
		//Permet de revenir au menu (annuler)
		if (!numPersonne.equals("0")) {
			
			((Directeur) persConnectee).supprimerPersonnel(Restaurant.getPersonnel().get(Integer.parseInt(numPersonne)), Restaurant.getPersonnel());
			System.out.println("Membre supprimé");
		}
  	}
  	
  	// Modifier le rôle d'un membre du personnel
    private static void modifierPersonnelDirecteur() {
    	// Affichage
		System.out.println("----------------------------------"
				+ "\n-----Modifier du personnel------"
				+ "\nListe du personnel : " + listingPersonnel()
				+ "\n----------------------------------"
				+ "\nVeuillez saisir le numéro de la personne à modifier, ou 0 pour revenir au menu");
		//numéro
		String numPersonne = scanner.nextLine();
		//Contrôle de l'entrée
		while ( estNullOuVide(numPersonne) //vide
				|| !uniquementChiffres(numPersonne) //pas que des chiffres
				|| (uniquementChiffres(numPersonne) && (!valeurIntOk(Integer.parseInt(numPersonne), Restaurant.getPersonnel().size()-1)  //personne n'existe pas
														|| !numPersonne.equals("0")) ) //valeur de retour
				){
			
			System.out.println("Erreur, veuillez réessayer");
			numPersonne = scanner.nextLine();
		}
		//Permet de revenir au menu (annuler)
		if (!numPersonne.equals("0")) {

			System.out.println("\nParmi assistant, serveur, maitrehotel, directeur, cuisinier;\nVeuillez saisir le nouveau role de " + Restaurant.getPersonnel().get(Integer.parseInt(numPersonne)).getNom()
					 + " qui est actuellement " + Restaurant.getPersonnel().get(Integer.parseInt(numPersonne)).getClass().getName().substring(11));
			String role = scanner.nextLine();
			//Contrôle de l'entrée
			while ( estNullOuVide(role) //vide
					|| !uniquementLettres(role) //pas que des lettres
					|| !estUnRole(role)  //n'est pas un rôle
				  ){
				System.out.println("Erreur, veuillez réessayer");
				role = scanner.nextLine();
			}
			
			((Directeur) persConnectee).modifierPersonnel(Restaurant.getPersonnel().get(Integer.parseInt(numPersonne)), role);
			System.out.println("Rôle modifié");
		
		}
  	}
    
    
  	// Ajouter un membre au personnel
    private static void ajouterPersonnelDirecteur() {

		// Affichage
		System.out.println("----------------------------------"
				+ "\n-----Ajouter du personnel------"
				+ "\nListe du personnel : " + listingPersonnel()
				+ "\n----------------------------------"
				+ "\nVeuillez saisir le nom de la personne à ajouter ou 0 pour annuler ");
		//nom
		String nom = scanner.nextLine();
		//Contrôle de l'entrée
		while ( estNullOuVide(nom) //vide
				|| (!uniquementLettres(nom) && !uniquementChiffres(nom))  // mélange de lettres/chiffres ou caractères spéciaux
				|| (uniquementLettres(nom) && nom.length() > Restaurant.TAILLE_MAX_NOM_PERSONNE) //chaine et ne respecte pas la longueur maximale
				|| (uniquementChiffres(nom) && !nom.equals("0") ) ){ //valeur de retour
			 
			System.out.println("Erreur, veuillez réessayer");
			nom = scanner.nextLine();
		}
		//Si l'utilisateur ne veut pas annuler
		if (!nom.equals("0")) {
			
			System.out.println("\nParmi assistant, serveur, maitrehotel, directeur, cuisinier;\nveuillez saisir le role de " + nom);
			String role = scanner.nextLine();
			//Contrôle de l'entrée
 			while ( estNullOuVide(role) //vide
 					|| !uniquementLettres(role) //pas que des lettres
 					|| !estUnRole(role)  //n'est pas un rôle
 				  ){
 				System.out.println("Erreur, veuillez réessayer");
 				role = scanner.nextLine();
 			}
			
			((Directeur) persConnectee).ajouterPersonnel(nom, role);
			System.out.println(nom + " (" + role + ") ajouté");
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
