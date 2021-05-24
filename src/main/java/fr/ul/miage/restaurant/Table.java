package fr.ul.miage.restaurant;

public class Table {

    private int		numero;
    private int		capacite;
    private EtatTable	etat;
    private Reservation	reservation;
    private Serveur	serveur	= null;
    private int		id;

    public Table(int id, int numero, int capacite, EtatTable etat) {
	super();
	this.id = id;
	this.numero = numero;
	this.capacite = capacite;
	this.etat = etat;
	this.reservation = null;
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

    public EtatTable getEtat() {
	return etat;
    }

    public void setEtat(EtatTable etat) {
	this.etat = etat;
    }

    public Serveur getServeur() {
	return serveur;
    }

    public void setServeur(Serveur serveur) {
	this.serveur = serveur;
    }

}
