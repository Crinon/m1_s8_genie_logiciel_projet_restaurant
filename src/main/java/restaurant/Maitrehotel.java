package restaurant;

import java.util.ArrayList;

public class Maitrehotel extends Personne {

    /**
     * @param id
     * @param nom
     * @param identifiant
     */
    public Maitrehotel(int id, String nom, String identifiant) {
	super(id, nom, identifiant);
    }

    // On affecte à un serveur une ou plusieurs tables
    public void affecterDesTables(Serveur serveur, ArrayList<Table> tables) {
	serveur.setTablesAffectees(tables);
    }

    public void affecterUneTable(Serveur serveur, Table tables) {
	serveur.ajouterTable(tables);
    }



}
