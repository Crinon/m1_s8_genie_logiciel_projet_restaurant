package restaurant;

import java.util.ArrayList;

public class Maitredhotel extends Personne{

	// On affecte à un serveur une ou plusieurs tables
	public void affecterDesTables(Serveur serveur, ArrayList<Table> tables) {
		serveur.setTablesAffectees(tables);
	}
	public void affecterUneTable(Serveur serveur, Table tables) {
		serveur.ajouterTable(tables);
	}

}
