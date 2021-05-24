package fr.ul.miage.restaurant;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Serveur extends Personne {

    private ArrayList<Table> tablesAffectees = new ArrayList<Table>();

    /**
     * @param id
     * @param nom
     * @param identifiant
     */
    public Serveur(int id, String nom, String identifiant) {
	super(id, nom, identifiant);
    }
    
    // Menu principal du serveur
 	public static void menuPrincipalServeur() throws ClassNotFoundException, SQLException, IOException {

 		
 		
 		// Affichage menu
 		System.out.println("----------------------------------"
	 				//Affichage des tables de son étage + couleurs (état) associées
	 		 		//TODO
 					+ "\n0: Déconnexion"
 					+ "\n1: Affecter un serveur à une table"
 					+ "\n----------------------------------\n");

 		switch (Main.choixUtilisateur(7)) { // valeurChoixMin = 0

 		// Déconnexion
 		case 0:
 			Main.persConnectee = null;
 			System.out.println("Déconnexion....\nVous avez été déconnecté.\n");
 			break;

 		// Affecter un serveur à une table
 		case 1:
 			
 			break;

 		// TODO
 		case 2:

 			break;


 		default:
 			break;
 		}

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
