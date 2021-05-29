package restaurant;

public class Assistant extends Personne {

	public Assistant(int id, String nom, String identifiant) {
		super(id, nom, identifiant);
	}

	public void nettoyerTable(Table table) {
		Sql sql;
		sql = new Sql();
		sql.modifierEtatTable(table, EtatTable.Libre);
		table.setEtat(EtatTable.Libre);
	}
}
