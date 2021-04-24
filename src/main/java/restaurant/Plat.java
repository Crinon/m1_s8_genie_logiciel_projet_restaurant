package restaurant;

import java.util.ArrayList;

public class Plat {

	private double prix;
	
	private int dureePreparation;
	
	private boolean disponibleCarte;
	
	private Type type;
	
	private Categorie categorie;
	
	private ArrayList<Ingredient> ingredients;

	public Plat(double prix, int dureePreparation, boolean disponibleCarte, Type type, Categorie categorie) {
		super();
		this.prix = prix;
		this.dureePreparation = dureePreparation;
		this.disponibleCarte = disponibleCarte;
		this.type = type;
		this.categorie = categorie;
		this.ingredients = new ArrayList<Ingredient>();
	}
	
	public ArrayList<Ingredient> getTables() {
		return ingredients;
	}

	public void setTables(ArrayList<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}
	
	public void addTable(Ingredient ingredient) {
		this.ingredients.add(ingredient);
	}

	public double getPrix() {
		return prix;
	}

	public void setPrix(double prix) {
		this.prix = prix;
	}

	public int getDureePreparation() {
		return dureePreparation;
	}

	public void setDureePreparation(int dureePreparation) {
		this.dureePreparation = dureePreparation;
	}

	public boolean isDisponibleCarte() {
		return disponibleCarte;
	}

	public void setDisponibleCarte(boolean disponibleCarte) {
		this.disponibleCarte = disponibleCarte;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Categorie getCategorie() {
		return categorie;
	}

	public void setCategorie(Categorie categorie) {
		this.categorie = categorie;
	}
	
	
	
	
}
