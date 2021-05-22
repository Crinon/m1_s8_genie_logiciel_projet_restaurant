package restaurant;

import java.sql.Date;
import java.util.ArrayList;

public class Affectation {

    private int			id;
    private Date		dateDebut;
    private Date		dateFin;
    private int			nbPersonne;
    private ArrayList<Commande>	commandes;
    private Facture		facture;
    private Table		table;

    public Affectation(int id, Date dateDebut, Date dateFin, int nbPersonne, ArrayList<Commande> commandes,
	    Facture facture, Table table) {
	this.id = id;
	this.dateDebut = dateDebut;
	this.dateFin = dateFin;
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

    public ArrayList<Commande> getCommandes() {
	return commandes;
    }

    public void setCommandes(ArrayList<Commande> commandes) {
	this.commandes = commandes;
    }

    public void addCommande(Commande commande) {
	this.commandes.add(commande);
    }

    public Facture getFacture() {
	return facture;
    }

    public void setFacture(Facture facture) {
	this.facture = facture;
    }

    public Table getTable() {
	return table;
    }

    public void setTable(Table table) {
	this.table = table;
    }

}
