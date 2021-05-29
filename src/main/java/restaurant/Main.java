package restaurant;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

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

	// Vérifie que les caractères de "valeur" crrespondent un rôle
	// parmi : assistant ; serveur ; maitrehotel ; directeur ; cuisinier
	public static boolean estUnRole(String valeur) {

		if (valeur.equals("assistant") || valeur.equals("serveur") || valeur.equals("maitrehotel")
				|| valeur.equals("directeur") || valeur.equals("cuisinier")) {
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

		if (valeurChoixMax == -1) {
			System.out.println("Rien à sélectionner, retour au menu");
			return 0;
		}
		String choix = "";

		while (estNullOuVide(choix) || !uniquementChiffres(choix)
				|| !valeurIntOk(Integer.parseInt(choix), valeurChoixMin, valeurChoixMax)) {
			System.out.println(
					">Veuillez saisir votre choix (valeur allant de " + valeurChoixMin + " à " + valeurChoixMax + ")");
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
				liste += numero + " :" + Restaurant.getPersonnel().get(i).getNom() + " ("
						+ Restaurant.getPersonnel().get(i).getIdentifiant() + ")\n";
				numero += 1;
			}
		}
		return liste;
	}

	// Affiche la liste des tables
	public static Serveur trouverServeur(int numServeur) {

		int numero = 0;
		for (int numPersonnel = 0; numPersonnel < Restaurant.getPersonnel().size(); numPersonnel++) { // 0 = directeur
			if (Restaurant.getPersonnel().get(numPersonnel).getClass().getName().equals("restaurant.Serveur")) {
				numero += 1;
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
		int i = 1; // numero de la table dans le menu (0 étant réservé au retour)

		for (int etage = 0; etage < Restaurant.getEtages().size(); etage++) {
			liste += "\n<Etage " + (Restaurant.getEtages().get(etage).getNiveau()+1) + " >";
			for (int table = 0; table < Restaurant.getEtages().get(etage).getTables().size(); table++) {
				liste += "\n" + i + " : table " + Restaurant.getEtages().get(etage).getTables().get(table).getNumero()
						+ " avec une capacite de : "
						+ Restaurant.getEtages().get(etage).getTables().get(table).getCapacite() + " personnes"
						+ " (état : " + Restaurant.getEtages().get(etage).getTables().get(table).getEtat() + ")";
				i += 1;
			}
		}
		return liste;
	}

	// Affiche la liste des tables
	public static String listingCommandes() {
		String liste = "";
		for (int i = Restaurant.getCommandes().size() - 1; i >= 0; i--) { // De la plus ancienne à la plus récente
			liste += "Commande " + (i - 1) + ": " + Restaurant.getCommandes().get(i).getPlat().toString(); // On réserve
			// le 0 pour
			// le retour
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
		for (int i = 1; i < Restaurant.getPersonnel().size(); i++) { // On n'inclut pas le directeur
			liste += "\n " + i + ":" + Restaurant.getPersonnel().get(i).getNom() + "; role : "
					+ Restaurant.getPersonnel().get(i).getClass().getName().substring(11)
					+ " (identifiant : " + Restaurant.getPersonnel().get(i).getIdentifiant() + ")";
		}
		return liste;
	}

	// Affiche les plats de la carte
	public static String listingPlats() {
		String liste = "";
		for (int i = 0; i < Restaurant.getPlats().size(); i++) {
			if (Restaurant.getPlats().get(i).isDisponibleCarte()) {
				liste += "\n " + i + ":" + Restaurant.getPlats().get(i).toString();
			}

		}
		return liste;
	}

	// Compte le nombre de serveurs
	public static int nbServeurs() {
		int cpt = 0;
		for (int i = 0; i < Restaurant.getPersonnel().size(); i++) {
			if (Restaurant.getPersonnel().get(i).getClass().getName().equals("restaurant.Serveur")) {
				cpt += 1;
			}
		}
		return cpt;
	}

	// Permet de commander un ingrédient pour l'ajouter au stock
	public static void commanderIngredient() throws ClassNotFoundException, SQLException, IOException {

		// Affichage menu
		System.out.println("----------------------------------" + "\n-----Commander un ingredient------"
				+ "\nListe des ingrédients : " + listingIngredients() + "\n----------------------------------"
				+ "\nVeuillez taper un nom si vous voulez commander un ingrédient qui ne figure"
				+ " pas dans la liste, ou le numéro d'un des ingrédients de la liste");

		String choix;
		int qtIngredient = 0;
		choix = scanner.nextLine();
		// Contrôle de l'entrée
		while (estNullOuVide(choix) // vide
				|| (!uniquementLettres(choix) && !uniquementChiffres(choix)) // mélange de lettres/chiffres ou
				// caractères spéciaux
				|| (uniquementLettres(choix) && choix.length() > Restaurant.getTAILLE_MAX_NOM_INGREDIENT()) // chaine et
				// ne
				// respecte
				// pas la
				// longueur
				// maximale
				|| (uniquementChiffres(choix) && (Restaurant.getIngredients().size() == 0 // pas d'ingrédients dans la
						// BDD
						|| !valeurIntOk(Integer.parseInt(choix), 0, Restaurant.getIngredients().size() - 1))) // valeur
		// qui
		// n'existe
		// pas
		) {
			System.out.println("Erreur, veuillez réessayer");
			choix = scanner.nextLine();
		}

		if (uniquementLettres(choix)) {
			// Nouvel ingrédient
			String nomIngredient = choix.toLowerCase();
			System.out.println(">Quantité de " + choix + " à commander ?");
			qtIngredient = choixUtilisateur(1, Restaurant.getQUANTITE_MAX_COMMANDE()); // Quantite max par commande :
			// 500
			((Directeur) persConnectee).ajouterIngredient(nomIngredient);
			((Directeur) persConnectee).commanderIngredient(
					Restaurant.getIngredients().get(Restaurant.getIngredients().size() - 1), qtIngredient); // Dernier
			System.out.println("Commande passée (quantite : " + qtIngredient + ")"); // inséré

		} else if (uniquementChiffres(choix)) {
			// MAJ quantite d'un ingrédient existant
			System.out.println(">Quantité à commander ?");
			qtIngredient = choixUtilisateur(1, Restaurant.getQUANTITE_MAX_COMMANDE()); // Quantite max par commande :
			// 500
			((Directeur) persConnectee).commanderIngredient(Restaurant.getIngredients().get(Integer.parseInt(choix)),
					qtIngredient); // Dernier
			System.out.println("Commande passée (quantite : " + qtIngredient + ")"); // inséré
		}
	}

	// Permet d'ajouter un étage au restaurant
	public static void ajouterEtage() throws ClassNotFoundException, SQLException, IOException {

		// Affichage menu
		System.out.println("----------------------------------" + "\n-------Ajouter un étage-----------"
				+ "\nListe des étages : " + listingEtages() + "\n----------------------------------"
				+ "\n1 pour valider, 0 pour annuler");

		if (Main.choixUtilisateur(0, 1) == 1) {
			((Directeur) persConnectee).ajouterEtage();
		}
		System.out.println("Etage ajouté");

	}

	// Permet de supprimer le dernier étage du restaurant
	public static void supprimerDernierEtage() throws ClassNotFoundException, SQLException, IOException {

		// Affichage menu
		System.out.println("----------------------------------" + "\n--------Supprimer un étage--------"
				+ "\nListe des étages : " + listingEtages() + "\n----------------------------------"
				+ "\n1 pour valider, 0 pour annuler");

		if (Main.choixUtilisateur(0, 1) == 1) {
			((Directeur) persConnectee).supprimerDernierEtage();
		}
		System.out.println("Dernier étage supprimé");
	}

	// Permet de trouver une table via son numéro
	public static Table trouverTable(int numero) {
		int nbTable = 0;
		for (int etage = 0; etage < Restaurant.getEtages().size(); etage++) {
			for (int table = 0; table < Restaurant.getEtages().get(etage).getTables().size(); table++) {
				if (nbTable == numero) {
					return Restaurant.getEtages().get(etage).getTables().get(table);
				}
				nbTable += 1;

			}
		}
		return null;
	}

	// Permet de trouver un plat via son numero
	public static Plat trouverPlat(int numPlat) {
		for (int plat = 0; plat < Restaurant.getPlats().size(); plat++) {
			if (numPlat == plat) {
				return Restaurant.getPlats().get(plat);
			}
		}
		return null;
	}

	private static void modifierCarte() {
	    System.out.println("----------------------------------" + "\n-----Modifier la carte------"
	        + "\n----------------------------------");
	    String choix = "1";
	    while (Integer.parseInt(choix) != 0) {
	        System.out.println("Voulez vous ajouter ou supprimer un plat ?");
	        System.out.println("1: Ajouter un plat à la carte");
	        System.out.println("2: Supprimer un plat de la carte");
	        choix = scanner.nextLine();
	        List<Plat> carte = Restaurant.getPlats().stream().filter(plat -> plat.isDisponibleCarte())
	            .collect(Collectors.toList());
	        List<Plat> nonCarte = Restaurant.getPlats().stream().filter(plat -> !plat.isDisponibleCarte())
	            .collect(Collectors.toList());
	        String plat = "1";
	        switch (Integer.parseInt(choix)) {
	        case 1:
	            if(carte.isEmpty()) {
	            System.out.println("Pas de plats à ajouter");
	            }
	            else {
	            for (int i = 0; i < nonCarte.size(); i++) {
	            System.out.println(i + 1 + ": " + nonCarte.get(i).getNom());
	            }
	            plat = scanner.nextLine();
	            ((Directeur) persConnectee).modifierCartePlat(nonCarte.get(Integer.parseInt(plat) - 1), true);}
	        break;

	        case 2:
	            if(carte.isEmpty()) {
	            System.out.println("Pas de plats à supprimer");
	            }
	            else {
	            for (int i = 0; i < carte.size(); i++) {
	                System.out.println(i + 1 + ": " + carte.get(i).getNom());
	            }
	            plat = scanner.nextLine();
	            ((Directeur) persConnectee).modifierCartePlat(carte.get(Integer.parseInt(plat) - 1), false);
	            }
	        break;

	        default:
	        break;
	        }
	    }
	    }

	// Permet de trouver un étage via une table
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

	// Permet de trouver une affectation
	public static Affectation trouverAffectation(int numTable, Date dateCommande) {

		Table table = trouverTable(numTable);
		for (int i = 0; i < Restaurant.getAffectationsJour().size(); i++) {

			if (Restaurant.getAffectationsJour().get(i).getTable().equals(table)
					&& Restaurant.getAffectationsJour().get(i).getDateDebut().before(dateCommande)
					&& Restaurant.getAffectationsJour().get(i).getDateFin() == null) {
				return Restaurant.getAffectationsJour().get(i);
			}
		}
		return null;
	}

	// Permet de supprimer une table d'un étage
	public static void supprimerTable() throws ClassNotFoundException, SQLException, IOException {

		// Affichage menu
		System.out.println("----------------------------------" + "\n-----Supprimer une table--------"
				+ "\nListe des tables : " + listingTables() + "\n----------------------------------"
				+ "\nVeuillez choisir le numero (menu) correspondant à la table à supprimer, ou 0 pour retourner au menu");

		// numero de la table
		System.out.println("\nNuméro de la table :");

		int nbTables = 0; // numero de la table dans le menu
		for (int etage = 0; etage < Restaurant.getEtages().size(); etage++) {
			nbTables += Restaurant.getEtages().get(etage).getTables().size();
		}

		int numero = Main.choixUtilisateur(0, nbTables); // numero du menu
		if (numero != 0) {
			((Directeur) persConnectee).supprimerTable(trouverTable(numero),
					trouverEtage(trouverTable(numero)).getTables());
			System.out.println("Table supprimée");
		}

	}

	// Permet de modifier le numéro d'une table
	public static void modifierTable() throws ClassNotFoundException, SQLException, IOException {

		// Affichage menu
		System.out.println("----------------------------------" + "\n-------Modifier une table---------"
				+ "\nListe des tables : " + listingTables() + "\n----------------------------------"
				+ "\nVeuillez choisir le numero (menu) correspondant à la table à modifier, ou 0 pour retourner au menu");

		// Numero de la table
		System.out.println("\nTable à modifier :");

		int nbTables = 0; // numero de la table dans le menu
		for (int etage = 0; etage < Restaurant.getEtages().size(); etage++) {
			nbTables += Restaurant.getEtages().get(etage).getTables().size();
		}

		int numero = Main.choixUtilisateur(0, nbTables); // numero du menu
		if (numero != 0) {
			// Nouveau numero de la table
			System.out.println("\nNouveau numéro de la table :");
			int nouveauNumero = Main.choixUtilisateur(1, Restaurant.getNUMERO_MAX_TABLE());
			((Directeur) persConnectee).modifierNumeroTable(trouverTable(numero), nouveauNumero);
			System.out.println("Table " + (numero - 1) + " modifiée en " + nouveauNumero);
		}
	}

	// Permet d'ajouter une table à un étage
	public static void ajouterTable() throws ClassNotFoundException, SQLException, IOException {

		// Affichage menu
		System.out.println("----------------------------------" + "\n-------Ajouter une table--------"
				+ "\nListe des tables : " + listingTables() + "\n----------------------------------"
				+ "\nVeuillez choisir l'étage de la table à ajouter, ou 0 pour retourner au menu");
		int etage = Main.choixUtilisateur(0, Restaurant.getEtages().size());

		if (etage != 0) {

			// numero de la table
			System.out.println("\nNuméro de la table : ");
			int numero = Main.choixUtilisateur(0, Restaurant.getNUMERO_MAX_TABLE());

			// capacite de la table
			System.out.println("\nVeuillez saisir sa capacité, ou 0 pour annuler et revenir au menu");
			int capacite = Main.choixUtilisateur(0, Restaurant.getCAPACITE_MAX_TABLE());

			if (capacite != 0) {
				((Directeur) persConnectee).ajouterTable(numero, capacite, Restaurant.getEtages().get(etage - 1));
				System.out.println("Table ajoutée");
			}
		}
	}

	// Permet de vider la base de données pour réinitialiser le restaurant
	private static void viderBdd() {
		Sql sql = new Sql();
		sql.hardResetPg();
		System.exit(1);
	}

	private static void viderBddDirecteurAvecJeudonnees() {
		Sql sql = new Sql();
		sql.hardResetPg();
		sql.insererJeuDonnees();
		System.exit(1);
	}

	// Menu principal du directeur
	public static void menuPrincipalDirecteur()
			throws ClassNotFoundException, SQLException, IOException, ParseException {

		// Affichage menu
		System.out.println("----------------------------------" + "\n0 : Déconnexion" + "\n1 : Commander/Ajouter un ingredient"
				+ "\n2 : Ajouter personnel" + "\n3 : Modifier personnel" + "\n4 : Supprimer personnel"
				+ "\n5 : Ajouter etage" + "\n6 : Supprimer dernier etage" + "\n7 : Ajouter une table"
				+ "\n8 : Modifier une table" + "\n9 : Supprimer une table" + "\n10: Ajouter un plat"
				+ "\n11: Modifier plat" + "\n12: Modifier carte" + "\n13: Supprimer plat"
				+ "\n14: Ajouter une réservation" + "\n15: Supprimer une réservation"
				+ "\n16: Affecter des clients à une table" + "\n17: Affecter un serveur à une table"
				+ "\n18: Prendre une commande" + "\n19: Editer une facture" + "\n20: Cuisiner un plat"
				+ "\n21: Servir un plat" + "\n22: Nettoyer table" + "\n23: Voir les statistiques"
				+ "\n24: Vider la base de données (ferme le programme)" + "\n25: Vider la BDD et ajouter un jeu de données (ferme le programme)"
				+ "\n----------------------------------\n");

		switch (Main.choixUtilisateur(0, 25)) { // valeurChoixMin = 0

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
			ajouterPersonnel();
			break;

		// Modifier le rôle d'un membre du personnel
		case 3:
			modifierPersonnel();
			break;

		// Supprimer personnel
		case 4:
			supprimerPersonnel();
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
			ajouterTable();
			break;

		// Modifier le numéro d'une table
		case 8:
			modifierTable();
			break;

		// Supprimer une table
		case 9:
			supprimerTable();
			break;
		// Ajouter un plat
		case 10:
			ajouterPlat("restaurant.Directeur");
			break;
		// Modifier un plat
		case 11:
			modifierPlat();
			break;
		// Modifier la carte
		case 12:
			modifierCarte();
			break;
		// Supprimer un plat
		case 13:
			supprimerPlat();
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
			affecterServeurTable("restaurant.Directeur");
			break;

		// Prendre une commande
		case 18:
			prendreCommande("restaurant.Directeur");
			break;

		// Editer une facture
		case 19:
			editerFacture("restaurant.Directeur");
			break;
		// Cuisiner un plat
		case 20:
			cuisinerUnPlat("restaurant.Directeur");
			break;
		// Servir un plat
		case 21:
			servirUnPlat();
			break;
		// Nettoyer table
		case 22:
			nettoyerUneTable("restaurant.Directeur");
			break;

		case 23:
			// Afficher les statistiques
			montrerStats();
			break;
		// Vider la BDD
		case 24:
			viderBdd();
			break;
		// Vider la BDD
		case 25:
			viderBddDirecteurAvecJeudonnees();
			break;
		default:
			break;
		}

	}

	private static void montrerStats() {
		Sql sql = new Sql();
		System.out.println("Statistiques du restaurant : ");

		// CLEAR
		System.out.println("Revenu quotidien : " + sql.revenuQuotidien() + "€");
		System.out.println("Revenu hebdomadaire : " + sql.revenuHebdomadaire()+ "€");
		System.out.println("Revenu mensuel : " + sql.revenuMensuel()+ "€");
		System.out.println("Profit réalisé sur le déjeuner du jour : " + sql.profitDejeunerJour()+ "€");
		System.out.println("Profit réalisé sur le dîner du jour : " + sql.profitDinerJour()+ "€");
		System.out.println("Profit réalisé sur tous les déjeuners : " + sql.profitDejeunerAlltime()+ "€");
		System.out.println("Profit réalisé sur tous les dîners : " + sql.profitDinerAlltime()+ "€");
		System.out.println("Temps de préparation moyen des plats : " + sql.tempsPreparationMoyen()+ "secondes");
		System.out.println("Temps de rotation moyen des clients : " + sql.tempsRotationMoyen()+ "minutes");
		System.out.println("Plats les plus populaires : ");
		System.out.println("Nom du plat \t\t\t\t\t\t\t\t\t Nombre de vente");
		sql.popularitePlats().forEach((nom, nombreVentes) -> {
			System.out.println(nom + " : " + nombreVentes);
		});
		System.out.println("Somme gagnée par plat : ");
		sql.partPlatRecette().forEach((nom, nombreVentes) -> {
			System.out.println(nom + " : " + nombreVentes);
		});

	}

	private static void ajouterPlat(String role) {
		System.out.println("----------------------------------" + "\n-----Ajouter un plat------"
				+ "\n----------------------------------");
		System.out.println("Veuillez saisir le nom du plat");
		String nomPlat = scanner.nextLine();
		System.out.println("Veuillez saisir le prix du plat");
		String prixPlat = scanner.nextLine();
		System.out.println("Veuillez saisir la durée de préparation du plat");
		String dureePlat = scanner.nextLine();
		System.out.println("Veuillez saisir le type du plat");
		for (int i = 0; i < Type.values().length; i++) {
			System.out.println(i + 1 + ": " + Type.values()[i].name());
		}
		String type = scanner.nextLine();
		System.out.println("Veuillez saisir la catégorie du plat");
		for (int i = 0; i < Categorie.values().length; i++) {
			System.out.println(i + 1 + ": " + Categorie.values()[i].name());
		}
		String categorie = scanner.nextLine();
		String ingredient = "1";
		HashMap<Ingredient, Integer> recette = new HashMap<Ingredient, Integer>();
		while (Integer.parseInt(ingredient) != Restaurant.getIngredients().size() + 1) {
			System.out.println("Veuillez saisir les ingédients de la recette");
			for (int i = 0; i < Restaurant.getIngredients().size(); i++) {
				System.out.println(i + 1 + ": " + Restaurant.getIngredients().get(i).getNom());
			}
			System.out.println(Restaurant.getIngredients().size() + 1 + ": Valider");
			ingredient = scanner.nextLine();
			if (Integer.parseInt(ingredient) != Restaurant.getIngredients().size() + 1) {
				System.out.println("Veuillez saisir la quantité");
				String quantite = scanner.nextLine();
				recette.put(Restaurant.getIngredients().get(Integer.parseInt(ingredient) - 1),
						Integer.parseInt(quantite));
			}
		}
		switch (role) {
		case "restaurant.Directeur":
			((Directeur) persConnectee).creerPlat(nomPlat, Double.parseDouble(prixPlat), Integer.parseInt(dureePlat),
					false, Type.values()[Integer.parseInt(type) - 1],
					Categorie.values()[Integer.parseInt(categorie) - 1], recette);
			break;
		case "restaurant.Cuisinier":
			((Cuisinier) persConnectee).creerPlat(nomPlat, Double.parseDouble(prixPlat), Integer.parseInt(dureePlat),
					false, Type.values()[Integer.parseInt(type) - 1],
					Categorie.values()[Integer.parseInt(categorie) - 1], recette);
			break;

		default:
			break;
		}

	}

	private static void modifierPlat() {
		System.out.println("----------------------------------" + "\n-----Modifier un plat------"
				+ "\n----------------------------------");
		String plat = "1";
		while (Integer.parseInt(plat) != 0) {
			System.out.println("Veuillez sélectionner un plat");
			for (int i = 0; i < Restaurant.getPlats().size(); i++) {
				System.out.println(i + 1 + ": " + Restaurant.getPlats().get(i).getNom() + " "
						+ Restaurant.getPlats().get(i).getType() + " " + Restaurant.getPlats().get(i).getCategorie());
			}
			plat = scanner.nextLine();
			System.out.println("Modifier le prix ou la durée de préparation ?");
			System.out.println("1: Prix");
			System.out.println("2: Durée de préparation");
			String choix = "1";
			String nombre;
			choix = scanner.nextLine();
			switch (Integer.parseInt(choix)) {
			case 1:
				System.out.println("Veuillez saisir le nouveau prix");
				nombre = scanner.nextLine();
				((Directeur) persConnectee).modifierPrixPlat(Restaurant.getPlats().get(Integer.parseInt(plat) - 1),
						Double.parseDouble(nombre));
				break;

			case 2:
				System.out.println("Veuillez saisir la nouvelle durée de préparation");
				nombre = scanner.nextLine();
				((Directeur) persConnectee).modifierPrixPlat(Restaurant.getPlats().get(Integer.parseInt(plat) - 1),
						Integer.parseInt(nombre));
				break;

			default:
				break;
			}
		}
	}

	private static void supprimerPlat() {
		System.out.println("----------------------------------" + "\n-----Supprimer un plat------"
				+ "\n----------------------------------");
		String plat = "1";
		while (Integer.parseInt(plat) != 0) {
			System.out.println("Veuillez choisir un plat à supprimer");
			for (int i = 0; i < Restaurant.getPlats().size(); i++) {
				System.out.println(i + 1 + ": " + Restaurant.getPlats().get(i).getNom());
			}
			plat = scanner.nextLine();
			((Directeur) persConnectee).supprimerPlat(Restaurant.getPlats().get(Integer.parseInt(plat) - 1));
		}
	}

	// Modifier le rôle d'un membre du personnel
	private static void supprimerPersonnel() {
		// Affichage
		System.out.println("----------------------------------" + "\n-----Supprimer du personnel------"
				+ "\nListe du personnel : " + listingPersonnel() + "\n----------------------------------"
				+ "\nVeuillez saisir le numéro du membre à supprimer, ou 0 pour revenir au menu");
		// numéro
		String numPersonne = scanner.nextLine();
		// Contrôle de l'entrée
		while (estNullOuVide(numPersonne) // vide
				|| !uniquementChiffres(numPersonne) // pas que des chiffres
				|| !(uniquementChiffres(numPersonne)
						&& (valeurIntOk(Integer.parseInt(numPersonne), 0, Restaurant.getPersonnel().size() - 1) // personne
								// n'existe
								// pas
								|| numPersonne.equals("0"))) // valeur de retour
		) {
			System.out.println("Erreur, veuillez réessayer");
			numPersonne = scanner.nextLine();
		}

		// Permet de revenir au menu (annuler)
		if (!numPersonne.equals("0")) {

			((Directeur) persConnectee).supprimerPersonnel(Restaurant.getPersonnel().get(Integer.parseInt(numPersonne)),
					Restaurant.getPersonnel());
			System.out.println("Membre supprimé");
		}
	}

	// Modifier le rôle d'un membre du personnel
	private static void modifierPersonnel() {
		// Affichage
		System.out.println("----------------------------------" + "\n-----Modifier du personnel------"
				+ "\nListe du personnel : " + listingPersonnel() + "\n----------------------------------"
				+ "\nVeuillez saisir le numéro de la personne à modifier, ou 0 pour revenir au menu");
		// numéro
		int numPersonne = choixUtilisateur(0, Restaurant.getPersonnel().size()-1);
		// Contrôle de l'entrée
		
		// Permet de revenir au menu (annuler)
		if (numPersonne != 0) {

			System.out.println(
					"\nParmi assistant, serveur, maitrehotel, directeur, cuisinier;\nVeuillez saisir le nouveau role de "
							+ Restaurant.getPersonnel().get(numPersonne).getNom()
							+ " qui est actuellement " + Restaurant.getPersonnel().get(numPersonne)
									.getClass().getName().substring(11));
			String role = scanner.nextLine();
			// Contrôle de l'entrée
			while (estNullOuVide(role) // vide
					|| !uniquementLettres(role) // pas que des lettres
					|| !estUnRole(role) // n'est pas un rôle
			) {
				System.out.println("Erreur, veuillez réessayer");
				role = scanner.nextLine();
			}

			((Directeur) persConnectee).modifierPersonnel(Restaurant.getPersonnel().get(numPersonne),
					role);
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
		if (mois == 1 || mois == 3 || mois == 5 || mois == 7 || mois == 8 || mois == 10 || mois == 12) {
			return 31;
		}
		if (mois == 2) {
			if (annee % 4 != 0 || (annee % 100 == 0 && annee % 400 != 0)) { // Année non bissextile
				return 28;
			}
			return 29; // Année bissextile
		}
		return 30;

	}

	// Transforme un entier en string et ajoute un 0 s'il est compris entre 0 et 9
	// (inclus)
	private static String infDixEnString(int nb) {
		if (nb >= 0 && nb <= 10) {
			return "0" + nb;
		}
		return String.valueOf(nb);
	}

	// Ajouter la réservation d'une table par un client
	private static void ajouterReservation() throws ParseException {

		String dateReserve = "";
		System.out.println("----------------------------------" + "\n-----Ajouter une réservation -----"
				+ "\nVeuillez entrer l'année");

		int annee = choixUtilisateur(Calendar.getInstance().get(Calendar.YEAR),
				Calendar.getInstance().get(Calendar.YEAR) + 1); // Année en cours ou année suivante

		System.out.println("Veuillez entrer le mois");
		int mois = choixUtilisateur(1, 12);

		System.out.println("Veuillez entrer le jour");
		dateReserve += infDixEnString(choixUtilisateur(1, nombredejours(annee, mois))); // vérifier en fonction des
		// horaires de service
		dateReserve += "/" + infDixEnString(mois);
		dateReserve += "/" + String.valueOf(annee);

		System.out.println("Voulez-vous réserver lors du déjeuner (0) ou du diner (1) ?");
		int service = choixUtilisateur(0, 1);

		// vérifier en fonction des horaires de service
		System.out.println("Veuillez entrer l'heure");
		if (service == 0) {
			dateReserve += " " + infDixEnString(choixUtilisateur(Restaurant.getHeureDejeunerOuverture().getHour(),
					Restaurant.getHeureDejeunerLimite().getHour() - 1));
		} else {
			dateReserve += " " + infDixEnString(choixUtilisateur(Restaurant.getHeureDinerOuverture().getHour(),
					Restaurant.getHeureDinerLimite().getHour() - 1));
		}

		System.out.println("Veuillez entrer les minutes");
		dateReserve += ":" + infDixEnString(choixUtilisateur(0, 59)) + ":00";

		// Date demandée par le client : exemple "27/12/2020 22:55:00"
		Date dateReservationSQL = new Timestamp(
				new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dateReserve).getTime());
		// La date de l'appel est immédiate

		System.out.println("Veuillez saisir le nombre de personnes, ou 0 pour retourner au menu");
		int nbPersonne = choixUtilisateur(0, capaciteMaxTables());
		if (nbPersonne != 0) {
			((Directeur) persConnectee).creationReservation(new Timestamp(new Date().getTime()), dateReservationSQL,
					nbPersonne);
			System.out.println("Réservation ajoutée");
		}
	}

	// Supprimer une réservation
	private static void supprimerReservation() {

		System.out.println("----------------------------------" + "\n---Supprimer une réservation -----");
		for (int i = 1; i < Restaurant.getReservationsJour().size(); i++) {
			System.out.println(i + " : Table numero " + Restaurant.getReservationsJour().get(i).getTable().getNumero()
					+ " réservée le " + Restaurant.getReservationsJour().get(i).getDateReservation() + " pour "
					+ Restaurant.getReservationsJour().get(i).getTable().getCapacite() + " personnes");
		}
		System.out.println("Veuillez taper le numero de la réservation à supprimer ou 0 pour revenir au menu");
		int reservation = choixUtilisateur(0, Restaurant.getReservationsJour().size() - 1);
		if (reservation != 0) {
			((Directeur) persConnectee).supprimerReservation(Restaurant.getReservationsJour().get(reservation - 1));
			System.out.println("Réservation supprimée");
		}

	}

	// Affecter des clients à une table
	private static void affecterClients() {
		System.out.println("----------------------------------" + "\n-----Affecter des clients-------"
				+ "\nVeuillez entrer le nombre de personnes ou 0 pour revenir au menu");
		int nbPers = choixUtilisateur(0, capaciteMaxTables());
		if (nbPers != 0) {
			((Directeur) persConnectee).creationAffectation(new Timestamp(new Date().getTime()), nbPers);
			System.out.println(nbPers + " affectées");
		}
	}

	// Affecter un serveur à une table
	private static void affecterServeurTable(String role) {
		System.out.println("-----------------------------------" + "\n--Affecter un serveur à une table--"
				+ "\nVeuillez choisir le numero du serveur ou 0 pour revenir au menu\n" + listingServeurs());
		int numServeur = choixUtilisateur(0, nbServeurs());
		if (numServeur != 0) {
			System.out.println("Parmi :\n" + listingTables() + "\nVeuillez choisir le numero correspondant à la table");
			int numTable = choixUtilisateur(1, Restaurant.getToutesLesTables().size())-1;

			Serveur serveur = trouverServeur(numServeur);
			Table table = trouverTable(numTable);
			switch (role) {
			case "restaurant.Maitrehotel":
				((Maitrehotel) persConnectee).affecterTableServeur(serveur, table);
				System.out.println("Serveur " + serveur.getNom() + " affecté à la table numéro " + table.getNumero() + " ("
						+ table.getCapacite() + " personnes)");
				break;
			case "restaurant.Directeur":
				((Directeur) persConnectee).affecterTableServeur(serveur, table);
				System.out.println("Serveur " + serveur.getNom() + " affecté à la table numéro " + table.getNumero() + " ("
						+ table.getCapacite() + " personnes)");
				break;

			default:
				break;
			}
			
		}
	}

	// Nettoyer une table
	private static void nettoyerUneTable(String role) {

		System.out.println("Parmi :\n" + listingTables()
				+ "\nVeuillez saisir le numero correspondant à la table à nettoyer, ou 0 pour retourner au menu");
		int numTable = choixUtilisateur(0, Restaurant.getToutesLesTables().size());
		if (numTable != 0) {
			numTable -= 1; // Car on utilisait le 0 pour le retour, mais une table peut bien avoir le
			// numéro 0

			Table table = trouverTable(numTable);
			
			if (table != null) {
				switch (role) {
				case "restaurant.Directeur":
					((Directeur) persConnectee).modifierEtatTable(table, EtatTable.Libre);
					System.out.println("Table nettoyée");
					break;
				case "restaurant.Assistant":
					((Assistant) persConnectee).modifierEtatTable(table, EtatTable.Libre);
					System.out.println("Table nettoyée");
					break;

				default:
					break;
				}
			} else {
				System.out.println("Mauvaise table sélectionnée");
			}
		}
	}

	// Prendre une commande
	private static void prendreCommande(String role) {
		System.out.println("-----------------------------------" + "\n-------Prendre une commande--------"
				+ "\nVeuillez choisir si c'est un enfant (1) ou non (2), ou 0 pour revenir au menu\n");
		int choix = choixUtilisateur(0, 2);
		if (choix != 0) {
			boolean estEnfant = false;
			if (choix == 1) {
				estEnfant = true;
			}
			System.out.println("Veuillez choisir votre plat parmi : " + listingPlats());
			int plat = choixUtilisateur(0, Restaurant.getPlats().size() - 1);

			System.out.println("Veuillez choisir votre numéro de table : " + listingTables());
			int numTable = choixUtilisateur(1, Restaurant.getToutesLesTables().size()) - 1;
			Affectation aff = trouverAffectation(numTable, new Timestamp(new Date().getTime()));
			
			//Si la table est occupée (et pas sale) on prend la commande
			if (aff != null && trouverTable(numTable).getEtat().name().equals(EtatTable.Occupe.name())) {
				
				switch (role) {
				
				case "restaurant.Serveur":
					((Serveur) persConnectee).creationCommande(new Timestamp(new Date().getTime()), trouverPlat(plat),
							estEnfant, aff);
					System.out.println("Commande effectuée (" + plat + ")");
					break;
				case "restaurant.Directeur":
					((Directeur) persConnectee).creationCommande(new Timestamp(new Date().getTime()), trouverPlat(plat),
							estEnfant, aff);
					System.out.println("Commande effectuée (" + plat + ")");
					break;

				default:
					break;
				}
				
			} else {
				System.out.println("Cette table n'est pas occupée");
			}
			

		}
	}

	// Editer une facture (lorsque le client part -> table sale)
	private static void editerFacture(String role) {
		System.out.println("-----------------------------------" + "\n--------Editer une facture---------"
				+ "\nVeuillez choisir votre numéro de table : " + listingTables()
				+ "\nou saisir 0 pour revenir au menu");
		int numTable = choixUtilisateur(0, Restaurant.getToutesLesTables().size());

		if (numTable != 0) {
			numTable -=1;
			Affectation aff = trouverAffectation(numTable, new Timestamp(new Date().getTime()));
			if (aff != null) { // Si l'affectation existe bien
				
				switch (role) {
				case "restaurant.Maitrehotel":
					((Maitrehotel) persConnectee).creerFacture(aff);
					((Maitrehotel) persConnectee).modifierEtatTable(trouverTable(numTable), EtatTable.Sale);
					System.out.println("Facture éditée");
					break;
				case "restaurant.Directeur":
					((Directeur) persConnectee).creerFacture(aff);
					((Directeur) persConnectee).modifierEtatTable(trouverTable(numTable), EtatTable.Sale);
					System.out.println("Facture éditée");
					break;

				default:
					break;
				}

			} else {
				System.out.println("Actuellement pas d'affectation pour cet table ");
			}
		}
	}

	
	// Menu principal du maitre d hotel
	public static void menuPrincipalMaitredhotel() throws ClassNotFoundException, SQLException, IOException {

		// Affichage menu
		System.out.println("----------------------------------"
						+ "\n0: Déconnexion"
						+ "\n1: Affecter un serveur à une table"
						+ "\n2: Editer une facture"
						+ "\n----------------------------------\n");

		switch (Main.choixUtilisateur(0, 2)) { // valeurChoixMin = 0

		// Déconnexion
		case 0:
			Main.persConnectee = null;
			System.out.println("Déconnexion....\nVous avez été déconnecté.\n");
			break;

		// Affecter un serveur à une table
		case 1:
			affecterServeurTable("restaurant.Maitrehotel");
			break;

		//Editer une facture
		case 2:
			editerFacture("restaurant.Maitrehotel");
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

		switch (Main.choixUtilisateur(0, 3)) { // valeurChoixMin = 0

		// Déconnexion
		case 0:
			Main.persConnectee = null;
			System.out.println("Déconnexion....\nVous avez été déconnecté.\n");
			break;

		// Définir un plat
		case 1:
			ajouterPlat("restaurant.Cuisinier");
			break;

		// Consulter les commandes
		case 2:
			consulterCommandes();
			break;

		// Cuisiner un plat
		case 3:
			cuisinerUnPlat("restaurant.Cuisinier");
			break;

		default:
			break;
		}
	}
	
	// Menu principal du serveur
	public static void menuPrincipalServeur() throws ClassNotFoundException, SQLException, IOException {

		// Affichage menu
		System.out.println("----------------------------------"
				// Affichage des tables de son étage + couleurs (état) associées
					+ "\n0: Déconnexion"
					+ "\n1: Voir l'état des tables"
					+ "\n2: Prendre les commandes d'une table"
					+ "\n----------------------------------\n");

		switch (Main.choixUtilisateur(0, 2)) { // valeurChoixMin = 0

		// Déconnexion
		case 0:
			Main.persConnectee = null;
			System.out.println("Déconnexion....\nVous avez été déconnecté.\n");
			break;

		// Voir l'état des tables
		case 1:
			System.out.println(listingTables());
			break;
			
		// Prendre les commandes d'une table
		case 2:
			prendreCommande("restaurant.Serveur");
			break;

		default:
			break;
		}
	};


	// Cuisiner un plat : modification de l'état du plat commandé
	private static void cuisinerUnPlat(String role) {

		System.out.println("-----------------------------------" + "\n---------Cuisiner un plat----------"
				+ "\nVeuillez entrer 0 pour retourner au menu \nou le numéro de la commande à traiter :\n"
				+ listingCommandes());

		int numCommande = choixUtilisateur(0, Restaurant.getCommandes().size()) - 1;
		if (numCommande != 0) {
			Commande commande = Restaurant.getCommandes().get(numCommande);
			// EN_PREPARATION puis PRETE mais on n'attends pas via l'interface
			
			switch (role) {
			case "restaurant.Directeur":
				((Directeur) persConnectee).modifierEtatCommande(commande, Etat.PRETE);
				System.out.println("Commande " + commande.getId() + " prête");
				break;
			case "restaurant.Cuisinier":
				((Cuisinier) persConnectee).modifierEtatCommande(commande, Etat.PRETE);
				System.out.println("Commande " + commande.getId() + " prête");
				break;

			default:
				break;
			}
			
		}
	}
	
	// Consulter les commandes passées
	private static void consulterCommandes() {

		System.out.println("-----------------------------------"
					   + "\n------Consulter les commandes------"
				+ "\nCommandes :\n"
				+ listingCommandes());
	}

	
	// Servir un plat : modification de l'état de la commande à "servie" (plat
	// commandé)
	private static void servirUnPlat() {
		System.out.println("-----------------------------------" + "\n---------Servir un plat----------"
				+ "\nVeuillez entrer 0 pour retourner au menu \nou le numéro de la commande à servir :\n"
				+ listingCommandes());

		int numCommande = choixUtilisateur(0, Restaurant.getCommandes().size()) - 1;
		if (numCommande != 0) {
			Commande commande = Restaurant.getCommandes().get(numCommande);
			((Directeur) persConnectee).modifierEtatCommande(commande, Etat.SERVIE);
			System.out.println("Commande " + commande.getId() + " servie");
		}
	}
	
	
			// Ajouter un membre au personnel
	private static void ajouterPersonnel() {
		// Affichage
		System.out.println("----------------------------------" + "\n-----Ajouter du personnel------"
				+ "\nListe du personnel : " + listingPersonnel() + "\n----------------------------------"
				+ "\nVeuillez saisir le nom de la personne à ajouter ou 0 pour annuler ");
		// nom
		String nom = scanner.nextLine();
		// Contrôle de l'entrée
		while (estNullOuVide(nom) // vide
				|| (!uniquementLettres(nom) && !uniquementChiffres(nom)) // mélange de lettres/chiffres ou caractères
				// spéciaux
				|| (uniquementLettres(nom) && nom.length() > Restaurant.getTAILLE_MAX_NOM_PERSONNE()) // chaine et ne
				// respecte pas la
				// longueur
				// maximale
				|| (uniquementChiffres(nom) && !nom.equals("0"))) { // valeur de retour

			System.out.println("Erreur, veuillez réessayer");
			nom = scanner.nextLine();
		}
		// Si l'utilisateur ne veut pas annuler
		if (!nom.equals("0")) {

			System.out.println(
					"\nParmi assistant, serveur, maitrehotel, directeur, cuisinier;\nveuillez saisir le role de "
							+ nom);
			String role = scanner.nextLine();
			// Contrôle de l'entrée
			while (estNullOuVide(role) // vide
					|| !uniquementLettres(role) // pas que des lettres
					|| !estUnRole(role) // n'est pas un rôle
			) {
				System.out.println("Erreur, veuillez réessayer");
				role = scanner.nextLine();
			}

			((Directeur) persConnectee).ajouterPersonnel(nom, role);
			System.out.println(nom + " (" + role + ") ajouté");
		}
	}
	
	// Menu principal de l'assistant
	public static void menuPrincipalAssistant() throws ClassNotFoundException, SQLException, IOException {

		// Affichage menu
		System.out.println("----------------------------------"
					+ "\n0: Déconnexion"
					+ "\n1: Nettoyer une table"
					+ "\n----------------------------------\n");

		switch (Main.choixUtilisateur(0, 1)) { // valeurChoixMin = 0

		// Déconnexion
		case 0:
			Main.persConnectee = null;
			System.out.println("Déconnexion....\nVous avez été déconnecté.\n");
			break;

		// Signaler table nettoyée
		case 1:
			nettoyerUneTable("restaurant.Assistant");
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
