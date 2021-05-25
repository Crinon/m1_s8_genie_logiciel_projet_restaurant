package restaurant;

import java.io.IOException;
import java.sql.SQLException;

public class Assistant extends Personne {

    public Assistant(int id, String nom, String identifiant) {
	super(id, nom, identifiant);
    }
    
    
    // Menu principal de l'assistant
 	public static void menuPrincipalAssistant() throws ClassNotFoundException, SQLException, IOException {

 		// Affichage menu
 		System.out.println("----------------------------------"
 					+ "\n0: Déconnexion"
 					+ "\n1: Signaler table nettoyée"
 					+ "\n----------------------------------\n");

 		switch (Main.choixUtilisateur(7)) { // valeurChoixMin = 0

 		// Déconnexion
 		case 0:
 			Main.persConnectee = null;
 			System.out.println("Déconnexion....\nVous avez été déconnecté.\n");
 			break;

 		// Signaler table nettoyée
 		case 1:
 			//TODO
 			break;

 		default:
 			break;
 		}

 	}

    public void nettoyerTable(Table table) {
	Sql sql;
	try {
	    sql = new Sql();
	    sql.modifierEtatTable(table, EtatTable.Libre);
	    table.setEtat(EtatTable.Libre);
	}
	catch (ClassNotFoundException | SQLException | IOException e) {
	    e.printStackTrace();
	}
    }
}
