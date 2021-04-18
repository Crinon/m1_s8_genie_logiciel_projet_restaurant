package restaurant;

public class Utilisateur {

	private enum Role {
	    ASSISTANT, CUISINIER, DIRECTEUR, SERVEUR, MAITRE  
	}
	
	private Role role;
	
	private String identifiant;

	public Utilisateur(Role role, String identifiant) {
		super();
		this.role = role;
		this.identifiant = identifiant;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getIdentifiant() {
		return identifiant;
	}

	public void setIdentifiant(String identifiant) {
		this.identifiant = identifiant;
	}
	
	
	
}
