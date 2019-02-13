package AppUtente;

public class Utente {
	
	private String username;
	private String password;
	
	public Utente() {
		setUsername("");
		setPassword("");
	}
	
	public Utente(String username, String password) {
		setUsername(username);
		setPassword(password);
	}
	
	public void setUsername(String u) {
		this.username = u;
	}
	
	public void setPassword(String p) {
		//ai fini del progetto non serve cifrare la password
		this.password = p;
	}
	
	public String getUsername() { return this.username; }
	
	public String getPassword() { return this.password; }
	
}
