package restaurant;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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

    public Personne ajouterPersonnel(String nom, String role, ArrayList<Personne> personnel) {
	Personne personne = null;
	// Objet pour intéragir avec la base de données
	try {
	    Sql sql = new Sql();
	    // Utilisation de la requête pour insérer un personnel
	    personne = sql.ajouterPersonne(nom, role);
	    personnel.add(personne);
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

    public void supprimerTable(int indexToRemove, ArrayList<Table> tables)
	    throws ClassNotFoundException, SQLException, IOException {
	boolean success;
	Sql sql = new Sql();
	success = sql.deleteTable(tables.get(indexToRemove));
	if (success) {
	    tables.remove(indexToRemove);
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
	boolean success;
	Sql sql = new Sql();
	success = sql.commanderIngredient(ingredient, ajout);
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

    public Plat supprimerPlat(Plat plat) {
	Sql sql;
	try {
	    sql = new Sql();
	    return plat;
	}
	catch (ClassNotFoundException | SQLException | IOException e) {
	    e.printStackTrace();
	}
	return null;
    }

}