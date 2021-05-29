package restaurant;

public class Ingredient {

	private int id;
	private String nom;
	private int quantite;

	public Ingredient(int id, String nom, int quantite) {
		this.id = id;
		this.nom = nom;
		this.quantite = quantite;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	@Override
	public String toString() {
		return "Ingredient [id=" + id + ", nom=" + nom + ", quantite=" + quantite + "]";
	}
}
