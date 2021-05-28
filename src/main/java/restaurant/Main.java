package restaurant;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
	public static int choixUtilisateur(int valeurChoixMin, int valeurChoixMax) {

		String choix = "";

		while (estNullOuVide(choix) || !uniquementChiffres(choix)
				|| !valeurIntOk(Integer.parseInt(choix),valeurChoixMin, valeurChoixMax)) {
			System.out.println(">Veuillez saisir votre choix (valeur allant de " + valeurChoixMin + " à " + valeurChoixMax + ")");
			choix = scanner.nextLine();
		}
		return Integer.parseInt(choix);
	}
	
	// Affiche la liste des étages
	public static String listingEtages() {
		String liste = "";
		for (int i = 0; i < Restaurant.getEtages().size(); i++) {
			liste += "\n " + i + ": niveau" + Restaurant.getEtages().get(i).getNiveau();
		}
		return liste;
	}
	
	// Affiche la liste des tables
	public static String listingServeurs() {
		String liste = "";
		int numero = 1;
		for (int i = 0; i < Restaurant.getPersonnel().size(); i++) {
			if (Restaurant.getPersonnel().get(i).getClass().getName().equals("restaurant.Serveur")) {
				liste += numero + " :" + Restaurant.getPersonnel().get(i).getNom()
						+ " (" + Restaurant.getPersonnel().get(i).getIdentifiant()+ ")\n";
				numero+=1;
			}	
		}
		return liste;
	}
		
	
	// Affiche la liste des tables
	public static Serveur trouverServeur(int numServeur) {

		System.out.println(Restaurant.getPersonnel().size());
		int numero = 0;
		for (int numPersonnel = 0; numPersonnel < Restaurant.getPersonnel().size(); numPersonnel++) { //0 = directeur
			if (Restaurant.getPersonnel().get(numPersonnel).getClass().getName().equals("restaurant.Serveur")) {
				numero+=1;
			}
			if (numero == numServeur) {
				return (Serveur) Restaurant.getPersonnel().get(numPersonnel);
			}
		}
		return null;
	}
	
	// Affiche la liste des tables
	public static String listingTables() {
		String liste = "";
		int i = 1; //numero de la table dans le menu (0 étant réservé au retour)
		
		for (int etage = 0; etage < Restaurant.getEtages().size(); etage++) {
			liste += "\n<Etage " + Restaurant.getEtages().get(etage).getNiveau() + " >";
			for (int table = 0; table <Restaurant.getEtages().get(etage).getTables().size(); table++) {
				liste += "\n"+ i  + " : table " + Restaurant.getEtages().get(etage).getTables().get(table).getNumero()
						+ " avec une capacite de : " + Restaurant.getEtages().get(etage).getTables().get(table).getCapacite() + " personnes"
						+ " (état : " + Restaurant.getEtages().get(etage).getTables().get(table).getEtat() + ")";
			i+=1;
			}
		}
		return liste;
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
	
	// Compte le nombre de serveurs
	public static int nbServeurs() {
		int cpt = 0;
		for (int i = 0; i < Restaurant.getPersonnel().size(); i++) {
			if (Restaurant.getPersonnel().get(i).getClass().getName().equals("restaurant.Serveur")) {
				cpt+=1;
			}	
		}
		return cpt;
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
 					                             	|| !valeurIntOk(Integer.parseInt(choix),0, Restaurant.getIngredients().size()-1)) ) //valeur qui n'existe pas
 				  ){
 				System.out.println("Erreur, veuillez réessayer");
 				choix = scanner.nextLine();
 			} 
			
 			if (uniquementLettres(choix)) {
				// Nouvel ingrédient
				String nomIngredient = choix.toLowerCase();
				System.out.println(">Quantité de " + choix + " à commander ?");
				qtIngredient = choixUtilisateur(1,Restaurant.QUANTITE_MAX_COMMANDE); // Quantite max par commande : 500
				((Directeur) persConnectee).ajouterIngredient(nomIngredient);
				((Directeur) persConnectee).commanderIngredient(
						Restaurant.getIngredients().get(Restaurant.getIngredients().size() - 1), qtIngredient); // Dernier
				System.out.println("Commande passée (quantite : " + qtIngredient + ")");														// inséré

			} else if (uniquementChiffres(choix)) {
				// MAJ quantite d'un ingrédient existant
				System.out.println(">Quantité à commander ?");
				qtIngredient = choixUtilisateur(1, Restaurant.QUANTITE_MAX_COMMANDE); // Quantite max par commande : 500
				((Directeur) persConnectee).commanderIngredient(
						Restaurant.getIngredients().get(Integer.parseInt(choix)), qtIngredient); // Dernier
				System.out.println("Commande passée (quantite : " + qtIngredient + ")");																						// inséré
			}
 	}
 	
 	
	// Permet d'ajouter un étage au restaurant
 	public static void ajouterEtage() throws ClassNotFoundException, SQLException, IOException {

 			// Affichage menu
 			System.out.println("----------------------------------"
 						   + "\n-------Ajouter un étage-----------"
 					+ "\nListe des étages : " + listingEtages()
 					+ "\n----------------------------------"
 					+ "\n1 pour valider, 0 pour annuler");
 			
 			if (Main.choixUtilisateur(0,1) == 1) {
 				((Directeur) persConnectee).ajouterEtage();
			}
 			System.out.println("Etage ajouté");
 			
 	}
 	
	// Permet de supprimer le dernier étage du restaurant
 	public static void supprimerDernierEtage() throws ClassNotFoundException, SQLException, IOException {

 			// Affichage menu
 			System.out.println("----------------------------------"
 						     + "\n--------Supprimer un étage--------"
		 					 + "\nListe des étages : " + listingEtages()
		 					 + "\n----------------------------------"
		 					 + "\n1 pour valider, 0 pour annuler");
 			
 			if (Main.choixUtilisateur(0,1) == 1) {
 				((Directeur) persConnectee).supprimerDernierEtage();
			}
 			System.out.println("Dernier étage supprimé");
 	}
 	
 	//Permet de trouver une table via son numéro
 	public static Table trouverTable(int numero) {
 		int nbTable = 1;
		for (int etage = 0; etage < Restaurant.getEtages().size(); etage++) {
			for (int table = 0; table < Restaurant.getEtages().get(etage).getTables().size(); table++) {
				if (nbTable ==  numero) {
					return Restaurant.getEtages().get(etage).getTables().get(table);
				}
				nbTable +=1;
				
			}
		}
 		return null;
 	}
 	
 	//Permet de trouver un étage via une table
 	public static Etage trouverEtage(Table tab) {
 		for (int etage = 0; etage < Restaurant.getEtages().size(); etage++) {
 			for (int table = 0; table < Restaurant.getEtages().get(etage).getTables().size(); table++) {
	 			if (Restaurant.getEtages().get(etage).getTables().get(table).equals(tab)) {
					return Restaurant.getEtages().get(etage);
				}
 			}
 		}
 		return null;
 	}
 	
 	
 	// Permet de supprimer une table d'un étage
  	public static void supprimerTableDirecteur() throws ClassNotFoundException, SQLException, IOException {

		// Affichage menu
		System.out.println("----------------------------------"
					     + "\n-----Supprimer une table--------"
	 					 + "\nListe des tables : " + listingTables()
	 					 + "\n----------------------------------"
	 					 + "\nVeuillez choisir le numero (menu) correspondant à la table à supprimer, ou 0 pour retourner au menu");

		//numero de la table
		System.out.println("\nNuméro de la table :");
		
		int nbTables = 0; //numero de la table dans le menu
		for (int etage = 0; etage < Restaurant.getEtages().size(); etage++) {
				nbTables+= Restaurant.getEtages().get(etage).getTables().size();
		}
		
		int numero = Main.choixUtilisateur(0,nbTables); //numero du menu
		if (numero != 0) {
			((Directeur) persConnectee).supprimerTable(trouverTable(numero), trouverEtage(trouverTable(numero)).getTables() );
			System.out.println("Table supprimée");
		}
	
  	}
  	
  	
	// Permet de modifier le numéro d'une table
 	public static void modifierTableDirecteur() throws ClassNotFoundException, SQLException, IOException {

		// Affichage menu
		System.out.println("----------------------------------"
					   + "\n-------Modifier une table---------"
	 					 + "\nListe des tables : " + listingTables()
	 					 + "\n----------------------------------"
	 					 + "\nVeuillez choisir le numero (menu) correspondant à la table à modifier, ou 0 pour retourner au menu");

		// Numero de la table
		System.out.println("\nTable à modifier :");
		
		int nbTables = 0; //numero de la table dans le menu
		for (int etage = 0; etage < Restaurant.getEtages().size(); etage++) {
			nbTables+= Restaurant.getEtages().get(etage).getTables().size();
		}
		
		int numero = Main.choixUtilisateur(0,nbTables); //numero du menu
		if (numero != 0) {
			// Nouveau numero de la table
			System.out.println("\nNouveau numéro de la table :");
			int nouveauNumero = Main.choixUtilisateur(1,Restaurant.NUMERO_MAX_TABLE);
			((Directeur) persConnectee).modifierNumeroTable(trouverTable(numero), nouveauNumero );
			System.out.println("Table " + (numero-1) + " modifiée en " + nouveauNumero);
		}
 	}
 	
	// Permet d'ajouter une table à un étage
 	public static void ajouterTableDirecteur() throws ClassNotFoundException, SQLException, IOException {

		// Affichage menu
		System.out.println("----------------------------------"
					     + "\n-------Ajouter une table--------"
	 					 + "\nListe des tables : " + listingTables()
	 					 + "\n----------------------------------"
	 					 + "\nVeuillez choisir l'étage de la table à ajouter, ou 0 pour retourner au menu");
		int etage = Main.choixUtilisateur(0, Restaurant.getEtages().size());
		
		if (etage != 0) {
			
			//numero de la table
			System.out.println("\nNuméro de la table : ");
			int numero = Main.choixUtilisateur(0,Restaurant.NUMERO_MAX_TABLE);
			
			//capacite de la table
			System.out.println("\nVeuillez saisir sa capacité, ou 0 pour annuler et revenir au menu");
			int capacite = Main.choixUtilisateur(0,Restaurant.CAPACITE_MAX_TABLE);
			
			if (capacite != 0) {
				((Directeur) persConnectee).ajouterTable(numero, capacite, Restaurant.getEtages().get(etage-1) );
				System.out.println("Table ajoutée");
			}
		}
 	}
 	
 	
 	// Permet de vider la base de données pour réinitialiser le restaurant
 	public static void viderBddDirecteur() throws ClassNotFoundException, SQLException, IOException {
 		//Sql.hardReset("hardResetPostgres");
 	}
 	
 // Menu principal du directeur
  	public static void menuPrincipalDirecteur() throws ClassNotFoundException, SQLException, IOException, ParseException {

  		// Affichage menu
  		System.out.println("----------------------------------"
  					+ "\n0 : Déconnexion"
  					+ "\n1 : Commander un ingredient"
  					+ "\n2 : Ajouter personnel"
  					+ "\n3 : Modifier personnel"
  					+ "\n4 : Supprimer personnel"
  					+ "\n5 : Ajouter etage"
  					+ "\n6 : Supprimer dernier etage"
  					+ "\n7 : Ajouter une table"
  					+ "\n8 : Modifier une table"
  					+ "\n9 : Supprimer une table"
  					+ "\n10: Ajouter un plat"
  					+ "\n11: Modifier plat"
  					+ "\n12: Modifier carte"
  					+ "\n13: Supprimer plat"
  					+ "\n14: Ajouter une réservation"
  					+ "\n15: Supprimer une réservation"
  					+ "\n16: Affecter des clients à une table"
  					+ "\n17: Affecter un serveur à une table"
  					+ "\n18: Prendre une commande"
  					+ "\n19: Editer une facture"
  					+ "\n20: Cuisiner un plat"
  					+ "\n21: Servir un plat"
  					+ "\n22: Nettoyer une table"
  					+ "\n23: Voir les statistiques"
  					+ "\n24: Vider la base de données"
  					+ "\n----------------------------------\n");

  		switch (Main.choixUtilisateur(0,24)) { // valeurChoixMin = 0

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
  			
  		// Ajouter etage
  		case 5:
  			ajouterEtage();
  			break;
  		
  		// Supprimer etage
  		case 6:
  			supprimerDernierEtage();
  			break;
  			
  		// Ajouter une table
  		case 7:
  			ajouterTableDirecteur();
  			break;

  		// Modifier le numéro d'une table
  		case 8:
  			modifierTableDirecteur();
  			break;
  			
  		// Supprimer une table
  		case 9:
  			supprimerTableDirecteur();
			break;
		// Ajouter un plat
  		case 10:
  		    ajouterPlatDirecteur();
			break;

  		case 11:

			break;

  		case 12:

			break;

  		case 13:

			break;
		// Ajouter une réservation
  		case 14:
  			ajouterReservation();
			break;
		// Supprimer une réservation
  		case 15:
  			supprimerReservation();
			break;
			
		// Affecter des clients à une table
  		case 16:
  			affecterClients();
			break;
		
		// Affecter un serveur à une table
  		case 17:
  			affecterServeurTable();
			break;
			
		// Prendre une commande
  		case 18:
  			prendreCommande();
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
		//Vider la BDD
  		case 24:
  			viderBddDirecteur();
			break;

  		default:
  			break;
  		}

  	}
  	
 
	private static void ajouterPlatDirecteur() {
	    System.out.println("----------------------------------"
			+ "\n-----Ajouter un plat------"
			+ "\n----------------------------------");
	    System.out.println("Veuillez saisir le nom du plat");
	    String nomPlat = scanner.nextLine();
	    System.out.println("Veuillez saisir le prix du plat");
	    String prixPlat = scanner.nextLine();
	    System.out.println("Veuillez saisir la durée de préparation du plat");
	    String dureePlat = scanner.nextLine();
	    System.out.println("Veuillez saisir le type du plat");
	    for (int i = 0; i < Type.values().length; i++) {
		System.out.println(i+1+": "+Type.values()[i].name());
	    }
	    String type = scanner.nextLine();
	    System.out.println("Veuillez saisir la catégorie du plat");
	    for (int i = 0; i < Categorie.values().length; i++) {
		System.out.println(i+1+": "+Type.values()[i].name());
	    }
	    String categorie = scanner.nextLine();
	    System.out.println("Veuillez saisir les ingédients de la recette");
	    for (int i = 0; i < Restaurant.getIngredients().size(); i++) {
		System.out.println(i+1+": "+Restaurant.getIngredients().get(i).getNom());
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
				|| !(uniquementChiffres(numPersonne) && (valeurIntOk(Integer.parseInt(numPersonne),0, Restaurant.getPersonnel().size()-1)  //personne n'existe pas
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
				|| (uniquementChiffres(numPersonne) && (!valeurIntOk(Integer.parseInt(numPersonne),0, Restaurant.getPersonnel().size()-1)  //personne n'existe pas
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
    
    
    // Renvoie la plus grande capacité parmi les tables du restaurant
    private static int capaciteMaxTables() {
    	int max = 0;
    	for (int etage = 0; etage < Restaurant.getEtages().size(); etage++) {
 			for (int table = 0; table < Restaurant.getEtages().get(etage).getTables().size(); table++) {
	 			if (Restaurant.getEtages().get(etage).getTables().get(table).getCapacite() > max) {
					max = Restaurant.getEtages().get(etage).getTables().get(table).getCapacite();
				}
 			}
 		}
    	return max;
    }
    
    // Renvoie le nombre de jours d'un mois
    private static int nombredejours(int annee, int mois) {
    	if(mois == 1
    	|| mois == 3
    	|| mois == 5
    	|| mois == 7
    	|| mois == 8
    	|| mois == 10
    	|| mois == 12) {
    	    return 31;
    	}
    	if (mois == 2) {
			if (annee % 4 != 0 || (annee % 100 == 0 && annee % 400 != 0 )) { //Année non bissextile
				return 28;
			}
			return 29; // Année bissextile
		}
    	return 30;
    	
    }
    
    // Transforme un entier en string et ajoute un 0 s'il est compris entre 0 et 9 (inclus)
    private static String infDixEnString(int nb) {
    	if (nb >= 0 && nb <= 10) {
			return "0" + nb;
		}
    	return String.valueOf(nb);
    }
    
    
    // Ajouter la réservation d'une table par un client
	private static void ajouterReservation() throws ParseException {

    	String dateReserve = "";
		System.out.println("----------------------------------"
			 	       + "\n-----Ajouter une réservation -----"
    				   + "\nVeuillez entrer l'année");
    	
    	int annee = choixUtilisateur(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.YEAR) + 1); //Année en cours ou année suivante    	
    	
    	System.out.println("Veuillez entrer le mois");
    	int mois = choixUtilisateur(1, 12);
    	
    	System.out.println("Veuillez entrer le jour");
    	dateReserve += infDixEnString(choixUtilisateur(1, nombredejours(annee,mois)) ); //vérifier en fonction des horaires de service
    	dateReserve += "/" + infDixEnString(mois);
    	dateReserve += "/" + String.valueOf(annee);
    	        
        System.out.println("Voulez-vous réserver lors du déjeuner (0) ou du diner (1) ?");
        int service = choixUtilisateur(0, 1);
        
        //vérifier en fonction des horaires de service
        System.out.println("Veuillez entrer l'heure");
        if (service == 0) {
        	dateReserve += " " +infDixEnString(choixUtilisateur(Restaurant.getHeureDejeunerOuverture().getHour(), Restaurant.getHeureDejeunerLimite().getHour()-1));
		}else {
			dateReserve += " " +infDixEnString(choixUtilisateur(Restaurant.getHeureDinerOuverture().getHour(), Restaurant.getHeureDinerLimite().getHour()-1));
		}
    	
    	System.out.println("Veuillez entrer les minutes");
    	dateReserve += ":" + infDixEnString(choixUtilisateur(0, 59)) + ":00";
    	
        // Date demandée par le client : exemple "27/12/2020 22:55:00"
        Date dateReservationSQL = new Timestamp(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dateReserve).getTime());
        // La date de l'appel est immédiate
       
        
        System.out.println("Veuillez saisir le nombre de personnes, ou 0 pour retourner au menu");
        int nbPersonne = choixUtilisateur(0, capaciteMaxTables());
        if (nbPersonne != 0) {
        	((Directeur) persConnectee).creationReservation(new Timestamp(new Date().getTime()), dateReservationSQL, nbPersonne);
        	System.out.println("Réservation ajoutée");
		}
	}
	
    // Supprimer une réservation 
	private static void supprimerReservation() {
        
		System.out.println("----------------------------------"
				 	   + "\n---Supprimer une réservation -----");
		for (int i = 1; i < Restaurant.getReservationsJour().size(); i++) {
			System.out.println(i + " : Table numero " + Restaurant.getReservationsJour().get(i).getTable().getNumero()
					+ " réservée le " + Restaurant.getReservationsJour().get(i).getDateReservation()
					+ " pour " + Restaurant.getReservationsJour().get(i).getTable().getCapacite() + " personnes"
					);
		}
		System.out.println("Veuillez taper le numero de la réservation à supprimer ou 0 pour revenir au menu");
        int reservation = choixUtilisateur(0, Restaurant.getReservationsJour().size()-1);
        if (reservation != 0) {
        	((Directeur) persConnectee).supprimerReservation(Restaurant.getReservationsJour().get(reservation - 1));
        	System.out.println("Réservation supprimée");
        }
    	
    }
		
	// Affecter des clients à une table
	private static void affecterClients() {
		System.out.println("----------------------------------"
						 + "\n-----Affecter des clients-------"
						 + "\nVeuillez entrer le nombre de personnes ou 0 pour revenir au menu");
        int nbPers = choixUtilisateur(0, capaciteMaxTables());
        if (nbPers != 0) {
        	((Directeur) persConnectee).creationAffectation(new Timestamp(new Date().getTime()), nbPers);
        	System.out.println(nbPers + " affectées" );
		}
    }
	
	// Affecter un serveur à une table
	private static void affecterServeurTable() {
		System.out.println("-----------------------------------"
					   + "\n--Affecter un serveur à une table--"
					   + "\nVeuillez choisir le numero du serveur ou 0 pour revenir au menu\n" + listingServeurs());
		 int numServeur = choixUtilisateur(0, nbServeurs());
		 if (numServeur != 0) {
			System.out.println("Parmi :\n"+ listingTables()
						   + "\nVeuillez choisir le numero correspondant à la table");
	        int numTable = choixUtilisateur(0, Restaurant.getToutesLesTables().size());
        
	        Serveur serveur = trouverServeur(numServeur);
	        System.out.println("num du serveur "+ numServeur +" correspond à " + serveur.getNom());
	        Table table = trouverTable(numTable);
        	((Directeur) persConnectee).affecterTableServeur(serveur, table);
        	System.out.println("Serveur " + serveur.getNom()
        					+ " affecté à la table numéro " + table.getNumero()
        					+ " (" + table.getCapacite() + " personnes)");
		 }
    }
	
	// Prendre une commande
	private static void prendreCommande() {
		System.out.println("-----------------------------------"
					   + "\n-------Prendre une commande--------"
					   + "\nVeuillez choisir si c'est un enfant (1) ou non (2), ou 0 pour revenir au menu\n" + listingServeurs());
		 int choix = choixUtilisateur(0, 2);
		 if (choix != 0) {
			boolean estEnfant = false;
			if (choix == 1) {
				 estEnfant = true;
			}
		

	        
        	((Directeur) persConnectee).creationCommande(new Timestamp(new Date().getTime())
        			, Plat plat, boolean estEnfant, Affectation affectation)
        	System.out.println("Commande prise : ");
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

 		switch (Main.choixUtilisateur(0,7)) { // valeurChoixMin = 0

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

 		switch (Main.choixUtilisateur(0,7)) { // valeurChoixMin = 0

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
 					+ "\n1: Voir l'état des tables"
 					+ "\n2: Prendre les commandes d'une table"
 					+ "\n3: Transmettre facture"
 					+ "\n----------------------------------\n");

 		switch (Main.choixUtilisateur(0,7)) { // valeurChoixMin = 0

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
 					+ "\n1: Nettoyer une table"
 					+ "\n----------------------------------\n");

 		switch (Main.choixUtilisateur(0,7)) { // valeurChoixMin = 0

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
				menuPrincipalDirecteur();
				break;

			case "restaurant.Maitrehotel":
				menuPrincipalMaitredhotel();
				break;

			case "restaurant.Cuisinier":
				menuPrincipalCuisinier();
				break;

			case "restaurant.Serveur":
				menuPrincipalServeur();
				break;

			case "restaurant.Assistant": // Assistant de service
				menuPrincipalAssistant();
				break;

			default:
				throw (new Exception("Problème de rôle"));
			}
			
		} while (true);
	}
}
