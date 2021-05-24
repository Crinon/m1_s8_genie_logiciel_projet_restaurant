package restaurant;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;

public final class Restaurant {

	private static ArrayList<Etage> etages;
	private static ArrayList<Personne> personnel;
	private static ArrayList<Ingredient> ingredients;
	private static ArrayList<Plat> plats;
	private static ArrayList<Affectation> affectationsJour;
	private static ArrayList<Reservation> reservationsJour;
	private static ArrayList<Commande> commandes;

	private static LocalTime heureDejeunerOuverture;
	private static LocalTime heureDejeunerLimite;
	private static LocalTime heureDinerOuverture;
	private static LocalTime heureDinerLimite;
	private static int nbTableMax;

	public static void initialisation() {
		Sql sql;
		try {
			sql = new Sql();
			sql.initialiserHoraires();
			sql.initialiserIngredients();
			sql.initialiserEtages();
			for (Etage etage : etages) {
				etage.initialiserTables();
//				toutesLesTables.addAll(etage.getTables());
			}
			// Ajout en base uniquement du directeur s'il n'y en a aucun dans la base
			sql.premierDemarrage();
			// Initialisation du personnel
			sql.initialiserPersonnel();
			sql.initialiserPlats();
			// Au lancement du programme en début de journée, aucune table n'est occupée
			affectationsJour = new ArrayList<Affectation>();
			
			reservationsJour = new ArrayList<Reservation>();
			sql.initialiserReservation();
			commandes  = new ArrayList<Commande>();
		} catch (ClassNotFoundException | SQLException | IOException e) {
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
	public static ArrayList<Table> getToutesLesTables() {
		ArrayList<Table> toutesLesTables = new ArrayList<>();
		etages.forEach(etage->etage.getTables().forEach(table->toutesLesTables.add(table)));
		return toutesLesTables;
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
