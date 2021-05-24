package restaurant;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Directeur extends Personne {

    /**
     * @param id
     * @param nom
     * @param identifiant
     */
    public Directeur(int id, String nom, String identifiant) {
	super(id, nom, identifiant);
    }

    @Override
    public String toString() {
	return "Directeur [id=" + id + ", nom=" + nom + ", identifiant=" + identifiant + "]";
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

 		// Commander un ingredient
 		case 1:
 			commanderIngredient();
 			break;

 		// Ajouter personnel
 		case 2:

 			break;

 		// Modifier personnel
 		case 3:

 			break;

 		// Supprimer personnel
 		case 4:

 			break;

 		// Suivi serveur
 		case 5:

 			break;

 		// Statistiques
 		case 6:

 			break;

 		// Méthodes des autres rôles....
 		case 7:

 			break;

 		default:
 			break;
 		}

 	}
 	
 // Permet de commander un ingrédient pour l'ajouter au stock
 	public static void commanderIngredient() throws ClassNotFoundException, SQLException, IOException {

 			// Affichage menu
 			System.out.println("----------------------------------"
 					+ "\n-----Commander un ingredient------"
 					+ "\nListe des ingrédients : " + Main.listingIngredients()
 					+ "\n----------------------------------\n"
 					+ "\nVeuillez taper un nom si vous voulez commander un ingrédient qui ne figure"
 					+ " pas dans la liste, ou le numéro d'un des ingrédients de la liste");
 			
 			String choix = Main.scanner.nextLine();
 			int qtIngredient = 0;
 			do {
 				if (!Main.estNulleOuVide(choix) && Main.uniquementLettres(choix)) {
 					// Nouvel ingrédient
 					String nomIngredient = choix.toLowerCase();
 					System.out.println(">Quantité de " + choix + " à commander ?");
 					qtIngredient = Main.choixUtilisateur(500); // Quantite max par commande : 500
 					((Directeur) Main.persConnectee).ajouterIngredient(nomIngredient);
 					((Directeur) Main.persConnectee).commanderIngredient(
 							Restaurant.getIngredients().get(Restaurant.getIngredients().size() - 1), qtIngredient); // Dernier
 					System.out.println("Commande passée (quantite : " + qtIngredient + ")");														// inséré

 				} else if (Restaurant.getIngredients().size() != 0 && !Main.estNulleOuVide(choix) && Main.uniquementChiffres(choix)
 						&& !Main.valeurIntOk(Integer.parseInt(choix), Restaurant.getIngredients().size())) {
 					// MAJ quantite d'un ingrédient existant
 					System.out.println(">Quantité à commander ?");
 					qtIngredient = Main.choixUtilisateur(500); // Quantite max par commande : 500
 					((Directeur) Main.persConnectee).commanderIngredient(
 							Restaurant.getIngredients().get(Restaurant.getIngredients().size() - 1), qtIngredient); // Dernier
 					System.out.println("Commande passée (quantite : " + qtIngredient + ")");																						// inséré

 				} else {
 					System.out.println("Ereur, veuillez réessayer");
 				}
 			} while (Main.estNulleOuVide(choix) || (!Main.uniquementLettres(choix) && !Main.uniquementChiffres(choix)));
 		
 	}

    public Personne ajouterPersonnel(String nom, String role) {
	Personne personne = null;
	// Objet pour intéragir avec la base de données
	try {
	    Sql sql = new Sql();
	    // Utilisation de la requête pour insérer un personnel
	    personne = sql.ajouterPersonne(nom, role);
	    Restaurant.getPersonnel().add(personne);
	}
	catch (ClassNotFoundException | SQLException | IOException e) {
	    e.printStackTrace();
	}
	return personne;
    }

    public Personne modifierPersonnel(Personne personne, String role) {
	// Objet pour intéragir avec la base de données
	try {
	    Sql sql = new Sql();
	    // Utilisation de la requête pour insérer un serveur
	    sql.modifierPersonne(personne, role);
	    Personne newPersonne;
	    switch (role) {
		case "assistant":
		    newPersonne = new Assistant(personne.getId(), personne.getNom(), personne.getIdentifiant());
		break;

		case "serveur":
		    newPersonne = new Serveur(personne.getId(), personne.getNom(), personne.getIdentifiant());
		break;

		case "maitrehotel":
		    newPersonne = new Maitrehotel(personne.getId(), personne.getNom(), personne.getIdentifiant());
		break;

		case "directeur":
		    newPersonne = new Directeur(personne.getId(), personne.getNom(), personne.getIdentifiant());
		break;

		case "cuisinier":
		    newPersonne = new Cuisinier(personne.getId(), personne.getNom(), personne.getIdentifiant());
		break;

		default:
		    newPersonne = null;
		break;
	    }
	    personne = newPersonne;
	}
	catch (ClassNotFoundException | SQLException | IOException e) {
	    e.printStackTrace();
	}
	return personne;
    }

    public void supprimerPersonnel(Personne personne, ArrayList<Personne> personnel) {
	try {
	    // Objet pour intéragir avec la base de données
	    Sql sql = new Sql();
	    sql.supprimerPersonne(personne);
	    personnel.remove(personne);
	}
	catch (ClassNotFoundException | SQLException | IOException e) {
	    e.printStackTrace();
	}
    }

    public boolean ajouterEtage() throws ClassNotFoundException, SQLException, IOException {
	Sql sql = new Sql();
	sql.insererEtage();
	Restaurant.addEtage(new Etage(sql.demanderDernierId("etage"), sql.demanderDernierEtage()));
	return true;
    }

    public void supprimerDernierEtage() throws ClassNotFoundException, SQLException, IOException {
	Sql sql = new Sql();
	sql.supprimerEtage();
	Restaurant.getEtages().remove(Restaurant.getEtages().size() - 1);
    }

    public Table ajouterTable(int numero, int capacite, Etage etage)
	    throws ClassNotFoundException, SQLException, IOException {
	Sql sql = new Sql();
	sql.insererTable(numero, capacite, etage);
	Table nouvelleTable = new Table(sql.demanderDernierId("tables"), numero, capacite, EtatTable.Libre);
	etage.addTable(nouvelleTable);
	return nouvelleTable;
    }

    public void modifierNumeroTable(Table table, int newNumero)
	    throws ClassNotFoundException, SQLException, IOException {
	boolean success;
	Sql sql = new Sql();
	success = sql.updateTable(table, newNumero);
	if (success) {
	    table.setNumero(newNumero);
	}
    }

    public void supprimerTable(Table tableToremove, ArrayList<Table> tables){

	try {
		boolean success;
		Sql sql;
		sql = new Sql();	
		success = sql.deleteTable(tableToremove);
		if (success) {
		    tables.remove(tableToremove);
		}
	} catch (ClassNotFoundException | SQLException | IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

    }

    public boolean ajouterIngredient(String nom) {
	boolean success = false;
	Sql sql;
	try {
	    sql = new Sql();
	    success = sql.insererIngredient(nom);
	    if (success) {
		Restaurant.getIngredients().add(new Ingredient(sql.demanderDernierId("ingredient"), nom, 0));
		return success;
	    }
	    else {
		System.out.println("L'ajout de l'ingrédient " + nom + " a échoué.");
		return false;
	    }
	}
	catch (ClassNotFoundException | SQLException | IOException e) {
	    e.printStackTrace();
	}
	return success;
    }

    public void commanderIngredient(Ingredient ingredient, int ajout)
	    throws ClassNotFoundException, SQLException, IOException {
	Sql sql = new Sql();
	sql.commanderIngredient(ingredient, ajout);
    }

    public Plat creerPlat(String nom, Double prixPlat, int tempsPrepa, boolean surCarte, Type type, Categorie categorie,
	    HashMap<Ingredient, Integer> ingredientQuantite) {
	Sql sql;
	try {
	    sql = new Sql();
	    Plat plat = sql.insererPlat(nom, prixPlat, tempsPrepa, surCarte, type, categorie, ingredientQuantite);
	    Restaurant.getPlats().add(plat);
	    return plat;
	}
	catch (ClassNotFoundException | SQLException | IOException e) {
	    e.printStackTrace();
	}
	return null;
    }

    public void modifierDureePlat(Plat plat, int duree) {
	Sql sql;
	try {
	    sql = new Sql();
	    sql.modifierDureePlat(plat, duree);
	    plat.setDureePreparation(duree);
	}
	catch (ClassNotFoundException | SQLException | IOException e) {
	    e.printStackTrace();
	}
    }

    public void modifierCartePlat(Plat plat, boolean estCarte) {
	Sql sql;
	try {
	    sql = new Sql();
	    sql.modifierCartePlat(plat, estCarte);
	    plat.setDisponibleCarte(estCarte);
	}
	catch (ClassNotFoundException | SQLException | IOException e) {
	    e.printStackTrace();
	}
    }

    public void modifierPrixPlat(Plat plat, double prix) {
	Sql sql;
	try {
	    sql = new Sql();
	    sql.modifierPrixPlat(plat, prix);
	    plat.setPrix(prix);
	}
	catch (ClassNotFoundException | SQLException | IOException e) {
	    e.printStackTrace();
	}
    }

    public void supprimerPlat(Plat plat) {
	Sql sql;
	try {
	    sql = new Sql();
	    sql.supprimerPlat(plat);
	    Restaurant.getPlats().remove(plat);
	}
	catch (ClassNotFoundException | SQLException | IOException e) {
	    e.printStackTrace();
	}
    }

    public Affectation creationAffectation(Date dateDebut, int nbPersonne, Table table) {
	Sql sql;
	try {
	    sql = new Sql();
	    Affectation affectation = sql.creationAffectation(dateDebut, nbPersonne, table);
	    Restaurant.getAffectationsJour().add(affectation);
	    return affectation;
	}
	catch (ClassNotFoundException | SQLException | IOException e) {
	    e.printStackTrace();
	}
	return null;
    }

    public Boolean dateFinAffectation(Affectation affectation, Date dateFin) {
	Sql sql;
	try {
	    sql = new Sql();
	    sql.dateFinAffectation(affectation, dateFin);
	    affectation.setDateFin(dateFin);
	    return true;
	}
	catch (ClassNotFoundException | SQLException | IOException e) {
	    e.printStackTrace();
	}
	return false;
    }

    public Reservation creationReservation(Date dateAppel, Date dateReserve, int nbPersonne, Table tableAreserver) {
	Sql sql;
	try {
	    sql = new Sql();
	    Reservation reservation = sql.creationReservation(dateAppel, dateReserve, nbPersonne, tableAreserver);
	    Restaurant.getReservationsJour().add(reservation);
	    return reservation;
	}
	catch (ClassNotFoundException | SQLException | IOException e) {
	    e.printStackTrace();
	}
	return null;
    }

//	public void updateFactureAffectation(Affectation affectation, double nouveauPrix) {
//		Sql sql;
//		try {
//			sql = new Sql();
//			affectation.setFacture(nouveauPrix);
//			sql.
//			Affectation affectation = sql.creationAffectation(dateDebut, nbPersonne, table);
//			Restaurant.getAffectations().add(affectation);
//			return affectation;
//		} catch (ClassNotFoundException | SQLException | IOException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}

    public void modifierEtatTable(Table table, EtatTable etat) {
	Sql sql;
	try {
	    sql = new Sql();
	    sql.modifierEtatTable(table, etat);
	    table.setEtat(etat);
	}
	catch (ClassNotFoundException | SQLException | IOException e) {
	    e.printStackTrace();
	}
    }

    public void affecterTableServeur(Serveur serveur, Table table) {
	Sql sql;
	try {
	    sql = new Sql();
	    sql.affecterTableServeur(serveur, table);
	    if (table.getServeur() != null) {
		table.getServeur().getTablesAffectees().remove(table);
	    }
	    serveur.getTablesAffectees().add(table);
	    table.setServeur(serveur);
	}
	catch (ClassNotFoundException | SQLException | IOException e) {
	    e.printStackTrace();
	}
    }

    public void supprimerReservation(Reservation asuppr) {
	try {
	    Sql sql = new Sql();
	    sql.supprimerReservation(asuppr);
	    int index = Restaurant.getReservationsJour().indexOf(asuppr);
	    Restaurant.getReservationsJour().remove(index);
	}
	catch (ClassNotFoundException | SQLException | IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}


    }

	public Commande creationCommande(Date dateCommande, Plat plat, boolean estEnfant, Affectation affectation) {
		try {
		    Sql sql = new Sql();
		    Commande commande = sql.creationCommande(dateCommande,plat,estEnfant, affectation);
		    Restaurant.getCommandes().add(commande);
		    return commande;
		}
		catch (ClassNotFoundException | SQLException | IOException e) {
		    e.printStackTrace();
		}
		return null;
	}

}