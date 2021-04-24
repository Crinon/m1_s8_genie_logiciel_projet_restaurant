package restaurant;

public class Assistant extends Personne {

    public Assistant(int id, String nom, String identifiant) {
	this.id = id;
	this.nom = nom;
	this.identifiant = identifiant;
    }

    public Assistant(String nom) {
	this.nom = nom;
    }

}
