package restaurant;

public class Table {
	
	private int numero;
	
	private int capacite;
	
	private Etat etat;
	
	private Reservation reservation;
	
	private int id;

	public Table(int id, int numero, int capacite, Etat etat) {
		super();
		this.id = id;
		this.numero = numero;
		this.capacite = capacite;
		this.etat = etat;
		this.reservation=null;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Reservation getReservation() {
		return reservation;
	}

	public void setReservation(Reservation reservation) {
		this.reservation = reservation;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public int getCapacite() {
		return capacite;
	}

	public void setCapacite(int capacite) {
		this.capacite = capacite;
	}

	public Etat getEtat() {
		return etat;
	}

	public void setEtat(Etat etat) {
		this.etat = etat;
	}
	
	
	
	

}
