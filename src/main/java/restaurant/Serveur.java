package restaurant;

import java.util.ArrayList;

public class Serveur extends Personne {

	
	private ArrayList<Table> tablesAffectees;

	
	public Serveur(String string) {
		this.identifiant = string;
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
