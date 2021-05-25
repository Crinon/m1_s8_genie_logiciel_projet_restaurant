package restaurant;

import java.io.IOException;
import java.sql.SQLException;

public class Assistant extends Personne {

    public Assistant(int id, String nom, String identifiant) {
	super(id, nom, identifiant);
    }
    

    public void nettoyerTable(Table table) {
	Sql sql;
	try {
	    sql = new Sql();
	    sql.modifierEtatTable(table, EtatTable.Libre);
	    table.setEtat(EtatTable.Libre);
	}
	catch (ClassNotFoundException | SQLException | IOException e) {
	    e.printStackTrace();
	}
    }
}
