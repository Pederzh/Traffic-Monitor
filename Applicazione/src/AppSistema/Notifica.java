package AppSistema;

public class Notifica {
	private String posizione;
	private String testo;
	
	
	public Notifica() {
		setPosizione("");
		setTesto("");
	}
	
	public Notifica(String posizione, String testo) {
		setPosizione(posizione);
		setTesto(testo);
		
	}
	
	public void setPosizione(String posizione) {
		this.posizione = posizione;
	}
	
	public void setTesto(String testo) {
		this.testo = testo;
	}
	
	public String toString() {
		return "" + getTesto() + " in " + getPosizione();
	}
	
	public String getPosizione() { return this.posizione; }
	
	public String getTesto() { return this.testo; }
}
