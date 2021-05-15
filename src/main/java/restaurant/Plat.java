package restaurant;

import java.util.ArrayList;
import java.util.HashMap;

public class Plat {

	private int id;
	private String nom;
	private double prix;
	private int dureePreparation;
	private boolean disponibleCarte;
	private Type type;
	private Categorie categorie;
	private HashMap<Ingredient, Integer> recette;
	
	
	@Override
	public String toString() {
		return "Plat [id=" + id + ", nom=" + nom + ", prix=" + prix + ", dureePreparation=" + dureePreparation
				+ ", disponibleCarte=" + disponibleCarte + ", type=" + type + ", categorie=" + categorie + ", recette="
				+ recette + "]";
	}

	public Plat(int id, String nom, double prix, int dureePreparation, boolean disponibleCarte, Type type, Categorie categorie,
			HashMap<Ingredient, Integer> recetteAcreer) {
		super();
		this.nom=nom;
		this.id = id;
		this.prix = prix;
		this.dureePreparation = dureePreparation;
		this.disponibleCarte = disponibleCarte;
		this.type = type;
		this.categorie = categorie;
		this.setRecette(recetteAcreer);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public HashMap<Ingredient, Integer> getRecette() {
		return recette;
	}

	public void setRecette(HashMap<Ingredient, Integer> recette) {
		this.recette = recette;
	}

	
	
	
	
}
