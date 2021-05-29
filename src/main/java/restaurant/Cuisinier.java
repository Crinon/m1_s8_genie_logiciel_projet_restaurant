package restaurant;

import java.util.HashMap;

public class Cuisinier extends Personne {

	/**
	 * @param id
	 * @param nom
	 * @param identifiant
	 */
	public Cuisinier(int id, String nom, String identifiant) {
		super(id, nom, identifiant);
	}

	// Menu principal du cuisinier

	// Il choisit parmis les plats pas encore en carte
	public void ajouterCarte(Plat plat) {

	}

	// Il choisit parmis les plats pas encore en carte
	public void retirerCarte(Plat plat) {

	}

	public Plat creerPlat(String nom, Double prixPlat, int tempsPrepa, boolean surCarte, Type type, Categorie categorie,
			HashMap<Ingredient, Integer> ingredientQuantite) {
		Sql sql;
		sql = new Sql();
		Plat plat = sql.insererPlat(nom, prixPlat, tempsPrepa, surCarte, type, categorie, ingredientQuantite);
		Restaurant.getPlats().add(plat);
		return plat;
	}
	
	public void modifierEtatCommande(Commande commande, Etat etat) {
		Sql sql = new Sql();
		sql.modifierEtatCommande(commande, etat);
		commande.setEtat(etat);
	}
	
	

}
