package restaurant;

import java.util.ArrayList;

public class Etage {

	private int niveau;
	
	private ArrayList<Table> tables;

	public Etage(int niveau) {
		super();
		this.niveau = niveau;
		this.tables = new ArrayList<Table>();
	}

	public int getNiveau() {
		return niveau;
	}

	public void setNiveau(int niveau) {
		this.niveau = niveau;
	}

	public ArrayList<Table> getTables() {
		return tables;
	}

	public void setTables(ArrayList<Table> tables) {
		this.tables = tables;
	}
	
	public void addTable(Table table) {
		this.tables.add(table);
	}
	
	
	
}
