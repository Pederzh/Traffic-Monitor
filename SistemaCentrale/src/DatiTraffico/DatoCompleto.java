package DatiTraffico;

import java.sql.Timestamp;

public class DatoCompleto extends Dato{
	
	private Traffico traffico;
	private Mittente mittente;
	
	
	public DatoCompleto() {
		setTraffico(Traffico.ASSENTE);
		setOrarioCorrente();
		setPosizione(null);
	}
	
	public DatoCompleto(Timestamp o, String p, Traffico s) {
		setTraffico(s);
		setOrario(o);
		setPosizione(p);
		setMittente(null);
	}
	
	public DatoCompleto(Timestamp o, String p, Traffico s, Mittente m) {
		setTraffico(s);
		setOrario(o);
		setPosizione(p);
		setMittente(m);
	}
	
	public DatoCompleto(Timestamp o, String p, String s, int m) {
		setOrario(o);
		setPosizione(p);
		if (m==1) setMittente(Mittente.APPLICAZIONE);
		if (m==4) setMittente(Mittente.CENTRALINASTRADALE);
		if (s=="ELEVATO") setTraffico(Traffico.ELEVATO);
		if (s=="BLOCCATO") setTraffico(Traffico.BLOCCATO);
		if (s=="MEDIO") setTraffico(Traffico.MEDIO);
		if (s=="SCORREVOLE") setTraffico(Traffico.SCORREVOLE);
		if (s=="ASSENTE") setTraffico(Traffico.ASSENTE);
	}
	
	public void setTraffico(Traffico t) {
		this.traffico=t;
	}
	
	public void setMittente(Mittente m) {
		this.mittente=m;
	}
	
	public Mittente getMittente() {
		return this.mittente;
	}
	
	public Traffico getTraffico() {
		return this.traffico;
	}
	
}
