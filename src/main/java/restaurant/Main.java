package restaurant;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    public static Scanner  scanner	 = new Scanner(System.in);
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
	}
	catch (Exception e) {
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
    public static String listingTables() {
	String liste = "";
	int i = 1; // numero de la table dans le menu (0 étant réservé au retour)

	for (int etage = 0; etage < Restaurant.getEtages().size(); etage++) {
	    liste += "\n<Etage " + Restaurant.getEtages().get(etage).getNiveau() + " >";
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
		    + Restaurant.getPersonnel().get(i).getClass().getName().substring(11);
	}
	return liste;
    }

    // Permet de commander un ingrédient pour l'ajouter au stock
    public static void commanderIngredientDirecteur() throws ClassNotFoundException, SQLException, IOException {

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
		|| (uniquementLettres(choix) && choix.length() > Restaurant.TAILLE_MAX_NOM_INGREDIENT) // chaine et ne
												       // respecte pas
												       // la longueur
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
	    qtIngredient = choixUtilisateur(1, Restaurant.QUANTITE_MAX_COMMANDE); // Quantite max par commande : 500
	    ((Directeur) persConnectee).ajouterIngredient(nomIngredient);
	    ((Directeur) persConnectee).commanderIngredient(
		    Restaurant.getIngredients().get(Restaurant.getIngredients().size() - 1), qtIngredient); // Dernier
	    System.out.println("Commande passée (quantite : " + qtIngredient + ")"); // inséré

	}
	else if (uniquementChiffres(choix)) {
	    // MAJ quantite d'un ingrédient existant
	    System.out.println(">Quantité à commander ?");
	    qtIngredient = choixUtilisateur(1, Restaurant.QUANTITE_MAX_COMMANDE); // Quantite max par commande : 500
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
	int nbTable = 1;
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

    // Permet de supprimer une table d'un étage
    public static void supprimerTableDirecteur() throws ClassNotFoundException, SQLException, IOException {

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
    public static void modifierTableDirecteur() throws ClassNotFoundException, SQLException, IOException {

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
	    int nouveauNumero = Main.choixUtilisateur(1, Restaurant.NUMERO_MAX_TABLE);
	    ((Directeur) persConnectee).modifierNumeroTable(trouverTable(numero), nouveauNumero);
	    System.out.println("Table " + (numero - 1) + " modifiée en " + nouveauNumero);
	}
    }

    // Permet d'ajouter une table à un étage
    public static void ajouterTableDirecteur() throws ClassNotFoundException, SQLException, IOException {

	// Affichage menu
	System.out.println("----------------------------------" + "\n-------Ajouter une table--------"
		+ "\nListe des tables : " + listingTables() + "\n----------------------------------"
		+ "\nVeuillez choisir l'étage de la table à ajouter");

	int etage = Main.choixUtilisateur(0, Restaurant.getEtages().size() - 1);
	// numero de la table
	System.out.println("\nNuméro de la table : ");
	int numero = Main.choixUtilisateur(0, Restaurant.NUMERO_MAX_TABLE);
	// capacite de la table
	System.out.println("\nVeuillez saisir sa capacité, ou 0 pour annuler et revenir au menu");
	int capacite = Main.choixUtilisateur(0, Restaurant.CAPACITE_MAX_TABLE);

	if (capacite != 0) {
	    ((Directeur) persConnectee).ajouterTable(numero, capacite, Restaurant.getEtages().get(etage));
	    System.out.println("Table ajoutée");
	}
    }

    // Permet de vider la base de données pour réinitialiser le restaurant
    public static void viderBddDirecteur() throws ClassNotFoundException, SQLException, IOException {
	// Sql.hardReset("hardResetPostgres");
    }

    // Menu principal du directeur
    public static void menuPrincipalDirecteur() throws ClassNotFoundException, SQLException, IOException {

	// Affichage menu
	System.out.println("----------------------------------" + "\n0: Déconnexion" + "\n1: Commander un ingredient"
		+ "\n2: Ajouter personnel" + "\n3: Modifier personnel" + "\n4: Supprimer personnel"
		+ "\n5: Ajouter etage" + "\n6: Supprimer dernier etage" + "\n7: Ajouter table" + "\n8: Modifier table"
		+ "\n9: Supprimer table" + "\n10: Ajouter plat" + "\n11: Modifier plat" + "\n12: Modifier carte"
		+ "\n13: Supprimer plat" + "\n14: Ajouter réservation" + "\n15: Supprimer réservation"
		+ "\n16: Affecter des clients à une table" + "\n17: Affecter un serveur à une table"
		+ "\n18: Prendre une commande" + "\n19: Editer une facture" + "\n20: Cuisiner un plat"
		+ "\n21: Servir un plat" + "\n22: Nettoyer table" + "\n23: Statistiques" + "\n24: Vider la BDD"
		+ "\n----------------------------------\n");

	switch (Main.choixUtilisateur(0, 24)) { // valeurChoixMin = 0

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

	    // Modifier le rôle d'un membre du personnel
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

	    case 10:
		ajouterPlatDirecteur();
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
	    // Vider la BDD
	    case 24:
		viderBddDirecteur();
	    break;

	    default:
	    break;
	}

    }

    private static void ajouterPlatDirecteur() {
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
	    System.out.println(i + 1 + ": " + Type.values()[i].name());
	}
	String categorie = scanner.nextLine();
	String ingredient = "1";
	HashMap<Ingredient, Integer> recette = new HashMap<Ingredient, Integer>();
	while (Integer.parseInt(ingredient) != Restaurant.getIngredients().size()) {
	    System.out.println("Veuillez saisir les ingédients de la recette");
	    for (int i = 0; i < Restaurant.getIngredients().size(); i++) {
		System.out.println(i + 1 + ": " + Restaurant.getIngredients().get(i).getNom());
	    }
	    System.out.println(Restaurant.getIngredients().size() + ": Valiser");
	    ingredient = scanner.nextLine();
	    if (Integer.parseInt(ingredient) != Restaurant.getIngredients().size()) {
		System.out.println("Veuillez saisir la quantité");
		String quantite = scanner.nextLine();
		recette.put(Restaurant.getIngredients().get(Integer.parseInt(ingredient) + 1),
			Integer.parseInt(quantite));
	    }
	}
	((Directeur) persConnectee).creerPlat(nomPlat, Double.parseDouble(prixPlat), Integer.parseInt(dureePlat), false,
		Type.valueOf(type), Categorie.valueOf(categorie), recette);

    }

    // Modifier le rôle d'un membre du personnel
    private static void supprimerPersonnelDirecteur() {
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
    private static void modifierPersonnelDirecteur() {
	// Affichage
	System.out.println("----------------------------------" + "\n-----Modifier du personnel------"
		+ "\nListe du personnel : " + listingPersonnel() + "\n----------------------------------"
		+ "\nVeuillez saisir le numéro de la personne à modifier, ou 0 pour revenir au menu");
	// numéro
	String numPersonne = scanner.nextLine();
	// Contrôle de l'entrée
	while (estNullOuVide(numPersonne) // vide
		|| !uniquementChiffres(numPersonne) // pas que des chiffres
		|| (uniquementChiffres(numPersonne)
			&& (!valeurIntOk(Integer.parseInt(numPersonne), 0, Restaurant.getPersonnel().size() - 1) // personne
														 // n'existe
														 // pas
				|| !numPersonne.equals("0"))) // valeur de retour
	) {

	    System.out.println("Erreur, veuillez réessayer");
	    numPersonne = scanner.nextLine();
	}
	// Permet de revenir au menu (annuler)
	if (!numPersonne.equals("0")) {

	    System.out.println(
		    "\nParmi assistant, serveur, maitrehotel, directeur, cuisinier;\nVeuillez saisir le nouveau role de "
			    + Restaurant.getPersonnel().get(Integer.parseInt(numPersonne)).getNom()
			    + " qui est actuellement " + Restaurant.getPersonnel().get(Integer.parseInt(numPersonne))
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

	    ((Directeur) persConnectee).modifierPersonnel(Restaurant.getPersonnel().get(Integer.parseInt(numPersonne)),
		    role);
	    System.out.println("Rôle modifié");

	}
    }

    // Ajouter un membre au personnel
    private static void ajouterPersonnelDirecteur() {

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
		|| (uniquementLettres(nom) && nom.length() > Restaurant.TAILLE_MAX_NOM_PERSONNE) // chaine et ne
												 // respecte pas la
												 // longueur maximale
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

    // Menu principal du maitre d hotel
    public static void menuPrincipalMaitredhotel() throws ClassNotFoundException, SQLException, IOException {

	// Affichage menu
	System.out.println(
		"----------------------------------" + "\n0: Déconnexion" + "\n1: Affecter un serveur à une table"
			+ "\n2: AJOUTER METHODES DU ROLE SERVEUR ?????" + "\n----------------------------------\n");

	switch (Main.choixUtilisateur(0, 7)) { // valeurChoixMin = 0

	    // Déconnexion
	    case 0:
		Main.persConnectee = null;
		System.out.println("Déconnexion....\nVous avez été déconnecté.\n");
	    break;

	    // Affecter un serveur à une table
	    case 1:
	    // TODO
	    break;

	    //
	    case 2:
	    // TODO
	    break;

	    default:
	    break;
	}

    }

    public static void menuPrincipalCuisinier() throws ClassNotFoundException, SQLException, IOException {

	// Affichage menu
	System.out.println("----------------------------------" + "\n0: Déconnexion" + "\n1: Définir un plat"
		+ "\n2: Consulter les commandes" + "\n3: Passer une commande à \"terminée\""
		+ "\n----------------------------------\n");

	switch (Main.choixUtilisateur(0, 7)) { // valeurChoixMin = 0

	    // Déconnexion
	    case 0:
		Main.persConnectee = null;
		System.out.println("Déconnexion....\nVous avez été déconnecté.\n");
	    break;

	    // Définir un plat
	    case 1:
	    // TODO
	    break;

	    // Consulter les commandes
	    case 2:
	    // TODO
	    break;

	    // Passer une commande à "terminée"
	    case 3:
	    // TODO
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
		// TODO
		+ "\n0: Déconnexion" + "\n1: Voir l'état des tables" + "\n2: Prendre les commandes d'une table"
		+ "\n3: Transmettre facture" + "\n----------------------------------\n");

	switch (Main.choixUtilisateur(0, 7)) { // valeurChoixMin = 0

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
	System.out.println("----------------------------------" + "\n0: Déconnexion" + "\n1: Nettoyer une table"
		+ "\n----------------------------------\n");

	switch (Main.choixUtilisateur(0, 7)) { // valeurChoixMin = 0

	    // Déconnexion
	    case 0:
		Main.persConnectee = null;
		System.out.println("Déconnexion....\nVous avez été déconnecté.\n");
	    break;

	    // Signaler table nettoyée
	    case 1:
	    // TODO
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

	}
	while (true);
    }
}
