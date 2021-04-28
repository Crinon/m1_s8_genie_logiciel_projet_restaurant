package restaurant;

public class Facture {

	private int id;
	private double Facture;
	private Affectation affectation;


	public Facture(int id, double facture, Affectation affectation) {
		super();
		this.id = id;
		Facture = facture;
		this.affectation = affectation;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
	
	
	
}
