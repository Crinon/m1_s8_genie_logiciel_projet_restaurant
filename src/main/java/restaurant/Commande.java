package restaurant;

import java.sql.Date;

public class Commande {

	private Date dateDemande;
	
	private boolean estEnfant;
	
	private int numTable;
	
	private Plat plat;
	
	private Affectation affectation;

	public Commande(Date dateDemande, boolean estEnfant, int numTable, Plat plat, Affectation affectation) {
		super();
		this.dateDemande = dateDemande;
		this.estEnfant = estEnfant;
		this.numTable = numTable;
		this.plat = plat;
		this.affectation = affectation;
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
