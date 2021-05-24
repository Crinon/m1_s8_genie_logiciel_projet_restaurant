package fr.ul.miage.restaurant;

import java.sql.Date;

public class Proporiete {

    private Date heureDejeunerOuverture;
    private Date heureDejeunerLimite;
    private Date heureDinerOuverture;
    private Date heureDinerLimite;

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

    public Proporiete(Date heureDejeunerOuverture, Date heureDejeunerLimite, Date heureDinerOuverture,
	    Date heureDinerLimite) {
	super();
	this.heureDejeunerOuverture = heureDejeunerOuverture;
	this.heureDejeunerLimite = heureDejeunerLimite;
	this.heureDinerOuverture = heureDinerOuverture;
	this.heureDinerLimite = heureDinerLimite;
    }

}