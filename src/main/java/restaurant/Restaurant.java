package restaurant;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

public final class Restaurant {
	private static ArrayList<Etage> etages;
	private static ArrayList<Personne> personnel;
	private static ArrayList<Ingredient> ingredients;
	private static Date heureDejeunerOuverture;
	private static Date heureDejeunerLimite;
	private static Date heureDinerOuverture;
	private static Date heureDinerLimite;
	private static int nbTableMax;
	
	public static void initialisation() throws ClassNotFoundException, SQLException {
    	Sql sql = new Sql();
    	sql.initialiserIngredients();
    	sql.initialiserEtages();
    	for (Etage etage : etages) {
			etage.initialiserTables();
		}
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

	public static Date getHeureDejeunerOuverture() {
		return heureDejeunerOuverture;
	}

	public static void setHeureDejeunerOuverture(Date heureDejeunerOuverture) {
		Restaurant.heureDejeunerOuverture = heureDejeunerOuverture;
	}

	public static Date getHeureDejeunerLimite() {
		return heureDejeunerLimite;
	}

	public static void setHeureDejeunerLimite(Date heureDejeunerLimite) {
		Restaurant.heureDejeunerLimite = heureDejeunerLimite;
	}

	public static Date getHeureDinerOuverture() {
		return heureDinerOuverture;
	}

	public static void setHeureDinerOuverture(Date heureDinerOuverture) {
		Restaurant.heureDinerOuverture = heureDinerOuverture;
	}

	public static Date getHeureDinerLimite() {
		return heureDinerLimite;
	}

	public static void setHeureDinerLimite(Date heureDinerLimite) {
		Restaurant.heureDinerLimite = heureDinerLimite;
	}

	public static int getNbTableMax() {
		return nbTableMax;
	}

	public static void setNbTableMax(int nbTableMax) {
		Restaurant.nbTableMax = nbTableMax;
	}

	private Restaurant() {}
	
}
