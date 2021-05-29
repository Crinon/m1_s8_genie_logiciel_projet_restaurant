package restaurant;

public class Assistant extends Personne {

	public Assistant(int id, String nom, String identifiant) {
		super(id, nom, identifiant);
	}
	
	public void modifierEtatTable(Table table, EtatTable etat) {
		Sql sql;
		sql = new Sql();
		sql.modifierEtatTable(table, etat);
	}
}
