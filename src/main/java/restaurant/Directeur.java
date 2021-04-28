package restaurant;

import java.sql.SQLException;
import java.util.ArrayList;

public class Directeur extends Personne {

	public Directeur(String string) {
		this.identifiant = string;
	}

	/**
	 * @param id
	 * @param nom
	 * @param identifiant
	 */
	public Directeur(int id, String nom, String identifiant) {
		this.id = id;
		this.nom = nom;
		this.identifiant = identifiant;
	}

	public void ajouterPersonnel(Personne personne, String role) throws ClassNotFoundException, SQLException {
		// Object pour intéragir avec la base de données
		Sql sql = new Sql();
		// Utilisation de la requête pour insérer un serveur
		sql.ajouterPersonne(personne, role);
	}

	public Personne modifierPersonnel(Personne personne, String role) throws ClassNotFoundException, SQLException {
		// Object pour intéragir avec la base de données
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
		personne = null;
		return newPersonne;
	}

	public void supprimerPersonnel(Personne personne) throws ClassNotFoundException, SQLException {
		// Object pour intéragir avec la base de données
		Sql sql = new Sql();
		// Utilisation de la requête pour insérer un serveur
		sql.supprimerPersonne(personne);
	}
		
	public void ajouterEtage() throws ClassNotFoundException, SQLException {
		Sql sql = new Sql();
		sql.insererEtage();
	}
	
	public void supprimerDernierEtage() throws ClassNotFoundException, SQLException {
		Sql sql = new Sql();
		sql.supprimerEtage();
	}
	
	public void ajouterTable(int numero, int capacite, Etage etage) throws ClassNotFoundException, SQLException {
		Sql sql = new Sql();
		sql.insererTable(numero, capacite, etage);
	}
	
	public void modifierNumeroTable(Table table, int newNumero) throws ClassNotFoundException, SQLException {
		boolean success;
		Sql sql = new Sql();
		success = sql.updateTable(table.getNumero(),newNumero, table);
		if(success) {
			table.setNumero(newNumero);
		}
	}
	
	public void supprimerTable(Table table, ArrayList<Table> tables) throws ClassNotFoundException, SQLException {
		boolean success;
		Sql sql = new Sql();
		success = sql.deleteTable(table);
		if(success) {
			tables.remove(table);
		}
	}
	
	public void commanderIngredient(Ingredient ingredient, int ajout) {
		boolean success;
		Sql sql = new Sql();
		success = sql.commanderIngredient(ingredient, ajout);

	}
	
	
	
}