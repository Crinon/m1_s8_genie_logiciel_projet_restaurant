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
    
    // Menu principal du directeur
 	public static void menuPrincipalDirecteur() throws ClassNotFoundException, SQLException, IOException {

 		// Affichage menu
 		System.out.println("----------------------------------"
 					+ "\n0: Déconnexion"
 				+ "\n1: Commander un ingredient"
 					+ "\n2: Ajouter personnel" + "\n3: Modifier personnel"
 					+ "\n4: Supprimer personnel"
 					+ "\n5: Suivi serveur" + "\n6: Statistiques"
 					+ "\n7: AJOUTER METHODES DES AUTRES ROLES"
 					+ "\n----------------------------------\n");

 		switch (Main.choixUtilisateur(7)) { // valeurChoixMin = 0

 		// Déconnexion
 		case 0:
 			Main.persConnectee = null;
 			System.out.println("Déconnexion....\nVous avez été déconnecté.\n");
 			break;

 		// 
 		case 1:
 			
 			break;

 		// 
 		case 2:

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
