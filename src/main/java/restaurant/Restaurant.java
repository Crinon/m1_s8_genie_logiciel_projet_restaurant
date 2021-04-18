package restaurant;

import java.sql.Date;

public class Restaurant {

	private Date HeureDejeunerOuverture;
	private Date HeureDejeunerLimite;
	private Date HeureDinerOuverture;
	private Date HeureDinerLimite;
	public Restaurant(Date heureDejeunerOuverture, Date heureDejeunerLimite, Date heureDinerOuverture,
			Date heureDinerLimite) {
		super();
		HeureDejeunerOuverture = heureDejeunerOuverture;
		HeureDejeunerLimite = heureDejeunerLimite;
		HeureDinerOuverture = heureDinerOuverture;
		HeureDinerLimite = heureDinerLimite;
	}
	public Date getHeureDejeunerOuverture() {
		return HeureDejeunerOuverture;
	}
	public void setHeureDejeunerOuverture(Date heureDejeunerOuverture) {
		HeureDejeunerOuverture = heureDejeunerOuverture;
	}
	public Date getHeureDejeunerLimite() {
		return HeureDejeunerLimite;
	}
	public void setHeureDejeunerLimite(Date heureDejeunerLimite) {
		HeureDejeunerLimite = heureDejeunerLimite;
	}
	public Date getHeureDinerOuverture() {
		return HeureDinerOuverture;
	}
	public void setHeureDinerOuverture(Date heureDinerOuverture) {
		HeureDinerOuverture = heureDinerOuverture;
	}
	public Date getHeureDinerLimite() {
		return HeureDinerLimite;
	}
	public void setHeureDinerLimite(Date heureDinerLimite) {
		HeureDinerLimite = heureDinerLimite;
	}
	
	
}
