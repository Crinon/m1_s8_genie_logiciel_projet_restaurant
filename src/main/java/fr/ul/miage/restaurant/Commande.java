package fr.ul.miage.restaurant;

import java.sql.Date;

public class Commande {

	private int id;
	private Date dateDemande;
	private boolean estEnfant;
	private Plat plat;
	private Affectation affectation;
	private Etat etat;

	public Commande(int id, Date dateDemande, boolean estEnfant, Plat plat, Affectation affectation, Etat etat) {
		this.id = id;
		this.dateDemande = dateDemande;
		this.estEnfant = estEnfant;
		this.plat = plat;
		this.affectation = affectation;
		this.etat = etat;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Plat getPlat() {
		return plat;
	}

	public void setPlat(Plat plat) {
		this.plat = plat;
	}

	public Affectation getAffectation() {
		return affectation;
	}

	public void setAffectation(Affectation affectation) {
		this.affectation = affectation;
	}

	public Etat getEtat() {
		return etat;
	}

	public void setEtat(Etat etat) {
		this.etat = etat;
	}

	public Date getDateDemande() {
		return dateDemande;
	}

	public void setDateDemande(Date dateDemande) {
		this.dateDemande = dateDemande;
	}

	public boolean isEstEnfant() {
		return estEnfant;
	}

	public void setEstEnfant(boolean estEnfant) {
		this.estEnfant = estEnfant;
	}

}
