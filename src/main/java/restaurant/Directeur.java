package restaurant;

import java.io.IOException;
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

    public void ajouterTable(int numero, int capacite, Etage etage)
	    throws ClassNotFoundException, SQLException, IOException {
	Sql sql = new Sql();
	sql.insererTable(numero, capacite, etage);
	etage.addTable(new Table(sql.demanderDernierId("tables"), capacite, numero, EtatTable.Libre));
    }

    public void modifierNumeroTable(Table table, int newNumero)
	    throws ClassNotFoundException, SQLException, IOException {
	boolean success;
	Sql sql = new Sql();
	success = sql.updateTable(table.getNumero(), newNumero, table);
	if (success) {
	    table.setNumero(newNumero);
	}
    }

    public void supprimerTable(Table tableToremove, ArrayList<Table> tables)
	    throws ClassNotFoundException, SQLException, IOException {
	boolean success;
	Sql sql = new Sql();
	success = sql.deleteTable(tableToremove);
	if (success) {
	    tables.remove(tableToremove);
	}
    }

    public boolean ajouterIngredient(String nom, ArrayList<Ingredient> ingredients) {
	boolean success = false;
	Sql sql;
	try {
	    sql = new Sql();
	    success = sql.insererIngredient(nom);
	    if (success) {
		ingredients.add(new Ingredient(sql.demanderDernierId("ingredient"), nom, 0));
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

    public Affectation creationAffectation(Timestamp dateDebut, int nbPersonne, Table table) {
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

}