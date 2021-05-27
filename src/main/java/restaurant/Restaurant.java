package restaurant;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public final class Restaurant {


	final static int TAILLE_MAX_NOM_INGREDIENT = 30; //Nombre de caractères max du nom d'un ingrédient
	final static int TAILLE_MAX_NOM_PERSONNE = 30; //Nombre de caractères max du nom d'une personne
	final static int QUANTITE_MAX_COMMANDE = 500; //quantité maximale pour une commande
	final static int NUMERO_MAX_TABLE = 500; //quantité maximale de tables
	final static int CAPACITE_MAX_TABLE = 500; //quantité maximale de tables

    private static ArrayList<Etage>	  etages;
    private static ArrayList<Personne>	  personnel;
    private static ArrayList<Ingredient>  ingredients;
    private static ArrayList<Plat>	  plats		   = new ArrayList<Plat>();
    private static ArrayList<Affectation> affectationsJour = new ArrayList<Affectation>();
    private static ArrayList<Reservation> reservationsJour = new ArrayList<Reservation>();
    private static ArrayList<Commande>	  commandes	   = new ArrayList<Commande>();;

    private static LocalTime heureDejeunerOuverture;
    private static LocalTime heureDejeunerLimite;
    private static LocalTime heureDinerOuverture;
    private static LocalTime heureDinerLimite;
    private static int	     nbTableMax;
    private static int	     QUANTITE_MAX_STOCK;    // quantité maximale de stock pour un ingrédient

    
    public static void initialisation() {
	Sql sql;
	sql = new Sql();
	// Ajout en base uniquement du directeur s'il n'y en a aucun dans la base
	System.out.println("Vérification s'il s'agit du premier démarrage");
	sql.premierDemarrage();
	System.out.println("Initialisation des horaires du restaurant");
	sql.initialiserHoraires();
	System.out.println("Chargement des constantes");
	sql.initialiserConstantes();
	System.out.println("Chargement des ingrédients existants");
	sql.initialiserIngredients();
	System.out.println("Chargement des étages existants");
	sql.initialiserEtages();
	System.out.println("Chargement des tables par étage");
	for (Etage etage : etages) {
		System.err.println("etage init : " + etage.getId());
	    etage.initialiserTables();
	}
	System.out.println("Chargement du personnel");
	sql.initialiserPersonnel();
	System.out.println("Chargement des plats existants");
	sql.initialiserPlats();
	System.out.println("Chargement des réservations");
	sql.initialiserReservation();
    }
    
    public static void resetRestaurant() {
       etages = new ArrayList<Etage>();
       personnel = new ArrayList<Personne>();
       ingredients = new ArrayList<Ingredient>();
       plats = new ArrayList<Plat>();
       affectationsJour = new ArrayList<Affectation>();
       reservationsJour = new ArrayList<Reservation>();
       commandes = new ArrayList<Commande>();;
       heureDejeunerOuverture = null;
       heureDejeunerLimite = null;
       heureDinerOuverture = null;
       heureDinerLimite = null;
       nbTableMax = 0;
       QUANTITE_MAX_STOCK = 0;
    }

//	public static void premierDemarrage(Sql sql) {
//		// Regarde si c'est le premier démarrage de l'application
//		sql.premierDemarrage();
//			// il faut créer un directeur automatiquement
//		
//	}

    // Méthode pour récupérer l'ensemble des tables du restaurant
    public static List<Table> getToutesLesTables() {
	List<Table> toutesLesTables = new ArrayList<Table>();
	etages.forEach(etage -> etage.getTables().forEach(table -> toutesLesTables.add(table)));
	return toutesLesTables;
    }

    // On veut avoir la plus petite table disponible pouvant contenir tous les
    // participants d'un repas
    public static Table getMiniTable(int nombreParticipants, Date dateReservation) {
	// Récupération des tables
	List<Table> toutesLesTables = getToutesLesTables();
	// Supression des tables deja reservees
	reservationsJour.stream().filter(reservation -> !memeDate(reservation.getDateVenue(), dateReservation))
	.filter(reservation -> reservation.isEffetive())
	.forEach(reservation -> toutesLesTables.remove(reservation.getTable()));
	// Filtre des tables selon la capacite et tri dans l'ordre croissant
	List<Table> tablesPossibles = toutesLesTables.stream()
		.filter(table -> table.getCapacite() >= nombreParticipants)
		.sorted(Comparator.comparingInt(Table::getCapacite)).collect(Collectors.toList());
	// Si au moins une table est disponible, on la retourne
	if (!tablesPossibles.isEmpty()) {
	    return tablesPossibles.get(0);
	}
	return null;
    }

    private static boolean memeDate(Date dateVenue, Date dateReservation) {
	Calendar calendarVenue = Calendar.getInstance();
	calendarVenue.setTime(dateVenue);
	LocalDate localdateVenue = LocalDate.of(calendarVenue.get(Calendar.YEAR), calendarVenue.get(Calendar.MONTH),
		calendarVenue.get(Calendar.DAY_OF_MONTH));
	LocalTime heureDateVenue = LocalTime.of(calendarVenue.get(Calendar.HOUR_OF_DAY),
		calendarVenue.get(Calendar.MINUTE), calendarVenue.get(Calendar.SECOND));
	Calendar calendarReservation = Calendar.getInstance();
	calendarReservation.setTime(dateReservation);
	LocalDate localdateReservation = LocalDate.of(calendarReservation.get(Calendar.YEAR),
		calendarReservation.get(Calendar.MONTH), calendarReservation.get(Calendar.DAY_OF_MONTH));
	LocalTime heureDateReservation = LocalTime.of(calendarReservation.get(Calendar.HOUR_OF_DAY),
		calendarReservation.get(Calendar.MINUTE), calendarReservation.get(Calendar.SECOND));
	return (localdateVenue.equals(localdateReservation) && ((heureDateVenue.isAfter(heureDejeunerOuverture)
		&& heureDateVenue.isBefore(heureDejeunerLimite) && heureDateReservation.isAfter(heureDejeunerOuverture)
		&& heureDateReservation.isBefore(heureDejeunerLimite))
		|| (heureDateVenue.isAfter(heureDinerOuverture) && heureDateVenue.isBefore(heureDinerLimite)
			&& heureDateReservation.isAfter(heureDinerOuverture)
			&& heureDateReservation.isBefore(heureDinerLimite))));
    }

    public static List<Plat> getCarte() {
	return plats.stream().filter(plat -> plat.isDisponibleCarte()).collect(Collectors.toList());
    }

    public static ArrayList<Ingredient> getIngredients() {
	return ingredients;
    }

    public static void setIngredients(ArrayList<Ingredient> ingredients) {
	Restaurant.ingredients = ingredients;
    }

    public static ArrayList<Etage> getEtages() {
	return etages;
    }

    public static void setEtages(ArrayList<Etage> etages) {
	Restaurant.etages = etages;
    }

    public static void addEtage(Etage etage) {
	Restaurant.etages.add(etage);
    }

    public static ArrayList<Personne> getPersonnel() {
	return personnel;
    }

    public static void setPersonnel(ArrayList<Personne> personnel) {
	Restaurant.personnel = personnel;
    }

    public static LocalTime getHeureDejeunerOuverture() {
	return heureDejeunerOuverture;
    }

    public static void setHeureDejeunerOuverture(LocalTime heureDejeunerOuverture) {
	Restaurant.heureDejeunerOuverture = heureDejeunerOuverture;
    }

    public static LocalTime getHeureDejeunerLimite() {
	return heureDejeunerLimite;
    }

    public static void setHeureDejeunerLimite(LocalTime heureDejeunerLimite) {
	Restaurant.heureDejeunerLimite = heureDejeunerLimite;
    }

    public static LocalTime getHeureDinerOuverture() {
	return heureDinerOuverture;
    }

    public static void setHeureDinerOuverture(LocalTime heureDinerOuverture) {
	Restaurant.heureDinerOuverture = heureDinerOuverture;
    }

    public static LocalTime getHeureDinerLimite() {
	return heureDinerLimite;
    }

    public static void setHeureDinerLimite(LocalTime heureDinerLimite) {
	Restaurant.heureDinerLimite = heureDinerLimite;
    }

    public static int getNbTableMax() {
	return nbTableMax;
    }

    public static void setNbTableMax(int nbTableMax) {
	Restaurant.nbTableMax = nbTableMax;
    }

    private Restaurant() {
    }

    public static ArrayList<Plat> getPlats() {
	return plats;
    }

    public static void setPlats(ArrayList<Plat> plats) {
	Restaurant.plats = plats;
    }

    public static ArrayList<Affectation> getAffectationsJour() {
	return affectationsJour;
    }

    public static void setAffectationsJour(ArrayList<Affectation> affectations) {
	Restaurant.affectationsJour = affectations;
    }

    public static ArrayList<Reservation> getReservationsJour() {
	return reservationsJour;
    }

    public static void setReservationsJour(ArrayList<Reservation> reservationsJour) {
	Restaurant.reservationsJour = reservationsJour;
    }

    public static ArrayList<Commande> getCommandes() {
	return commandes;
    }

    public static void setCommandes(ArrayList<Commande> commandes) {
	Restaurant.commandes = commandes;
    }

    public static int getQUANTITE_MAX_STOCK() {
	return QUANTITE_MAX_STOCK;
    }

    public static void setQUANTITE_MAX_STOCK(int qUANTITE_MAX_STOCK) {
	QUANTITE_MAX_STOCK = qUANTITE_MAX_STOCK;
    }

}
