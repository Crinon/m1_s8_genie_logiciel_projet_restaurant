package restaurant;

import java.sql.Date;

public class Commande {

	private Date dateDemande;
	
	private boolean estEnfant;
	
	private int numTable;
	
	private Plat plat;
	
	private Affectation affectation;
	
	private Etat etat;

	public Commande(Date dateDemande, boolean estEnfant, int numTable, Plat plat, Affectation affectation, Etat etat) {
		super();
		this.dateDemande = dateDemande;
		this.estEnfant = estEnfant;
		this.numTable = numTable;
		this.plat = plat;
		this.affectation = affectation;
		this.etat = etat;
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

	public int getNumTable() {
		return numTable;
	}

	public void setNumTable(int numTable) {
		this.numTable = numTable;
	}
	
}
