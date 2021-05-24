package fr.ul.miage.restaurant;

import java.io.IOException;
import java.sql.SQLException;
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
    
    // Menu principal du maitre d hotel
 	public static void menuPrincipalMaitredhotel() throws ClassNotFoundException, SQLException, IOException {

 		// Affichage menu
 		System.out.println("----------------------------------"
 					+ "\n0: Déconnexion"
 					+ "\n1: Affecter un serveur à une table"
 					+ "\n2: AJOUTER METHODES DU ROLE SERVEUR ?????"
 					+ "\n----------------------------------\n");

 		switch (Main.choixUtilisateur(7)) { // valeurChoixMin = 0

 		// Déconnexion
 		case 0:
 			Main.persConnectee = null;
 			System.out.println("Déconnexion....\nVous avez été déconnecté.\n");
 			break;

 		// Affecter un serveur à une table
 		case 1:
 			//TODO
 			break;

 		// 
 		case 2:
 			//TODO
 			break;


 		default:
 			break;
 		}

 	}

    // On affecte à un serveur une ou plusieurs tables
    public void affecterDesTables(Serveur serveur, ArrayList<Table> tables) {
	serveur.setTablesAffectees(tables);
    }

    public void affecterUneTable(Serveur serveur, Table tables) {
	serveur.ajouterTable(tables);
    }



}
