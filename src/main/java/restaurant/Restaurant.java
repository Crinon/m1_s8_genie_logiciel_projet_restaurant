package restaurant;

import java.sql.Date;
import java.util.ArrayList;

public class Restaurant {
	private ArrayList<Etage> etages;
	private ArrayList<Personne> personnel;
	private Date heureDejeunerOuverture;
	private Date heureDejeunerLimite;
	private Date heureDinerOuverture;
	private Date heureDinerLimite;
	private int nbTableMax;
	public ArrayList<Etage> getEtages() {
		return etages;
	}
	public void setEtages(ArrayList<Etage> etages) {
		this.etages = etages;
	}
	public ArrayList<Personne> getPersonnel() {
		return personnel;
	}
	public void setPersonnel(ArrayList<Personne> personnel) {
		this.personnel = personnel;
	}
	public Date getHeureDejeunerOuverture() {
		return heureDejeunerOuverture;
	}
	public void setHeureDejeunerOuverture(Date heureDejeunerOuverture) {
		this.heureDejeunerOuverture = heureDejeunerOuverture;
	}
	public Date getHeureDejeunerLimite() {
		return heureDejeunerLimite;
	}
	public void setHeureDejeunerLimite(Date heureDejeunerLimite) {
		this.heureDejeunerLimite = heureDejeunerLimite;
	}
	public Date getHeureDinerOuverture() {
		return heureDinerOuverture;
	}
	public void setHeureDinerOuverture(Date heureDinerOuverture) {
		this.heureDinerOuverture = heureDinerOuverture;
	}
	public Date getHeureDinerLimite() {
		return heureDinerLimite;
	}
	public void setHeureDinerLimite(Date heureDinerLimite) {
		this.heureDinerLimite = heureDinerLimite;
	}
	public int getNbTableMax() {
		return nbTableMax;
	}
	public void setNbTableMax(int nbTableMax) {
		this.nbTableMax = nbTableMax;
	}
	
	public Restaurant(ArrayList<Etage> etages, ArrayList<Personne> personnel, Date heureDejeunerOuverture,
			Date heureDejeunerLimite, Date heureDinerOuverture, Date heureDinerLimite, int nbTableMax) {
		super();
		this.etages = etages;
		this.personnel = personnel;
		this.heureDejeunerOuverture = heureDejeunerOuverture;
		this.heureDejeunerLimite = heureDejeunerLimite;
		this.heureDinerOuverture = heureDinerOuverture;
		this.heureDinerLimite = heureDinerLimite;
		this.nbTableMax = nbTableMax;
	}
	
}
