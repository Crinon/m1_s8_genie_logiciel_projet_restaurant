package restaurant;

import java.sql.Date;

public class Reservation {

    private int	    id;
    private boolean effetive;
    private Date    dateReservation;
    private Date    DateVenue;
    private int	    nBPersonnesPrevues;
    private Table   table;

    public Reservation(int id, boolean effetive, Date dateReservation, Date dateVenue, int nBPersonnesPrevues,
	    Table table) {
	this.id = id;
	this.effetive = effetive;
	this.dateReservation = dateReservation;
	DateVenue = dateVenue;
	this.nBPersonnesPrevues = nBPersonnesPrevues;
	this.table = table;
    }

    public int getId() {
	return id;
    }

    public void setId(int id) {
	this.id = id;
    }

    public boolean isEffetive() {
	return effetive;
    }

    public void setEffetive(boolean effetive) {
	this.effetive = effetive;
    }

    public Date getDateReservation() {
	return dateReservation;
    }

    public void setDateReservation(Date dateReservation) {
	this.dateReservation = dateReservation;
    }

    public Date getDateVenue() {
	return DateVenue;
    }

    public void setDateVenue(Date dateVenue) {
	DateVenue = dateVenue;
    }

    public int getnBPersonnesPrevues() {
	return nBPersonnesPrevues;
    }

    public void setnBPersonnesPrevues(int nBPersonnesPrevues) {
	this.nBPersonnesPrevues = nBPersonnesPrevues;
    }

    public Table getTable() {
	return table;
    }

    public void setTable(Table table) {
	this.table = table;
    }

}
