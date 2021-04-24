package restaurant;

import java.util.ArrayList;

public class Maitrehotel extends Personne {

    public Maitrehotel(String string) {
	this.nom = string;
    }

    /**
     * @param id
     * @param nom
     * @param identifiant
     */
    public Maitrehotel(int id, String nom, String identifiant) {
	this.id = id;
	this.nom = nom;
	this.identifiant = identifiant;
    }

    // On affecte à un serveur une ou plusieurs tables
    public void affecterDesTables(Serveur serveur, ArrayList<Table> tables) {
	serveur.setTablesAffectees(tables);
    }

    public void affecterUneTable(Serveur serveur, Table tables) {
	serveur.ajouterTable(tables);
    }

}
