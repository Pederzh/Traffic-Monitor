package DatiTraffico;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class StatoTraffico {
	
	protected int velocita;
	protected Traffico traffico;
	protected int correttezza;
	protected Calendar orario;
	
	protected int MAXLIVELLO = 5;
	protected int MINLIVELLO = 0;
	
	public StatoTraffico() {
		setVelocita(0);
		setTraffico(Traffico.ASSENTE);
		setCorrettezza(MINLIVELLO);
		setOrarioCorrente();
	}
	
	public StatoTraffico(int v, Traffico t, int c, Timestamp o) {
		setVelocita(v);
		setTraffico(t);
		setCorrettezza(c);
		setOrario(o);
	}
	
	
	public void setOrarioCorrente() {
		Timestamp t=new Timestamp (System.currentTimeMillis());
		setOrario(t);
	}
	
	public void setVelocita(int v) {
		this.velocita=v;
	}
	
	public void setTraffico(Traffico t) {
		this.traffico=t;
	}
	
	public void setCorrettezza(int c) {
		if (c<MINLIVELLO) this.correttezza=MINLIVELLO;
		else if (c>MAXLIVELLO) this.correttezza=MAXLIVELLO;
		else this.correttezza=c;
	}
	
	public void setOrario(Timestamp t) {
		this.orario= Calendar.getInstance();
		this.orario.setTime(t);
	}
	
	public void setOrarioCalendar(Calendar t) {
		if (t==null) 
		{
			this.orario=null;
		}
		else
		{
			this.orario= Calendar.getInstance();
			Date d=t.getTime();
			this.orario.setTime(d);
		}
	}
	
	public void aumentaCorrettezza(Mittente m) {
		int correttezza=getCorrettezza();
		if (m==Mittente.APPLICAZIONE) correttezza++;
		if (m==Mittente.CENTRALINASTRADALE) correttezza+=4;
		setCorrettezza(correttezza);
	}
	
	public void diminuisciCorrettezza(Mittente m, Traffico t) {
		int correttezza=getCorrettezza();
		if (m==Mittente.APPLICAZIONE) correttezza--;
		if (m==Mittente.CENTRALINASTRADALE) correttezza-=4;
		if (correttezza<0) {
			correttezza*=-1;
			setTraffico(t);
		}
		setCorrettezza(correttezza);	
	}
	
	public void diminuisciCorrettezza() {
		int correttezza=getCorrettezza();
		if (correttezza>0) 
		{
			correttezza--;
		}
		setCorrettezza(correttezza);	
	}
	
	public int getVelocita() {
		return this.velocita;
	}
	
	public Traffico getTraffico() {
		return this.traffico;
	}
	
	public int getCorrettezza() {
		return this.correttezza;
	}
	
	public Calendar getOrario() {
		return this.orario;
	}
	
    public void kill() {
    	//this.kill();
    }

}
