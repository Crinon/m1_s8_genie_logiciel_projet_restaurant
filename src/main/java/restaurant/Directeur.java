package restaurant;

import java.time.LocalTime;
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
	Sql sql = new Sql();
	// Utilisation de la requête pour insérer un personnel
	personne = sql.ajouterPersonne(nom, role);
	Restaurant.getPersonnel().add(personne);
	return personne;
    }

    public Personne modifierPersonnel(Personne personne, String role) {
	// Objet pour intéragir avec la base de données
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
	return personne;
    }

    public void supprimerPersonnel(Personne personne, ArrayList<Personne> personnel) {
	// Objet pour intéragir avec la base de données
	Sql sql = new Sql();
	if (personne.getClass().getName().toLowerCase().contains("serveur")) {
	    ((Serveur) personne).getTablesAffectees().forEach(table -> {
		sql.affecterTableServeur(null, table);
		table.setServeur(null);
	    });
	}
	sql.supprimerPersonne(personne);
	personnel.remove(personne);
    }

    public Etage ajouterEtage() {
	Sql sql = new Sql();
	sql.insererEtage();
	Etage etage = new Etage(sql.demanderDernierId("etage"), sql.demanderDernierEtage());
	Restaurant.addEtage(etage);
	return etage;
    }

    public void supprimerDernierEtage() {
	Sql sql = new Sql();
	sql.supprimerEtage();
	Restaurant.getEtages().remove(Restaurant.getEtages().size() - 1);
    }

    public Table ajouterTable(int numero, int capacite, Etage etage) {
	Sql sql;
	sql = new Sql();
	sql.insererTable(numero, capacite, etage);
	Table nouvelleTable = new Table(sql.demanderDernierId("tables"), numero, capacite, EtatTable.Libre);
	etage.addTable(nouvelleTable);
	return nouvelleTable;
    }

    public void modifierNumeroTable(Table table, int newNumero) {
	boolean success;
	Sql sql;
	sql = new Sql();
	success = sql.updateTable(table, newNumero);
	if (success) {
	    table.setNumero(newNumero);
	}
    }

    public boolean supprimerTable(Table tableToremove, ArrayList<Table> tables) {
    	System.out.println(tableToremove.getEtat());
    	if (!(tableToremove.getEtat() == EtatTable.Libre || tableToremove.getEtat() == EtatTable.Sale)) {
    		System.err.println("Vous avez tenté de supprimer une table qui est occupé.");
    		return false;
    	}
	boolean success;
	Sql sql;
	sql = new Sql();
	success = sql.deleteTable(tableToremove);
	if (success) {
	    tables.remove(tableToremove);
	    return true;
	}
	return false; 
    }

    public Ingredient ajouterIngredient(String nom) {
	Sql sql;
	sql = new Sql();
	if (sql.insererIngredient(nom)) {
	    Ingredient ingredient = new Ingredient(sql.demanderDernierId("ingredient"), nom, 0);
	    Restaurant.getIngredients().add(ingredient);
	    return ingredient;
	}
	else {
	    System.err.println("L'ajout de l'ingrédient " + nom + " a échoué.");
	    return null;
	}
    }

    public void commanderIngredient(Ingredient ingredient, int ajout) {
	Sql sql;
	sql = new Sql();
	sql.commanderIngredient(ingredient, ajout);
    }

    public Plat creerPlat(String nom, Double prixPlat, int tempsPrepa, boolean surCarte, Type type, Categorie categorie,
	    HashMap<Ingredient, Integer> ingredientQuantite) {
	Sql sql;
	sql = new Sql();
	Plat plat = sql.insererPlat(nom, prixPlat, tempsPrepa, surCarte, type, categorie, ingredientQuantite);
	Restaurant.getPlats().add(plat);
	return plat;
    }

    public void modifierDureePlat(Plat plat, int duree) {
	Sql sql;
	sql = new Sql();
	sql.modifierDureePlat(plat, duree);
	plat.setDureePreparation(duree);
    }

    public void modifierCartePlat(Plat plat, boolean estCarte) {
	Sql sql;
	sql = new Sql();
	sql.modifierCartePlat(plat, estCarte);
	plat.setDisponibleCarte(estCarte);
    }

    public void modifierPrixPlat(Plat plat, double prix) {
	Sql sql;
	sql = new Sql();
	sql.modifierPrixPlat(plat, prix);
	plat.setPrix(prix);
    }

    public void supprimerPlat(Plat plat) {
	Sql sql;
	sql = new Sql();
	sql.supprimerPlat(plat);
	Restaurant.getPlats().remove(plat);
    }

    public Affectation creationAffectation(Date dateDebut, int nbPersonne) {
	Sql sql;
	sql = new Sql();
	Table table = Restaurant.getMiniTable(nbPersonne, dateDebut);
	if (table == null) {
	    System.err.println("Aucune table n'est disponible");
	}
	else {
	    Affectation affectation = sql.creationAffectation(dateDebut, nbPersonne, table);
	    Restaurant.getAffectationsJour().add(affectation);
	    return affectation;
	}
	return null;
    }

    public Boolean dateFinAffectation(Affectation affectation, Date dateFin) {
	Sql sql;
	sql = new Sql();
	sql.dateFinAffectation(affectation, dateFin);
	affectation.setDateFin(dateFin);
	return true;
    }

    public void creerFacture(Affectation affectation) {
	Sql sql = new Sql();
	double prixFacture = affectation.getCommandes().stream().mapToDouble(commande -> commande.getPlat().getPrix())
		.sum();
	sql.creerFacture(affectation, prixFacture);
	affectation.setFacture(prixFacture);
    }

    public Reservation creationReservation(Date dateAppel, Date dateReserve, int nbPersonne) {
	Sql sql;
	sql = new Sql();
	Table table = Restaurant.getMiniTable(nbPersonne, dateReserve);
	if (table == null) {
	    System.err.println("Aucune table n'est disponible");
	}
	else {
	    Reservation reservation = sql.creationReservation(dateAppel, dateReserve, nbPersonne, table);
	    Restaurant.getReservationsJour().add(reservation);
	    return reservation;
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

    public void supprimerReservation(Reservation asuppr) {
        Sql sql = new Sql();
        sql.supprimerReservation(asuppr);
        int index = Restaurant.getReservationsJour().indexOf(asuppr);
        Restaurant.getReservationsJour().remove(index);
    }

    public void modifierEtatTable(Table table, EtatTable etat) {
	Sql sql;
	sql = new Sql();
	sql.modifierEtatTable(table, etat);
	table.setEtat(etat);
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

    public Commande creationCommande(Date dateCommande, Plat plat, boolean estEnfant, Affectation affectation) {
	Sql sql = new Sql();
	Commande commande = sql.creationCommande(dateCommande, plat, estEnfant, affectation);
	affectation.getCommandes().add(commande);
	Restaurant.getCommandes().add(commande);
	return commande;
    }

    public void modifierEtatCommande(Commande commande, Etat etat) {
	Sql sql = new Sql();
	sql.modifierEtatCommande(commande, etat);
	commande.setEtat(etat);
    }

    public Double revenuHebdomadaire() {
	return new Sql().revenuHebdomadaire();
    }

    public Double revenuQuotidien() {
	return new Sql().revenuQuotidien();
    }

    public Double revenuMensuel() {
	return new Sql().revenuMensuel();
    }

    public Double tempsPreparationMoyen() {
	return new Sql().tempsPreparationMoyen();
    }

    public Double popularitePlats() {
	return new Sql().popularitePlats();
    }

    public Double profitDejeunerJour() {
	return new Sql().profitDejeunerJour();
    }

    public Double profitDinerJour() {
	return new Sql().profitDinerJour();
    }

	public void annulerReservation(Reservation reservation) {
		Sql sql = new Sql();
		sql.annulerReservation(reservation);
		reservation.setEffetive(false);
	}

	public void modifierHoraire(String string, int horaire) {
		Sql sql = new Sql();
		sql.modifierHoraire(string,horaire);		
	}



}