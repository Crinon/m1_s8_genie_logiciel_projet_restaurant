package restaurant;

import java.util.ArrayList;

public class Serveur extends Personne {

    private ArrayList<Table> tablesAffectees;

    public Serveur(String string) {
	this.nom = string;
    }

    /**
     * @param id
     * @param nom
     * @param identifiant
     */
    public Serveur(int id, String nom, String identifiant) {
	this.id = id;
	this.nom = nom;
	this.identifiant = identifiant;
    }

    public ArrayList<Table> getTablesAffectees() {
	return tablesAffectees;
    }

    public void setTablesAffectees(ArrayList<Table> tablesAffectees) {
	this.tablesAffectees = tablesAffectees;
    }

    public void ajouterTable(Table table) {
	this.tablesAffectees.add(table);
    }

    public void retirerTable(Table table) {
	this.tablesAffectees.remove(table);
    }

}
