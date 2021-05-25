package restaurant;

import java.util.Date;
import java.util.List;

public class Affectation {

    private int		   id;
    private Date	   dateDebut;
    private Date	   dateFin;
    private int		   nbPersonne;
    private List<Commande> commandes;
    private double	   facture;
    private Table	   table;

    public Affectation(int id, Date dateDebut, int nbPersonne, List<Commande> commandes, double facture, Table table) {
	this.id = id;
	this.dateDebut = dateDebut;
	this.dateFin = null;
	this.nbPersonne = nbPersonne;
	this.commandes = commandes;
	this.facture = facture;
	this.table = table;
    }

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public Date getDateDebut() {
	return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
	this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
	return dateFin;
    }

    public void setDateFin(Date dateFin) {
	this.dateFin = dateFin;
    }

    public int getNbPersonne() {
	return nbPersonne;
    }

    public void setNbPersonne(int nbPersonne) {
	this.nbPersonne = nbPersonne;
    }

    public List<Commande> getCommandes() {
	return commandes;
    }

    public void setCommandes(List<Commande> commandes) {
	this.commandes = commandes;
    }

    public double getFacture() {
	return facture;
    }

    public void setFacture(double facture) {
	this.facture = facture;
    }

    public Table getTable() {
	return table;
    }

    public void setTable(Table table) {
	this.table = table;
    }

    @Override
    public String toString() {
	return "Affectation [id=" + id + ", dateDebut=" + dateDebut + ", dateFin=" + dateFin + ", nbPersonne="
		+ nbPersonne + ", commandes=" + commandes + ", facture=" + facture + ", table=" + table + "]";
    }
}
