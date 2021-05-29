package restaurant;

import java.util.ArrayList;

public class Etage {

	private int niveau;
	private int id;
	private ArrayList<Table> tables;

	public Etage(int id, int niveau) {
		super();
		this.id = id;
		this.niveau = niveau;
		this.tables = new ArrayList<Table>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public void initialiserTables() {
		Sql sql = new Sql();
		sql.initialiserTables(this);
	}

	@Override
	public String toString() {
		return "Etage [niveau=" + niveau + ", tables=" + tables + ", id=" + id + "]";
	}
}
