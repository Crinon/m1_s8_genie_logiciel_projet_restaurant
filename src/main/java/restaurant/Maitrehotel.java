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
	
	public void affecterTableServeur(Serveur serveur, Table table) {
		Sql sql;
		sql = new Sql();
		sql.affecterTableServeur(serveur, table);
		if (table.getServeur() != null) {
			table.getServeur().getTablesAffectees().remove(table);
		}
		serveur.getTablesAffectees().add(table);
		table.setServeur(serveur);
	}
	
	public void creerFacture(Affectation affectation) {
		Sql sql = new Sql();
		double prixFacture = affectation.getCommandes().stream().mapToDouble(commande -> commande.getPlat().getPrix())
				.sum();
		sql.creerFacture(affectation, prixFacture);
		affectation.setFacture(prixFacture);
	}
	public void modifierEtatTable(Table table, EtatTable etat) {
		Sql sql;
		sql = new Sql();
		sql.modifierEtatTable(table, etat);
	}
	
	

}
