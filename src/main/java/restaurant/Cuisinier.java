package restaurant;

public class Cuisinier extends Personne {

    public Cuisinier(String string) {
	this.nom = string;
    }

    /**
     * @param id
     * @param nom
     * @param identifiant
     */
    public Cuisinier(int id, String nom, String identifiant) {
	this.id = id;
	this.nom = nom;
	this.identifiant = identifiant;
    }

    // Il choisit parmis les plats pas encore en carte
    public void ajouterCarte(Plat plat) {

    }

    // Il choisit parmis les plats pas encore en carte
    public void retirerCarte(Plat plat) {

    }

    public void preparerPlat(Commande commande) {
	// Select * from commandes where pret is false order by estEnfant IS TRUE AND
	// date desc
    }

    public void definirPlat(double prix, int dureePreparation, boolean disponibleCarte, Type type,
	    Categorie categorie) {
	// Insertion SQL
    }

}
