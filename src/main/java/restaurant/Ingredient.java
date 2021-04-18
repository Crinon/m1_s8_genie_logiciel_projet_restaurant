package restaurant;

public class Ingredient {

	private String nom;
	private int quantite;

	public Ingredient(String nom, int quantite) {
		super();
		this.nom = nom;
		this.quantite = quantite;
	}
	
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public int getQuantite() {
		return quantite;
	}
	public void setQuantite(int quantite) {
		this.quantite = quantite;
	}
}
