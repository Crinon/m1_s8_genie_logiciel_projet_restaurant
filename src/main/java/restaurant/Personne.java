package restaurant;

public abstract class Personne {

	int id;
	int masterid;
	String nom;
	String identifiant;

	public Personne(int id, String nom, String identifiant) {
		this.id = id;
		this.nom = nom;
		this.identifiant = identifiant;
	}

	public int getMasterid() {
		return masterid;
	}

	public void setMasterid(int masterid) {
		this.masterid = masterid;
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

	public String getIdentifiant() {
		return identifiant;
	}

	public void setIdentifiant(String identifiant) {
		this.identifiant = identifiant;
	}

}
