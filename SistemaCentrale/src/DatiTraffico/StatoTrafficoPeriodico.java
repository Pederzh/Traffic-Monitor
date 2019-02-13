package DatiTraffico;

import java.util.Date;
import java.sql.Timestamp;
import java.util.Calendar;

public class StatoTrafficoPeriodico extends StatoTraffico{
	
	private Calendar primoOrario;
	
	public StatoTrafficoPeriodico() {
		setVelocita(0);
		setTraffico(Traffico.ASSENTE);
		setCorrettezza(0);
		setOrarioCalendar(null);
		setPrimoOrarioCalendar(null);
	}
	
	public StatoTrafficoPeriodico(int v, Traffico t, int c, Timestamp o) {
		setVelocita(v);
		setTraffico(t);
		setCorrettezza(c);
		setOrario(o);
		setPrimoOrario(o);
		modulazioneOra();
	}
	
	public void setStatoTrafficoPeriodico(StatoTraffico s) {
		setVelocita(s.getVelocita());
		setTraffico(s.getTraffico());
		setCorrettezza(s.getCorrettezza());
		Calendar t=s.getOrario();
		setOrarioCalendar(t);
		setPrimoOrarioCalendar(t);
		modulazioneOra();
	}
	
	public void setDato(DatoCompleto d) {
		setVelocita(0);
		setTraffico(d.getTraffico());
		setCorrettezza(0);
		setOrario(d.getOrario());
		setPrimoOrario(d.getOrario());
		modulazioneOra();
	}
	
	public void setPrimoOrario(Timestamp t) {
		this.primoOrario= Calendar.getInstance();
		this.primoOrario.setTime(t);
	}
	
	public void setPrimoOrarioCalendar(Calendar t) {
		if (t==null) 
		{
			this.primoOrario=null;
		}
		else
		{
			this.primoOrario= Calendar.getInstance();
			Date d=t.getTime();
			this.primoOrario.setTime(d);
		}
	}
	
	public Calendar getPrimoOrario() {
		return this.primoOrario;
	}
	
	public void modulazioneOra() {
		Calendar calendar=Calendar.getInstance();
		calendar=getOrario();
		if (calendar.get(Calendar.MINUTE)%5!=0) {
			calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE)-calendar.get(Calendar.MINUTE)%5);
			setPrimoOrarioCalendar(calendar);
			;
		}
	}
	
}
