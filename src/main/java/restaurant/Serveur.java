package restaurant;

import java.util.ArrayList;
import java.util.Date;

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

	public Commande creationCommande(Date dateCommande, Plat plat, boolean estEnfant, Affectation affectation) {
		Sql sql = new Sql();
		Commande commande = sql.creationCommande(dateCommande, plat, estEnfant, affectation);
		affectation.getCommandes().add(commande);
		Restaurant.getCommandes().add(commande);
		sql.platDoitEtreIndisponible(commande.getPlat());
		return commande;
	}
	
	
}
