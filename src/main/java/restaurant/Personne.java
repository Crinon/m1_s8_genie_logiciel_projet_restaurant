package restaurant;

public abstract class Personne {

    int	   id;
    String nom;
    String identifiant;

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

    public String getIdentifiant() {
	return identifiant;
    }

    public void setIdentifiant(String identifiant) {
	this.identifiant = identifiant;
    }

}
