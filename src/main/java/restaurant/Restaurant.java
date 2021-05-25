package restaurant;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public final class Restaurant {

    private static ArrayList<Etage>	  etages;
    private static ArrayList<Personne>	  personnel;
    private static ArrayList<Ingredient>  ingredients;
    private static ArrayList<Plat>	  plats =new ArrayList<Plat>();
    private static ArrayList<Affectation> affectationsJour = new ArrayList<Affectation>();
    private static ArrayList<Reservation> reservationsJour = new ArrayList<Reservation>();
    private static ArrayList<Commande>	  commandes = new ArrayList<Commande>();;

    private static LocalTime heureDejeunerOuverture;
    private static LocalTime heureDejeunerLimite;
    private static LocalTime heureDinerOuverture;
    private static LocalTime heureDinerLimite;
    private static int	     nbTableMax;

    public static void initialisation() {
	Sql sql;
	try {
	    sql = new Sql();
	    sql.initialiserHoraires();
	    sql.initialiserIngredients();
	    sql.initialiserEtages();
	    for (Etage etage : etages) {
		etage.initialiserTables();
	    }
	    // Ajout en base uniquement du directeur s'il n'y en a aucun dans la base
	    sql.premierDemarrage();
	    // Initialisation du personnel
	    sql.initialiserPersonnel();
	    sql.initialiserPlats();
	    sql.initialiserReservation();
	}
	catch (ClassNotFoundException | SQLException | IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
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
	return (localdateVenue.equals(localdateReservation) && 
		( (heureDateVenue.isAfter(heureDejeunerOuverture)
		&& heureDateVenue.isBefore(heureDejeunerLimite) && heureDateReservation.isAfter(heureDejeunerOuverture)
		&& heureDateReservation.isBefore(heureDejeunerLimite))
		|| 
		  (heureDateVenue.isAfter(heureDinerOuverture) && heureDateVenue.isBefore(heureDinerLimite)
		&& heureDateReservation.isAfter(heureDinerOuverture)
		&& heureDateReservation.isBefore(heureDinerLimite))
		)
	);
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

}
